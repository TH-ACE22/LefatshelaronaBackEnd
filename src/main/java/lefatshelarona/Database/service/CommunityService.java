package lefatshelarona.Database.service;

import lefatshelarona.Database.dto.CommunitySummaryDTO;
import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.model.Community;
import lefatshelarona.Database.repository.ChannelRepository;
import lefatshelarona.Database.repository.CommunityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final ChannelRepository channelRepository;

    // ✅ Proper constructor injection — don't manually instantiate repositories!
    public CommunityService(CommunityRepository communityRepository, ChannelRepository channelRepository) {
        this.communityRepository = communityRepository;
        this.channelRepository = channelRepository;
    }

    /**
     * Retrieve all communities.
     */
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    /**
     * Retrieve a single community by its ID.
     */
    public Optional<Community> getCommunityById(String id) {
        return communityRepository.findById(id);
    }
    public List<CommunitySummaryDTO> getCommunitySummaries() {
        List<Community> communities = communityRepository.findAll();
        return communities.stream()
                .map(c -> new CommunitySummaryDTO(
                        c.getId(),
                        c.getName(),
                        c.getLocation(),
                        c.getDescription(),
                        c.getMembers().size()
                ))
                .toList();
    }

    /**
     * ✅ Return full Channel objects for a given community.
     */
    public List<Channel> getChannelsForCommunity(String communityId) {
        return communityRepository.findById(communityId)
                .map(community -> channelRepository.findAllByCommunityId(communityId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Community not found"));
    }


    public Community createCommunity(Community community) {
        return communityRepository.save(community);
    }

    public Optional<Community> updateCommunity(String id, Community updated) {
        return communityRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setLocation(updated.getLocation());
                    existing.setDescription(updated.getDescription());
                    return communityRepository.save(existing);
                });
    }

    public void deleteCommunity(String id) {
        communityRepository.deleteById(id);
    }

    public Community joinCommunity(String communityId, String userId) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    community.addMember(userId);
                    return communityRepository.save(community);
                })
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    public Community leaveCommunity(String communityId, String userId) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    community.removeMember(userId);
                    return communityRepository.save(community);
                })
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    public List<String> getMembers(String communityId) {
        return communityRepository.findById(communityId)
                .map(community -> new ArrayList<>(community.getMembers()))
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    public Community addChannelToCommunity(String communityId, String channelId) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    community.addChannelId(channelId);
                    return communityRepository.save(community);
                })
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    public Community removeChannelFromCommunity(String communityId, String channelId) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    community.removeChannelId(channelId);
                    return communityRepository.save(community);
                })
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    public List<String> getChannelIds(String communityId) {
        return communityRepository.findById(communityId)
                .map(community -> new ArrayList<>(community.getChannelIds()))
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    public List<Community> getCommunitiesForUser(String userId) {
        return communityRepository.findByMembersContaining(userId);
    }
}
