package QuizkampenKlient;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private String answer;
    
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
