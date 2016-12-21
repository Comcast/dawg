package com.comcast.video.dawg.common.security;

public class AuthServerConfig {
	private String ldapHost = "localhost";
	private int ldapPort = 389;
	private String ldapDomain = "dc=dawg,dc=comcast,dc=com";
	private String ldapBindCn = "Manager";
	private String ldapBindPassword;
	private boolean enabled = false;
	
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
	
}
