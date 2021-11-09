package io.strmprivacy.driver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Config {
    @Builder.Default
    private final String gatewayScheme = "https";
    @Builder.Default
    private final String gatewayHost = "events.strmprivacy.io";
    @Builder.Default
    private final String gatewayEndpoint = "/event";
    @Builder.Default
    private final int gatewayPort = 443;

    @Builder.Default
    private final String egressScheme = "https";
    @Builder.Default
    private final String egressWsScheme = "wss";
    @Builder.Default
    private final String egressHost = "websocket.strmprivacy.io";
    @Builder.Default
    private final String egressWsEndpoint = "/ws";
    private final String egressHealthEndpoint = "/is-alive";

    @Builder.Default
    private final String stsScheme = "https";
    @Builder.Default
    private final String stsHost = "sts.strmprivacy.io";
    @Builder.Default
    private final String stsAuthEndpoint = "/auth";
    @Builder.Default
    private final String stsRefreshEndpoint = "/refresh";

    public String getImplementationVersion() {
        String version = this.getClass().getPackage().getImplementationVersion();

        return version != null ? version : "development";
    }
}
