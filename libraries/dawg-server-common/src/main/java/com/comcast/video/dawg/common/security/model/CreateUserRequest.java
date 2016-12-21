package com.comcast.video.dawg.common.security.model;

import java.util.Set;

public class CreateUserRequest extends DawgUser {
	private Set<String> roles;

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
}
