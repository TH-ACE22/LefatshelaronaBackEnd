package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.repository.ChannelRepository;
import lefatshelarona.Database.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final CommunityRepository communityRepository;
    public Channel createChannel(Channel channel, String communityId) {
        // Save the new channel
        Channel savedChannel = channelRepository.save(channel);

        // Link the channel to the community
        communityRepository.findById(communityId).ifPresent(community -> {
            community.addChannelId(savedChannel.getId());
            communityRepository.save(community);
        });

        return savedChannel;
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
