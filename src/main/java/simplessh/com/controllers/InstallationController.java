package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.InstallationServices;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Corneli F.
 *
 * Install controller
 */

@RestController
@RequestMapping("/api/v1/")
public class InstallationController {

    @Autowired
    private InstallationServices service;

    /**
     * check if app is instaled
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/check-app-status")
    public String checkStatus(@RequestHeader("id") String id, HttpServletRequest request) {

      return service.checkStatus(id, request);
    }

    /**
     * uninstall app
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/uninstall-app")
    public String uninstall(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.uninstall(id, request);
    }


    /**
     * install app
     * @param id
     * @param data
     * @param request
     * @return
     */
    @PutMapping(path = "/install-app" , consumes = "application/json", produces = "application/json")
    public String install(@RequestHeader("id") String id,
                          @RequestBody Map<String, String> data,
                          HttpServletRequest request) {

        return service.install(id,  data, request);
    }

}
