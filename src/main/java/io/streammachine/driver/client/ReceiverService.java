package io.streammachine.driver.client;

import io.streammachine.driver.domain.Config;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

class ReceiverService {
    private final URI isAliveUri;
    private final URI defaultSseEndpointUri;
    private final Client sseClient;
    private final HttpClient client;
    private final AuthService authService;

    public ReceiverService(String billingId, String clientId, String clientSecret, Config config) throws URISyntaxException, InterruptedException {
        this.isAliveUri = new URI(String.format("%s://%s%s", config.getEgressProtocol(), config.getEgressHost(), config.getEgressHealthEndpoint()));
        this.defaultSseEndpointUri = new URI(String.format("%s://%s%s", config.getEgressProtocol(), config.getEgressHost(), config.getEgressEndpoint()));
        this.sseClient = ClientBuilder.newBuilder().register(new AddAuthHeader()).build();
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
    }

    public void start(boolean asJson, Consumer<InboundSseEvent> consumer) {
        var uri = asJson ? UriBuilder.fromUri(this.defaultSseEndpointUri).queryParam("asJson", true).build() : this.defaultSseEndpointUri;

        SseEventSource eventSource = SseEventSource.target(sseClient.target(uri))
                .reconnectingEvery(60, TimeUnit.SECONDS)
                .build();

        eventSource.register(consumer);
        eventSource.open();
    }

    public HttpResponse<String> isAlive() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(isAliveUri)
                .header(AUTHORIZATION, getBearerHeaderValue())
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String getBearerHeaderValue() {
        return String.format("Bearer %s", authService.getAccessToken());
    }

    private class AddAuthHeader implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) {
            String bearerToken = String.format("Bearer %s", authService.getAccessToken());
            requestContext.getHeaders().add(AUTHORIZATION, bearerToken);
        }
    }
}
