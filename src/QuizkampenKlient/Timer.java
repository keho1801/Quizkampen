package QuizkampenKlient;

import java.awt.Color;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JPanel;

public class Timer extends Thread {
    
    long timeToAnswer;
    int interval;
    int height = 100;
    int heightReduction;
    Socket socketToServer;
    JPanel timer;
    String fromUser;
    PrintWriter out;
    
    public Timer (int timeToAnswer, JPanel timer, String fromUser, PrintWriter out, Socket socketToServer) {
        this.timeToAnswer = timeToAnswer;
        interval = timeToAnswer / 100;
        this.timer = timer;
        this.fromUser = fromUser;
        this.out = out;
        this.socketToServer = socketToServer;
        
        timer.setBackground(Color.green);
        heightReduction = (height/interval) / 2;
    }
    
    @Override
    public void run() {
        
        while (timeToAnswer >= 0) {  
            try {
                Thread.sleep(interval);
                timeToAnswer -= interval;
                timer.setSize(timer.getWidth(), height);
                height -= heightReduction;
                
                if (height <= 0) {
                    fromUser = "Wrong Answer, hombre";
                        out.println(fromUser);
                    this.interrupt();
                    break;
                }
                
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
