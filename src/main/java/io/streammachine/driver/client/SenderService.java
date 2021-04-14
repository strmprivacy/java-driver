package io.streammachine.driver.client;

import io.streammachine.driver.common.CompletableFutureResponseListener;
import io.streammachine.driver.domain.Config;
import io.streammachine.driver.domain.StreamMachineEventDTO;
import io.streammachine.driver.serializer.SerializationType;
import io.streammachine.schemas.StreamMachineEvent;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.util.concurrent.CompletableFuture;

class SenderService {
    private final String endpointUri;
    private final HttpClient httpClient;
    private final AuthService authService;
    private final Config config;

    public SenderService(String billingId, String clientId, String clientSecret, Config config) {
        this.endpointUri = String.format("%s://%s:%s%s", config.getGatewayScheme(), config.getGatewayHost(), config.getGatewayPort(), config.getGatewayEndpoint());
        this.authService = AuthService.builder()
                .purpose(this.getClass().getSimpleName())
                .billingId(billingId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .config(config)
                .build();
        this.config = config;

        HTTP2Client http2Client = new HTTP2Client();
        SslContextFactory sslContextFactory = new SslContextFactory.Client();
        http2Client.addBean(sslContextFactory);
        httpClient = new HttpClient(new HttpClientTransportOverHTTP2(http2Client), sslContextFactory);
        httpClient.setMaxRequestsQueuedPerDestination(65536);

        try {
            http2Client.start();
            httpClient.start();
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred while starting a new Sender for Stream Machine.", e);
        }
    }

    public CompletableFuture<ContentResponse> send(StreamMachineEvent event, SerializationType type) {
        StreamMachineEventDTO dto = new StreamMachineEventDTO(event, type);
        CompletableFuture<ContentResponse> completableFuture = new CompletableFuture<>();

        httpClient.POST(this.endpointUri)
                .header(HttpHeader.AUTHORIZATION, getBearerHeaderValue())
                .header(HttpHeader.CONTENT_TYPE, "application/octet-stream")
                .header("Strm-Driver-Version", config.getImplementationVersion())
                .header("Strm-Serialization-Type", dto.getSerializationTypeHeader())
                .header("Strm-Schema-Id", dto.getSchemaId())
                .content(new BytesContentProvider(dto.serialize()))
                .send(new CompletableFutureResponseListener(completableFuture));

        return completableFuture;
    }

    private String getBearerHeaderValue() {
        return String.format("Bearer %s", authService.getAccessToken());
    }
}
