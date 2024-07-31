package core.infra.middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import core.entity.User;

public class ProcessManager {
    
    public static void RegisterUser(User user){
        Process p;
    
        try{
            List<String> cmdList = new ArrayList<String>();
            cmdList.add("sh");
            cmdList.add("/");
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            p=pb.start();
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line=reader.readLine()) != null){
                System.out.println(line);
            };

        }catch(IOException | InterruptedException ex){
            ex.printStackTrace();
        }
    }
}