package com.example.demo.config.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private static final String SECRET_KEY = "secret";
    private static final String USERNAME = "testUser";
    private static final String TOKEN = JwtUtil.

    @Test
    public void testExtractUsername() {
        String username = JwtUtil.extractUsername(TOKEN);
        assertEquals(USERNAME, username);
    }

    @Test
    public void testExtractExpiration() {
        Date expiration = JwtUtil.extractExpiration(TOKEN);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testExtractClaim() {
        String claim = JwtUtil.extractClaim(TOKEN, Claims::getSubject);
        assertEquals(USERNAME, claim);
    }

    @Test
    public void testExtractAllClaims() {
        Claims claims = JwtUtil.extractAllClaims(TOKEN);
        assertEquals(USERNAME, claims.getSubject());
    }

    @Test
    public void testIsTokenExpired() {
        boolean isExpired = JwtUtil.isTokenExpired(TOKEN);
        assertFalse(isExpired);
    }

    @Test
    public void testGenerateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", "ROLE_ADMIN");
        String token = JwtUtil.createToken(claims, USERNAME);
        assertTrue(!token.isEmpty());
    }

    @Test
    public void testValidateToken() {
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return USERNAME;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
        boolean isValid = JwtUtil.validateToken(TOKEN, userDetails);
        assertTrue(isValid);
    }
}

