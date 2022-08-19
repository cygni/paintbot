package se.cygni.paintbot.config;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.cygni.paintbot.game.GameManager;

@Configuration
public class ApplicationConfig {

    // @Bean
    // public GameManager gameManager() {
    // return new GameManager(globalEventBus());
    // }

    @Bean
    public EventBus globalEventBus() {
        return new EventBus("globalEventBus");
    }

}
