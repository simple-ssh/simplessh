package simplessh.com.response;

import java.util.List;
import java.util.Map;

public class ListOfMysqUsersAndDb {
    private List<Map<String,String>> dbs;
    private List<Map<String,String>> users;

    public ListOfMysqUsersAndDb(List<Map<String, String>> dbs, List<Map<String, String>> users) {
        this.dbs = dbs;
        this.users = users;
    }

    public ListOfMysqUsersAndDb() {
    }

    public List<Map<String, String>> getDbs() {
        return dbs;
    }

    public void setDbs(List<Map<String, String>> dbs) {
        this.dbs = dbs;
    }

    public List<Map<String, String>> getUsers() {
        return users;
    }

    public void setUsers(List<Map<String, String>> users) {
        this.users = users;
    }
}
