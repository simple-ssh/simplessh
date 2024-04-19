package simplessh.com.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplessh.com.Helpers;
import simplessh.com.services.CustomUserDetailsService;
import simplessh.com.services.KeyStoreService;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private KeyStoreService keyStoreService;

    private Key getKey(){
        String jwtKey = keyStoreService.getKeyStoreValue("jwtkey");
        //String base64Key = Encoders.BASE64.encode(key.getEncoded());
        return new SecretKeySpec(Base64.getDecoder().decode(jwtKey), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateJwtToken(String userName) {

       //Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
     return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getKey()).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }
}