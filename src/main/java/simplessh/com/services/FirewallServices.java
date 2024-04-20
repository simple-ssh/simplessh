package simplessh.com.services;



import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Corneli F.
 */
@Service
public class FirewallServices extends SshCommand{
    

    /**
     * get list of firewall
     * @param id
     * @return
     */
    public List<Map<String,String>> getList(String id) {
        return getDataList(id);
    }

    /**
     * enable disable firewall
     * @param id
     * @param request
     * @return
     */
    public List<Map<String,String>> actionData(String id, HttpServletRequest request ) {
        String actionBtn = request.getParameter("actionBtn");

        if(actionBtn.contains("disable")) {
            execute("firewall_disable", id);
            return new ArrayList<>();
         }

         execute("commandline", id, "ufw allow '22/tcp'; ufw allow '80/tcp'; ufw allow '443/tcp'; ufw allow 'Bind9'; ufw allow 'Nginx HTTP'; ufw allow '2525/tcp'");
         execute("firewall_enable", id);
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
        String dowhat = request.getParameter("dowhat");
         if(name.contains(",")) {
            StringBuilder newName = new StringBuilder("");
            Stream.of(name.split(",")).forEach(e -> newName.append("ufw "+dowhat+" '" + e + "'; "));
            execute("commandline", id,newName.toString() );
        }else{
            execute("commandline", id, "ufw "+dowhat+" '" + name + "'");
          }
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
        String idUfw = request.getParameter("id_ufw");

        execute("firewall_remove_rule", id, name, idUfw );
        return getDataList(id);
    }


    /**
     * firewall list
     * @param id
     * @return
     */
    private List<Map<String,String>> getDataList(String id){
       String data = execute("firewall_list", id );
              data = data.replaceAll("ALLOW","@ALLOW - ")
                         .replaceAll("DENY", "@DENY - ")
                         .replaceAll("DISABLED", "@DISABLED - ")
                         .replaceAll("]", "@")
                         .replaceAll("\\[", "")
                         .replaceAll(" +", " ");

         return  Arrays.stream(data.split("\\r?\\n")).
                        filter(st->st.contains("@")).
                        map(st ->st.split("@")).
                        map(st-> Map.of("id",   st[0],
                                         "name", st.length>0 ? st[1] :"",
                                         "type", st.length>1 ? st[2]:"")
                        ).collect(Collectors.toList());
    }
}
