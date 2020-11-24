package io.streammachine.driver.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.streammachine.driver.domain.Config;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.asynchttpclient.Dsl.asyncHttpClient;

@Slf4j
class AuthService {
    private static final AsyncHttpClient HTTP_CLIENT = asyncHttpClient();
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

    private final String authUri;
    private final String refreshUri;

    @Builder
    public AuthService(String purpose, String billingId, String clientId, String clientSecret, Config config) {
        this.purpose = purpose;
        this.billingId = billingId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.config = config;

        try {
            this.authUri = new URI(String.format("%s://%s%s", config.getStsScheme(), config.getStsHost(), config.getStsAuthEndpoint())).toString();
            this.refreshUri = new URI(String.format("%s://%s%s", config.getStsScheme(), config.getStsHost(), config.getStsRefreshEndpoint())).toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Malformed URI(s) for " + this.getClass().getCanonicalName(), e);
        }

        this.timer = new Timer();
        this.latch = new CountDownLatch(1);
        timer.schedule(new AuthProviderInitializerTask(), 0, Duration.ofMinutes(5).toMillis());

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Error while setting up authentication for Stream Machine", e);
        }
    }

    public String getAccessToken() {
        return authProvider.getIdToken();
    }

    private void authenticate(String billingId, String clientId, String clientSecret) {
        try {
            ObjectNode payload = MAPPER.createObjectNode()
                    .put("billingId", billingId)
                    .put("clientId", clientId)
                    .put("clientSecret", clientSecret);

            doPost(authUri, payload);
        } catch (IOException | InterruptedException e) {
            log.error("An error occurred while requesting an access token with clientId '{}' and billingId '{}'", clientId, billingId, e);
        }
    }

    private void refresh(String refreshToken, String billingId, String clientId, String clientSecret) {
        try {
            ObjectNode payload = MAPPER.createObjectNode();
            payload.put("refreshToken", refreshToken);

            doPost(refreshUri, payload);
        } catch (IOException | InterruptedException e) {
            log.debug("Failed to refresh token with clientId '{}' and billingId '{}'", clientId, billingId);
            log.debug("Trying to request a new token with clientId '{}' and billingId '{}'", clientId, billingId);

            authenticate(billingId, clientId, clientSecret);
        }
    }

    private void doPost(String uri, ObjectNode payload) throws IOException, InterruptedException {
        Response response = HTTP_CLIENT.preparePost(uri)
                .setBody(MAPPER.writeValueAsString(payload))
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .execute()
                .toCompletableFuture()
                .join();

        this.authProvider = MAPPER.readValue(response.getResponseBody(), AuthProvider.class);
    }

    private class AuthProviderInitializerTask extends TimerTask {
        private final long expirationSlackTimeSeconds = Duration.ofMinutes(10).getSeconds();

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
