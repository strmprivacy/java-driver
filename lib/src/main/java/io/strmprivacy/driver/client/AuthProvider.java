package io.strmprivacy.driver.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
class AuthProvider {
    private String idToken;
    private String refreshToken;
    private long expiresAt;
}
