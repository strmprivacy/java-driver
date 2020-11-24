package io.streammachine.driver.client;

import io.streammachine.driver.domain.Config;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.asynchttpclient.Dsl.asyncHttpClient;

class ReceiverService {
    private String isAliveUri;
    private URI defaultSseEndpointUri;
    private URI defaultWsEndpointUri;
    private final Client sseClient;
    private final AsyncHttpClient client;
    private final AuthService authService;

    public ReceiverService(String billingId, String clientId, String clientSecret, Config config) {
        try {
            this.isAliveUri = String.format("%s://%s%s",
                    config.getEgressScheme(),
                    config.getEgressHost(),
                    config.getEgressHealthEndpoint()
            );

            this.defaultSseEndpointUri = new URI(
                    String.format("%s://%s%s",
                            config.getEgressScheme(),
                            config.getEgressHost(),
                            config.getEgressSseEndpoint()
                    )
            );

            this.defaultWsEndpointUri = new URI(
                    String.format("%s://%s%s",
                            config.getEgressWsScheme(),
                            config.getEgressHost(),
                            config.getEgressWsEndpoint()
                    )
            );
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Malformed URI(s) for " + this.getClass().getCanonicalName(), e);
        }

        this.sseClient = ClientBuilder.newBuilder().register(new AddAuthHeader()).build();
        this.client = asyncHttpClient();
        this.authService = AuthService.builder()
                .purpose(this.getClass().getSimpleName())
                .billingId(billingId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .config(config)
                .build();
    }

    public void receiveWs(boolean asJson, WebSocketListener consumer) {
        URI uri = asJson ? UriBuilder.fromUri(this.defaultWsEndpointUri).queryParam("asJson", true).build() : this.defaultWsEndpointUri;

        client.prepareGet(uri.toString())
                .addHeader(AUTHORIZATION, getBearerHeaderValue())
                .execute(new WebSocketUpgradeHandler.Builder()
                        .addWebSocketListener(consumer)
                        .build()
                );
    }

    public void receiveSse(boolean asJson, Consumer<InboundSseEvent> consumer) {
        URI uri = asJson ? UriBuilder.fromUri(this.defaultSseEndpointUri).queryParam("asJson", true).build() : this.defaultSseEndpointUri;

        SseEventSource eventSource = SseEventSource.target(sseClient.target(uri))
                .reconnectingEvery(60, TimeUnit.SECONDS)
                .build();

        eventSource.register(consumer);
        eventSource.open();
    }

    public Response isAlive() {
        return client.prepareGet(isAliveUri)
                .addHeader(AUTHORIZATION, getBearerHeaderValue())
                .execute()
                .toCompletableFuture()
                .join();
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
