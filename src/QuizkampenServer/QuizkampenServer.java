package QuizkampenServer;

import Models.Player;
import java.net.InetAddress;
import java.net.ServerSocket;


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
                game.start();
                
            }
            } finally {
                serverSocket.close();
            }
        
        }

    }
