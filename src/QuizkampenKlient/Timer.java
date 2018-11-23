package QuizkampenKlient;

import javax.swing.JButton;

public class Timer extends Thread {
    
    long timeToAnswer;
    long second = 1000;
    JButton t1;
    JButton t2;
    JButton t3;
    
    public Timer (int timeToAnswer, JButton t1, JButton t2, JButton t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.timeToAnswer = timeToAnswer;
    }
    

    @Override
    public void run() {
        
        while(timeToAnswer >= second) {  
            System.out.println("Tid kvar: " + timeToAnswer);
            try {
                Thread.sleep(second);
                timeToAnswer -= second;
                t1.setVisible(false);
                
            } catch (InterruptedException e) {
                t2.setVisible(false);
                // returnera sträng som inte är rätt svar
                System.out.println("Du hann svara på frågan innan tiden tog slut. Duktig du är. " + timeToAnswer);
                
            }

        }
    }
}
