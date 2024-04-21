package simplessh.com.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DomainsServicesTest {

    @Mock
    private SshCommand sshCommand;

    @InjectMocks
    private DomainsServices domainsServices;

    @Test
    void getDataListEmpty() {
        when(sshCommand.execute("show_folder_content_ls", "sDSD", "/etc/nginx/conf.d")).
               thenReturn("");




        List<Map<String,String>> list = domainsServices.getDataList("sDSD");
        assertTrue(list.isEmpty());
    }

    @Test
    void getDataListWithData() {
        when(sshCommand.execute("show_folder_content_ls", "sDSD", "/etc/nginx/conf.d")).
                thenReturn("domain1.com.conf\ndomain2.com.suspended\ndomain3.com.conf\ndomain4.com.conf\ndomain5.com.conf\n");

        
        List<Map<String,String>> list = domainsServices.getDataList("sDSD");
        assertTrue(list.size()==5);
    }
}