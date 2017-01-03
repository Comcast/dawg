package com.comcast.video.dawg.common.security;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.jwt.DawgCreds;
import com.google.common.collect.Sets;

import io.jsonwebtoken.SignatureException;


public class JwtTest {

    @Test
    public void testJwtEncodeAndDecode() {
        DawgJwtEncoder helper = new DawgJwtEncoder("14C549805A5911368929D139C5C6282E66CE017F8E861C4EAC4D2975269BEA6F");
        DawgCreds dawgUser = new DawgCreds("kpears201", "pw", Sets.newHashSet("admin", "house"), Sets.newHashSet("id1"));
        String jwt = helper.createUserJWT(dawgUser);
        DawgCreds decoded = helper.decodeJwt(jwt).getCreds();
        Assert.assertEquals(decoded.getUserName(), "kpears201");
        Assert.assertEquals(decoded.getRoles(), Sets.newHashSet("admin", "house"));
        Assert.assertEquals(decoded.getDeviceIds(), Sets.newHashSet("id1"));
    }

    @Test(expectedExceptions=SignatureException.class)
    public void testJwtEncodeAndDecodeWithBadKey() {
        DawgJwtEncoder helper = new DawgJwtEncoder("14C549805A5911368929D139C5C6282E66CE017F8E861C4EAC4D2975269BEA6F");
        DawgCreds dawgJwt = new DawgCreds("kpears201", "pw", Sets.newHashSet("admin", "house"), Sets.newHashSet("id1"));
        String jwt = helper.createUserJWT(dawgJwt);
        helper = new DawgJwtEncoder("14C549805A5911368929D139C5C6282E66CE017F8E861C4EAC4D2975269BEA60");
        helper.decodeJwt(jwt);
    }
}
