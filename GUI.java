/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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

  Color gray = new Color(218, 229, 234);
  Color green = new Color(188, 226, 158);

  Question() {
    this.setPreferredSize(new Dimension(400, 20)); // set size of task
    this.setBackground(gray); // set background color of task
    this.setLayout(new BorderLayout()); // set layout of task


    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index, BorderLayout.WEST); // add index label to task

    questionName = new JTextField(""); // create task name text field
    questionName.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    questionName.setBackground(gray); // set background color of text field

    this.add(questionName, BorderLayout.CENTER);


    trashCanButton = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("trashCanIcon.jpeg"));
      trashCanButton.setIcon(new ImageIcon(img));
    } catch (Exception ex) {
      System.out.println(ex);
    }

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

class List extends JPanel {

  Color backgroundColor = new Color(50, 50, 50);  // Dark gray

  List() {
    GridLayout layout = new GridLayout(10, 1);
    layout.setVgap(5); // Vertical gap

    this.setLayout(layout); // 10 tasks
    this.setPreferredSize(new Dimension(400, 560));
    this.setBackground(backgroundColor);
  }

  public void updateNumbers() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof Question) {
        ((Question) listItems[i]).changeIndex(i + 1);
      }
    }
  }


  public void clearAllQuestions() {
    for (Component c : getComponents()) {
      if (c instanceof Question) {
        remove(c); // remove the component
        updateNumbers(); // update the indexing of all items
      }
    }
  }

  /**
   * Loads tasks from a file called "tasks.txt"
   * @return an ArrayList of Task
   */
  public ArrayList<Question> loadQuestions() {
    File file = new File("Questions.txt");
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        ArrayList<Question> result = new ArrayList<Question>();  
        while ((line = br.readLine()) != null) {
          Question question = new Question();
          question.questionName.setText(line);
          result.add(question);
          this.add(question);
        }
        br.close();
        this.updateNumbers(); // Updates the numbers of the tasks
        revalidate();
        return result;
    } catch (IOException e) {
        e.printStackTrace();
    }

    return null;
  }


  /**
   * Saves tasks to a file called "tasks.txt"
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

    }
    
  }
}

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
    recordButton.setHorizontalAlignment(SwingConstants.CENTER);
    recordButton.setFont(new Font("Sans-serif", Font.BOLD, 18));
    recordButton.setBackground(red);
    recordButton.setForeground(foregroundColor);
    recordButton.setBorder(new EmptyBorder(10, 20, 10, 20)); //set margins
    recordButton.setFocusPainted(false); // disable text highlight
    UIManager.put("Button.select", darkRed);
    this.add(recordButton);
  }

  public JButton getRecordButton() {
    return recordButton;
  }
}

class Header extends JPanel {

  JButton clearAllButton;
  
  Color foregroundColor = new Color(200, 200, 200);
  Color backgroundColor = new Color(50, 50, 50);

  JButton clearAllButton;

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

  public JButton getClearAllButton() {
    return clearAllButton;
  }
}

class AppFrame extends JFrame {
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);

  private Header header;
  private Footer footer;
  private List list;

  private JButton clearAllButton;
  private JButton recordButton;
  private JButton clearAllButton;

  AppFrame() {


    this.setSize(400, 600); // 400 width and 600 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    header = new Header();
    footer = new Footer();
    list = new List();

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    this.add(list, BorderLayout.CENTER); // Add list in middle of footer and title

    recordButton = footer.getRecordButton();
    clearAllButton = header.getClearAllButton();
<<<<<<< HEAD

=======
>>>>>>> variant

    addListeners();
    revalidate();
  }

  public void addListeners() {

    recordButton.addActionListener (
        (ActionEvent e) -> {
            Question question = new Question();
            question.questionName.setText("How did dinosaurs get here?");
            list.add(question);
            list.updateNumbers();

            JButton trashCanButton = question.getTrashCan();
            trashCanButton.addMouseListener(
              new MouseAdapter() {
                @override
                public void mousePressed(MouseEvent e) {
                  list.remove(question);
                  list.updateNumbers(); // Updates the numbers of the tasks
                  revalidate(); // Updates the frame
                }
              }
            );

        }
    );

    clearAllButton.addMouseListener(
      new MouseAdapter(){
        @override
        public void mousePressed(MouseEvent e) {
<<<<<<< HEAD
          list.removeCompletedTasks();
        }
      }
    );



=======
          list.clearAllQuestions();
        }
      }
    );
>>>>>>> variant
  }
}

public class GUI {

  public static void main(String args[]) {
    new AppFrame(); // Create the frame
  }
}

@interface override {
}
