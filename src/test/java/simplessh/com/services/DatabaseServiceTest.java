package simplessh.com.services;



import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DatabaseServiceTest {

    @Test
    void getDataListEmpty() {
        DatabaseService db = new DatabaseService(mock(SshCommand.class), mock(DatabaseTablesServices.class));
        List<Map<String,String>> data = db.getDataList("");
        assertTrue(data.size()==0);
    }

    @Test
    void getDataListNotEmpty() {
        DatabaseService db = new DatabaseService(mock(SshCommand.class), mock(DatabaseTablesServices.class));
        String st = "db\tuser\thost\n" +
                "db1\tuser1\tlocalhost\n" +
                "db2\tuser1\tlocalhost\n" +
                "db3\tuser1\tlocalhost\n" +
                "db4\tuser1\tlocalhost\n" +
                "db5\n" +
                "db1";
        List<Map<String,String>> data = db.getDataList(st);
        assertTrue(data.size()==5 &&
                            data.get(0).get("name").equals("db1")&&
                            data.get(1).get("name").equals("db2")&&
                            data.get(2).get("name").equals("db3")&&
                            data.get(3).get("name").equals("db4")&&
                            data.get(4).get("name").equals("db5")&&
                            data.get(0).get("host").equals("localhost") &&
                            data.get(0).get("user").equals("user1") &&
                            data.get(4).get("host").isEmpty() &&
                            data.get(4).get("user").isEmpty()
                  );
    }
}