package simplessh.com.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import simplessh.com.config.JwtUtils;
import simplessh.com.request.LoginRequest;
import simplessh.com.response.JwtResponse;
import simplessh.com.services.KeyStoreService;
import simplessh.com.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Corneli F.
 *
 * User controller
 */
@RestController
@RequestMapping("/api/")
public class UserController {
    @Autowired
    private UserServices services;


    /**
     * login page
     * @param request
     * @return
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    /**
     *
     * @param user
     * @return
     */
    @PostMapping(path = "/signin" , consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> singin(@RequestBody LoginRequest user) {

       return services.singin(user);
    }



    /**
     * get system users
     * @return
     */
    @GetMapping("/v1/get-system-users")
    public LinkedHashMap<String,String> get() {

        return services.get();
    }

    /**
     * Add or change password for the user
     * @param data
     * @return
     */
    @PutMapping(path = "/v1/add-change-system-users" , consumes = "application/json", produces = "application/json")
    public LinkedHashMap<String,String> addUser(@RequestBody Map<String, String> data) {

        return services.addUser(data);
    }



    /**
     * Remove User by username
     * @param request
     * @return
     */
    @DeleteMapping("/v1/remove-system-user")
    public LinkedHashMap<String,String> removeUser(HttpServletRequest request) {

       return services.removeUser(request);
    }




}
