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
    int nrOfQuestionsInGame;
    int nrOfQuestionsPerRound;
    
    public QuestionUtil() throws FileNotFoundException, IOException {
        
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/QuestionSettings.properties"));
            nrOfQuestionsInGame = Integer.parseInt(properties.getProperty("questionsPerGame"));
            nrOfQuestionsPerRound = Integer.parseInt(properties.getProperty("questionsPerRound"));   
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
    }
    
        public void initializeQuestionDatabase() throws IOException {
            
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

    public List<Question> getQuestionsInGame(int nrOfQuestionsInGame, String category) {
        List<Question> nullListDatabase = new ArrayList<>();
        for (int i = 0; i < questionsDatabase.size(); i++) {
            if (questionsDatabase.get(i).getQuestion() != null){
                if (questionsDatabase.get(i).getCategory().equalsIgnoreCase(category)){
                    questionsInGame.add(questionsDatabase.get(i));
                    questionsDatabase.get(i).setQuestionNull();
                    if (questionsInGame.size() == nrOfQuestionsInGame){
                        return questionsInGame;
                    }              
                }
                        
            }
        }
        return nullListDatabase;
    }

    public int getNrOfQuestionsInGame() {
        return nrOfQuestionsInGame;
    }

    public int getNrOfQuestionsPerRound() {
        return nrOfQuestionsPerRound;
    }

    public String[] getCategory() {
        return category;
    }
    
}