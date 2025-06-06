package simplessh.com.services;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import simplessh.com.config.JwtUtils;
import simplessh.com.request.LoginRequest;
import simplessh.com.response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import simplessh.com.services.KeyStoreService;

/**
 * @author Corneli F.
 *
 * User Services
 */
@Service
public class UserServices {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private  KeyStoreService keyStoreService;

   /**
     *
     * @param user
     * @return
     */

    public ResponseEntity<?> singin(LoginRequest user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        String jwt = jwtUtils.generateJwtToken(loggedInUser.getName());

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer",
                loggedInUser.getName(),
                null));
    }

  /**
     * get system users
     * @return
     */
   public LinkedHashMap<String,String> get() {
          return keyStoreService.getUsers();
    }

    /**
     * Add or change password for the user
     * @param data
     * @return
     */

    public LinkedHashMap<String,String> addUser(Map<String, String> data) {
        LinkedHashMap<String,String> users = keyStoreService.getUsers();

        String userName = data.getOrDefault("username","");
        String password = data.getOrDefault("password","");

        users.put(userName, new BCryptPasswordEncoder().encode(password));

        keyStoreService.setKeyStoreValue("users", (new Gson()).toJson(users));
        return users;
    }

   /**
     * Remove User by username
     * @param request
     * @return
     */

    public LinkedHashMap<String,String> removeUser(HttpServletRequest request) {
         LinkedHashMap<String,String> users = keyStoreService.getUsers();
         String userName = request.getParameter("username");
         users.remove(userName);
         keyStoreService.setKeyStoreValue("users", (new Gson()).toJson(users));
         return users;
    }

}
