package core.infra.middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import core.entity.User;
import core.shared.Helpers;

public class ProcessManager {
    
    public static int RegisterUser(User user){
        Process p;
    
        try{
            List<String> cmdList = new ArrayList<String>();
            cmdList.add("sh");
            cmdList.add("core/infra/scripts/test_script.sh");
            //assumption that path to file is under scripts directory
            // cmdList.add("/infra/scripts/user_manage.sh");
            
            Helpers.printInfo("Calling bash script...");
            ProcessBuilder pb = new ProcessBuilder(cmdList);
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
            Helpers.printError("Could not complete script execution.");
        }

        return 0;
    }
}