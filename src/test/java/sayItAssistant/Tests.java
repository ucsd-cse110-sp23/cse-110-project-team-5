
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.awt.Component;
import java.io.IOException;

public class Tests {

    @Test
    void testChangeIndex() {
        Question question = new Question();
        question.changeIndex(2);
        assertEquals("2", question.index.getText());
    }

    @Test
    void testUpdateNumbers () {
        List l = new List();
        for (int i = 5; i >= 0; i--) {
            Question q = new Question();
            q.changeIndex(i);
            l.add(q);
        }

        Component[] listItems = l.getComponents();
        l.updateNumbers();
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i] instanceof Question) {
                assertEquals(Integer.toString(i + 1), ((Question) listItems[i]).index.getText());
                
            }
        }
    }

    @Test
    void testClearAllQuestions() {
        List l = new List();
        for (int i = 5; i >= 0; i--) {
            Question q = new Question();
            q.changeIndex(i);
            l.add(q);
        }

        l.clearAllQuestions();
        Component[] listItems = l.getComponents();
        assertEquals(0, listItems.length);
    }

    @Test
    void testRemoveSingle() {
        List l = new List();
        for (int i = 1; i <=5; i++) {
            Question q = new Question();
            q.changeIndex(i);
            l.add(q);
        }

        Component[] listItems = l.getComponents();
        l.removeSingle((Question) listItems[0]);
        Component[] listItems2 = l.getComponents();
        assertEquals(4, listItems2.length);
        // Check revalidate
        assertEquals("1", ((Question) listItems2[0]).index.getText());
    }

    @Test 
    void testUserStory3() {
        // When the user 
        // Given that the user has opened the app
        // When the user has no prompt history
        // Then the user will see an empty question prompt history section
        // Along with the clear all button at the top right and ask a question button at the bottom
        List l = new List();
        Component[] listItems = l.getComponents();
        assertEquals(0, listItems.length);
    }
    
    @Test
    void testUserStory32() {
        //User has prompt History
        //Given that the user has opened the app
        //When the user has prompt history
        //Then the user will see their past questions,
        //Along with the clear all button at the top right and ask a question button at the bottom
        List l = new List();
        for (int i = 1; i <=5; i++) {
            Question q = new Question();
            l.add(q);
        }
        Component[] listItems = l.getComponents();
        assertEquals(5, listItems.length);
    }

    /** Given I finish asking the question
     * When the question is transcribed
     * Then make the transcribed question appear in the question prompt
     */
    @Test
    void testUserStory7() {
        MockWhisper whisper = new MockWhisper();
        String mockQuestion = "";
        try {
            mockQuestion = whisper.getQuestion();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        List l = new List();
        Question q = l.createQuestion(mockQuestion);
        assertEquals(q.questionName.getText(),mockQuestion);
    }
    
    //Given the transcripted question is sent as a prompt to ChatGPT
    //When the answer is sent back from ChatGPT
    //Then make the answer appear in the Answer Box
    @Test
    void testUserStory72(){
        MockChatGPT chatgpt = new MockChatGPT();
        String mockTrans = "";
        String mockAnswer = "";
        try {
            mockAnswer = chatgpt.getAnswer(mockTrans);
        }
        catch (IOException e) {
            System.out.println(e);
        }
         List l = new List();
        Question q = l.createQuestion(mockAnswer);
        assertEquals(q.questionName.getText(),"Sorry, I don't know the answer to that question.");
    }
        




}
