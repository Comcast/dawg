package com.comcast.video.dawg.common.security.jwt;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpUriRequest;

import com.comcast.drivethru.RestClient;
import com.comcast.drivethru.exception.HttpException;
import com.comcast.drivethru.security.SecurityProvider;
import com.google.common.collect.Sets;

/**
 * Security provider that can be given to the {@link RestClient} in order to set the Authorization header
 * to contain the jwt that authenticates the given user and password as an admin.
 * The jwt will refresh itself before it expires
 * @author Kevin Pearson
 *
 */
public class JwtSecurityProvider implements SecurityProvider {
    private String user;
    private String password;
    private DawgJwtEncoder jwtEncoder;
    private String jwt;
    private Date jwtExpiration;

    public JwtSecurityProvider(String user, String password, DawgJwtEncoder jwtEncoder) {
        this.user = user;
        this.password = password;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public void sign(HttpUriRequest request) throws HttpException {
        refreshToken();
        String auth = "Bearer " + new String(Base64.encodeBase64(this.jwt.getBytes(), false));
        request.setHeader("Authorization", auth);
    }
    
    private synchronized void refreshToken() {
        Date exp = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
        if ((jwt == null) || (jwtExpiration == null) || (exp.after(jwtExpiration))) {
            DawgCreds creds = new DawgCreds(this.user, this.password, Sets.newHashSet("ADMIN"));
            this.jwt = jwtEncoder.createUserJWT(creds);
            this.jwtExpiration = jwtEncoder.decodeJwt(this.jwt).getExpiration();
        }
    }

}
