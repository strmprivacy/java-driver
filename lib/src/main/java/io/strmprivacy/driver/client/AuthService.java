package io.strmprivacy.driver.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.domain.StrmPrivacyException;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


class AuthService {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final String clientId;
    private final String clientSecret;

    private final HttpClient httpClient;

    private final CountDownLatch latch;
    private final Timer timer;
    private AuthProvider authProvider;

    private final String authUri;
    private final String refreshUri;

    public AuthService(String clientId, String clientSecret, Config config) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.httpClient = new HttpClient(new SslContextFactory.Client());
        try {
            this.httpClient.start();
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred while starting a new AuthService for STRM Privacy.", e);
        }

        try {
            this.authUri = new URI(String.format("%s://%s/%s", config.getKeycloakScheme(), config.getKeycloakHost(), config.getKeycloakEndpoint())).toString();
            this.refreshUri = new URI(String.format("%s://%s/%s", config.getKeycloakScheme(), config.getKeycloakHost(), config.getKeycloakEndpoint())).toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Malformed URI(s) for " + this.getClass().getCanonicalName(), e);
        }

        this.timer = new Timer();
        this.latch = new CountDownLatch(1);
        this.timer.schedule(new AuthProviderInitializerTask(), 0, Duration.ofMinutes(5).toMillis());

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Error while setting up authentication for STRM Privacy", e);
        }
    }

    public String getAccessToken() {
        return authProvider.getAccess_token();
    }

    public void stop() {
        try {
            this.timer.cancel();
            this.httpClient.stop();
        } catch (Exception e) {
            throw new StrmPrivacyException("Error stopping AuthService HttpClient", e);
        }
    }

    private void authenticate(String clientId, String clientSecret) {
        try {
            String payload = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", clientId, clientSecret);
            doPost(authUri, payload);
        } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
            log.error("An error occurred while requesting an access token with clientId '{}'", clientId, e);
        }
    }

    private void refresh(String refreshToken, String clientId, String clientSecret) {
        try {
            String payload = String.format("grant_type=refresh_token&refresh_token=%s", refreshToken);

            doPost(refreshUri, payload);
        } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
            log.debug("Failed to refresh token with clientId '{}'", clientId);
            log.debug("Trying to request a new token with clientId '{}'", clientId);

            authenticate(clientId, clientSecret);
        }
    }

    private void doPost(String uri, String payload) throws IOException, InterruptedException, TimeoutException, ExecutionException {
        ContentResponse response = httpClient.POST(uri)
                .content(new StringContentProvider(payload))
                .header(HttpHeader.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .send();

        this.authProvider = MAPPER.readValue(response.getContentAsString(), AuthProvider.class);
    }

    private class AuthProviderInitializerTask extends TimerTask {
        private final long expirationSlackTimeSeconds = Duration.ofMinutes(10).getSeconds();

        public void run() {
            if (authProvider == null) {
                log.debug("Initializing a new Auth Provider");
                authenticate(clientId, clientSecret);
                latch.countDown();
            } else if (isAlmostExpired(authProvider.getExpiresAt())) {
                log.debug("Refreshing an existing Auth Provider");
                refresh(authProvider.getRefreshToken(), clientId, clientSecret);
            }
        }

        private boolean isAlmostExpired(long expirationTime) {
            long currentTime = Instant.now().getEpochSecond();

            return (currentTime + expirationSlackTimeSeconds) >= expirationTime;
        }
    }
}
