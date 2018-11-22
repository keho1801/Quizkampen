package QuizkampenServer;

import Models.Player;
import Models.Question;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
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
    private String filePath;
    private String XstrInput;
    private String YstrInput;
    private int numberOfQuestionsPerRound;
    private int numberOfRoundsPerGame;
    private List<String> nameList;
    private List<Integer> scoreList;

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

        String filePath = "src/QuizKampenServer/playerScoreboard.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            nameList = new ArrayList<>();
            scoreList = new ArrayList<>();
            String tempName;
            int tempScore;
            while (reader.readLine() != null) {
                String line = reader.readLine();
                tempName = line.substring(0, line.indexOf(":") - 1);
                tempScore = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
                nameList.add(tempName);
                scoreList.add(tempScore);
            }
        }catch(Exception e){
            System.out.println("Kunde inte läsa från filen");
        }

    }
    public void writeToFile(Player player){
        try(FileWriter writer = new FileWriter(filePath,true);){
            String print;
            print = player.getName() + ": " + player.getWins()+"\n";
            writer.write(print);
            writer.close();

            }catch(Exception e){
                System.out.println("Kunde inste skriva till filen");
            }
    }

    @Override
    public void run() {
        try {

            //player x och y får inte ha samma namn
            while(Xinput.readLine()!= Yinput.readLine()){
                playerX.setName(Xinput.readLine());
                playerY.setName(Yinput.readLine());
            //ifall namnet kenny finns i namelist så får vi wins i samma index för player.
            //namnet kommer adderas därför kollar vi sista indexens poäng när vi skriver ut.
            if (nameList.contains(playerX.getName().toLowerCase())) {
                playerX.setWins(scoreList.get(nameList.lastIndexOf(playerX.getName())));
            }
            if (nameList.contains(playerY.getName().toLowerCase())) {
                playerY.setWins(scoreList.get(nameList.lastIndexOf(playerY.getName())));
            }
            }
            Xoutput.writeObject(playerX);
            Youtput.writeObject(playerY);
            while (true) {
                QuestionUtil q1 = new QuestionUtil();
                q1.initializeQuestionDatabase();
                q1.shuffleQuestionList();
                numberOfQuestionsPerRound = q1.getnrOfQuestionsPerRound();
                numberOfRoundsPerGame = q1.getnrOfRoundsPerGame();

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
                                System.out.println(playerX.getName() + " fick en poäng");
                            }
                            if (questionsInGame.get(i).getAnswers()[0].equals(YstrInput)) {
                                playerY.setScorePerRound(playerY.getScorePerRound() + 1);
                                playerY.setScorePerGame(playerY.getScorePerGame() + 1);
                                System.out.println(playerY.getName() + " fick en poäng");
                            }
                        }
                    }
                    System.out.println("Runda avklarad!");
                    Xoutput.writeObject(playerY);
                    Youtput.writeObject(playerX);
                    o++;
                }

                if (playerX.getScorePerGame() > playerY.getScorePerGame()) {
                    playerX.setWins(playerX.getWins() + 1);
                }
                if (playerY.getScorePerGame() > playerX.getScorePerGame()) {
                    playerY.setWins(playerY.getWins() + 1);
                }
                    writeToFile(playerX);
                    writeToFile(playerY);
                    

                if (!Xinput.readLine().equalsIgnoreCase("nytt spel") && !Yinput.readLine().equalsIgnoreCase("nytt spel")) {
                    break;
                }

            }
        } catch (IOException ex) {
            System.out.println("något gick fel");
        }
    }
}
