package simplessh.com.services;

import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 */
@Service
public class GroupsServices{
    private SshCommand ssh;

    public SshCommand getSsh() {
        return ssh;
    }

    public void setSsh(SshCommand ssh) {
        this.ssh = ssh;
    }

    /**
     * get list of firewall
     * @param id
     * @return
     */
    public List<Map<String,String>> getList(String id) {
       return getDataList(id);
    }

    /**
     * add new rule to firewall
     * @param id
     * @param request
     * @return
     */
    public List<Map<String,String>> addNewOne(String id, HttpServletRequest request) {
         String name = request.getParameter("name");

         ssh.execute("add_new_group", id, name);
        return getDataList(id);
    }

    /**
     * remove rule from firewall
     * @param id
     * @param request
     * @return
     */
    public List<Map<String,String>> remove(String id, HttpServletRequest request ) {
        String name = request.getParameter("name");

         ssh.execute("remove_group", id, name);
        return getDataList(id);
    }


    /**
     * firewall list
     * @param id
     * @return
     */
    private List<Map<String,String>> getDataList(String id){
        String data = ssh.execute("list_of_groups", id);

        return  Arrays.stream(data.split("\\r?\\n")).
                       map(st-> Map.of("name",  st)).
                       collect(Collectors.toList());
    }
}
