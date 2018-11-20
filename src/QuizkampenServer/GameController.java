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
        q.initializeQuestionDatabase();
        q.shuffleQuestionList();
        
        questionsInGame = q.getQuestionsDatabase();
        
        this.playerX = X;
        this.playerY = Y;
        this.Xsocket = socketX;
        this.Ysocket = socketY;
        try {
            Xinput = new BufferedReader(new InputStreamReader(Xsocket.getInputStream()));
            Xoutput = new ObjectOutputStream(Xsocket.getOutputStream());
            Yinput = new BufferedReader(new InputStreamReader(Ysocket.getInputStream()));
            Youtput = new ObjectOutputStream(Ysocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("något gick fel" + e);
        }

    }

    public void sendQuestions(ObjectOutputStream output) throws IOException {
        for (Question q : questionsInGame) {
            output.writeObject(q);
        }
    }

    @Override
    public void run() {
        try {
            playerX.setName(Xinput.readLine());
            playerY.setName(Yinput.readLine());
            Xoutput.writeObject(playerX);
            Youtput.writeObject(playerY);

            while (true) {
                
                for (int i = 0; i < numberOfGamesPerRound; i++) {

                    Xoutput.writeObject(questionsInGame.get(i));
                    Youtput.writeObject(questionsInGame.get(i));

//                    XstrInput = Xinput.readLine();
//                    System.out.println(XstrInput);
//                    YstrInput = Yinput.readLine();
//                    System.out.println(YstrInput);
                    
                    if ((XstrInput = Xinput.readLine()) != null && (YstrInput = Yinput.readLine()) != null) {
                        
                        
                        if (questionsInGame.get(i).getAnswers()[0].equals(XstrInput)) {
                            playerX.setScore(playerX.getScore() + 1);
                            System.out.println(playerX.getName() + " fick en poäng");
                        }
                        if (questionsInGame.get(i).getAnswers()[0].equals(YstrInput)) {
                            playerY.setScore(playerY.getScore() + 1);
                            System.out.println(playerY.getName() + " fick en poäng");
                        }
                    }
                }
                System.out.println("Runda avklarad!");
                Xoutput.writeObject(playerY);
                Youtput.writeObject(playerX);

//                if (!Xinput.readLine().equalsIgnoreCase("nytt spel") && !Yinput.readLine().equalsIgnoreCase("nytt spel")) {
//                    break;
//                }
            }
        } catch (IOException ex) {
            System.out.println("något gick fel");
        }
    }
}
