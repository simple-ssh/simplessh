package simplessh.com.response;


import java.util.List;
import java.util.Map;

public class ListMapResponse{
    private List<Map<String,String>> rows;
    private String response;

    public ListMapResponse() {}

    public ListMapResponse(List<Map<String, String>> rows, String response) {
        this.rows = rows;
        this.response = response;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
