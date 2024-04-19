package simplessh.com.terminal;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import simplessh.com.dao.SshAccount;
import simplessh.com.services.KeyStoreService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Here is all the magical staff where we start shell terminal and push messages and get live data from ssh
 */
@Service
@Slf4j
public class TerminalWebsocketService {
    @Autowired
    private KeyStoreService keyService ;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private Map<String, SSHConnectInfo> connections = new ConcurrentHashMap<>();

    /**
     * we initate the user terminal this method is used in: WebsocketAuthInterceptor.java
     * @param sessionID
     */
    public void init(String sessionID, String singleToken, String connectionId ){

        connections.put(singleToken, new  SSHConnectInfo(new JSch(), sessionID, singleToken, connectionId));

        try{
           setTerminal(singleToken);
         }catch (Exception e){
           log.error("Connection to ssh not initiated: "+e.getMessage());
        }
    }

    public void publish(String eventData, String singleToken) {
        messagingTemplate.convertAndSend("/receive-data/terminal/"+singleToken, eventData );
    }

    public void setTerminal(String singleToken) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        // initiate a  new terminal
        executorService.execute(() -> {
           try {
                connectToSSH(singleToken);
           } catch (JSchException | IOException e) {
                log.error("Error connect to ssh", e.getMessage());
                try {
                    submitCommand(singleToken, e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                close(singleToken);
            }
        });
    }

    /**
     * Close Connection, this method is used in: WebsocketAuthInterceptor.java
     *  here may recive or session id or singleToken
     * @param singleToken
     */
    public void close(String singleToken) {
        SSHConnectInfo connectInfo = connections.entrySet().stream().
                                      filter(e->e.getValue().getSessionID().equals(singleToken) ||
                                             e.getValue().getSingleToken().equals(singleToken)).
                                      map(e->e.getValue()).
                                      findFirst().orElse(null); //connections.getOrDefault(sessionID, null) ;

        if (connectInfo != null) {
            log.info("Terminal disconnected:"+singleToken);
            //Disconnect
            if (connectInfo.getChannel()  != null)
                connectInfo.getChannel().disconnect();

            if (connectInfo.getSession()  != null)
                connectInfo.getSession().disconnect();

            //Remove from map
            connections.remove(singleToken);
        }
    }


    public void submitCommand(String singleToken, String command) throws IOException {
        SSHConnectInfo connectInfo = connections.getOrDefault(singleToken, null) ;

        if (connectInfo != null && connectInfo.getChannel() != null) {
            OutputStream outputStream = connectInfo.getChannel().getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            // Execute command
            writer.println(command);
            writer.flush();
            // outputStream.write(command.getBytes());
            //outputStream.flush();
        }
    }

    /***
     * Start terminal
     * Here is all magical staff
     * @throws JSchException
     * @throws IOException
     */
    private void connectToSSH(String singleToken) throws JSchException, IOException {
       SSHConnectInfo connectInfo = connections.getOrDefault(singleToken, null);
       if(connectInfo == null)
            return;

        log.info("Terminal connected:"+connectInfo.getSessionID());

        SshAccount connData = keyService.getSshAccount(connectInfo.getConnectionId());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        JSch jSch = connectInfo.getJSch();
        // here generate session by .pem file
        if( !connData.getSshPem().isEmpty() && connData.getSshPass().isEmpty())
            jSch.addIdentity("privateKey.pem", connData.getSshPem().getBytes(), null, null);

        //Get the session of jsch
        Session session = jSch.getSession(connData.getSshLog(), connData.getSshHost(), 22);
        session.setConfig(config);

        //set password
        if( !connData.getSshPass().isEmpty())
        session.setPassword(connData.getSshPass());

        //Connection timeout 30s
        session.connect(30000);

        //Open shell channel
        ChannelShell channel = (ChannelShell)session.openChannel("shell");

        //Channel connection timeout 3s
        channel.connect(3000);

        //Set channel
        connectInfo.setChannel(channel);
        connectInfo.setSession(session);
        connections.put(singleToken, connectInfo);
        //Forward message

        //submitCommand(singleToken,"\r");

        //Read the information stream returned by the terminal
        InputStream inputStream = channel.getInputStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove non-printable characters before sending the message
                // line = line.replaceAll("[^\\x20-\\x7e]", "");
                // Remove control characters and escape sequences
                line = line.replaceAll("\\u001B\\[[;\\d]*m", "");
                line = line.replaceAll("\\x1B\\[[0-9;]*[a-zA-Z]", "");
                line = line.replaceAll("\\e\\[[0-9;]*[mGKJHr]", "");
                line = line.replaceAll("\\^.", ""); // Removes ^ followed by any character
                // Trim the line to remove leading and trailing whitespace
                line = line.trim();
                publish(line, singleToken);
            }
        } finally {
            //Close session after disconnecting
            session.disconnect();
            channel.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    private String convertObjectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
