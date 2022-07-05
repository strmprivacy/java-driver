package io.strmprivacy.driver.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.serializer.SerializerTests;
import io.strmprivacy.schemas.demo.v1.DemoEvent;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
class StrmPrivacyClientTest {

    @Test
    void testSend204Avro(WireMockRuntimeInfo wireMockRuntimeInfo) {

        Config config = getTestConfig(wireMockRuntimeInfo);
        stubFor(post(urlEqualTo(config.getGatewayEndpoint()))
                .willReturn(aResponse().withStatus(204)));

        StrmPrivacyClient client = StrmPrivacyClient.builder()
                .clientId("clientId")
                .clientSecret("clientSecret")
                .config(config)
                .build();

        DemoEvent event = SerializerTests.avroDemoEvent();
        client.send(event)
                .whenComplete((res, exc) -> assertEquals(204, res.getStatus()));
    }

    @Test
    void testSend204JsonSchema(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {

        Config config = getTestConfig(wireMockRuntimeInfo);
        stubFor(post(urlEqualTo(config.getGatewayEndpoint()))
                .willReturn(aResponse().withStatus(204)));

        StrmPrivacyClient client = StrmPrivacyClient.builder()
                .clientId("clientId")
                .clientSecret("clientSecret")
                .config(config)
                .build();

        SerializerTests.JsonSchemaDemoEvent event = SerializerTests.jsonSchemaDemoEvent();
        client.send(event)
                .whenComplete((res, exc) -> assertEquals(204, res.getStatus()));

    }

    private Config getTestConfig(WireMockRuntimeInfo wireMockRuntimeInfo) {
        return Config.builder()
                .gatewayHost("localhost")
                .gatewayPort(wireMockRuntimeInfo.getHttpPort())
                .gatewayScheme("http")
                .keycloakScheme("https")
                .keycloakHost("accounts.dev.strmprivacy.io")
                .keycloakEndpoint("/auth/realms/streams/protocol/openid-connect/token")
                .build();
    }

}
