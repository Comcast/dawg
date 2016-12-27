package com.comcast.video.dawg.common.security.service;

import java.util.Set;

import com.comcast.video.dawg.common.exceptions.UserException;
import com.comcast.video.dawg.common.security.model.DawgUser;
import com.comcast.video.dawg.common.security.model.DawgUserAndRoles;

public interface UserService {

    void addUser(DawgUser user, Set<String> roles) throws UserException;
    DawgUserAndRoles getUser(String userId) throws UserException;
    void modifyUser(DawgUser modifications) throws UserException;
    void deleteUser(String userId) throws UserException;
    void changeRoles(String userId, Set<String> addRoles, Set<String> removeRoles) throws UserException;
    Set<String> getUserRoles(String userId) throws UserException;
}
