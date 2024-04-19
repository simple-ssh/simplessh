package simplessh.com.response;

import java.util.List;


public class ListStringResponse {
    private List<String> rows;
    private String response;

    public ListStringResponse(List<String> rows, String response) {
        this.rows = rows;
        this.response = response;
    }

    public ListStringResponse() {
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
