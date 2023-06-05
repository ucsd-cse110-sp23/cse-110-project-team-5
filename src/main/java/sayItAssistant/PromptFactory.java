// package src.main.java.sayItAssistant;
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
  public Prompt createPrompt(String question, String answer) {
    //create question
    Prompt p = new Prompt();
    p.setLabel(question);
    p.setContent(answer);
    if(p.getContent().contains("Invalid Command: ")) p.setIsInvalidCommand();
    return p;
  }
}