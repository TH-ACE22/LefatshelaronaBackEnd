package lefatshelarona.Database.dto;


import lombok.Data;


@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;  // ✅ must be here
    private String lastName;   // ✅ must be here
    private String phone;
}

