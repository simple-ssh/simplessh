package simplessh.com.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import simplessh.com.dao.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Corneli F.
 *
 * This service is for authorisate the user
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private KeyStoreService keyStoreService;

    /**
     * get user by username from keystore
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       /*Here we are using dummy data, you need to load user data from
     database or other third party application*/
        User user = findUserbyUername(username);

        UserBuilder builder ;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles(user.getRoles());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }

    /**
     * implimentation
     * @param username
     * @return
     */
    private User findUserbyUername(String username) {

        String json = keyStoreService.getKeyStoreValue("users");

        Map<String,String> users = new HashMap<>();

        try {
            Type type = new TypeToken<Map<String,String>>(){}.getType();
            users = (new Gson()).fromJson(json, type);
        }catch (Exception e){
            System.out.println("error to convert:"+e.getMessage());
        }

        if(users.size()>0 && users.containsKey(username)) {
            return new User(username,  users.get(username), "ADMIN");
        }
        return null;
    }
}
