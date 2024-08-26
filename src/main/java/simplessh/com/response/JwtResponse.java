package simplessh.com.response;

import lombok.*;

import java.util.List;

public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, String type, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.type = type;
        this.username = username;
        this.roles = roles;
    }

    public JwtResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
