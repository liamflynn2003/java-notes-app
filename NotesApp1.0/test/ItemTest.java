package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemTest {

    private Item javaItemOne, javaItemTwo, javaItemThree;

    @BeforeEach
    void setUp() {
        javaItemOne = new Item("Study Objects and Classes");
        javaItemTwo = new Item("Study ArrayLists and Collections", true);
        javaItemThree = new Item("Study Persistence, eXtensible Markup Language and Exception Handling");
    }

    @AfterEach
    void tearDown() {
        javaItemOne = javaItemTwo = javaItemThree = null;
    }

    @Test
    void itemDescriptionGetAndSetWorkingCorrectly() {
        //testing 49 chars
        assertEquals("Study Persistence, eXtensible Markup Language and ", javaItemThree.getItemDescription());
        javaItemThree.setItemDescription("Study Persistence, eXtensible Markup Language-XML");
        assertEquals("Study Persistence, eXtensible Markup Language-XML", javaItemThree.getItemDescription());

        //testing 50 chars
        javaItemThree.setItemDescription("Study Persistence, eXtensible Markup Language(XML)");
        assertEquals("Study Persistence, eXtensible Markup Language(XML)", javaItemThree.getItemDescription());

        //tests 51 chars - update should be ignored
        javaItemThree.setItemDescription("Study Persistence, eXtensible Markup Language(XML)");
        assertEquals("Study Persistence, eXtensible Markup Language(XML)", javaItemThree.getItemDescription());

        //tests 51 chars - update should be ignored
        javaItemTwo.setItemDescription("Study Persistence, eXtensible Markup Language (XML)");
        assertEquals("Study ArrayLists and Collections", javaItemTwo.getItemDescription());
    }

    @Test
    void isItemCompletedGetAndSetWorkingCorrectly() {
        assertFalse(javaItemOne.isItemCompleted());
        assertTrue(javaItemTwo.isItemCompleted());
        assertFalse(javaItemThree.isItemCompleted());

        javaItemOne.setItemCompleted(true);
        assertTrue(javaItemOne.isItemCompleted());
        javaItemTwo.setItemCompleted(false);
        assertFalse(javaItemTwo.isItemCompleted());
    }

    @Test
    void validatingTheEqualsMethod() {
        Item itemFour = new Item("Study Objects and Classes");
        assertNotSame(javaItemOne, itemFour);  //checking they are not the same identify i.e. memory location
        assertEquals(javaItemOne, itemFour);  //checking they have the same object state i.e. variable contents.
    }

    @Test
    void toStringContainsAllFieldsInObject() {
        //checking an item that is not complete
        assertTrue(javaItemOne.toString().contains("Study Objects and Classes"));
        assertTrue(javaItemOne.toString().contains("TODO"));

        //checking an item that is complete
        assertTrue(javaItemTwo.toString().contains("Study ArrayLists and Collections"));
        assertTrue(javaItemTwo.toString().contains("Complete"));
    }
}

