package backend.logic;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@Value
@ConfigurationProperties("jwt")
@Validated
public class JwtProperties {
     String secret;
     String accessCookieName;
     String refreshCookieName;
     String accessCookieExpiration;
     String refreshCookieExpiration;
}
