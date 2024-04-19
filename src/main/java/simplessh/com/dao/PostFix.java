package simplessh.com.dao;

public class PostFix {


    public String postfixMain(String domain){
        String data = "# See /usr/share/postfix/main.cf.dist for a commented, more complete version\n" +
                "\n" +
                "# Debian specific:  Specifying a file name will cause the first\n" +
                "# line of that file to be used as the name.  The Debian default\n" +
                "# is /etc/mailname.\n" +
                "#myorigin = /etc/mailname\n" +
                "\n" +
                "smtpd_banner = $myhostname ESMTP $mail_name (Ubuntu)\n" +
                "biff = no\n" +
                "\n" +
                "# appending .domain is the MUA's job.\n" +
                "append_dot_mydomain = no\n" +
                "\n" +
                "# Uncomment the next line to generate \"delayed mail\" warnings\n" +
                "#delay_warning_time = 4h\n" +
                "\n" +
                "readme_directory = no\n" +
                "\n" +
                "# See http://www.postfix.org/COMPATIBILITY_README.html -- default to 2 on\n" +
                "# fresh installs.\n" +
                "compatibility_level = 2\n" +
                "\n" +
                "# TLS parameters\n" +
                "smtpd_tls_cert_file=/etc/letsencrypt/live/"+domain+"/fullchain.pem\n" +
                "smtpd_tls_key_file=/etc/letsencrypt/live/"+domain+"/privkey.pem\n" +
                "smtpd_use_tls=yes\n" +
                "smtpd_tls_auth_only = yes\n" +
                "smtp_tls_security_level = may\n" +
                "smtpd_tls_security_level = may\n" +
                "smtpd_sasl_security_options = noanonymous, noplaintext\n" +
                "smtpd_sasl_tls_security_options = noanonymous\n" +
                "\n" +
                "# Authentication\n" +
                "smtpd_sasl_type = dovecot\n" +
                "smtpd_sasl_path = private/auth\n" +
                "smtpd_sasl_auth_enable = yes\n" +
                "strict_mailbox_ownership = no\n" +
                "# See /usr/share/doc/postfix/TLS_README.gz in the postfix-doc package for\n" +
                "# information on enabling SSL in the smtp client.\n" +
                "\n" +
                "# Restrictions\n" +
                "smtpd_helo_restrictions =\n" +
                "        permit_mynetworks,\n" +
                "        permit_sasl_authenticated,\n" +
                "        reject_invalid_helo_hostname,\n" +
                "        reject_non_fqdn_helo_hostname\n" +
                "smtpd_recipient_restrictions =\n" +
                "        permit_mynetworks,\n" +
                "        permit_sasl_authenticated,\n" +
                "        reject_non_fqdn_recipient,\n" +
                "        reject_unknown_recipient_domain,\n" +
                "        reject_unlisted_recipient,\n" +
                "        reject_unauth_destination\n" +
                "smtpd_sender_restrictions =\n" +
                "        permit_mynetworks,\n" +
                "        permit_sasl_authenticated,\n" +
                "        reject_non_fqdn_sender,\n" +
                "        reject_unknown_sender_domain\n" +
                "smtpd_relay_restrictions =\n" +
                "        permit_mynetworks,\n" +
                "        permit_sasl_authenticated,\n" +
                "        defer_unauth_destination\n" +
                "\n" +
                "# See /usr/share/doc/postfix/TLS_README.gz in the postfix-doc package for\n" +
                "# information on enabling SSL in the smtp client.\n" +
                "\n" +
                "myhostname = "+domain+"\n" +
                "alias_maps = hash:/etc/aliases\n" +
                "alias_database = hash:/etc/aliases\n" +
                "mydomain = "+domain+"\n" +
                "myorigin = $mydomain\n" +
                "mydestination = localhost\n" +
                "relayhost =\n" +
                "mynetworks = 127.0.0.0/8 [::ffff:127.0.0.0]/104 [::1]/128\n" +
                "mailbox_size_limit = 0\n" +
                "recipient_delimiter = +\n" +
                "inet_interfaces = all\n" +
                "inet_protocols = ipv4\n" +
                "#inet_protocols = all\n" +
                "\n" +
                "# Handing off local delivery to Dovecot's LMTP, and telling it where to store mail\n" +
                "virtual_transport = lmtp:unix:private/dovecot-lmtp\n" +
                "\n" +
                "# Virtual domains, users, and aliases\n" +
                "virtual_mailbox_domains = mysql:/etc/postfix/mysql-virtual-mailbox-domains.cf\n" +
                "virtual_mailbox_maps = mysql:/etc/postfix/mysql-virtual-mailbox-maps.cf\n" +
                "virtual_alias_maps = mysql:/etc/postfix/mysql-virtual-alias-maps.cf,\n" +
                "        mysql:/etc/postfix/mysql-virtual-email2email.cf\n" +
                "\n" +
                "# Even more Restrictions and MTA params\n" +
                "disable_vrfy_command = yes\n" +
                "strict_rfc821_envelopes = yes\n" +
                "#smtpd_etrn_restrictions = reject\n" +
                "#smtpd_reject_unlisted_sender = yes\n" +
                "#smtpd_reject_unlisted_recipient = yes\n" +
                "smtpd_delay_reject = yes\n" +
                "smtpd_helo_required = yes\n" +
                "smtp_always_send_ehlo = yes\n" +
                "#smtpd_hard_error_limit = 1\n" +
                "smtpd_timeout = 30s\n" +
                "smtp_helo_timeout = 15s\n" +
                "smtp_rcpt_timeout = 15s\n" +
                "smtpd_recipient_limit = 40\n" +
                "minimal_backoff_time = 180s\n" +
                "maximal_backoff_time = 3h\n" +
                "\n" +
                "# Reply Rejection Codes\n" +
                "invalid_hostname_reject_code = 550\n" +
                "non_fqdn_reject_code = 550\n" +
                "unknown_address_reject_code = 550\n" +
                "unknown_client_reject_code = 550\n" +
                "unknown_hostname_reject_code = 550\n" +
                "unverified_recipient_reject_code = 550\n" +
                "unverified_sender_reject_code = 550\n" +
                "header_checks = regexp:/etc/postfix/list_unsub_header\n" +
                "\n" +
                "\n" +
                "# SPF\n" +
                "policy-spf_time_limit = 3600s\n" +
                "\n" +
                "# DKIM\n" +
                "milter_default_action = accept\n" +
                "milter_protocol = 2\n" +
                "smtpd_milters = inet:localhost:8891,local:opendmarc/opendmarc.sock\n" +
                "non_smtpd_milters = inet:localhost:8891";
     return data;
    }
    public String postfixMasterSubmission(){
        String comand = "postconf -M submission/inet='submission inet n       -       y      -       -       smtpd'\n" +
                "postconf -P submission/inet/syslog_name=postfix/submission\n" +
                "postconf -P submission/inet/smtpd_tls_security_level=encrypt\n" +
                "postconf -P submission/inet/smtpd_sasl_auth_enable=yes\n" +
                "postconf -P submission/inet/smtpd_sasl_type=dovecot\n" +
                "postconf -P submission/inet/smtpd_sasl_path=private/auth\n" +
                "postconf -P submission/inet/smtpd_reject_unlisted_recipient=no\n" +
                "postconf -P submission/inet/smtpd_client_restrictions=permit_sasl_authenticated,reject\n" +
                "postconf -P submission/inet/milter_macro_daemon_name=ORIGINATING\n";
         return comand;
    }
    public String postfixMastersmtps(){
        String comand = "postconf -M smtps/inet='smtps     inet  n       -       -       -       -       smtpd'\n" +
                "postconf -P smtps/inet/syslog_name=postfix/smtps\n" +
                "postconf -P smtps/inet/smtpd_tls_wrappermode=yes\n" +
                "postconf -P smtps/inet/smtpd_sasl_auth_enable=yes\n" +
                "postconf -P smtps/inet/smtpd_sasl_type=dovecot\n" +
                "postconf -P smtps/inet/smtpd_sasl_path=private/auth\n" +
                "postconf -P smtps/inet/smtpd_client_restrictions=permit_sasl_authenticated,reject\n" +
                "postconf -P smtps/inet/milter_macro_daemon_name=ORIGINATING\n";
        return comand;
    }
    public String postfixSpf(){
        //sed -i '3i\policy-spf unix - n n - - spawn user=nobody argv=/usr/bin/policyd-spf' /etc/postfix/master.cf
        return "postconf -M policy-spf/unix='policy-spf  unix  -       n       n       -       -       spawn       user=nobody argv=/usr/bin/policyd-spf'\n";
    }

    public String dbData(String user, String pass, String dbName){
        return "user = "+user+"\n" +
                "password = "+pass+"\n" +
                "hosts = 127.0.0.1\n" +
                "dbname = "+dbName+"\n";
    }
    public String mysqlVirtualAliasMaps(String user, String pass, String dbName){
         return dbData(user, pass, dbName)+"query = SELECT destination FROM virtual_aliases WHERE source='%s'";
    }
    public String mysqlVirtualEmail2email(String user, String pass, String dbName){
        return dbData(user, pass, dbName)+"query = SELECT destination FROM virtual_aliases WHERE source='%s'";
    }

    public String mysqlVirtualMailboxDomains(String user, String pass, String dbName){
        return dbData(user, pass, dbName)+"query = SELECT 1 FROM virtual_domains WHERE name='%s'";
    }

    public String mysqlVirtualMailboxMaps (String user, String pass, String dbName){
        return dbData(user, pass, dbName)+"query = SELECT 1 FROM virtual_users WHERE email='%s'";
    }
}
