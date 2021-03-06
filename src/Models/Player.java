package Models;

import QuizkampenServer.GameController;
import java.io.Serializable;

public class Player implements Serializable {
    static final long serialVersionUID = 43L;
    private String name;
    private int scorePerGame;
    private int scorePerRound;
    private int wins;
    private int numberOfRoundsPerGame;
    
public Player(String name) {
    this.name = name;
}

    public String getPlayerName() {
        return name;
    }

    public void setScorePerGame(int score) {
        this.scorePerGame = score;
    }

    public int getScorePerGame() {
        return scorePerGame;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
    
    public void setName(String name){
        this.name = name;  
    }
    
    public String getName(){
        return name;
    }
    public void setScorePerRound(int score){
        this.scorePerRound = score ;
        
    }
    public int getScorePerRound(){
        return scorePerRound;
        
    }
    
    public void setNumberOfRoundsPerGame(int i){
        this.numberOfRoundsPerGame = i;
    }
    
    public int getNumberOfRoundsPerGame(){
        return numberOfRoundsPerGame;
    }
}
