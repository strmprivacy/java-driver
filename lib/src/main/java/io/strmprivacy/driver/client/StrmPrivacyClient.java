package io.strmprivacy.driver.client;

import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.serializer.SerializationType;
import io.strmprivacy.schemas.StrmPrivacyEvent;
import org.eclipse.jetty.client.api.ContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;


/**
 * Talks to the STRM Privacy, both to the gateway and the egress.
 */
public class StrmPrivacyClient {
    private AuthService authService;
    private SenderService senderService;

    private static final Logger log = LoggerFactory.getLogger(StrmPrivacyClient.class);
    private String billingId;
    private String clientId;
    private String clientSecret;
    private Config config;

    public StrmPrivacyClient(String billingId,
                             String clientId,
                             String clientSecret,
                             Config config) {
        this.billingId = billingId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.config = config;
        this.authService = new AuthService(billingId, clientId, clientSecret, config);
        this.senderService = new SenderService(authService, config);
    }

    private StrmPrivacyClient(Builder builder) {
        this(builder.billingId, builder.clientId, builder.clientSecret, builder.config);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * send an StrmPrivacyEvent.
     *
     * @param event the event.
     * @param type  the serialization type. only for Avro
     * @return future containing the content response
     */
    public CompletableFuture<ContentResponse> send(StrmPrivacyEvent event, SerializationType type) {
        return senderService.send(event, type);
    }

    /**
     * Disconnects the client and frees up any resources
     */
    public void stop() {
        senderService.stop();
        authService.stop();
    }

    public static final class Builder {
        private String billingId;
        private String clientId;
        private String clientSecret;
        private Config config;

        private Builder() {
        }

        public StrmPrivacyClient build() {
            return new StrmPrivacyClient(this);
        }

        public Builder billingId(String val) {
            billingId = val;
            return this;
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
