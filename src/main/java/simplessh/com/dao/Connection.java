package simplessh.com.dao;

import com.jcraft.jsch.Session;

/**
 * @author Corneli F.
 */
public class Connection {
    private SshAccount sshAccount;
    private Session session;

    public Connection(SshAccount sshAccount, Session session) {
        this.sshAccount = sshAccount;
        this.session = session;
    }

    public Connection() {
    }

    public SshAccount getSshAccount() {
        return sshAccount;
    }

    public void setSshAccount(SshAccount sshAccount) {
        this.sshAccount = sshAccount;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
