package simplessh.com.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simplessh.com.services.LogsServices;

import jakarta.servlet.http.HttpServletRequest;
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
@RestController
@RequestMapping("/api/v1/")
public class LogsController {

    @Autowired
    private LogsServices services;

    /**
     * get logs
     * @return
     */
    @GetMapping("/get-logs")
    public String getStatus(HttpServletRequest request) {
       Integer limit = Integer.valueOf(request.getParameter("limit"));
      return services.getStatus(limit);
    }

    /**
     * remove all logs
     * @return
     */
    @GetMapping("/empty-logs")
    public String emptyLogs() {

       return services.emptyLogs();
    }

}
