package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Query;
import lefatshelarona.Database.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;

    public Query createQuery(Query query) {
        query.setSubmittedAt(new Date()); // Set the timestamp
        return queryRepository.save(query);
    }

    public List<Query> getAllQueries() {
        return queryRepository.findAll();
    }

    public Optional<Query> getQueryById(String queryId) {
        return queryRepository.findById(queryId);
    }

    public List<Query> getQueriesByUser(String userId) {
        return queryRepository.findBySentBy(userId);
    }

    public List<Query> getQueriesByCommunity(String communityId) {
        return queryRepository.findByCommunityId(communityId);
    }

    public Query updateQueryStatus(String queryId, String newStatus) {
        Optional<Query> queryOpt = queryRepository.findById(queryId);
        if (queryOpt.isPresent()) {
            Query query = queryOpt.get();
            query.setStatus(newStatus);
            return queryRepository.save(query);
        }
        return null; // Query not found
    }

    public void deleteQuery(String queryId) {
        queryRepository.deleteById(queryId);
    }
}
