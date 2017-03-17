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

/**
 * Encodes and decodes a java web token used to identify a dawg user
 * @author Kevin Pearson
 *
 */
public class DawgJwtEncoder {
    public static final String DEFAULT_ISSUER = "dawg";
    public static final long DEFAULT_TTL = TimeUnit.HOURS.toMillis(1);
    public static final SignatureAlgorithm SIGNATURE_ALG = SignatureAlgorithm.HS256;
    public static final String JWT_SUBJECT = "dawguser";

    private String jwtSecret;
    private String jwtIssuer;
    private long ttlMillis;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a DawgJetEncoder
     * @param jwtSecret The secret used to sign the jwt
     */
    public DawgJwtEncoder(String jwtSecret) {
        this(jwtSecret, DEFAULT_ISSUER, DEFAULT_TTL);
    }
    
    /**
     * Creates a DawgJetEncoder
     * @param jwtSecret The secret used to sign the jwt
     * @param jwtIssuer The name of the entity that is issuing this jwt
     * @param ttlMillis The time in milliseconds that the jwt will live
     */
    public DawgJwtEncoder(String jwtSecret, String jwtIssuer, long ttlMillis) {
        this.jwtSecret = jwtSecret;
        this.jwtIssuer = jwtIssuer;
        this.ttlMillis = ttlMillis;
    }

    /**
     * Creates the jwt for the given dawg credentials
     * @param dawgCreds The credentials to encode
     * @return
     */
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
    
    /**
     * Turns an existing jwt object into a jwt string and signs it
     * @param dawgJwt
     * @return
     */
    public String createUserJWT(DawgJwt dawgJwt) {
        Key signingKey = new SecretKeySpec(jwtSecret.getBytes(), SIGNATURE_ALG.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setClaims(dawgJwt)
                .setId(UUID.randomUUID().toString())
                .signWith(SIGNATURE_ALG, signingKey);
        return builder.compact();
    }

    /**
     * Decodes a java web token into the DawgJwt, which can be used to retrieve the {@link DawgCreds}
     * @param jwt The jwt to decode
     * @return
     */
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
