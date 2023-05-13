package ru.hh.techradar.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Value("${token.secret-key}")
  private String TOKEN_SECRET_KEY;
  @Value("${token.expired.access}")
  private Long TOKEN_EXPIRED_ACCESS;
  @Value("${token.expired.refresh}")
  private Long TOKEN_EXPIRED_REFRESH;

  public boolean isTokenValid(String token, UserDetails userDetails) {
    return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String generateAccessToken(UserDetails userDetails) {
    return generateAccessToken(new HashMap<>(), userDetails, TOKEN_EXPIRED_ACCESS);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return generateAccessToken(new HashMap<>(), userDetails, TOKEN_EXPIRED_REFRESH);
  }

  private String generateAccessToken(Map<String, Object> claims, UserDetails userDetails, Long expired) {
    return Jwts
        .builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expired))
        .signWith(getSecretKey(), SignatureAlgorithm.HS256)
        .compact();

  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
    Claims claims = extractClaims(token);
    return claimsFunction.apply(claims);
  }

  private Claims extractClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SECRET_KEY));
  }
}
