package QuizkampenServer;

import Models.Player;
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
    private QuestionUtil q;
    private String XstrInput;
    private String YstrInput;
    private int numberOfGamesPerRound = 2;

    public GameController(Player X, Player Y, Socket socketX, Socket socketY) throws IOException {
        q = new QuestionUtil();
        //tar in alla frågor
        q.initializeQuestionDatabase();
        //shufflar listan för frågorna
        q.shuffleQuestionList();
        //läs in antal frågor/ronder från properties filen 
        questionsInGame = q.getQuestionsInGame(numberOfGamesPerRound,"Vetenskap");
        this.playerX = X;
        this.playerY = Y;
        this.Xsocket = socketX;
        this.Ysocket = socketY;
        try{
            Xinput = new BufferedReader(new InputStreamReader(Xsocket.getInputStream()));
            Xoutput = new ObjectOutputStream(Xsocket.getOutputStream());
            Yinput = new BufferedReader(new InputStreamReader(Ysocket.getInputStream()));
            Youtput = new ObjectOutputStream(Ysocket.getOutputStream());
        }catch(IOException e){
            System.out.println("något gick fel" + e);
        }

    }
public void sendQuestions(ObjectOutputStream output) throws IOException{
    for(Question q: questionsInGame){
        output.writeObject(q);
    }
}
public void checkQuestions(BufferedReader input,Player player) throws IOException{
    String answer;
    int score = 0;
    for(Question q: questionsInGame){
        if((answer = input.readLine())!= null){
            if(q.getAnswers()[0].equals(answer)){
                player.setScore(player.getScore()+1);
            }
        }
    }
}
    @Override
    public void run() {
        try{
            
            playerX.setName(Xinput.readLine());
            playerY.setName(Yinput.readLine());
            
            Xoutput.writeObject(playerX);
//            Xoutput.writeObject(playerX.getOpponent());
            Youtput.writeObject(playerY);
//            Youtput.writeObject(playerY.getOpponent());
            
        while(true){
//            
//            sendQuestions(Xoutput);
//            sendQuestions(Youtput);
//            
//            checkQuestions(Xinput,playerX);
//            checkQuestions(Yinput,playerY);
//            
            
                for(int i=0; i<numberOfGamesPerRound; i++){
                    Xoutput.writeObject(questionsInGame.get(0));
                    Xoutput.writeObject(questionsInGame.get(1));
                    Youtput.writeObject(questionsInGame.get(0));
                    Youtput.writeObject(questionsInGame.get(1));
                    
                    for(int j= 0; j<2; j++){   
                        if((XstrInput = Xinput.readLine())!=null){
                            if(questionsInGame.get(j).getAnswers()[0].equals(XstrInput)){
                                playerX.setScore(playerX.getScore()+1);   
                            }
                        }
                        if((YstrInput = Yinput.readLine())!=null){
                            if(questionsInGame.get(j).getAnswers()[0].equals(YstrInput)){
                                    playerY.setScore(playerY.getScore()+1);   
                            }

                        }
                    }
                    Xoutput.writeObject(playerY);
                    Youtput.writeObject(playerX);    
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



