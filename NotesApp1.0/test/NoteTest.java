package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    private Note javaWork, gymHobbyArchived, summerHoliday, emptyInvalidHobby;
    private Item javaItemOne, javaItemTwo, javaItemThree;
    private Item hobbyItemOne, hobbyItemTwo;
    private Item holidayItemOne, holidayItemTwo, holidayItemThree, holidayItemFour;

    @BeforeEach
    void setUp(){
        //setting up javaWork note with 3 items
        javaWork = new Note("Learning Java", 5, "Work");
        javaItemOne = new Item("Study Objects and Classes");
        javaItemTwo = new Item("Study ArrayLists and Collections", true);
        javaItemThree = new Item("Study Persistence, eXtensible Markup Language and Exception Handling");
        javaWork.addItem(javaItemOne);
        javaWork.addItem(javaItemTwo);
        javaWork.addItem(javaItemThree);

        gymHobbyArchived = new Note("Gym", 4, "Hobby");
        hobbyItemOne = new Item("Swimming 40 lengths of 25m pool", true);
        hobbyItemTwo = new Item("Cycling 30km", true);
        gymHobbyArchived.addItem(hobbyItemOne);
        gymHobbyArchived.addItem(hobbyItemTwo);
        gymHobbyArchived.setNoteArchived(true);

        summerHoliday = new Note("Summer Holiday to France", 1, "Holiday");
        holidayItemOne = new Item("Book flights", false);
        holidayItemTwo = new Item("Book transfers from airport", false);
        holidayItemThree = new Item("Book hotel", false);
        holidayItemFour = new Item("Book transfers to airport", false);
        summerHoliday.addItem(holidayItemOne);
        summerHoliday.addItem(holidayItemTwo);
        summerHoliday.addItem(holidayItemThree);
        summerHoliday.addItem(holidayItemFour);

        emptyInvalidHobby = new Note("No plans", 6, "No Plans" );
    }

    @AfterEach
    void tearDown(){
        javaWork = gymHobbyArchived = summerHoliday = emptyInvalidHobby = null;
        javaItemOne = javaItemTwo = javaItemThree = null;
        hobbyItemOne = hobbyItemTwo = null;
        holidayItemOne = holidayItemTwo = holidayItemThree = holidayItemFour = null;
    }

    @Nested
    class GettersAndSetters {
        @Test
        void noteTitleGetAndSetWorkingCorrectly() {
            //testing 19 chars
            assertEquals("Learning Java", javaWork.getNoteTitle());
            javaWork.setNoteTitle("Studying: Code Java");
            assertEquals("Studying: Code Java", javaWork.getNoteTitle());

            //testing 20 chars
            javaWork.setNoteTitle("Studying: Code Java.");
            assertEquals("Studying: Code Java.", javaWork.getNoteTitle());

            //tests 21 chars - update doesn't take place
            javaWork.setNoteTitle("Studying: Coding Java");
            assertEquals("Studying: Code Java.", javaWork.getNoteTitle());
        }

        @Test
        void isNoteArchivedGetAndSetWorkingCorrectly() {
            //tests active/archive status set by constructor
            assertFalse(javaWork.isNoteArchived());
            assertTrue(gymHobbyArchived.isNoteArchived());
            assertFalse(summerHoliday.isNoteArchived());
            assertFalse(emptyInvalidHobby.isNoteArchived());

            //sets a note to archived and validates it
            emptyInvalidHobby.setNoteArchived(true);
            assertTrue(emptyInvalidHobby.isNoteArchived());

            //sets an archived note to active and validates it
            gymHobbyArchived.setNoteArchived(false);
            assertFalse(gymHobbyArchived.isNoteArchived());
        }

        @Test
        void categoryGetAndSetWorkingCorrectly() {
            assertEquals("Work", javaWork.getNoteCategory());
            javaWork.setNoteCategory("Invalid");
            assertEquals("Work", javaWork.getNoteCategory());
            javaWork.setNoteCategory("Hobby");
            assertEquals("Hobby", javaWork.getNoteCategory());

            assertEquals("Hobby", gymHobbyArchived.getNoteCategory());
            assertEquals("Holiday", summerHoliday.getNoteCategory());
            assertEquals("", emptyInvalidHobby.getNoteCategory());
        }

        @Test
        void priorityGetAndSetWorkingCorrectly() {
            assertEquals(5, javaWork.getNotePriority());
            javaWork.setNotePriority(4);
            assertEquals(4, javaWork.getNotePriority());
            javaWork.setNotePriority(-1);
            assertEquals(1, javaWork.getNotePriority());

            assertEquals(4, gymHobbyArchived.getNotePriority());
            assertEquals(1, summerHoliday.getNotePriority());

            assertEquals(1, emptyInvalidHobby.getNotePriority());
            emptyInvalidHobby.setNotePriority(4);
            assertEquals(4, emptyInvalidHobby.getNotePriority());
        }

        @Test
        void itemsListGetAndSetWorkingCorrectly() {
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.getItems().get(0).equals(javaItemOne));
            assertTrue(javaWork.getItems().get(1).equals(javaItemTwo));
            assertTrue(javaWork.getItems().get(2).equals(javaItemThree));


            ArrayList<Item>  newList = new ArrayList<Item>();
            newList.add(javaItemOne);
            newList.add(hobbyItemOne);

            assertEquals(0, emptyInvalidHobby.numberOfItems());
            emptyInvalidHobby.setItems(newList);
            assertEquals(2, emptyInvalidHobby.numberOfItems());
            assertTrue(emptyInvalidHobby.getItems().get(0).equals(javaItemOne));
            assertTrue(emptyInvalidHobby.getItems().get(1).equals(hobbyItemOne));
        }
    }

    @Nested
    class ArrayListTests {
        @Test
        void numberOfItemsCalculatedCorrectly() {
            assertEquals(3, javaWork.numberOfItems());
            assertEquals(2, gymHobbyArchived.numberOfItems());
            assertEquals(4, summerHoliday.numberOfItems());
            assertEquals(0, emptyInvalidHobby.numberOfItems());
        }

        @Test
        void allCompleteItemsOnNoteReturnsTrueForCompletionStatus() {
            assertEquals(2, gymHobbyArchived.numberOfItems());
            assertEquals(true, gymHobbyArchived.checkNoteCompletionStatus());
        }

        @Test
        void incompleteItemsOnNoteReturnsFalseForCompletionStatus() {
            assertEquals(3, javaWork.numberOfItems());
            assertEquals(false, javaWork.checkNoteCompletionStatus());
            assertEquals(0, emptyInvalidHobby.numberOfItems());
            assertEquals(true, emptyInvalidHobby.checkNoteCompletionStatus());
        }

        @Test
        void addItemIncreasesSizeOfArrayList(){
            //adding items when items already exist
            assertEquals(3, javaWork.numberOfItems());
            Item javaItemBoilerPlate = new Item ("Boilerplate Coding");
            javaWork.addItem(javaItemBoilerPlate);
            assertEquals(4, javaWork.numberOfItems());
            assertEquals(javaItemBoilerPlate, javaWork.findItem(3));

            //adding items when no items already exist
            assertEquals(0, emptyInvalidHobby.numberOfItems());
            Item javaItemScanner = new Item ("Scanner I/O");
            emptyInvalidHobby.addItem(javaItemScanner);
            assertEquals(1, emptyInvalidHobby.numberOfItems());
            assertEquals(javaItemScanner, emptyInvalidHobby.findItem(0));
        }

        @Test
        void updateItemChangesArrayListContents(){
            //valid update
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.getItems().get(0).equals(javaItemOne));
            javaWork.updateItem(0,hobbyItemOne.getItemDescription(), hobbyItemOne.isItemCompleted());
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.getItems().get(0).equals(hobbyItemOne));

            //invalid index update - make sure nothing changes!

            assertEquals(3, javaWork.numberOfItems());
            javaWork.updateItem(-1,hobbyItemOne.getItemDescription(), hobbyItemOne.isItemCompleted());
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.getItems().get(0).equals(hobbyItemOne));
            assertTrue(javaWork.getItems().get(1).equals(javaItemTwo));
            assertTrue(javaWork.getItems().get(2).equals(javaItemThree));

            assertEquals(3, javaWork.numberOfItems());
            javaWork.updateItem(-3,hobbyItemOne.getItemDescription(), hobbyItemOne.isItemCompleted());
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.getItems().get(0).equals(hobbyItemOne));
            assertTrue(javaWork.getItems().get(1).equals(javaItemTwo));
            assertTrue(javaWork.getItems().get(2).equals(javaItemThree));
        }

        @Test
        void deleteItemDecreasesSizeOfArrayList(){
            //deleting items when items already exist
            assertEquals(3, javaWork.numberOfItems());
            assertEquals(javaItemOne, javaWork.deleteItem(0));
            assertEquals(2, javaWork.numberOfItems());
            assertEquals(javaItemTwo, javaWork.deleteItem(0));
            assertEquals(1, javaWork.numberOfItems());

            //deleting items when no items exist
            assertEquals(0, emptyInvalidHobby.numberOfItems());
            assertNull(emptyInvalidHobby.deleteItem(0));
        }

        @Test
        void validateIndexValidatesCorrectly(){
            //boundary testing valid indices when items exist
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.isValidIndex(0));
            assertTrue(javaWork.isValidIndex(2));
            assertFalse(javaWork.isValidIndex(-1));
            assertFalse(javaWork.isValidIndex(3));

            //boundary testing valid indices when no items exist
            assertEquals(0, emptyInvalidHobby.numberOfItems());
            assertFalse(emptyInvalidHobby.isValidIndex(0));
            assertFalse(emptyInvalidHobby.isValidIndex(-1));
            assertFalse(emptyInvalidHobby.isValidIndex(1));
        }

        @Test
        void findItemReturnsCorrectItem(){
            assertEquals(3, javaWork.numberOfItems());
            assertTrue(javaWork.findItem(0).equals(javaItemOne));
            assertTrue(javaWork.findItem(1).equals(javaItemTwo));
            assertTrue(javaWork.findItem(2).equals(javaItemThree));
            assertNull(javaWork.findItem(-1));
            assertNull(javaWork.findItem(3));
        }

        @Test
        void listItemsReturnsCorrectDetails(){
            //checking item fields are listed
            assertTrue(javaWork.listItems().contains("TODO"));
            assertTrue(javaWork.listItems().contains("Study Objects and Classes"));
            assertTrue(javaWork.listItems().contains("Study ArrayLists and Collections"));
            assertTrue(javaWork.listItems().contains("Complete"));
            //checking "" returned if no items
            assertTrue(emptyInvalidHobby.listItems().contains("No items added"));

        }
    }

    @Nested
    class BoilerPlateTests {
        @Test
        void validatingTheEqualsMethod() {
            Note javaWork2 = new Note("Learning Java", 5, "Work");
            javaWork2.addItem(javaItemOne);
            javaWork2.addItem(javaItemTwo);
            javaWork2.addItem(javaItemThree);
            assertNotSame(javaWork, javaWork2);  //checking they are not the same identify i.e. memory location
            assertEquals(javaWork, javaWork2);  //checking they have the same object state i.e. variable contents.
        }

        @Test
        void toStringContainsAllFieldsInObject() {
            //checking a Note that is archived
            assertTrue(gymHobbyArchived.toString().contains("Gym"));
            assertTrue(gymHobbyArchived.toString().contains("Swimming 40 lengths of 25m pool"));
            assertTrue(gymHobbyArchived.toString().contains("Cycling 30km"));
            assertTrue(gymHobbyArchived.toString().contains("Archived=Y"));

            //checking a Note that is active
            assertTrue(summerHoliday.toString().contains("Summer Holiday to Fr"));
            assertTrue(summerHoliday.toString().contains("Book flights"));
            assertTrue(summerHoliday.toString().contains("Book transfers from airport"));
            assertTrue(summerHoliday.toString().contains("Book hotel"));
            assertTrue(summerHoliday.toString().contains("Book transfers to airport"));
            assertTrue(summerHoliday.toString().contains("Archived=N"));
        }

    }

}
