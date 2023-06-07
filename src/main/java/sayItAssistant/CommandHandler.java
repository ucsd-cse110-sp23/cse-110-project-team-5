// package src.main.java.sayItAssistant;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.FileWriter;
// import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
// import java.util.ArrayList;
// import javax.swing.border.Border;
// import java.io.File;
// import javax.swing.border.EmptyBorder;
// import javax.imageio.ImageIO;
// import java.awt.*;
// import java.net.*;
// import java.net.http.HttpResponse.ResponseInfo;
// import com.mongodb.client.model.Filters;
// import com.mongodb.client.model.Projections;
// import org.bson.json.JsonWriterSettings;
// import java.util.Random;

import javax.swing.*;
import java.io.IOException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import com.mongodb.client.model.UpdateOptions;
import org.bson.json.JsonWriterSettings;
import org.json.JSONObject;

import java.util.Random;
import org.json.JSONObject;
import org.json.JSONArray;

import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONArray;
import static java.util.Arrays.asList;

/*
 * Handles different voice commands to take appropriate actions and updates database of user
 */
class CommandHandler {
  List lst;
  PromptFactory pf;
  //AppFrame app;
  String email;
  String selectedPrompt;
  //Database db;
  public final String db_uri = "mongodb://xicoreyes513:gtejvn59@ac-yy71iyh-shard-00-00.pr6de6a.mongodb.net:27017,ac-yy71iyh-shard-00-01.pr6de6a.mongodb.net:27017,ac-yy71iyh-shard-00-02.pr6de6a.mongodb.net:27017/?ssl=true&replicaSet=atlas-10r8w7-shard-0&authSource=admin&retryWrites=true&w=majority";

  CommandHandler() {
    
  }

  void setEmail(String e) {
    this.email = e;
  }

  void setSelectedPrompt(String prompt) {
    selectedPrompt = prompt;
    System.out.println("Prompt to delete set to: " + selectedPrompt);
  }

  /*
   * Handles different voice commands
   */
  String HandlePrompt(String transcriptionFromWhisper) {
    String response = "";
    transcriptionFromWhisper = transcriptionFromWhisper.trim();
    System.out.println(transcriptionFromWhisper);
    
    // If the voice command was a question, 
    // it calls a method that gets the chatgpt answer
    if (transcriptionFromWhisper.toLowerCase().indexOf("question") == 0) {
      try {
        response = this.question(this.email, transcriptionFromWhisper);
        return response;
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("delete prompt") == 0) {
      response = this.deletePrompt(this.email, this.selectedPrompt);
      return response;
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("clear all") == 0) {
      response = this.clearAll(this.email, transcriptionFromWhisper);
      return response;
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("create email") == 0) {
      try {
        response = this.createEmail(this.email, transcriptionFromWhisper);
        return response;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("send email") == 0) {
      response = this.sendEmail(transcriptionFromWhisper);
      return response;
    }
    else if (transcriptionFromWhisper.toLowerCase().indexOf("setup email") == 0 || 
                transcriptionFromWhisper.toLowerCase().indexOf("set up email") == 0) {
      response = this.setUpEmail(transcriptionFromWhisper);
      return response;
      
    }
    else {
      response = this.wrongCommand(this.email, transcriptionFromWhisper);
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

      // Create all the account fields
      Document update = new Document("$set", new Document()
      .append("password", password)
      .append("prompts", asList())
      .append("firstName", "")
      .append("lastName", "")
      .append("displayName", "")
      .append("smtp", "")
      .append("tls", "")
      .append("emailEmail", "")
      .append("emailPassword", "")
      );

      // Perform the upsert operation
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
      if (doc != null && doc.getString("password").equals(password)) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
    }

    return response;
  }

  /**
   * Given a voice command with a question, it gets the answer from chatgpt
   * and updates the user's browsing history in the database.
   * @param email
   * @param transcriptionFromWhisper
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
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

  String deletePrompt(String email, String selectedPrompt) {
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
      new Document("question", selectedPrompt)));
      
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

  /**
   * Given a voice command to clear all prompts, it clears all prompts from the database.
   * @param email
   * @param transcriptionFromWhisper
   * @return
   */
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

  /**
   * send email to user 
   * @param transcription
   * @return response
   */
  String sendEmail(String transcription) {
    //usable transcription uses "@" instead of "at"
    String usableTranscription = transcription.replace(" at ", "@");
    if (usableTranscription.endsWith("."))
      usableTranscription = usableTranscription.substring(0, usableTranscription.length() - 1);
    TLSEmail tls = new TLSEmail();
    String response;

    // create a new pop up window, handle all that stuff in its class
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");

      Document doc = users.find(eq("email", email)).first();

      if(selectedPrompt != null && (selectedPrompt.toLowerCase().startsWith("create email"))) {

      //get email set up from database
      String jsonString = doc.toJson().toString();
      JSONObject jsonObject = new JSONObject(jsonString);
      String fromEmail = jsonObject.getString("emailEmail");
      String displayName = jsonObject.getString("displayName");
      String password = jsonObject.getString("emailPassword");
      String smtpHost = jsonObject.getString("smtp");
      String tlsPort = jsonObject.getString("tls");
      String[] words = usableTranscription.split(" ");
      //identify email in transcription using @
      String toEmail = null;
      for(int i = 0; i < words.length; i++) {
        if(words[i].contains("@")) toEmail = words[i];
      }

      // Access created email from the "prompts" array
      JSONArray promptsArray = jsonObject.getJSONArray("prompts");
      JSONObject matchedPrompt = null;
      for (int i = 0; i < promptsArray.length(); i++) {
        JSONObject prompt = promptsArray.getJSONObject(i);
        if(prompt.getString("question").equals(selectedPrompt)) 
             matchedPrompt = prompt;
      }
  
      //get subject and body
      String subject = matchedPrompt.getString("question");
      subject = subject.substring(subject.indexOf(subject.split(" ")[2]) + 1);
      String body = matchedPrompt.getString("answer");
      
      //set email config and send
      tls.setEmailConfig(fromEmail, toEmail, displayName, password, smtpHost, tlsPort,
      subject, body);
      System.out.println(toEmail);
      String status = tls.send();

      // Create the filter
      Document filter = new Document("email", email);

      // Create an update document to push a new prompt into the existing "prompts" field
      Document update = new Document("$push", new Document("prompts",
      new Document("question", usableTranscription)
              .append("answer", status)));

      // Perform the update operation
      users.updateOne(filter, update);

      } else {
        // Create the filter
        Document filter = new Document("email", email);

        // Create an update document to push a new prompt into the existing "prompts" field
        Document update = new Document("$push", new Document("prompts",
        new Document("question", usableTranscription)
                .append("answer", "That doesn't work, you need to select a create email prompt")));

        // Perform the update operation
        users.updateOne(filter, update);
      }

      doc = users.find(eq("email", email)).first();

      response = "";
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
      
      System.out.println(response);
      }
    return response;
  }
  
  /**
   * Set up the email detail for the user
   * @param transcription
   * @return response
   */
  String setUpEmail(String transcription) {
    String response;
    // create a new pop up window, handle all that stuff in its class
    EmailSetupPage page = new EmailSetupPage(this.email);
    page.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");

      // Create the filter
      Document filter = new Document("email", email);


      // Create an update document to push a new prompt into the existing "prompts" field
      Document update = new Document("$push", new Document("prompts",
      new Document("question", transcription)
      .append("answer", "")));

      // Perform the update operation
      users.updateOne(filter, update);


      response = "";
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

    /**
     *  create user email using whisper transcription
     * @param email
     * @param transcriptionFromWhisper
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
  String createEmail(String email, String transcriptionFromWhisper) throws IOException, InterruptedException {
    // upsert this into the database, and pass back the entire user
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");

      Document doc = users.find(eq("email", email)).first();

      String jsonString = doc.toJson().toString();
      JSONObject jsonObject = new JSONObject(jsonString);
      String displayName = jsonObject.getString("displayName");
      String display = "with the signature: \"Best Regards, \n" + displayName + "\"";
      String answer = ChatGPT.getAnswer(transcriptionFromWhisper + display);

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
      doc = users.find(eq("email", email)).first();
      
      // Create response based on matching user email
      if (doc != null) {
          response = doc.toJson();
      } else {
          response = "No matching documents found.";
      }
      
      System.out.println(response);
      return response;
    }
  }

  String wrongCommand(String email, String transcriptionFromWhisper) {
    // upsert this into the database, and pass back the entire user
    try (MongoClient mongoClient = MongoClients.create(db_uri)) {
      MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
      MongoCollection<Document> users = sayItAssistant.getCollection("users");

      // Create the filter
      Document filter = new Document("email", email);

      // Create an update document to push a new prompt into the existing "prompts" field
      Document update = new Document("$push", new Document("prompts",
      new Document("question", transcriptionFromWhisper)
              .append("answer", "Invalid Command: " + "\"" + transcriptionFromWhisper + "\"")));

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


}