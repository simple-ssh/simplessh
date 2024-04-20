package simplessh.com.services;


import org.springframework.stereotype.Service;
import simplessh.com.dao.PerformDataImpl;
import simplessh.com.request.AddUpdateRow;
import simplessh.com.response.GetTableFullData;
import simplessh.com.response.ListMapResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 */
@Service
public class DatabaseTablesServices extends PerformDataImpl{

    private SshCommand ssh;
    public DatabaseTablesServices(SshCommand ssh){
        this.ssh = ssh;
    }

    private Integer perPage =100;

    /**
     * get list of database or tables
     * @param id
     * @param request
     * @return
     */

    public List<Map<String, String>> getList(String id, HttpServletRequest request) {

        return getDataList(id, request.getParameter("database"));
    }

    /**
     * convert to List<Map<String,String>>
     * @param database name of database
     * @return
     */
    private List<Map<String, String>> getDataList(String id, String database){
        String data = ssh.execute("mysql_show_table_from_db_with_size", id, database);
        data = data.trim().replaceAll("\t", " ");

      return  Arrays.stream(data.split("\n"))
                    .filter(line ->!line.isEmpty() && !line.contains("Tables_in_"+database) && !line.contains("TABLENAME"))
                    .map(line -> line.split(" "))
                    .filter(parts -> parts.length == 2)
                    .map(e->Map.of("name", e[0],"size", e[1]))
                    .collect(Collectors.toList());
      }

    /**
     * add new table for database
     * @param id
     * @param data
     * @param request
     * @return
     */

    public ListMapResponse addNewDatabase(String id, List<Map<String,String>> data,
                                             HttpServletRequest request) {

        String databaseName = request.getParameter("database");
        String tableName = request.getParameter("table");
        String sql =  "CREATE TABLE IF NOT EXISTS "+databaseName+"."+tableName+" ("+generateSqlForFields(data)+")";

        String response = ssh.execute("mysql_command", id, sql );

        return new ListMapResponse(getDataList(id, databaseName),response);
    }

    /**
     * litle helper will generate sql for fields of table when add new table
     * example: title varchar(255) NOT NULL, start_date DATE
     * @param data
     * @return
     */
    private String generateSqlForFields(List<Map<String,String>> data){
        String keyName = "";
        StringJoiner sqlFields = new StringJoiner(",");
        for(Map <String, String> entry : data){
            String name          = entry.getOrDefault("fname","").trim();
            String nameFrom      = entry.getOrDefault("fnameFrom","").trim();
            String type          = entry.getOrDefault("ftype","");
            String collation     = entry.getOrDefault("fcollation","");
            String length        = entry.getOrDefault("flength","");
            String nullData      = entry.getOrDefault("fnull","");
            String defaultData   = entry.getOrDefault("fdefault","");
            String primaryKey    = entry.getOrDefault("fprimary","");
            String autoIncrement = entry.getOrDefault("fautoincrement","");

            // check if is text than not need length
            String fieldSql =  !type.contains("TEXT")&!type.contains("DATE") && !type.contains("TIME") &&
                    !type.contains("YEAR") && !type.contains("JSON")&& !type.contains("BOOLEAN") ?
                    "("+length+") "+(!collation.isEmpty() ? " COLLATE "+collation:""): "";

            if(!defaultData.isEmpty())
                fieldSql = type+fieldSql+" DEFAULT '"+defaultData+"'"+(nullData.isEmpty()?" NOT NULL":"");

            if(defaultData.isEmpty() && !nullData.isEmpty())
                fieldSql = type+fieldSql+" DEFAULT NULL" ;

            if(defaultData.isEmpty() && nullData.isEmpty())
                fieldSql = type+fieldSql+" NOT NULL" ;

            String columnName = !nameFrom.isEmpty() && !nameFrom.equals(name)  ? nameFrom +" "+name : name;
            sqlFields.add(columnName+" "+fieldSql+(!autoIncrement.isEmpty()?" AUTO_INCREMENT":""));

            if(!primaryKey.isEmpty()){
                keyName = name;
            }
        }

        if(!keyName.isEmpty())
            sqlFields.add("PRIMARY KEY("+keyName+")");

        return  sqlFields.toString();
    }

    /**
     * remove table from database
     * @param id
     * @param data
     * @return
     */
    public ListMapResponse remove(String id, Map<String,String> data) {
        String databaseName   = data.getOrDefault("database","");
        String tableNames     = data.getOrDefault("tables","");

        String response       = ssh.execute("mysql_delete_table", id, tableNames);

        return new ListMapResponse(getDataList(id, databaseName),response);
    }

    /**
     * get list of columns from table
     * @param request
     * @return
     */
   public List<Map<String,String>> getListStructure(String id, HttpServletRequest request) {

        String databaseName   = request.getParameter("database");
        String tableName      = request.getParameter("table");


        return getListOfColumnsIMPL(id,databaseName, tableName);
    }

    private List<Map<String,String>> getListOfColumnsIMPL(String id, String databaseName,String tableName){

       return extractTheData(
                ssh.execute("mysql_show_column_from_table_from_db", id, databaseName+"."+tableName)
          );
    }



    /**
     * remove column from table
     * @param request
     * @return
     */
    public ListMapResponse removeField(String id, HttpServletRequest request) {
        String databaseName    = request.getParameter("database");
        String tableName       = request.getParameter("table");
        String column          = request.getParameter("column");

        String response        = ssh.execute("mysql_delete_field_from_table", id,
                                                databaseName+"."+tableName, column);

        return new ListMapResponse(getListOfColumnsIMPL(id,databaseName, tableName), response);
    }

    /**
     * add edit table column
     * @param data
     * @param request
     * @return
     */
    public ListMapResponse addEditDatabase(String id, List<Map<String,String>> data,
                                           HttpServletRequest request) {
        String editOperation = "MODIFY";
        if(data.size()==1){
           String name        = data.get(0).getOrDefault("fname","").trim();
           String nameFrom    = data.get(0).getOrDefault("fnameFrom","").trim();
           editOperation      = !nameFrom.isEmpty() && !nameFrom.equals(name)  ? "CHANGE" : editOperation;
        }

        String databaseName   = request.getParameter("database");
        String tableName      = request.getParameter("table");
        String typeEditAdd    = request.getParameter("typeEditAdd");
        String sql            = typeEditAdd.contains("add")?
                                "Alter table "+databaseName+"."+tableName+" ADD( "+generateSqlForFields(data)+" )":
                                "Alter table "+databaseName+"."+tableName+" "+editOperation+" "+generateSqlForFields(data); //MODIFY

        String response       = ssh.execute("mysql_command", id, sql);
        return new ListMapResponse(getListOfColumnsIMPL(id,databaseName, tableName), response);
    }


    /**
     * get list of table data
     * @param request
     * @return
     */
    public GetTableFullData getListData(String id, HttpServletRequest request) {
        String databaseName   = request.getParameter("database");
        String tableName      = request.getParameter("table");
        String page           = request.getParameter("page");

        Integer pageNr        = Integer.parseInt(page);
        Integer pagination    = pageNr==1? 0 : ((pageNr - 1) * perPage);

        GetTableFullData retturnDAta ;

        String dataSplit = ssh.execute( "mysql_show_data_first_call",
                                         id, databaseName+"."+tableName,  pagination+", "+perPage );

        try{

            String[] split =  dataSplit.split("---------split---------");
            // get total of rows
            String[] countSplit =  split[1].split("\\r?\\n");
            String totalRows = countSplit[1].trim();
            totalRows = totalRows.contains("COUNT") ? countSplit[2].trim() : totalRows;

            retturnDAta = new GetTableFullData(extractTheData(split[0]),// get list of columns
                    extractTheData(split[2]), // get list of data
                    totalRows
            );
        }catch (Exception e){
            retturnDAta = new GetTableFullData();
        }

        return retturnDAta;

    }



    /**
     * add data in the table
     * @param data
     * @param request
     * @return
     */
    public ListMapResponse addDataInTheTable(String id, AddUpdateRow data,
                                             HttpServletRequest request) {

        String databaseName      = request.getParameter("database");
        String tableName         = request.getParameter("table");
        String typeBtn           = request.getParameter("typeBtn");

        String page              = request.getParameter("page");

        Integer pageNr           = Integer.parseInt(page);
        Integer pination         = pageNr==1? 0 : ((pageNr - 1) * perPage);

        StringJoiner columns     = new StringJoiner(", ");
        StringJoiner values      = new StringJoiner(", ");
        StringJoiner updateData  = new StringJoiner(", ");


        data.getData().forEach((key,val)-> {

            String keyType = data.getColumns().stream().filter(e->e.get("Field").compareTo(key)==0).
                                  map(e->e.get("Type")).collect(Collectors.joining()).toLowerCase(Locale.ROOT);

            String value = keyType.contains("int") || keyType.contains("float") || keyType.contains("number") ?
                              val : "'" + val + "'";

            if (!val.isEmpty() && val.compareTo("NULL") != 0) {
                columns.add(key);
               // val = val.replaceAll("\\\\", "\\\\\\\\\\\\\\\\");
                //val = val.replaceAll("'", "\\\\'");
                //val = val.replaceAll("\"", "\\\\\"");
                //val = val.replaceAll("\\\\$", "\\\\$");
                //System.out.println("val:"+val);
                values.add(value);
            }

            if (val.compareTo("NULL") != 0)
                updateData.add("p." + key + " = " + value);

        });

        String sql = typeBtn.contains("add") ?
                "SET NAMES 'utf8'; INSERT INTO " + databaseName + "." + tableName + " ( " + columns + ") VALUES (" + values + ")":
                "SET NAMES 'utf8'; UPDATE " + databaseName + "." + tableName + " p SET " + updateData + " WHERE " + data.getWhere();


        String response = ssh.execute("mysql_command", id, sql);

        String rows     = ssh.execute("mysql_show_data_from_table",
                                          id, databaseName+"."+tableName, pination+","+perPage);

        return new ListMapResponse(extractTheData(rows), response);
    }


    /**
     * remove table row
     * @param request
     * @return
     */

    public ListMapResponse removeDatabaseRow(String id, HttpServletRequest request) {
        String databaseName   = request.getParameter("database");
        String tableName      = request.getParameter("table");
        String where          = request.getParameter("where");
        String page           = request.getParameter("page");

        Integer pageNr        = Integer.parseInt(page);
        Integer pination      = pageNr==1? 0 : ((pageNr - 1) * perPage);

        String sql            =  "DELETE FROM " + databaseName + "." + tableName + " p WHERE " + where;

        String response       = ssh.execute("mysql_command", id, sql);


        String rows           = ssh.execute("mysql_show_data_from_table", id,
                                        databaseName+"."+tableName, pination+","+perPage);

        return new ListMapResponse(extractTheData(rows), response);
    }

    /**
     * add data in the table
     * @param data
     * @param data
     * @return
     */
    public ListMapResponse executeMysqlQueryTablesData(String id, Map<String ,String>data  ) {
        String dbName = data.getOrDefault("database","");
        String sql = "USE "+dbName+"; "+data.getOrDefault("query","");
        Map<String, String> rows = ssh.executeMap("mysql_command", id, sql);
        return new ListMapResponse( extractTheData(rows.get("data")), rows.get("error"));
    }

    /**
     * add data in the table
     * @param data
     * @param data
     * @return
     */
    public ListMapResponse executeMysqlQueryTables(String id, Map<String ,String>data ) {
        String dbName = data.getOrDefault("database","");
        String sql = "USE "+dbName+"; "+data.getOrDefault("query","");
        Map<String, String> rows = ssh.executeMap("mysql_command", id, sql);

        return new ListMapResponse( getDataList(id, dbName), rows.get("error"));
    }


    /**
     * Empty table content
     * @param id
     * @param data
     * @return
     */
    public ListMapResponse empty(String id, Map<String, String> data) {
        String databaseName   = data.getOrDefault("database","");
        String tableName      = data.getOrDefault("table","");

        String response       = ssh.execute("mysql_empty_table", id, databaseName+"."+tableName);
        return new ListMapResponse(getDataList(id, databaseName),response);
    }

}
