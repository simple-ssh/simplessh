package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.response.ListMapResponse;
import simplessh.com.services.TerminalServices;
import java.util.*;

/**
 * @author Corneli F.
 *
 * Terminal controller
 */

@RestController
@RequestMapping("/api/v1/")
public class TerminalController {
    @Autowired
    private TerminalServices service ;

    /**
     * execute ny comand you enter and return the result
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/execute-command" , consumes = "application/json", produces = "application/json")
    public ListMapResponse executeCommmand(@RequestHeader("id") String id,
                                           @RequestBody Map<String, String> data) {

        return service.executeCommmand(id,data);
    }


}
