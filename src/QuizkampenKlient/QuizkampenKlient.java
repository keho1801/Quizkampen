package QuizkampenKlient;

import Models.*;
import java.awt.*;
import static java.awt.BorderLayout.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Random;
import javax.swing.*;

public class QuizkampenKlient  extends JFrame implements ActionListener{
    
    JPanel backgoundPanel = new JPanel();
    JPanel questionsPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JLabel question = new JLabel();
    JLabel category = new JLabel("Kategori");
    JButton nextRound = new JButton("Nästa fråga");
    JButton[] buttons = new JButton[4];
    JButton button1 = new JButton("");
    JButton button2 = new JButton("");
    JButton button3 = new JButton("");
    JButton button4 = new JButton("");
    
    Object fromServer;
    Question questionFromServer;
    Player playerFromServer;
    String answer;
    PrintWriter out;
    Properties properties = new Properties();
    ObjectInputStream in;
    String fromUser;
    
    public QuizkampenKlient(){
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        fromUser = JOptionPane.showInputDialog("Ange namn: ");
        
        
        try {
            properties.load(new FileInputStream("src/QuizkampenKlient/ClientSettings.properties"));
        } catch (Exception e){
            e.printStackTrace();
        }

        String fName = properties.getProperty("fontName","Verdana");
        int fSize = Integer.parseInt(properties.getProperty("fontSize", "24"));
        

        question.setFont(new Font(fName, Font.PLAIN, fSize));
        question.setForeground(Color.black);
        question.setBackground(Color.WHITE);
        backgoundPanel.setLayout(new BorderLayout());
        questionsPanel.setLayout(new GridLayout(2, 2));
        questionsPanel.setLayout(new FlowLayout());
        backgoundPanel.add(question, NORTH);
        question.setPreferredSize(new Dimension(550, 300));
        backgoundPanel.add(questionsPanel, CENTER);
        backgoundPanel.add(infoPanel, SOUTH);
        infoPanel.add(category);
        category.setPreferredSize(new Dimension(350, 50));
        infoPanel.add(nextRound);
        nextRound.setPreferredSize(new Dimension(200, 50));
        nextRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                runWhile();
            }
        });
        questionsPanel.add(button1);
        questionsPanel.add(button2);
        questionsPanel.add(button3);
        questionsPanel.add(button4);
        button1.setPreferredSize(new Dimension(270, 150));
        button2.setPreferredSize(new Dimension(270, 150));
        button3.setPreferredSize(new Dimension(270, 150));
        button4.setPreferredSize(new Dimension(270, 150));
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        add(backgoundPanel);
        
        setSize(600, 760);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        try{
            Socket socketToServer = new Socket(InetAddress.getLocalHost(), 12345);
            out = new PrintWriter(socketToServer.getOutputStream(), true);
            in = new ObjectInputStream(socketToServer.getInputStream());
            
            out.println(fromUser);
            runWhile();
            Thread.sleep(1000);
            runWhile();
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setButtons(String[] answers){
        int index;
        emptyButtons();
        nextRound.setVisible(false);
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            while(true) {
                index = random.nextInt(4);
                if (buttons[index].getText().equals("")) {
                    buttons[index].setText(answers[i]);
                    buttons[index].setBackground(Color.white);
                    buttons[index].setToolTipText("You can do it!");
                    buttons[i].setEnabled(true);
                    break;
                }
            }
        }
    }
    
    public void emptyButtons(){
        buttons[0].setText("");
        buttons[1].setText("");
        buttons[2].setText("");
        buttons[3].setText("");
    }
    
    public boolean isCorrectAnswer(JButton button, Question answers){
        boolean result;
        if (button.getText().equals(answers.getAnswers()[0])) {
            result = true;
        }else {
            result = false;
        }
        for (int i = 0; i < 4; i++) {
            buttons[i].setEnabled(false);
        }
        return result;
    }
    
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
        } 
        
        out.write(((JButton) event.getSource()).getText());
        nextRound.setVisible(true);
    }
    
    //Om en Player tas emot ska namnet kollas och poäng placeras ut
    //Om användaren vill spela ett nytt spel skickas en "nytt spel" sträng
    //Sätt fast kategorilabeln
    
    public void runWhile(){
        try {
            
            if (fromServer instanceof Question) {
                questionFromServer = ((Question) in.readObject());
                if (questionFromServer.getQuestion() == null){
                    question.setText("Välkommen " + fromUser);
                } else {
                    question.setText(questionFromServer.getQuestion());
                    setButtons(questionFromServer.getAnswers());
                    category.setText(questionFromServer.getCategory());
                }
            } else if (fromServer instanceof Player) {
                playerFromServer = ((Player) in.readObject());
                question.setText(playerFromServer.getName());
                playerFromServer = ((Player) in.readObject());
                nextRound.setText(playerFromServer.getName());
            } else if (fromServer instanceof String) {
                question.setText((String) fromServer);
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        QuizkampenKlient qk = new QuizkampenKlient();
        
    }

}