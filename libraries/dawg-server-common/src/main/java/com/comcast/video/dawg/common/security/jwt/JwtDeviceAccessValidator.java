package com.comcast.video.dawg.common.security.jwt;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comcast.video.dawg.common.DeviceProvider;
import com.comcast.video.dawg.common.exceptions.DeviceNotAuthorizedException;

/**
 * Checks if a user has access to devices based on the jwt that is in the cookie or authorization header.
 * @author Kevin Pearson
 *
 */
public class JwtDeviceAccessValidator {

    private JwtDevicePopulator jwtDevicePopulator;
    private DawgJwtEncoder jwtEncoder;
    private boolean enabled;
    private DawgCookieUtils cookieUtils = new DawgCookieUtils();
    
    public JwtDeviceAccessValidator(DawgJwtEncoder jwtEncoder, DeviceProvider deviceProvider, boolean enabled) {
        this.jwtDevicePopulator = new JwtDevicePopulator(jwtEncoder, deviceProvider);
        this.jwtEncoder = jwtEncoder;
        this.enabled = enabled;
    }
    
    /**
     * Checks if a user has access to devices based on the jwt that is in the cookie or authorization header.
     * If the user is an admin, they have access to all devices.
     * @param req The request used to get the jwt cookie
     * @param resp The response used to set a new cookie with the devices
     * @param refreshDeviceList true if this should go off to find what devices the user has reserved and reset the devices in the cookie
     * @param deviceIds The list of deviceIds to check
     * @throws DeviceNotAuthorizedException
     */
    public void validateUserHasAccessToDevices(HttpServletRequest req, HttpServletResponse resp, boolean refreshDeviceList, String... deviceIds) throws DeviceNotAuthorizedException {
        if (enabled) {
            DawgJwt jwt;
            if (refreshDeviceList) {
                jwt = this.jwtDevicePopulator.assignDevicesToCookie(req, resp);
            } else {
                jwt = this.jwtEncoder.decodeJwt(this.cookieUtils.extractJwt(req));
            }
            if (!jwt.getCreds().getRoles().contains("ROLE_ADMIN")) {
                Set<String> reserved = jwt.getCreds().getDeviceIds();
                for (String deviceId : deviceIds) {
                    if ((reserved == null) || !reserved.contains(deviceId)) {
                        throw new DeviceNotAuthorizedException(jwt.getCreds().getUserName() + " is not authorized to view " + deviceId);
                    }
                }
            }
        }
    }
}
