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
    private final AuthService authService;
    private final SenderService senderService;

    private static final Logger log = LoggerFactory.getLogger(StrmPrivacyClient.class);

    public StrmPrivacyClient(String billingId, String clientId, String clientSecret, Config config) {
        this.authService = new AuthService(billingId, clientId, clientSecret, config);
        this.senderService = new SenderService(authService, config);
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
}
