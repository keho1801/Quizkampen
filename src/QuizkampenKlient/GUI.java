package QuizkampenKlient;

import Models.Player;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame{
    JPanel backgroundPanel = new JPanel();
    JPanel questionPanel = new JPanel();
    JPanel answersPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JPanel player1 = new JPanel();
    JPanel player2 = new JPanel();
    JTextArea question = new JTextArea();
    JTextArea player1Text  = new JTextArea();
    JTextArea player2Text  = new JTextArea();
    JLabel category = new JLabel("Kategori");
    JButton[] buttons;
    JButton nextRound;
    
    JButton iconFemale = new JButton();
    JButton iconMale = new JButton();
    Properties properties = new Properties();
    JPanel timerBar = new JPanel();
    
    String fName;
    int fSize;
    
    String icon = "src/Models/avatar_female.jpg";
    
    public GUI (JButton[] buttons, JButton nextRound) throws IOException{
        this.buttons = buttons;
        this.nextRound = nextRound;
        add(backgroundPanel);
        setTitle("Quizkampen");
        
        setSize(600, 570);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setBasicLayout(){
        try {
            properties.load(new FileInputStream("src/QuizkampenKlient/ClientSettings.properties"));
        } catch (Exception e){
            e.printStackTrace();
        }

        fName = properties.getProperty("fontName","Verdana");
        fSize = Integer.parseInt(properties.getProperty("fontSize", "24"));
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBackground(Color.WHITE);
        category.setPreferredSize(new Dimension(350, 50));
        
        questionPanel.setLayout(new BorderLayout());
        backgroundPanel.add(questionPanel, NORTH);
        questionPanel.add(question, CENTER);
        question.setFont(new Font(fName, Font.PLAIN, fSize));
        question.setForeground(Color.black);
        question.setBackground(Color.WHITE);
        question.setEditable(false);
        question.setLineWrap(true);
        question.setPreferredSize(new Dimension(550, 150));
        question.setMargin(new Insets(30, 30, 30, 30));
        question.setWrapStyleWord(true);
        
        backgroundPanel.add(infoPanel, SOUTH);
        infoPanel.setLayout(new GridLayout(1, 2));
        infoPanel.add(category, 0, 0);
        infoPanel.setBorder(new EmptyBorder(0, 20, 10, 20));
        infoPanel.add(nextRound, 0, 1);
        nextRound.setPreferredSize(new Dimension(200, 50));
        
        answersPanel.setLayout(new FlowLayout());
        backgroundPanel.add(answersPanel, CENTER);
        for (int i = 0; i < 4; i++) {
            buttons[i].setOpaque(true);
            buttons[i].setPreferredSize(new Dimension(270, 150));
            answersPanel.add(buttons[i]);
        }
    }
    
    public void setWelcomeLayout(String fromUser) throws IOException{
        question.setText("Välkommen " + fromUser + "\nVälj en avatar");
        category.setText("Väntar på motståndare");
        nextRound.setText("Starta spel");
        nextRound.setVisible(false);
        nextRound.setPreferredSize(new Dimension(200, 50));
        
        for (int i = 0; i < 4; i++) {
            answersPanel.remove(buttons[i]);
        }
        
        answersPanel.add(iconFemale);
        answersPanel.add(iconMale);
        iconFemale.setPreferredSize(new Dimension(270, 300));
        iconMale.setPreferredSize(new Dimension(270, 300));
        iconFemale.setBackground(Color.WHITE);
        iconMale.setBackground(Color.WHITE);

        iconFemale.setIcon(new ImageIcon("src/Models/avatar_female.jpg"));
        iconMale.setIcon(new ImageIcon("src/Models/avatar_male.jpg"));
        iconFemale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                icon = "src/Models/avatar_female.jpg";
            }
        });
        iconMale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                icon = "src/Models/avatar_male.jpg";
            }
        });
        repaint();
        revalidate();
    }
    
    public void setGameLayout(){
        backgroundPanel.remove(player1);
        backgroundPanel.remove(player2);
        answersPanel.remove(iconFemale);
        answersPanel.remove(iconMale);
        
        backgroundPanel.add(answersPanel);
        backgroundPanel.add(questionPanel);
        for (int i = 0; i < 4; i++) {
            answersPanel.add(buttons[i]);
        }
        
        timerBar.setPreferredSize(new Dimension(30, 100));
        timerBar.setBackground(Color.green);
        questionPanel.add(timerBar, EAST);
        //Timer timer = new Timer(5000, timerBar);
        //timer.start();
        
        nextRound.setText("Nästa fråga");
        repaint();
        revalidate();
    }
    
    public int setRoundLayout(Player player, Player opponent, int roundNumber){
        backgroundPanel.remove(answersPanel);
        backgroundPanel.remove(questionPanel);
        
        player1.setLayout(new FlowLayout());
        backgroundPanel.add(player1, NORTH);
        player1.add(iconFemale);
        iconFemale.setIcon(new ImageIcon(icon));
        iconFemale.setPreferredSize(new Dimension(200, 200));
        player1Text.setPreferredSize(new Dimension(200, 200));
        player1.add(player1Text);
        
        player2.setLayout(new FlowLayout());
        backgroundPanel.add(player2, CENTER);
        player2.add(iconMale);
        iconMale.setIcon(new ImageIcon("src/Models/avatar_female.jpg"));
        iconMale.setPreferredSize(new Dimension(200, 200));
        player2Text.setPreferredSize(new Dimension(200, 200));
        player2.add(player2Text);
        
        if (roundNumber == 1){
            player1Text.setText(player.getName() + "\nPoäng denna runda: " + player.getScorePerRound());
            player2Text.setText(opponent.getName() + "\nPoäng denna runda: " + opponent.getScorePerRound());
            nextRound.setText("Starta nästa runda");
            roundNumber++;
        } else if (roundNumber == 2){
            player1Text.setText(player.getName() + "\nPoäng denna runda: " + player.getScorePerRound() 
                    + "\nPoäng detta spel: " + player.getScorePerGame());
            player2Text.setText(opponent.getName() + "\nPoäng denna runda: " + opponent.getScorePerRound() 
                    + "\nPoäng detta spel: " + opponent.getScorePerGame());
            
            nextRound.setText("Nytt spel");
            roundNumber = 0;
        }
        repaint();
        revalidate();
        
        return roundNumber;
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
        for (int i = 0; i < 4; i++) {
            buttons[i].setText("");
        }
    }
}
