package io.strmprivacy.driver.client;

import io.strmprivacy.driver.common.WebSocketConsumer;
import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.serializer.SerializationType;
import io.strmprivacy.schemas.StrmPrivacyEvent;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.api.ContentResponse;

import java.util.concurrent.CompletableFuture;


/**
 * Talks to the STRM Privacy, both to the gateway and the egress.
 */
@Slf4j
public class StrmPrivacyClient {
    private final AuthService authService;
    private final SenderService senderService;
    private final ReceiverService receiverService;

    @Builder
    public StrmPrivacyClient(String billingId, String clientId, String clientSecret, Config config) {
        this.authService = new AuthService(billingId, clientId, clientSecret, config);
        this.senderService = new SenderService(authService, config);
        this.receiverService = new ReceiverService(authService, config);
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
     * Start an endless loop that receives events through a websocket and applies `consumer`  to them.
     *
     * @param asJson   tells the server to convert the event data to json. Otherwise you get base64 encoded data
     *                 that might be avro binary, avro json or json depending on the schema and the serialization type.
     *                 <p>
     *                 This will be handled in a future version of the Java driver.
     * @param consumer The consumer to apply to the ws events
     */
    public void startReceivingWs(boolean asJson, WebSocketConsumer consumer) {
        receiverService.receiveWs(asJson, consumer);
    }

    /**
     * queries the egress /is-alive endpoint, which should return ok
     *
     * @return ok
     */
    public ContentResponse egressIsAlive() {
        return receiverService.isAlive();
    }

    /**
     * Disconnects the client and frees up any resources
     */
    public void stop() {
        senderService.stop();
        receiverService.stop();
        authService.stop();
    }
}
