package simplessh.com.dao;

import java.util.List;
import java.util.Map;

public interface PerformData {
    List<Map<String,String>> extractTheData(String data);
}
