package core.infra.middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import core.entity.Admin;
import core.entity.Patient;
import core.entity.Role;
import core.entity.User;
import core.shared.Helpers;

public class ProcessManager {
    
    public static int initiateUserRegistration(User user){
      Process p;
    
      try{
          String[] cmdArray = new String[]{"bash", "core/infra/scripts/user_registration.sh",user.getEmail(),user.getUuid(),user.getRole().toString()};
          
          Helpers.printInfo("Calling bash script...");
          ProcessBuilder pb = new ProcessBuilder(cmdArray);
          pb.redirectErrorStream(true);
          pb.inheritIO();
          Helpers.printInfo("Executing bash script...");
          p=pb.start();
          p.waitFor();
          
          Helpers.printInfo("Bash script executed. Returning results...");
          BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line;
          while((line=reader.readLine()) != null){
              Helpers.printInfo(line);
          };
          return 1;

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }

      return 0;
    }

    public static int registerPatient(Patient patient){
      String isHIVPositive =  patient.getHIVPositive() ? "true" : "false";
      String isOnART = patient.isOnART() ? "true" : "false";
        Process p;
    
        try{
            String[] cmdArray = new String[]{"bash", "core/infra/scripts/patient_registration.sh", patient.getFirstName(),patient.getLastName(),patient.getUserId(),patient.getPassword(), patient.getUuid(), patient.getEmail(), patient.getDateOfBirth().toString(),isHIVPositive, patient.getDiagnosisDate().toString(), isOnART, patient.getArtStartDate().toString(), patient.getCountryISO() };
            
            Helpers.printInfo("Calling bash script...");
            ProcessBuilder pb = new ProcessBuilder(cmdArray);
            pb.redirectErrorStream(true);
            pb.inheritIO();
            Helpers.printInfo("Executing bash script...");
            p=pb.start();
            p.waitFor();
            
            Helpers.printInfo("Bash script executed. Returning results...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line=reader.readLine()) != null){
                Helpers.printInfo(line);
            };

            return 1;

        }catch(IOException | InterruptedException ex){
            Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
        }

        return 0;
    }

    public static int registerAdmin(Admin admin){
        Process p;
    
        try{
            String[] cmdArray = new String[]{"bash", "core/infra/scripts/admin_registration.sh", admin.getFirstName(),admin.getLastName(),admin.getUserId(),admin.getPassword(), admin.getUuid(), admin.getEmail()};
            
            Helpers.printInfo("Calling bash script...");
            ProcessBuilder pb = new ProcessBuilder(cmdArray);
            pb.redirectErrorStream(true);
            pb.inheritIO();
            Helpers.printInfo("Executing bash script...");
            p=pb.start();
            p.waitFor();
            
            Helpers.printInfo("Bash script executed. Returning results...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line=reader.readLine()) != null){
                Helpers.printInfo(line);
            };

            return 1;

        }catch(IOException | InterruptedException ex){
            Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
        }

        return 0;
    }

    public static int login(String email, String password){
      
      Process p;
  
      try{
          String[] cmdArray = new String[]{"bash", "core/infra/scripts/login.sh", email, password};
          
          Helpers.printInfo("Calling bash script...");
          ProcessBuilder pb = new ProcessBuilder(cmdArray);
          pb.redirectErrorStream(true);
          pb.inheritIO();
          Helpers.printInfo("Executing bash script...");
          p=pb.start();
          
          Helpers.printInfo("Bash script executed. Returning results...");
          BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line;
          while((line=reader.readLine()) != null){
            Helpers.printInfo(line);
          }
          return  p.waitFor();

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }

      return 2;
  }

    public static int findUser(String uuid){
      String[] cmdArray = new String[]{"bash", "core/infra/scripts/find_user.sh", uuid};

      return runBashScript(cmdArray);
    }

    public static int viewUser(String uuid){
      //TODO: implement view user
      findUser(uuid);
      return 1;
    }


    public static int editUser(String uuid, String current_value, String new_value) {
    //   function to start the update profile process
      Process p;
      try {
        String[] cmdArray = new String[]{"bash", "core/infra/scripts/update_profile.sh", uuid, current_value, new_value};

        Helpers.printInfo("Calling the bash script...");
        ProcessBuilder pb = new ProcessBuilder(cmdArray);

        pb.redirectErrorStream(true);
        pb.inheritIO();

        Helpers.printInfo("Executing bash script...");
        p=pb.start();
        p.waitFor();

		Helpers.printInfo("Bash script executed successfully. Returning results....");
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while((line=reader.readLine()) != null) {
			Helpers.printInfo(line);
		};

		return 0;

      } catch(IOException | InterruptedException e) {
        Helpers.printError("Could not complete script execution." + e.getLocalizedMessage());
      }
	  return 0;
      
    }

    
    public static int findUserByRole(String uuid, Role role){
      String[] cmdArray = new String[] {"bash", "core/infra/scripts/find_user_by_role.sh", uuid, role.toString()};
      return runBashScript(cmdArray);
    }

    public static Map<String, String> getLifeExpectancyStats(){
      Process p;
      Map<String,String> statsMap = new HashMap<>();
      try{
        String[] cmdArray = new String[]{"bash", "core/infra/scripts/life_expectancy.sh"};
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectErrorStream(true);
        p=pb.start();
        p.waitFor();
        
        while(p.inputReader().ready()){
          String readInput = p.inputReader().readLine();
          statsMap.put(readInput.split(" ")[0], readInput.split(" ")[1]);
        }
        
        return statsMap;
        
      }catch(IOException ex ){
        ex.getStackTrace();
      }catch(InterruptedException ex){
        ex.getStackTrace();
      }

      return statsMap;
    }

	
    public static int verifyCountryISO(String isoCode){
      Process p;
    
      try{
          String[] cmdArray = new String[]{"bash", "core/infra/scripts/countryISOVerification.sh", isoCode};
          
          Helpers.printInfo("Verifying ISO code...");
          ProcessBuilder pb = new ProcessBuilder(cmdArray);
          pb.redirectErrorStream(true);
          pb.inheritIO();
          p=pb.start();
          p.waitFor();
          
          BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line;
          while((line=reader.readLine()) != null){
              Helpers.printInfo(line);
          };

          return 1;

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }

    return 0;
  }

    private static int runBashScript(String[] commandArray){
      try{
        Helpers.printInfo("Calling bash script...");
        ProcessBuilder pb = new ProcessBuilder(commandArray);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        Helpers.printInfo("Executing bash script...");
        Process p=pb.start();
        
        Helpers.printInfo("Bash script executed. Returning results...");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line=reader.readLine()) != null){
          Helpers.printInfo(line);
        }

        return p.waitFor();
        

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }

      //something went 
      return 1;
    }

}