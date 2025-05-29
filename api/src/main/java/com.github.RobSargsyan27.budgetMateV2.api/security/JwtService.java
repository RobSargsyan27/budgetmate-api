package com.github.RobSargsyan27.budgetMateV2.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "n7wfDeE91HArAnZGewzSoADRBP8Vo2vCwdVSOJ5TW6VdahlEXa2tIROhINORFz1+";

    public String extractUsername(String token) throws ExpiredJwtException {
        return this.extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3 ))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = this.extractUsername(token);

        return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) throws ExpiredJwtException {
        return this.extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException{
        Claims claims = this.extractAllClaims(token);

        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(JwtService.SECRET_KEY);

        return Keys.hmacShaKeyFor(bytes);
    }
}
