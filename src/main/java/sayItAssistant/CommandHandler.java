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
import java.net.http.HttpResponse.ResponseInfo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.model.UpdateOptions;
import org.bson.json.JsonWriterSettings;


import java.util.Random;


import static java.util.Arrays.asList;


class CommandHandler {
  List lst;
  PromptFactory pf;
  AppFrame app;
  String email;
  //Database db;
  public final String db_uri = "mongodb+srv://xicoreyes513:gtejvn59@gettingstarted.pr6de6a.mongodb.net/?retryWrites=true&w=majority";

  CommandHandler() {
    
  }

  void setEmail(String e) {
    this.email = e;
  }

  String HandlePrompt(String transcriptionFromWhisper) {
    String response = "Invalid Command";
    transcriptionFromWhisper = transcriptionFromWhisper.trim();
    System.out.println(transcriptionFromWhisper);
    
    if (transcriptionFromWhisper.toLowerCase().indexOf("question") == 0) {
      try {
        response = this.question(this.email, transcriptionFromWhisper);
        return response;
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("delete prompt") == 0) {
      response = this.deletePrompt(this.email, transcriptionFromWhisper);
      return response;
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("clear all") == 0) {
      response = this.clearAll(this.email, transcriptionFromWhisper);
      return response;
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
      return response;
    }

    return response;
  }

  String createAccount(String email, String password) {
    // System.out.println(password);
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");


      // Create the filter
      Document filter = new Document("email", email);

      // Create the update for password
      Document update = new Document("$set", new Document("password", password));

      // Perform the upsert operation
      users.updateOne(filter, update, new UpdateOptions().upsert(true));

      // Create the update for prompts (empty)
      update = new Document("$set", new Document("prompts", asList()));
      users.updateOne(filter, update, new UpdateOptions().upsert(true));

      String response = "";
      Document doc = users.find(eq("email", email)).first();
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
      
      System.out.println(response);
      return response;
    }
  }

  String getAccount(String email, String password) {
    String response = "No Account Found";
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");

      // Find account and returns its json as a string
      Document doc = users.find(eq("email", email)).first();
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
    }

    return response;
  }

  String question(String email, String transcriptionFromWhisper) throws IOException, InterruptedException {
    String answer = ChatGPT.getAnswer(transcriptionFromWhisper);
    // upsert this into the database, and pass back the entire user
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");

      // Create the filter
      Document filter = new Document("email", email);


      // Create an update document to push a new prompt into the existing "prompts" field
      Document update = new Document("$push", new Document("prompts",
      new Document("question", transcriptionFromWhisper)
              .append("answer", answer)));

      // Perform the update operation
      users.updateOne(filter, update);

      System.out.println("Prompt inserted successfully.");

      String response = "";
      Document doc = users.find(eq("email", email)).first();
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
      
      System.out.println(response);
      return response;
    }
  }

  String deletePrompt(String email, String transcriptionFromWhisper) {
    String response = "Delete Prompt Entered";
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");


      // Create the filter
      Document filter = new Document("email", email);

      // Create the update for password
      // Create an update document to remove a specific prompt from the "prompts" array
      // Create an update document to remove a specific prompt based on the question field
      Document update = new Document("$pull", new Document("prompts",
      new Document("question", transcriptionFromWhisper)));
      
      users.updateOne(filter, update);
      System.out.println("Prompt Deleted Successfully");

      response = "";
      Document doc = users.find(eq("email", email)).first();
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
      
      System.out.println(response);
    }
    return response;
  }

  String clearAll(String email, String transcriptionFromWhisper) {
    String response = "Clear All Was Called";
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");


      // Create the filter
      Document filter = new Document("email", email);

      // Create the update for password
      Document update = new Document("$set", new Document("prompts", asList()));
      users.updateOne(filter, update);

      Document doc = users.find(eq("email", email)).first();
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
      
      System.out.println(response);
    }
    return response;
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
    // WrongPrompt wp = pf.createWrongPrompt(transcription);
    // lst.addPrompt(wp);
    // app.content.setText(wp.getContent());
    // app.repaint();
    // app.revalidate();
  }


}