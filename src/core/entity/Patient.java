package core.entity;

import java.util.Date;

public class Patient extends User{
    public Patient(Role role, String email) {
        super(role, email);
    }
    
    private String dateOfBirth;
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    private boolean isHIVPositive;
    public void setHIVPositive(boolean isHIVPositive) {
        this.isHIVPositive = isHIVPositive;
    }

    public boolean getHIVPositive() {
        return isHIVPositive;
    }

    private Date diagnosisDate;
    public Date getDiagnosisDate() {
        return diagnosisDate;
    }
    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    private boolean isOnART;
    public boolean isOnART() {
        return isOnART;
    }
    public void setOnART(boolean isOnART) {
        this.isOnART = isOnART;
    }

    private Date artStartDate;
    public Date getArtStartDate() {
        return artStartDate;
    }
    public void setArtStartDate(Date artStartDate) {
        this.artStartDate = artStartDate;
    }

    private String countryISO;
    public String getCountryISO() {
        return countryISO;
    }
    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }


    public boolean isHIVPositive(){
        return false;
    }
    public float calculateSurvivalRate(){
        return 0;
    }
    public int modifyProfile(User userUpdate){
        return 0;
    }p
}