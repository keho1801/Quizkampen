package QuizkampenKlient;

import java.awt.Color;
import javax.swing.JPanel;

public class Timer extends Thread {
    
    long timeToAnswer;
    int interval;
    int height = 100;
    int heightReduction;
    JPanel timer;
    String fromUser;
    
    public Timer (int timeToAnswer, JPanel timer, String fromUser) {
        this.timeToAnswer = timeToAnswer;
        this.timer = timer;
        this.fromUser = fromUser;
        timer.setBackground(Color.green);
        interval = timeToAnswer / 100;
        heightReduction = (height/interval) / 2;
    }
    
    @Override
    public void run() {
        
        while(timeToAnswer >= 0) {  
            try {
                Thread.sleep(interval);
                timeToAnswer -= interval;
                timer.setSize(timer.getWidth(), height);
                height -= heightReduction;
                if (height <= 0) {
                    fromUser = "Wrong Answer, hombre";
                }
                
            } catch (InterruptedException e) {
                // Player answered the question in time.
            }

        }
    }
}
