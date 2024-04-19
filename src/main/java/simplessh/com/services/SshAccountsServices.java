package simplessh.com.services;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import simplessh.com.Helpers;
import simplessh.com.dao.SshAccount;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 *
 * SSH accounts services
 */

@Service
public class SshAccountsServices {

    @Autowired
    private KeyStoreService keyStoreService;

    /**
     * get list of ssh account
     * @return
     */
    public List<SshAccount> getList() {
         List<SshAccount> acc = keyStoreService.getSshAcconts();

        acc.forEach(e->{ e.setSshPassStar();  e.setSshPemStar(); e.setMysqlPassStar(); });

        return acc ;
    }

    /**
     * insert update ssh account
     * @param data
     * @return
     */
   public List<SshAccount> addDataInTheTable(SshAccount data ) {
        List<SshAccount> acc = keyStoreService.getSshAcconts();
        if(data.getId().isEmpty()){
            data.setId(Helpers.getAlphaNumericString(7));
            acc.add(data);
        }else{
            acc.forEach(e->{  if(e.getId().compareTo(data.getId())==0){
                                 e.setPlatform(data.getPlatform());
                                 e.setSshHost(data.getSshHost());
                                 e.setSshLog(data.getSshLog());

                                 if(data.getSshPass().compareTo("****")!=0)
                                   e.setSshPass(data.getSshPass());

                                 if(data.getSshPem().compareTo("****")!=0)
                                   e.setSshPem(data.getSshPem());


                                 e.setMysqlLog(data.getMysqlLog());

                                 if(data.getMysqlPass().compareTo("****")!=0)
                                   e.setMysqlPass(data.getMysqlPass());

                                 e.setFast(data.getFast());


            }});

        }

        //save data to keystore unde the entry name: sshAccounts
        keyStoreService.setKeyStoreValue("sshaccounts", (new Gson()).toJson(acc));

        List<SshAccount> returnData = acc;
        returnData.forEach(e->{ e.setSshPassStar();  e.setSshPemStar(); e.setMysqlPassStar(); });

        return returnData;
    }

    /**
     * remove ssh account by key
     * @param request
     * @return
     */

    public List<SshAccount> removeAccount(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<SshAccount> acc = keyStoreService.getSshAcconts().stream().filter(e->e.getId()
                                          .compareTo(id)!=0).collect(Collectors.toList());

        //save data to keystore unde the entry name: sshaccounts
         keyStoreService.setKeyStoreValue("sshaccounts", (new Gson()).toJson(acc));

        acc.forEach(e->{ e.setSshPassStar();  e.setSshPemStar(); e.setMysqlPassStar(); });

        return acc;
    }

    /**
     * get list for bottom select
     * @return
     */
    public List<SshAccount> getListHeader() {
        List<SshAccount> acc = keyStoreService.getSshAcconts();
        acc.forEach(e->{  e.setSshPass(""); e.setSshPem(""); e.setMysqlLog("");   e.setMysqlPass(""); });
        return acc;
    }


}
