package com.comcast.video.dawg.common.security.jwt;

import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

public class DawgJwt extends DefaultClaims {
    public static final String JWT_FIELD_CREDS = "creds";
    
    public DawgJwt() {
        
    }
    
    public DawgJwt(Map<String, Object> map) {
        super(map);
    }

    public DawgCreds getCreds() {
        return get(JWT_FIELD_CREDS, DawgCreds.class);
    }

    public Claims setCreds(DawgCreds user) {
        setValue(JWT_FIELD_CREDS, user);
        return this;
    }
    
    
}
