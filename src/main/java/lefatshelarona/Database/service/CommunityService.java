package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Community;
import lefatshelarona.Database.repository.CommunityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> getCommunityById(String id) {
        return communityRepository.findById(id);
    }

    public Community createCommunity(Community community) {
        return communityRepository.save(community);
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
}
