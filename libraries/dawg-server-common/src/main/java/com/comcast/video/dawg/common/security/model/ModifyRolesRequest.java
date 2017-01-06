package com.comcast.video.dawg.common.security.model;

import java.util.Set;

/**
 * A request to modify roles that are assigned to a user.
 * This allows adding or removing roles.
 * @author Kevin Pearson
 *
 */
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
