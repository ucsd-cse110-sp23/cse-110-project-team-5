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
import javax.swing.*;
import java.awt.*;

class Task extends JPanel {

  JLabel index;
  JTextField taskName;
  JButton doneButton;

  Color gray = new Color(218, 229, 234);
  Color green = new Color(188, 226, 158);

  private boolean markedDone;

  Task() {
    this.setPreferredSize(new Dimension(400, 20)); // set size of task
    this.setBackground(gray); // set background color of task
    this.setLayout(new BorderLayout()); // set layout of task

    markedDone = false;

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index, BorderLayout.WEST); // add index label to task

    taskName = new JTextField(""); // create task name text field
    taskName.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    taskName.setBackground(gray); // set background color of text field

    this.add(taskName, BorderLayout.CENTER);

    doneButton = new JButton("Done");
    doneButton.setPreferredSize(new Dimension(80, 20));
    doneButton.setBorder(BorderFactory.createEmptyBorder());
    doneButton.setFocusPainted(false);

    this.add(doneButton, BorderLayout.EAST);
  }

  public void changeIndex(int num) {
    this.index.setText(num + ""); // num to String
    this.revalidate(); // refresh
  }

  public JButton getDone() {
    return doneButton;
  }

  public boolean getState() {
    return markedDone;
  }

  public void changeState() {
    if(getState()) {
      this.setBackground(gray);
      taskName.setBackground(gray);
      markedDone = false;
    } else {
      this.setBackground(green);
      taskName.setBackground(green);
      markedDone = true;
    }
    revalidate();
  }
}

class List extends JPanel {

  Color backgroundColor = new Color(50, 50, 50);

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
      if (listItems[i] instanceof Task) {
        ((Task) listItems[i]).changeIndex(i + 1);
      }
    }
  }

  public void removeCompletedTasks() {
    for (Component c : getComponents()) {
      if (c instanceof Task) {
        if (((Task) c).getState()) {
          remove(c); // remove the component
          updateNumbers(); // update the indexing of all items
        }
      }
    }
  }

  /**
   * Loads tasks from a file called "tasks.txt"
   * @return an ArrayList of Task
   */
  public ArrayList<Task> loadTasks() {
    File file = new File("tasks.txt");
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        ArrayList<Task> result = new ArrayList<Task>();  
        while ((line = br.readLine()) != null) {
          Task task = new Task();
          task.taskName.setText(line);
          result.add(task);
          this.add(task);
        }
        br.close();
        this.updateNumbers(); // Updates the numbers of the tasks
        revalidate();
        return result;
    } catch (IOException e) {
        e.printStackTrace();
    }
    // hint 1: use try-catch block
    // hint 2: use BufferedReader and FileReader
    // hint 3: task.taskName.setText(line) sets the text of the task
    System.out.println("loadTasks() not implemented");
    return null;
  }

  // TODO: Complete this method
  /**
   * Saves tasks to a file called "tasks.txt"
   */
  public void saveTasks() {
    // hint 1: use try-catch block
    // hint 2: use FileWriter
    // hint 3 get list of Tasks using this.getComponents()
    try (FileWriter fw = new FileWriter("tasks.txt")) {
      Component[] listItems = this.getComponents();

      for (int i = 0; i < listItems.length; i++) {
        if (listItems[i] instanceof Task) {
          fw.write(((Task) listItems[i]).taskName.getText() + '\n');
        }
      }
      fw.close();
    } catch(IOException e) {

    }
    
    System.out.println("saveTasks() not implemented");
  }
}

class Footer extends JPanel {

  JButton recordButton;

  // TODO: Add a JButton called "loadButton" to load tasks from a file
  // TODO: Add a JButton called "saveButton" to save tasks to a file

  Color foregroundColor = new Color(200, 200, 200);
  Color backgroundColor = new Color(50, 50, 50);
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);
  Border emptyBorder = BorderFactory.createEmptyBorder();

  Footer() {
    this.setPreferredSize(new Dimension(400, 60));
    this.setBackground(backgroundColor);
    // TODO: Set the layout of the footer to a GridLayout with 1 row and 4 columns

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
    JButton clearAllButton = new JButton("<html><p style='text-align:center;'>Clear<br>All</p></html>");
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
}

class AppFrame extends JFrame {
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);

  private Header header;
  private Footer footer;
  private List list;

  private JButton recordButton;

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

    addListeners();
    revalidate();
  }

  public void addListeners() {

    recordButton.addActionListener (
        (ActionEvent e) -> {
            Task currTask = new Task();
            currTask.taskName.setText("How did dinosaurs get here?");
            list.add(currTask);
            list.updateNumbers();
        }
    );

  }
}

public class GUI {

  public static void main(String args[]) {
    new AppFrame(); // Create the frame
  }
}

@interface override {
}
