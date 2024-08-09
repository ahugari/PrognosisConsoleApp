package core.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Calendar;
import core.entity.Role;
import core.infra.middleware.ProcessManager;
import core.shared.Helpers;


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
    public Double calculateSurvivalRate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Helpers.printError(this.getDateOfBirth());
            Date myDate = new Date();
        Calendar myCalendar =Calendar.getInstance();
        myCalendar.setTime(myDate);
        Integer current_year=Calendar.YEAR;
        myCalendar.setTime(sdf.parse(this.dateOfBirth));
        Integer birth_year=Calendar.YEAR;
        Integer age= current_year - birth_year;
        myCalendar.setTime(sdf.parse(this.diagnosisDate));
        Integer diagnosis_year=Calendar.YEAR;
        myCalendar.setTime(sdf.parse(this.artStartDate));
        Integer artstart_year=Calendar.YEAR;
        Integer diff =artstart_year - diagnosis_year;
        //get lifespan from bash script
        Double lifespan=0.0;
        Helpers.printError("HELP: CUURENT YEAR" + current_year);
        Helpers.printError("HELP: DOB" + birth_year);
        Helpers.printError("HELP: age" + age);
        Helpers.printError("HELP: diagnosis_year" + diagnosis_year);
        Map<String, String> map = ProcessManager.getLifeExpectancyStats();
        if(map.containsKey(this.countryISO)){
            lifespan=Double.parseDouble(map.get(this.countryISO));
            Helpers.printInfo(lifespan.toString());
        }
        Helpers.printInfo("HELP:" +age);
        Double life_expectancy=lifespan-age;
        if (isHIVPositive==true){
            if (isOnART==true){
                // artstartyear-diagnosisyear determines the number of times you'll apply the 0.9 survival rate chances 
                // apply log logic maybe
                
                life_expectancy=(Double)((lifespan-age)*Math.pow(0.9, diff+1));
                return life_expectancy;
            }
            else{
              life_expectancy=(diagnosis_year+5) - Double.parseDouble(current_year.toString());
              return life_expectancy;
            }
        }
        
        return life_expectancy;

        } catch (Exception e) {
            Helpers.printError("Error while calculating survival rate:" + e.getLocalizedMessage());
        }

        return -1.0;
    }
    public int modifyProfile(User userUpdate){
        return 0;
    }
}