package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import simplessh.com.dao.SshAccount;
import simplessh.com.services.SshAccountsServices;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Corneli F.
 *
 * SSH accounts controller
 */

@RestController
@RequestMapping("/api/v1/")
public class SshAccountsController {

    @Autowired
    private SshAccountsServices service;

    /**
     * get list of ssh account
     * @return
     */
    @GetMapping("/get-list-of-accounts")
    public List<SshAccount> getList() {

        return service.getList();
    }

    /**
     * insert update ssh account
     * @param data
     * @return
     */
    @PutMapping(path = "/add-update-settings-account", consumes = "application/json", produces = "application/json")
    public List<SshAccount> addDataInTheTable(@RequestBody SshAccount data ) {

        return service.addDataInTheTable(data);
    }

    /**
     * remove ssh account by key
     * @param request
     * @return
     */
    @DeleteMapping("/remove-setting-account")
    public List<SshAccount> removeAccount(HttpServletRequest request) {

        return service.removeAccount(request);
    }

    /**
     * get list for bottom select
     * @return
     */
    @GetMapping("/get-header-list-of-accounts")
    public List<SshAccount> getListHeader() {

        return service.getListHeader();
    }


}
