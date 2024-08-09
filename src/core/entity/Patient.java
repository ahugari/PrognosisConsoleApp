package core.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import core.entity.Role;

public class Patient extends User{
    

    public Patient( String email) {
        super(Role.PATIENT, email);
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

    private String diagnosisDate;
    public String getDiagnosisDate() {
        return diagnosisDate;
    }
    public void setDiagnosisDate(String diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    private boolean isOnART;
    public boolean isOnART() {
        return isOnART;
    }
    public void setOnART(boolean isOnART) {
        this.isOnART = isOnART;
    }

    private String artStartDate;
    public String getArtStartDate() {
        return artStartDate;
    }
    public void setArtStartDate(String artStartDate) {
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
    }
}