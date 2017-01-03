package com.comcast.video.dawg.common.security;

import java.util.Set;

public class AuthServerConfig {
	private String ldapHost = "localhost";
	private int ldapPort = 389;
	private String ldapDomain = "dc=dawg,dc=comcast,dc=com";
	private String ldapBindCn = "Manager";
	private String ldapBindPassword;
	private boolean enabled = false;
	private String jwtSecret;
	private Set<String> corsDomains = null;
	
	public String getLdapHost() {
		return ldapHost;
	}
	
	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}
	
	public int getLdapPort() {
		return ldapPort;
	}
	
	public void setLdapPort(int ldapPort) {
		this.ldapPort = ldapPort;
	}
	
	public String getLdapDomain() {
		return ldapDomain;
	}
	
	public void setLdapDomain(String ldapDomain) {
		this.ldapDomain = ldapDomain;
	}
	
	public String getLdapBindCn() {
		return ldapBindCn;
	}
	
	public void setLdapBindCn(String ldapBindCn) {
		this.ldapBindCn = ldapBindCn;
	}
	
	public String getLdapBindPassword() {
		return ldapBindPassword;
	}
	
	public void setLdapBindPassword(String ldapBindPassword) {
		this.ldapBindPassword = ldapBindPassword;
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
