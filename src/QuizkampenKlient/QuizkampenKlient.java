package QuizkampenKlient;

import Models.Player;
import Models.Question;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import javax.swing.*;

public class QuizkampenKlient extends JFrame implements ActionListener{
    JButton nextRound = new JButton("Nästa fråga");
    JButton[] buttons = new JButton[4];
    
    Object fromServer;
    Question questionFromServer;
    Player player;
    Player opponent;
    PrintWriter out;
    ObjectInputStream in;
    
    String fromUser;
    int roundNumber = 0;
    GUI gui;
    
    public QuizkampenKlient() throws IOException{
        fromUser = JOptionPane.showInputDialog("Ange namn: ");
        
        for (int i = 0; i < 4; i++) {
            buttons[i] = new JButton("");
            buttons[i].addActionListener(this);
        }
        
        nextRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (nextRound.getText().equals("Nytt spel")){
                    out.println("nytt spel");
                    player.setScorePerGame(0);
                    roundNumber = 1;
                }
                runWhile();
            }
        });
        gui = new GUI(buttons, nextRound);
        gui.setBasicLayout();
        gui.setWelcomeLayout(fromUser);            

        try {
            Socket socketToServer = new Socket(InetAddress.getByName("172.20.201.216"), 12345);
            out = new PrintWriter(socketToServer.getOutputStream(), true);
            in = new ObjectInputStream(socketToServer.getInputStream());
  
            out.println(fromUser);
            
            runWhile();
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean isCorrectAnswer(JButton button, Question answers){
        boolean result;
        if (button.getText().equals(questionFromServer.getAnswers()[0])) {
            result = true;
        }else {
            result = false;
        }
        for (int i = 0; i < 4; i++) {
            buttons[i].setEnabled(false);
        }
        return result;
    }
    
    @Override
    public void actionPerformed(ActionEvent event){
        String correctAnswer = "";
        for (int i = 0; i < 4; i++) {
            if (isCorrectAnswer(buttons[i], questionFromServer)){
                buttons[i].setBackground(Color.green);
                correctAnswer = buttons[i].getText();
            }
        }
        if (!((JButton) event.getSource()).getText().equals(correctAnswer)) {
            ((JButton) event.getSource()).setBackground(Color.red);
        } else {
            player.setScorePerRound(player.getScorePerRound() + 1);
            player.setScorePerGame(player.getScorePerGame() + 1);
        }
        
        out.println(((JButton) event.getSource()).getText());
        nextRound.setVisible(true);
    }
        
    public void runWhile(){
        try {
            fromServer = in.readObject();
            if (fromServer instanceof Question) {
                questionFromServer = (Question) fromServer;
                gui.question.setText(questionFromServer.getQuestion());
                gui.setButtons(questionFromServer.getAnswers());
                gui.category.setText(questionFromServer.getCategory());
                gui.setGameLayout();    
                
            } else if (fromServer instanceof Player) {
                if (roundNumber == 0){
                    player = ((Player) fromServer);
                    gui.category.setText("Kopplad till motståndare");
                    nextRound.setVisible(true);
                    roundNumber++;
                    
                } else if (roundNumber >= 1){
                    opponent = ((Player) fromServer);
                    roundNumber = gui.setRoundLayout(player, opponent, roundNumber);
                    player.setScorePerRound(0);
                }  
            }
        }catch (EOFException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }  
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
          
        QuizkampenKlient q = new QuizkampenKlient();
    }

}
