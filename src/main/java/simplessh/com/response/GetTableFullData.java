package simplessh.com.response;

import java.util.List;
import java.util.Map;

public class GetTableFullData {
    private List<Map<String,String>> columns;
    private List<Map<String,String>> rows;
    private String total;

    public GetTableFullData(List<Map<String, String>> columns, List<Map<String, String>> rows, String total) {
        this.columns = columns;
        this.rows = rows;
        this.total = total;
    }

    public GetTableFullData() {
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
