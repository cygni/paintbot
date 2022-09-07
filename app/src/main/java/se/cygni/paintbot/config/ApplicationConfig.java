package se.cygni.paintbot.config;

//import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    // @Bean
    // public GameManager gameManager() {
    // return new GameManager(globalEventBus());
    // }

    @Bean
    public Object globalEventBus() throws Exception {
        throw new Exception("ay");
//        return new EventBus("globalEventBus");
    }

}
