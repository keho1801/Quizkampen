package QuizkampenServer;

import Models.Question;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController extends Thread {
    private List<Question> questionsInGame = new ArrayList<>();
    private Player playerX;
    private Player playerY;
    private Socket Xsocket;
    private Socket Ysocket;
    private BufferedReader Xinput;
    private ObjectOutputStream Xoutput;
    private BufferedReader Yinput;
    private ObjectOutputStream Youtput;
    private int state;
    private QuestionUtil q;
    private String XstrInput;
    private String YstrInput;
    private int numberOfGamesPerRound = 2;

    public GameController() {
        q = new QuestionUtil();
        //tar in alla frågor
        q.initializeQuestionDatabase();
        //shufflar listan för frågorna
        q.shuffleQuestionList();
        //läs in antal frågor/ronder från properties filen 
        questionsInGame = q.getQuestionsInGame(numberOfGamesPerRound,"Vetenskap");
        }
public void setPlayers(Player X, Player Y){
        this.playerX = X;
        this.playerY = Y;
        this.Xsocket = X.socket;
        this.Ysocket = Y.socket;
        try{
            Xinput = new BufferedReader(new InputStreamReader(Xsocket.getInputStream()));
            Xoutput = new ObjectOutputStream(Xsocket.getOutputStream());
            Yinput = new BufferedReader(new InputStreamReader(Ysocket.getInputStream()));
            Youtput = new ObjectOutputStream(Ysocket.getOutputStream());
        }catch(IOException e){
            System.out.println("något gick fel" + e);
        }
}


    @Override
    public void run() {
        try{
        state = 0;
        int Xscore = 0;
        int Yscore = 0;
        while(true){
                for(int i=0; i<numberOfGamesPerRound; i++){
                    Xoutput.writeObject(questionsInGame);
                    Youtput.writeObject(questionsInGame);
                    for(int y= 0; y<2; y++){   
                        if((XstrInput = Xinput.readLine())!=null){
                            if(questionsInGame.get(y).getAnswers()[0].equals(XstrInput)){
                                playerX.setScore(Xscore++);   
                            }
                        }
                        if((YstrInput = Yinput.readLine())!=null){
                            if(questionsInGame.get(y).getAnswers()[0].equals(XstrInput)){
                                    playerX.setScore(Xscore++);   
                            }

                        }
                    }
                    Xoutput.writeObject(playerY);
                    Xoutput.writeObject(playerX);    
                }
                if(!Xinput.readLine().equalsIgnoreCase("nytt spel")&& !Yinput.readLine().equalsIgnoreCase("nytt spel")){
                    break;
                }    
        }
        }
        catch (IOException ex) {
            System.out.println("något gick fel");
    }
}
}



