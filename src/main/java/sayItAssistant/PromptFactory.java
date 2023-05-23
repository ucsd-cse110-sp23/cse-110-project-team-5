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


class PromptFactory {

  /*
   * Creates question with the label of given string transcription
   */
  public Question createQuestion(String transcription) {
    //create question
    Question question = new Question();
    question.setLabel(transcription);

    // Get answer
    try {
      String answer = ChatGPT.getAnswer(transcription);
      question.setContent(answer);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return question;
  }

  /*
   * Handles incorrect prompt command
   */
  public WrongPrompt createWrongPrompt(String transcription) {
    WrongPrompt wrongprompt = new WrongPrompt();
    wrongprompt.setLabel(transcription);

    wrongprompt.setContent("Invalid Command: \n\"" + transcription + "\"");
    return wrongprompt;
  }

  // public createEmailPrompt createEmail(String transcription) {
  //   createEmailPrompt p = new Prompt();
  //   createEmailPrompt.label.setText(transcription);
  //   return createEmailPrompt;
  // }

  // public sendEmailPrompt createSendEmail(String transcription) {
  //   Prompt sendEmailPrompt = new Prompt();
  //   sendEmailPrompt.label.setText(transcription);
  //   return sendEmailPrompt;
  // }

}