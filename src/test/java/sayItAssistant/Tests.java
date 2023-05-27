import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Tests {

    @Test
    void testSum() {
        assertEquals(2, 1+ 1);
    }

    // @Test
    // void testChangeIndex() {
    //     Question question = new Question();
    //     question.changeIndex(2);
    //     assertEquals("2", question.index.getText());
    // }

    // @Test
    // void testUpdateNumbers () {
    //     List l = new List();
    //     for (int i = 5; i >= 0; i--) {
    //         Question q = new Question();
    //         q.changeIndex(i);
    //         l.add(q);
    //     }

    //     Component[] listItems = l.getComponents();
    //     l.updateNumbers();
    //     for (int i = 0; i < listItems.length; i++) {
    //         if (listItems[i] instanceof Question) {
    //             assertEquals(Integer.toString(i + 1), ((Question) listItems[i]).index.getText());
                
    //         }
    //     }
    // }

    // @Test
    // void testClearAllQuestions() {
    //     List l = new List();
    //     for (int i = 5; i >= 0; i--) {
    //         Question q = new Question();
    //         q.changeIndex(i);
    //         l.add(q);
    //     }

    //     l.clearAll();
    //     Component[] listItems = l.getComponents();
    //     assertEquals(0, listItems.length);
    // }

    // @Test
    // void testDeleteSelected() {
    //     List l = new List();
    //     for (int i = 1; i <=5; i++) {
    //         Question q = new Question();
    //         q.changeIndex(i);
    //         l.add(q);
    //     }

    //     Component[] listItems = l.getComponents();
    //     l.selectedPrompt = (Question) listItems[0];
    //     l.deleteSelected();
    //     Component[] listItems2 = l.getComponents();
    //     assertEquals(4, listItems2.length);
    //     // Check revalidate
    //     assertEquals("1", ((Question) listItems2[0]).index.getText());
    // }

    // @Test 
    // void testUserStory3() {
    //     // When the user 
    //     // Given that the user has opened the app
    //     // When the user has no prompt history
    //     // Then the user will see an empty question prompt history section
    //     // Along with the clear all button at the top right and ask a question button at the bottom
    //     List l = new List();
    //     Component[] listItems = l.getComponents();
    //     assertEquals(0, listItems.length);
    // }
    
    // @Test
    // void testUserStory32() {
    //     //User has prompt History
    //     //Given that the user has opened the app
    //     //When the user has prompt history
    //     //Then the user will see their past questions,
    //     //Along with the clear all button at the top right and ask a question button at the bottom
    //     List l = new List();
    //     for (int i = 1; i <=5; i++) {
    //         Question q = new Question();
    //         l.add(q);
    //     }
    //     Component[] listItems = l.getComponents();
    //     assertEquals(5, listItems.length);
    // }

    // /** Given I finish asking the question
    //  * When the question is transcribed
    //  * Then make the transcribed question appear in the question prompt
    //  */
    // @Test
    // void testUserStory7() {
    //     PromptFactory pf = new PromptFactory();

    //     MockWhisper whisper = new MockWhisper();
    //     String mockQuestion = "";
    //     try {
    //         mockQuestion = whisper.getQuestion();
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //     }
    //     List l = new List();
    //     Question q = pf.createQuestion(mockQuestion);
    //     assertEquals(q.label.getText(),mockQuestion);
    // }

    // @Test
    // void testCreateQuestion() {
    //     PromptFactory pf = new PromptFactory();
    //     List l = new List();
    //     l.addPrompt(pf.createQuestion("Question 1"));

    //     Component[] listItems = l.getComponents();
    //     assertEquals(1, listItems.length);
    // }

    // @Test
    // void testNumQuestions () {
    //     PromptFactory pf = new PromptFactory();
    //     List l = new List();
    //     l.addPrompt(pf.createQuestion("Question 1"));
    //     l.addPrompt(pf.createQuestion("Question 2"));
    //     l.addPrompt(pf.createQuestion("Question 3"));

    //     assertEquals(3, l.numPrompts);
    // }

    // @Test
    // void testQuestionTranscription() {
    //     List l = new List();

    //     l.createQuestion("Question 1");

    //     Component[] listItems = l.getComponents();
    //     assertEquals("Question 1", ((Prompt)listItems[0]).label.getText());
    // }

    // @Test
    // void testPreferredSizeSmall() {
    //     List l = new List();

    //     l.createQuestion("Question 1");
    //     assertEquals(new Dimension(400, 105), l.getPreferredSize());
    // }

    // @Test
    // void testPreferredSizeLarge () {
    //     List l = new List();

    //     l.createQuestion("Question 1");
    //     l.createQuestion("Question 2");
    //     l.createQuestion("Question 3");
    //     l.createQuestion("Question 4");
    //     l.createQuestion("Question 5");
    //     l.createQuestion("Question 6");

    //     assertEquals(new Dimension(400, 105 * 6), l.getPreferredSize());
    // }

    // @Test
    // void testUserStory1Scenario1() {
    //     List l = new List();

    //     l.createQuestion("Question 1");
    //     l.createQuestion("Question 2");
    //     l.createQuestion("Question 3");
    //     l.createQuestion("Question 4");
    //     l.createQuestion("Question 5");
    //     l.createQuestion("Question 6");

    //     assertTrue(l.getPreferredSize().getHeight() > 500);
    // }

    // @Test
    // void testUserStory1Scenario3() {
    //     List l = new List();
    //     assertTrue(l.getPreferredSize().getHeight() == 0);
    // }

    // @Test
    // void testUserStory4Scenario2() {
    //     List l = new List();

    //     l.createQuestion("This is some question given to create question");

    //     Component[] listItems = l.getComponents();
    //     // Check if the question name is set properly
    //     assertEquals("This is some question given to create question", ((Question)listItems[0]).label.getText());
    // }

    // @Test
    // void testUserStory4Scenario4 () {
    //     MockWhisper whisper = new MockWhisper();
    //     String mockQuestion = "";
    //     try {
    //         mockQuestion = whisper.getQuestion();
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //     }
    //     List l = new List();
    //     Question q = l.createQuestion(mockQuestion);
    //     Component[] listItems = l.getComponents();
    //     assertEquals(mockQuestion, ((Question)listItems[0]).label.getText());
    // }
}
