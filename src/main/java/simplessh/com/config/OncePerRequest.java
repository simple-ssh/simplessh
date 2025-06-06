package simplessh.com.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OncePerRequest  extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(OncePerRequest.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

            //CustomHttpServletRequest req = new CustomHttpServletRequest(request);
            //req.putHeader("Upgrade", "websocket");
            //req.putHeader("Connection", "Upgrade");
        request.getHeader("Connection") ;
         filterChain.doFilter(request, response);
    }


}

