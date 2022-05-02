package tn.request.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Long TOKEN_VALIDITY_IN_MILLS = 1000L * 60 * 60 * 10;

    private static final String SECRET_KEY = "a".repeat(64);

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();

        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(TOKEN_VALIDITY_IN_MILLS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * @param token A JWT token generated using {@link JwtService#generateToken}
     * @return {@code true} if {@code token} is expired, {@code false} otherwise
     */
    public Boolean isTokenExpired(String token) {
        Instant tokenExpirationInstant = extractExpirationDateFromToken(token).toInstant();
        return tokenExpirationInstant.isBefore(Instant.now());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(
                Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build().parseClaimsJws(token).getBody());
    }

    public Date extractExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if {@code token} is still valid for {@code user}
     *
     * @return {@code true} if token is match {@code user} and is valid
     */
    public Boolean isTokenValidForUser(String token, UserDetails user) {
        return extractUsernameFromToken(token).equals(user.getUsername()) && !isTokenExpired(token);
    }
}
