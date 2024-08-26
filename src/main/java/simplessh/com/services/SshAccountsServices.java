package simplessh.com.services;

import com.google.gson.Gson;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import simplessh.com.Helpers;
import simplessh.com.dao.SshAccount;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 *
 * SSH accounts services
 */

@Service
public class SshAccountsServices {
    private KeyStoreService keyStoreService;

    public SshAccountsServices(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }

    /**
     * get list of ssh account
     */
    public List<SshAccount> getList() {
        return setStars(keyStoreService.getSshAcconts());
    }

    /**
     * insert update ssh account
     */
   public List<SshAccount> addDataInTheTable(SshAccount data ) {
        List<SshAccount> acc = keyStoreService.getSshAcconts();
        if(data.getId().isEmpty()){
            data.setId(Helpers.getAlphaNumericString(7));
            acc.add(data);
        }else{
            acc.stream().filter(e->e.getId().compareTo(data.getId())==0)
                        .forEach(e->{
                             e.setPlatform(data.getPlatform());
                             e.setSshHost(data.getSshHost());
                             e.setSshLog(data.getSshLog());
                             e.setSshPort(data.getSshPort());

                             if(data.getSshPass().compareTo("****")!=0)
                               e.setSshPass(data.getSshPass());

                             if(data.getSshPem().compareTo("****")!=0)
                               e.setSshPem(data.getSshPem());


                             e.setMysqlLog(data.getMysqlLog());

                             if(data.getMysqlPass().compareTo("****")!=0)
                               e.setMysqlPass(data.getMysqlPass());

                             e.setFast(data.getFast());
                        });

        }

        //save data to keystore unde the entry name: sshAccounts
        keyStoreService.setKeyStoreValue("sshaccounts", (new Gson()).toJson(acc));
        return setStars(acc);
    }

    public String changeJWTToken(){
         Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
         String base64Key = Encoders.BASE64.encode(key.getEncoded());
         keyStoreService.setKeyStoreValue("jwtkey", base64Key);
         return  "ok";
    }

    /**
     * remove ssh account by key
     */

    public List<SshAccount> removeAccount(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<SshAccount> acc = keyStoreService.getSshAcconts().stream().filter(e->e.getId()
                                          .compareTo(id)!=0).collect(Collectors.toList());

        //save data to keystore unde the entry name: sshaccounts
         keyStoreService.setKeyStoreValue("sshaccounts", (new Gson()).toJson(acc));
         return setStars(acc);
    }

    /**
     * get list for bottom select
     */
    public List<SshAccount> getListHeader() {
        return setStars(keyStoreService.getSshAcconts());
    }

    private List<SshAccount> setStars(List<SshAccount> acc){
        if(acc != null)
          acc.forEach(e->{ e.setSshPassStar();  e.setSshPemStar(); e.setMysqlPassStar(); });

       return acc == null ? new ArrayList<>() : acc;
    }

}
