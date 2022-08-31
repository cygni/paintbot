package se.cygni.paintbot.socketserver.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.standard.WebSocketContainerFactoryBean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import se.cygni.paintbot.socketserver.wshandler.EventSocketHandler;
import se.cygni.paintbot.socketserver.wshandler.TournamentWebSocketHandler;
import se.cygni.paintbot.socketserver.wshandler.TrainingWebSocketHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final EventSocketHandler eventSocketHandler;
    private final TrainingWebSocketHandler trainingWebSocketHandler;
    private final TournamentWebSocketHandler tournamentWebSocketHandler;
    private final static String API_PREFIX = "/ws/api/v1";


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eventSocketHandler, API_PREFIX + "/events").setAllowedOrigins("http://localhsot:5173");
        registry.addHandler(eventSocketHandler, API_PREFIX + "/events-native").setAllowedOrigins("http://localhost:5173");
        registry.addHandler(trainingWebSocketHandler, API_PREFIX + "/training").setAllowedOrigins("http://localhost:5173");
        registry.addHandler(tournamentWebSocketHandler, API_PREFIX + "/tournament").setAllowedOrigins("http://localhost:5173");
//        registry.addHandler(new SocketTextHandler(), "/user").withSockJS();
    }


//    @Bean
//    public WebSocketHandler eventWebSocketHandler() {
//        return new PerConnectionWebSocketHandler(EventSocketHandler.class, true);
//    }

//    @Bean
//    public WebSocketHandler paintbotTrainingWebSocketHandler() {
//        return new PerConnectionWebSocketHandler(TrainingWebSocketHandler.class, true);
//    }

//    @Bean
//    public WebSocketHandler paintbotTournamentWebSocketHandler() {
//        return new PerConnectionWebSocketHandler(TournamentWebSocketHandler.class, true);
//    }

//    @Bean
//    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxTextMessageBufferSize(512000);
//        container.setMaxBinaryMessageBufferSize(512000);
//        return container;
//    }
//
    @Bean
    public WebSocketContainerFactoryBean createWebSocketContainer() {
        WebSocketContainerFactoryBean container = new WebSocketContainerFactoryBean();
        container.setMaxTextMessageBufferSize(512000);
        container.setMaxBinaryMessageBufferSize(512000);
        return container;
    }
}
