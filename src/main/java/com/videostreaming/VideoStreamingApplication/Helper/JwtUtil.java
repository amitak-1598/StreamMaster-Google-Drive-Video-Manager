package com.videostreaming.VideoStreamingApplication.Helper;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "DejDJcjc5UbVKkHjn7RnLEMRdCyA9T57LqzyTPCfP9Y=";

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public static String getRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    public static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public static boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
