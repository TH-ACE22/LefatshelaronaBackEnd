package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    public Channel createChannel(Channel channel) {
        if (channelRepository.findByChannelName(channel.getChannelName()).isPresent()) {
            throw new RuntimeException("Channel already exists!");
        }
        return channelRepository.save(channel);
    }

    public void deleteChannel(String channelId) {
        if (channelRepository.existsById(channelId)) {
            channelRepository.deleteById(channelId);
        } else {
            throw new RuntimeException("Channel not found!");
        }
    }

    public Channel joinChannel(String channelId, String userId) {
        return channelRepository.findById(channelId)
                .map(channel -> {
                    channel.addUser(userId);
                    return channelRepository.save(channel);
                })
                .orElseThrow(() -> new RuntimeException("Channel not found!"));
    }

    public Channel leaveChannel(String channelId, String userId) {
        return channelRepository.findById(channelId)
                .map(channel -> {
                    channel.removeUser(userId);
                    return channelRepository.save(channel);
                })
                .orElseThrow(() -> new RuntimeException("Channel not found!"));
    }

    public Optional<Channel> getChannelById(String channelId) {
        return channelRepository.findById(channelId);
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }
}
