package simplessh.com.terminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import simplessh.com.config.JwtUtils;
import simplessh.com.services.CustomUserDetailsService;


@Component
public class WebsocketAuthInterceptor  implements ChannelInterceptor {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TerminalWebsocketService terminalWebsocketService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = accessor.getMessageHeaders().get("simpSessionId").toString();
        String singleToken = accessor.getFirstNativeHeader("singleToken");
        String connectionId = accessor.getFirstNativeHeader("connectionId");
        String jwt = parseJwt(accessor);

        if (StompCommand.CONNECT.equals(accessor.getCommand()) && singleToken!=null) {
            if(jwtUtils.validateJwtToken(jwt))
             terminalWebsocketService.init(sessionId, singleToken, connectionId);
            else
             terminalWebsocketService.close(sessionId);
         } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
             terminalWebsocketService.close(sessionId);
        }

        return message;

        /*
        if (jwt != null && !jwt.contains("null") && jwtUtils.validateJwtToken(jwt)  ){
            System.out.println("Good");
           return message;
        }else{
             //throw new MessageDeliveryException(message, "autentification failed");
            return message;
        }
       */
    }


    private String parseJwt(StompHeaderAccessor accessor) {
        String jwtToken = accessor.getFirstNativeHeader("Authorization");
        if (jwtToken !=null &&  jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7);
        }
        return null;
    }
}