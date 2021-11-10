package io.strmprivacy.driver.client;

import io.strmprivacy.driver.common.CompletableFutureResponseListener;
import io.strmprivacy.driver.common.WebSocketConsumer;
import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.domain.StrmPrivacyException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
class ReceiverService {
    private final String isAliveUri;
    private final URI defaultWsEndpointUri;
    private final HttpClient httpClient;
    private final WebSocketClient wsClient;
    private final AuthService authService;

    public ReceiverService(AuthService authService, Config config) {
        try {
            this.isAliveUri = String.format("%s://%s%s",
                    config.getEgressScheme(),
                    config.getEgressHost(),
                    config.getEgressHealthEndpoint()
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


        SslContextFactory sslContextFactory = new SslContextFactory.Client();
        this.httpClient = new HttpClient(sslContextFactory);
        this.wsClient = new WebSocketClient(httpClient);
        this.authService = authService;

        try {
            httpClient.start();
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred while starting a new Receiver for STRM Privacy.", e);
        }
    }

    public void receiveWs(boolean asJson, WebSocketConsumer consumer) {
        URI uri = asJson ? UriBuilder.fromUri(this.defaultWsEndpointUri).queryParam("asJson", true).build() : this.defaultWsEndpointUri;

        try {
            wsClient.start();

            ClientUpgradeRequest request = new ClientUpgradeRequest();
            request.setHeader(HttpHeader.AUTHORIZATION.asString(), getBearerHeaderValue());

            Future<Session> future = wsClient.connect(consumer, uri, request);

            Session session = future.get();

            consumer.awaitClosure();

            session.close();
        } catch (Exception e) {
            throw new StrmPrivacyException("An error occurred while (dis)connecting WebSocket.", e);
        } finally {
            try {
                wsClient.stop();
            } catch (Exception e) {
                throw new StrmPrivacyException("An error occurred while disconnecting WebSocket ", e);
            }
        }
    }

    public ContentResponse isAlive() {
        CompletableFuture<ContentResponse> completableFuture = new CompletableFuture<>();

        httpClient.newRequest(isAliveUri)
                .method(HttpMethod.GET)
                .header(HttpHeader.AUTHORIZATION, getBearerHeaderValue())
                .send(new CompletableFutureResponseListener(completableFuture));

        return completableFuture.join();
    }

    private String getBearerHeaderValue() {
        return String.format("Bearer %s", authService.getAccessToken());
    }

    public void stop() {
        StrmPrivacyException exception = null;
        try {
            this.httpClient.stop();
        } catch (Exception e) {
            exception = new StrmPrivacyException("Error stopping ReceiverService HttpClient", e);
        }
        try {
            this.wsClient.stop();
        } catch (Exception e) {
            if (exception != null) {
                exception.addSuppressed(e);
            } else {
                exception = new StrmPrivacyException("Error stopping ReceiverService WsClient", e);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }
}
