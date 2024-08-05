package core.infra.ui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
                //     Helpers.printOption(1, "Register New User");
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

        int loginResult = ProcessManager.login(loginEmail, userPassword);
        
        if(loginResult==0)
        {
            Helpers.printMessage("User logged in successfully.");
            if(userRole == Role.PATIENT){
                showPatientUI();
            }else if (userRole == Role.ADMIN){
                showAdminUI();
            }                     
        }
    }

    private static void completeProfileRegistration(){
        Helpers.printLine();
        Helpers.printMessage("Complete Profile Registration");
        Helpers.printLine();

        Helpers.printUserFieldPrompt("email");
        String userEmail = input.nextLine();

        Helpers.printUserFieldPrompt("uuid");
        String uuid = input.nextLine();

        if (userRole == Role.PATIENT) {
            completePatientRegistration(uuid, userEmail);
        } else if (userRole == Role.ADMIN) {
            completeAdminRegistration(uuid, userEmail);
        }
    }

    private static void completePatientRegistration(String uuid, String userEmail){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

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

        String dateOfBirth = input.nextLine();

        Helpers.printMessage("Are you HIV positive?");
        Helpers.printOption(1, "True");
        Helpers.printOption(2, "False");
        Helpers.printUserOptionPrompt();

        Integer isHIVPositive = Integer.parseInt(input.nextLine());

        Date artStartDate = new Date();
        Date diagnosisDate = new Date();
        Integer isOnART = 0;

        if (isHIVPositive == 1) {
            Helpers.printUserFieldPrompt("diagnosis date (YYYY-MM-DD)");

            try {
                diagnosisDate = dateFormat.parse(input.nextLine());
    
                Helpers.printMessage("Are you on ART treatment?");
                Helpers.printOption(1, "True");
                Helpers.printOption(2, "False");
                Helpers.printUserOptionPrompt();
    
                isOnART = Integer.parseInt(input.nextLine());
    
                if (isOnART == 1) {
                    Helpers.printUserFieldPrompt("ART Start date (YYYY-MM-DD)");
                    artStartDate = dateFormat.parse(input.nextLine());
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
        patient.setOnART(isOnART==1? true: false);
        patient.setFirstName(firstname);
        patient.setPassword(password);
        patient.setUuid(uuid);
        patient.setUserId(uuid);
        patient.setHIVPositive(isHIVPositive ==1? true :false);


        Helpers.printInfo("Completing Patient Profile...");

        ProcessManager.registerPatient(patient); 
    }

    private static void completeAdminRegistration(String uuid, String userEmail){
        int findResult = ProcessManager.findUser(uuid);

        if(findResult!=0){
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

    private static void showAdminUI(){
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
    private static void showPatientUI(){
    //TODO: implement patient UI
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
                break;

            case 2:
                //TODO: edit user
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
    }
    private static void createUserReports(){
        try {
            File userDataReport = createUserDataReport();
            if(userDataReport!=null){                
                Helpers.printInfo("User report created. Please check resources folder.");            
            }
            File userAnalyticsReport = createUserAnalyticsReport();
            if(userAnalyticsReport!=null){
                Helpers.printInfo("User Analytics created. Please check resources folder.");            
            }
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
            Helpers.printMessage("Please take note of your UUID and use it to login and complete registration.");
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
