package QuizkampenKlient;

import java.awt.Color;
import javax.swing.JPanel;

public class Timer extends Thread {
    
    int timeToAnswer;
    int interval = timeToAnswer / 100;
    int height = 100;
    int heightReduction = 1;
    JPanel timer;
    
    public Timer (int timeToAnswer, JPanel timer) {
        this.timer = timer;
        this.timeToAnswer = timeToAnswer;
        timer.setBackground(Color.green);
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
                    System.out.println("timeout");
                    break;
                }
                
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception" + e.getMessage());
            }
        }
    }
}
