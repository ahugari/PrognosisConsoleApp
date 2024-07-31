package core.infra.ui;

import java.util.Scanner;

public class Main {
        public static void main(String[] args){
            //entry point of app
            Scanner input = new Scanner(System.in);

            System.out.print("Enter an integer: ");

            int myInt = input.nextInt();
            System.out.printf("You entered %d.%n%n", myInt);
            
    }
}
