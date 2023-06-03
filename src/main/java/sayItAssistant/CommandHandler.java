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


class CommandHandler {
  List lst;
  PromptFactory pf;
  AppFrame app;
  //Database db;

  CommandHandler(AppFrame appframe, PromptFactory pf /*, Database db */) {
    this.lst = appframe.list;
    this.pf = pf;
    this.app = appframe;
    //this.db = db;
    app.registerCommHandler(this);
  }

  void HandlePrompt(String transcriptionFromWhisper) {
    transcriptionFromWhisper = transcriptionFromWhisper.trim();
    System.out.println(transcriptionFromWhisper);
    
    if (transcriptionFromWhisper.toLowerCase().indexOf("question") == 0) {
      this.question(transcriptionFromWhisper);
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("delete prompt") == 0) {
      this.deletePrompt();
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("clear all") == 0) {
      this.clearAll();
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("create email") == 0) {
      this.createEmail();
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("send email") == 0) {
      this.sendEmail();
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("set up email") == 0) {
      this.setUpEmail();
    }
    else {
      this.wrongCommand(transcriptionFromWhisper);
    }
  }

  /* Creates a question using whisper transcription and adds to list */
  void question(String transcriptionFromWhisper) {
    Question question = pf.createQuestion(transcriptionFromWhisper);
    lst.addPrompt(question);
    app.content.setText(question.getContent());
    app.repaint();
    app.revalidate();
    // Add to server
  }

  void deletePrompt() {
    int deletedIndex = lst.deleteSelected();
    System.out.println(deletedIndex);
    app.content.setText("");
    app.repaint();
    app.revalidate();
    // clear from server
    //db.delete(deletedIndex);
  }

  void clearAll() {
    lst.clearAll();
    app.content.setText("");
    app.repaint();
    app.revalidate();
    // clear everything off server
    //db.clearAll();
  }

  void sendEmail() {
    // Check if everything is correct
    // if so send, else, show error
  }
  
  void setUpEmail() {
    // create a new pop up window, handle all that stuff in its class 
  }

  void createEmail() {
    //Email email = pf.createEmail(...);
    //app.list.add(email)
    //app.
  }

  void wrongCommand(String transcription) {
    //InvalidCommand invalidCommand = pf.createInvalidCommand(transcriptionFromWhisper);
    //app.list.add(invalidCommand);
    WrongPrompt wp = pf.createWrongPrompt(transcription);
    lst.addPrompt(wp);
    app.content.setText(wp.getContent());
    app.repaint();
    app.revalidate();
  }


}