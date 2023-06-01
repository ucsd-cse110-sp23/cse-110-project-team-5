import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
  
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

public class MS2Tests {
  @Test
  void US1Scenario1() {
    String email = "someemail@gmail.com";
    String password = "1234";
    String vPassword = "1234";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertTrue(result);
  }

  @Test
  void US1Scenario2() {
    String email = "someemail@gmail.com";
    String password = "12345";
    String vPassword = "4321";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertFalse(result);
  }

  @Test
  void US1Scenario3() {
    String email = null;
    String password = "1234";
    String vPassword = "1234";

    boolean result = CreateAccountPage.checkAccountDetails(email, password, vPassword);
    assertFalse(result);
  }

  // @Test
  //   public void testInsertedAndUpdatedDocument() throws Exception {
  
  //       try {
  
  //           /**** Connect to MongoDB ****/
  //           // Since 2.10.0, uses MongoClient
  //           MongoClient mongo = new MongoClient("localhost", 27017);
  
  //           /**** Get database ****/
  //           // if database doesn't exists, MongoDB will create it for you
  //           DB db = mongo.getDB("geeksforgeeks");
  
  //           /**** Get collection / table from 'geeksforgeeks' ****/
  //           // if collection doesn't exists, MongoDB will create it for you
  //           DBCollection table = db.getCollection("authors");
  
  //           /**** Insert ****/
  //           // create a document to store key and value
  //           BasicDBObject document = new BasicDBObject();
  //           document.put("name", "author1");
  //           document.put("age", 30);
  //           document.put("technology", "java,mongodb");
  //           document.put("createdDate", new Date());
  //           table.insert(document);
  
  //           /**** Find and display ****/
  //           BasicDBObject searchQuery = new BasicDBObject();
  //           searchQuery.put("name", "author1");
              
  //           DBCursor cursor = table.find(searchQuery);
  //           // While displaying let us check 
  //             // with assert statements as well
  //           while (cursor.hasNext()) {
  //               DBObject object = cursor.next();
  //               // Checking whether inserted name is author1
  //               assertEquals("author1", object.get("name").toString());
  //               // Checking whether inserted age is 30
  //               assertEquals(30, Integer.parseInt(object.get("age").toString()));
  //               // Checking whether inserted technology is java,mongodb
  //               assertEquals("java,mongodb", object.get("technology").toString());
                  
                  
  //           }
              
  //           /**** Update ****/
  //           // search document where name="author1" 
  //             // and update it with new values
  //           BasicDBObject query = new BasicDBObject();
  //           query.put("name", "author1");
  
  //           BasicDBObject newDocument = new BasicDBObject();
  //           // changing the technology column value
  //           newDocument.put("technology", "java,.net,mongodb");
  //           // additionally adding a new column. 
  //             // This is the advantage of mongodb. 
  //           // We no need to have concrete structure 
  //             // from the beginning as like RDBMS database
  //           newDocument.put("numberofposts", "10");
  
  //           BasicDBObject updateObj = new BasicDBObject();
  //           updateObj.put("$set", newDocument);
              
  //             // This way we can update the document
  //           table.update(query, updateObj);
  
  //           /**** Find and display ****/
  //           BasicDBObject searchQuery2 
  //               = new BasicDBObject().append("name", "author1");
  
  //           DBCursor cursor2 = table.find(searchQuery2);
  //           // Check the same as well
  //           // Now we can check whether the technology
  //             // got changed to java,.net,mongodb
  //           // and also numberofposts to 10
  //           while (cursor2.hasNext()) {
  //               DBObject object = cursor2.next();
  //               assertEquals("author1", object.get("name").toString());
  //               assertEquals(30, Integer.parseInt(object.get("age").toString()));
  //               assertEquals("java,.net,mongodb", object.get("technology").toString());
  //               assertEquals("10", object.get("numberofposts").toString());
  //           }
  
              
  //       } catch (UnknownHostException e) {
  //           e.printStackTrace();
  //       } catch (MongoException e) {
  //           e.printStackTrace();
  //       }
  
  //   }


}
