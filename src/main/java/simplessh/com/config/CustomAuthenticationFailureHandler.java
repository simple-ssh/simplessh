package simplessh.com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Corneli F.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException exception)
                        throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put(
                "timestamp",
                Calendar.getInstance().getTime());
        data.put(
                "exception",
                exception.getMessage());

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }
}