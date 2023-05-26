/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/ given to us by Lab4
 * 
 * This file contains 4 classes essential to the GUI of the app, 
 * List - Contains a list of all questions
 * Header - Contains App title and clear all button
 * Footer - Contains Ask a question button
 * App frame - Brings together 3 aforementioned classes
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.border.Border;
import java.io.File;

import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.rmi.ConnectException;

/*
 * Footer that contains ask a question button
 */
class Footer extends JPanel {

  JButton recordButton;
  
  Color foregroundColor = new Color(200, 200, 200);
  Color backgroundColor = new Color(50, 50, 50);
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);
  Border emptyBorder = BorderFactory.createEmptyBorder();

  Footer() {
    this.setPreferredSize(new Dimension(400, 60));
    this.setBackground(backgroundColor);

    recordButton = new JButton("<html><p style='text-align:center;'>Start</p></html>");
    recordButton.setOpaque(true);
    recordButton.setBorderPainted(false);
    recordButton.setHorizontalAlignment(SwingConstants.CENTER);
    recordButton.setFont(new Font("Sans-serif", Font.BOLD, 18));
    recordButton.setBackground(red);
    recordButton.setForeground(foregroundColor);
    recordButton.setBorder(new EmptyBorder(10, 20, 10, 20)); //set margins
    recordButton.setFocusPainted(false); // disable text highlight
    UIManager.put("Button.select", darkRed);
    this.add(recordButton);
  }

  /**
   * Getter method for record button 
   */
  public JButton getRecordButton() {
    return recordButton;
  }
}

/**
 * Header that contains the title of app and clear all button
 */
class Header extends JPanel {
  
  Color foregroundColor = new Color(200, 200, 200);
  Color backgroundColor = new Color(50, 50, 50);

  Header() {
    //nested panel in header for more control
    JPanel nestedPanel = new JPanel(new BorderLayout());
    nestedPanel.setBackground(backgroundColor);
    this.add(nestedPanel, BorderLayout.CENTER);

    // create the program title for the first 3/4 of the header
    JLabel titleText = new JLabel("SayIt Assistant"); // Text of the header
    titleText.setHorizontalAlignment(JLabel.CENTER);
    titleText.setPreferredSize(new Dimension(200, 60));
    titleText.setFont(new Font("Sans-serif", Font.BOLD, 20));
    titleText.setForeground(foregroundColor);
    titleText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the center
    nestedPanel.add(titleText, BorderLayout.CENTER);

    this.setPreferredSize(new Dimension(400, 60)); // Size of the header
    this.setBackground(backgroundColor);
  }
}

/**
 * App frame class that combines all buttons, header, footer, and list
 */
class AppFrame extends JFrame {
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);
  Color darkGrey = new Color (50,50,50);
  Color foregroundColor = new Color(200, 200, 200);

  private Header header;
  private Footer footer;
  public List list;
  public JScrollPane scrollPane; // making list of questions scrollable
  public JSplitPane splitPane; // split window for question history and answer display
  public JPanel answerPane; // custom JPanel to display answer in our split pane
  public JScrollPane answerScroll; // contains answer Pane to make it scrollable
  static JTextArea content; // added to answerPane to display text and change when needed

  private JButton recordButton;
  public CommandHandler commhandler;

  AppFrame() {

    this.setSize(1200, 1000); // 1200 width and 1000 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    header = new Header();
    footer = new Footer();
    list = new List();

    answerPane = new JPanel();
    answerPane.setBackground(darkGrey);
    content = new JTextArea() ;
    content.setLineWrap(true);
    content.setWrapStyleWord(true);
    content.setPreferredSize(new Dimension(400, 400));
    content.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

    content.setFont(new Font("Arial", Font.BOLD, 15));
    content.setForeground(foregroundColor);
    content.setBackground(darkGrey);
    answerPane.add(content);

    answerScroll = new JScrollPane(answerPane);

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    scrollPane = new JScrollPane(list);

    list.setPreferredSize(new Dimension(400, 105 * list.numPrompts));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, answerScroll);
    splitPane.setBackground(darkGrey);
    this.add(splitPane);

    recordButton = footer.getRecordButton();
    revalidate();
  }

  /**
   * This method adds mouse listeners to the record button 
   * and the delete and clear all button
   */
  public void addListeners() {
    Record recorder = new Record();
    
    
    recordButton.addMouseListener (
      new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          recorder.startRecording();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
          recorder.stopRecording();

          // try {
          //   String questionTranscription = Whisper.getQuestion();
          //   Question question = list.createQuestion(questionTranscription);
          //   String answer = ChatGPT.getAnswer(questionTranscription);
          //   question.setContent(answer);
          //   label.setText(answer);
          //   list.saveQuestions();
          //   repaint();
          //   revalidate();
          // } catch (Exception w) {
          //   w.printStackTrace();
          // }
          try {
            String transcription = Whisper.getQuestion();
            commhandler.HandlePrompt(transcription);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    );
  }

  public void registerCommHandler(CommandHandler ch) {
    this.commhandler = ch;
  }
}

public class GUI {

  public static void main(String args[]) {
    // Create the frame
    AppFrame app = new AppFrame();
    PromptFactory pf = new PromptFactory();
    CommandHandler ph = new CommandHandler(app, pf);
    app.addListeners();

    // app.list.loadQuestions();
    app.repaint();
    app.revalidate();
  }
}


@interface override {
}