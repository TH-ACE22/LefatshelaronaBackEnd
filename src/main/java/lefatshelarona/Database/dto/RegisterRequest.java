package lefatshelarona.Database.dto;


import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String fullName;
    private String phone;
    private String username;
    private String password;
    private String role;
    private  String location;
    // Optional: Admin/User


}
