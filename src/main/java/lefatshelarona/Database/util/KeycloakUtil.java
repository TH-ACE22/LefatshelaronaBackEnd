package lefatshelarona.Database.util;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public class KeycloakUtil {

    public static boolean userExists(UsersResource usersResource, String email) {
        List<UserRepresentation> users = usersResource.search(email);
        return !users.isEmpty();
    }
}
