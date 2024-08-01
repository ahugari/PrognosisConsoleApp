package core.infra.middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import core.entity.Admin;
import core.entity.Patient;
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
            String[] cmdArray = new String[]{"bash", "core/infra/scripts/patient_registration.sh", patient.getFirstName(),patient.getLastName(),patient.getUserId(),patient.getPassword(), patient.getUuid(), patient.getEmail(), patient.getDateOfBirth(),isHIVPositive, patient.getDiagnosisDate().toString(), isOnART, patient.getArtStartDate().toString(), patient.getCountryISO() };
            
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
          String[] cmdArray = new String[]{"bash", "core/infra/scripts/admin_registration.sh", email, password};
          
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

    public static int verifyCountryISO(String isoCode){
    Process p;
  
    try{
        String[] cmdArray = new String[]{"bash", "core/infra/scripts/verify_Country_ISO.sh", isoCode};
        
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
}