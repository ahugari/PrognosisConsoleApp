package core.entity;


import java.io.File;

public abstract class User{
    public User(Role role, String email) {
        this.role = role;
        this.email = email;
    }
    protected String firstName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }

    protected String lastName;
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    protected String userId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    protected String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    protected Role role = Role.Patient;
    public Role getRole() {
        return role;
    }
    
    protected String uuid;
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    protected String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public User viewProfile(){
        return null;
    }

    public File downloadCalendar(){
        return null;
    }
}