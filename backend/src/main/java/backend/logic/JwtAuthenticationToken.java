package backend.logic;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;


@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JwtAuthenticationToken(String accessToken, List<GrantedAuthority> authorities) {
        super(accessToken, accessToken, authorities);
    }
}
