package io.strmprivacy.driver.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.serializer.AvroSerializerTest;
import io.strmprivacy.driver.serializer.SerializationType;
import io.strmprivacy.schemas.demo.v1.DemoEvent;
import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class StrmPrivacyClientTest {

    @Test
    void testSend204(WireMockRuntimeInfo wireMockRuntimeInfo) {

        Config config = Config.builder()
                .gatewayHost("localhost")
                .gatewayPort(wireMockRuntimeInfo.getHttpPort())
                .gatewayScheme("http")
                .build();
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/"+config.getGatewayEndpoint()))
                        .willReturn(WireMock.aResponse().withStatus(204)));

        StrmPrivacyClient client = StrmPrivacyClient.builder()
                .billingId("billingId")
                .clientId("clientId")
                .clientSecret("clientSecret")
                .config(config)
                .build();

        DemoEvent event = AvroSerializerTest.demoEvent();
        CompletableFuture<ContentResponse> response = client.send(event, SerializationType.AVRO_BINARY);
        response.whenComplete((res, exc) -> {
            assertEquals(204, res.getStatus());

        });


    }
}
