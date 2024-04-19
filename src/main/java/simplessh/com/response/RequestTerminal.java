package simplessh.com.response;

public class RequestTerminal {
    private String command;

    public RequestTerminal() {
    }

    public RequestTerminal(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
