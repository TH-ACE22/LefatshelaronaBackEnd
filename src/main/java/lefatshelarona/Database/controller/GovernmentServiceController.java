package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.GovernmentService;
import lefatshelarona.Database.service.GovernmentServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/government-services")
public class GovernmentServiceController {
    private final GovernmentServiceService service;

    public GovernmentServiceController(GovernmentServiceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GovernmentService>> getAllServices() {
        return ResponseEntity.ok(service.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GovernmentService> getServiceById(@PathVariable String id) {
        return service.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GovernmentService> createService(@RequestBody GovernmentService governmentService) {
        return ResponseEntity.ok(service.createService(governmentService));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GovernmentService> updateService(@PathVariable String id, @RequestBody GovernmentService governmentService) {
        return ResponseEntity.ok(service.updateService(id, governmentService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        service.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
