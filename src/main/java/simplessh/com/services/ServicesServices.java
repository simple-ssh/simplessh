package simplessh.com.services;

import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Corneli F.
 *
 * Service
 */
@Service
public class ServicesServices{
    private SshCommand ssh;

    public ServicesServices(SshCommand ssh) {
        this.ssh = ssh;
    }

    /**
     * get list of services
     * @param id
     * @return
     */
    public List<Map<String,String>> getList(String id) {
         //open the connection
        return getDataList(id);
    }

    /**
     * service action like disable or enable or activate
     * @param id
     * @param request
     * @return
     */
    public List<Map<String,String>> actionData(String id, HttpServletRequest request) {
        String actionBtn = request.getParameter("actionBtn");
        String name = request.getParameter("name");
        if(name == null)
            return new ArrayList<>();

        name = name.replace(".service","");


        if(actionBtn.contains("disable")) {
            ssh.execute("stop_services", id, name);
            //try{Thread.sleep(1000);}catch (Exception e){}
            //ssh. ssh.execute(id,"disable_services", new String[]{name}, true,false);

        } else if (actionBtn.contains("enable")) {
            String is=  ssh.execute("is_enabled_service", id, name);
            if(is.contains("enabled")) {
                ssh.execute( "enable_services", id, name);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
            ssh.execute("start_services", id, name);
        }else if (actionBtn.contains("restart")) {

            ssh.execute("restart_services", id, name);
        }else if (actionBtn.contains("remove")) {
            ssh.execute("remove_services", id, name);
            //ssh.execute("remove_file", new String[]{"/etc/systemd/system/"+name}, false,false,false);
        }

       return getDataList(id);
   }

    /**
     *  service action like disable or enable or activate
     * @param id
     * @param request
     * @return
     */
    public String actionService(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        String action = request.getParameter("actionService");
        return  ssh.execute(action+"_services", id, name);
    }

    /**
     * get service data
     * @param id
     * @param request
     * @return
     */
    public String getServiceData(String id, HttpServletRequest request) {
        String name = request.getParameter("name");

        return  ssh.execute("get_file_content", id, "/etc/systemd/system/"+name);
    }

    /**
     * show status of service
     * @param id
     * @param request
     * @return
     */
    public String showStatus(String id, HttpServletRequest request ) {
      String name = request.getParameter("name");
      String service =  ssh.execute("status_services", id, name);
      return service.replace("Active: active", "<spam style=\"color:green;\">Active: active</spam>").
              replace("Active: inactive","<spam style=\"color:red;\">Active: inactive</spam>").
              replace("Active: failed","<spam style=\"color:red;\">Active: failed</spam>");
   }

    /**
     * add new serrvice
     * @param id
     * @param data
     * @return
     */
    public List<Map<String,String>> addNewOne(String id, Map<String, String> data) {
        String name = data.getOrDefault("name","");
        String description = data.getOrDefault("description","");
        String runCode = data.getOrDefault("runcode","/var/www/");

        String fileConten="[Unit]\n" +
                "Description="+description+"\n" +
                "After=syslog.target\n" +
                "After=network.target[Service]\n" +
                "User=username\n" +
                "Type=simple\n" +
                "\n" +
                "[Service]\n" +
                "ExecStart="+runCode+"\n" +
                "Restart=always\n" +
                "StandardOutput=syslog\n" +
                "StandardError=syslog\n" +
                "SyslogIdentifier="+name+"\n" +
                "\n" +
                "[Install]\n" +
                "WantedBy=multi-user.target";


         ssh.execute("put_content_in_file_simple", id, fileConten, "/etc/systemd/system/"+name+".service");
        try{Thread.sleep(1000);}catch (Exception e){}
         ssh.execute("start_services", id, name+".service" );
        try{Thread.sleep(1000);}catch (Exception e){}
         ssh.execute("enable_services", id, name+".service");

        try{Thread.sleep(1000);}catch (Exception e){}
        return getDataList(id);
      }



    /**
     * ger list of all services
     * @param id
     * @return
     */
    private List<Map<String,String>> getDataList(String id){
        String data =  ssh.execute("show_services",id);
        data = data.replaceAll(" +"," ") ;

        List<String> yourList = getYourList(id);
        return Arrays.stream(data.split("\\r?\\n")).
                filter(st->st.contains(" ")).
                map(line -> line.split(" ")).
                filter(parts -> parts.length>4 && parts[1].contains(".service")).
                map(e -> {
                    StringJoiner description = new StringJoiner(" ");
                    for (int j=5; j<e.length; j++)
                        description.add(e[j]);

                    return Map.of("name",        e[1],
                                 "description", description.toString(),
                                 "status",      e[2] + "; " + e[3] + "; " + e[4],
                                 "allow",        yourList.contains(e[1]) ? "yes":"");
                }).collect(Collectors.toList());
    }

    /**
     * get your services, the services you added, actually it save in /etc/systemd/system/
     * @param id
     * @return
     */
    private List<String> getYourList(String id){
        String fileList =  ssh.execute("show_folder_content_ls_short_and_full", id, "/etc/systemd/system/");
        //System.out.println(fileList);

        String[] split = fileList.split("@@@@@@");
        if(split.length<2)
            return Collections.EMPTY_LIST;

        String[] listSplitShort = split[0].split("\\r?\\n");
        String[] listSplitLong = split[1].split("\\r?\\n");


      return IntStream.range(0, listSplitShort.length)
                .boxed()
                .filter(j->listSplitShort[j].contains(".service") &&
                           listSplitLong.length > j+2 &&
                          !listSplitLong[j+2].isEmpty() &&
                           listSplitLong[j+2].substring(0, 1).equals("-"))
                .map(j->listSplitShort[j])
                .collect(Collectors.toList());

    }

}
