package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.response.ListMapResponse;
import simplessh.com.services.DomainsServices;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Corneli F.
 *
 * DomainsController
 */
@RestController
@RequestMapping("/api/v1/")
public class DomainsController {

    @Autowired
    private DomainsServices service;

    /**
     * get list of domains
     * @param id
     * @return
     */
   @GetMapping("/get-list-of-domains")
    public List<Map<String,String>> getList(@RequestHeader("id") String id) {

        return service.getList(id);
    }

    /**
     * suspend domain
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/suspend-activate-domain")
    public List<Map<String,String>> suspendActivateDomain(@RequestHeader("id") String id,
                                                           HttpServletRequest request) {

        return service.suspendActivateDomain(id, request);
    }


    /***
     * add new domain
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/add-new-domain" , consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> addNewDomain(@RequestHeader("id") String id,
                                                 @RequestBody Map<String, String> data ) {

        return service.addNewDomain(id, data);
    }

    /**
     *  install Let's encrypt SSL
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/install-ssl" , consumes = "application/json", produces = "application/json")
    public ListMapResponse changePassword(@RequestHeader("id") String id, @RequestBody Map<String, String> data ) {

        return service.installSSLToDomain(id,data);
      }

    /**
     * renew domain
     * @param id
     * @return
     */
    @GetMapping("/renew-ssl")
    public ListMapResponse renewSSL(@RequestHeader("id") String id) {

        return service.renewSSL(id);
    }

    /**
     * edit dns
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/edit-dns" , consumes = "application/json", produces = "application/json")
    public List<Map<String,String>> changePath(@RequestHeader("id") String id,
                                               @RequestBody Map<String, String> data ) {

       return service.changePath(id, data);
    }

    /**
     * remove domain
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/remove-domain")
    public List<Map<String,String>> removeDomain(@RequestHeader("id") String id, HttpServletRequest request ) {

        return service.removeDomain(id, request);
    }

    /**
     * setup dns to domain
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/setup-dns" , consumes = "application/json", produces = "application/json")
    public String setupDNS(@RequestHeader("id") String id, @RequestBody Map<String, String> data ) {

        return service.setupDNS(id, data);
    }

}
