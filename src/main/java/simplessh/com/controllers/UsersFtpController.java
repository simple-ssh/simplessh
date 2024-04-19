package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.UsersFtpServices;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Corneli F.
 *
 * System (Ubuntu or any other system users)
 * Users controller
 *
 * this users controller are for email user for ftp and your system there are one user for all this 3
 */
@RestController
@RequestMapping("/api/v1/")
public class UsersFtpController {
    @Autowired
    private UsersFtpServices service;

    /**
     * get list of your system users
     * @param id
     * @return
     */
    @GetMapping("/get-list-of-users")
    public List<Map<String,String>> getListOfUsers(@RequestHeader("id") String id) {
        return service.getListOfUsers(id);
    }

    /**
     * remove users of your system
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/add-remove-user-from-sudo")
    public String addRemoveFromSudo(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.addRemoveFromSudo(id,request);
    }

    /**
     * change password of your system user
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/user-change-password" , consumes = "application/json", produces = "application/json")
    public String changePassword(@RequestHeader("id") String id,
                                 @RequestBody Map<String, String> data ) {

        return service.changePassword(id,data);
      }

    /**
     * change path of your system user
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/user-change-path" , consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> changePath(@RequestHeader("id") String id, @RequestBody Map<String, String> data ) {

        return service.changePath(id,data);
    }

    /**
     * remove user from your system
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-user")
    public List<Map<String,String>> removeUser(@RequestHeader("id") String id, HttpServletRequest request ) {

      return service.removeUser(id,request);
    }

    /**
     * add new user to your system
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/add-new-user" , consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> addUser(@RequestHeader("id") String id, @RequestBody Map<String, String> data ) {

       return service.addUser(id,data);
    }


}
