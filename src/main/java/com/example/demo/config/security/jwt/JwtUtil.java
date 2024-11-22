package com.example.demo.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j; // Импортируем аннотацию для логирования
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j // Аннотация для логирования
public class JwtUtil {
    private JwtUtil() {
        throw new UnsupportedOperationException();
    }

    private static final String SECRET_KEY = "secret";

    public static String extractUsername(String token) {
        log.info("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpiration(String token) {
        log.info("Extracting expiration date from token");
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.info("Extracting claims from token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static Claims extractAllClaims(String token) {
        log.info("Extracting all claims from token");
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Collection<? extends GrantedAuthority> extractAuthority(String token) {
        log.info("Extracting authorities from token");
        return extractClaim(token, (Claims claims) -> claims.get("roles", Collection.class));
    }

    public static Boolean isTokenExpired(String token) {
        log.info("Checking if token is expired");
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(UserDetails userDetails) {
        log.info("Generating token for user: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    public static String createToken(Map<String, Object> claims, String subject) {
        log.info("Creating token for subject: {}", subject);
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        log.info("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        log.info("Token validation result for user {}: {}", userDetails.getUsername(), isValid);
        return isValid;
    }
}
