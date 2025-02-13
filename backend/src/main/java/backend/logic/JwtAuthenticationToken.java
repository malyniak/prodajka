package backend.logic;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;


@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String refreshToken;

    public JwtAuthenticationToken(String accessToken, String refreshToken, List<GrantedAuthority> authorities) {
        super(accessToken, accessToken, authorities);
        this.refreshToken = refreshToken;
    }
}
