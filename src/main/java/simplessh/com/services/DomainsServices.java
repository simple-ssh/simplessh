package simplessh.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplessh.com.dao.Data;
import simplessh.com.response.ListMapResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Corneli F.
 */
@Service
public class DomainsServices extends SshCommand{


    @Autowired
    private KeyStoreService keyStoreService;

    /**
     * get list of domains
     * @param id
     * @return
     */
    public List<Map<String,String>> getList(String id) {
      return getDataList(id);
    }

    /**
     * suspend domain
     * @param id
     * @param request
     * @return
     */
   public List<Map<String,String>> suspendActivateDomain(String id, HttpServletRequest request) {
        String name = request.getParameter("name");
        String type = request.getParameter("type");

        name =  name +(type.contains("off")? ".suspended" :  ".conf");
        execute("move", id, "/etc/nginx/conf.d/" + name, "/etc/nginx/conf.d/" + name);

        execute("nginx_restart", id );

        try{Thread.sleep(1000);}catch(Exception e){}

        return getDataList(id);
    }

    /***
     * add new domain
     * @param id
     * @param data
     * @return
     */
    public List<Map<String,String>> addNewDomain(String id, Map<String, String> data ) {
        String dName = data.getOrDefault("name","");
        String proxy = data.getOrDefault("proxy","");
        String path = data.getOrDefault("path","/var/www/");
        String pathToDomain = path.substring(path.length() - 1).contains("/") ? path : path+"/";

        String ns1 = data.getOrDefault("ns1","");
        String ns2 = data.getOrDefault("ns2","");
        String ns3 = data.getOrDefault("ns3","");
        String ns4 = data.getOrDefault("ns4","");

        String type = data.getOrDefault("typeDomain","php");

        if(!dName.isEmpty()) {
            String phpVersion = execute("php_version", id);
                   phpVersion = !phpVersion.isEmpty() ? phpVersion : "8.3";

            String javaString ="server { \n" +
                    "             listen 80; \n" +
                    "             listen [::]:80;  \n" +
                    "             server_name "+dName+" www."+dName+";\n" +
                    "             access_log "+pathToDomain+dName+"/public_html/access.log;\n" +
                    "             error_log "+pathToDomain+dName+"/public_html/error.log;\n" +
                    "             location / {\n" +
                    "                 proxy_pass "+proxy+";\n" +
                    "                 proxy_set_header X-Forwarded-For \\$proxy_add_x_forwarded_for;\n" +
                    "                 proxy_set_header X-Forwarded-Proto \\$scheme;\n" +
                    "                 proxy_set_header X-Forwarded-Port \\$server_port;\n" +
                    "             }\n" +
                    "             location ~ \\.log {\n" +
                    "                deny  all;\n" +
                    "              }\n" +
                    "            }";

            String phpString = "server {  \n" +
                    "             listen 80; \n" +
                    "             listen [::]:80;  \n" +
                    "             server_name "+dName+" www."+dName+";\n" +
                    "             root "+pathToDomain+dName+"/public_html;\n" +
                    "             index index.php index.html index.htm;\n" +
                    "\n" +
                    "        location / {\n" +
                    "                index index.php index.html;\n" +
                    "                try_files \\$uri \\$uri/ /index.php?\\$args;\n" +
                    "\n" +
                    "               location ~* ^.+\\\\.(jpeg|jpg|png|gif|bmp|ico|svg|css|js|html|htm)\\$ {\n" +
                    "                    expires max;\n" +
                    "                }\n"+
                    "\n" +
                    "               location ~ \\.php$ {\n" +
                    "                   root "+pathToDomain+dName+"/public_html;\n" +
                    "                   fastcgi_pass unix:/var/run/php/php"+phpVersion.trim().replaceAll("\\n","")+"-fpm.sock;\n" +
                    "                   fastcgi_index  index.php;\n" +
                    "                   fastcgi_split_path_info ^(.+\\\\.php)(/.+)$;\n" +
                    "                   fastcgi_param SCRIPT_FILENAME \\$document_root\\$fastcgi_script_name;\n" +
                    "                   include    fastcgi_params;\n" +
                    "                }\n" +

                    "        }\n" +
                    "\n" +
                    "     error_page   500 502 503 504  /50x.html;\n" +
                    "     location = /50x.html {\n" +
                    "        root "+pathToDomain+dName+"/public_html;\n" +
                    "     }\n" +
                    "\n" +

                    "\n\n" +
                    "         access_log "+pathToDomain+dName+"/public_html/access.log;\n" +
                    "         error_log "+pathToDomain+dName+"/public_html/error.log;\n" +

                    "\n\n" +
                    "      # deny access to .htaccess files, if Apache is document root\n" +
                    "      location ~ \\.ht {\n" +
                    "          deny  all;\n" +
                    "      }" +
                    "\n\n" +
                    "      location ~ \\.log {\n" +
                    "         deny  all;\n" +
                    "      }\n" +
                    "}";

            String ip = keyStoreService.getSshAccountByName(id,"sshHost");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime now = LocalDateTime.now();

            String dns = "\\$TTL 14400\n" +
                    "@    IN    SOA    "+ns1+".    root."+dName+". (\n" +
                    "                                            "+dtf.format(now)+"01\n" +
                    "                                            7200\n" +
                    "                                            3600\n" +
                    "                                            1209600\n" +
                    "                                            180 )\n" +
                    "\n" +
                    "@\t14400\tIN\tNS\t\t"+ns1+".\n" +
                    "@\t14400\tIN\tNS\t\t"+ns2+".\n" +
                    (ns3.isEmpty()?"":"@\t14400\tIN\tNS\t\t"+ns3+".\n")+
                    (ns4.isEmpty()?"":"@\t14400\tIN\tNS\t\t"+ns4+".\n")+
                    "@\t14400\tIN\tA\t\t"+ip+"\n" +
                    "www\t14400\tIN\tA\t\t"+ip+"\n" +
                    "ftp\t14400\tIN\tA\t\t"+ip+"\n" +
                    "mail\t14400\tIN\tA\t\t"+ip+"\n" +
                    "smtp\t14400\tIN\tA\t\t"+ip+"\n" +
                    "pop\t14400\tIN\tA\t\t"+ip+"\n" +
                    "imap\t14400\tIN\tA\t\t"+ip+"\n" +
                    "*\t14400\tIN\tA\t\t"+ip+"\n" +
                    "@\t14400\tIN\tMX\t10\tmail."+dName+".\n" +
                    "@\t14400\tIN\tTXT\t\t\\\"v=spf1 a mx ip4:"+ip+" ~all\\\"\n" +
                    "_dmarc\t14400\tIN\tTXT\t\t\\\"v=DMARC1; p=quarantine; rua=mailto:dmarc@"+dName+"; ruf=mailto:dmarc@"+dName+"\\\"";


            Data[] params =    new Data[5];
            params[0] = new Data("put_content_in_file_simple",
                    (type.contains("php")? phpString : javaString), "/etc/nginx/conf.d/"+dName+".conf");

            String checkDir= execute("check_if_directory_exist", id, pathToDomain+dName);
            if(!checkDir.contains("yes")){
                params[1] = new Data("new_directory","www-data", pathToDomain+dName+"/public_html/");
                params[2] = new Data("assign_www_data_group_to_folder",pathToDomain+dName);
                params[3] = new Data("automatically_given_www-data_for_new",pathToDomain+dName+"/*");
                params[4] = new Data("automatically_set_permision_rwrr_for_new",pathToDomain+dName+"/*");
            }
            executeAll(id, Arrays.stream(params).filter(Objects::nonNull).toArray(Data[]::new));


            try{Thread.sleep(1000);}catch(Exception e){}

            if(!ns1.isEmpty()&&!ns2.isEmpty()) {
                // create a file your-domain.com.db
                execute("put_content_in_file_simple", id, dns, "/etc/bind/"+dName+".db");

                try{Thread.sleep(1000);}catch(Exception e){}

                // Add a line bellow other content in file /etc/bind/named.conf :
                // zone "your-domain.com.db" {type master; file "/etc/bind/your-domain.com.db";};
                addRemoveToNamedConf(id, dName, "add");
            }

            /*
             // create file in /etc/nginx/conf.d/yourdomain.com.conf
            String confFile = type.contains("php")? phpString : javaString;
            Map<String, InputStream> file =   new HashMap<>(){{
                 put(dName+".conf",new ByteArrayInputStream(confFile.getBytes()));
            }};
            sftpUpload(connection, file, "/etc/nginx/conf.d/", "root",  "644");
            */

            execute("put_content_in_file_simple", id,
                          "<h1>Oops!</h1><br/><h2>Something went wrong</h2>", pathToDomain+dName+"/public_html/50x.html");

            try{Thread.sleep(1000);}catch(Exception e){}
            execute("nginx_restart", id );
        }

        try{Thread.sleep(1000);}catch(Exception e){}
        return getDataList(id);
    }

    // add or remove a row in /etc/bind/named.conf first read the content file
    private void addRemoveToNamedConf(String idConnection, String domainName, String typeOperation){

        String content = execute("get_file_content", idConnection, "/etc/bind/named.conf");

        if(!content.isEmpty()){
           StringJoiner newData = new StringJoiner("\n");
           Arrays.stream(content.split("\\r?\\n")).
                    filter(e->!e.contains("/"+domainName+".db")).
                    forEach(e->newData.add(e.replaceAll("\"","\\\\\"")));

            if(typeOperation.contains("add"))
                newData.add("zone \\\""+domainName+"\\\" {type master; file \\\"/etc/bind/"+domainName+".db\\\";};");

            if((typeOperation.contains("add") && !content.contains("/"+domainName+".db")) || typeOperation.contains("remove"))
              execute("put_content_in_file_simple", idConnection, newData.toString(), "/etc/bind/named.conf");

            try{Thread.sleep(1000);}catch (Exception e){}
            execute( "app_restart", idConnection, "bind");
        }

    }


    /**
     *  install Let's encrypt SSL
     * @param id
     * @param data
     * @return
     */
   public ListMapResponse installSSLToDomain(String id, Map<String, String> data ) {
        String name = data.getOrDefault("name","");
        String email = data.getOrDefault("email","");

        //certbot --nginx -d simplecom -d www.simplecom
        String response = execute("installssl", id, email, name, name);
        try{Thread.sleep(1000);}catch(Exception e){}
        return new ListMapResponse(getDataList(id), response);
    }

    /**
     * renew domain
     * @param id
     * @return
     */
   public ListMapResponse renewSSL(String id) {
        String result = execute("renew_ssl", id);
        try{Thread.sleep(1000);}catch(Exception e){}
        return new ListMapResponse(getDataList(id), result);
    }

    /**
     * edit dns
     * @param id
     * @param data
     * @return
     */
    public List<Map<String,String>> changePath(String id, Map<String, String> data ) {
        String name = data.getOrDefault("name","");
        String path = data.getOrDefault("path","");

        execute("ftp_set_directory", id, path, name.trim());
        try{Thread.sleep(1000);}catch(Exception e){}
        return getDataList(id);
    }

    /**
     * remove domain
     * @param id
     * @param request
     * @return
     */
   public List<Map<String,String>> removeDomain(String id, HttpServletRequest request ) {
        String name = request.getParameter("name");

        execute("move", id, "/etc/nginx/conf.d/"+name, "/var/trash/");
        try {  Thread.sleep(2000);  } catch (InterruptedException e) { }
        execute("nginx_restart", id );
        try {  Thread.sleep(2000);  } catch (InterruptedException e) { }

        addRemoveToNamedConf(id,
                name.replace(".conf","").replace(".suspended",""),
                "remove");


        return getDataList(id);
    }


    /**
     * get list of domains
     * @param id
     * @return
     */
    public List<Map<String,String>> getDataList(String id){
        String domainList = execute( "show_folder_content_ls", id, "/etc/nginx/conf.d");
        return Arrays.stream(domainList.split("\\r?\\n")).
                      filter(st->!st.contains("file:") && !st.contains(".key") && !st.isEmpty()).
                      map(st->{
                          String dbName = st.replace("--", "").replace("|", "").
                                             replace(".conf", "").replace(" ","").
                                             replace("`","").replace(".suspended","");
                          return Map.of("name",   dbName, "active", st,"ssl", "no");
                      }).collect(Collectors.toList());
     }

    /**
     * check if ssl is activated
     * @param domain
     * @return
     */
    private String checkSSL(String domain){

        try{
            URL url = new URL("https://"+domain);
            new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            return "yes";
        }catch (Exception e){
            return "no";
        }
    }

    public String setupDNS(String id, Map<String, String> data) {
        String dName = data.getOrDefault("domain","");
        String dns = data.getOrDefault("content","");

        // create a file your-domain.com.db
        execute("put_content_in_file_simple", id, dns, "/etc/bind/"+dName+".db");

        try{Thread.sleep(1000);}catch(Exception e){}
        // Add a line bellow other content in file /etc/bind/named.conf :
        // zone "your-domain.com.db" {type master; file "/etc/bind/your-domain.com.db";};
        addRemoveToNamedConf(id, dName, "add");
        return "ok";
    }
}