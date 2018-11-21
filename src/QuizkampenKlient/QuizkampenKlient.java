package QuizkampenKlient;

import Models.Player;
import Models.Question;
import java.awt.*;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


public class QuizkampenKlient extends JFrame implements ActionListener{
    
    JPanel backgoundPanel = new JPanel();
    JPanel questionsPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JTextArea question = new JTextArea();
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
    String fName;
    int fSize;
    String icon;
    
    public QuizkampenKlient() throws IOException{
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

        fName = properties.getProperty("fontName","Verdana");
        fSize = Integer.parseInt(properties.getProperty("fontSize", "24"));
        backgoundPanel.setLayout(new BorderLayout());

        
        add(backgoundPanel);
        setTitle("Quizkampen");
        
        setSize(600, 570);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setWelcomeLayout();            

        
        try {
            Socket socketToServer = new Socket(InetAddress.getByName("172.20.201.127"), 12345);
            out = new PrintWriter(socketToServer.getOutputStream(), true);
            in = new ObjectInputStream(socketToServer.getInputStream());
  
            out.println(fromUser);
            runWhile();
            Thread.sleep(1000);
//            runWhile();
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setBasicLayout(){
        question.setFont(new Font(fName, Font.PLAIN, fSize));
        question.setForeground(Color.black);
        question.setBackground(Color.WHITE);
        infoPanel.setLayout(new GridLayout(1, 2));
        questionsPanel.setLayout(new FlowLayout());
        backgoundPanel.add(question, NORTH);
        question.setEditable(false);
        question.setLineWrap(true);
        question.setPreferredSize(new Dimension(550, 150));
        question.setMargin(new Insets(30, 30, 30, 30));
        question.setWrapStyleWord(true);
        backgoundPanel.add(questionsPanel, CENTER);
        backgoundPanel.add(infoPanel, SOUTH);
        infoPanel.add(category, 0, 0);
        infoPanel.setBorder(new EmptyBorder(0, 20, 10, 20));
        category.setPreferredSize(new Dimension(350, 50));
        infoPanel.add(nextRound, 0, 1);
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
        backgoundPanel.setBackground(Color.WHITE);
    }
    
    public void setWelcomeLayout() throws IOException{
        question.setFont(new Font(fName, Font.PLAIN, fSize));
        question.setForeground(Color.black);
        question.setBackground(Color.WHITE);
        infoPanel.setLayout(new GridLayout(1, 2));
        questionsPanel.setLayout(new FlowLayout());
        backgoundPanel.add(question, NORTH);
        question.setEditable(false);
        question.setLineWrap(true);
        question.setPreferredSize(new Dimension(550, 150));
        question.setMargin(new Insets(30, 30, 30, 30));
        question.setWrapStyleWord(true);
        question.setText("Välkommen " + fromUser + "\nVälj en avatar");
        backgoundPanel.add(questionsPanel, CENTER);
        backgoundPanel.add(infoPanel, SOUTH);
        infoPanel.add(category, 0, 0);
        infoPanel.setBorder(new EmptyBorder(0, 20, 10, 20));
        category.setPreferredSize(new Dimension(350, 50));
        infoPanel.add(nextRound, 0, 1);
        category.setText("Väntar på motståndare");
        nextRound.setText("Starta spel");
        nextRound.setVisible(false);
        nextRound.setPreferredSize(new Dimension(200, 50));
        nextRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                button1.setIcon(null);
                button2.setIcon(null);
                
                runWhile();
            }
        });
        questionsPanel.add(button1);
        questionsPanel.add(button2);
        button1.setPreferredSize(new Dimension(270, 300));
        button2.setPreferredSize(new Dimension(270, 300));
        button1.setBackground(Color.WHITE);
        button2.setBackground(Color.WHITE);

        button1.setIcon(new ImageIcon("src/Models/avatar_female.jpg"));
        button2.setIcon(new ImageIcon("src/Models/avatar_male.jpg"));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                icon = "src/Models/avatar_female.jpg";
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                icon = "src/Models/avatar_male.jpg";
            }
        });
    }
    
    public void setGameLayout(){
        question.setFont(new Font(fName, Font.PLAIN, fSize));
        question.setForeground(Color.black);
        question.setBackground(Color.WHITE);
        infoPanel.setLayout(new GridLayout(1, 2));
        questionsPanel.setLayout(new FlowLayout());
        backgoundPanel.add(question, NORTH);
        question.setEditable(false);
        question.setLineWrap(true);
        question.setPreferredSize(new Dimension(550, 150));
        question.setMargin(new Insets(30, 30, 30, 30));
        question.setWrapStyleWord(true);
        backgoundPanel.add(questionsPanel, CENTER);
        backgoundPanel.add(infoPanel, SOUTH);
        infoPanel.add(category, 0, 0);
        infoPanel.setBorder(new EmptyBorder(0, 20, 10, 20));
        category.setPreferredSize(new Dimension(350, 50));
        infoPanel.add(nextRound, 0, 1);
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
        } 
        
        out.println(((JButton) event.getSource()).getText());
        nextRound.setVisible(true);
    }
        
    public void runWhile(){
        try {
            fromServer = in.readObject();
            if (fromServer instanceof Question) {
                
                questionFromServer = (Question) fromServer;
                if (questionFromServer.getQuestion() == null){
                    question.setText("Välkommen " + fromUser);
                } else {
                    question.setText(questionFromServer.getQuestion());
                    setButtons(questionFromServer.getAnswers());
                    category.setText(questionFromServer.getCategory());
                    setGameLayout();
                }
            } else if (fromServer instanceof Player) {
                playerFromServer = ((Player) fromServer);
                //question.setText(playerFromServer.getName());
                category.setText("Kopplad till motståndare");
                nextRound.setVisible(true);
                
            } else if (fromServer instanceof String) {
                question.setText((String) fromServer);
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
