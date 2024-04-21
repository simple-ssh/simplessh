package simplessh.com.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailsServicesTest {

    @Mock
    private SshCommand sshCommand ;

    @InjectMocks
    private EmailsServices emailsServices;

    @Test
    void getDbDataEmpty()  {
         when(sshCommand.execute("get_file_content", "sDSD", "/etc/postfix/mysql-virtual-email2email.cf")).
                thenReturn("");

        Map<String,String> list = emailsServices.getDbData("sDSD");
        assertTrue(list.isEmpty());
    }

    @Test
    void getDbDataWithData() {
        when(sshCommand.execute("get_file_content",  "sDSD", "/etc/postfix/mysql-virtual-email2email.cf")).
                 thenReturn("dbname = mailserver\ndbuser = mailserveruser\ndbpassword=pass\n");

        Map<String,String> list = emailsServices.getDbData("sDSD");
        assertTrue(list.size()==3);
    }

    @Test
    void simpleTestEmpty() {
         when(sshCommand.execute("get_file_content",  "sDSD", "/etc/postfix/mysql-virtual-email2email.cf")).
                thenReturn("");

        String str= emailsServices.simpleTest("sDSD");
        assertTrue(str==null);
    }

}