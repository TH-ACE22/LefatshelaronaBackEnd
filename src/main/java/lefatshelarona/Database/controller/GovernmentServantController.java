package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.GovernmentServant;
import lefatshelarona.Database.service.GovernmentServantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/government-servants")
public class GovernmentServantController {

    private final GovernmentServantService service;

    public GovernmentServantController(GovernmentServantService service) {
        this.service = service;
    }

    @GetMapping
    public List<GovernmentServant> getAllGovernmentServants() {
        return service.getAllGovernmentServants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GovernmentServant> getGovernmentServantById(@PathVariable String id) {
        return service.getGovernmentServantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GovernmentServant> getGovernmentServantByEmail(@PathVariable String email) {
        return service.getGovernmentServantByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GovernmentServant> createGovernmentServant(@RequestBody GovernmentServant servant) {
        return ResponseEntity.ok(service.createGovernmentServant(servant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GovernmentServant> updateGovernmentServant(@PathVariable String id, @RequestBody GovernmentServant updatedServant) {
        try {
            return ResponseEntity.ok(service.updateGovernmentServant(id, updatedServant));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGovernmentServant(@PathVariable String id) {
        service.deleteGovernmentServant(id);
        return ResponseEntity.noContent().build();
    }
}
