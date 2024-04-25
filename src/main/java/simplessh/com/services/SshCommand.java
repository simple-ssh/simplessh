package simplessh.com.services;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplessh.com.Helpers;
import simplessh.com.Variables;
import simplessh.com.dao.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 */

@Slf4j
@Service
public class SshCommand {

    protected KeyStoreService keyStoreService ;
    public SshCommand(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }

    private static Map<String, Session> connections = new ConcurrentHashMap<>();
   
    
    private Session getSession(SshAccount account){
        Session session = null;
         try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            // here generate session by .pem file
            if( !account.getSshPem().isEmpty() && account.getSshPass().isEmpty())
              jsch.addIdentity("privateKey.pem", account.getSshPem().getBytes(), null, null);


             session = jsch.getSession(account.getSshLog(), account.getSshHost(), 22);
             // here generate session by password file
            if(!account.getSshPass().isEmpty())
               session.setPassword(account.getSshPass());

            session.setConfig(config);
            //session.setServerAliveInterval(3600000);
            session.connect();

            log.info("Connected to "+account.getSshHost());

        } catch (Exception e) {
             log.error("Error generating session:"+e.getMessage());
             System.out.println("Error generating session: " + e.getMessage());
         }

        return session ;
    }

    /**
     * if session is not connection then we add it,
     * if session is not connect than we connect and replace it
     * https://stackoverflow.com/questions/16127200/jsch-how-to-keep-the-session-alive-and-up
     * @param sshAccount
     * @param idConnection
     */
    private void addSession(SshAccount sshAccount,  String idConnection){
        if(!connections.containsKey(idConnection)){
            connections.put(idConnection, getSession(sshAccount));
            log.info("Generate new session!" );
        }else{
            Session session = connections.get(idConnection);
            if(session == null || !session.isConnected()){
                connections.put(idConnection, getSession(sshAccount));
                log.info("New session was generated, because the old one was down!" );
            }

        }
    }


    /**
     *
     * @param idConnection
     * @param datas
     * @return
     */
    public String executeAll(String idConnection, Data... datas) {
        Arrays.stream(datas).forEach(v->{
             execute(v.getCommandName(), idConnection, v.getParams());
            try { Thread.sleep(2000); } catch (Exception e) { }
        });

        return "";
    }



    public String execute(String command, String idConnection, String... array ){
        Map<String, String> execute = executeMap(command, idConnection, array);

        String error                = execute.getOrDefault("error","");
        error                       = (error.toLowerCase(Locale.ROOT).contains("sudo") ||
                                      error.toLowerCase(Locale.ROOT).contains("warning") ||
                                      error.isEmpty()) &&
                                      !error.toLowerCase(Locale.ROOT).contains("sql syntax") &&
                                      !error.toLowerCase(Locale.ROOT).contains("at line")  ? "": error+"\n";

        return error+execute.getOrDefault("data","");
    }

    /**
     * execute comand it self
     * @param command
     * @param array
     * @param idConnection
     * @return
     */
    public Map<String, String> executeMap(String command, String idConnection, String... array ){

        SshAccount sshAccount = keyStoreService.getSshAccount(idConnection);
        //add session if is not
        addSession(sshAccount, idConnection);


        String commandName = command;
        command = command(sshAccount.getPlatform(), command,
                (commandName.contains("mysql_") ? Helpers.createMysql(sshAccount.getMysqlPass(), array) : array));


        if((commandName.contains("mysql_") || commandName.contains("mysqldump")) &&
                (sshAccount.getMysqlPass() == null || sshAccount.getMysqlPass().isEmpty())){
            command = command.replace("mysql -u  -p''","/usr/bin/mysql -u root");
            command = command.replace("mysql -u root -p''","/usr/bin/mysql -u root");
            command = command.replace("mysql -u mysqluser -p''","/usr/bin/mysql -u root");
            command = command.replace("mysqldump -u mysqluser -p''","/usr/bin/mysqldump -u root");
        }


        if(commandName.contains("mysql_"))
           command = command.replace("mysqluser", sshAccount.getMysqlLog());

        log.info("Command: echo '****' | sudo -S "+command);

        command = (!sshAccount.getSshPass().isEmpty() ? "echo '"+sshAccount.getSshPass()+"' | ":"")+"sudo -S "+command;

        StringBuilder returnComandData = new StringBuilder();
        Map<String, String> result = new HashMap<>();

        ChannelExec channel = null;
        CompletableFuture<String> cf ;
        String error="";

        Session session = connections.get(idConnection);
        try{
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);
            InputStream in = channel.getInputStream();
            // channel.setPty(true);
            channel.connect(60000);


            InputStream inErr =  channel.getErrStream();
            cf = CompletableFuture.supplyAsync(() -> Helpers.inputStreamToString(inErr,"err")).orTimeout(1, TimeUnit.SECONDS);

            while(true){
                returnComandData.append(Helpers.inputStreamToString(in,""));

                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }

                try{ Thread.sleep(1000);  }catch(Exception ee){  }
            }
            error = cf!=null? cf.get(): "";
            in.close();
            log.info("Command executed successful." );
            if (channel != null)
                channel.disconnect();
        } catch(Exception e){
            log.info("Error when execute command: "+e.getMessage());
        } finally {
            //if(close) disconnect(connection.getSession());
            if (channel != null)  channel.disconnect();
        }

        result.put("data", returnComandData.toString());
        result.put("error", error);

        return  result;
    }


    /**
     * Generate command
     * @param platform
     * @param name
     * @param array
     * @return
     */
    public String command(String platform, String name, String[] array)  {
        try {
            String fileName = platform.isEmpty() ? "Ubuntu" : platform;
            Map<String,String> map = Variables.map.get(fileName);

            String command = map.getOrDefault(name,"");

            if(!command.isEmpty() && array !=null && array.length >0){
                for (int i = 0; i <array.length ; i++) {
                    command = command.replace("%"+i, array[i]);
                }
            }

            return command;
        }catch (Exception e){
            log.error("Error file:"+e);
        }
        return "";
    }

    /**
     * disconnect
     * @param session
     */
    public void disconnect(Session session){
        try{
            session.disconnect();
            log.info("Disconnected");
        }catch (Exception e){
            log.error("Error disconnected: "+e.getMessage());
        }
    }


    /**
     * will download the file and get the content of the file and put in string
     * @param remoteFile
     * @param idConnection
     * @return
     */
    public String getStringFileContent(String remoteFile, String idConnection){
        DownloadFile download = downloadFileStream(remoteFile, idConnection);
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(download.getFile()))) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error reading file: " + e);
            return "Error reading file!";
        } finally {
            // Make sure to close the InputStream
            // Make sure to close resources and disconnect channels
            try {
                download.getFile().close();
            } catch (IOException e) {
                log.error("Error closing input stream: " + e);
            }

            if(download.getChannelSftpDownload() != null){
                download.getChannelSftpDownload().disconnect();
            }

            if(download.getChannelDownload() != null){
                download.getChannelDownload().disconnect();
            }
        }

        boolean reverseContent = remoteFile.endsWith(".log");
        // Reverse the content if the file ends with ".log"
        if(reverseContent) {
            Collections.reverse(lines);
        }

        return String.join("\n", lines);

        /*DownloadFile download = downloadFileStream(remoteFile, idConnection);

        InputStream inp = download.getFile();
        StringBuffer buf = new StringBuffer();
        try {
            InputStream file = new BufferedInputStream(inp);
            int c;
            while ((c = file.read()) != -1) {
                buf.append((char) c);
            }
        }catch (Exception e){
            log.error("Error read file:"+e);
        }

        if(download.getChannelDownload() != null){
            download.getChannelDownload().disconnect();
        }

        if(download.getChannelSftpDownload() != null){
            download.getChannelSftpDownload().disconnect();
        }

        return buf.toString();*/
    }


    /**
     * upload file to the server
     *@param idConnection - id connections
     *@param files - {"filename.txt":InputStream}
     *@param path: /eyc/postfix/
     *@param owner: root
     *@param permission : 644
     */
    public void sftpUpload(String idConnection, Map<String, InputStream> files, String path,
                           String owner, String permission){

        String checkDir=  execute("check_if_directory_exist", idConnection, "/var/easyvps/" );

        if(!checkDir.contains("yes"))
            executeAll(idConnection, new Data("new_directory","/var/easyvps/"),
                                     new Data("only_folder_permision","775", "/var/easyvps/") );


        uploadFile(files,  "/var/easyvps/", idConnection );

        try{ Thread.sleep(1000);  }catch(InterruptedException ee){
            System.out.println(ee.getMessage());
        }

        // move accept multiple files separated by coma like {f_path1, f_path2,...} we put it here

        String dest = files.size()>1?  "{/var/easyvps/"+String.join(",/var/easyvps/", files.keySet())+"} " :
                "/var/easyvps/"+ files.keySet().toArray()[0];

        String newPath   = path.endsWith("/") ? path : path+"/";
        String destFinal = files.size()>1? newPath+String.join("  "+newPath, files.keySet()) :
                newPath+files.keySet().toArray()[0];

        executeAll(idConnection, new Data("move",dest, path),
                                 new Data( (owner.contains(":")? "update_file_set_owner" :"add_path_file_to_group_perm"), owner, destFinal, permission) );
    }

    /**
     * donload file like a inputstream
     * @param remoteFile
     * @param idConnection
     * @return
     */
    public DownloadFile downloadFileStream(String remoteFile, String idConnection){

        SshAccount sshAccount = keyStoreService.getSshAccount(idConnection);
        //add session if is not
        addSession(sshAccount, idConnection);

        Session session = connections.get(idConnection);
        Channel channelDownload = null;
        ChannelSftp channelSftpDownload = null;
        try{
            channelDownload=session.openChannel("sftp");
            channelDownload.connect();
            channelSftpDownload = (ChannelSftp) channelDownload;

            InputStream inp= channelSftpDownload.get(remoteFile);
            return new DownloadFile(channelDownload, channelSftpDownload, inp) ;
        }catch(Exception e){
            log.error("Error Run Download:"+e);
            disconnectSFTP(channelDownload, channelSftpDownload);
        }

        return new DownloadFile(channelDownload, channelSftpDownload, null);
    }

    /**
     * upload file to server
     * @param files
     * @param path
     * @param idConnection
     * @return
     */
    public String uploadFile(Map<String, InputStream> files, String path, String idConnection ){

        Session session          = connections.get(idConnection);
        Channel channel          = null;
        ChannelSftp channelSftp  = null;
        try{
            channel      = session.openChannel("sftp");
            channel.connect();
            channelSftp  = (ChannelSftp) channel;
            channelSftp.cd(path);
            for(Map.Entry<String, InputStream> entry : files.entrySet()){
                channelSftp.put(entry.getValue(), entry.getKey());
            }

            log.info("File uploaded.");
        }catch(Exception e){
            log.error("Error uploadFile: "+e );
        }finally {
            if (channel != null)  channel.disconnect();
            if (channelSftp != null)  {
                channelSftp.disconnect();
            }
        }
        return "";
    }

    /**
     *@param id - id connections
     *@param files - {"filename.php":InputStream}
     *@param path: /eyc/postfix/
     *@param owner: root
     *@param permission : 644
     */
    public void sftpFastUpload(String id, Map<String, InputStream> files, String path,
                               String owner, String permission){

        uploadFile(files, path, id);

        //if(!owner.isEmpty() && !permission.isEmpty())
         //  ssh.execute("add_path_file_to_group_perm", new String[]{owner, path, permission}, id );
    }

    /**
     * will check if path /var/easyvps exist if not than create it
     */
    public void checkForVarEasyvpsPath(String idConnection){
        String checkDir =  execute("check_if_directory_exist", idConnection,"/var/easyvps/" );

        if(!checkDir.contains("yes")) {
          executeAll(idConnection, new Data("new_directory","www-data", "/var/easyvps/"),
                                   new Data("only_folder_permision","775", "/var/easyvps/") );
        }
    }

    /**
     * disconnect from sftp
     * @param channelDownload
     * @param channelSftpDownload
     */
    public void disconnectSFTP(Channel channelDownload, ChannelSftp channelSftpDownload){
        if(channelDownload != null){
            channelDownload.disconnect();
        }

        if(channelSftpDownload != null){
            channelSftpDownload.disconnect();
        }

    }

    public Boolean isFast(String id){
        SshAccount account = keyStoreService.getSshAccount(id);
        return account.getFast().contains("yes");
    }


}
