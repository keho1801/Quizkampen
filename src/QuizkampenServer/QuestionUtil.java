package QuizkampenServer;

import Models.Question;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class QuestionUtil {
    
    private List<Question> questionsDatabase = new ArrayList<>();
    private List<Question> questionsInGame = new ArrayList<>();
    String[] category = { "Samtid", "Sport och fritid", "Kultur och musik", "Vetenskap & historia" };
    private int nrOfRoundsPerGame;
    private int nrOfQuestionsPerRound;

    public QuestionUtil() throws FileNotFoundException, IOException {
        
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/QuizkampenServer/QuestionSettings.properties"));
            String questionsPerGameString = properties.getProperty("roundsPerGame").trim();
            nrOfRoundsPerGame = Integer.parseInt(questionsPerGameString);
            String questionsPerRoundString = properties.getProperty("questionsPerRound").trim();
            nrOfQuestionsPerRound = Integer.parseInt(questionsPerRoundString);   
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
    }
    
        public void initializeQuestionDatabase() throws IOException {
            questionsDatabase.clear();
            
            String filePath = "src/QuizKampenServer/questionDatabase.txt";
            String dbQuestion, dbAnswers, dbCategory;

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                while ((dbQuestion = reader.readLine()) != null) {
                    dbAnswers = reader.readLine();
                    dbCategory = reader.readLine();
                    String[] dbAnswersArr = dbAnswers.split(",");
                    questionsDatabase.add(new Question(dbQuestion, dbAnswersArr, dbCategory));       
                }
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
            }
    }
    
    public void shuffleQuestionList() {
        Collections.shuffle(questionsDatabase);
    }

    public List<Question> getQuestionsDatabase() {
        return questionsDatabase;
    }

    public List<Question> getQuestionsInGame() {
        List<Question> questionsInGame = new ArrayList<>();
        String tempCategory = questionsDatabase.get(0).getCategory();
        questionsInGame.add(questionsDatabase.get(0));
        
        for (int i = 1; i < questionsDatabase.size(); i++) {
            if(questionsDatabase.get(i).getCategory().equals(questionsDatabase.get(0).getCategory())){
                questionsInGame.add(questionsDatabase.get(i));
                questionsDatabase.remove(i);
            }
        }
        return questionsInGame;
    }
    
    public int getnrOfRoundsPerGame(){
        return nrOfRoundsPerGame;
    }
    
    public int getnrOfQuestionsPerRound(){
        return nrOfQuestionsPerRound;
    }
    
}
