package simplessh.com.request;

import java.util.List;
import java.util.Map;

public class AddUpdateRow {
    private Map<String,String> data;
    private List<Map<String,String>> columns;

    private String where;

    public AddUpdateRow(Map<String, String> data, List<Map<String, String>> columns, String where) {
        this.data = data;
        this.columns = columns;
        this.where = where;
    }

    public AddUpdateRow() {
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}
