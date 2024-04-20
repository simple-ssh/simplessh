package simplessh.com.terminal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import simplessh.com.config.JwtUtils;

@Controller
@Slf4j
public class TerminalWebsocketController {
    @Autowired
    private TerminalWebsocketService terminalWebsocketService;

    @Autowired
    private JwtUtils jwtUtils;

    @MessageMapping("/terminal")
    @SendTo("/receive-data/terminal")
    public void receiver(TerminalRequest request, StompHeaderAccessor headerAccessor) throws Exception {
        String jwt = parseJwt(headerAccessor);
        log.info("Command:" + request.getCommand());
        String singleToken = headerAccessor.getFirstNativeHeader("singleToken");

        if(jwtUtils.validateJwtToken(jwt)) {
            terminalWebsocketService.submitCommand(singleToken, request.getCommand());
        }else{
            if(singleToken != null){
            terminalWebsocketService.submitCommand(singleToken, "Token Is Down, refresh the page!");
            terminalWebsocketService.close(singleToken);
            } 
        }
    }


    private String parseJwt(StompHeaderAccessor accessor) {
        String jwtToken = accessor.getFirstNativeHeader("Authorization");
        if (jwtToken !=null &&  jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7);
        }
        return null;
    }
}
