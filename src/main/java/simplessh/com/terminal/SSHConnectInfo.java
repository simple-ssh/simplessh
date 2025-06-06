package simplessh.com.terminal;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SSHConnectInfo {
    private String sessionID;
    private JSch jSch;
    private ChannelShell channel;
    private Session session;
    private String singleToken;
    private String connectionId;

    public SSHConnectInfo(JSch jSch, String sessionID, String singleToken, String connectionId) {
        this.sessionID = sessionID;
        this.singleToken = singleToken;
        this.connectionId = connectionId;
        this.jSch = jSch;
    }
}
