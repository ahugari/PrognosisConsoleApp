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
    private static Role userRole;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args){

        Helpers.printLine();
        Helpers.printMessage("Welcome to the Life Prognosis Application!!");
        Helpers.printLine();

        Helpers.printMessage("Let's get started. What is your role?");
        Helpers.printOption(1, "Patient");
        Helpers.printOption(2, "Admin");
        Helpers.printOption(0, "Exit");
        Helpers.printLine();

        try{
            int roleInput = Integer.parseInt(input.nextLine());
            switch (roleInput) {
                case 1:
                    userRole = Role.PATIENT;
                    break;

                case 2:
                    userRole = Role.ADMIN;
                    break;

                case 0:
                    Helpers.printInfo("Goodbye :)!");
                    return;

                default:
                    Helpers.printError("Unknown role. Exiting application...");
                    return;
            }

            while (true) {
                Helpers.printLine();
                Helpers.printMessage("Choose an option:");

                // if(userRole == Role.ADMIN) {
                    // }
                Helpers.printOption(2, "Login");
                Helpers.printOption(3, "Complete Profile Registration");
                Helpers.printOption(0, "Exit");
                Helpers.printLine();

                int userInput = Integer.parseInt(input.nextLine());

                switch (userInput) {
                    case 1:
                    //registering new user
                        //TODO: might not need this after refactoring
                        break;

                    case 2:
                        //Application login
                        loginUser();
                        break;

                    case 3:
                        //profile registration
                        completeProfileRegistration();
                        break;

                    case 0:
                        //application exit
                        Helpers.printInfo("Goodbye :)!");
                        return;

                    default:
                        Helpers.printError("Unknown option. Exiting application...");
                        return;
                    }
                }
        } catch (NumberFormatException e) {
            Helpers.printError("Wrong input value. Application exiting...");
        } catch (Exception ex){
            Helpers.printError("An error occured while processing your input: " + ex.getLocalizedMessage());
        }

    }

    private static void loginUser(){
        Helpers.printLine();
        Helpers.printMessage("Logging into the Application");
        Helpers.printLine();

        String loginEmail = setInputfromScanner(input, "email");
        String userPassword = setInputfromScanner(input, "password");

        Helpers.printInfo("Logging you in...");

        String loginUserId = ProcessManager.login(loginEmail, userPassword);
        
        if(loginUserId!=null)
        {
            Helpers.printMessage("User logged in successfully.");
            if(userRole == Role.PATIENT){
                showPatientUI(loginUserId);
            }else if (userRole == Role.ADMIN){
                showAdminUI(loginUserId);
            }                     
        }
    }

    private static void completeProfileRegistration(){
        Helpers.printLine();
        Helpers.printMessage("Complete Profile Registration");
        Helpers.printLine();

        boolean validationResult =false;
        String uuid = "";
        String userEmail = "";
        while(!validationResult){
            userEmail= setInputfromScanner(input,"email");
            uuid= setInputfromScanner(input, "uuid");
            if(!uuid.isEmpty() && !userEmail.isEmpty()){
                validationResult =true;
            }
        }
        
        if (userRole == Role.PATIENT) {
            completePatientRegistration(uuid, userEmail);
        } else if (userRole == Role.ADMIN) {
            completeAdminRegistration(uuid, userEmail);
        }
    }

    private static void completePatientRegistration(String uuid, String userEmail){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        String dateOfBirth = "";
        int findResult = ProcessManager.findUserByRole(uuid, userRole);

        if( findResult == 1){
            //since script's normal execution result is 0, we check for 1 as the result
        }else if(findResult == 0){
            //if we found the user then the registration was completed
            Helpers.printInfo("Registration completion not available for provided user details.");
            return;
        }else{
            Helpers.printInfo("Something went wrong with while veriying user profile");
            return;
        }

        Helpers.printUserFieldPrompt("firstname");
        String firstname = input.nextLine();

        Helpers.printUserFieldPrompt("lastname");
        String lastname = input.nextLine();

        Helpers.printUserFieldPrompt("password");
        String password = input.nextLine();

        Helpers.printUserFieldPrompt("date of birth (YYYY-MM-DD)");
        try{
            dateOfBirth = dateFormat.format(dateFormat.parse(input.nextLine()));
        } catch (ParseException ex) {
            Helpers.printError("Invalid date format entered: " + ex.getLocalizedMessage());
        } 
        Helpers.printMessage("Are you HIV positive?");
        Helpers.printOption(1, "True");
        Helpers.printOption(2, "False");
        Helpers.printUserOptionPrompt();

        Integer isHIVPositive = Integer.parseInt(input.nextLine());

        String artStartDate = "";
        String diagnosisDate = "";
        Integer isOnART = 0;

        if (isHIVPositive == 1) {
            Helpers.printUserFieldPrompt("diagnosis date (YYYY-MM-DD)");

            try {
                diagnosisDate = dateFormat.format(dateFormat.parse(input.nextLine()));
    
                Helpers.printMessage("Are you on ART treatment?");
                Helpers.printOption(1, "True");
                Helpers.printOption(2, "False");
                Helpers.printUserOptionPrompt();
    
                isOnART = Integer.parseInt(input.nextLine());
    
                if (isOnART == 1) {
                    Helpers.printUserFieldPrompt("ART Start date (YYYY-MM-DD)");
                    artStartDate = dateFormat.format(dateFormat.parse(input.nextLine()));
                }
            } catch (ParseException ex) {
                Helpers.printError("Invalid date format entered: " + ex.getLocalizedMessage());
            } catch (NumberFormatException ex){
                Helpers.printError("Invalid number entered: " + ex.getLocalizedMessage());
            }
        } 

        Helpers.printUserFieldPrompt("Country ISO (e.g., US, UK)");

        String countryISO = input.nextLine();

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
        String findResult = ProcessManager.findUser(uuid);

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
            inputFromScanner= input.nextLine();
        }
        return inputFromScanner;
    }

    private static void showAdminUI(String loginUserId){
        if(userRole != Role.ADMIN) {
            return;
        }
        Helpers.printLine();
        Helpers.printMessage("Choose an option:");

        Helpers.printOption(1, "Register New User");
        Helpers.printOption(2, "Download User Reports");

        Helpers.printOption(0, "Exit");
        Helpers.printLine();

        int userInput = Integer.parseInt(input.nextLine());

        switch (userInput) {
            case 1:
                initiateNewUser();
                break;
            case 2:
                createUserReports();
                break;
            case 0:
                //application exit
                Helpers.printInfo("Goodbye :)!");

                return;
            default:
            Helpers.printError("Unknown option. Exiting application...");

            return;
        }

    }
    private static void showPatientUI(String loginUserId){
    String rawUser = ProcessManager.findUser(loginUserId);
    Helpers.printInfo("yeah" +rawUser);
    User user = convertRawUserIntoUser(rawUser);
    while (true) {
        Helpers.printLine();
        Helpers.printMessage("Choose an option:");

        Helpers.printOption(1, "View Profile");
        Helpers.printOption(2, "Edit Profile");
        Helpers.printOption(0, "Exit");
        Helpers.printLine();

        int userInput = Integer.parseInt(input.nextLine());

        switch (userInput) {
            case 1:
                //TODO: read user
                ProcessManager.getLifeExpectancyStats();
                break;

            case 2:
                //TODO: edit user
                showEditPatientMenu(user);

                return;

            case 0:
                //application exit
                Helpers.printInfo("Goodbye :)!");
                return;

            default:
                Helpers.printError("Unknown option. Exiting application...");
                return;
            }
        }
    }

    private static User convertRawUserIntoUser(String rawUser){
        String[] userArray = rawUser.split(",");
        Helpers.printInfo(userArray[0]);
        Map<String, String> userAttributes = new HashMap<>();
        for (String attribute : userArray) {
            var attr = attribute.trim().split(":");
            Helpers.printInfo(attribute.toString());
            Helpers.printInfo(attr[0].toString());
            Helpers.printInfo(attr[1].toString());
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
    private static void showEditPatientMenu(User user) {
        // TODO Auto-generated method study
        //placeholder content
        Patient patient = (Patient)ProcessManager.viewUser("");
        // UUID,email,role,isProfileComplete,firstName,lastName,hashed_password,userId,dateOfBirth,isHIVPositive,diagnosisDate,isOnART,ARTStartDate,countryISO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Helpers.printMessage("Choose a field to edit:");
        Helpers.printOption(1, "First Name:"+ patient.getFirstName());
        Helpers.printOption(2, "Last Name"+ patient.getLastName());
        Helpers.printOption(3, "Email" + patient.getEmail());
        Helpers.printOption(4, "Date of Birth" + patient.getDateOfBirth());
        Helpers.printOption(5, "Is HIV Positive?" + patient.getHIVPositive());
        Helpers.printOption(6, "Diagnosis Date" + patient.getDiagnosisDate());
        Helpers.printOption(7, "Is on ART?" + patient.isOnART());
        Helpers.printOption(8, "ART Start Date" + patient.getArtStartDate());
        Helpers.printOption(9, "Country");
        Helpers.printLine();
                
        int userInput = Integer.parseInt((input.nextLine()));
        
        switch (userInput) {
            case 1:
                Helpers.printOption(1, "Capture new first name");
                ProcessManager.editUser(patient.getUuid(), patient.getFirstName(), input.nextLine());
                break;
        
                case 2:
                    Helpers.printUserFieldPrompt("first name");
                    ProcessManager.editUser(patient.getUuid(), patient.getFirstName(), input.nextLine());
                    break;
            default:
                break;
        }

    }

    private static void createUserReports(){
        try {
            ProcessManager.generateAllUserData();
            //Old delivarable to download two empty csv files.
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
        }
    }
    
    private static void initiateNewUser(){
            User newUser;

            Helpers.printLine();
            Helpers.printMessage("Register New User");
            Helpers.printLine();

            String email = setInputfromScanner(input, "email");

            Helpers.printMessage("Choose a role for this account:");
            Helpers.printOption(1, "Patient");
            Helpers.printOption(2, "Admin");
            Helpers.printUserOptionPrompt();

            String newUserRole = input.nextLine();

            UserManager userManager = new UserManager();
            newUser = userManager.createUser(email, newUserRole);

            if(newUser == null)
                return;
                
            Helpers.printMessage("User created successfully.");
            Helpers.printMessage("Please take note of your UUID below and use it to login and complete registration:");
            Helpers.printMessage(newUser.getUuid());
            Helpers.printInfo("Registering user...");

            ProcessManager.initiateUserRegistration(newUser);
            
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
