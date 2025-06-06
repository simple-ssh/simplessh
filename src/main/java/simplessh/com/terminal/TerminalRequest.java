package simplessh.com.terminal;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TerminalRequest {
   private String command;
   private String token;
   private String sessionId;

   public TerminalRequest(String command){
      this.command = command;
   }
}