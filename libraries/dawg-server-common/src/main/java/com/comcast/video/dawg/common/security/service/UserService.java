package com.comcast.video.dawg.common.security.service;

import java.util.Set;

import com.comcast.video.dawg.common.exceptions.UserException;
import com.comcast.video.dawg.common.security.model.DawgUser;

public interface UserService {

	void addUser(DawgUser user, Set<String> roles) throws UserException;
}
