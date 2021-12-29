package io.strmprivacy.driver.domain;


public class Config {
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

    public Config(String gatewayScheme, String gatewayHost, String gatewayEndpoint, int gatewayPort, String egressScheme, String egressWsScheme, String egressHost, String egressWsEndpoint, String egressHealthEndpoint, String stsScheme, String stsHost, String stsAuthEndpoint, String stsRefreshEndpoint) {
        this.gatewayScheme = gatewayScheme;
        this.gatewayHost = gatewayHost;
        this.gatewayEndpoint = gatewayEndpoint;
        this.gatewayPort = gatewayPort;
        this.egressScheme = egressScheme;
        this.egressWsScheme = egressWsScheme;
        this.egressHost = egressHost;
        this.egressWsEndpoint = egressWsEndpoint;
        this.egressHealthEndpoint = egressHealthEndpoint;
        this.stsScheme = stsScheme;
        this.stsHost = stsHost;
        this.stsAuthEndpoint = stsAuthEndpoint;
        this.stsRefreshEndpoint = stsRefreshEndpoint;
    }

    public String getImplementationVersion() {
        String version = this.getClass().getPackage().getImplementationVersion();

        return version != null ? version : "development";
    }

    public String getGatewayScheme() {
        return gatewayScheme;
    }

    public void setGatewayScheme(String gatewayScheme) {
        this.gatewayScheme = gatewayScheme;
    }

    public String getGatewayHost() {
        return gatewayHost;
    }

    public void setGatewayHost(String gatewayHost) {
        this.gatewayHost = gatewayHost;
    }

    public String getGatewayEndpoint() {
        return gatewayEndpoint;
    }

    public void setGatewayEndpoint(String gatewayEndpoint) {
        this.gatewayEndpoint = gatewayEndpoint;
    }

    public int getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(int gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    public String getEgressScheme() {
        return egressScheme;
    }

    public void setEgressScheme(String egressScheme) {
        this.egressScheme = egressScheme;
    }

    public String getEgressWsScheme() {
        return egressWsScheme;
    }

    public void setEgressWsScheme(String egressWsScheme) {
        this.egressWsScheme = egressWsScheme;
    }

    public String getEgressHost() {
        return egressHost;
    }

    public void setEgressHost(String egressHost) {
        this.egressHost = egressHost;
    }

    public String getEgressWsEndpoint() {
        return egressWsEndpoint;
    }

    public void setEgressWsEndpoint(String egressWsEndpoint) {
        this.egressWsEndpoint = egressWsEndpoint;
    }

    public String getEgressHealthEndpoint() {
        return egressHealthEndpoint;
    }

    public void setEgressHealthEndpoint(String egressHealthEndpoint) {
        this.egressHealthEndpoint = egressHealthEndpoint;
    }

    public String getStsScheme() {
        return stsScheme;
    }

    public void setStsScheme(String stsScheme) {
        this.stsScheme = stsScheme;
    }

    public String getStsHost() {
        return stsHost;
    }

    public void setStsHost(String stsHost) {
        this.stsHost = stsHost;
    }

    public String getStsAuthEndpoint() {
        return stsAuthEndpoint;
    }

    public void setStsAuthEndpoint(String stsAuthEndpoint) {
        this.stsAuthEndpoint = stsAuthEndpoint;
    }

    public String getStsRefreshEndpoint() {
        return stsRefreshEndpoint;
    }

    public void setStsRefreshEndpoint(String stsRefreshEndpoint) {
        this.stsRefreshEndpoint = stsRefreshEndpoint;
    }
}
