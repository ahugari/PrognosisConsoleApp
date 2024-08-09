package core.infra.middleware;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
          //use while in debug mode 
          // String[] cmdArray = new String[]{"bash", "src/core/infra/scripts/patient_registration.sh", patient.getFirstName(),patient.getLastName(),patient.getUserId(),patient.getPassword(), patient.getUuid(), patient.getEmail(), patient.getDateOfBirth().toString(),isHIVPositive, patient.getDiagnosisDate().toString(), isOnART, patient.getArtStartDate().toString(), patient.getCountryISO() };

          //use while in production mode
            String[] cmdArray = new String[]{"bash", "core/infra/scripts/patient_registration.sh", patient.getFirstName(),patient.getLastName(),patient.getUserId(),patient.getPassword(), patient.getUuid(), patient.getEmail(), patient.getDateOfBirth(),isHIVPositive, patient.getDiagnosisDate(), isOnART, patient.getArtStartDate(), patient.getCountryISO() };
            
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
            //use while in debug mode
            // String[] cmdArray = new String[]{"bash", "src/core/infra/scripts/admin_registration.sh", admin.getFirstName(),admin.getLastName(),admin.getUserId(),admin.getPassword(), admin.getUuid(), admin.getEmail()};

            //use while in production mode
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

    public static String login(String email, String password){
      Process p;
  
      try{
          //use while in debug mode
           // String[] cmdArray = new String[]{"bash", "src/core/infra/scripts/login.sh", email, password};
          //use while in production mode
          String[] cmdArray = new String[]{"bash", "core/infra/scripts/login.sh", email, password};
          
          Helpers.printInfo("Calling bash script...");
          ProcessBuilder pb = new ProcessBuilder(cmdArray);
          pb.redirectErrorStream(true);
          Helpers.printInfo("Executing bash script...");
          p=pb.start();
          p.waitFor();
          
          UUID uId=null;
          try{
            String rs = p.inputReader().readLine();
            Helpers.printError(rs);
            if(rs != null){
              uId = UUID.fromString(rs);
            }
          }catch(IllegalArgumentException ex){
            Helpers.printError("Unable to complete login.");
          }
          Helpers.printInfo("Bash script executed. Returning results...");

          return uId.toString();

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }

      return null;
  }

    public static String findUser(String uuid){
      String[] cmdArray = new String[]{"bash", "core/infra/scripts/find_user.sh", uuid};
      try{
        Helpers.printInfo("Calling bash script...");
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectErrorStream(true);
        Helpers.printInfo("Executing bash script...");
        Process p=pb.start();
        p.waitFor();
        
        String result = p.inputReader().readLine();
        Helpers.printInfo("Bash script executed. Returning results...");

        return result;
      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }
      return null;
    }

    public static User viewUser(String uuid){
      //TODO: implement view user
      findUser(uuid);

      //return this as a plceholder
      return new Patient("");
    }

    public static File generateAllUserData(){
      String path = "core/infra/scripts/generate_all_user_data.sh";
      String[] cmdArray = new String[]{"bash",path};

      try{
        Helpers.printInfo("Calling bash script...");
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        Helpers.printInfo("Executing bash script...");
        Process p=pb.start();
        p.waitFor();
        
        Helpers.printInfo("Bash script executed. Returning results...");
        
        Helpers.printInfo("Complete user data report has been created. Download it from: "+ path);

        //bonus formatted file
        generateAllUserDataFormatted();
      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }

      //something went wrong
      return null;
    }

    public static void generateAllUserDataFormatted(){
      String path = "core/infra/scripts/generate_all_user_data_formatted.sh";
      String[] cmdArray = new String[]{"bash",path};

      try{
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        Process p=pb.start();
        p.waitFor();
        
        Helpers.printInfo("Formatted user data report has been created. Download it from: "+ path);

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }
    }

    public static int editUser(String uuid, String oldValue, String newValue){
          //   function to start the update profile process
          Process p;
          try {
            String[] cmdArray = new String[]{"bash", "core/infra/scripts/update_profile.sh", uuid, oldValue, newValue};
    
            Helpers.printInfo("Calling the bash script...");
            ProcessBuilder pb = new ProcessBuilder(cmdArray);
    
            pb.redirectErrorStream(true);
            pb.inheritIO();
    
            Helpers.printInfo("Executing bash script...");
            p=pb.start();
            p.waitFor();
    
        Helpers.printInfo("Bash script executed successfully. Returning results....");

    
        return 0;
    
          } catch(IOException | InterruptedException e) {
            Helpers.printError("Could not complete script execution." + e.getLocalizedMessage());
          }
        return 1;
    }
    
    public static int findUserByRole(String uuid, Role role){
      //use this in debug mode only
      // String[] cmdArray = new String[] {"bash", "src/core/infra/scripts/find_user_by_role.sh", uuid, role.toString()};

      //use this in production mode only
      String[] cmdArray = new String[] {"bash", "core/infra/scripts/find_user_by_role.sh", uuid, role.toString()};

      try{
        Helpers.printInfo("Calling bash script...");
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        Helpers.printInfo("Executing bash script...");
        Process p=pb.start();
        p.waitFor();
        
        String result = p.inputReader().readLine();

        Helpers.printInfo(result);

        Helpers.printInfo("Bash script executed. Returning results...");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line=reader.readLine()) != null){
          Helpers.printInfo(line);
        }

        

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }


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

      //something went wrong
      return 1;
    }

    public static void getPatientProfileIncludingLifeSpan(String uuid, Double lifeSpan) {
      String[] cmdArray = new String[]{"bash", "core/infra/scripts/view_patient_details.sh", uuid, lifeSpan.toString()};
      try{
        Helpers.printInfo("Calling bash script...");
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        Helpers.printInfo("Executing bash script...");
        Process p=pb.start();
        p.waitFor();
        
        Helpers.printInfo("Bash script executed. Returning results...");
        

      }catch(IOException | InterruptedException ex){
          Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
      }
    }

}