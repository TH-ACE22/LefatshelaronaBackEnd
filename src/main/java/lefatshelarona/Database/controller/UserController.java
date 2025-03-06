package lefatshelarona.Database.controller;


import lefatshelarona.Database.model.User;
import lefatshelarona.Database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Create a new user
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Get all users
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/getById/{id}")
    public User getUserById(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    // Update user by ID
    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setJoinedChannels(updatedUser.getJoinedChannels());
            user.setLocation(updatedUser.getLocation());
            user.setName(updatedUser.getName());
            user.setPhone(updatedUser.getPhone());
            user.setProfilePicture(updatedUser.getProfilePicture());
            user.setRole(updatedUser.getRole());
            user.setUsername(updatedUser.getUsername());
            return userRepository.save(user);
        }
        return null;
    }

    // Delete user by ID
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully.";
        }
        return "User not found.";
    }
}
