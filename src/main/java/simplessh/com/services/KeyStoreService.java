package simplessh.com.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import simplessh.com.dao.SshAccount;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Corneli F.
 */

/**
 * This service is for key store
 */
@Service
@Slf4j
public class KeyStoreService {
    public String fileKeyStoreName = "data-setore.jceks";

    @Value("${keystore.pass}")
    private String passToFile;

    /**
     * it will get store value by entry name
     * @param entryName
     * @return
     */
    public String getKeyStoreValue(String entryName){
        char[] pass = passToFile.toCharArray();
        String value= "";

        try{
            InputStream is = new FileInputStream(fileKeyStoreName);
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(is, pass);

            KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(pass);
            KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) ks.getEntry(entryName, pp);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHMD5ANDDES");
            PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(ske.getSecretKey(), PBEKeySpec.class);
            value = new String(keySpec.getPassword());
        }catch (Exception e){
            log.error("Error get:"+e.getMessage());
        }

        return value;
    }

    /**
     * it will set an entry value by entry name
     * @param entryName
     * @param entryValue
     * @return
     */
    public String setKeyStoreValue(String entryName, String entryValue){
        String message ="success";


           // check if file exist if not we create it

            char[] pass= passToFile.toCharArray();

            try{
                InputStream is = new FileInputStream(fileKeyStoreName);

                KeyStore ks = KeyStore.getInstance("JCEKS");
                ks.load(is, pass);

                FileOutputStream out = new FileOutputStream(fileKeyStoreName);
                setEntryKeyStore(ks, entryName, entryValue);

                ks.store(out,pass);
                out.close();

            }catch (Exception e){
                System.out.printf("Error set:"+e.getMessage());
                message = e.getMessage();
            }


        return message;
    }

    /**
     * Small helper wwhat will set new entry
     * @param ks
     * @param entryName
     * @param entryData
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws KeyStoreException
     */
      public void setEntryKeyStore(KeyStore ks, String entryName, String entryData )
            throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException {

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(entryData.toCharArray()));

        KeyStore.ProtectionParameter  protectionParameter = new KeyStore.PasswordProtection(passToFile.toCharArray());
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);

        ks.setEntry(entryName, secretKeyEntry, protectionParameter);
     }

     public LinkedHashMap<String,String> getUsers(){
         String json =  getKeyStoreValue("users");

         if(json.isEmpty())
             return new LinkedHashMap<>();

         LinkedHashMap<String,String> users = new LinkedHashMap<>();


         try {
             Type type = new TypeToken<LinkedHashMap<String,String>>(){}.getType();
             users = (new Gson()).fromJson(json, type);
         }catch (Exception e){
             log.error("error to convert:"+e.getMessage());
         }

        return users;
     }

    public List<SshAccount> getSshAcconts(){
        String json =  getKeyStoreValue("sshaccounts");

        List<SshAccount> users =  new ArrayList<>();
        if(json.isEmpty())
            return new ArrayList<>();

        try {
            Type type = new TypeToken<List<SshAccount>>(){}.getType();
            users = (new Gson()).fromJson(json, type);
        }catch (Exception e){
            log.error("error to convert:"+e.getMessage());
        }

        return users == null ? new ArrayList<>() : users;
    }

    public SshAccount getSshAccount(String id){
        String json =  getKeyStoreValue("sshaccounts");

        List<SshAccount> users =  new ArrayList<>();
        if(json.isEmpty())
            return new SshAccount();

        try {
            Type type = new TypeToken<List<SshAccount>>(){}.getType();
            users = (new Gson()).fromJson(json, type);
        }catch (Exception e){
            log.error("error to convert:"+e.getMessage());
        }

        if(users != null && users.size()>0){
            return users.stream().filter(e->e.getId().compareTo(id)==0).findAny().orElse(new SshAccount());

        }

        return new SshAccount();
    }

    public String getSshAccountByName(String id, String name){
        return getSshAccount(id).getByName(name);
    }


    /**
     * this method will run when spring is loaded only oance
     */
    public void setUp(){
        String fileName = fileKeyStoreName;

        File f = new File(fileName);
        // check if file exist if not we create it
        if(!f.exists() || f.isDirectory()) {
            char[] pass= passToFile.toCharArray();

            try{
                KeyStore ks = KeyStore.getInstance("JCEKS");
                ks.load(null, pass);
                FileOutputStream out = new FileOutputStream(fileName);

                setEntryKeyStore(ks, "sshaccounts","");
                setEntryKeyStore(ks, "users","{\"admin\":\""+new BCryptPasswordEncoder().encode("admin")+"\"}");

                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                String base64Key = Encoders.BASE64.encode(key.getEncoded());

                setEntryKeyStore(ks, "jwtkey",base64Key);

                ks.store(out,pass);
                out.close();

            }catch (Exception e){
               log.error("Error set:"+e.getMessage());
            }
        }

    }

}
