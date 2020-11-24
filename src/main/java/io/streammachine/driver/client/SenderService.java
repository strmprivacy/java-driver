package io.streammachine.driver.client;

import io.streammachine.driver.domain.Config;
import io.streammachine.driver.domain.StreamMachineEvent;
import io.streammachine.driver.domain.StreamMachineEventDTO;
import io.streammachine.driver.serializer.SerializationType;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletableFuture;

import static org.asynchttpclient.Dsl.asyncHttpClient;

class SenderService {
    private final String endpointUri;
    private final AsyncHttpClient client;
    private final AuthService authService;
    private final Config config;

    public SenderService(String billingId, String clientId, String clientSecret, Config config) {
        this.endpointUri = String.format("%s://%s%s", config.getGatewayScheme(), config.getGatewayHost(), config.getGatewayEndpoint());
        this.client = asyncHttpClient();
        this.authService = AuthService.builder()
                .purpose(this.getClass().getSimpleName())
                .billingId(billingId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .config(config)
                .build();

        this.config = config;
    }

    public CompletableFuture<Response> send(StreamMachineEvent event, SerializationType type) {
        StreamMachineEventDTO dto = new StreamMachineEventDTO(event, type);

        return client.preparePost(endpointUri)
                .setBody(dto.serialize())
                .addHeader("Authorization", getBearerHeaderValue())
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Strm-Serialization-Type", dto.getSerializationTypeHeader())
// TODO retrieve version number from Maven / Gitlab CI build
                .addHeader("Strm-Driver-Version", config.getImplementationVersion())
                .addHeader("Strm-Driver-Build", "GET FROM VERSION FILE-Add with Maven")
                .addHeader("Strm-Schema-Id", dto.getSchemaId())
                .execute()
                .toCompletableFuture();
    }

    private String getBearerHeaderValue() {
        return String.format("Bearer %s", authService.getAccessToken());
    }
}
