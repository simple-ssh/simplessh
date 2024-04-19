package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.ServicesServices;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Corneli F.
 *
 * Service controller
 */
@RestController
@RequestMapping("/api/v1/")
public class ServicesController {
    @Autowired
    private ServicesServices service ;

    /**
     * get list of services
     * @param id
     * @return
     */
   @GetMapping("/get-list-of-services")
    public List<Map<String,String>> getList(@RequestHeader("id") String id) {

        return service.getList(id);
    }

    /**
     * service action like disable or enable or activate
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/services-action")
    public List<Map<String,String>> actionData(@RequestHeader("id") String id, HttpServletRequest request) {

       return service.actionData(id, request);
   }

    /**
     *  service action like disable or enable or activate
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/action-service")
    public String actionService(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.actionService(id, request);
    }

    /**
     * get service data
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get-service-data")
    public String getServiceData(@RequestHeader("id") String id, HttpServletRequest request) {

      return service.getServiceData(id, request);
    }

    /**
     * show status of service
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/service-show-status")
    public String showStatus(@RequestHeader("id") String id, HttpServletRequest request ) {

      return service.showStatus(id, request);
   }

    /**
     * add new serrvice
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/add-new-service" , consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> addNewOne(@RequestHeader("id") String id,
                                              @RequestBody Map<String, String> data) {

        return service.addNewOne(id, data);
      }

}
