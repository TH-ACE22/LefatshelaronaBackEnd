package lefatshelarona.Database.service;

import lefatshelarona.Database.model.PollSuggestion;
import lefatshelarona.Database.repository.PollSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PollSuggestionService {

    private final PollSuggestionRepository pollSuggestionRepository;

    public PollSuggestion saveSuggestion(String text, String userId, String communityId) {
        PollSuggestion suggestion = PollSuggestion.builder()
                .suggestionText(text)
                .userId(userId)
                .communityId(communityId)
                .submittedAt(new Date())
                .build();

        return pollSuggestionRepository.save(suggestion);
    }

    public List<PollSuggestion> getAllSuggestions() {
        return pollSuggestionRepository.findAll();
    }

    public List<PollSuggestion> getSuggestionsByCommunity(String communityId) {
        return pollSuggestionRepository.findByCommunityId(communityId);
    }

    public PollSuggestion updateSuggestionRank(String suggestionId, int newRank) {
        PollSuggestion suggestion = pollSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        suggestion.setRank(newRank);
        return pollSuggestionRepository.save(suggestion);
    }
}
