package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.SubGroupsServices;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Corneli F.
 */
@RestController
@RequestMapping("/api/v1/")
public class SubGroupsController {

    @Autowired
    private SubGroupsServices service ;

    /**
     * get list of firewall
     * @param id
     * @return
     */
    @GetMapping("/get-list-of-subgroups")
    public List<Map<String,String>> getList(@RequestHeader("id") String id, HttpServletRequest request) {
        return service.getList(id, request);
    }



    /**
     * add new rule to firewall
     * @param id
     * @param request
     * @return
     */
    @PutMapping(path = "/add-new-element-to-subgroup")
    public List<Map<String,String>> addNewOne(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.addNewOne(id, request);
    }

    /**
     * remove rule from firewall
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-element-from-subgroup")
    public List<Map<String,String>> remove(@RequestHeader("id") String id, HttpServletRequest request ) {

        return service.remove(id, request);
    }
}
