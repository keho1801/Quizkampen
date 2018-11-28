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

    private String XstrInput;
    private String YstrInput;
    private int numberOfQuestionsPerRound;
    private int numberOfRoundsPerGame;

    public GameController(Player X, Player Y, Socket socketX, Socket socketY) throws IOException {

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

    @Override
    public void run() {
        try {
            QuestionUtil q1 = new QuestionUtil();
            numberOfQuestionsPerRound = q1.getnrOfQuestionsPerRound();
            numberOfRoundsPerGame = q1.getnrOfRoundsPerGame();
            int xScore;
            int yScore;
            int xScoreGame;
            int yScoreGame;
            String xName;
            String yName;
            playerX.setScorePerGame(0);
            playerY.setScorePerGame(0);
            playerX.setName(Xinput.readLine());
            playerY.setName(Yinput.readLine());
            playerX.setNumberOfRoundsPerGame(numberOfRoundsPerGame);
            playerY.setNumberOfRoundsPerGame(numberOfRoundsPerGame);
            Xoutput.writeObject(playerX);
            Youtput.writeObject(playerY);
            while (true) {

                q1.initializeQuestionDatabase();
                q1.shuffleQuestionList();
                playerX.setScorePerGame(0);
                playerY.setScorePerGame(0);
                xScoreGame = 0;
                yScoreGame = 0;

                int o = 0;
                while (o < numberOfRoundsPerGame) {
                    questionsInGame = new ArrayList<>();
                    q1.shuffleQuestionList();
                    questionsInGame = q1.getQuestionsInGame();

                    for (int i = 0; i < numberOfQuestionsPerRound; i++) {

                        Xoutput.writeObject(questionsInGame.get(i));
                        Youtput.writeObject(questionsInGame.get(i));
                        
                        if ((XstrInput = Xinput.readLine()) != null && (YstrInput = Yinput.readLine()) != null) {

                            if (questionsInGame.get(i).getAnswers()[0].equals(XstrInput)) {
                                playerX.setScorePerRound(playerX.getScorePerRound() + 1);
                                playerX.setScorePerGame(playerX.getScorePerGame() + 1);
                                System.out.println(playerX.getName() + " fick en poäng"+ " "+ playerX.getScorePerRound() + " "+playerX.getScorePerGame());
                            }
                            if (questionsInGame.get(i).getAnswers()[0].equals(YstrInput)) {
                                playerY.setScorePerRound(playerY.getScorePerRound() + 1);
                                playerY.setScorePerGame(playerY.getScorePerGame() + 1);
                                System.out.println(playerY.getName() + " fick en poäng" + " "+ playerY.getScorePerRound() + " "+playerY.getScorePerGame());
                            }
                        }
                    }
                    
                    xName = playerX.getName();
                    yName = playerY.getName();
                    xScore = playerX.getScorePerRound();
                    xScoreGame = playerX.getScorePerGame();
                    
                    yScore = playerY.getScorePerRound();
                    yScoreGame = playerY.getScorePerGame();
                    //checka 0:an

                    playerX = new Player(xName);
                    playerY = new Player(yName);

                    playerY.setScorePerRound(yScore);
                    playerY.setScorePerGame(yScoreGame);

                    playerX.setScorePerRound(xScore);
                    playerX.setScorePerGame(xScoreGame);
                    
                    Xoutput.writeObject(playerY);
                    Youtput.writeObject(playerX); 
                    yScore = 0;
                    xScore = 0;
                                        
                    playerX.setScorePerRound(0);
                    playerY.setScorePerRound(0);
                    
                      

                    o++;
                }
                
                if(!Xinput.readLine().equalsIgnoreCase("nytt spel") && !Yinput.readLine().equalsIgnoreCase("nytt spel")) {
                    break;
                }
                YstrInput = Yinput.readLine();
            }
        } catch (IOException ex) {
            System.out.println("något gick fel");
        }
    }
}