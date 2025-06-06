package simplessh.com.services;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;

/**
 * @author Corneli F.
 */

@Service
public class KeyStoreSetup {

   @Autowired
   private KeyStoreService keyStoreService;

    @Value("${keystore.pass}")
    private String keystore;



}
