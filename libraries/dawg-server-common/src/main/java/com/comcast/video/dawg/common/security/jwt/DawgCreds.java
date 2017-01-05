package com.comcast.video.dawg.common.security.jwt;

import java.util.Set;

/**
 * Crdentials that identify a dawg user.
 * This will be encoded in the jwt and used as authentication
 * @author Kevin Pearson
 *
 */
public class DawgCreds {

    private String userName;
    private String password;
    private Set<String> roles;
    private Set<String> deviceIds;
    
    public DawgCreds() {
        
    }
    
    /**
     * Creates a DawgCreds
     * @param userName The name of the user
     * @param password The user's password
     * @param roles The roles that the user belongs to
     */
    public DawgCreds(String userName, String password, Set<String> roles) {
        this(userName, password, roles, null);
    }
    
    /**
     * Creates a DawgCreds
     * @param userName The name of the user
     * @param password The user's password
     * @param roles The roles that the user belongs to
     * @param deviceIds The ids of the devices that the user has access to
     */
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
