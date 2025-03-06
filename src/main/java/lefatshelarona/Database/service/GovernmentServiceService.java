package lefatshelarona.Database.service;

import lefatshelarona.Database.model.GovernmentService;
import lefatshelarona.Database.repository.GovernmentServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GovernmentServiceService {
    private final GovernmentServiceRepository repository;

    public GovernmentServiceService(GovernmentServiceRepository repository) {
        this.repository = repository;
    }

    public List<GovernmentService> getAllServices() {
        return repository.findAll();
    }

    public Optional<GovernmentService> getServiceById(String id) {
        return repository.findById(id);
    }

    public Optional<GovernmentService> getServiceByName(String name) {
        return repository.findByName(name);
    }

    public GovernmentService createService(GovernmentService service) {
        return repository.save(service);
    }

    public GovernmentService updateService(String id, GovernmentService service) {
        return repository.findById(id).map(existingService -> {
            existingService.setName(service.getName());
            existingService.setChannelId(service.getChannelId());
            existingService.setDescription(service.getDescription());
            existingService.setContactEmail(service.getContactEmail());
            return repository.save(existingService);
        }).orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public void deleteService(String id) {
        repository.deleteById(id);
    }
}
