package simplessh.com;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Corneli F.
 */

/**
 * this file is for ll command
 */
public class Variables {
 
    public static String[] system = {"Ubuntu"};
    public static Map<String,Map<String, String>> map = new HashMap<>(){{

         Map<String, String> dataUbuntu=new LinkedHashMap<>(){{
             //cp -r from to
             put("copy", "cp -r \"%0\" \"%1\"");

             // mv from to
             put("move", "mv \"%0\" \"%1\"");

             //rm -r -f /directory/dir
             put("remove_directory", "rm -r -f %0");

             //rm -r -f /directory/file.file
             put("remove_file", "rm %0");

             //zip -r new-file.zip /dir/file.txt
             put("zip", "zip -r %0 %1");

             //unzip new-file.zip -d /unzip/dir/; chown -R "$USER":www-data /unzip/dir/
             put("unzip", "unzip %0 -d %1; chown -R \"$USER\":www-data %1");

             //tar xzf new-file.zip --directory /unzip/dir/; chown -R "$USER":www-data /unzip/dir/
             put("tarunzip", "tar xzf %0 --directory %1; chown -R \"$USER\":www-data %1");

             //ls /etc/nginx
             put("show_folder_content_ls", "ls -a %0");

             //ls -g --block-size=KB /etc/nginx
             put("show_folder_content_ls_full", "ls -a -g --block-size=KB %0");

             //ls -lh --block-size=KB --time-style="+%d/%m/%Y-%H:%M:%S" /etc/nginx  -> drwxr-xr-x 53 root root 5kB 06/04/2024-18:23:42 lib
             put("show_folder_content_ls_short_and_full", "ls -a -lh --block-size=KB --time-style=\"+%d/%m/%Y-%H:%M:%S\" %0");

             //less /etc/nginx
             put("get_file_content", "less %0");

             //bash -c  "printf %s $'content of file' > path/where/to/save.txt"
             put("put_content_in_file", "bash -c \\\"printf %s $'%0' > %1\\\"");

             //bash -c 'text=blah; printf "content of file" > path/where/to/save.txt'"
             put("put_content_in_file_simple", "bash -c 'text=blah; printf \"%0\" > %1'");

             //put("put_content_in_file_simple", "awk -vORS=, '{ print \"%0\" }' %1 | sed 's/,$/\\n/'");

             //bash -c 'echo > path/where/to/newfile.txt'; chown -R \"$USER\":www-data path/where/to/newfile.txt"
             put("new_empty_file", "bash -c 'echo > \"%1\"'; chown -R \"$USER\":%0 %1");

             //mkdir -p path/where/to/new-folder; chown -R \"$USER\":www-data path/where/to/new-folder"
             put("new_directory", "mkdir -p %1; chown -R \"$USER\":%0 %1");

             //mkdir -p path/where/to/new-folder && bash -c 'echo > path/where/to/newfile.txt' && chmod 644 path/where/to/newfile.txt
             put("new_directory_new_file", "mkdir -p %0 && bash -c 'echo > %1' && chmod 644 %1");

             //mv path/where/to/existing-name path/where/to/new-name
             put("rename", "mv %0 %1");

             //chmod 644 path/to/folder-file
             put("file_permission", "chmod %0 %1");

             //chmod -R 644 path/to/folder/
             put("file_permission_all", "chmod -R %0 %1");

             //chmod -R 644 path/to/folder
             put("only_folder_permision", "chmod -R %0 %1");

             //find /path/to/folder -type d -exec chmod 644 {} \;
             put("all_folders_permission", "find %0 -type d -exec chmod %1 {} \\;");

             //find /path/to/folder -type f -exec chmod 644 {} \
             put("all_files_permission", "find %0 -type f -exec chmod %1 {} \\;");

             //chown -R "$USER":www-data /path/to/folder
             put("assign_www_data_group_to_folder", "chown -R \"$USER\":www-data %0");

             //chmod g+s /path/to/folder
             put("automatically_given_www-data_for_new", "chmod g+s %0");

             //chmod o-rwx /path/to/folder
             put("automatically_set_permision_rwrr_for_new", "chmod o-rwx %0");

             //systemctl restart nginx
             put("nginx_restart", "systemctl restart nginx");

             //systemctl restart postfix
             put("postfix_restart", "systemctl restart postfix");

             //systemctl restart postfix
             put("app_restart", "systemctl restart %0");

             //systemctl restart php7.4-fpm
             put("php_restart", "systemctl restart php7.4-fpm");

             put("php_version", "php -v | sed -e '/^PHP/!d' -e 's/.* \\([0-9]\\+\\.[0-9]\\+\\).*$/\\1/'");

             //
             put("show_folder_content_gio", "gio tree %0");

             //
             put("empty_trash", "rm -r -f /tmp/simplessh_trash/*");

             //
             put("installssl", "certbot --nginx --debug --non-interactive --email %0 --agree-tos --domains %1,www.%2 --redirect --keep-until-expiring");

             //
             put("set_timer_renew_ssl", "certbot --nginx --debug --force-renewal");

             //
             put("renew_ssl", "certbot renew");

             //
             put("move_to_trash", "");

             //
             put("empty_file_content", "truncate -s 0 %0");

             //this is mysql it's the same for all systems
             put("mysql_command", "mysql -u mysqluser -p'%0' -e \"SET NAMES 'utf8';%1;\"");
             put("mysql_rename_table", "mysql -u mysqluser -p'%0' -e \"RENAME TABLE %1 TO %2;\"");
             put("mysql_dbList", "mysql -u mysqluser -p'%0' -e \"show databases;\"");
             put("mysql_dbList_full", "mysql -u mysqluser -p'%0' -e \" SELECT db, user, host FROM mysql.db; show databases;\"");
             put("mysql_db_and_users_list", "mysql -u mysqluser -p'%0' -e \"SELECT db, user, host FROM mysql.db;\"");
             put("mysql_usersList", "mysql -u mysqluser -p'%0' -e \"SELECT User, Host FROM mysql.user;\"");
             put("mysql_new_database_user", "mysql -u mysqluser -p'%0' -e \"CREATE DATABASE %1; CREATE USER '%2'@'%3' IDENTIFIED BY '%4'; GRANT %5 ON %1.* to '%2'@'%3'; FLUSH PRIVILEGES;\"");
             put("mysql_new_database", "mysql -u mysqluser -p'%0' -e \"CREATE DATABASE %1;\"");
             put("mysql_new_user", "mysql -u mysqluser -p'%0' -e \"CREATE USER '%1'@'%2' IDENTIFIED BY '%3';\"");
             put("mysql_show_user_grand_permision", "mysql -u mysqluser -p'%0' -e \"SHOW GRANTS FOR '%1'@'%2';\"");
             put("mysql_user_grand_permision", "mysql -u mysqluser -p'%0' -e \"GRANT %1 ON %2.* to %3;\"");
             put("mysql_user_revoke_permision", "mysql -u mysqluser -p'%0' -e \"REVOKE ALL PRIVILEGES ON %1.* FROM %2;\"");
             put("mysql_show_table_from_db", "mysql -u mysqluser -p'%0' -e \"SHOW TABLES FROM %1;\"");
             put("mysql_show_table_from_db_with_size", "mysql -u mysqluser -p'%0' -e \"SELECT table_name AS  TABLENAME , ROUND(((data_length + index_length) / 1024 / 1024), 2) AS  TABLESIZE  FROM information_schema.TABLES WHERE table_schema = '%1'\""); // ORDER BY (data_length + index_length) DESC
             put("mysql_show_column_from_table_from_db", "mysql -u mysqluser -p'%0' -e \"SHOW COLUMNS FROM %1;\"");
             put("mysql_show_data_first_call", "mysql -u mysqluser -p'%0' -e \"SHOW COLUMNS FROM %1; " +
                                                                               "select '---------split---------' AS '';" +
                                                                               "SELECT COUNT(*) AS COUNT FROM %1;" +
                                                                               "select '---------split---------' AS '';" +
                                                                               "SET NAMES 'utf8'; SELECT * FROM %1 LIMIT %2;\"");

             put("mysql_show_rows_from_table_from_db", "mysql -u mysqluser -p'%0' -e \"SET NAMES 'utf8'; SELECT * FROM %1 LIMIT %2;\"");
             put("mysql_show_data_from_table", "mysql -u mysqluser -p'%0' -e \"SET NAMES 'utf8';SELECT * FROM %1 LIMIT %2;\"");
             put("mysql_gent_counts_of_rows", "mysql -u mysqluser -p'%0' -e \"SELECT COUNT(*) FROM %1;\"");
             put("mysql_remove_db", "mysql -u mysqluser -p'%0' -e \"DROP DATABASE IF EXISTS  %1;\"");
             put("mysql_remove_db_from_sql_table", "mysql -u mysqluser -p'%0' -e \"DELETE FROM mysql.db WHERE db = '%1';\"");
             put("mysql_flush", "mysql -u mysqluser -p'%0' -e \"FLUSH PRIVILEGES;\"");
             put("mysql_empty_table", "mysql -u mysqluser -p'%0' -e \"TRUNCATE TABLE %1; FLUSH TABLE %1; \"");
             put("mysql_delete_table", "mysql -u mysqluser -p'%0' -e \"DROP TABLE IF EXISTS %1;\"");
             put("mysql_delete_field_from_table", "mysql -u mysqluser -p'%0' -e \"ALTER TABLE %1 DROP COLUMN %2; \"");
             put("mysql_remove_rows_from_table", "mysql -u mysqluser -p'%0' -e \"DELETE FROM %1 WHERE %2 IN (%3) ;\"");
             put("mysql_count_rows_from_table", "mysql -u mysqluser -p'%0' -e \"SELECT COUNT(*) AS COUNT FROM %1;\"");
             put("mysql_show_users_list", "mysql -u mysqluser -p'%0' -e \"SELECT User, Host FROM mysql.user;\"");
             put("mysql_old_user_change_password", "mysql -u mysqluser -p'%0' -e \"SET PASSWORD FOR %1 = PASSWORD('%2');\"");
             put("mysql_user_change_password", "mysql -u mysqluser -p'%0' -e \"ALTER USER %1 IDENTIFIED BY '%2';\"");
             put("mysql_delete_user", "mysql -u mysqluser -p'%0' -e \"DROP USER '%1'@'%2'; FLUSH PRIVILEGES;\"");
             put("mysql_export", "mysqldump -u mysqluser -p'%0' %1 > %2");
             put("mysql_import", "mysql -u mysqluser -p'%0' %1 < %2");
             put("mysql_update_field_in_table", "mysql -u mysqluser -p'%0' -e \"UPDATE %1 SET %2 ='%3' where %4='%5'; \"");
             put("mysql_replace_field_in_table", "mysql -u mysqluser -p'%0' -e \"UPDATE %1 SET %2 = REPLACE(%2,':1:1:','\\\"') where %3='%4'; \"");
             // end mysql

             put("mysql_select", "mysql -u mysqluser -p'%0' -e \"SET NAMES 'utf8'; SELECT %1 FROM %2 ;\"");
             put("mysql_select_where", "mysql -u %0 -p'%1' -e \"SELECT %2 FROM %3 WHERE %4;\"");

             //mkdir -p
             put("new_folder_with_permision", "mkdir -p %0  %1 ");

             //getent group | awk -F: '{print $1}'
             put("list_of_groups", "getent group | awk -F: '{print $1}'");

             //getent group %0 | awk -F: '{print $4}'
             put("list_of_users_in_group", "getent group %0 | awk -F: '{print $4}'");

             //groupadd groupName
             put("add_new_group", "groupadd %0");

             //adduser userName www-data
             put("add_user_to_group", "adduser %0 %1");

             //useradd –a –G userName www-data
             put("add_new_user_and_add_to_group", "useradd –a –G %0 %1");

             //gpasswd -d userName www-data
             put("remove_user_from_group", "gpasswd -d %0 %1");

             //chown -R "$USER":www-data /path/to/file
             put("modify_owner_group", "chown -R \"$USER\":%0 %1");

             //chown root:www-data /path/to/file
             put("modify_owner_user_group", "chown -R %0 %1");

             put("update_file_set_owner", "chown -R %0 %1 && sudo chmod %2 %1");

             //chown -R "$USER":/path/to/file www-data
             put("add_path_file_to_group_perm", "chown -R \"$USER\":%0 %1 && chmod %2 %1");

             //stat -c "%G" /path/to/folder
             put("get_folder_group", "stat -c \"%G\" %0"); //stat -c "%U %G" /path/to/file

             //groupdel www-data
             put("remove_group", "groupdel %0");

             //ufw status
             put("firewall_list", "ufw status numbered");

             //ufw allow '553/tcp'
             put("firewall_add_rule", "ufw allow '%0'");

             //ufw delete allow '553/tcp'
             put("firewall_remove_rule", "ufw delete allow '%0'; ufw delete deny '%0';");

             //ufw --force enable
             put("firewall_enable", "ufw --force enable");

             //ufw --force disable
             put("firewall_disable", "ufw --force disable");

             //useradd -m userName
             put("add_ftp_account", "useradd -m %0");

             //echo 'username':'userPassword' | sudo chpasswd
             put("set_password_to_ftp_account", "echo '%0':'%1' | sudo chpasswd");

             //usermod -d /path/to/user/dir userName
             put("ftp_set_directory", "usermod -d %0 %1");

             //cat /etc/passwd | grep -vE '(/bin/false|/sbin/nologin|/bin/sync)' | cut -d: -f1,6
             put("view_ftp_account", "cat /etc/passwd | grep -vE '(/bin/false|/sbin/nologin|/bin/sync)' | cut -d: -f1,6");

             //userdel userName
             put("remove_ftp_account", "userdel '%0'");

             //systemctl list-units --all --type=service --no-pager
             put("show_services", "systemctl list-units --all --type=service --no-pager");

             //systemctl start serviceName
             put("start_services", "systemctl start %0");

             //systemctl enable serviceName
             put("enable_services", "systemctl enable %0");

             //systemctl is-enabled serviceName
             put("is_enabled_service", "systemctl is-enabled %0");

             //systemctl stop serviceName
             put("stop_services", "systemctl stop %0");

             //systemctl disable serviceName
             put("disable_services", "systemctl disable %0");

             //systemctl restart serviceName
             put("restart_services", "systemctl restart %0");

             //systemctl stop serviceName;systemctl disable serviceName;rm /etc/systemd/system/serviceName;rm /usr/lib/systemd/system/serviceName;systemctl daemon-reload;systemctl reset-failed
             put("remove_services", "systemctl stop %0;systemctl disable %0;rm /etc/systemd/system/%0;rm /usr/lib/systemd/system/%0;systemctl daemon-reload;systemctl reset-failed");

             //systemctl status serviceName
             put("status_services", "systemctl status %0");

             //DEBIAN_FRONTEND=noninteractive apt-get -q -y install installName
             put("install", "DEBIAN_FRONTEND=noninteractive apt-get -q -y install %0 ");

             //DEBIAN_FRONTEND=noninteractive some comand name example(ls /etc)
             put("commandline", "DEBIAN_FRONTEND=noninteractive %0 ");

             //some comand name example(ls /etc)
             put("executecommand", "%0");

             //
             put("secure_database", "/usr/bin/mysql -u root -e \"DELETE FROM mysql.user WHERE User=''; DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1'); DROP DATABASE IF EXISTS test; select plugin_name, plugin_status from information_schema.plugins where plugin_name like 'validate%'; install plugin validate_password soname 'validate_password.so'; SET GLOBAL validate_password_policy=MEDIUM; FLUSH PRIVILEGES;\"");

             //apt-cache policy installName
             put("check_app_is_install", "apt-cache policy %0 ");

             //apt-cache policy installName, check if repository is set
             put("check_if_repository_is_set", "apt-cache policy|grep '%0' ");

             //add repository
             put("add_repository", "DEBIAN_FRONTEND=noninteractive apt-add-repository %0 ");

             // DEBIAN_FRONTEND=noninteractive apt-get -q -y remove installName
             put("remove_app", "DEBIAN_FRONTEND=noninteractive apt-get -q -y  remove --purge %0");

             //apt-get update
             put("apt_get_update", "apt-get update");

             //
             put("db_change_pass_for_root", "/usr/bin/mysql -u root -e \"ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '%0';\"");

             //apt -y install software-properties-common; add-apt-repository ppa:ondrej/php -y
             put("change_repo_for_php", "apt -y install software-properties-common; add-apt-repository ppa:ondrej/php -y");

             //usermod -aG sudo userName
             put("make_user_sudoer", "usermod -aG sudo %0");

             //deluser userName sudo
             put("remove_user_sudo", "deluser %0 sudo");

             //du -cha --max-depth=1 /path/to/folder | grep -E "M|G"
             put("get_folder_size", "du -cha --max-depth=1 %0 | grep -E \"M|G\"");

             //du -hs /path/to/folder-file
             put("get_folder_size_only", "du -hs %0");

             //ls -l --b=M  /path/to/file | cut -d " " -f5
             put("get_file_size", "ls -l --b=M  %0 | cut -d \" \" -f5");

             //du -cha --max-depth=1 /path/to/folder
             put("get_all_fil_folder_size", "du -cha --max-depth=1 %0");

             //test -d '/path/to/folder' && echo yes || echo not
             put("check_if_directory_exist", "test -d '%0' && echo yes || echo not");
         }};

        put("Ubuntu", dataUbuntu);
    }};
}
