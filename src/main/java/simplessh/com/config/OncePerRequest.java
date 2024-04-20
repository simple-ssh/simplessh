package simplessh.com.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplessh.com.services.SshCommand;

@Component
public class OncePerRequest  extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(OncePerRequest.class);

    private SshCommand ssh;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        //System.out.println("safasfasfas:"+request.getHeader("id"));
       filterChain.doFilter(request, response);
    }


}

