package QuizkampenKlient;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class Timer extends Thread {
    
    long timeToAnswer;
    long second = 100;
    int height = 50;
    JPanel timer;
    
    public Timer (int timeToAnswer, JPanel timer) {
        this.timeToAnswer = timeToAnswer;
        this.timer = timer;
        timer.setBackground(Color.green);
//        timer.setPreferredSize(new Dimension(30, height));
    }
    
    @Override
    public void run() {
        
        while(timeToAnswer >= second) {  
            System.out.println("Tid kvar: " + timeToAnswer);
            try {
                Thread.sleep(second);
                timeToAnswer -= second;
                timer.setSize(timer.getWidth(), height);
                height -= 1;
                
            } catch (InterruptedException e) {
                System.out.println("Du hann svara på frågan innan tiden tog slut. Duktig du är. " + timeToAnswer);
                
            }

        }
    }
}
