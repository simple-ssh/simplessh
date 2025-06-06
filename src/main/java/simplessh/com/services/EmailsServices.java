package simplessh.com.services;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplessh.com.Helpers;
import simplessh.com.dao.PerformDataImpl;
import simplessh.com.dao.SshAccount;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 */
@Service
public class EmailsServices extends PerformDataImpl {

    private FileService fileService;
    private SshCommand ssh;
    private KeyStoreService keyStoreService;

    public EmailsServices(FileService fileService, SshCommand ssh, KeyStoreService keyStoreService) {
        this.fileService = fileService;
        this.ssh = ssh;
        this.keyStoreService = keyStoreService;
    }

    /**
     * get list of emails
     */
    public List<Map<String,String>> getList(String id) {
       Map<String, String> map = getDbData(id);

          //map.getOrDefault("user","")
         //map.getOrDefault("password","")

        if(map.isEmpty())
            return null;

        String dbName = map.getOrDefault("dbname","");
        String listOfEmails = ssh.execute( "mysql_select", id, "vu.id, vu.email, vu.domain_id as domainID, va.destination, va.id AS idDestination ",
                dbName+".virtual_users vu LEFT JOIN "+dbName+".virtual_aliases va ON va.source= vu.email" );

        return  extractTheData(listOfEmails);
    }


    public Map<String,String> getDbData(String id) {
        String dbContent = ssh.execute( "get_file_content", id, "/etc/postfix/mysql-virtual-email2email.cf");

        return Arrays.stream(dbContent.split("\n"))
                .map(line -> line.split("="))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(e->e[0].trim(), e->e[1].trim()));
    }

    /**
     * add ssl to postfix and dovecot
     */
    public Map<String,String> setupSSLToPostfix(String id, Map<String, String> data) {
        String  type = data.getOrDefault("typeSSL", "1");
        String  cert = data.getOrDefault("cert", "");
        String  key = data.getOrDefault("key", "");
        String  capath = data.getOrDefault("capath", "");
        String  domain = data.getOrDefault("domain", "");



        if(type.contains("1") && domain.isEmpty())
            return Map.of("domain","","response","Not valid domain, please!");

        if(type.contains("1") && !domain.isEmpty()){
            String isSSLInstaled= ssh.execute("executecommand", id, "ls -l /etc/letsencrypt/live/"+domain+"/cert.pem >/dev/null 2>&1 && echo \"File exists.\" || echo \"File does not exist.\"" );

            if(isSSLInstaled.contains("File does not exist"))
                return Map.of("domain",domain,"response","Let’s Encrypt SSL is not installed to the domain: "+domain+"! Please Install Let’s Encrypt SSL to domain first!");

            cert   =  "/etc/letsencrypt/live/"+domain+"/cert.pem";
            key    = "/etc/letsencrypt/live/"+domain+"/privkey.pem";
            capath = "/etc/letsencrypt/live/"+domain+"/fullchain.pem";
        }

        if(type.contains("3")){
            cert   =  "/etc/ssl/certs/ssl-cert-snakeoil.pem";
            key    = "/etc/ssl/private/ssl-cert-snakeoil.key";
            capath = "/etc/ssl/certs";
        }

        // set ssl for postfix
        ssh.execute("executecommand", id,"postconf -e 'smtpd_tls_cert_file="+cert+"'");
        ssh.execute("executecommand", id, "postconf -e 'smtpd_tls_key_file="+key+"'");

        if(!capath.isEmpty())
          ssh.execute("executecommand", id, "postconf -e 'smtp"+(type.contains("3") ? "":"d")+"_tls_CAfile="+capath+"'");

        //if is return how it was we remove smtpd_tls_CAfile if different smtp_tls_CAfile from main.cf
        //ssh.execute( "executecommand", id, "postconf -X 'smtp"+(type.contains("3") ? "d":"")+"_tls_CAfile'");

        capath   = type.contains("3") ? "/etc/dovecot/private/dovecot.pem" : capath;
        key    = type.contains("3") ? "/etc/dovecot/private/dovecot.key" : key;

        // set ssl for dovecot
        ssh.execute( "executecommand", id, "sed -i '/^ssl_cert =/s@.*@ssl_cert = <"+capath+"@' /etc/dovecot/conf.d/10-ssl.conf");
        ssh.execute( "executecommand", id, "sed -i '/^ssl_key =/s@.*@ssl_key = <"+key+"@' /etc/dovecot/conf.d/10-ssl.conf");

        // reload service postfix and close session
        ssh.execute("executecommand", id, "systemctl restart postfix dovecot");

        return Map.of("domain", "","response","Certificate added to mail server!");
    }


    /**
     * add new email
     */
    public List<Map<String,String>> addNewOne(String id, Map<String, String> data) {

        String email = data.getOrDefault("email","");
        String forward = data.getOrDefault("forward", email);
        String password = data.getOrDefault("password","");
        String passwordEncripted = Crypt.crypt(password, "$6$" + Helpers.getAlphaNumericString(16) + "$");
        String domain = email.split("@")[1];


        // this part we need to add a domain to DKIM if not exist
        String domainLine = ssh.execute("executecommand", id,"sed -n '/^Domain/p' /etc/opendkim.conf");
        if(!domainLine.isEmpty() && !domainLine.contains(domain)){
            domainLine = domainLine.replace("Domain", "");
            domainLine = domainLine.replaceAll("\\s", "");
            StringJoiner str = new StringJoiner(", ");
            Arrays.stream(domainLine.split(",")).filter(e->!e.isEmpty()).forEach(str::add);
            str.add(domain);
            ssh.execute("executecommand", id,"sed -i '/^Domain/s/.*/Domain                  "+str+"/' /etc/opendkim.conf");
          }else if(domainLine.isEmpty()){
            ssh.execute("executecommand", id,"sed -i '22iDomain                  "+domain+"' /etc/opendkim.conf");
          }
       //END DKIM

        //postsrsd
        String postsrsdLine = ssh.execute("executecommand", id,"sed -n '/^SRS_DOMAIN/p' /etc/default/postsrsd");
        if(!postsrsdLine.isEmpty() && !postsrsdLine.contains(domain)){
            postsrsdLine = postsrsdLine.replace("SRS_DOMAIN", "");
            postsrsdLine = postsrsdLine.replace("=", "");
            postsrsdLine = postsrsdLine.replaceAll("\\s", "");

            StringJoiner strPostsrsd = new StringJoiner(", ");
            Arrays.stream(postsrsdLine.split(",")).filter(e->!e.isEmpty()).forEach(strPostsrsd::add);
            strPostsrsd.add(domain);
            ssh.execute("executecommand", id,"sed -i '/^SRS_DOMAIN/s/.*/SRS_DOMAIN="+strPostsrsd+"/' /etc/default/postsrsd");
        }else if(postsrsdLine.isEmpty()){
            ssh.execute("executecommand", id,"sed -i '22iSRS_DOMAIN="+domain+"' /etc/default/postsrsd");
        }
        //end postsrsd


        Map<String, String> map = getDbData(id);

        if(map.isEmpty())
            return null;

        String db = map.getOrDefault("dbname","");

        String getDomain = ssh.execute("mysql_command", id, "INSERT INTO "+db+".virtual_domains (name) SELECT '"+domain+"' FROM DUAL WHERE NOT EXISTS (SELECT id FROM "+db+".virtual_domains WHERE name = '"+domain+"');" +
                                                                               "SELECT id FROM "+db+".virtual_domains WHERE name = '"+domain+"';");
        int domainID = Arrays.stream(getDomain.split("\\r?\\n")).
                               filter(e->e.matches("\\d+")).
                               mapToInt(Integer::parseInt).
                               findFirst().orElse(-1);

        ssh.execute("mysql_command", id,
           "INSERT INTO "+db+".virtual_users (domain_id, password , email ) VALUES ('"+domainID+"', '"+passwordEncripted.replaceAll("\\$", "\\\\\\$")+"', '"+email+"');" +
                   (!email.trim().contains(forward) ?
                   "INSERT INTO "+db+".virtual_aliases (domain_id, source, destination) VALUES ('"+domainID+"', '"+email+"', '"+forward+"');":"") );

        ssh.execute("executecommand", id,"mkdir -p /var/mail/vhosts/"+domain+"; chmod g+w /var/mail/vhosts/"+domain+"/");
        return getList(id);
    }

    public List<Map<String, String>> updateForwardEmail(String id, Map<String, String> data) {
        String idForward = data.getOrDefault("idForward","");
        String email = data.getOrDefault("email", "");
        String forward = data.getOrDefault("forward", "");
        String domainID = data.getOrDefault("domainID", "");
        Map<String, String> map = getDbData(id);
        if(map.isEmpty())
            return null;

        System.out.println("idForward:"+idForward+"; forward:"+forward+"; domainID:"+domainID);
        String db = map.getOrDefault("dbname","");

        if(idForward.isEmpty() && !forward.isEmpty()){
            ssh.execute("mysql_command", id, "INSERT INTO "+db+".virtual_aliases (domain_id, source, destination) VALUES ('"+domainID+"', '"+email+"', '"+forward+"')");
        }else if(!idForward.isEmpty() && forward.isEmpty()){
            ssh.execute("mysql_command", id, "DELETE FROM "+db+".virtual_aliases WHERE id='"+idForward+"'");
        }else {
            ssh.execute("mysql_command", id, "UPDATE "+db+".virtual_aliases SET destination = '"+forward+"' WHERE id='"+idForward+"'");
        }
       return getList(id);
    }

    /**
     * don't use no where just a simple test
     */
    public String simpleTest(String id) {
        Map<String, String> map = getDbData(id);
        if(map.isEmpty())
            return null;

        return map.getOrDefault("dbname","");
    }
    public String updatePassword(String id, Map<String, String> data) {
        String idAcc = data.getOrDefault("accid","");
        String password = data.getOrDefault("password", "");

        String passwordEncripted = Crypt.crypt(password, "$6$" + Helpers.getAlphaNumericString(16) + "$");

         Map<String, String> map = getDbData(id);
        if(map.isEmpty())
            return null;

        String db = map.getOrDefault("dbname","");
        ssh.execute("mysql_command", id, "UPDATE "+db+".virtual_users SET password = '"+passwordEncripted.replaceAll("\\$", "\\\\\\$")+"' WHERE id='"+idAcc+"';" );

        return "OK";
    }


    /**
     * remove email
     * @return List<Map<String,String>>
     */
   public List<Map<String,String>> remove(String id, HttpServletRequest request ) {
       String email = request.getParameter("email");

       Map<String, String> map = getDbData(id);
       if(map.isEmpty())
           return null;

       String db = map.getOrDefault("dbname","");
       ssh.execute("mysql_command", id, "DELETE FROM "+db+".virtual_users WHERE email='"+email+"';" +
                                                         "DELETE FROM "+db+".virtual_aliases WHERE source='"+email+"';" );


        return getList(id);
    }

    public String setupEmailServer(String id, Map<String, String> data) {
        String domain = data.getOrDefault("domain","");
        String dbName = "mailserver"+Helpers.getAlphaString(3);
        String dbUser = "mailuser"+Helpers.getAlphaString(3);
        String dbPassword = Helpers.getAlphaNumericString(6)+"@"+Helpers.getAlphaNumericString(7);
        String dbHost = "localhost";
        SshAccount sshAccount = keyStoreService.getSshAccount(id);

        if(sshAccount.getMysqlLog() == null || sshAccount.getMysqlLog().isEmpty()){
            return "Please add to SSH connections a <Mysql user name> AND/OR <Mysql password> go to: Settings->SSH connection from the top menu";
        }

        // create database for server
        ssh.executeMap("mysql_new_database_user", id, dbName, dbUser, dbHost,
                                                                 dbPassword, "ALL PRIVILEGES" );

        String virtual_domains= """
                CREATE TABLE \\`virtual_domains\\` (
                  \\`id\\` int(11) NOT NULL auto_increment,
                  \\`name\\` varchar(50) NOT NULL,
                  PRIMARY KEY (\\`id\\`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8""";

        String virtual_users= """
                CREATE TABLE \\`virtual_users\\` (
                  \\`id\\` int(11) NOT NULL auto_increment,
                  \\`domain_id\\` int(11) NOT NULL,
                  \\`password\\` varchar(106) NOT NULL,
                  \\`email\\` varchar(100) NOT NULL,
                  PRIMARY KEY (\\`id\\`),
                  UNIQUE KEY \\`email\\` (\\`email\\`),
                  FOREIGN KEY (domain_id) REFERENCES virtual_domains(id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8""";

        String virtual_aliases= """
                CREATE TABLE \\`virtual_aliases\\` (
                  \\`id\\` int(11) NOT NULL auto_increment,
                  \\`domain_id\\` int(11) NOT NULL,
                  \\`source\\` varchar(100) NOT NULL,
                  \\`destination\\` varchar(100) NOT NULL,
                  PRIMARY KEY (\\`id\\`),
                  FOREIGN KEY (domain_id) REFERENCES virtual_domains(id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8""";

        String virtual_admins= """
                CREATE TABLE \\`virtual_admins\\` (
                  \\`id\\` int(11) NOT NULL auto_increment,
                  \\`email\\` varchar(200) NOT NULL,
                  \\`password\\` varchar(200) NOT NULL,
                  \\`status\\` varchar(50) DEFAULT NULL,
                  \\`login_token\\` varchar(100) DEFAULT NULL,
                  \\`reset_token\\` varchar(100) DEFAULT NULL,
                  \\`date_log\\` datetime DEFAULT NULL,
                  PRIMARY KEY (\\`id\\`) 
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8""";

        // create database an user
        ssh.executeMap("mysql_command", id, "USE "+dbName+"; "+virtual_domains +";\n"+virtual_users +";\n"+virtual_aliases+";\n"+virtual_admins);

        // check if folder /tmp/simplessh_tmp exist if not create it
        ssh.checkForVarEasyvpsPath(id);

        // prepare postfix.sh
        String postFix = fileService.convertToString("files/postfix/postfixBash.txt");

        postFix = postFix.replaceAll("domainReplace",domain);
        postFix = postFix.replaceAll("dbNmeReplace",dbName);
        postFix = postFix.replaceAll("dbUserReplace",dbUser);
        postFix = postFix.replaceAll("dbPasswordReplace",dbPassword);
        //postFix.replaceAll("",dbHost);

        System.out.println("postFix:"+postFix);

        // prepare dovecot.sh
        String dovecot = fileService.convertToString("files/postfix/dovecot.txt");
        dovecot = dovecot.replaceAll("domainReplace",domain);
        dovecot = dovecot.replaceAll("dbNmeReplace",dbName);
        dovecot = dovecot.replaceAll("dbUserReplace",dbUser);
        dovecot = dovecot.replaceAll("dbPasswordReplace",dbPassword);
        //dovecot = dovecot.replaceAll("",dbHost);


        Map<String, InputStream> file = Map.of("postfix.sh", new ByteArrayInputStream(postFix.getBytes()),
                                               "dovecot.sh", new ByteArrayInputStream(dovecot.getBytes()));

        // upload files to /tmp/simplessh_tmp/
        ssh.sftpFastUpload(id, file, "/tmp/simplessh_tmp/", "",  "");

        // add execute permission to postfix.sh and dovecot.sh
        ssh.execute("commandline", id, "chmod +x /tmp/simplessh_tmp/postfix.sh; chmod +x /tmp/simplessh_tmp/dovecot.sh" );

        // execute file /tmp/simplessh_tmp/postfix.sh
        ssh.execute("commandline", id, "/tmp/simplessh_tmp/postfix.sh" );

        try { Thread.sleep(2000); } catch (Exception ignored) { }

        // execute file /tmp/simplessh_tmp/postfix.sh
        ssh.execute("commandline", id, "/tmp/simplessh_tmp/dovecot.sh" );

        try { Thread.sleep(2000); } catch (Exception ignored) { }
        // remove files
        ssh.execute("commandline", id, "rm /tmp/simplessh_tmp/postfix.sh; rm /tmp/simplessh_tmp/dovecot.sh" );

        return "It's look like all done, give a try and see, don't forget to add the DNS spf,dmark,dkim TXT record to your domain(s)";
    }

    public Map<String, String> getDkimInfo(String id) {
         String data =  ssh.execute( "get_file_content", id, "/root/mail.txt");
         try {
             if(data.contains("p=")) {
                 String[] split1 = data.split("p=");
                 String[] key = split1[1].replaceAll("\n", "").replaceAll("\"", "").split("\\)");
                 return Map.of("key", key[0].replaceAll("\\s", ""), "response", "ok");
             }else {
                 return Map.of("key", "", "response", "No DKIM key not found, please install/reconfigure DKIM");
             }
         }catch (Exception e){
             return Map.of("key", "", "response", e.getMessage());
         }
    }

    public String regenerateDkimKey(String id) {
           String dname=  ssh.execute("executecommand", id, "postconf -n myhostname" );

           String[] split = dname.split("=");
           if(split.length <=1 || split[1].contains("localhost"))
             return "Not valid domain, please setup manually by run(replace yourdomain.com with your real domain): sudo postconf -e \"myhostname = yourdomain.com\"! " ;


           ssh.execute("executecommand", id, "opendkim-genkey -t -s mail -d "+split[1] );
           ssh.execute("executecommand", id, "cp mail.private /etc/postfix/dkim.key");

          return "Ok";
    }

    public Map<String,String> checkDNS(String id, String domainName, String ip) {
        String spf=  ssh.execute("executecommand", id, "dig "+domainName+" TXT +short | grep '^\\\"'" );
        String dmarc=  ssh.execute("executecommand", id, "dig _dmarc."+domainName+" TXT +short | grep '^\\\"'" );
        try{Thread.sleep(1000);}catch(Exception e){}
        String dkim=  ssh.execute("executecommand", id, "dig mail._domainkey."+domainName+" TXT +short | grep '^\\\"'" );
        String mx=  ssh.execute("executecommand", id, "dig "+domainName+" MX +short | grep '^'" );
        try{Thread.sleep(1000);}catch(Exception e){}
        String rdns=  ssh.execute("executecommand", id, "dig -x "+ip+" +short | grep '^'" );

        String dkimKey =  ssh.execute( "get_file_content", id, "/root/mail.txt");
               dkim = dkim.replaceAll("\"", "").replaceAll("\n", "").replaceAll("\\s", "");

        String finalDkim = dkim;
        Map<String,String> response = new HashMap<>(){{
                                        put("spf",spf);
                                        put("dmark",dmarc);
                                        put("dkim", finalDkim);
                                        put("mx",mx);
                                        put("rdns",rdns);
                                        put("dkimok","bi-dash-circle");
                                        put("dmarkok",(dmarc.contains("p=") ? "bi-check2-circle":"bi-dash-circle"));
                                        put("spfok",(spf.contains("v=spf1") ? "bi-check2-circle":"bi-dash-circle"));
                                        put("mxok",(mx.contains("mail."+domainName) ? "bi-check2-circle":"bi-dash-circle"));
                                        put("rdnsok",(!rdns.isEmpty() ? "bi-check2-circle":"bi-dash-circle"));
                                 }};

        if(dkimKey.contains("p=") && !dkim.isEmpty() && dkim.contains("p=")) {
            String[] split1 = dkimKey.split("p=");
            String[] key = split1.length >0 ?  split1[1].replaceAll("\n", "").replaceAll("\"", "").split("\\)") : null;
            String finalKey = key!=null? key[0].replaceAll("\\s", ""):"";
            if(finalKey!=null && !finalKey.isEmpty() && dkim.contains(finalKey))
                response.put("dkimok","bi-check2-circle");
           }else{
                response.put("dkim", "YOUR DKIM DO NOT MATCH WITH YOUR DNS; <br/>" +
                                   "Solutions 1: if you just update your dns, restart Bind9 and check again or later!<br/>" +
                                   "Solutions 2: Update your dkim, restart bind9 and check again or later!<br/>"+finalDkim);
           }

        return response;
    }

    public String getServerHost(String id) {
       return  ssh.execute("executecommand", id, "hostname -f" );
    }
}
