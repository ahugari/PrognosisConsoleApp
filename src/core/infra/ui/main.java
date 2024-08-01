package core.infra.ui;

import java.util.Scanner;

import core.entity.Patient;
import core.entity.Role;
import core.entity.User;
import core.entity.UserManager;
import core.infra.middleware.ProcessManager;
import core.shared.Helpers;

public class Main {
    public static void main(String[] args){
        
        Scanner input = new Scanner(System.in);

        Helpers.printLine();
        Helpers.printMessage("Welcome to Prognosis Application!");
        Helpers.printLine();
        Helpers.printMessage("Let's get started. What would you like to do?");
        Helpers.printOption(1, "Register New User");
        Helpers.printOption(2, "Login");
        Helpers.printOption(3, "Complete Registration as Patient");
        Helpers.printUserOptionPrompt();
        User newUser = null;
        try {
            Integer userInput = Integer.parseInt(input.nextLine());

            switch (userInput) {
                case 1:
                Helpers.printLine();
                Helpers.printMessage("Register New User");
                Helpers.printLine();
                Helpers.printUserFieldPrompt("email");
                
                String userEmail = input.nextLine();

                Helpers.printMessage("Choose a role for this account:");
                Helpers.printOption(1, "Patient");
                Helpers.printOption(2, "Admin");
                Helpers.printUserOptionPrompt();
                
                String userRole = input.nextLine();
                
                UserManager userManager = new UserManager();
                newUser = userManager.createUser(userEmail, userRole);
                if(newUser == null)
                    return;
                
                Helpers.printMessage("User created successfully.");
                Helpers.printMessage("Please take note of your UUID and use it to login and complete registration.");
                Helpers.printMessage(newUser.getUuid());

                Helpers.printInfo("Registering user...");
                ProcessManager.registerUser(newUser);

                break;

                case 2:
                Helpers.printLine();
                Helpers.printMessage("User Login");
                Helpers.printLine();
                UserManager exUserManager = new UserManager();
                
                Helpers.printUserFieldPrompt("email");
                String exUserEmail = input.nextLine();
                
                Helpers.printUserFieldPrompt("password");
                String exUserPassword = input.nextLine();
                
                Helpers.printInfo("Logging you in...");
                //hash the password and compare with existing
                //run login method here
                //show different menu depending on user type
                break;

                case 0:
                Helpers.printInfo("Goodbye :)!");
                    return;

                default:
                Helpers.printError("Unknown input value. Application exiting...");
                    break;
                }
        } catch (NumberFormatException e) {
            Helpers.printError("Wrong input value. Application exiting...");
        } catch (Exception ex){
            Helpers.printError("An error occured while processing your input: " + ex.getLocalizedMessage());
        }

        // ProcessManager.initiateUser(newUser);

    }
}
