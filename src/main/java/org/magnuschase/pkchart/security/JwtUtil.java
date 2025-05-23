package org.magnuschase.pkchart.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.magnuschase.pkchart.model.Role;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  @org.springframework.beans.factory.annotation.Value("${jwt.secret}")
  private String secret;

  private Key key;

  @PostConstruct
  public void init() {
    byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String username, String role) {
    // 1 day
    long expirationMs = 86400000;
    return Jwts.builder()
        .setSubject(username)
        .claim("role", role)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public Role extractRole(String token) {
    String roleStr =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    return Role.valueOf(roleStr);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
