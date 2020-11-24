package io.streammachine.driver.client;

import io.streammachine.driver.domain.Config;
import io.streammachine.driver.domain.StreamMachineEvent;
import io.streammachine.driver.domain.StreamMachineEventDTO;
import io.streammachine.driver.serializer.SerializationType;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

class SenderService {
    private final URI endpointUri;
    private final HttpClient client;
    private final AuthService authService;
    private final Config config;

    public SenderService(String billingId, String clientId, String clientSecret, Config config) throws URISyntaxException, InterruptedException {
        this.endpointUri = new URI(String.format("%s://%s%s", config.getGatewayProtocol(), config.getGatewayHost(), config.getGatewayEndpoint()));
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.authService = AuthService.builder()
                .purpose(this.getClass().getSimpleName())
                .billingId(billingId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .config(config)
                .build();

        this.config = config;
    }

    public CompletableFuture<HttpResponse<String>> send(StreamMachineEvent event, SerializationType type) {
        StreamMachineEventDTO dto = new StreamMachineEventDTO(event, type);

        HttpRequest request = HttpRequest.newBuilder().uri(endpointUri)
                .header("Authorization", getBearerHeaderValue())
                .header("Content-Type", "application/octet-stream")
                .header("Strm-Serialization-Type", dto.getSerializationTypeHeader())
                // TODO retrieve version number from Maven / Gitlab CI build
                .header("Strm-Driver-Version", config.getImplementationVersion())
                .header("Strm-Driver-Build", "GET FROM VERSION FILE-Add with Maven")
                .header("Strm-Schema-Id", dto.getSchemaId())
                .POST(HttpRequest.BodyPublishers.ofByteArray(dto.serialize()))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private String getBearerHeaderValue() {
        return String.format("Bearer %s", authService.getAccessToken());
    }
}
