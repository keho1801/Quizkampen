package QuizkampenServer;

import Models.Player;
import Models.Question;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public GameController() throws IOException {
        q = new QuestionUtil();
        //tar in alla frågor
        q.initializeQuestionDatabase();
        //shufflar listan för frågorna
        q.shuffleQuestionList();
        //läs in antal frågor/ronder från properties filen
        
        Random random = new Random(4);
        String[] randomCategory = q.getCategory();
        
        
//        questionsInGame = q.getQuestionsInGame(numberOfGamesPerRound,randomCategory[random.nextInt(4)]); 
        questionsInGame = q.getQuestionsDatabase();
        
        
//        System.out.println(questionsInGame.get(0));
//        questionsInGame = q.getQuestionsDatabase().get
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
        while(true){
//            
//            sendQuestions(Xoutput);
//            sendQuestions(Youtput);
//            
//            checkQuestions(Xinput,playerX);
//            checkQuestions(Yinput,playerY);
//            
            
                for (int i = 0; i < numberOfGamesPerRound; i++){
                    
                    Xoutput.writeObject(questionsInGame.get(i));
                    Youtput.writeObject(questionsInGame.get(i));
         
                        if ((XstrInput = Xinput.readLine())!= null && (YstrInput = Yinput.readLine())!= null) {
                            
                            if (questionsInGame.get(i).getAnswers()[i].equals(XstrInput)){
                                playerX.setScore(playerX.getScore()+1);   
                            }
                            
                            if (questionsInGame.get(i).getAnswers()[i].equals(YstrInput)){
                                    playerY.setScore(playerY.getScore()+1);   
                            }
                        }
                        
                    Xoutput.writeObject(playerY);
                    Youtput.writeObject(playerX);
                }
                if (!Xinput.readLine().equalsIgnoreCase("nytt spel") && !Yinput.readLine().equalsIgnoreCase("nytt spel")){
                    break;
                }    
        }
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
    }
}
}
