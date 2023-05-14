
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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.chrono.ThaiBuddhistEra;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.io.File;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;


class Question extends JPanel {

  JLabel index;
  JTextField questionName;
  JButton trashCanButton;

  Color backgroundColor = new Color(100, 100, 100);  // Dark gray
  Color green = new Color(188, 226, 158);

  Question() {
    this.setPreferredSize(new Dimension(400, 20)); // set size of question
    this.setBackground(backgroundColor); // set background color of question
    this.setLayout(new BorderLayout()); // set layout of question

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index, BorderLayout.WEST); // add index label to question

    questionName = new JTextField(""); // create question name text field
    questionName.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    questionName.setBackground(backgroundColor); // set background color of text field

    this.add(questionName, BorderLayout.CENTER);


    trashCanButton = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("../../../static/trashCanIcon.jpeg")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);;
      trashCanButton.setIcon(new ImageIcon(img));
    } catch (Exception ex) {
      System.out.println(ex);
    }

    trashCanButton.setBackground(backgroundColor);
    this.add(trashCanButton, BorderLayout.EAST);
  }

  public void changeIndex(int num) {
    this.index.setText(num + ""); // num to String
    this.revalidate(); // refresh
  }

  public JButton getTrashCan() {
    return trashCanButton;
  }
}


/*
 * This class extends JPanel and serves as the list container
 * for all questions and answers that the app has been asked
 * 
 * contains: updateNumbers, clearAllQuestions, removeSingle, 
 * saveQuestions, and loadQuestions
 */
class List extends JPanel {

  public int numQuestions = 5;
  Color backgroundColor = new Color(50, 50, 50);  // Dark gray

  List() {
    GridLayout layout = new GridLayout(0, 1);
    layout.setVgap(5); // Vertical gap
    this.setLayout(layout); // 10 questions
    this.setBackground(backgroundColor);
    this.setPreferredSize(new Dimension(400, 105 * this.numQuestions));
  }

  /**
   * Update the question numbers on the list
   */
  public void updateNumbers() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof Question) {
        ((Question) listItems[i]).changeIndex(i + 1);
      }
    }
  }


  /**
   * Clears all questions from app and database
   */
  public void clearAllQuestions() {
    for (Component c : getComponents()) {
      if (c instanceof Question) {
        remove(c); // remove the question
        updateNumbers(); // update the indexing of all items
        revalidate();
      }
    }
    this.setPreferredSize(new Dimension(400, 1000));
    this.numQuestions = 0;
    this.saveQuestions();
  }

  /**
   * Removes one question from the App frame
   */
  public void removeSingle(Component x){
    remove(x);
    updateNumbers();
    this.numQuestions -= 1;
    this.setPreferredSize(new Dimension(400, 105 * this.numQuestions));
    repaint();
    revalidate();
    this.saveQuestions();
  }

  /**
   * Loads questions from a file called "questions.txt"
   * @return an ArrayList of question
   */
  public ArrayList<Question> loadQuestions() {
    File file = new File("Questions.txt");
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        ArrayList<Question> result = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
          Question question = new Question();
          question.questionName.setText(line);
          this.add(question);
          result.add(question);
          revalidate();
          JButton trashCanButton = question.getTrashCan();
          trashCanButton.addMouseListener(
            new MouseAdapter() {
              @override
              public void mousePressed(MouseEvent e) {
                removeSingle(question);
              }
            }
          );
        }
        br.close();
        this.updateNumbers(); // Updates the numbers of the questions
        this.numQuestions = result.size();
        revalidate();
        return result;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
  }


  /**
   * Saves questions to a file called "Questions.txt"
   */
  public void saveQuestions() {
    
    try (FileWriter fw = new FileWriter("Questions.txt")) {
      Component[] listItems = this.getComponents();

      for (int i = 0; i < listItems.length; i++) {
        if (listItems[i] instanceof Question) {
          fw.write(((Question) listItems[i]).questionName.getText() + '\n');
        }
      }
      fw.close();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
  }

  /*
   * Creates question with the label of given string transcription
   */
  public Question createQuestion(String transcription) {
    Question q = new Question();
    q.questionName.setText(transcription);
    add(q);
    updateNumbers();
    return q;
  }
}


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

    recordButton = new JButton("<html><p style='text-align:center;'>Ask Question</p></html>");
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

  JButton clearAllButton;
  
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

    // create the clear all button for the last 1/4 of the header
    clearAllButton = new JButton("<html><p style='text-align:center;'>Clear<br>All</p></html>");
    clearAllButton.setHorizontalAlignment(SwingConstants.CENTER);
    clearAllButton.setVerticalAlignment(SwingConstants.CENTER);
    clearAllButton.setFont(new Font("Sans-serif", Font.BOLD, 12));
    clearAllButton.setBackground(backgroundColor);
    clearAllButton.setForeground(foregroundColor);
    clearAllButton.setBorder(BorderFactory.createLineBorder(foregroundColor));
    clearAllButton.setBorder(new EmptyBorder(10, 20, 10, 20)); //set margins
    clearAllButton.setFocusPainted(false); // disable text highlight
    nestedPanel.add(clearAllButton, BorderLayout.EAST);

    this.setPreferredSize(new Dimension(400, 60)); // Size of the header
    this.setBackground(backgroundColor);
  }

  /**
   * Getter method for clearAllButton
   */
  public JButton getClearAllButton() {
    return clearAllButton;
  }
}

/**
 * App frame class that combines all buttons, header, footer, and list
 */
class AppFrame extends JFrame {
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);

  private Header header;
  private Footer footer;
  public List list;
  public JScrollPane scrollPane;

  private JButton clearAllButton;
  private JButton recordButton;

  AppFrame() {


    this.setSize(1000, 1000); // 1000 width and 1000 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    header = new Header();
    footer = new Footer();
    list = new List();

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    // this.add(list, BorderLayout.CENTER); // Add list in middle of footer and title
    scrollPane = new JScrollPane(list);

    list.setPreferredSize(new Dimension(400, 105 * list.numQuestions));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(scrollPane);

    recordButton = footer.getRecordButton();
    clearAllButton = header.getClearAllButton();

    addListeners();
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

          
          try {
          String questionTranscription = MockWhisper.getQuestion();
          Question question = list.createQuestion(questionTranscription);
          revalidate();
          JButton trashCanButton = question.getTrashCan();
          trashCanButton.addMouseListener(
            new MouseAdapter() {
              @override
              public void mousePressed(MouseEvent e) {
                list.removeSingle(question);
              }
            }
          );
            Question answer = new Question();
            list.add(answer);
            list.updateNumbers();
            list.numQuestions += 2;
            list.setPreferredSize(new Dimension(400, 105 * list.numQuestions));
            repaint();
            revalidate();

            JButton trashCanButton2 = answer.getTrashCan();
            trashCanButton2.addMouseListener(
              new MouseAdapter() {
                @override
                public void mousePressed(MouseEvent e) {
                  list.removeSingle(answer);
                }
              }
            );

            String answerText = MockChatGPT.getAnswer(questionTranscription);
            answer.questionName.setText(answerText);
            list.saveQuestions();
          } catch (Exception w) {

          }
          
        }
      }
    );

    clearAllButton.addMouseListener(
      new MouseAdapter(){
        @override
        public void mousePressed(MouseEvent e) {
          list.clearAllQuestions();
          repaint();
          revalidate();
        }
      }
    );
  }
}

public class GUI {

  public static void main(String args[]) {
    AppFrame app = new AppFrame(); // Create the frame
    app.list.loadQuestions();
  }
}

@interface override {
}