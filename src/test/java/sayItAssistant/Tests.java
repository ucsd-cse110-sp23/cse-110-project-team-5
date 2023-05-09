package src.test.java.sayItAssistant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.awt.Component;

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


}
