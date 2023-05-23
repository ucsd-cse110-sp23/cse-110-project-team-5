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

/*
 * This class extends JPanel and serves as the list container
 * for all questions and answers that the app has been asked
 * 
 * contains: updateNumbers, clearAllQuestions, removeSingle, 
 * saveQuestions, and loadQuestions
 */
class List extends JPanel {

  public final String URL = "http://localhost:8100/";

  public int numPrompts = 0;
  AbstractPrompt selectedPrompt = null;

  Color backgroundColor = new Color(50, 50, 50);  // Dark gray

  List() {
    GridLayout layout = new GridLayout(0, 1);
    layout.setVgap(5); // Vertical gap
    this.setLayout(layout); // 10 questions
    this.setBackground(backgroundColor);
    this.setPreferredSize(new Dimension(400, 105 * this.numPrompts));
    this.selectedPrompt = this.findSelected();
  }

  public AbstractPrompt findSelected() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof AbstractPrompt) {
        if (((AbstractPrompt) listItems[i]).isSelected()) {
          // System.out.println("found selected");
          this.selectedPrompt = ((AbstractPrompt) listItems[i]);
          return ((AbstractPrompt) listItems[i]);
        }
      }
    }
    return null;
  }

  /**
   * Loads questions from a file called "questions.txt"
   * @return an ArrayList of question
   */
  // public void loadQuestions(){
  //   try {
  //     URL url = new URL(URL);
  //     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
  //     conn.setRequestMethod("PUT");
  //     BufferedReader inLength = new BufferedReader(new InputStreamReader(conn.getInputStream()));
  //     String length = inLength.readLine();
  //     inLength.close();
  //     int leng = Integer.parseInt(length);
  //     for (int i = 0; i < leng; i++){
  //       System.out.println("Loop " + i);
  //       String query = Integer.toString(i);
  //       URL url2 = new URL(URL + "?=" + query);
  //       HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
  //       conn2.setRequestMethod("GET");
  //       BufferedReader in = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
  //       String temp = "";
  //       String response = in.readLine();
  //       while ((temp = in.readLine()) != null){
  //         response = response + temp;
  //       }
  //       System.out.println(response);
  //       String[] parts = response.split("~`~");
  //       Question q = this.createQuestion(parts[0]);
  //       q.setContent(parts[1]);
  //       in.close();
  //     }
  //     this.updateNumbers(); // Updates the numbers of the questions
  //     this.numPrompts = leng;
  //     this.setPreferredSize(new Dimension(400, 105 * this.numPrompts));
  //     repaint();
  //     revalidate();
  //   } catch (Exception ex) {
  //     ex.printStackTrace();
  //   }
  // }


  /**
   * Saves questions to a file called "Questions.txt"
   */
  // public void saveQuestions() {
  //   System.out.println("Saving");
  //   try{
  //     URL url = new URL(URL);
  //     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
  //     conn.setRequestMethod("DELETE");
  //     BufferedReader inLength = new BufferedReader(new InputStreamReader(conn.getInputStream()));
  //     inLength.close();
  //   } catch (Exception ex){
  //     ex.printStackTrace();
  //   }

  //   Component[] listItems = this.getComponents();
  //   for (int i = 0; i < listItems.length; i++) {
  //     if (listItems[i] instanceof Prompt) {
  //       try{
  //         URL url2 = new URL(URL);
  //         HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
  //         conn2.setRequestMethod("POST");
  //         conn2.setDoOutput(true);
  //         OutputStreamWriter out = new OutputStreamWriter(conn2.getOutputStream());
  //         System.out.println(((Prompt)listItems[i]).label.getText() + "~`~" + ((Prompt)listItems[i]).getContent());
  //         out.write(((Prompt)listItems[i]).label.getText() + "~`~" + ((Prompt)listItems[i]).getContent().trim());
  //         out.flush();
  //         out.close();
  //         BufferedReader in = new BufferedReader(
  //           new InputStreamReader(conn2.getInputStream())
  //         );
  //         in.close();
  //       } catch (Exception eeex) {
  //         eeex.printStackTrace();
  //       }
  //     }
  //   }
  // }

  /*
   * Creates question with the label of given string transcription
   */
  // public Prompt createQuestion(String transcription) {
  //   Prompt question = new Prompt();
  //   question.label.setText(transcription);
  //   JButton trashCanButton = question.getDeleteButton();
  //           trashCanButton.addMouseListener(
  //             new MouseAdapter() {
  //               @override
  //               public void mousePressed(MouseEvent e) {
  //                 removeSingle(question);
  //               }
  //             }
  //           );
  //   JButton expandButton = question.getExpand();
  //   expandButton.addMouseListener(
  //     new MouseAdapter() {
  //       @override
  //       public void mousePressed(MouseEvent e) {
  //         AppFrame.label.setText(question.getContent());
  //       }
  //     }
  //   );
  //   add(question);
  //   this.numPrompts += 1;
  //   this.setPreferredSize(new Dimension(400, 105 * this.numPrompts));
  //   repaint();
  //   revalidate();
  //   updateNumbers();
  //   return question;
  // }

  public void deselectAll() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof AbstractPrompt) {
        ((AbstractPrompt) listItems[i]).selected = false;
        ((AbstractPrompt) listItems[i]).setBackground(new Color(100, 100, 100));
        ((AbstractPrompt) listItems[i]).repaint();
        ((AbstractPrompt) listItems[i]).revalidate();
      }
    }
  }

  public void addPrompt(AbstractPrompt p) {
    this.add(p);

    // Add selected listener to question
    p.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          deselectAll();
          p.selected = true;
          p.setBackground(new Color(188, 226, 158));
          AppFrame.content.setText(p.getContent());
          repaint();
          revalidate();
        }
      }
    );
    numPrompts += 1;
    setPreferredSize(new Dimension(400, 105 * this.numPrompts));
    repaint();
    revalidate();
    updateNumbers();
  }

  public int deleteSelected() {
    findSelected();
    if (selectedPrompt == null) {
      System.out.println("selectPrompt is null");
      return -1;
    }
    int index = this.selectedPrompt.getIndex();
    this.remove(selectedPrompt);
    repaint();
    revalidate();
    this.selectedPrompt = null;
    this.updateNumbers();
    return index;
  }

  /**
   * Update the question numbers on the list
   */
  public void updateNumbers() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof AbstractPrompt) {
        ((AbstractPrompt) listItems[i]).changeIndex(i + 1);
      }
    }
  }

  /**
   * Clears all questions from app and database
   */
  public void clearAll() {
    for (Component c : getComponents()) {
        remove(c); // remove the question
        updateNumbers(); // update the indexing of all items
        revalidate();
    }
    this.setPreferredSize(new Dimension(400, 1000));
    this.numPrompts = 0;
    // this.saveQuestions();
  }

  // /**
  //  * Removes one question from the App frame
  //  */
  // public void removeSingle(Component x){
  //   remove(x);
  //   updateNumbers();
  //   this.numPrompts -= 1;
  //   this.setPreferredSize(new Dimension(400, 105 * this.numPrompts));
  //   repaint();
  //   revalidate();
  //   // this.saveQuestions();
  // }
}
