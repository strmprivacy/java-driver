package io.streammachine.driver.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.streammachine.driver.domain.Config;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@Slf4j
class AuthService {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final String purpose;
    private final String billingId;
    private final String clientId;
    private final String clientSecret;
    private final Config config;

    private final Timer timer;
    private final CountDownLatch latch;
    private AuthProvider authProvider;

    @Builder
    public AuthService(String purpose, String billingId, String clientId, String clientSecret, Config config) throws InterruptedException {
        this.purpose = purpose;
        this.billingId = billingId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.config = config;

        this.timer = new Timer();
        this.latch = new CountDownLatch(1);
        timer.schedule(new AuthProviderInitializerTask(), 0, Duration.ofMinutes(5).toMillis());
        latch.await();
    }

    public String getAccessToken() {
        return authProvider.getIdToken();
    }

    private void authenticate(String billingId, String clientId, String clientSecret) {
        try {
            URI uri = new URI(String.format("%s://%s%s", config.getStsProtocol(), config.getStsHost(), config.getStsAuthEndpoint()));

            ObjectNode payload = MAPPER.createObjectNode()
                    .put("billingId", billingId)
                    .put("clientId", clientId)
                    .put("clientSecret", clientSecret);

            doPost(uri, payload);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("An error occurred while requesting an access token with clientId '{}' and billingId '{}'", clientId, billingId, e);
        }
    }

    private void refresh(String refreshToken, String billingId, String clientId, String clientSecret) {
        try {
            URI uri = new URI(String.format("%s://%s%s", config.getStsProtocol(), config.getStsHost(), config.getStsRefreshEndpoint()));

            ObjectNode payload = MAPPER.createObjectNode();
            payload.put("refreshToken", refreshToken);

            doPost(uri, payload);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.debug("Failed to refresh token with clientId '{}' and billingId '{}'", clientId, billingId);
            log.debug("Trying to request a new token with clientId '{}' and billingId '{}'", clientId, billingId);

            authenticate(billingId, clientId, clientSecret);
        }
    }

    private void doPost(URI uri, ObjectNode payload) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("content-type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(payload)))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        this.authProvider = MAPPER.readValue(response.body(), AuthProvider.class);
    }

    private class AuthProviderInitializerTask extends TimerTask {
        private final long expirationSlackTimeSeconds = Duration.ofMinutes(10).toSeconds();

        public void run() {
            if (authProvider == null) {
                log.debug("Initializing a new Auth Provider for {}", purpose);
                authenticate(billingId, clientId, clientSecret);
                latch.countDown();
            } else if (isAlmostExpired(authProvider.getExpiresAt())) {
                log.debug("Refreshing an existing Auth Provider {}", purpose);
                refresh(authProvider.getRefreshToken(), billingId, clientId, clientSecret);
            }
        }

        private boolean isAlmostExpired(long expirationTime) {
            long currentTime = Instant.now().getEpochSecond();

            return (currentTime + expirationSlackTimeSeconds) >= expirationTime;
        }
    }
}
