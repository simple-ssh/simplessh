package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simplessh.com.request.DataBaseNewRequest;
import simplessh.com.response.ImportResponse;
import simplessh.com.services.DatabaseService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Corneli F.
 *
 * Database controller
 */
@RestController
@RequestMapping("/api/v1/")
public class DatabaseController {

    @Autowired
    private DatabaseService service;

    private String[] privilegies = {"ALL PRIVILEGES","CREATE","DROP","DELETE","INSERT","SELECT","UPDATE" };

    /**
     * Get list of database list
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get-list-of-database-users")
    public List<Map<String,String>> getList(@RequestHeader("id") String id, HttpServletRequest request) {
        return service.getList(id, request);
    }


    @PutMapping(path = "/add-new-database", consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> addNewDatabase(@RequestHeader("id") String id,
                                                   @RequestBody DataBaseNewRequest data) {

        return service.addNewDatabase(id, data);
    }

    /**
     * remove a database
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-database")
    public List<Map<String,String>> removeDatabase(@RequestHeader("id") String id,
                                                     HttpServletRequest request) {

        return service.removeDatabase(id, request);
    }

    /**
     * export data from database
     * @param request
     * @param response
     * @throws IOException
     */

    @RequestMapping("/export-database")
    public void exportDatabase(HttpServletRequest request,
                                    HttpServletResponse response ) throws IOException {

        service.exportDatabase(request, response);
    }

    /**
     * import data to database
     * @param id
     * @param request
     * @param file
     * @return
     */
    @PutMapping(path = "/import-database")
    public ImportResponse importDb(@RequestHeader("id") String id, HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file) {

      return service.importDb(id, request, file);
    }

    /**USERS PART**/

    /**
     * get list of database
     * @return
     */
    @GetMapping("/get-list-of-mysql-database")
    public List<Map<String,String>> getListOfDb(@RequestHeader("id") String id) {

        return service.getListOfDb(id);
    }

    /**
     * add new user and asign to database
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/add-new-database-user", consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> addNewDatabaseUser(@RequestHeader("id") String id,
                                                       @RequestBody DataBaseNewRequest data) {

        return service.addNewDatabaseUser(id,data);
    }

    /**
     * remove user
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-database-user")
    public List<Map<String,String>> removeDatabaseUser(@RequestHeader("id") String id,
                                                       HttpServletRequest request) {

      return service.removeDatabaseUser(id, request);
    }

    /**
     * chance user password
     * @param id
     * @param data
     * @return
     */

    @PutMapping(path = "/user-mysqldb-change-password", consumes = "application/json", produces = "application/json")
    public String changePassDatabaseUser(@RequestHeader("id") String id,
                                         @RequestBody Map<String,String> data) {

        service.changePassDatabaseUser(id,data);
        return "Password changed, if not than enter a password what contain one or more upper case letter," +
               " lower case letter, @, and numbers!" ;
    }
}
