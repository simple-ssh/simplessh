package simplessh.com.dao;

import lombok.Getter;

/**
 * @author Corneli F.
 *
 * SSH accounts services
 */
public class SshAccount {
    private String sshHost;
    private String platform;
    private String sshLog;
    private Integer sshPort;
    private String sshPass;
    private String sshPem;
    private String mysqlLog;
    private String mysqlPass;
    private String id;
    private String fast;

    public String getByName(String name){
        switch (name)
        {    case "id":
               return id;
             case "sshHost":
                return sshHost;
             case "platform":
                return platform;
             case "sshLog":
                return sshLog;
            case "sshPort":
                return String.valueOf(sshPort);
            case "sshPass":
                return sshPass.replace("(","\\(").replace(")","\\)").replace("$","\\$");
            case "sshPem":
                return sshPem;
            case "mysqlLog":
                return mysqlLog;
            case "mysqlPass":
                return mysqlPass;
            case "fast":
                return fast;
             default:
                return "";

        }
    }

    public String getId() {
        return id== null? "":id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public String getSshHost() {
        return sshHost == null? "": sshHost;
    }

    public void setSshHost(String sshHost) {
        this.sshHost = sshHost;
    }

    public String getPlatform() {
        return platform == null? "": platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSshLog() {
        return sshLog  == null? "": sshLog;
    }

    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }
    public Integer getSshPort() {
        return sshPort == null ? 22 : sshPort;
    }

    public void setSshLog(String sshLog) {
        this.sshLog = sshLog;
    }
    public String getSshPass() {
        return sshPass == null? "":  sshPass;
    }

    public void setSshPass(String sshPass) {
        this.sshPass = sshPass;
    }
    public void setSshPassStar() {
        if(this.sshPass !=null && !this.sshPass.isEmpty()) this.sshPass = "****";
    }
    public String getSshPem() {
        return sshPem == null? "":  sshPem;
    }

    public void setSshPem(String sshPem) {
        this.sshPem = sshPem;
    }
    public void setSshPemStar() {
        if(this.sshPem !=null && !this.sshPem.isEmpty()) this.sshPem = "****";
    }

   public String getMysqlLog() {
        return mysqlLog  == null? "": mysqlLog;
    }

    public void setMysqlLog(String mysqlLog) {
        this.mysqlLog = mysqlLog;
    }

    public String getMysqlPass() {
        return mysqlPass  == null? "": mysqlPass;
    }

    public void setMysqlPassStar() {
        if(this.mysqlPass !=null && !this.mysqlPass.isEmpty()) this.mysqlPass = "****";
    }

    public void setMysqlPass(String mysqlPass) {
        this.mysqlPass = mysqlPass;
    }

    public String getFast() {
        return fast  == null ? "not": fast;
    }

    public void setFast(String fast) {
        this.fast = fast;
    }


}
