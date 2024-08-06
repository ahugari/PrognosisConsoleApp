package core.entity;

import java.util.Date;
import java.util.Calendar;
import core.entity.Role;


public class Patient extends User{
    public Patient( String email) {
        super(Role.PATIENT, email);
    }
    
    private Date dateOfBirth;
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
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
    public int calculateSurvivalRate(){
        Date myDate = new Date();
        Calendar myCalendar =Calendar.getInstance();
        myCalendar.setTime(myDate);
        int current_year=Calendar.YEAR;
        myCalendar.setTime(this.dateOfBirth);
        int birth_year=Calendar.YEAR;
        int age= current_year - birth_year;
        myCalendar.setTime(this.diagnosisDate);
        int diagnosis_year=Calendar.YEAR;
        myCalendar.setTime(this.artStartDate);
        int artstart_year=Calendar.YEAR;

        //get lifespan from bash script
        int lifespan=0;
        int life_expectancy=lifespan-age;
        if (isHIVPositive==true){
            if (isOnART==true){
               
                // artstartyear-diagnosisyear determines the number of times you'll apply the 0.9 survival rate chances 
                // apply log logic maybe
                life_expectancy=(int)((lifespan-age)*(0.9));
                return life_expectancy;
            }
            else{
              life_expectancy=(diagnosis_year+5) - current_year;
              return life_expectancy;
            }
            
        }
        


        return life_expectancy;
    }
    public int modifyProfile(User userUpdate){
        return 0;
    }
}