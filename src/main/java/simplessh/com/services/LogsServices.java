package simplessh.com.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Corneli F.
 *
 * Logs controller
 */
@Service
public class LogsServices {
    private static final Logger logger = LogManager.getLogger(LogsServices.class);

    /**
     * get logs
     * @return
     */

    public String getStatus() {

        File userDir = new File(System.getProperty("user.dir"));
        File logFile= new File(userDir , "logs/simplessh.log");

        //System.out.println(logFile.getAbsolutePath());

        Path filePath = Path.of(logFile.getAbsolutePath());
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream
                     = Files.lines(Paths.get(String.valueOf(filePath)), StandardCharsets.UTF_8))
        {

            //Read the content with Stream
            stream.collect(reverseStream()).forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();

    }

    /**
     * remove all logs
     * @return
     */
    public String emptyLogs() {

        File userDir = new File(System.getProperty("user.dir"));

        File logFile= new File(userDir , "logs/simplessh.log");

        //System.out.println(logFile.getAbsolutePath());

        try{
            new FileWriter( logFile, false).close();
        }catch (Exception e){}

        return  "ok" ;

    }

    // reverse  lines from bottoms to top
    public static <T> Collector<T, ?, Stream<T> > reverseStream()
    {
        return Collectors
                .collectingAndThen(Collectors.toList(),
                        list -> {
                            Collections.reverse(list);
                            return list.stream();
                        });
    }
}
