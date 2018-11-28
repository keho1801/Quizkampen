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
            playerX.setScorePerGame(0);
            playerY.setScorePerGame(0);
            playerX.setName(Xinput.readLine());
            playerY.setName(Yinput.readLine());
            Xoutput.writeObject(playerX);
            Youtput.writeObject(playerY);
            while (true) {
                QuestionUtil q1 = new QuestionUtil();
                q1.initializeQuestionDatabase();
                q1.shuffleQuestionList();
                numberOfQuestionsPerRound = q1.getnrOfQuestionsPerRound();
                numberOfRoundsPerGame = q1.getnrOfRoundsPerGame();
                playerX.setNumberOfRoundsPerGame(numberOfRoundsPerGame);
                playerY.setNumberOfRoundsPerGame(numberOfRoundsPerGame);

                int o = 0;
                while (o < numberOfRoundsPerGame) {
                    questionsInGame = new ArrayList<>();
                    q1.shuffleQuestionList();
                    questionsInGame = q1.getQuestionsInGame();
                    
                    playerX.setScorePerRound(0);
                    playerY.setScorePerRound(0);
                    

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
                    System.out.println("Runda avklarad!");
                    System.out.println("Skickar resultat til klient, spelare X"+ " "+ playerX.getScorePerRound() + " "+playerX.getScorePerGame());
                    System.out.println("Skickar resultat til klient, spelare y"+ " "+ playerY.getScorePerRound() + " "+playerY.getScorePerGame());
                    Xoutput.writeObject(playerY);
                    Youtput.writeObject(playerX);
                    
                    playerY.setName("Y");
                    playerX.setName("X");

                    o++;
                }

                if(!Xinput.readLine().equalsIgnoreCase("nytt spel") && !Yinput.readLine().equalsIgnoreCase("nytt spel")) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("något gick fel");
        }
    }
}
