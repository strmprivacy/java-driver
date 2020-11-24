package io.streammachine.driver.client;

import io.streammachine.driver.domain.Config;
import io.streammachine.driver.domain.StreamMachineEvent;
import io.streammachine.driver.serializer.SerializationType;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
import org.asynchttpclient.ws.WebSocketListener;

import javax.ws.rs.sse.InboundSseEvent;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


/**
 * Talks to the Stream machine, both to the gateway and the egress.
 */
@Slf4j
public class StreamMachineClient {
    private final SenderService senderService;
    private final ReceiverService receiverService;

    @Builder
    public StreamMachineClient(String billingId, String clientId, String clientSecret, Config config) {
        this.senderService = new SenderService(billingId, clientId, clientSecret, config);
        this.receiverService = new ReceiverService(billingId, clientId, clientSecret, config);
    }

    /**
     * send a StreamMachineEvent.
     *
     * @param event the event.
     * @param type  the serialization type. only for Avro
     * @return a future http response
     */
    public CompletableFuture<Response> send(StreamMachineEvent event, SerializationType type) {
        return senderService.send(event, type);
    }

    /**
     * Start an endless loop that receives events and applies `consumer` to them.
     * Deprecated in favor of websocket
     *
     * @param asJson   tells the server to convert the event data to json. Otherwise you get base64 encoded data
     *                 that might be avro binary, avro json or json depending on the schema and the serialization type.
     *                 <p>
     *                 This will be handled in a future version of the Java driver.
     * @param consumer The consumer to apply to the sse
     */
    @Deprecated
    public void startReceivingSse(boolean asJson, Consumer<InboundSseEvent> consumer) {
        receiverService.receiveSse(asJson, consumer);
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
    public void startReceivingWs(boolean asJson, WebSocketListener consumer) {
        receiverService.receiveWs(asJson, consumer);
    }

    /**
     * queries the egress /is-alive endpoint, which should return ok
     *
     * @return ok
     */
    public Response egressIsAlive() {
        return receiverService.isAlive();
    }
}
