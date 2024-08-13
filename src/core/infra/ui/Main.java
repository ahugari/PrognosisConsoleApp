package core.infra.ui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import core.entity.Admin;
import core.entity.Patient;
import core.entity.Role;
import core.entity.User;
import core.entity.UserManager;
import core.infra.middleware.ProcessManager;
import core.shared.Helpers;

public class Main {
    private static boolean isLoggedIn = false;
    private static Role currentUserRole;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args){
        boolean showStartMenu = true;
        while (showStartMenu) {
        showStartMenu= false;
        Helpers.printHeader("Welcome to the Life Prognosis Application");
        Helpers.printMessage("Let's get started. What would you like to do?");
        Helpers.printOption(1, "Login");
        Helpers.printOption(2, "Complete Profile Registration");
        Helpers.print1OptionFooter("0) to exit");

            try{
                String in = input.next();
                int userInput = Integer.parseInt(in);
                switch (userInput) {
                    case 1:
                        //Application login
                        String loginResult = loginUser();
                        if(loginResult!=null){
                            try{
                               UUID userId =UUID.fromString(loginResult);
                               Integer interactionResult = showUIAfterLogin(userId);
                               if(interactionResult==0){
                                showStartMenu=true;
                               }else if(interactionResult==9){
                                //exit application
                                return;
                               }
                               if(isLoggedIn){
                                logoutUser();
                               }
                            }catch (IllegalArgumentException ex){
                                 Helpers.printInfo("Login failed. Returning to Start menu.");
                                 break;   
                            }
                        }else{
                            showStartMenu=true;
                            break;
                        }
                        break;

                    case 2:
                        //profile registration
                        Integer registrationResult = completeProfileRegistration();
                        if(registrationResult == -1) {
                            //exit application
                            Helpers.printInfo("Goodbye :)!");
                            return;
                        }else if(registrationResult == 00){
                            //display start menu
                            break;
                        }
                        //if anything failed or everything went well we display the menu again
                        break;

                    case 0:
                        //application exit
                        Helpers.printInfo("Goodbye :)!");
                        return;

                    default:
                        Helpers.printError("Unknown option. Exiting application...");
                        break;
                }
            } catch (NumberFormatException e) {
                Helpers.printError("Wrong input value. Application exiting...");
                showStartMenu=true;
            } catch (Exception ex){
                Helpers.printError("An error occurred while processing your input: " + ex.getLocalizedMessage());
            }
        }
    }

    private static void logoutUser() {
        isLoggedIn= false;
        currentUserRole=null;
    }

    private static Integer showUIAfterLogin(UUID userId) {
        if(currentUserRole == Role.PATIENT){
            //todo
            Integer interationResult = showPatientUI(userId.toString());
            //if back option is selected, show menu again
            while(interationResult==99)
            {
                interationResult = showPatientUI(userId.toString());
            }
            //all other options are sent to method caller
            return interationResult;
        }else if (currentUserRole == Role.ADMIN){
            Integer interactionResult = showAdminUI(userId.toString());
            //if back option is selected, show menu again
            while(interactionResult==99){
                interactionResult = showAdminUI(userId.toString());
            }
            //all other options are sent to method caller
            return interactionResult;
        }
            return 9;
    }

    private static String loginUser(){
        Helpers.printHeader("Logging into the Application");

        String loginEmail = setInputfromScanner(input, "email");
        while(loginEmail.trim().equals("0")||loginEmail.isBlank()){
            loginEmail = setInputfromScanner(input, "email");
        }
        String userPassword = setInputfromScanner(input, "password");
        while(userPassword.equals("0") || userPassword.isBlank()){
            userPassword = setInputfromScanner(input, "password");
        }

        Helpers.printInfo("Logging you in...");

        String loginUserId = ProcessManager.login(loginEmail, userPassword);

        if(loginUserId == null) return null;

        isLoggedIn=true;
        String userProfileAsString = ProcessManager.getUserProfileAsString(loginUserId);
        Role userRole = getRoleFromUserStringProfile(userProfileAsString);
        isLoggedIn = true;
        currentUserRole = userRole; 
        Helpers.printMessage("User logged in successfully.");

        return loginUserId;
        
    }

    private static Role getRoleFromUserStringProfile(String userProfileAsString) {
        String[] userArray = userProfileAsString.split(",");
        Map<String, String> userAttributes = new HashMap<>();
        for (String attribute : userArray) {
            var attr = attribute.trim().split(":");
            userAttributes.put(attr[0].trim(), attr[1].trim());
        }
        return Role.valueOf(userAttributes.get("role"));
    }

    private static int completeProfileRegistration(){
        Helpers.printHeader("Complete Profile Registration");

        boolean validationResult =false;
        String uuid = "";
        String userEmail = "";
        while(!validationResult){
            userEmail= setInputfromScanner(input,"email");
            if(userEmail.equals("0")){
                //exit app
                return -1;
            }else if(userEmail.equals("00")){
                //display start menu
                return 00;
            }
            uuid= setInputfromScanner(input, "uuid");
            if(uuid.equals("0")){
                //exit app
                return -1;
            }else if(userEmail.equals("00")){
                //display start menu
                return 00;
            }
            if(!uuid.trim().isEmpty() && !userEmail.trim().isEmpty()){
                validationResult =true;
            }
        }
        
        if (currentUserRole == Role.PATIENT) {
            completePatientRegistration(uuid, userEmail);
        } else if (currentUserRole == Role.ADMIN) {
            completeAdminRegistration(uuid, userEmail);
        }
        return 0;
    }

    private static void completePatientRegistration(String uuid, String userEmail){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        String dateOfBirth = "";
        int findResult = ProcessManager.findUserByRole(uuid, currentUserRole);

        if( findResult == 1){
            //since script's normal execution result is 0, we check for 1 as the result
        }else if(findResult == 0){
            //if we found the user then the registration was completed
            Helpers.printInfo("Registration completion not available for provided user details.");
            return;
        }else{
            Helpers.printInfo("Something went wrong with while verifying user profile");
            return;
        }

        Helpers.printUserFieldPrompt("firstname");
        String firstname = input.next();

        Helpers.printUserFieldPrompt("lastname");
        String lastname = input.next();

        Helpers.printUserFieldPrompt("password");
        String password = input.next();

        Helpers.printUserFieldPrompt("date of birth (YYYY-MM-DD)");
        try{
            dateOfBirth = dateFormat.format(dateFormat.parse(input.next()));
        } catch (ParseException ex) {
            Helpers.printError("Invalid date format entered: " + ex.getLocalizedMessage());
        } 
        Helpers.printMessage("Are you HIV positive?");
        Helpers.printOption(1, "Yes");
        Helpers.printOption(2, "N0");
        Helpers.printUserOptionPrompt();

        Integer isHIVPositive = Integer.parseInt(input.next());

        String artStartDate = "";
        String diagnosisDate = "";
        Integer isOnART = 0;

        if (isHIVPositive == 1) {
            Helpers.printUserFieldPrompt("diagnosis date (YYYY-MM-DD)");

            try {
                diagnosisDate = dateFormat.format(dateFormat.parse(input.next()));
    
                Helpers.printMessage("Are you on ART treatment?");
                Helpers.printOption(1, "Yes");
                Helpers.printOption(2, "No");
                Helpers.printUserOptionPrompt();
    
                isOnART = Integer.parseInt(input.next());
    
                if (isOnART == 1) {
                    Helpers.printUserFieldPrompt("ART Start date (YYYY-MM-DD)");
                    artStartDate = dateFormat.format(dateFormat.parse(input.next()));
                }
            } catch (ParseException ex) {
                Helpers.printError("Invalid date format entered: " + ex.getLocalizedMessage());
            } catch (NumberFormatException ex){
                Helpers.printError("Invalid number entered: " + ex.getLocalizedMessage());
            }
        } 

        Helpers.printUserFieldPrompt("Country ISO (e.g., US, UK)");

        String countryISO = input.next();

        Patient patient = new Patient(userEmail);
        patient.setArtStartDate(artStartDate);
        patient.setCountryISO(countryISO);
        patient.setDateOfBirth(dateOfBirth);
        patient.setDiagnosisDate(diagnosisDate);
        patient.setEmail(userEmail);
        patient.setLastName(lastname);
        patient.setOnART(isOnART==1);
        patient.setFirstName(firstname);
        patient.setPassword(password);
        patient.setUuid(uuid);
        patient.setUserId(uuid);
        patient.setHIVPositive(isHIVPositive ==1);

        Helpers.printInfo("Completing Patient Profile...");

        ProcessManager.registerPatient(patient); 
    }

    private static void completeAdminRegistration(String uuid, String userEmail){
        String findResult = ProcessManager.getUserProfileAsString(uuid);

        if(findResult==null){
                Helpers.printInfo("Could not verify user.");
                return;
            }

            String adminFirstname = setInputfromScanner(input, "firstname");
            String adminLastname = setInputfromScanner(input, "lastname");
            String adminPassword = setInputfromScanner(input, "password");

            Admin admin = new Admin(userEmail);

            admin.setFirstName(adminFirstname);
            admin.setLastName(adminLastname);
            admin.setPassword(adminPassword);
            admin.setUserId(uuid);
            admin.setUuid(uuid);

            ProcessManager.registerAdmin(admin); 
    }

    private static String setInputfromScanner(Scanner input, String fieldName) {
        String inputFromScanner = "";
        while (inputFromScanner.isBlank()) {
            Helpers.printUserFieldPrompt(fieldName);
            inputFromScanner= input.next();
        }
        return inputFromScanner;
    }

    private static Integer showAdminUI(String loginUserId){
        Helpers.printMessage("User Role:" + currentUserRole);

        if(currentUserRole != Role.ADMIN) {
            return 9;
        }
        Helpers.printHeader("Choose an option:");

        Helpers.printOption(1, "Register New User");
        Helpers.printOption(2, "Download User Reports");
        
        Helpers.print2OptionFooter("0) to logout","9) to exit");

        int adminInput = Integer.parseInt(input.next());

        switch (adminInput) {
            case 1:
                return initiateNewUser();
            case 2:
                return createUserReports();
            case 0:
                //application exit
                Helpers.printInfo("Logging you out...");
                return 00;
            case 9:
                //application exit
                Helpers.printInfo("Goodbye :)!");
                return 9;
            default:
            Helpers.printError("Unknown option. Exiting application...");
            break;
        }
        return 9;
    }
    
    private static Integer showPatientUI(String loginUserId){
    String rawUser = ProcessManager.getUserProfileAsString(loginUserId);
    // Helpers.printInfo(rawUser);
    Patient patient = convertRawUserIntoPatient(rawUser);
        Helpers.printHeader("Choose an option:");
        Helpers.printOption(1, "View Profile");
        Helpers.printOption(2, "Edit Profile");
        Helpers.print2OptionFooter("0) to logout","9) to exit");


        int patientInput = Integer.parseInt(input.next());

        switch (patientInput) {
            case 1:
                Double lifeSpan = patient.calculateSurvivalRate();
                Helpers.printHeader("Patient");
                ProcessManager.getPatientProfileIncludingLifeSpan(patient.getUuid(), lifeSpan);
                Helpers.print3OptionFooter("99) to go back","0) to logout","9) to exit");
                int viewPatientInput = Integer.parseInt(input.next());
                    return viewPatientInput;

            case 2:
                return showEditPatientMenu(patient.getUuid());

            case 0:
                //application exit
                Helpers.printInfo("Goodbye :)!");
                break ;

            default:
                return patientInput;
        }
            return 99;
    }

    private static Patient convertRawUserIntoPatient(String rawUser){
        String[] userArray = rawUser.split(",");
        Map<String, String> userAttributes = new HashMap<>();
        for (String attribute : userArray) {
            var attr = attribute.trim().split(":");
            userAttributes.put(attr[0].trim(), attr[1].trim());
        }

        String role = userAttributes.get("role");
        Helpers.printInfo(role);

            try{
                Patient patient = new Patient(userAttributes.get("email"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                patient.setArtStartDate(sdf.format(sdf.parse(userAttributes.get("ARTStartDate"))));
                patient.setCountryISO(userAttributes.get("countryISO"));
                patient.setEmail(userAttributes.get("email"));
                patient.setDateOfBirth(sdf.format(sdf.parse(userAttributes.get("dob"))));
                patient.setDiagnosisDate(sdf.format(sdf.parse(userAttributes.get("diagnosisDate"))));
                patient.setLastName(userAttributes.get("lastName"));
                patient.setOnART("true".equals(userAttributes.get("isOnART")));
                patient.setFirstName(userAttributes.get("firstName"));
                patient.setPassword(userAttributes.get("password"));
                patient.setUuid(userAttributes.get("uuid"));
                patient.setHIVPositive("true".equals(userAttributes.get("isHIVPositive")));
                return patient;
            }catch(Exception ex){
                Helpers.printError(ex.getLocalizedMessage());
            }

        return null;
    }

    private static User convertRawUserIntoAdmin(String rawUser){
        String[] userArray = rawUser.split(",");
        Helpers.printInfo(userArray[0]);
        Map<String, String> userAttributes = new HashMap<>();
        for (String attribute : userArray) {
            var attr = attribute.trim().split(":");
            Helpers.printInfo(attribute);
            Helpers.printInfo(attr[0]);
            Helpers.printInfo(attr[1]);
            userAttributes.put(attr[0].trim(), attr[1].trim());
        }

        String role = userAttributes.get("role");
        Helpers.printInfo(role);
        if(role.equals(Role.ADMIN.toString())){
            Admin admin = new Admin(userAttributes.get("email"));
            admin.setFirstName(userAttributes.get("firstName"));
            admin.setLastName(userAttributes.get("lastName"));
            admin.setUuid(userAttributes.get("uuid"));
            return admin;
        }else{
            try{
                Patient patient = new Patient(userAttributes.get("email"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                patient.setArtStartDate(sdf.format(sdf.parse(userAttributes.get("ARTStartDate"))));
                patient.setCountryISO(userAttributes.get("countryISO"));
                patient.setEmail(userAttributes.get("email"));
                patient.setDateOfBirth(sdf.format(sdf.parse(userAttributes.get("dob"))));
                patient.setDiagnosisDate(sdf.format(sdf.parse(userAttributes.get("diagnosisDate"))));
                patient.setLastName(userAttributes.get("email"));
                patient.setOnART("true".equals(userAttributes.get("isOnART")));
                patient.setFirstName(userAttributes.get("firstName"));
                patient.setPassword(userAttributes.get("lastName"));
                patient.setUuid(userAttributes.get("uuid"));
                patient.setHIVPositive("true".equals(userAttributes.get("isHIVPositive")));
                return patient;
            }catch(Exception ex){
                Helpers.printError(ex.getLocalizedMessage());
            }
        }

        return null;
    }

    private static Integer showEditPatientMenu(String uuid) {
         String rawUser = ProcessManager.getUserProfileAsString(uuid);
        Patient patient = convertRawUserIntoPatient(rawUser);
        // UUID,email,role,isProfileComplete,firstName,lastName,hashed_password,userId,dateOfBirth,isHIVPositive,diagnosisDate,isOnART,ARTStartDate,countryISO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Helpers.printMessage("Choose a field to edit:");
        Helpers.printOption(1, "First Name: "+ patient.getFirstName());
        Helpers.printOption(2, "Last Name: "+ patient.getLastName());
        Helpers.printOption(3, "Date of Birth: " + patient.getDateOfBirth());
        Helpers.printOption(4, "Is HIV Positive?: " + patient.getHIVPositive());
        Helpers.printOption(5, "Diagnosis Date: " + patient.getDiagnosisDate());
        Helpers.printOption(6, "Is on ART?: " + patient.isOnART());
        Helpers.printOption(7, "ART Start Date: " + patient.getArtStartDate());
        Helpers.printOption(8, "Country: "+ patient.getCountryISO());
        Helpers.print3OptionFooter("99) to go back","0) to logout","9) to exit");
                
        int userInput = Integer.parseInt((input.next()));
        
        switch (userInput) {
            case 1:
                Helpers.printOption(1, "Capture new first name");
                ProcessManager.editUser(patient.getUuid(), patient.getFirstName(), input.next());
                break;
        
                case 2:
                    Helpers.printUserFieldPrompt("first name");
                    ProcessManager.editUser(patient.getUuid(), patient.getFirstName(), input.next());
                    break;

                case 0:
                //application exit
                Helpers.printInfo("Goodbye :)!");
                return userInput;
            default:
                return userInput;
        }
        return 99;

    }

    private static Integer createUserReports(){
        try {
            Helpers.printHeader("User Reports");
            
            ProcessManager.generateAllUserData();
            
            Helpers.print3OptionFooter("99) to go back","0) to logout","9) to exit");
            
            return Integer.parseInt(input.next());

            //Old deliverable to download two empty csv files.
            // File userDataReport = createUserDataReport();
            // if(userDataReport!=null){                
            //     Helpers.printInfo("User report created. Please check resources folder.");            
            // }
            // File userAnalyticsReport = createUserAnalyticsReport();
            // if(userAnalyticsReport!=null){
            //     Helpers.printInfo("User Analytics created. Please check resources folder.");            
            // }
        } catch (Exception ex) {
            Helpers.printError("Failed to create user reports: " + ex.getLocalizedMessage());     
            return 99;       
        }
    }
    
    private static Integer initiateNewUser(){
            User newUser;
            Helpers.printHeader("Register New User");
            String email = setInputfromScanner(input, "email");

            Helpers.printMessage("Choose a role for this account:");
            Helpers.printOption(1, "Patient");
            Helpers.printOption(2, "Admin");
            Helpers.printUserOptionPrompt();

            String newUserRole= input.next();

            UserManager userManager = new UserManager();
            newUser = userManager.createUser(email, newUserRole);

            if(newUser == null)
                return 00;
                
            Helpers.printMessage("User created successfully.");
            Helpers.printMessage("Please take note of your UUID below and use it to login and complete registration:");
            Helpers.printMessage(newUser.getUuid());
            Helpers.printInfo("Registering user...");

            ProcessManager.initiateUserRegistration(newUser);
            
            return 00;
    }

    private static File createUserDataReport(){
        String filePath = "core/infra/resources/userData.csv";
        File userData = new File(filePath);
        try{
            if(userData.createNewFile()){
                Helpers.printInfo("User data report created. Download this report from this path:" + filePath);
                return userData;
            }else{
                Helpers.printInfo("Failed to create user report.");
            }
        }catch(IOException ex){
            Helpers.printError("Failed to create user report." + ex.getLocalizedMessage());
        }
        return null;
    }
    
    private static File createUserAnalyticsReport(){
        String filePath = "core/infra/resources/userAnalytics.csv";
        File userData = new File(filePath);
        try{
            if(userData.createNewFile()){
                Helpers.printInfo("User data report created. Download this report from this path:" + filePath);
                return userData;
            }else{
                Helpers.printInfo("Failed to create user report.");
            }
        }catch(IOException ex){
            Helpers.printError("Failed to create user report." + ex.getLocalizedMessage());
        }
        return null;
    }
    
}
