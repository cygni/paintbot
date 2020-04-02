package se.cygni.paintbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.standard.WebSocketContainerFactoryBean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import se.cygni.paintbot.websocket.event.EventSocketHandler;
import se.cygni.paintbot.websocket.tournament.TournamentWebSocketHandler;
import se.cygni.paintbot.websocket.training.TrainingWebSocketHandler;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eventWebSocketHandler(), "/events").setAllowedOrigins("*").withSockJS();
        registry.addHandler(eventWebSocketHandler(), "/events-native").setAllowedOrigins("*");
        registry.addHandler(paintbotTrainingWebSocketHandler(), "/training");
        registry.addHandler(paintbotTournamentWebSocketHandler(), "/tournament");
    }

    @Bean
    public WebSocketHandler eventWebSocketHandler() {
        return new PerConnectionWebSocketHandler(EventSocketHandler.class, true);
    }

    @Bean
    public WebSocketHandler paintbotTrainingWebSocketHandler() {
        return new PerConnectionWebSocketHandler(TrainingWebSocketHandler.class, true);
    }

    @Bean
    public WebSocketHandler paintbotTournamentWebSocketHandler() {
        return new PerConnectionWebSocketHandler(TournamentWebSocketHandler.class, true);
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(512000);
        container.setMaxBinaryMessageBufferSize(512000);
        return container;
    }

    @Bean
    public WebSocketContainerFactoryBean createWebSocketContainer() {
        WebSocketContainerFactoryBean container = new WebSocketContainerFactoryBean();
        container.setMaxTextMessageBufferSize(512000);
        container.setMaxBinaryMessageBufferSize(512000);
        return container;
    }
}
