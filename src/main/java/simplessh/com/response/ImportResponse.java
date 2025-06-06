package simplessh.com.response;

import java.util.List;
import java.util.Map;

public class ImportResponse {
    private String response;
    private List<Map<String, String>> list;

    public ImportResponse() {
    }

    public ImportResponse(String response, List<Map<String, String>> list) {
        this.response = response;
        this.list = list;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }
}
