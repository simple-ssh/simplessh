package simplessh.com.dao;

public class Dovecot {

    public String config(String domain){
        //chmod +x sed_commands.sh
      String data = "sudo sed -i '/^!include_try \\/usr\\/share\\// s/^/#/' /etc/dovecot/dovecot.conf\n" +
              "sudo sed -i '/^protocols =/ s/^/#/' /etc/dovecot/dovecot.conf\n" +
              "sudo sed -i '/^postmaster_address =/ s/^/#/' /etc/dovecot/dovecot.conf\n" +
              "sudo sed -i '22i!include_try \\/usr\\/share\\/dovecot\\/protocols.d\\/*.protocol' /etc/dovecot/dovecot.conf\n" +
              "sudo sed -i '23iprotocols = imap pop3 lmtp' /etc/dovecot/dovecot.conf\n" +
              "sudo sed -i '24ipostmaster_address = postmaster@"+domain+"' /etc/dovecot/dovecot.conf\n";
      return data;
    }


    public String f10Mail(){

        String data = "sudo sed -i '/^mail_location =/ s/^/#/' /etc/dovecot/conf.d/10-mail.conf\n" +
                "sudo sed -i '/^mail_privileged_group =/ s/^/#/' /etc/dovecot/conf.d/10-mail.conf\n" +
                "sudo sed -i '31imail_location = maildir:/var/mail/vhosts/%d/%n/' /etc/dovecot/conf.d/10-mail.conf\n" +
                "sudo sed -i '32imail_privileged_group = mail' /etc/dovecot/conf.d/10-mail.conf\n" ;
        return data;
    }

    public String f10Auth(){

        String data = "sudo sed -i '/^disable_plaintext_auth =/ s/^/#/' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '/^auth_mechanisms =/ s/^/#/' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '/^!include auth-system.conf.ext/ s/^/#/' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '/^!include auth-sql.conf.ext/ s/^/#/' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '10idisable_plaintext_auth = yes' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '11iauth_mechanisms = plain login' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '12i!include auth-system.conf.ext' /etc/dovecot/conf.d/10-auth.conf\n" +
                "sudo sed -i '13i!include auth-sql.conf.ext' /etc/dovecot/conf.d/10-auth.conf\n" ;
        return data;
    }

    public String f10SSL(String domain){

        String data = "sudo sed -i '/^ssl =/ s/^/#/' /etc/dovecot/conf.d/10-ssl.conf\n" +
                "sudo sed -i '/^ssl_cert =/ s/^/#/' /etc/dovecot/conf.d/10-ssl.conf\n" +
                "sudo sed -i '/^ssl_key =/ s/^/#/' /etc/dovecot/conf.d/10-ssl.conf\n" +
                "sudo sed -i '10issl = required' /etc/dovecot/conf.d/10-ssl.conf\n" +
                "sudo sed -i '12issl_cert = </etc/letsencrypt/live/"+domain+"/fullchain.pem' /etc/dovecot/conf.d/10-ssl.conf\n" +
                "sudo sed -i '13issl_key = </etc/letsencrypt/live/"+domain+"/privkey.pem' /etc/dovecot/conf.d/10-ssl.conf\n" ;
        return data;
    }
    public String dovecotSql(String dbName, String dbUser, String dbPassword){

        String data = "sudo sed -i '/^driver =/ s/^/#/' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '/^connect =/ s/^/#/' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '/^default_pass_scheme =/ s/^/#/' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '/^password_query =/ s/^/#/' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '32idriver = mysql' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '33iconnect = host=127.0.0.1 dbname="+dbName+" user="+dbUser+" password="+dbPassword+"' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '34idefault_pass_scheme = SHA512-CRYPT' /etc/dovecot/dovecot-sql.conf.ext\n" +
                "sudo sed -i '35ipassword_query = SELECT email as user, password FROM virtual_users WHERE email='%u';' /etc/dovecot/dovecot-sql.conf.ext\n" ;
        return data;
    }
    public String f10AuthSql(String domain){
        //chmod +x sed_commands.sh
        String data = "# Authentication for SQL users. Included from 10-auth.conf.\n" +
                "#\n" +
                "# <doc/wiki/AuthDatabase.SQL.txt>\n" +
                "\n" +
                "passdb {\n" +
                "  driver = sql\n" +
                "\n" +
                "  # Path for SQL configuration file, see example-config/dovecot-sql.conf.ext\n" +
                "  args = /etc/dovecot/dovecot-sql.conf.ext \n" +
                "}\n" +
                "\n" +
                "# \"prefetch\" user database means that the passdb already provided the\n" +
                "# needed information and there's no need to do a separate userdb lookup.\n" +
                "# <doc/wiki/UserDatabase.Prefetch.txt>\n" +
                "#userdb {\n" +
                "#  driver = prefetch\n" +
                "#}\n" +
                "\n" +
                "userdb {\n" +
                "  driver = static\n" +
                "  args = uid=vmail gid=vmail home=/var/mail/vhosts/%d/%n\n" +
                "}\n" +
                "\n" +
                "#userdb {\n" +
                "#  driver = sql\n" +
                "#  args = /etc/dovecot/dovecot-sql.conf.ext\n" +
                "#}\n" +
                "\n" +
                "# If you don't have any user-specific settings, you can avoid the user_query\n" +
                "# by using userdb static instead of userdb sql, for example:\n" +
                "# <doc/wiki/UserDatabase.Static.txt>" ;
        return data;
    }
    public String f10Master(){
        String data = "#default_process_limit = 100\n" +
                "#default_client_limit = 1000\n" +
                "\n" +
                "# Default VSZ (virtual memory size) limit for service processes. This is mainly\n" +
                "# intended to catch and kill processes that leak memory before they eat up\n" +
                "# everything.\n" +
                "#default_vsz_limit = 256M\n" +
                "\n" +
                "# Login user is internally used by login processes. This is the most untrusted\n" +
                "# user in Dovecot system. It shouldn't have access to anything at all.\n" +
                "#default_login_user = dovenull\n" +
                "\n" +
                "# Internal user is used by unprivileged processes. It should be separate from\n" +
                "# login user, so that login processes can't disturb other processes.\n" +
                "#default_internal_user = dovecot\n" +
                "\n" +
                "service imap-login {\n" +
                "  inet_listener imap {\n" +
                "     port = 0\n" +
                "  }\n" +
                "  inet_listener imaps {\n" +
                "    port = 993\n" +
                "    ssl = yes\n" +
                "  }\n" +
                "\n" +
                "  # Number of connections to handle before starting a new process. Typically\n" +
                "  # the only useful values are 0 (unlimited) or 1. 1 is more secure, but 0\n" +
                "  # is faster. <doc/wiki/LoginProcess.txt>\n" +
                "  #service_count = 1\n" +
                "\n" +
                "  # Number of processes to always keep waiting for more connections.\n" +
                "  #process_min_avail = 0\n" +
                "\n" +
                "  # If you set service_count=0, you probably need to grow this.\n" +
                "  #vsz_limit = $default_vsz_limit\n" +
                "}\n" +
                "\n" +
                "service pop3-login {\n" +
                "  inet_listener pop3 {\n" +
                "    port = 0\n" +
                "  }\n" +
                "  inet_listener pop3s {\n" +
                "     port = 995\n" +
                "     ssl = yes\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "service submission-login {\n" +
                "  inet_listener submission {\n" +
                "    #port = 587\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "service lmtp {\n" +
                "  #unix_listener lmtp {\n" +
                "    #mode = 0666\n" +
                "  #}\n" +
                "\n" +
                "  unix_listener /var/spool/postfix/private/dovecot-lmtp {\n" +
                "    #mode = 0666i\n" +
                "    mode = 0600\n" +
                "    user = postfix\n" +
                "    group = postfix\n" +
                "  }\n" +
                "\n" +
                "  # Create inet listener only if you can't use the above UNIX socket\n" +
                "  #inet_listener lmtp {\n" +
                "    # Avoid making LMTP visible for the entire internet\n" +
                "    #address =\n" +
                "    #port = \n" +
                "  #}\n" +
                "}\n" +
                "\n" +
                "service imap {\n" +
                "  # Most of the memory goes to mmap()ing files. You may need to increase this\n" +
                "  # limit if you have huge mailboxes.\n" +
                "  #vsz_limit = $default_vsz_limit\n" +
                "\n" +
                "  # Max. number of IMAP processes (connections)\n" +
                "  #process_limit = 1024\n" +
                "}\n" +
                "\n" +
                "service pop3 {\n" +
                "  # Max. number of POP3 processes (connections)\n" +
                "  #process_limit = 1024\n" +
                "}\n" +
                "\n" +
                "service submission {\n" +
                "  # Max. number of SMTP Submission processes (connections)\n" +
                "  #process_limit = 1024\n" +
                "}\n" +
                "\n" +
                "service auth {\n" +
                "  # auth_socket_path points to this userdb socket by default. It's typically\n" +
                "  # used by dovecot-lda, doveadm, possibly imap process, etc. Users that have\n" +
                "  # full permissions to this socket are able to get a list of all usernames and\n" +
                "  # get the results of everyone's userdb lookups.\n" +
                "  #\n" +
                "  # The default 0666 mode allows anyone to connect to the socket, but the\n" +
                "  # userdb lookups will succeed only if the userdb returns an \"uid\" field that\n" +
                "  # matches the caller process's UID. Also if caller's uid or gid matches the\n" +
                "  # socket's uid or gid the lookup succeeds. Anything else causes a failure.\n" +
                "  #\n" +
                "  # To give the caller full permissions to lookup all users, set the mode to\n" +
                "  # something else than 0666 and Dovecot lets the kernel enforce the\n" +
                "  # permissions (e.g. 0777 allows everyone full permissions).\n" +
                "  \n" +
                "  unix_listener /var/spool/postfix/private/auth {\n" +
                "    mode = 0660\n" +
                "    user = postfix\n" +
                "    group = postfix\n" +
                "  }\n" +
                "\n" +
                "  unix_listener auth-userdb {\n" +
                "    mode = 0600\n" +
                "    user = vmail\n" +
                "  }\n" +
                "  \n" +
                "  user = dovecot\n" +
                "\n" +
                "  # Postfix smtp-auth\n" +
                "  #unix_listener /var/spool/postfix/private/auth {\n" +
                "  #  mode = 0666\n" +
                "  #}\n" +
                "\n" +
                "  # Auth process is run as this user.\n" +
                "  #user = $default_internal_user\n" +
                "}\n" +
                "\n" +
                "service auth-worker {\n" +
                "  # Auth worker process is run as root by default, so that it can access\n" +
                "  # /etc/shadow. If this isn't necessary, the user should be changed to\n" +
                "  # $default_internal_user.\n" +
                "  #user = root\n" +
                "  user = vmail\n" +
                "}\n" +
                "\n" +
                "service dict {\n" +
                "  # If dict proxy is used, mail processes should have access to its socket.\n" +
                "  # For example: mode=0660, group=vmail and global mail_access_groups=vmail\n" +
                "  unix_listener dict {\n" +
                "    #mode = 0600\n" +
                "    #user = \n" +
                "    #group = \n" +
                "  }\n" +
                "}" ;
        return data;
    }
}
