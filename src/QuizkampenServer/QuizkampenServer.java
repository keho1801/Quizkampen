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

            ServerSocket serverSocket = new ServerSocket(12345);
            
            try{
                while (true) {

                Player playerX = new Player("PlayerX");

                Player playerY = new Player("PlayerY");
                
                GameController game = new GameController(playerX, playerY,serverSocket.accept(),serverSocket.accept());
                
                playerX.setOpponent(playerY);
                playerY.setOpponent(playerX);
                playerX.setGame(game);
                playerY.setGame(game);
                game.start();
                
            }
            }finally{
                serverSocket.close();
            }
        
        }

    }
