package Models;

import QuizkampenServer.GameController;
import java.io.Serializable;

public class Player implements Serializable{
    static final long serialVersionUID = 43L;
    private String name;
    private Player opponent;
    private int score;
    private int wins;
    private GameController game;
    
public Player(String name) {
    this.name = name;
    this.game = game;
}

    public String getPlayerName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return opponent;
    }
    public void setGame(GameController g){
        this.game=g;
    }
    public void setName(String name){
        this.name = name;  
    }
    
}
