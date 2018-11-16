package Models;

import java.io.Serializable;

public class Question implements Serializable {
    static final long serialVersionUID = 42L;
    private String question;
    private String category;
    private String[] answers;
    
    public Question(String question, String[] answers, String category) {
        this.question = question;
        this.category = category;
        this.answers = answers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
    public void setQuestionNull(){
        this.question = null; 
    }
}
