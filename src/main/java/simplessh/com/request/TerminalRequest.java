package simplessh.com.request;

public class TerminalRequest {
    private String username;
    private String password;
    private String host;
    private String sessionUser;
    private int port;
    private String command;
    private String res;

    public TerminalRequest(String username, String password, String host, String sessionUser, int port, String command, String res) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.sessionUser = sessionUser;
        this.port = port;
        this.command = command;
        this.res = res;
    }

    public TerminalRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(String sessionUser) {
        this.sessionUser = sessionUser;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
