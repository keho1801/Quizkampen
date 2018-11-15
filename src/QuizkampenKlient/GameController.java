package QuizkampenKlient;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<Question> allQuestions = new ArrayList<>();
    
    public GameController() {
        
        allQuestions.add(new Question("Hur långt är ett snöre?", "3.1415"));
        allQuestions.add(new Question("Svara ja.", "ja"));
        allQuestions.add(new Question("Hur många frågor är det i detta spel?", "4"));
        allQuestions.add(new Question("Stämde fråga 1?", "nej"));
        allQuestions.add(new Question("Vad kommer efter 9?", "10"));
        allQuestions.add(new Question("Ska det finnas ananas på pizza?", "nej"));
        
    }
}
