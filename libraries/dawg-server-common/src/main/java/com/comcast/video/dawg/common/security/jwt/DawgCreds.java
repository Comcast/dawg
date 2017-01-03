package com.comcast.video.dawg.common.security.jwt;

import java.util.Set;

public class DawgCreds {

    private String userName;
    private String password;
    private Set<String> roles;
    private Set<String> deviceIds;
    
    public DawgCreds() {
        
    }
    
    public DawgCreds(String userName, String password, Set<String> roles) {
        this(userName, password, roles, null);
    }
    
    public DawgCreds(String userName, String password, Set<String> roles, Set<String> deviceIds) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.deviceIds = deviceIds;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    public Set<String> getDeviceIds() {
        return deviceIds;
    }
    public void setDeviceIds(Set<String> deviceIds) {
        this.deviceIds = deviceIds;
    }
    
    
}
