/*
 */
package QuizkampenServer;

import Models.Player;
import Models.Question;
import java.io.BufferedReader;
import QuizkampenKlient.*;
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
                ObjectOutputStream out = new ObjectOutputStream(klientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(klientSocket.getInputStream()));
                PrintWriter outString = new PrintWriter(klientSocket.getOutputStream(), true);) {

            String input;
            Question q = new Question("pelle", null, "123");
            while ((input = in.readLine()) != null) {
                GameController game = new GameController();
                
                input = in.readLine();
                outString.println("Spelare " + input + " uppkopplad");
                Player playerX = new Player(serverSocket.accept(), input, game);
                
                input = in.readLine();
                outString.println("Spelare " + input + " uppkopplad");
                Player playerO = new Player(serverSocket.accept(), input, game);
                
                game.setPlayers(playerX, playerO);
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                game.start();
                
            }
        }
        }

    }
