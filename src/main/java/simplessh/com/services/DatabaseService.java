package simplessh.com.services;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simplessh.com.dao.Data;
import simplessh.com.dao.DownloadFile;
import simplessh.com.request.DataBaseNewRequest;
import simplessh.com.response.ImportResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 */
@Service
public class DatabaseService{

    private SshCommand ssh;

    private DatabaseTablesServices databaseTablesServices;

    public DatabaseService(SshCommand ssh, DatabaseTablesServices databaseTablesServices) {
        this.ssh = ssh;
        this.databaseTablesServices=databaseTablesServices;
    }

    /**
     * Get list of database list
     * @param id
     * @param request
     * @return
     */
   public List<Map<String,String>> getList(String id, HttpServletRequest request) {
        String type=request.getParameter("dataType");

        String data = ssh.execute(type.contains("database")? "mysql_dbList_full":"mysql_show_users_list", id);

        return getDataList(data);
    }



    public List<Map<String,String>> addNewDatabase(String id, DataBaseNewRequest data) {
        String privileges = data.getPrivileges() == null ? "ALL PRIVILEGES" :
                String.join(",", data.getPrivileges());

        ssh.executeAll(id, new Data("mysql_new_database",data.getName()),
                          new Data("mysql_new_user", data.getUser(), data.getHost(), data.getPassword()),
                          new Data("mysql_user_grand_permision",privileges, data.getName(), "'"+data.getUser() +"'@'"+data.getHost()+"'"),
                          new Data("mysql_flush")
                       );


        String dbListWithUsers = ssh.execute( "mysql_dbList_full", id);
        return getDataList(dbListWithUsers);
    }



    /**
     * remove a database
     * @param id
     * @param request
     * @return
     */

    public List<Map<String,String>> removeDatabase(String id, HttpServletRequest request) {
        String name = request.getParameter("name");

        ssh.executeAll(id, new Data("mysql_remove_db",name),
                           new Data("mysql_remove_db_from_sql_table",name),
                           new Data("mysql_flush") );


        String data = ssh.execute("mysql_dbList_full", id);
        return getDataList(data);
    }

    /**
     * export data from database
     * @param request
     * @param response
     * @throws IOException
     */

    public void exportDatabase(HttpServletRequest request, HttpServletResponse response ) throws IOException {
        String name   = request.getParameter("name");
        String tables = request.getParameter("tables");
               tables  = tables!=null && !tables.isEmpty() ? " "+tables : "";
        // check if path /tmp/simplessh_tmp path exist and open the connection
        String idConnection = request.getParameter("id");
        ssh.checkForVarEasyvpsPath(idConnection);

        ssh.executeAll(idConnection, new Data("new_empty_file","root","/tmp/simplessh_tmp/"+name+".sql"),
                                     new Data("file_permission","666","/tmp/simplessh_tmp/"+name+".sql"),
                                     new Data("mysql_export",name+tables, "/tmp/simplessh_tmp/"+name+".sql") );

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + name + ".sql\""));

        DownloadFile inp= ssh.downloadFileStream("/tmp/simplessh_tmp/"+name+".sql", idConnection);
        FileCopyUtils.copy(inp.getFile(), response.getOutputStream());

        ssh.disconnectSFTP(inp.getChannelDownload(),  inp.getChannelSftpDownload() );
    }

    /**
     * import data to database
     * @param id
     * @param request
     * @param file
     * @return
     */

    public ImportResponse importDb(String id, HttpServletRequest request, MultipartFile file) {
        String dbname= request.getParameter("dbname");
        String getList = request.getParameter("getList");
        //connect to server
        ssh.checkForVarEasyvpsPath(id);

        Map<String, InputStream> listF = new HashMap<>();
        try {
            listF.put(file.getOriginalFilename(), file.getInputStream());
        }catch (Exception e){}

        ssh.uploadFile(listF,  "/tmp/simplessh_tmp/", id );

        try{Thread.sleep(2000);}catch (Exception e){}

        ssh.executeAll(id, new Data("file_permission","666","/tmp/simplessh_tmp/"+file.getOriginalFilename()),
                           new Data("mysql_import",dbname, "/tmp/simplessh_tmp/"+file.getOriginalFilename()) );

        return new ImportResponse("Data imported", (getList != null ? databaseTablesServices.getDataList(id, dbname) : null) ) ;
    }

    /**USERS PART**/

    /**
     * get list of database
     * @return
     */
    @GetMapping("/get-list-of-mysql-database")
    public List<Map<String,String>> getListOfDb(String id) {
        return getDataList(ssh.execute("mysql_dbList", id));
    }

    /**
     * add new user and asign to database
     * @param id
     * @param data
     * @return
     */

    public List<Map<String,String>> addNewDatabaseUser(String id, DataBaseNewRequest data) {
        String privileges = data.getPrivileges() == null ? "ALL PRIVILEGES" :
                String.join(",", data.getPrivileges());

        ssh.executeAll(id, new Data("mysql_new_user",data.getUser(), data.getHost(), data.getPassword()),
                          new Data("mysql_user_grand_permision",privileges, data.getName(), "'"+data.getUser() +"'@'"+data.getHost()+"'"),
                          new Data("mysql_flush") );

     return getDataList(ssh.execute("mysql_show_users_list",  id));
    }

    /**
     * remove user
     * @param id
     * @param request
     * @return
     */

    public List<Map<String,String>> removeDatabaseUser(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        String hostdb = request.getParameter("hostdb");

        ssh.execute("mysql_delete_user", id, name, hostdb);
        return getDataList(ssh.execute("mysql_show_users_list", id));
    }

    /**
     * chance user password
     * @param id
     * @param data
     * @return
     */


    public String changePassDatabaseUser(String id, Map<String,String> data) {
        String name = data.getOrDefault("name","");
        String host = data.getOrDefault("host","");
        String password = data.getOrDefault("password","");
        ssh.executeAll(id, new Data("mysql_user_change_password","'"+name+"'@'"+host+"'", password),
                           new Data("mysql_old_user_change_password","'"+name+"'@'"+host+"'", password),
                           new Data("mysql_flush") );
       return "Password changed, if not than enter a password what contain one or more upper case letter, lower case letter, @, and numbers!" ;
    }

    /**
     * parse string and transform to list of map
     * convert to List<Map<String,String>>
     * @param data
     * @return
     */
    public List<Map<String,String>> getDataList(String data){
        List<String> excludeName = List.of("Database","sys","mysql","db","performance_schema","information_schema",
                "mysql.session","mysql.sys","mysql.infoschema","debian-sys-maint","User");

        List<String> duplicates= new ArrayList<>();
        return Arrays.stream(data.trim().replaceAll("\t", " ").split("\\r?\\n")).
                       filter(e->!e.isEmpty() && !excludeName.contains(e.split(" ")[0])&&
                                 !duplicates.contains(e.split(" ")[0])).
                       map(st->{
                           String[] split = st.split(" ");
                           duplicates.add(split[0]);
                          return Map.of("name", split[0],
                                   "host", (st.contains(" ") ? (split.length>2? split[2] : split[1]):""),
                                   "user", (st.contains(" ") ? split[1]:""));
                       }).collect(Collectors.toList());
   }

}
