package simplessh.com.request;

import java.util.List;

public class DataBaseNewRequest {
    private String name;
    private String user;
    private String password;
    private String host;
    private List<String> privileges;

    public DataBaseNewRequest(String name, String user, String password, String host, List<String> privileges) {
        this.name = name;
        this.user = user;
        this.password = password;
        this.host = host;
        this.privileges = privileges;
    }

    public DataBaseNewRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }
}
