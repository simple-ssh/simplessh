package simplessh.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplessh.com.dao.PerformData;
import simplessh.com.dao.PerformDataImpl;
import simplessh.com.response.ListMapResponse;

import java.util.*;

/**
 * @author Corneli F.
 *
 * Terminal service
 */

@Service
public class TerminalServices extends PerformDataImpl {
    private SshCommand ssh;

    public TerminalServices(SshCommand ssh) {
        this.ssh = ssh;
    }

    /**
     * execute command
     * @param id
     * @param data
     * @return
     */
    public ListMapResponse executeCommmand(String id, Map<String, String> data) {
        String name = data.getOrDefault("name","");
        String mysql = data.getOrDefault("mysql","");

       Map<String, String> result= ssh.executeMap( (!mysql.isEmpty()?   "mysql_command" :"commandline"), id, name);
         //System.out.println("result:"+result.get("data"));
        ListMapResponse response = new ListMapResponse();
        if(!mysql.isEmpty() && name.toLowerCase(Locale.ROOT).contains("select")){
            response.setRows(extractTheData(result.get("data")));
            response.setResponse(result.get("error"));
        }else {
            response.setRows(new ArrayList<>());
            response.setResponse(result.get("data"));
        }

       return response;
    }


}
