package io.strmprivacy.driver.client;

public final class AuthProviderBuilder {
    private String idToken;
    private String refreshToken;
    private long expiresAt;

    private AuthProviderBuilder() {
    }

    public static AuthProviderBuilder anAuthProvider() {
        return new AuthProviderBuilder();
    }

    public AuthProviderBuilder withIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

    public AuthProviderBuilder withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public AuthProviderBuilder withExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public AuthProvider build() {
        return new AuthProvider(idToken, refreshToken, expiresAt);
    }
}
