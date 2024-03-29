// package src.main.java.sayItAssistant;
// import java.util.ArrayList;
// import org.json.simple.JSONArray;
// import org.json.simple.JSONObject;
// import org.json.simple.parser.ParseException;
// import javax.swing.border.Border;
// import java.io.File;
// import javax.swing.border.EmptyBorder;
// import javax.imageio.ImageIO;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
// import org.json.simple.JSONArray;
// import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.*;
import org.json.JSONObject;
import org.json.JSONArray;
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
  Prompt selectedPrompt = null;
  PromptFactory pf = new PromptFactory();
  JSONParser jp = new JSONParser();

  Color backgroundColor = new Color(50, 50, 50);  // Dark gray
  Color green = new Color(188, 226, 158);;
  Color darkGray = new Color(100, 100, 100);

  List() {
    GridLayout layout = new GridLayout(0, 1);
    layout.setVgap(5); // Vertical gap
    this.setLayout(layout); // 10 questions
    this.setBackground(backgroundColor);
    this.setPreferredSize(new Dimension(400, 105 * this.numPrompts));
    this.selectedPrompt = this.findSelected();
  }

  void update(String jsonString) {
    //clear everything first
    this.clearAll();
    // Read the json file
    JSONObject jsonObject = new JSONObject(jsonString);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");

    if (promptsArray.length() == 0) {
      AppFrame.content.setText("");
    }
    // Iterate through the prompts array
    for (int i = 0; i < promptsArray.length(); i++) {
        JSONObject prompt = promptsArray.getJSONObject(i);

        // Access question and answer values in each prompt
        String question = prompt.getString("question");
        String answer = prompt.getString("answer");
        System.out.println(question);
        System.out.println(answer);

        // Create the JPanels
        Prompt p = pf.createPrompt(question, answer);
        addPrompt(p);

        if (i == promptsArray.length() - 1) {
          if(p.isInvalidCommand()) {
            AppFrame.content.setText(p.getContent());
          }
          else AppFrame.content.setText(p.getLabel() + "\n\n" + p.getContent());
          selectPrompt(p);
        }
    }

    numPrompts += 0;
    setPreferredSize(new Dimension(400, 105 * this.numPrompts));
    repaint();
    revalidate();
    updateNumbers();
  }

  public void selectPrompt(Prompt p) {
    this.selectedPrompt = p;
    deselectAll();
    p.selected = true;
    selectedPrompt = p;
    p.label.setBackground(green);
    p.setBackground(green);
    if(p.isInvalidCommand()) {
      AppFrame.content.setText(p.getContent());
    }
    else AppFrame.content.setText(p.getLabel() + "\n\n" + p.getContent());
    p.label.repaint();
    p.label.revalidate();
    repaint();
    revalidate();
    //set up for delete prompt by setting selectedPrompt in commandHandler
    try {
      String encodedValue = URLEncoder.encode(selectedPrompt.getLabel(), "UTF-8");
      String queryString = "PromptLabel=" + encodedValue;
      URL url = new URL(URL + "?" + queryString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("DELETE");
      System.out.println("WE got here: " + URL + "?" + queryString);
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      in.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public Prompt findSelected() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof Prompt) {
        if (((Prompt) listItems[i]).isSelected()) {
          // System.out.println("found selected");
          this.selectedPrompt = ((Prompt) listItems[i]);
          return ((Prompt) listItems[i]);
        }
      }
    }
    return null;
  }

  public void deselectAll() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof Prompt) {
        ((Prompt) listItems[i]).selected = false;
        ((Prompt) listItems[i]).setBackground(darkGray);
        ((Prompt) listItems[i]).label.setBackground(darkGray);
        ((Prompt) listItems[i]).label.repaint();
        ((Prompt) listItems[i]).label.revalidate();
        ((Prompt) listItems[i]).repaint();
        ((Prompt) listItems[i]).revalidate();
      }
    }
  }

  public void addPrompt(Prompt p) {
    this.add(p);
    MouseAdapter ma = new MouseAdapter() {
      @override
      public void mousePressed(MouseEvent e) {
        selectPrompt(p);
      }
    };      
    p.addMouseListener(ma);
    p.label.addMouseListener(ma);    
    numPrompts += 1;
    setPreferredSize(new Dimension(400, 105 * this.numPrompts));
    repaint();
    revalidate();
    updateNumbers();
  }

  public int deleteSelected() {
    //delete from GUI
    findSelected();
    if (selectedPrompt == null) {
      System.out.println("selectedPrompt is null");
      return -1;
    }
    int index = this.selectedPrompt.getIndex();
    this.remove(selectedPrompt);
    repaint();
    revalidate();
    //delete from database

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
      if (listItems[i] instanceof Prompt) {
        ((Prompt) listItems[i]).changeIndex(i + 1);
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
}
