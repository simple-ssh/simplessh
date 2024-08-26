package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.services.EmailsServices;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Corneli F.
 *
 * Email controller when go to menu emails it will process this api
 */
@RestController
@RequestMapping("/api/v1/")
public class EmailsController {

    @Autowired
    private EmailsServices service;

    /**
     * get list of emails
     * @param id
     * @return
     */
   @GetMapping("/get-list-of-emails")
    public List<Map<String,String>> getList(@RequestHeader("id") String id) {
        return service.getList(id);
    }

    /**
     * add ssl to postfix and dovecot
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/setup-ssl-to-postfix" , consumes = "application/json", produces = "application/json")
    public Map<String,String> setupSSLToPostfix(@RequestHeader("id") String id,
                                    @RequestBody Map<String, String> data) {

      return  service.setupSSLToPostfix(id, data);
    }

    /**
     * add new email
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/add-new-email-account", consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> addNewOne(@RequestHeader("id") String id,
                                              @RequestBody Map<String, String> data) {
       return service.addNewOne(id, data);
    }


    /**
     * change forward email
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/email-change-forward", consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> updateForwardEmail(@RequestHeader("id") String id,
                                                       @RequestBody Map<String, String> data) {
        return service.updateForwardEmail(id, data);
    }

    /**
     * email update password
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/email-change-password", consumes = "application/json", produces = "application/json")
    public String updatePassword(@RequestHeader("id") String id,
                                 @RequestBody Map<String, String> data) {
        return service.updatePassword(id, data);
    }
    @PutMapping(path = "/setup-email-server", consumes = "application/json", produces = "application/json")
    public String setupEmailServer(@RequestHeader("id") String id,
                                 @RequestBody Map<String, String> data) {
        return service.setupEmailServer(id, data);
    }
    /**
     * remove email
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-email" )
    public List<Map<String,String>> remove(@RequestHeader("id") String id,
                                           HttpServletRequest request ) {

       return service.remove(id, request);
    }

    @GetMapping("/get-dkim-info")
    public Map<String,String> getDkimInfo(@RequestHeader("id") String id) {
        return service.getDkimInfo(id);
    }

    @GetMapping("/get-dns-info")
    public Map<String,String> getDnsInfo(@RequestHeader("id") String id, HttpServletRequest request) {
        return service.checkDNS(id, request.getParameter("domain"),
                                    request.getParameter("ip" ));
    }

    @GetMapping("/regenerate-dkim-key")
    public String regenerateDkimKey(@RequestHeader("id") String id) {
        return service.regenerateDkimKey(id);
    }

    @GetMapping("/get-server-host")
    public String getServerHost(@RequestHeader("id") String id) {
        return service.getServerHost(id);
    }

}
