import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
  
// import java.util.List;
import java.util.ArrayList;
// Following imports are necessary for JUNIT
import static org.junit.Assert.assertEquals;
  
// Following imports are necessary for MongoDB
import java.net.UnknownHostException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;


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

  @Test
  void US1Scenario3() {
    String email = null;
    String password = "1234";
    String vPassword = "1234";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertFalse(result);
  }

  @Test
    public void US2Scenario1() throws Exception {
  
        try {
  
            /**** Connect to MongoDB ****/
            // Since 2.10.0, uses MongoClient
            MongoClient mongo = new MongoClient("localhost", 8100);
  
            /**** Get database ****/
            // if database doesn't exists, MongoDB will create it for you
            DB db = mongo.getDB("say_it_assistant");
  
            /**** Get collection / table from 'geeksforgeeks' ****/
            // if collection doesn't exists, MongoDB will create it for you
            DBCollection table = db.getCollection("users");
  
            /**** Insert ****/
            // create a document to store key and value
            BasicDBObject document = new BasicDBObject();
            document.put("email", "Suprith1");
            document.put("password", "123Password");
            document.put("prompts", new ArrayList<String>());
            table.insert(document);
  
            /**** Find and display ****/
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("email", "Suprith1");
              
            DBCursor cursor = table.find(searchQuery);
            // While displaying let us check 
              // with assert statements as well
            while (cursor.hasNext()) {
                DBObject object = cursor.next();
                // Checking whether inserted name is Suprith1
                assertEquals("Suprith1", object.get("email").toString());
                // Checking whether inserted age is 123Password
                assertEquals("123Password", object.get("password").toString());
            }       
        } catch (MongoException e) {
            e.printStackTrace();
        }
  
    }



    @Test
    public void US4Scenario2() throws Exception {
  
        try {
  
            /**** Connect to MongoDB ****/
            // Since 2.10.0, uses MongoClient
            MongoClient mongo = new MongoClient("localhost", 8100);
  
            /**** Get database ****/
            // if database doesn't exists, MongoDB will create it for you
            DB db = mongo.getDB("say_it_assistant");
  
            /**** Get collection / table from 'geeksforgeeks' ****/
            // if collection doesn't exists, MongoDB will create it for you
            DBCollection table = db.getCollection("users");
  
            ArrayList<String> prompts = new ArrayList<>();
            prompts.add("How did the chicken cross the road?");
            /**** Insert ****/
            // create a document to store key and value
            BasicDBObject document = new BasicDBObject();
            document.put("email", "Suprith1");
            document.put("password", "123Password");
            document.put("prompts", prompts);
            table.insert(document);
  
            /**** Find and display ****/
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("email", "Suprith1");
              
            DBCursor cursor = table.find(searchQuery);
            // While displaying let us check 
              // with assert statements as well
            while (cursor.hasNext()) {
                DBObject object = cursor.next();
                // Checking whether inserted name is Suprith1
                assertEquals("Suprith1", object.get("email").toString());
                // Checking whether inserted age is 123Password
                assertEquals("123Password", object.get("password").toString());
                assertEquals("How did the chicken cross the road?", object.get("prompts").toString());
            }       
        } catch (MongoException e) {
            e.printStackTrace();
        }
  
    }


}
