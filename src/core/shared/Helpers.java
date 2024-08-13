package core.shared;

import java.io.Console;
import java.text.MessageFormat;

@SuppressWarnings("unused")
public class Helpers {
    final static String RESET = "\u001B[0m";
    final static String BLUE = "\u001B[34m";
    final static String BOLD = "\u001B[1m";

    public static void printInfo(String message){
        print(message, "Info:");
    }
    
    public static void printError(String message){
        print(message, "Error:");
    }
    public static void printLargeSpace(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");

    }
    public static void printLine(){
        print("------------------------------------------------------------------------------------",null);
    }
    public static void printHeader(String message){
        printLine();
        printLine();
        System.out.printf(MessageFormat.format("\t\t\t{0}{1}{2}{3}\n", BLUE, BOLD, message, RESET));
        printLine();
    }
    public static void printMessage(String message){
        print(message, null);
    }
    public static void print1OptionFooter(String option1){
        printLargeSpace();
        printLine();
        System.out.printf(MessageFormat.format("\t\t\t\t\t\t\t\t\t{0}{1}{2}{3}\n", BLUE, BOLD, option1, RESET));
        printLine();
        printLine();
    }

    public static void print2OptionFooter(String option1, String option2 ){
        printLargeSpace();
        printLine();
        System.out.printf(MessageFormat.format("\t\t\t\t\t\t\t{0}{1}{2}{3}\t{0}{1}{4}{3}\n", BLUE, BOLD, option1, RESET, option2));
        printLine();
        printLine();
    }

    public static void print3OptionFooter(String option1, String option2, String option3 ){
        printLargeSpace();
        printLine();
        System.out.printf(MessageFormat.format("{0}{1}{2}{3}\t\t\t\t\t\t{0}{1}{4}{3}\t{0}{1}{5}{3}\n", BLUE, BOLD, option1, RESET, option2, option3));
        printLine();
        printLine();
    }
    public static void printOption(int optionNumber, String optionMessage){
        print(optionNumber + ")" + " "+ optionMessage,null);
    }
    public static void printUserOptionPrompt(){
        print("Choose an option to continue or enter '0' to close the app.", null);
    }
    public static void printUserFieldPrompt(String fieldRequired){
        print("Enter your " + fieldRequired + " to continue:", null);
    }

    
    private static void print(String message, String prefix){
        if(prefix == null || prefix.isBlank()){
            System.out.println(message);
            return;
        }
        System.out.println(prefix + ':' + ' ' + message);
    }
}
