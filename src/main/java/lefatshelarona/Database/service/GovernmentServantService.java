package lefatshelarona.Database.service;

import lefatshelarona.Database.model.GovernmentServant;
import lefatshelarona.Database.repository.GovernmentServantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GovernmentServantService {
    private final GovernmentServantRepository repository;

    public GovernmentServantService(GovernmentServantRepository repository) {
        this.repository = repository;
    }

    public List<GovernmentServant> getAllGovernmentServants() {
        return repository.findAll();
    }

    public Optional<GovernmentServant> getGovernmentServantById(String id) {
        return repository.findById(id);
    }

    public Optional<GovernmentServant> getGovernmentServantByEmail(String email) {
        return repository.findByEmail(email);
    }

    public GovernmentServant createGovernmentServant(GovernmentServant servant) {
        return repository.save(servant);
    }

    public GovernmentServant updateGovernmentServant(String id, GovernmentServant updatedServant) {
        return repository.findById(id).map(servant -> {
            servant.setName(updatedServant.getName());
            servant.setEmail(updatedServant.getEmail());
            servant.setContactNumber(updatedServant.getContactNumber());
            servant.setGovernmentDepartment(updatedServant.getGovernmentDepartment());
            servant.setRole(updatedServant.getRole());
            servant.setWaterIssues(updatedServant.getWaterIssues());
            servant.setAssignedChannels(updatedServant.getAssignedChannels());
            return repository.save(servant);
        }).orElseThrow(() -> new RuntimeException("Government Servant not found with id: " + id));
    }

    public void deleteGovernmentServant(String id) {
        repository.deleteById(id);
    }
}
