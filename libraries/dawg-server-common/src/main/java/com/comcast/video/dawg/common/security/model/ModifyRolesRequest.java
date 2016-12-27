package com.comcast.video.dawg.common.security.model;

import java.util.Set;

public class ModifyRolesRequest {
    private Set<String> addRoles;
    private Set<String> removeRoles;
    
    public Set<String> getAddRoles() {
        return addRoles;
    }
    public void setAddRoles(Set<String> addRoles) {
        this.addRoles = addRoles;
    }
    public Set<String> getRemoveRoles() {
        return removeRoles;
    }
    public void setRemoveRoles(Set<String> removeRoles) {
        this.removeRoles = removeRoles;
    }
    
    
}
