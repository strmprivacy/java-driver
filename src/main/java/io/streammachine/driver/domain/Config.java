package io.streammachine.driver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Config {
    @Builder.Default
    private final String gatewayProtocol = "https";
    @Builder.Default
    private final String gatewayHost = "in.strm.services";
    @Builder.Default
    private final String gatewayEndpoint = "/event";

    @Builder.Default
    private final String egressProtocol = "https";
    @Builder.Default
    private final String egressHost = "out.strm.services";
    @Builder.Default
    private final String egressEndpoint = "/sse";
    private final String egressHealthEndpoint = "/is-alive";

    @Builder.Default
    private final String stsProtocol = "https";
    @Builder.Default
    private final String stsHost = "auth.strm.services";
    @Builder.Default
    private final String stsAuthEndpoint = "/auth";
    @Builder.Default
    private final String stsRefreshEndpoint = "/refresh";

    @Builder.Default
    private final String implementationVersion = "development";
}
