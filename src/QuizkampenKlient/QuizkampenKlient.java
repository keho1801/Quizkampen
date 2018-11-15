package QuizkampenKlient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuizkampenKlient {
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        System.out.println("hejhej");
        
        try (Socket socketToServer = new Socket(InetAddress.getByName("172.20.201.136"), 12345);
                PrintWriter out = new PrintWriter(socketToServer.getOutputStream(), true);
                ObjectInputStream in = new ObjectInputStream(socketToServer.getInputStream());
                
                BufferedReader userReader = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));){
            
            //Question fromServer;
            String fromUser = "Anna";
            System.out.println(fromUser);
            
            out.println(fromUser);
            
            while (true){
                Question fromServer = (Question) in.readObject();
                System.out.println(fromServer.getCorrectAnswer());
                System.out.println(userReader.readLine());
                System.out.println("Välkommen" + fromUser);
                
                System.out.println("hej");
                //Sätter frågan och knapparna
                }
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
