package core.entity;

import java.io.File;

public class Admin extends User {
    public Admin(Role role, String email) {
        super(role, email);
        //TODO Auto-generated constructor stub
    }

    public int deleteUser(User user){
        return 0;
    }

    public User createUser(String email, String role){
        return null;
    }
    private int setUserRole(String role){
        return 0;
    }
    public File downloadUserData(){
        return null;
    }
}