package simplessh.com.dao;

public class Data {
    private String commandName;

    private String[] params;

    public Data() {
    }

    public Data(String commandName) {
        this.commandName = commandName;
    }

    public Data(String commandName, String... params ) {
        this.commandName = commandName;
        this.params = params;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
