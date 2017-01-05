package com.comcast.video.dawg.common.security.service;

import java.util.Set;

import com.comcast.video.dawg.common.exceptions.UserException;
import com.comcast.video.dawg.common.security.model.DawgUser;
import com.comcast.video.dawg.common.security.model.DawgUserAndRoles;

/**
 * Service for handling getting, modifying, creating of dawg users
 * @author Kevin Pearson
 *
 */
public interface UserService {

    /**
     * Creates a user 
     * @param user The user to create
     * @param roles The roles to assign to the user
     * @throws UserException
     */
    void addUser(DawgUser user, Set<String> roles) throws UserException;
    
    /**
     * Retrieves the user by user id
     * @param userId The id of the user to retrieve
     * @return
     * @throws UserException
     */
    DawgUserAndRoles getUser(String userId) throws UserException;
    
    /**
     * Makes modifications to a user
     * @param modifications The modifications to make, anything set to null will not change.
     * @throws UserException
     */
    void modifyUser(DawgUser modifications) throws UserException;
    
    /**
     * Deletes a user by the given id
     * @param userId The id of the user to delete
     * @throws UserException
     */
    void deleteUser(String userId) throws UserException;
    
    /**
     * Changes which roles the user identified by userId belong to that user
     * @param userId The id of the user to change the roles for
     * @param addRoles The roles to add to the user
     * @param removeRoles The roles to remove from the user
     * @throws UserException
     */
    void changeRoles(String userId, Set<String> addRoles, Set<String> removeRoles) throws UserException;
    
    /**
     * Retrieves the roles that the user identified by userId belong to
     * @param userId The id of the user to get the roles for
     * @return
     * @throws UserException
     */
    Set<String> getUserRoles(String userId) throws UserException;
}
