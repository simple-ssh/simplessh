package simplessh.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import simplessh.com.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
/**
 * @author Corneli F.
 *
 * In this file will be the authorisation by JWT token
 */
@Component
public class JWTAuthorizationFilter extends GenericFilterBean {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     *  authorisation by JWT token
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter (ServletRequest request,
                          ServletResponse response,
                          FilterChain chain) throws IOException, ServletException {

        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //Authentication isAuth = SecurityContextHolder.getContext().getAuthentication();
        //if (!(isAuth instanceof AnonymousAuthenticationToken)) {
        //    String currentUserName = authentication.getName();

       // }


        String jwt = parseJwt(httpRequest);

       try {
         if (jwt != null && jwtUtils.validateJwtToken(jwt) && !jwt.contains("null") ) {
             //System.out.println("start auth");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            System.out.println("jvt is:problem");
        }

        chain.doFilter(request, response);
     }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        String url =request.getRequestURL().toString();
        String reqAuth = request.getParameter("tok");
        if ( reqAuth!=null && StringUtils.hasText(reqAuth) &&
                (url.contains("/download-file") || url.contains("/export-database"))) {
            return reqAuth;
        }


        return null;
    }


}