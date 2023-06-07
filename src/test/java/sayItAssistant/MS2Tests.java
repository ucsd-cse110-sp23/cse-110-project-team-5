import org.bson.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

  


}
