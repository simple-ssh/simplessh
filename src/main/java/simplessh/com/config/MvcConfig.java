package simplessh.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.ServletContext;

/**
 * config file
 */
@Configuration
@ComponentScan("simplessh.com.config")

public class MvcConfig implements WebMvcConfigurer{
    private final ApplicationContext applicationContext;
    @Autowired
    private ServletContext servletContext;

    //@Value("${static.path}")
    //private String staticPath;

    @Value("${static.crossOrigin}")
    private String CrossOrigin;

    @Value("${static.crossOrigin1}")
    private String CrossOrigin1;

    @Value("${static.crossOrigin2}")
    private String CrossOrigin2;
    @Value("${static.crossOrigin3}")
    private String CrossOrigin3;


    public MvcConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(CrossOrigin, CrossOrigin1, CrossOrigin2, CrossOrigin3)
                .allowedMethods("GET","POST","PUT", "DELETE");
                //.allowedHeaders("header1","headers", "header2", "header3")
                //.exposedHeaders("header1","headers", "header2")
               // .allowCredentials(false).maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       if(!registry.hasMappingForPattern("/templates/build/**")) {
         registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/build/");
        }
    }

/*
    @Bean
    public ViewResolver getViewResolver() {

        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(false);
        //  resolver.setPrefix("");
        resolver.setSuffix(".html");
        resolver.setPrefix("");
        resolver.setContentType("text/html;charset=utf-8");
        return resolver;

    }
 */


}
