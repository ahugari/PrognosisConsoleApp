package core.entity;

import java.util.UUID;

import core.shared.Helpers;

public class UserManager {
    public UserManager() {
        super();
    }

    public User createUser(String email, String role){
        if(email == null || email.length()<1){
            Helpers.printError("Invalid email: " + email);
        }
        //check for invalid input such as '12' since we have two roles only
        if(role == null || role.length()!=1){
            Helpers.printError(("Invalid role: "+ role));
        }
        try {
            String userEmail = email.trim();

            String uuid = UUID.randomUUID().toString();
            int roleAsIndex = Integer.parseInt(role.trim())-1;
            Role userRole = Role.values()[roleAsIndex];
            User user;
            Helpers.printInfo("User role as index: " + roleAsIndex);
            Helpers.printInfo("User role: " + userRole);
            switch (userRole) {
                case ADMIN:
                    user = new Admin(email);
                    user.uuid = uuid;
                    return user;
                case PATIENT:
                    user = new Patient(userEmail);
                    user.uuid = uuid;
                    return user;
                default:
                    return null;
            }



        } catch (NumberFormatException e) {
            Helpers.printError("Invalid Role selected: " + e.getLocalizedMessage());
        }


        return null;
    }
}
