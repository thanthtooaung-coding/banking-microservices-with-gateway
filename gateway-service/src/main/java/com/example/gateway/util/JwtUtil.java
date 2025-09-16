package com.example.gateway.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
  private final String secret;
  private final long expirationMs;
  private final Algorithm alg;
  public JwtUtil(String secret,long expirationMs){this.secret=secret;this.expirationMs=expirationMs;this.alg=Algorithm.HMAC256(secret);}
  public String generateToken(Map<String,Object> claims){
    var b = JWT.create().withIssuer("gateway").withExpiresAt(new Date(System.currentTimeMillis()+expirationMs));
    for(var e: claims.entrySet()) b.withClaim(e.getKey(), String.valueOf(e.getValue()));
    return b.sign(alg);
  }
  public DecodedJWT verify(String token){ return JWT.require(alg).build().verify(token); }
}
