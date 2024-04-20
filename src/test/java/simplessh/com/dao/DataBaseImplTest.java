package simplessh.com.dao;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseImplTest {

    @Test
    void extractTheDataEmpty() {
        PerformData dt = new PerformDataImpl(){};
        List<Map<String,String>> list = dt.extractTheData("");
        assertTrue(list.isEmpty());
    }

    @Test
    void extractTheDataWithData1() {
        PerformData dt = new PerformDataImpl(){};
        String st = "id\temail\tdestination\tidDestination\n" +
                "2\tdmarc@test1.com\tdmarc@test1.com\t2\n" +
                "1\tinfo@test1.com\tinfo@test1.com\t1\n" +
                "3\tinfo@test2.com\tinfo@test2.com\t3\n" +
                "6\tinfo@test3.com\tinfo@test3.com\t6";
        List<Map<String,String>> list = dt.extractTheData(st);
        assertTrue(list.size()==4);
    }

    @Test
    void extractTheDataWithData2() {
        PerformData dt = new PerformDataImpl(){};
        String st = "id\temail\tdestination\tidDestination\n" +
                "2\tdmarc@test1.com\tdmarc@test11.com\t2\n" +
                "1\tinfo@test1.com\tinfo@test11.com\t1\n" +
                "3\tinfo@test2.com\t\t3\n" +
                "6\tinfo@test3.com";
        List<Map<String,String>> list = dt.extractTheData(st);
        assertTrue(list.size()==4 && list.get(2).get("destination").isEmpty() &&
                            list.get(3).get("destination").isEmpty() &&
                            list.get(3).get("idDestination").isEmpty() &&
                            list.get(3).get("id").equals("6") &&
                            list.get(3).get("email").equals("info@test3.com") );
    }
}