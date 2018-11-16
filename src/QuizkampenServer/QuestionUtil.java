package QuizkampenServer;

import Models.Question;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    
    public void initializeQuestionDatabase() {
        String[] q1 = { "349", "251", "198", "365" };
        questionsDatabase.add(new Question("Hur många ledamöter har Sveriges riksdag?", q1, category[0]));
        
        String[] q2 = { "David Lagercrantz", "Henning Mankell", "Mons Kallentoft", "Håkan Nesser" };
        questionsDatabase.add(new Question("Vad heter författaren som skrev uppföljaren till Stig Larssons Millenium-trilogi?", q2, category[2]));
        
        String[] q3 = { "Nationalteatern", "Hoola Bandoola Band", "Blå tåget", "KSMB" };
        questionsDatabase.add(new Question("Vilken känd musikgrupp var Ica-Stig (Hans Mosesson) medlem i?", q3, category[2]));
        
        String[] q4 = { "Herrens ankomst", "I väntan", "Förberedelse", "Jesus återuppståndelse" };
        questionsDatabase.add(new Question("Vad betyder ordet ”advent”?", q4, category[3]));
        
        String[] q5 = { "Mona-Lisa", "Mona Sahlin", "Monet", "Monica Zetterlund" };
        questionsDatabase.add(new Question("Vem ler på en känd tavla?", q5, category[2]));
        
        String[] q6 = { "Huden", "Hjärnan", "Gluteus maximus", "Tjocktarmen" };
        questionsDatabase.add(new Question("Vilket är kroppens största organ?", q6, category[3]));
        
        String[] q7 = { "Långe Jan", "Långe Karl", "Kort Karl", "Halv-Dan" };
        questionsDatabase.add(new Question("Vilken av följande fyrer hittar man på Ölands södra udde?", q7, category[3]));
        
        String[] q8 = { "Håkan Hellström", "Iron Maiden", "Bruce Springsteen", "Beyoncé" };
        questionsDatabase.add(new Question("Vem har publikrekordet för en musikföreställning på Ullevi?", q8, category[1]));
        
        String[] q9 = { "Katharine Hepburn", "Meryl Streep", "Daniel Day-Lewis", "Jack Nicholson" };
        questionsDatabase.add(new Question("En av dessa har belönats med hela 4st Oscar-statyetter för bästa huvudroll. Vem?", q9, category[2]));
        
        String[] q10 = { "100C", "-100C", "0C", "10C" };
        questionsDatabase.add(new Question("Vilken var den urpsrungliga fryspunkten satt av Anders Celsius?", q10, category[3]));
        
        String[] q11 = { "Vänsterpartiet", "Kristdemokraterna", "Folkpartiet", "Centern" };
        questionsDatabase.add(new Question("Vilket parti ingick inte i Decemberöverenskommelsen 2014?", q11, category[0]));
        
        String[] q12 = { "272cm", "255cm", "261cm", "288cm" };
        questionsDatabase.add(new Question("Hur lång var världens längsta man?", q12, category[0]));
     
    }
    
    public void shuffleQuestionList() {
        Collections.shuffle(questionsDatabase);
    }

    public List<Question> getQuestionsDatabase() {
        return questionsDatabase;
    }

    public List<Question> getQuestionsInGame(int nrOfQuestionsInGame,String category) {
        List<Question> nullListDatabase = new ArrayList<>();
        for (int i = 0; i <questionsDatabase.size(); i++) {
            if(questionsDatabase.get(i).getQuestion() != null){
                if(questionsDatabase.get(i).getCategory().equalsIgnoreCase(category)){
                    questionsInGame.add(questionsDatabase.get(i));
                    questionsDatabase.get(i).setQuestionNull();
                    if(questionsInGame.size() == nrOfQuestionsInGame){
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
