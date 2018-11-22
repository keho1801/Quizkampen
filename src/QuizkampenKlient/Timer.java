package QuizkampenKlient;

public class Timer extends Thread {
    
    long timeToAnswer;
    long second = 1000;
    
    public Timer (int timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
    }
    

    @Override
    public void run() {
        
        while(timeToAnswer > second) {  
            System.out.println("Tid kvar: " + timeToAnswer);
            try {
                Thread.sleep(second);
                timeToAnswer -= second;
                
            } catch (InterruptedException e) {
                System.out.println("Du hann svara på frågan innan tiden tog slut. Duktig du är. " + timeToAnswer);
                
            }

        }
    }
}
