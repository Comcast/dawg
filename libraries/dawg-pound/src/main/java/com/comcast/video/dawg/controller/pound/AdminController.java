package com.comcast.video.dawg.controller.pound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.video.dawg.common.exceptions.UserException;
import com.comcast.video.dawg.common.security.model.DawgUserAndRoles;
import com.comcast.video.dawg.common.security.model.DawgUser;
import com.comcast.video.dawg.common.security.model.ModifyRolesRequest;
import com.comcast.video.dawg.common.security.service.UserService;


@RequestMapping("/admin")
@Controller
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/user/{userId}", method = { RequestMethod.PUT } )
    @ResponseBody
    public void createUser(@PathVariable String userId, @RequestBody DawgUserAndRoles user) throws UserException {
        user.setUid(userId);
        userService.addUser(user, user.getRoles());
    }
    
    @RequestMapping(value="/user/{userId}", method = { RequestMethod.GET } )
    @ResponseBody
    public DawgUserAndRoles getUser(@PathVariable String userId) throws UserException {
        return userService.getUser(userId);
    }
    
    @RequestMapping(value="/user/{userId}", method = { RequestMethod.POST } )
    @ResponseBody
    public void modifyUser(@PathVariable String userId, @RequestBody DawgUser modifications) throws UserException {
        modifications.setUid(userId);
        userService.modifyUser(modifications);
    }
    
    @RequestMapping(value="/user/{userId}/roles", method = { RequestMethod.POST } )
    @ResponseBody
    public void modifyUser(@PathVariable String userId, @RequestBody ModifyRolesRequest modifications) throws UserException {
        userService.changeRoles(userId, modifications.getAddRoles(), modifications.getRemoveRoles());
    }
    
    @RequestMapping(value="/user/{userId}", method = { RequestMethod.DELETE } )
    @ResponseBody
    public void deleteUser(@PathVariable String userId) throws UserException {
        userService.deleteUser(userId);
    }

}
