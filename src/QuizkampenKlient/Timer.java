package QuizkampenKlient;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Timer extends Thread {
    
    long timeToAnswer;
    long second = 100;
    int height = 100;
    int width = 30;
    JPanel timer;
    
    public Timer (int timeToAnswer, JPanel timer) {
        this.timeToAnswer = timeToAnswer;
        this.timer = timer;
        timer.setBackground(Color.green);
        timer.setPreferredSize(new Dimension(width, height));
    }
    
    @Override
    public void run() {
        
        while(timeToAnswer >= second) {  
            System.out.println("Tid kvar: " + timeToAnswer);
            try {
                Thread.sleep(second);
                timeToAnswer -= second;
                height -=5;
                timer.setSize(timer.getHeight(), height);
                
            } catch (InterruptedException e) {
                // returnera sträng som inte är rätt svar
                System.out.println("Du hann svara på frågan innan tiden tog slut. Duktig du är. " + timeToAnswer);
                
            }

        }
    }
}
