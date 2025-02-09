package backend.logic;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.accessCookieExpiration}")
    private Duration accessCookieExpiration;

    private JwtParser jwtParser;
    private SecretKey secretKey;


    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessCookieExpiration.toMillis()))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims payload = jwtParser.parseSignedClaims(token).getPayload();
        return payload.get("username", String.class);
    }

    public boolean validateToken(String token, String username) {
        return getUsernameFromToken(token).equals(username) && !isExpired(token);
    }

    public boolean isExpired(String token) {
        return jwtParser.parseSignedClaims(token).getPayload()
                .getExpiration()
                .before(new Date());
    }
}
