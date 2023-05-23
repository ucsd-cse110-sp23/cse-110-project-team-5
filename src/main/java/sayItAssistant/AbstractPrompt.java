import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

abstract class AbstractPrompt extends JPanel {

  JLabel index;
  JTextField label;
  String content;
  boolean selected;

  Color darkGray = new Color(100, 100, 100);
  Color green = new Color(188, 226, 158);

  MouseListener ml;

  public void setup() {
    this.setPreferredSize(new Dimension(400, 20)); // set size of question
    this.setBackground(darkGray); // set background color of question
    this.setLayout(new BorderLayout()); // set layout of question

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index, BorderLayout.WEST); // add index label to question

    label = new JTextField(""); // create question name text field
    label.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    label.setBackground(darkGray); // set background color of text field

    this.add(label, BorderLayout.CENTER);
    revalidate();

    addMouseListener();
  }

  public void addMouseListener() {
    this.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          selected = true;
          setBackground(green);
          repaint();
          revalidate();
        }
      }
    );
  }
  
  public void changeIndex(int num) {
    this.index.setText(num + ""); // num to String
    this.revalidate(); // refresh
  }

  public int getIndex() {
    return Integer.parseInt(this.index.getText());
  }

  // update the content of this prompt
  public void setContent(String text) {
    this.content = text;
  }

  // return the content of this prompt
  public String getContent() {
    return this.content;
  }

  public void setLabel(String transcriptionFromWhisper) {
    this.label.setText(transcriptionFromWhisper);
  }

  public boolean isSelected() {
    return this.selected;
  }

}


class Question extends AbstractPrompt {

  Question() {
    setup(); // sets up with jpanel configurations from AbstractPrompt
  }

}

class CreateEmail extends AbstractPrompt {

  CreateEmail() {
    setup(); // sets up with jpanel configurations from AbstractPrompt
  }

}

class SendEmail extends AbstractPrompt {

  SendEmail() {
    setup(); // sets up with jpanel configurations from AbstractPrompt
  }

}

class WrongPrompt extends AbstractPrompt {

  WrongPrompt() {
    setup(); // sets up with jpanel configurations from AbstractPrompt
  }
  
}