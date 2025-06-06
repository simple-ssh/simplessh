package simplessh.com.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public InputStream getFileFromResources(String filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filename);
        return resource.getInputStream();
    }

    public String convertToString(String filename) {
       try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getFileFromResources(filename)))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }catch (IOException io){
            return "";
       }
    }
}
