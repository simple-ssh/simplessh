package simplessh.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 */
@Service
public class SubGroupsServices{

    private SshCommand ssh;

    public SubGroupsServices(SshCommand ssh) {
        this.ssh = ssh;
    }

    /**
     * get list of firewall
     * @param id
     * @return
     */
    public List<Map<String,String>> getList(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        return getDataList(id, name);
    }



    /**
     * add new rule to firewall
     * @param id
     * @param request
     * @return
     */
    public List<Map<String,String>> addNewOne(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        String user = request.getParameter("user");

        ssh.execute("add_user_to_group", id, user,name);
        return getDataList(id, name);
    }

    /**
     * remove rule from firewall
     * @param id
     * @param request
     * @return
     */
    public List<Map<String,String>> remove(String id, HttpServletRequest request ) {
        String name = request.getParameter("name");
        String user = request.getParameter("user");

        ssh.execute("remove_user_from_group", id, user, name);
        return getDataList(id, name);
    }


    /**
     * firewall list
     * @param id
     * @return
     */
    private List<Map<String,String>> getDataList(String id, String name){
        String data = ssh.execute("list_of_users_in_group", id, name);

        return  Arrays.stream(data.split(",")).
                filter(st->!st.trim().isEmpty()).
                map(st-> Map.of("name",  st)).
                collect(Collectors.toList());

    }
}
