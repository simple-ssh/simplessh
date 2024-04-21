package simplessh.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplessh.com.services.SshCommand;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Corneli F.
 *
 * Install controller
 */

@Service
public class InstallationServices {
    private SshCommand ssh;

    public InstallationServices(SshCommand ssh) {
        this.ssh = ssh;
    }

    /**
     * check if app is instaled
     * @param id
     * @param request
     * @return
     */
   public String checkStatus(String id, HttpServletRequest request) {
      String name = request.getParameter("name");
       if(name.contains("php")){
           String is = ssh.execute("check_if_repository_is_set", id, "ondrej/php");
           if(!is.contains("ondrej")){
              ssh.execute("add_repository", id, "ppa:ondrej/php");
           }
       }
      String result = ssh.execute("check_app_is_install", id, name);
      return result;
    }

    /**
     * uninstall app
     * @param id
     * @param request
     * @return
     */
    public String uninstall(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        String result = ssh.execute("remove_app", id, name);

        if(name.contains("dovecot"))
        ssh.execute("remove_app", id, "--auto-remove dovecot-core");

        ssh.execute("apt_get_update", id);
        return result;
    }


    /**
     * install app
     * @param id
     * @param data
     * @param request
     * @return
     */
    public String install(String id, Map<String, String> data, HttpServletRequest request) {
        String name = data.getOrDefault("name","");
        String additional = data.getOrDefault("additional","");

        ssh.execute("apt_get_update", id);

        if(name.contains("php")){
            String is = ssh.execute("check_if_repository_is_set", id, "ondrej/php");
            if(!is.contains("ondrej")){
                try{Thread.sleep(1000);}catch (Exception e){}
                ssh.execute("add_repository", id, "ppa:ondrej/php");
            }
        }

        try{Thread.sleep(1000); }catch (Exception e){}

        if(name.contains("postfix")){
          ssh.execute("executecommand",
                       id, "debconf-set-selections <<< 'postfix postfix/mailname string "+additional+"'");
          ssh.execute("commandline",
                       id, "debconf-set-selections <<< 'postfix postfix/main_mailer_type string 'Internet Site'\"");
        }


        String result = ssh.execute("install",  id, name);

        //if install postfix and dovecot
        /*if(name.contains("postfix")){

            //add options to /etc/postfix/master.cf
            ssh.execute("executecommand",
                    id, "postconf -M submission/inet='submission inet n       -       y       -       -       smtpd'");
            ssh.execute("executecommand",
                    id, "postconf -P submission/inet/syslog_name=postfix/submission");
            execute("executecommand",
                    id, "postconf -P submission/inet/smtpd_tls_security_level=encrypt");
            execute("executecommand",
                    id, "postconf -P submission/inet/smtpd_sasl_auth_enable=yes");

            try{Thread.sleep(500); }catch (Exception e){}
            execute("executecommand",
                    id, "postconf -M smtps/inet='smtps     inet  n       -       y       -       -       smtpd'");
            execute("executecommand",
                    id,"postconf -P smtps/inet/syslog_name=postfix/smtps" );
            execute("executecommand",
                    id, "postconf -P smtps/inet/smtpd_tls_wrappermode=yes");
            execute("executecommand",
                    id, "postconf -P smtps/inet/smtpd_sasl_auth_enable=yes");
            //end options to /etc/postfix/master.cf


            // create file /etc/postfix/virtual
            execute("put_content_in_file_simple",
                    id,  "","/etc/postfix/virtual");

            try{Thread.sleep(500); }catch (Exception e){}

            //add options to /etc/postfix/main.cf
            execute("executecommand",
                    id, "postconf -e 'smtpd_sasl_type = dovecot'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_sasl_path = private/auth'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_sasl_security_options = noanonymous'");
            execute("executecommand",
                    id, "postconf -e 'broken_sasl_auth_clients = yes'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_sasl_auth_enable = yes'");
            try{Thread.sleep(1000); }catch (Exception e){}

            execute("executecommand",
                    id, "postconf -e 'smtpd_recipient_restrictions = permit_mynetworks, permit_sasl_authenticated, reject_unauth_destination'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_tls_security_level = may'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_tls_received_header = yes'");

            execute("executecommand",
                    id, "postconf -e 'smtpd_tls_auth_only = no'");

            execute("executecommand",
                    id, "postconf -e 'smtpd_tls_loglevel = 1'");

            try{Thread.sleep(1000); }catch (Exception e){}

            execute("executecommand",
                    id, "postconf -e 'smtpd_use_tls = yes'");
            execute("executecommand",
                    id, "postconf -e 'smtp_tls_note_starttls_offer = yes'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_tls_session_cache_timeout = 3600s'");
            execute("executecommand",
                    id, "postconf -e 'smtp_tls_security_level=may'");
            execute("executecommand",
                    id, "postconf -e 'smtpd_relay_restrictions = permit_mynetworks permit_sasl_authenticated defer_unauth_destination'");


            try{Thread.sleep(1000); }catch (Exception e){}

            execute("executecommand",
                    id, "postconf -e 'home_mailbox= Maildir/'");

            execute("executecommand",
                    id, "postconf -e 'virtual_alias_maps= hash:/etc/postfix/virtual'");

            execute("executecommand",
                    id, "postconf -e 'virtual_alias_domains = '");

            //update line in /etc/postfix/postconf.ch
            execute("executecommand",
                    id, "postconf -e 'mydestination = "+additional+", mail."+additional+", localhost'");

            execute("executecommand",
                    id, "postconf -e 'smtpd_tls_session_cache_database=btree:${data_directory}/smtpd_scache'");

            execute("executecommand",
                    id, "postconf -e 'smtp_tls_session_cache_database=btree:${data_directory}/smtp_scache'");


            try{Thread.sleep(1000); }catch (Exception e){}
            //update/generate /etc/postfix/virtual.db
            execute("commandline",
                    id, "postmap /etc/postfix/virtual");

            execute("commandline",
                    id, "postconf -X 'mailbox_command'");

            try{Thread.sleep(500); }catch (Exception e){}


            // add rules to firewal
            execute("firewall_add_rule", id, "Postfix");
            execute("firewall_add_rule", id, "25/tcp");
            execute("firewall_add_rule", id, "587/tcp");
            execute("firewall_add_rule", id, "143/tcp");
            try{Thread.sleep(500); }catch (Exception e){}
            execute("firewall_add_rule", id, "993/tcp");
            execute("firewall_add_rule", id, "110/tcp");
            execute("firewall_add_rule", id, "995/tcp");
            execute("firewall_add_rule", id, "2525/tcp");
            try{Thread.sleep(500); }catch (Exception e){}
            execute("firewall_add_rule", id, "Dovecot POP3");
            execute("firewall_add_rule", id, "Dovecot IMAP");
            execute("firewall_add_rule", id, "Dovecot Secure IMAP");
            execute("firewall_add_rule", id, "Dovecot Secure POP3");

            execute("firewall_add_rule", id, "Postfix SMTPS");
            execute("firewall_add_rule", id, "Postfix Submission");
            execute("firewall_add_rule", id, "465/tcp");

            try{Thread.sleep(500); }catch (Exception e){}
            execute("commandline",
                     id, "printf \"\\nmail_location = maildir:~/Maildir\\nmail_privileged_group = mail\" >> /etc/dovecot/dovecot.conf");


            Map<String, InputStream> files = new HashMap<>();

            // file : /etc/dovecot/conf.d/10-master.conf
            String content = execute( "get_file_content",
                    id, "/etc/dovecot/conf.d/10-master.conf");
            if(!content.isEmpty()) {
                String[] listSplit = content.split("\\r?\\n");
                StringJoiner newData10Master = new StringJoiner("\n");

                boolean start = false;
                for (String st : listSplit) {

                    if (st.contains("unix_listener /var/spool/postfix/private/aut") && !st.contains("#"))
                        start = true;

                    if (start) {
                        newData10Master.add("#" + st);
                        if (st.contains("}"))
                            start = false;
                    } else {
                        newData10Master.add(st);
                    }


                    if (st.contains("service auth {") || st.contains("service auth{") || st.contains("service auth  {")) {
                        newData10Master.add("  unix_listener /var/spool/postfix/private/auth {");
                        newData10Master.add("    mode = 0666");
                        newData10Master.add("    user = postfix");
                        newData10Master.add("    group = postfix");
                        newData10Master.add("  }");
                    }

                }

                files.put("10-master.conf", new ByteArrayInputStream(newData10Master.toString().getBytes()));
            }


            // file: /etc/dovecot/conf.d/10-auth.conf
            String content2 = execute( "get_file_content", id, "/etc/dovecot/conf.d/10-auth.conf");


            if(!content2.isEmpty()){
                StringJoiner newData10Auth = new StringJoiner("\n");
                String[] listSplit2 = content2.split("\\r?\\n");
                // we put this on first line
                newData10Auth.add("auth_mechanisms = plain login");
                // and the rest auth_mechanisms we just comments
                for(String st : listSplit2)
                    newData10Auth.add((st.contains("auth_mechanisms") && !st.contains("#") ? "#":"")+st);


                files.put("10-auth.conf", new ByteArrayInputStream(newData10Auth.toString().getBytes()));
            }

            if(files.size()>0){
              try{Thread.sleep(1000);}catch (Exception e){}
              sftpUpload( id, files, "/etc/dovecot/conf.d/", "root", "644");
            }


            execute("commandline", id,"systemctl restart dovecot" );

            try{Thread.sleep(500); }catch (Exception e){}
            // reload service postfix
            execute("commandline",
                    id, "service postfix reload");

            // reload service postfix
            execute("commandline",
                    id, "systemctl restart postfix");
        }*/

        //if install BIND9
        if(name.contains("bind9")){
             ssh.execute("firewall_add_rule", id, "53/tcp");
             ssh.execute("firewall_add_rule", id, "Bind9");

            try{Thread.sleep(1000);}catch (Exception e){}

            String content =  ssh.execute( "get_file_content", id, "/etc/bind/named.conf.options");

            if(!content.isEmpty()){

                StringJoiner newData = new StringJoiner("\n");
                String[] listSplit = content.split("\\r?\\n");

                for(String st : listSplit){
                    if(!st.contains("dnssec-validation") && !st.contains("auth-nxdomain") &&
                            !st.contains("listen-on-v6")){
                        newData.add(st.replaceAll("\"","\\\\\"")
                                .replace("'","\\\""));

                        if(st.contains("directory")){
                            newData.add("\tdnssec-validation auto;");
                            newData.add("\tauth-nxdomain no;");
                            newData.add("\t//listen-on-v6 { any; };");
                        }
                    }
                }

                try{Thread.sleep(1000);}catch (Exception e){}
                 ssh.execute( "put_content_in_file_simple", id, newData.toString(),
                              "/etc/bind/named.conf.options" );
            }
        } // end BIND9

        // if install mysql
        if(name.compareTo("Mysql") == 0){
            try{Thread.sleep(1000); }catch (Exception e){}

            result = result +  ssh.execute("secure_database", id);
             ssh.execute("put_content_in_file_simple", id, "[mysqld]\n skip-log-bin", "/etc/mysql/conf.d/disable_binary_log.cnf");
        } // END mysql


        //if install fail2ban
        if(name.compareTo("fail2ban") == 0){
            String checkApp =  ssh.execute("check_app_is_install", id, "postfix" );

            String contentF2B =
                    "[sshd]\n" +
                    "enabled   = true\n" +
                    "maxretry  = 5\n" +
                    "findtime  = 1d\n" +
                    "bantime   = 4w\n" +
                    "filter   = sshd\n" +
                    "logpath  = /var/log/auth.log\n" +
                    "ignoreip  = 127.0.0.1/8\n\n"+

                    "[ssh-iptables]\n" +
                    "enabled  = true\n" +
                    "filter   = sshd\n" +
                    "logpath  = /var/log/auth.log\n" +
                    "maxretry = 5\n\n"+

                    "[postfix]\n" +
                    "enabled = "+(checkApp.contains("(none)")? "false":"true")+"\n" +
                    "mode    = more\n" +
                    "port    = smtp,465,submission\n" +
                    "logpath = %(postfix_log)s\n" +
                    "backend = %(postfix_backend)s\n" +
                    "maxretry = 5\n" +
                    "findtime = 60\n" +
                    "bantime = 86400\n\n"+

                    "[postfix-sasl]\n" +
                    "enabled   = "+(checkApp.contains("(none)")? "false":"true")+"\n" +
                    "filter   = postfix[mode=auth]\n" +
                    "port     = smtp,465,submission,imap,imaps,pop3,pop3s\n" +
                    "maxretry = 5\n" +
                    "findtime = 60\n" +
                    "bantime  = 86400\n" +
                    "logpath  = %(postfix_log)s\n" +
                    "backend  = %(postfix_backend)s\n\n"+

                    "[postfix-sasl2]\n" +
                    "enabled = "+(checkApp.contains("(none)")? "false":"true")+"\n" +
                    "port = smtp\n" +
                    "filter = postfix-sasl2\n" +
                    "logpath = /var/log/mail.log\n" +
                    "maxretry = 5\n\n"+

                    "[dovecot]\n" +
                    "enabled = "+(checkApp.contains("(none)")? "false":"true")+"\n" +
                    "port    = pop3,pop3s,imap,imaps,submission,465,sieve\n" +
                    "logpath = %(dovecot_log)s\n" +
                    "backend = %(dovecot_backend)s\n" +
                    "findtime = 60\n" +
                    "bantime = 86400\n"+
                    "maxretry = 5\n\n"+

                    "[roundcube-auth]\n" +
                    "enabled  = false\n" +
                    "filter   = roundcube-auth\n" +
                    "port     = http,https\n" +
                    "logpath  = /var/log/mail.log\n" +
                    "maxretry = 5\n";

             ssh.execute( "put_content_in_file_simple", id, contentF2B, "/etc/fail2ban/jail.local");


            Map<String, InputStream> files = new HashMap<>();
            String contentDovecot=
                    "# Fail2Ban filter Dovecot authentication and pop3/imap server\n" +
                            "#\n" +
                            "\n" +
                            "[INCLUDES]\n" +
                            "\n" +
                            "before = common.conf\n" +
                            "\n" +
                            "[Definition]\n" +
                            "\n" +
                            "_auth_worker = (?:dovecot: )?auth(?:-worker)?\n" +
                            "_daemon = (?:dovecot(?:-auth)?|auth)\n" +
                            "\n" +
                            "prefregex = ^%(__prefix_line)s(?:%(_auth_worker)s(?:\\([^\\)]+\\))?: )?(?:%(__pam_auth)s(?:\\(dovecot:auth\\))?: |(?:pop3|imap)-login: )?(?:Info: )?<F-CONTENT>.+</F-CONTENT>$\n" +
                            "\n" +
                            "failregex = ^authentication failure; logname=<F-ALT_USER1>\\S*</F-ALT_USER1> uid=\\S* euid=\\S* tty=dovecot ruser=<F-USER>\\S*</F-USER> rhost=<HOST>(?:\\s+user=<F-ALT_USER>\\S*</F-ALT_USER>)?\\s*$\n" +
                            "            ^(?:Aborted login|Disconnected)(?::(?: [^ \\(]+)+)? \\((?:auth failed, \\d+ attempts(?: in \\d+ secs)?|tried to use (?:disabled|disallowed) \\S+ auth|proxy dest auth failed)\\):(?: user=<<F-USER>[^>]*</F-USER>>,)?(?: method=\\S+,)? rip=<HOST>(?:[^>]*(?:, session=<\\S+>)?)\\s*$\n" +
                            "            ^pam\\(\\S+,<HOST>(?:,\\S*)?\\): pam_authenticate\\(\\) failed: (?:User not known to the underlying authentication module: \\d+ Time\\(s\\)|Authentication failure \\(password mismatch\\?\\)|Permission denied)\\s*$\n" +
                            "            ^[a-z\\-]{3,15}\\(\\S*,<HOST>(?:,\\S*)?\\): (?:unknown user|invalid credentials|Password mismatch)\\s*$\n" +
                            "            <mdre-<mode>>\n" +
                            "\n" +
                            "mdre-aggressive = ^(?:Aborted login|Disconnected)(?::(?: [^ \\(]+)+)? \\((?:no auth attempts|disconnected before auth was ready,|client didn't finish \\S+ auth,)(?: (?:in|waited) \\d+ secs)?\\):(?: user=<[^>]*>,)?(?: method=\\S+,)? rip=<HOST>(?:[^>]*(?:, session=<\\S+>)?)\\s*$\n" +
                            "\n" +
                            "mdre-normal = \n" +
                            "\n" +
                            "# Parameter `mode` - `normal` or `aggressive`.\n" +
                            "# Aggressive mode can be used to match log-entries like:\n" +
                            "#   'no auth attempts', 'disconnected before auth was ready', 'client didn't finish SASL auth'.\n" +
                            "# Note it may produce lots of false positives on misconfigured MTAs.\n" +
                            "# Ex.:\n" +
                            "# filter = dovecot[mode=aggressive]\n" +
                            "mode = normal\n" +
                            "\n" +
                            "ignoreregex = \n" +
                            "\n" +
                            "journalmatch = _SYSTEMD_UNIT=dovecot.service\n" +
                            "\n" +
                            "datepattern = {^LN-BEG}TAI64N\n" +
                            "              {^LN-BEG}\n\n";

            files.put("dovecot.conf", new ByteArrayInputStream(contentDovecot.getBytes()));

            if(files.size()>0){
              try{Thread.sleep(1000);}catch (Exception e){}
                ssh.sftpUpload(id, files, "/etc/fail2ban/filter.d/", "root","644");
            }
            // reload service postfix
             ssh.execute("commandline", id, "systemctl restart fail2ban");

         } // end fail2ban



        return result;
    }


}
