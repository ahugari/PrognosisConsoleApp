package core.infra.middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import core.entity.User;
import core.shared.Helpers;

public class ProcessManager {
    
    public static int initiateUser(User user){
        Process p;
        try {        
          List<String> cmdList = new ArrayList<String>();
          cmdList.add("sh core/infra/scripts/test_script.sh");
          // adding command and args to the list
          ProcessBuilder pb = new ProcessBuilder(cmdList);
          p = pb.start();
                    
          p.waitFor(); 
          BufferedReader reader=new BufferedReader(new InputStreamReader(
           p.getInputStream())); 
          String line; 
          while((line = reader.readLine()) != null) { 
            System.out.println(line);
          } 
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return 0;
    }

    public static int registerUser(User user){
        Process p;
    
        try{
            List<String> cmdList = new ArrayList<String>();
            cmdList.add("sh");
            // cmdList.add("ls");
            // cmdList.add("sh core/infra/scripts/admin_creation.sh ");
            cmdList.add("core/infra/scripts/test_script.sh " + user.getEmail() + " " + user.getUuid());
            //assumption that path to file is under scripts directory
            // cmdList.add("/infra/scripts/user_manage.sh initiate_user_registration " + user.getEmail() + " " +user.getUuid() +" "+ user.getRole());
            
            Helpers.printInfo("Calling bash script...");
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            pb.redirectErrorStream(true);
            Helpers.printInfo("Executing bash script...");
            p=pb.start();
            p.waitFor();
            
            Helpers.printInfo("Bash script executed. Returning results...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line=reader.readLine()) != null){
                Helpers.printInfo(line);
            };

        }catch(IOException | InterruptedException ex){
            Helpers.printError("Could not complete script execution." + ex.getLocalizedMessage());
        }

        return 0;
    }
}