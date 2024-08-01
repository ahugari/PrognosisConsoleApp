package core.entity;

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
            int roleAsIndex = Integer.parseInt(role.trim())-1;
            Role userRole = Role.values()[roleAsIndex];
            User user;
            switch (userRole) {
                case ADMIN:
                    user = new Admin(email);
                    return user;
                case PATIENT:
                    user = new Patient(userEmail);
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
