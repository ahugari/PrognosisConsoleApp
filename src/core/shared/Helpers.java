package core.shared;

import java.io.Console;

public class Helpers {
    public static void printInfo(String message){
        print(message, "Info:");
    } 
    public static void printError(String message){
        print(message, "Error:");
    }
    public static void printLine(){
        print("------------------------------",null);
    }
    public static void printMessage(String message){
        print(message, null);
    }
    public static void printOption(int optionNumber, String optionMessage){
        print(optionNumber + ")" + " "+ optionMessage,null);
    }
    public static void printUserOptionPrompt(){
        print("Choose an option to continue or enter '0' to close the app.", null);
    }
    public static void printUserFieldPrompt(String fieldRequired){
        print("Enter your " + fieldRequired + " to continue...", null);
    }

    
    private static void print(String message, String prefix){
        if(prefix == null || prefix.isBlank()){
            System.out.println(message);
            return;
        }
        System.out.println(prefix + ':' + ' ' + message);
    }
}
