package com.comcast.video.dawg.common.security;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The base authentication server config
 * @author Kevin Pearson
 *
 */
public class AuthServerConfig {
	private String mode = "none";
	private String jwtSecret;
	private long jwtTtl = TimeUnit.HOURS.toMillis(1);
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

    public long getJwtTtl() {
        return jwtTtl;
    }

    public void setJwtTtl(long jwtTtl) {
        this.jwtTtl = jwtTtl;
    }
}
