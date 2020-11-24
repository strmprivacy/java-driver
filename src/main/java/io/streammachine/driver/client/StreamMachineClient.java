package io.streammachine.driver.client;

import io.streammachine.driver.domain.Config;
import io.streammachine.driver.domain.StreamMachineEvent;
import io.streammachine.driver.serializer.SerializationType;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.sse.InboundSseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
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
    public StreamMachineClient(String billingId, String clientId, String clientSecret, Config config) throws URISyntaxException, InterruptedException {
        this.senderService = new SenderService(billingId, clientId, clientSecret, config);
        this.receiverService = new ReceiverService(billingId, clientId, clientSecret, config);
    }

    /**
     * send a StreamMachineEvent.
     * @param event the event.
     * @param type the serialization type. only for Avro
     * @return a future http response
     */
    public CompletableFuture<HttpResponse<String>> send(StreamMachineEvent event, SerializationType type) {
        return senderService.send(event, type);
    }

    /**
     * Start an endless loop that receives events and applies `consumer`  to them.
     * @param asJson tells the server to convert the event data to json. Otherwise you get base64 encoded data
     *               that might be avro binary, avro json or json depending on the schema and the serialization type.
     *
     *               This will be handled in a future version of the Java driver.
     */
    public void startReceivingSse(boolean asJson, Consumer<InboundSseEvent> consumer) {
        receiverService.start(asJson, consumer);
    }

    /**
     * queryies the egress /is-alive endpoint, which should return ok
     * @return ok
     * @throws IOException if an I/O error occurs during the request
     * @throws InterruptedException if the request is interrupted
     */
    public HttpResponse<String> egressIsAlive() throws IOException, InterruptedException {
        return receiverService.isAlive();
    }
}
