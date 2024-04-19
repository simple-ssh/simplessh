package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.FirewallServices;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Corneli F.
 *
 * Firewall controller
 */
@RestController
@RequestMapping("/api/v1/")
public class FirewallController {
    @Autowired
    private FirewallServices service ;

    /**
     * get list of firewall
     * @param id
     * @return
     */
   @GetMapping("/get-list-of-firewall-rules")
    public List<Map<String,String>> getList(@RequestHeader("id") String id) {
         return service.getList(id);
    }

    /**
     * enable disable firewall
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/enable-disable-firewall")
    public List<Map<String,String>> actionData(@RequestHeader("id") String id, HttpServletRequest request ) {

       return service.actionData(id, request);
    }

    /**
     * add new rule to firewall
     * @param id
     * @param request
     * @return
     */
    @PutMapping(path = "/add-new-firewall-rule")
    public List<Map<String,String>> addNewOne(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.addNewOne(id, request);
      }

    /**
     * remove rule from firewall
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-firewall-rule")
    public List<Map<String,String>> remove(@RequestHeader("id") String id, HttpServletRequest request ) {

        return service.remove(id, request);
    }


}
