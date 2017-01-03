package com.comcast.video.dawg.common.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class DawgJwtEncoder {
    public static final String DEFAULT_ISSUER = "dawg";
    public static final long DEFAULT_TTL = TimeUnit.HOURS.toMillis(1);
    public static final SignatureAlgorithm SIGNATURE_ALG = SignatureAlgorithm.HS256;
    public static final String JWT_SUBJECT = "dawguser";

    private String jwtSecret;
    private String jwtIssuer;
    private long ttlMillis;
    private ObjectMapper mapper = new ObjectMapper();

    public DawgJwtEncoder(String jwtSecret) {
        this(jwtSecret, DEFAULT_ISSUER, DEFAULT_TTL);
    }

    public DawgJwtEncoder(String jwtSecret, String jwtIssuer, long ttlMillis) {
        this.jwtSecret = jwtSecret;
        this.jwtIssuer = jwtIssuer;
        this.ttlMillis = ttlMillis;
    }

    public String createUserJWT(DawgCreds dawgCreds) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key signingKey = new SecretKeySpec(jwtSecret.getBytes(), SIGNATURE_ALG.getJcaName());
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(DawgJwt.JWT_FIELD_CREDS, dawgCreds);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now) 
                .setSubject(JWT_SUBJECT)
                .setIssuer(this.jwtIssuer)
                .signWith(SIGNATURE_ALG, signingKey);

        if (this.ttlMillis >= 0) {
            long expMillis = nowMillis + this.ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    public DawgJwt decodeJwt(String jwt) {
        Jws<Claims> token = Jwts.parser()         
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jwt);
        Claims claims =  token.getBody();
        if (claims.containsKey(DawgJwt.JWT_FIELD_CREDS)) {
            DawgCreds serializedUser = mapper.convertValue(claims.get(DawgJwt.JWT_FIELD_CREDS, Map.class), DawgCreds.class);
            claims.put(DawgJwt.JWT_FIELD_CREDS, serializedUser);
        }
        return new DawgJwt(claims);
    }
}
