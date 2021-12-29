package io.strmprivacy.driver.domain;

public final class ConfigBuilder {
    private String gatewayScheme = "https";
    private String gatewayHost = "events.strmprivacy.io";
    private String gatewayEndpoint = "/event";
    private int gatewayPort = 443;
    private String egressScheme = "https";
    private String egressWsScheme = "wss";
    private String egressHost = "websocket.strmprivacy.io";
    private String egressWsEndpoint = "/ws";
    private String egressHealthEndpoint = "/is-alive";
    private String stsScheme = "https";
    private String stsHost = "sts.strmprivacy.io";
    private String stsAuthEndpoint = "/auth";
    private String stsRefreshEndpoint = "/refresh";

    private ConfigBuilder() {
    }

    public static ConfigBuilder aConfig() {
        return new ConfigBuilder();
    }

    public ConfigBuilder withGatewayScheme(String gatewayScheme) {
        this.gatewayScheme = gatewayScheme;
        return this;
    }

    public ConfigBuilder withGatewayHost(String gatewayHost) {
        this.gatewayHost = gatewayHost;
        return this;
    }

    public ConfigBuilder withGatewayEndpoint(String gatewayEndpoint) {
        this.gatewayEndpoint = gatewayEndpoint;
        return this;
    }

    public ConfigBuilder withGatewayPort(int gatewayPort) {
        this.gatewayPort = gatewayPort;
        return this;
    }

    public ConfigBuilder withEgressScheme(String egressScheme) {
        this.egressScheme = egressScheme;
        return this;
    }

    public ConfigBuilder withEgressWsScheme(String egressWsScheme) {
        this.egressWsScheme = egressWsScheme;
        return this;
    }

    public ConfigBuilder withEgressHost(String egressHost) {
        this.egressHost = egressHost;
        return this;
    }

    public ConfigBuilder withEgressWsEndpoint(String egressWsEndpoint) {
        this.egressWsEndpoint = egressWsEndpoint;
        return this;
    }

    public ConfigBuilder withEgressHealthEndpoint(String egressHealthEndpoint) {
        this.egressHealthEndpoint = egressHealthEndpoint;
        return this;
    }

    public ConfigBuilder withStsScheme(String stsScheme) {
        this.stsScheme = stsScheme;
        return this;
    }

    public ConfigBuilder withStsHost(String stsHost) {
        this.stsHost = stsHost;
        return this;
    }

    public ConfigBuilder withStsAuthEndpoint(String stsAuthEndpoint) {
        this.stsAuthEndpoint = stsAuthEndpoint;
        return this;
    }

    public ConfigBuilder withStsRefreshEndpoint(String stsRefreshEndpoint) {
        this.stsRefreshEndpoint = stsRefreshEndpoint;
        return this;
    }

    public Config build() {
        return new Config(gatewayScheme, gatewayHost, gatewayEndpoint, gatewayPort, egressScheme, egressWsScheme, egressHost, egressWsEndpoint, egressHealthEndpoint, stsScheme, stsHost, stsAuthEndpoint, stsRefreshEndpoint);
    }
}
