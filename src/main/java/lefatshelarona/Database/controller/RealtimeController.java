// RealtimeController.java
package lefatshelarona.Database.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lefatshelarona.Database.model.Post;

@Controller
public class RealtimeController {
    private final SimpMessagingTemplate messagingTemplate;
    public RealtimeController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void broadcastPostUpdate(Post post) {
        messagingTemplate.convertAndSend(
                "/topic/posts/" + post.getChannelId(), post);
    }
}
