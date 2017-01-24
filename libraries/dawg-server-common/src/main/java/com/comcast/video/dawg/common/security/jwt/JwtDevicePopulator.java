package com.comcast.video.dawg.common.security.jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comcast.video.dawg.common.DeviceProvider;
import com.comcast.video.dawg.common.MetaStb;

/**
 * Populates devices on a user's jwt cookie.
 * @author Kevin Pearson
 *
 */
public class JwtDevicePopulator {
    
    private DawgJwtEncoder jwtEncoder;
    private DeviceProvider deviceProvider;
    private DawgCookieUtils cookieUtils = new DawgCookieUtils();
    
    public JwtDevicePopulator(DawgJwtEncoder jwtEncoder, DeviceProvider deviceProvider) {
        this.jwtEncoder = jwtEncoder;
        this.deviceProvider = deviceProvider;
    }
    
    /**
     * Extracts the cookie, finds the devices a user has reserved and then resets the cookie with the devices added
     * @param req The request object, for getting the cookie
     * @param resp The response object, for setting the cookie
     * @return
     */
    public DawgJwt assignDevicesToCookie(HttpServletRequest req, HttpServletResponse resp) {
        String jwt = cookieUtils.extractJwt(req);
        DawgJwt dawgJwt = jwtEncoder.decodeJwt(jwt);
        modifyJwtWithDeviceIds(dawgJwt);
        String jwtWithDevs = jwtEncoder.createUserJWT(dawgJwt);
        resp.addCookie(cookieUtils.createCookie(jwtWithDevs, -1, req));
        return dawgJwt;
    }
    
    /**
     * Gets the deviceIds that are reserved by the logged in user and then puts them in the jwt.
     * @param dawgJwt The jwt to modify
     */
    public void modifyJwtWithDeviceIds(DawgJwt dawgJwt) {
        String user = dawgJwt.getCreds().getUserName();
        Collection<MetaStb> devices = this.deviceProvider.getDevices(user);
        Set<String> ids = new HashSet<String>();
        for (MetaStb device : devices) {
            ids.add(device.getId());
        }
        dawgJwt.getCreds().setDeviceIds(ids);
    }
}
