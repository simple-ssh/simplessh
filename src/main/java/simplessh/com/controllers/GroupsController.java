package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.GroupsServices;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Corneli F.
 */
@RestController
@RequestMapping("/api/v1/")
public class GroupsController {

    @Autowired
    private GroupsServices service ;

    /**
     * get list of firewall
     * @param id
     * @return
     */
    @GetMapping("/get-list-of-groups")
    public List<Map<String,String>> getList(@RequestHeader("id") String id) {
        return service.getList(id);
    }



    /**
     * add new rule to firewall
     * @param id
     * @param request
     * @return
     */
    @PutMapping(path = "/add-new-element-to-group")
    public List<Map<String,String>> addNewOne(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.addNewOne(id, request);
    }

    /**
     * remove rule from firewall
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-element-from-group")
    public List<Map<String,String>> remove(@RequestHeader("id") String id, HttpServletRequest request ) {

        return service.remove(id, request);
    }
}
