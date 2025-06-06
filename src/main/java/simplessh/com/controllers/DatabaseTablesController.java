package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.request.AddUpdateRow;
import simplessh.com.response.GetTableFullData;
import simplessh.com.response.ListMapResponse;
import simplessh.com.response.ListStringResponse;
import simplessh.com.services.DatabaseTablesServices;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * @author Corneli F.

 * Data base controller
 */
@RestController
@RequestMapping("/api/v1/")
public class DatabaseTablesController {

    @Autowired
    private DatabaseTablesServices service;

    /**
     * get list of database or tables
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get-list-of-database-tables")
    public List<Map<String, String>> getList(@RequestHeader("id") String id,
                                             HttpServletRequest request) {

        return service.getList(id, request);
    }


    /**
     * add new table for database
     * @param id
     * @param data
     * @param request
     * @return
     */
    @PutMapping(path = "/add-new-database-table", consumes = "application/json", produces = "application/json")
    public ListMapResponse addNewDatabase(@RequestHeader("id") String id,
                                          @RequestBody List<Map<String,String>> data,
                                          HttpServletRequest request) {
        return  service.addNewDatabase(id, data, request);
    }

    /**
     * remove table from database
     * @param id
     * @param data
     * @return
     */
    @RequestMapping(value ="/remove-database-table", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ListMapResponse remove(@RequestHeader("id") String id,
                                  @RequestBody Map<String,String> data) {

        return service.remove(id, data);
    }


    /**
     * empty table from database
     * @param id
     * @param data
     * @return
     */
    @RequestMapping(value ="/empty-database-table", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ListMapResponse empty(@RequestHeader("id") String id,
                                 @RequestBody Map<String,String> data) {

        return service.empty(id, data);
    }


    /**
     * get list of columns from table
     * @param request
     * @return
     */
    @GetMapping("/get-list-of-database-table-structure")
    public List<Map<String,String>> getListStructure(@RequestHeader("id") String id,
                                                     HttpServletRequest request) {

        return service.getListStructure(id, request);
    }


    /**
     * remove column from table
     * @param request
     * @return
     */
    @DeleteMapping("/remove-database-table-column")
    public ListMapResponse removeField(@RequestHeader("id") String id,
                                       HttpServletRequest request) {

      return service.removeField(id, request);
    }

    /**
     * add edit table column
     * @param data
     * @param request
     * @return
     */
    @PutMapping(path = "/add-edit-database-table-column", consumes = "application/json", produces = "application/json")
    public ListMapResponse addEditDatabase(@RequestHeader("id") String id,
                                           @RequestBody List<Map<String,String>> data,
                                           HttpServletRequest request) {

         return service.addEditDatabase(id, data, request);
    }


    /**
     * get list of table data
     * @param request
     * @return
     */
    @GetMapping("/get-list-of-database-table-data")
    public GetTableFullData getListData(@RequestHeader("id") String id,
                                        HttpServletRequest request) {

       return service.getListData(id, request);
    }



    /**
     * add data in the table
     * @param data
     * @param request
     * @return
     */
    @PutMapping(path = "/add-new-data-to-table", consumes = "application/json", produces = "application/json")
    public ListMapResponse addDataInTheTable(@RequestHeader("id") String id,
                                             @RequestBody AddUpdateRow data,
                                             HttpServletRequest request) {

        return service.addDataInTheTable(id, data, request);
    }

    /**
     * remove table row
     * @param request
     * @return
     */
    @DeleteMapping("/remove-database-row")
    public ListMapResponse removeDatabaseRow(@RequestHeader("id") String id,
                                             HttpServletRequest request) {
        return service.removeDatabaseRow(id, request);
    }

    /**
     * add data in the table
     * @param data
     * @param data
     * @return
     */
    @PutMapping(path = "/execute-query", consumes = "application/json", produces = "application/json")
    public ListMapResponse executeQueryTableData(@RequestHeader("id") String id,
                                                 @RequestBody Map<String ,String>data ) {

      return service.executeMysqlQueryTablesData(id,  data );
    }

    /**
     * add data in the table
     * @param data
     * @param data
     * @return
     */
    @PutMapping(path = "/execute-query-tables", consumes = "application/json", produces = "application/json")
    public ListMapResponse executeMysqlQueryTables(@RequestHeader("id") String id,
                                                   @RequestBody Map<String ,String>data ) {

        return service.executeMysqlQueryTables(id,  data );
    }
}
