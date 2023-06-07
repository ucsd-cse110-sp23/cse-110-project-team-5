import org.bson.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

// import java.util.Date;
  
// import java.util.List;
import java.util.ArrayList;
// Following imports are necessary for JUNIT
import static org.junit.Assert.assertEquals;
  
// Following imports are necessary for MongoDB
// import java.net.UnknownHostException;
// import com.mongodb.BasicDBObject;
// import com.mongodb.DB;
// import com.mongodb.DBCollection;
// import com.mongodb.DBCursor;
// import com.mongodb.DBObject;
// import com.mongodb.MongoClient;
// import com.mongodb.MongoException;
// import org.bson.Document;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import org.json.JSONObject;
import org.json.JSONArray;

public class MS2Tests {

  @Test
  void TestCheckAccountDetails() {
    String email = "someemail@gmail.com";
    String password = "12345";
    String vPassword = "4321";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertFalse(result);
  }

  @Test
  void TestNullAccountDetails() {
    String email = null;
    String password = "1234";
    String vPassword = "1234";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertFalse(result);
  }
  
  @Test
    public void TestGetSelected() {

      List promptHistory = new List();

      Prompt chosen = new Prompt();
      chosen.label.setText("selected prompt");
      chosen.selected = true;
      promptHistory.add(chosen);

      for (int i = 0; i < 4; i++) {
        Prompt p = new Prompt();
        promptHistory.add(p);
      }

      assertEquals("selected prompt", promptHistory.findSelected().label.getText());

    }


  @Test
  void US1Scenario1() {
    String email = "someemail@gmail.com";
    String password = "1234";
    String vPassword = "1234";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertTrue(result);
  }

  // User logs in correctly 
  @Test
  void US2Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "email@gmail.com";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    
    assertEquals(createdAccount, ch.getAccount(email, password));
  }

  // User enters incorrect information
  @Test
  void US2Scenario2() {
    CommandHandler ch = new CommandHandler();

    String email = "email@gmail.com";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    
    assertEquals("No matching documents found.", ch.getAccount(email, "4321"));
    assertNotEquals(createdAccount, ch.getAccount(email, "4321"));
  }

  // User does not have existing Account
  @Test
  void US2Scenario3() {
    CommandHandler ch = new CommandHandler();

    String email = "email@gmail.com";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    
    assertEquals("No matching documents found.", ch.getAccount("Never Before Seen Email", "4321"));
  }

  // User wants auto login
  @Test 
  void US3Scenario1() {

    String email = "email@gmail.com";
    String password = "1234";

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/sayItAssistant/LoginInfo.txt"))) {
      writer.write(email + "\n");
      writer.write(password + "\n");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String result = LoginPage.autoLoad();
    assertEquals(email + " " + password, result);
  }

  // User does not want AutoLogin
  @Test 
  void US3Scenario2() {

    try (FileOutputStream fos = new FileOutputStream("src/main/java/sayItAssistant/LoginInfo.txt")) {
      // Clear the file by writing an empty byte array
      fos.write(new byte[0]);
      System.out.println("File contents cleared successfully.");
    } catch (IOException ex) {
      System.out.println("An error occurred while clearing the file: " + ex.getMessage());
    }
    

    String result = LoginPage.autoLoad();
    assertEquals(null, result);
  }


  // User has no search history
  @Test
  void US4Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "email@gmail.com";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    
    assertEquals(createdAccount, ch.getAccount(email, password));
  }

  // User has search history
  @Test
  void US4Scenario2() {
    CommandHandler ch = new CommandHandler();

    String email = "US4Scenario2";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    ch.wrongCommand(email, "Prompt 0");
    String result = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    // Iterate through the prompts array
    for (int i = 0; i < promptsArray.length(); i++) {
      JSONObject prompt = promptsArray.getJSONObject(i);

      // Access question and answer values in each prompt
      String question = prompt.getString("question");
      assertEquals("Prompt 0", question);
    }
  }

  // User correctly speaks the “question” command
  @Test
  void US5Senario1() throws IOException, InterruptedException{
    String transcription = MockWhisper.getQuestion();
    
    CommandHandler ch = new CommandHandler();
    ch.email = "MockEmail";
    
    String result = ch.HandlePrompt(transcription);

    assertEquals(result, "No matching documents found.");
    
    String email = "US6Scenario1";
    String password = "1234";

    ch.createAccount(email, password);

    String expected = ch.question(email, transcription);
    JSONObject jsonObject2 = new JSONObject(expected);
    // Access the "prompts" array
    JSONArray promptsArray2 = jsonObject2.getJSONArray("prompts");

    String result2 = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result2);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    assertEquals(promptsArray2.getJSONObject(0).toString(), promptsArray.getJSONObject(promptsArray.length()-1).toString());
  }

  // User wrongly speaks the “question” command
  @Test
  void US5Senario2() throws IOException{
    String transcription = MockWhisper.getFalseQuestion();
    
    CommandHandler ch = new CommandHandler();
    ch.email = "MockEmail";
    
    String result = ch.HandlePrompt(transcription);

    assertEquals(result, "wrongCommand");

  }

  // User correctly deletes prompt
  @Test
  void US6Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "US6Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    try {
      ch.wrongCommand(email, "Invalid Command");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Now delete this prompt
    ch.deletePrompt(email, "Invalid Command");
    
    String result = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    assertEquals(0, promptsArray.length());
  }

  // User incorrectly deletes prompt
  @Test
  void US6Scenario2() {
    CommandHandler ch = new CommandHandler();

    String email = "US6Scenario2";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    ch.setEmail(email);
    try {
      ch.wrongCommand(email, "Invalid Command");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // incorrectly tries to erase Prompt
    ch.HandlePrompt("erase prompt");
    
    String result = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    assertEquals(2, promptsArray.length());

    ch.deletePrompt(email, "Invalid Command");
    ch.deletePrompt(email, "erase prompt");
  }
  
  // User correctly deletes prompt
  @Test
  void US6Scenario3() {
    CommandHandler ch = new CommandHandler();

    String email = "US6Scenario3";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);

    // Now delete this prompt
    ch.deletePrompt(email, "Invalid Command");
    
    String result = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    assertEquals(0, promptsArray.length());
  }

  // User correctly clears all prompts
  @Test
  void US7Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "US7Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    ch.setEmail(email);
    try {
      ch.wrongCommand(email, "Invalid Command");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Now delete this prompt
    String result = ch.HandlePrompt("Clear All");
    String result2 = ch.getAccount(email, password);
  
    JSONObject jsonObject = new JSONObject(result2);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    //expected 0 but actual 1
    assertEquals(0, promptsArray.length());
  }

    // User incorrectly clears all prompts
  @Test
  void US7Scenario2() {
    CommandHandler ch = new CommandHandler();

    String email = "US7Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    ch.setEmail(email);
    try {
      ch.wrongCommand(email, "Invalid Command");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Now delete this prompt
    ch.HandlePrompt("Erase All Prompts");
    
    String result = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    assertEquals(2, promptsArray.length());
    ch.HandlePrompt("Clear All");
  }
  
  // User wrongly speaks the “setup email” command
  @Test
  void US8Senario1() throws IOException{
    String transcription = "";
    
    CommandHandler ch = new CommandHandler();
    ch.email = "MockEmail";
    
    String result = ch.HandlePrompt(transcription);

    assertEquals(result, "wrongCommand");
  }

  // User correctly speaks the “question” command
  @Test 
  void US8Senario2() throws IOException, InterruptedException{
    boolean result = EmailSetupPage.uploadEmailDetails("first", "last", "display", "smtp", "tls", "email", "password");
    assertTrue(result);

    boolean result2 = EmailSetupPage.uploadEmailDetails("first", null, "display", "smtp", "tls", "email", "password");
    assertTrue(!result2);
  }

  // User correctly speaks the "Create Email" command
  @Test
  void US9Senario1() throws IOException, InterruptedException{
    String transcription = MockWhisper.getQuestion();
    
    CommandHandler ch = new CommandHandler();
    ch.email = "wrongEmail";
    
    String result = ch.HandlePrompt(transcription);

    assertEquals(result, "No matching documents found.");
    
    String email = "US6Scenario1";
    String password = "1234";

    ch.createAccount(email, password);

    String expected = ch.createEmail(email, transcription);
    JSONObject jsonObject2 = new JSONObject(expected);
    // Access the "prompts" array
    JSONArray promptsArray2 = jsonObject2.getJSONArray("prompts");

    String result2 = ch.getAccount(email, password);
    JSONObject jsonObject = new JSONObject(result2);
    // Access the "prompts" array
    JSONArray promptsArray = jsonObject.getJSONArray("prompts");
    
    assertEquals(promptsArray2.getJSONObject(0).toString(), promptsArray.getJSONObject(promptsArray.length()-1).toString());
  }
  
  // User wrongly speaks the “Create Email” command
  @Test
  void US9Senario2() throws IOException{
    String transcription = MockWhisper.getFalseQuestion();
    
    CommandHandler ch = new CommandHandler();
    ch.email = "MockEmail";
    
    String result = ch.HandlePrompt(transcription);

    assertEquals(result, "wrongCommand");

  }

  // User correctly sets displayName
  @Test
  void US10Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "US10Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    EmailSetupPage.emailOfUser = email;
    EmailSetupPage.uploadEmailDetails("firstName", "lastName", "displayName", "smtp", "tls", "emailEmail", "emailPassword");

    String result = ch.getAccount(email, password);
  
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    String displayName = jsonObject.getString("displayName");
    
    //expected 0 but actual 1
    assertEquals("displayName", displayName);
  }

  // User correctly sets displayName
  @Test
  void US10Scenario2() {
    CommandHandler ch = new CommandHandler();

    String email = "US10Scenario2";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);

    String result = ch.getAccount(email, password);
  
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    String displayName = jsonObject.getString("displayName");
    
    //expected 0 but actual 1
    assertEquals("", displayName);
  }

  // User has incorrect email Set up
  @Test
  void US11Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "US11Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    EmailSetupPage.emailOfUser = email;
    Boolean result = EmailSetupPage.uploadEmailDetails("firstName", "lastName", "displayName", null, "tls", "emailEmail", "emailPassword");

    assertFalse(result);
  }

  // User has incorrect email Set up
  @Test
  void US12Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "US11Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    EmailSetupPage.emailOfUser = email;

    TLSEmail tlsEmail = new TLSEmail();
    tlsEmail.setEmailConfig("incorrectEmail", "invalidEmail", "displayName", "password", "smtpHost", "tlsPort", "subject", "body");
    String result = tlsEmail.send();
    
    assertEquals("Email error \n" + "SMTP Host: " + "smtpHost" + "\n", result);
  }


  // User correctly saves emailSetup
  @Test
  void US13Scenario1() {
    CommandHandler ch = new CommandHandler();

    String email = "US13Scenario1";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);
    EmailSetupPage.emailOfUser = email;
    EmailSetupPage.uploadEmailDetails("firstName", "lastName", "displayName", "smtp", "tls", "emailEmail", "emailPassword");

    String result = ch.getAccount(email, password);
  
    JSONObject jsonObject = new JSONObject(result);
    // Access the "prompts" array
    String firstName = jsonObject.getString("firstName");
    String lastName = jsonObject.getString("lastName");
    String displayName = jsonObject.getString("displayName");
    String smtp = jsonObject.getString("smtp");
    String tls = jsonObject.getString("tls");
    String emailEmail = jsonObject.getString("emailEmail");
    String emailPassword = jsonObject.getString("emailPassword");
    
    //expected 0 but actual 1
    assertEquals("firstName", firstName);
    assertEquals("lastName", lastName);
    assertEquals("displayName", displayName);
    assertEquals("smtp", smtp);
    assertEquals("tls", tls);
    assertEquals("emailEmail", emailEmail);
    assertEquals("emailPassword", emailPassword);
  }

  // User does not have EmailSetup
  @Test
  void US13Scenario2() {
    CommandHandler ch = new CommandHandler();

    String email = "US13Scenario2";
    String password = "1234";

    String createdAccount = ch.createAccount(email, password);

    String result = ch.getAccount(email, password);
  
    JSONObject jsonObject = new JSONObject(result);
    
    String firstName = jsonObject.getString("firstName");
    String lastName = jsonObject.getString("lastName");
    String displayName = jsonObject.getString("displayName");
    String smtp = jsonObject.getString("smtp");
    String tls = jsonObject.getString("tls");
    String emailEmail = jsonObject.getString("emailEmail");
    String emailPassword = jsonObject.getString("emailPassword");

    assertEquals("", firstName);
    assertEquals("", lastName);
    assertEquals("", displayName);
    assertEquals("", smtp);
    assertEquals("", tls);
    assertEquals("", emailEmail);
    assertEquals("", emailPassword);
  }
  
}


