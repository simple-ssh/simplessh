package simplessh.com.controllers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;


/**
 * @author Corneli F.
 *
 * Home Controller
 */
@Controller
public class HomeController {

  /**
     * Home page
     * @param request
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
       return "build/index";
    }




    /**
     * login page
     * @param request
     * @return
     */
    @GetMapping("/accessDenied")
    public String accessDenied(HttpServletRequest request) {
        return "accessDenied";
    }


    /**
     * login page
     * @param request
     * @return
     */
    @GetMapping("/readk")
    public String readk(HttpServletRequest request) {
        /*
        char[] pass= "keypassword".toCharArray();

        SecretKey key;
        KeyStore.ProtectionParameter  protectionParameter;
        KeyStore.SecretKeyEntry secretKeyEntry;

        char[] encrypt = "dssdgsd sdg sdfgsdfgs dfh sdfh sdfh sdfh sdfh sdfh sdfh sdfh dg dfg dfg sdfhdg hdgh  dgj dgfj dgfj dfgj dfgj dfgj dfgj dfgj".toCharArray();

        try{

            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(null, pass);
            FileOutputStream out = new FileOutputStream(new File("my-keystore.jceks"));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
              key = keyFactory.generateSecret(new PBEKeySpec(encrypt));

            protectionParameter = new KeyStore.PasswordProtection(pass);
            secretKeyEntry = new KeyStore.SecretKeyEntry(key);

            ks.setEntry("my-alias", secretKeyEntry, protectionParameter);


            ks.store(out,pass);
            out.close();

        }catch (Exception e){
            System.out.printf("Error set:"+e.getMessage());
        }


           try{
               InputStream is = new FileInputStream(new File("my-keystore.jceks"));
               KeyStore ks = KeyStore.getInstance("JCEKS");
               ks.load(is, pass);

               KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(pass);
               KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) ks.getEntry("my-alias", pp);
               SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHMD5ANDDES");
               PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(ske.getSecretKey(), PBEKeySpec.class);
               String password = new String(keySpec.getPassword());
               System.out.println(password);

           }catch (Exception e){
               System.out.printf("Error get:"+e.getMessage());
           }

         */

        return "accessDenied";
    }

}
