package io.streammachine.driver.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.streammachine.driver.domain.Config;
import io.streammachine.driver.domain.StreamMachineException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Slf4j
class AuthService {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final String billingId;
    private final String clientId;
    private final String clientSecret;

    private final HttpClient httpClient;

    private final CountDownLatch latch;
    private final Timer timer;
    private AuthProvider authProvider;

    private final String authUri;
    private final String refreshUri;

    @Builder
    public AuthService(String billingId, String clientId, String clientSecret, Config config) {
        this.billingId = billingId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.httpClient = new HttpClient(new SslContextFactory.Client());
        try {
            this.httpClient.start();
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred while starting a new AuthService for Stream Machine.", e);
        }

        try {
            this.authUri = new URI(String.format("%s://%s%s", config.getStsScheme(), config.getStsHost(), config.getStsAuthEndpoint())).toString();
            this.refreshUri = new URI(String.format("%s://%s%s", config.getStsScheme(), config.getStsHost(), config.getStsRefreshEndpoint())).toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Malformed URI(s) for " + this.getClass().getCanonicalName(), e);
        }

        this.timer = new Timer();
        this.latch = new CountDownLatch(1);
        this.timer.schedule(new AuthProviderInitializerTask(), 0, Duration.ofMinutes(5).toMillis());

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Error while setting up authentication for Stream Machine", e);
        }
    }

    public String getAccessToken() {
        return authProvider.getIdToken();
    }

    public void stop() {
        try {
            this.timer.cancel();
            this.httpClient.stop();
        } catch (Exception e) {
            throw new StreamMachineException("Error stopping AuthService HttpClient", e);
        }
    }

    private void authenticate(String billingId, String clientId, String clientSecret) {
        try {
            ObjectNode payload = MAPPER.createObjectNode()
                    .put("billingId", billingId)
                    .put("clientId", clientId)
                    .put("clientSecret", clientSecret);

            doPost(authUri, payload);
        } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
            log.error("An error occurred while requesting an access token with clientId '{}' and billingId '{}'", clientId, billingId, e);
        }
    }

    private void refresh(String refreshToken, String billingId, String clientId, String clientSecret) {
        try {
            ObjectNode payload = MAPPER.createObjectNode();
            payload.put("refreshToken", refreshToken);

            doPost(refreshUri, payload);
        } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
            log.debug("Failed to refresh token with clientId '{}' and billingId '{}'", clientId, billingId);
            log.debug("Trying to request a new token with clientId '{}' and billingId '{}'", clientId, billingId);

            authenticate(billingId, clientId, clientSecret);
        }
    }

    private void doPost(String uri, ObjectNode payload) throws IOException, InterruptedException, TimeoutException, ExecutionException {
        ContentResponse response = httpClient.POST(uri)
                                             .content(new StringContentProvider(MAPPER.writeValueAsString(payload)))
                                             .header(HttpHeader.CONTENT_TYPE, "application/json; charset=UTF-8")
                                             .send();

        this.authProvider = MAPPER.readValue(response.getContentAsString(), AuthProvider.class);
    }

    private class AuthProviderInitializerTask extends TimerTask {
        private final long expirationSlackTimeSeconds = Duration.ofMinutes(10).getSeconds();

        public void run() {
            if (authProvider == null) {
                log.debug("Initializing a new Auth Provider");
                authenticate(billingId, clientId, clientSecret);
                latch.countDown();
            } else if (isAlmostExpired(authProvider.getExpiresAt())) {
                log.debug("Refreshing an existing Auth Provider");
                refresh(authProvider.getRefreshToken(), billingId, clientId, clientSecret);
            }
        }

        private boolean isAlmostExpired(long expirationTime) {
            long currentTime = Instant.now().getEpochSecond();

            return (currentTime + expirationSlackTimeSeconds) >= expirationTime;
        }
    }
}
