package simplessh.com.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import simplessh.com.response.FileData;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileManagerServicesTest {
    @Mock
    private SshCommand sshCommand;

    @InjectMocks
    private FileManagerServices fileManagerServices;

    @Test
    void getList() {
        when(sshCommand.execute("show_folder_content_ls_short_and_full", "safe", "/var/www")).
                thenReturn("""
                        total 136kB
                        drwxrwsr-x 2 root staff 5kB 15/04/2020-14:09:51 local
                        drwxr-xr-x 53 root root 5kB 06/04/2024-18:23:42 lib
                        drwxr-xr-x 3 root root 5kB 05/04/2024-18:54:42 docker
                        lrwxrwxrwx 1 root root 1kB 21/12/2020-20:32:03 lock -> /run/lock
                        drwxrwxr-x 12 root syslog 5kB 18/04/2024-00:00:04 log
                        drwxrwsrwt 3 vmail vmail 5kB 18/04/2024-12:05:51 mail
                        """);

         //FileManagerServices fileManagerServices = new FileManagerServices(mock(SaveContentInFileService.class, SshCommand.class));
        List<FileData> list = fileManagerServices.getList("safe","/var/www");
          assertTrue(list.size()==6);
     }
}