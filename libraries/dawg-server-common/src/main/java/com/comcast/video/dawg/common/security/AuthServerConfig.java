package com.comcast.video.dawg.common.security;

import java.util.Set;

/**
 * The base authentication server config
 * @author Kevin Pearson
 *
 */
public class AuthServerConfig {
	private String mode = "none";
	private String jwtSecret;
	private Set<String> corsDomains = null;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Set<String> getCorsDomains() {
        return corsDomains;
    }

    public void setCorsDomains(Set<String> corsDomains) {
        this.corsDomains = corsDomains;
    }
}
