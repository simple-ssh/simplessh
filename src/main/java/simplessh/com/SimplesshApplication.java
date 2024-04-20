package simplessh.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import simplessh.com.config.MvcConfig;
import simplessh.com.services.KeyStoreService;

@SpringBootApplication
public class SimplesshApplication implements ApplicationListener<ContextRefreshedEvent> {
    private static  FirstStart load = new  FirstStart();

	@Autowired
	private KeyStoreService keyStoreService;

    public static void main(String[] args) {
	   load.showLoad();
	   ConfigurableApplicationContext context = SpringApplication.run(SimplesshApplication.class, args);
       load.setSpringContext(context);
   }

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		  load.hideLoad();
		 keyStoreService.setUp();
    }
 }

