package simplessh.com.services;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplessh.com.dao.SshAccount;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@Slf4j
public class SaveContentInFileService {

    private KeyStoreService keyService ;

    public SaveContentInFileService(KeyStoreService keyService){
        this.keyService=keyService;
    }

    public String save(List<Map<String, String>> list, String connectionId){
        if(list.size()==0)
            return "";

        SshAccount connData = keyService.getSshAccount(connectionId);
        try {
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            if( !connData.getSshPem().isEmpty() && connData.getSshPass().isEmpty())
                jsch.addIdentity("privateKey.pem", connData.getSshPem().getBytes(), null, null);

            Session session = jsch.getSession(connData.getSshLog(), connData.getSshHost(), 22);
            //set password
            if( !connData.getSshPass().isEmpty())
                session.setPassword(connData.getSshPass());

            session.setConfig(config);

            session.connect(30000);

           list.forEach(e-> {
               String path= e.getOrDefault("path","")+"/"+e.getOrDefault("fileName","");
                      path= path.replaceAll("//","/");
               try {
                //Open shell channel
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                String command = "sudo cat > " + path;
                //(!connData.getSshPass().isEmpty() ? "echo '"+connData.getSshPass()+"' | sudo -S ":"sudo ")+"cat > " + path;
                channel.setCommand(command);//+" && sudo sed -i '$d' "+ path
                channel.connect(3000);

                OutputStream outputStream = channel.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream);
                // Execute command
                String contentWithoutNewline = e.getOrDefault("content","").replaceAll("\\r?\\n$", "");
                writer.println(contentWithoutNewline);
                writer.flush();
                // Close the SSH channel to send the EOF (Ctrl + D) signal
                channel.disconnect();
                writer.close();
               } catch (Exception ex) {
                   log.error("Error save the file:"+ex.getMessage());
               }
                try{ Thread.sleep(300);  }catch(Exception ee){  }
             });
             // remove last line in the file: sudo sed -i '$d' filepath OR sudo sed -i '${/^$/d}' filepath
            session.disconnect();
            log.info("Disconnected");
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("Error save content in "+filePath+":"+e.getMessage());
        }
        return "";
    }

    public String chanelConnect(){

        return "";
    }

}
