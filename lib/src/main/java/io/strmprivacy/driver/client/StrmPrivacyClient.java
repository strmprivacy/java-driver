package io.strmprivacy.driver.client;

import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.schemas.StrmPrivacyEvent;
import org.eclipse.jetty.client.api.ContentResponse;

import java.util.concurrent.CompletableFuture;


/**
 * Talks to the STRM Privacy, both to the gateway and the egress.
 */
public class StrmPrivacyClient {
    private final AuthService authService;
    private final SenderService senderService;

//    private static final Logger log = LoggerFactory.getLogger(StrmPrivacyClient.class);

    public StrmPrivacyClient(String clientId,
                             String clientSecret,
                             Config config) {
        this.authService = new AuthService(clientId, clientSecret, config);
        this.senderService = new SenderService(authService, config);
    }

    private StrmPrivacyClient(Builder builder) {
        this(builder.clientId, builder.clientSecret, builder.config);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated()
    public CompletableFuture<ContentResponse> send(StrmPrivacyEvent event,
                                                   @SuppressWarnings("unused") Object serializationType) {
        return send(event);
    }
    /**
     * send an StrmPrivacyEvent.
     *
     * @param event the event.
     * @return future containing the content response
     */
    public CompletableFuture<ContentResponse> send(StrmPrivacyEvent event) {
        return senderService.send(event);
    }

    /**
     * Disconnects the client and frees up any resources
     */
    public void stop() {
        senderService.stop();
        authService.stop();
    }

    public static final class Builder {
        private String clientId;
        private String clientSecret;
        private Config config;

        private Builder() {
        }

        public StrmPrivacyClient build() {
            return new StrmPrivacyClient(this);
        }

        public Builder clientId(String val) {
            clientId = val;
            return this;
        }

        public Builder clientSecret(String val) {
            clientSecret = val;
            return this;
        }

        public Builder config(Config val) {
            config = val;
            return this;
        }
    }
}
