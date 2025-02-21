package backend.logic;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.accessCookieExpiration}")
    private Duration accessCookieExpiration;
    @Value("${jwt.refreshCookieExpiration}")
    private Duration refreshCookieExpiration;

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
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshCookieExpiration.toMillis()))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
            Claims payload = claimsJws.getPayload();
            return payload.get("username", String.class);
        } catch (ExpiredJwtException e) {
            return null;
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        return !isExpired(token);
    }

    public boolean isExpired(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new JwtException("Некоректний JWT токен!");
        }
    }

}
