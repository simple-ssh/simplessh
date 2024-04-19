package simplessh.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import simplessh.com.services.CustomUserDetailsService;


/**
 * security config file
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${static.crossOrigin}")
    private String CrossOrigin;

    @Autowired
    private  AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private  JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    };

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
          return new CustomAuthenticationFailureHandler();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean(name="myAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler).and()
                 //.addFilter( new JWTAuthorizationFilter(authenticationManager()))
                .addFilterAfter(jwtAuthorizationFilter,  BasicAuthenticationFilter.class) //  new JWTAuthorizationFilter(authenticationManager())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/**",
                        "/content/config/**",
                        "/content/files/**",
                        "/*.xml",
                        "/*.json")
                .hasAnyRole("ADMIN")
                .antMatchers("/",
                        "/**",
                        "/api/v1/homepage",
                        "/api/v1/header",
                        "/api/v1/signin",
                        "/api/v1/login",
                        "/api/v1/signup",
                        "/api/v1/reset-password",
                        "/content/**",
                        "/registration",
                        "/error",
                        "/favicon.ico",
                        "/*.png",
                        "/*.gif",
                        "/*.svg",
                        "/*.jpg",
                        "/*.jpg",
                        "/*.css",
                        "/*.js")
                .permitAll() ;

    }

}
