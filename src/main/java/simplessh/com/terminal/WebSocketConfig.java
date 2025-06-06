package simplessh.com.terminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{ //WebSocketConfigurer,

    @Autowired
   private WebsocketAuthInterceptor websocketAuthInterceptor;
   @Autowired
   private HandshakeInterceptorImpl handshakeInterceptor;

   // inspired from : https://stackoverflow.com/questions/70418738/how-to-build-a-push-notifications-service-with-spring-and-websocket
    /*
    this one use when go with this one: WebSocketConfigurer
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/api/v2/terminal-console").setAllowedOrigins("*");
    }*/

    @Override
   public void configureMessageBroker(MessageBrokerRegistry registry) {
       registry.setApplicationDestinationPrefixes("/send-data");
       registry.enableSimpleBroker("/receive-data");
   }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/ws1").setAllowedOrigins("*").addInterceptors(handshakeInterceptor);
        registry.addEndpoint("/api/ws1").setAllowedOrigins("*").addInterceptors(handshakeInterceptor).withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(websocketAuthInterceptor);
    }




}
