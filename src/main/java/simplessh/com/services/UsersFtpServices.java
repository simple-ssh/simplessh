package simplessh.com.services;

import org.springframework.stereotype.Service;
import simplessh.com.dao.Data;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 *
 * System (Ubuntu or any other system users)
 * Users services
 *
 * this user services are for ftp and your system there are one user for all
 */
 @Service
public class UsersFtpServices extends SshCommand{
   /**
     * get list of your system users
     * @param id
     * @return
     */

    public List<Map<String,String>> getListOfUsers(String id) {
        return getUsersList(id);
    }

    /**
     * remove users of your system
     * @param id
     * @param request
     * @return
     */
   public String addRemoveFromSudo(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        String type = request.getParameter("type");

        String result = execute((type.contains("add") ? "make_user_sudoer" :"remove_user_sudo"), id, name);

        return result;
    }

    /**
     * change password of your system user
     * @param id
     * @param data
     * @return
     */
    public String changePassword(String id, Map<String, String> data ) {
        String name = data.getOrDefault("name","");
        String password = data.getOrDefault("password","");

         execute("set_password_to_ftp_account", id, name, password);
        return "Password Changed!";
      }

    /**
     * change path of your system user
     * @param id
     * @param data
     * @return
     */
    public List<Map<String,String>> changePath(String id, Map<String, String> data ) {
        String name = data.getOrDefault("name","");
        String path = data.getOrDefault("path","");

        execute("ftp_set_directory", id, path, name.trim());
        return getUsersList(id);
    }

    /**
     * remove user from your system
     * @param id
     * @param request
     * @return
     */

    public List<Map<String,String>> removeUser(String id, HttpServletRequest request) {
        String name = request.getParameter("name");

        execute("remove_ftp_account", id, name.trim());
        return getUsersList(id);
    }

    /**
     * add new user to your system
     * @param id
     * @param data
     * @return
     */
    public List<Map<String,String>> addUser(String id, Map<String, String> data ) {
        String name = data.getOrDefault("name","");
        String password = data.getOrDefault("password","");
        String path = data.getOrDefault("path","");

         Data [] params =    new Data[4];
         params[0] = new Data("add_ftp_account", name);
         params[1] = new Data("set_password_to_ftp_account", name, password);
         if(path != null && !path.isEmpty())
         params[2] = new Data("ftp_set_directory", path, name);

         params[3] = new Data("add_user_to_group",name, "www-data");

         executeAll(id, Arrays.stream(params).filter(Objects::nonNull).toArray(Data[]::new));

         return getUsersList(id);
    }

    /**
     * will get the users list and put in this format List<Map<String,String>>
     * @param id
     * @return
     */
    private List<Map<String,String>> getUsersList(String id){
        String data = execute("view_ftp_account", id);
        return  Arrays.stream(data.split("\\r?\\n")).
                 map(st ->st.split(":")).
                 map(st-> Map.of( "name", st[0],"path", st.length>1 ? st[1]:"")).
                 collect(Collectors.toList());
    }
}
