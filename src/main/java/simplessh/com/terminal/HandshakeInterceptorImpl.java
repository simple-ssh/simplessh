package simplessh.com.terminal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HandshakeInterceptorImpl  implements HandshakeInterceptor {
 @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        /* if (request instanceof ServletServerHttpRequest) {
             ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
             HttpSession session = servletRequest.getServletRequest().getSession();
             attributes.put("sessionId", session.getId());
          }*/

         //log.info("Headers Befor:"+request.getHeaders());
         //request.getHeaders().set("Upgrade", "websocket");
         List<String> connection = request.getHeaders().get("Connection");
         if(connection!=null && !connection.contains("Keep-Alive"))
         request.getHeaders().add("Connection", "Keep-Alive");
         //log.info("Headers After:"+request.getHeaders());

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
