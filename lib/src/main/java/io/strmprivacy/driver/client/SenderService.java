package io.strmprivacy.driver.client;

import io.strmprivacy.driver.common.CompletableFutureResponseListener;
import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.domain.StrmPrivacyException;
import io.strmprivacy.driver.serializer.EventSerializer;
import io.strmprivacy.driver.serializer.SerializerProvider;
import io.strmprivacy.schemas.StrmPrivacyEvent;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.BytesRequestContent;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

class SenderService {
    private final String endpointUri;
    private final HttpClient httpClient;
    private final HTTP2Client http2Client;
    private final AuthService authService;
    private final Config config;

    public SenderService(AuthService authService, Config config) {
        this.endpointUri = String.format("%s://%s:%s%s", config.getGatewayScheme(), config.getGatewayHost(), config.getGatewayPort(), config.getGatewayEndpoint());
        this.authService = authService;
        this.config = config;

        http2Client = new HTTP2Client();
        httpClient = new HttpClient(new HttpClientTransportOverHTTP2(http2Client));

        try {
            http2Client.start();
            httpClient.start();
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred while starting a new Sender for STRM Privacy.", e);
        }
    }


    public CompletableFuture<ContentResponse> send(StrmPrivacyEvent event) {
        CompletableFuture<ContentResponse> completableFuture = new CompletableFuture<>();

        httpClient.POST(this.endpointUri)
                .headers(headers -> {
                    headers.add(HttpHeader.AUTHORIZATION, "Bearer " + authService.getAccessToken());
                    headers.add(HttpHeader.CONTENT_TYPE, "application/octet-stream");
                    headers.add("Strm-Driver-Version", config.getImplementationVersion());
                    headers.add("Strm-Schema-Ref", event.getSchemaRef());
                })
                .body(new BytesRequestContent(serialize(event)))
                .send(new CompletableFutureResponseListener(completableFuture));

        return completableFuture;
    }

    byte[] serialize(StrmPrivacyEvent event) {
        try {
            final EventSerializer serializer = SerializerProvider.getSerializer(event.getSchemaRef(), event.getSchema());
            return serializer.serialize(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        StrmPrivacyException exception = null;
        try {
            this.httpClient.stop();
        } catch (Exception e) {
            exception = new StrmPrivacyException("Error stopping SenderService HttpClient", e);
        }
        try {
            this.http2Client.stop();
        } catch (Exception e) {
            if (exception != null) {
                exception.addSuppressed(e);
            } else {
                exception = new StrmPrivacyException("Error stopping SenderService Http2Client", e);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }
}
