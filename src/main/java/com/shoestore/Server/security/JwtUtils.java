package com.shoestore.Server.security;

import com.shoestore.Server.dto.response.LoginResponse;
import com.shoestore.Server.dto.response.UserInsideTokenResponse;
import com.shoestore.Server.dto.response.UserLoginResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long accessExpirationMs;//10 mínutes

    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationMs;//2 weeks

    private Key getSigningKey() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT Secret Key is not configured");
        }
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String createAccessToken(String identifier, LoginResponse resLoginDTO) {
        UserLoginResponse user = resLoginDTO.getUser();

        UserInsideTokenResponse userInsideToken = new UserInsideTokenResponse(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getName()
        );

        Instant now = Instant.now();
        Instant validity = now.plus(accessExpirationMs, ChronoUnit.MILLIS);

        String subject = identifier.contains("@") ? user.getEmail() : user.getPhoneNumber();

        return Jwts.builder()
                .setSubject(subject)
                .claim("user", userInsideToken)
                .claim("roles", Collections.singletonList(resLoginDTO.getUser().getRole().getName()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(String identifier, LoginResponse resLoginDTO, Instant originalExpiration) {
        UserLoginResponse user = resLoginDTO.getUser();
        UserInsideTokenResponse userInsideToken = new UserInsideTokenResponse(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getName()
        );

        Instant now = Instant.now();
        Instant validity = (originalExpiration == null) ? now.plus(refreshExpirationMs, ChronoUnit.MILLIS) : originalExpiration;

        String subject = identifier.contains("@") ? user.getEmail() : user.getPhoneNumber();

        return Jwts.builder()
                .setSubject(subject)
                .claim("user", userInsideToken)
                .claim("refresh_exp", validity.getEpochSecond())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

    public Optional<Claims> checkValidJWTRefreshToken(String token) {
        try {
            return Optional.of(
                    Jwts.parser()
                            .setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
            );
        } catch (ExpiredJwtException e) {
            log.warn(">>> Refresh Token Expired: {}", e.getMessage());
            throw new JwtException("Refresh Token has expired", e);
        } catch (MalformedJwtException e) {
            log.warn(">>> Invalid Refresh Token: {}", e.getMessage());
            throw new JwtException("Invalid Refresh Token", e);
        } catch (Exception e) {
            log.error(">>> Refresh Token Error: {}", e.getMessage());
            throw new JwtException("Unknown Refresh Token Error", e);
        }
    }

    public String extractUsername(String token) {
        return parseClaims(token).map(Claims::getSubject).orElse(null);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        if (token == null || userDetails == null) {
            return false;
        }
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return parseClaims(token)
                .map(claims -> claims.getExpiration().before(new Date()))
                .orElse(true);
    }

    public Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(
                    Jwts.parser()
                            .setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
            );
        } catch (JwtException e) {
            log.warn(">>> Invalid JWT: " + e.getMessage());
        }
        return Optional.empty();
    }
}
