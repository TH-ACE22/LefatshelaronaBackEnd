package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel) {
        return ResponseEntity.ok(channelService.createChannel(channel));
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join")
    public ResponseEntity<Channel> joinChannel(@RequestParam String channelId, @RequestParam String userId) {
        return ResponseEntity.ok(channelService.joinChannel(channelId, userId));
    }

    @PostMapping("/leave")
    public ResponseEntity<Channel> leaveChannel(@RequestParam String channelId, @RequestParam String userId) {
        return ResponseEntity.ok(channelService.leaveChannel(channelId, userId));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannelById(@PathVariable String channelId) {
        Optional<Channel> channel = channelService.getChannelById(channelId);
        return channel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(channelService.getAllChannels());
    }
}
