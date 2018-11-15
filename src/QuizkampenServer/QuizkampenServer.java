/*
 */

package QuizkampenServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author kenny
 */
public class QuizkampenServer {
    public static void main(String[] args) throws Exception {
        try (
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket klientSocket = serverSocket.accept();
            ObjectOutputStream ut = new ObjectOutputStream(klientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(klientSocket.getInputStream()));
            PrintWriter outString = new PrintWriter(klientSocket.getOutputStream(), true);
            ) {
            
            String input;
            Question q = new Question("pelle", "123");
            while ((input = in.readLine()) != null) {
                System.out.println(input);
                ut.writeObject(q);
                outString.println("Hejhej");
                ut.flush();

            }

        }
        
//            String input;
//            while ((input = in.readLine()) != null) {
////                ServerSideGame game = new ServerSideGame();
////                Player playerX 
////                        = new Player(listener.accept(), 'X', game);
////                Player playerO 
////                        = new Player(listener.accept(), 'O', game);
////                playerX.setOpponent(playerO);
////                playerO.setOpponent(playerX);
////                playerX.start();
////                playerO.start();
//
//                String namn = in.readLine();
//                System.out.println(namn);
//                Question q1 = new Question(null, "123", null, "12332");
//                ut.writeObject((Object) q1);
            }
        
        }
    


