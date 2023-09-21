package controllers;

import models.Item;
import models.Note;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoteAPITest {

    private Note javaWork, gymHobbyArchived, summerHoliday, archivedNoteNoItems, emptyInvalidHobby;
    private Note codeApp, testApp, deployApp;
    private Item javaItemOne, javaItemTwo, javaItemThree, codeOne, codeTwo;
    private Item hobbyItemOne, hobbyItemTwo;
    private Item holidayItemOne, holidayItemTwo, holidayItemThree, holidayItemFour;
    private NoteAPI notes = new NoteAPI();
    private NoteAPI emptyNotes = new NoteAPI();
    private NoteAPI archivedNotes = new NoteAPI();


    @BeforeEach
    void setUp(){
        //setting up javaWork note with 3 items, one complete, two todo
        javaWork = new Note("Learning Java", 5, "Work");
        javaItemOne = new Item("Study Objects and Classes");
        javaItemTwo = new Item("Study ArrayLists and Collections", true);
        javaItemThree = new Item("Study Persistence, eXtensible Markup Language and Exception Handling");
        javaWork.addItem(javaItemOne);
        javaWork.addItem(javaItemTwo);
        javaWork.addItem(javaItemThree);

        //setting up an archived note, gymHobbyArchived with two notes
        gymHobbyArchived = new Note("Gym", 4, "Hobby");
        hobbyItemOne = new Item("Swimming 40 lengths of 25m pool", true);
        hobbyItemTwo = new Item("Cycling 30km", true);
        gymHobbyArchived.addItem(hobbyItemOne);
        gymHobbyArchived.addItem(hobbyItemTwo);
        gymHobbyArchived.setNoteArchived(true);

        //setting up a summerHoliday note with four items, each todo
        summerHoliday = new Note("Summer Holiday to France", 1, "Holiday");
        holidayItemOne = new Item("Book flights", false);
        holidayItemTwo = new Item("Book transfers from airport", false);
        holidayItemThree = new Item("Book hotel", false);
        holidayItemFour = new Item("Book transfers to airport", false);
        summerHoliday.addItem(holidayItemOne);
        summerHoliday.addItem(holidayItemTwo);
        summerHoliday.addItem(holidayItemThree);
        summerHoliday.addItem(holidayItemFour);

        //setting up a codeApp note with three items, two complete, one todo
        codeApp = new Note("Code App", 4, "Work");
        codeOne = new Item("Code the UI", true);
        codeTwo = new Item("Code the Backend", true);
        codeApp.addItem(codeOne);
        codeApp.addItem(codeTwo);

        //setting up two notes with no items
        testApp = new Note("Test App", 4, "Work");
        deployApp = new Note("Deploy App", 3, "Work");

        //setting up an archived note with no items
        archivedNoteNoItems = new Note("Archived Note", 1, "Hobby");
        archivedNoteNoItems.setNoteArchived(true);

        //a note that will test the validation at constructor level
        emptyInvalidHobby = new Note("No plans", 6, "No Plans" );

        //adding 6 Note to the notes api
        notes.add(javaWork);
        notes.add(gymHobbyArchived);
        notes.add(summerHoliday);
        notes.add(codeApp);
        notes.add(testApp);
        notes.add(deployApp);

        //adding 2 archived Note to the archivedNotes api
        archivedNotes.add(gymHobbyArchived);
        archivedNotes.add(archivedNoteNoItems);
    }

    @AfterEach
    void tearDown(){
        javaWork = gymHobbyArchived = summerHoliday = emptyInvalidHobby = null;
        javaItemOne = javaItemTwo = javaItemThree = codeOne = codeTwo = null;
        hobbyItemOne = hobbyItemTwo = null;
        holidayItemOne = holidayItemTwo = holidayItemThree = holidayItemFour = null;
        notes = emptyNotes = null;
    }

    @Nested
    class ArrayListCRUD {

        @Test
        void addingANoteAddsToArrayList(){
            Note newNote = new Note("Note Title", 1, "College");
            assertTrue(notes.add(newNote));
            assertTrue(notes.searchNotesByTitle("Note Title").contains("Note Title"));
            assertEquals(newNote, notes.findNote(notes.numberOfNotes() -1));
        }

        @Test
        void updatingANoteThatDoesNotExistReturnsFalse(){
            assertFalse(notes.updateNote(6, "Updating Note", 2, "Work"));
            assertFalse(notes.updateNote(-1, "Updating Note", 2, "Work"));
            assertFalse(emptyNotes.updateNote(0, "Updating Note", 2, "Work"));
        }

        @Test
        void updatingANoteThatExistsReturnsTrueAndUpdates(){
            //check note 5 exists and check the contents
            assertEquals(deployApp, notes.findNote(5));
            assertEquals("Deploy App", notes.findNote(5).getNoteTitle());
            assertEquals(3, notes.findNote(5).getNotePriority());
            assertEquals("Work", notes.findNote(5).getNoteCategory());

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(notes.updateNote(5, "Updating Note", 2, "College"));
            assertEquals("Updating Note", notes.findNote(5).getNoteTitle());
            assertEquals(2, notes.findNote(5).getNotePriority());
            assertEquals("College", notes.findNote(5).getNoteCategory());
        }

        @Test
        void deletingANoteThatDoesNotExistReturnsNull(){
            assertNull(emptyNotes.deleteNote(0));
            assertNull(notes.deleteNote(-1));
            assertNull(notes.deleteNote(6));
        }

        @Test
        void deletingANoteThatExistsDeletesAndReturnsDeletedObject(){
            assertEquals(6, notes.numberOfNotes());
            assertEquals(deployApp, notes.deleteNote(5));
            assertEquals(5, notes.numberOfNotes());
            //ensuring items associated with deleted notes are also deleted i.e. cascading deletes.
            assertTrue(notes.searchItemByDescription("Study Objects and Classes").contains("0: Study Objects and Classes. [TODO]"));
            assertEquals(javaWork, notes.deleteNote(0));
            assertEquals(4, notes.numberOfNotes());
            assertEquals("No items found for: Study Objects and Classes", notes.searchItemByDescription("Study Objects and Classes"));
        }

        @Test
        void archivingANoteThatDoesNotExistReturnsFalse(){
            assertFalse(emptyNotes.archiveNote(0));
            assertFalse(notes.archiveNote(-1));
            assertFalse(notes.archiveNote(6));
        }

        @Test
        void archivingANoteThatHasToDoItemsReturnsFalse(){
            assertFalse(notes.archiveNote(0));
        }

        @Test
        void archivingANoteThatIsAlreadyArchivedReturnsFalse(){
            assertFalse(notes.archiveNote(1)); //gymHobbyArchived already archived
        }

        @Test
        void archivingANoteThatHasAllCompleteItemsReturnsTrueAndArchivesNote(){
            assertFalse(notes.findNote(3).isNoteArchived());
            assertTrue(notes.archiveNote(3));
            assertTrue(notes.findNote(3).isNoteArchived());
        }

        @Test
        void archivingAllNotesWhenNoNotesOrActiveNotesExistReturnsMessage(){
            assertEquals("No active notes stored", emptyNotes.archiveNotesWithAllItemsComplete());
            assertEquals("No active notes stored",archivedNotes.archiveNotesWithAllItemsComplete());
        }

        @Test
        void archivingAllNotesWhenActiveNotesWithAllCompletedItemsExistArchivesNotes(){
            assertEquals(1, notes.numberOfArchivedNotes());
            assertFalse(notes.findNote(3).isNoteArchived());  //codeApp - two completed items
            assertFalse(notes.findNote(4).isNoteArchived());  //testApp - no items
            assertFalse(notes.findNote(5).isNoteArchived());  //deployApp - no items
            notes.archiveNotesWithAllItemsComplete();
            assertTrue(notes.findNote(3).isNoteArchived());
            assertTrue(notes.findNote(4).isNoteArchived());
            assertTrue(notes.findNote(5).isNoteArchived());
            assertEquals(4, notes.numberOfArchivedNotes());
        }

        @Test
        void archivingAllNotesWhenActiveNotesWithToDoItemsExistDoesntArchivesNotes(){
            //todo
            assertFalse(notes.findNote(0).isNoteArchived());  //javaWork - 3 items, one Completed
            assertFalse(notes.findNote(2).isNoteArchived());  //summerHoliday - 4 items - all todo
            notes.archiveNotesWithAllItemsComplete();
            assertFalse(notes.findNote(0).isNoteArchived());
            assertFalse(notes.findNote(2).isNoteArchived());
        }
    }

    @Nested
    class CountingMethods {

        @Test
        void numberOfNotesCalculatedCorrectly() {
            assertEquals(6, notes.numberOfNotes());
            assertEquals(0, emptyNotes.numberOfNotes());
        }

        @Test
        void numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(1, notes.numberOfArchivedNotes());
            assertEquals(0, emptyNotes.numberOfArchivedNotes());
        }

        @Test
        void numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(5, notes.numberOfActiveNotes());
            assertEquals(0, emptyNotes.numberOfActiveNotes());
        }

        @Test
        void numberOfNotesByCategoryCalculatedCorrectly() {
            assertEquals(4, notes.numberOfNotesByCategory("Work"));
            assertEquals(1, notes.numberOfNotesByCategory("Holiday"));
            assertEquals(1, notes.numberOfNotesByCategory("Hobby"));
            assertEquals(0, notes.numberOfNotesByCategory("Home"));
            assertEquals(0, emptyNotes.numberOfNotesByCategory("Work"));
        }

        @Test
        void numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, notes.numberOfNotesByPriority(1));
            assertEquals(0, notes.numberOfNotesByPriority(2));
            assertEquals(1, notes.numberOfNotesByPriority(3));
            assertEquals(3, notes.numberOfNotesByPriority(4));
            assertEquals(1, notes.numberOfNotesByPriority(5));
            assertEquals(0, emptyNotes.numberOfNotesByPriority(1));
        }

        @Test
        void numberOfItemsCalculatedCorrectly() {
            assertEquals(11, notes.numberOfItems());
            assertEquals(0, emptyNotes.numberOfItems());
            assertEquals(11, notes.numberOfCompleteItems() + notes.numberOfTodoItems());
        }

        @Test
        void numberOfCompletedItemsCalculatedCorrectly() {
            assertEquals(5, notes.numberOfCompleteItems());
            assertEquals(0, emptyNotes.numberOfCompleteItems());
            assertEquals(notes.numberOfCompleteItems(), notes.numberOfItems() - notes.numberOfTodoItems());
        }

        @Test
        void numberOfTODOItemsCalculatedCorrectly() {
            assertEquals(6, notes.numberOfTodoItems());
            assertEquals(0, emptyNotes.numberOfTodoItems());
            assertEquals(notes.numberOfTodoItems(), notes.numberOfItems() - notes.numberOfCompleteItems());
        }

    }

    @Nested
    class ListingMethods {

        @Test
        void listAllNotesReturnsNoNotesStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.listAllNotes().toLowerCase().contains("no notes"));
        }

        @Test
        void listAllNotesReturnsNotesWhenArrayListHasNotesStored() {
            assertEquals(6, notes.numberOfNotes());
            String notesString = notes.listAllNotes().toLowerCase();
            assertTrue(notesString.contains("learning java"));
            assertTrue(notesString.contains("code app"));
            assertTrue(notesString.contains("test app"));
            assertTrue(notesString.contains("deploy app"));
            assertTrue(notesString.contains("gym"));
            assertTrue(notesString.contains("summer holiday"));
        }

        @Test
        void listActiveNotesReturnsNoActiveNotesStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfActiveNotes());
            assertTrue(emptyNotes.listActiveNotes().toLowerCase().contains("no active notes"));
        }

        @Test
        void listActiveNotesReturnsActiveNotesWhenArrayListHasActiveNotesStored() {
            assertEquals(5, notes.numberOfActiveNotes());
            String activeNotesString = notes.listActiveNotes().toLowerCase();
            assertTrue(activeNotesString.contains("learning java"));
            assertTrue(activeNotesString.contains("code app"));
            assertTrue(activeNotesString.contains("test app"));
            assertTrue(activeNotesString.contains("deploy app"));
            assertTrue(activeNotesString.contains("summer holiday"));
            assertFalse(activeNotesString.contains("gym"));
        }

        @Test
        void listArchivedNotesReturnsNoArchivedNotesStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfArchivedNotes());
            assertTrue(emptyNotes.listArchivedNotes().toLowerCase().contains("no archived notes"));
        }

        @Test
        void listArchivedNotesReturnsArchivedNotesWhenArrayListHasArchivedNotesStored() {
            assertEquals(1, notes.numberOfArchivedNotes());
            String archivedNotesString = notes.listArchivedNotes().toLowerCase();
            assertTrue(archivedNotesString.contains("gym"));
            assertFalse(archivedNotesString.contains("learning java"));
            assertFalse(archivedNotesString.contains("code app"));
            assertFalse(archivedNotesString.contains("test app"));
            assertFalse(archivedNotesString.contains("deploy app"));
            assertFalse(archivedNotesString.contains("summer holiday"));
        }

        @Test
        void listNotesBySelectedCategoryReturnsNoNotesWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.listNotesBySelectedCategory("Home").toLowerCase().contains("no notes"));
        }

        @Test
        void listNotesBySelectedCategoryReturnsNoNotesByCategoryWhenNoNotesOfThatCategoryExist() {
            //4 work, 1 hobby, 1 holiday
            assertEquals(6, notes.numberOfNotes());
            String collegeString = notes.listNotesBySelectedCategory("College").toLowerCase();
            assertTrue(collegeString.contains("no notes"));
            assertTrue(collegeString.contains("college"));
        }

        @Test
        void listNotesBySelectedCategoryReturnsAllNotesByCategoryWhenNotesOfThatCategoryExist() {
            //4 work, 1 hobby, 1 holiday
            assertEquals(6, notes.numberOfNotes());

            String hobbyString = notes.listNotesBySelectedCategory("Hobby").toLowerCase();
            assertTrue(hobbyString.contains("1 note"));
            assertTrue(hobbyString.contains("hobby"));
            assertTrue(hobbyString.contains("gym"));
            assertFalse(hobbyString.contains("learning java"));
            assertFalse(hobbyString.contains("code app"));
            assertFalse(hobbyString.contains("test app"));
            assertFalse(hobbyString.contains("deploy app"));
            assertFalse(hobbyString.contains("summer holiday"));

            String workString = notes.listNotesBySelectedCategory("Work").toLowerCase();
            assertTrue(workString.contains("4 note"));
            assertTrue(workString.contains("work"));
            assertTrue(workString.contains("learning java"));
            assertTrue(workString.contains("code app"));
            assertTrue(workString.contains("test app"));
            assertTrue(workString.contains("deploy app"));
            assertFalse(workString.contains("gym"));
            assertFalse(workString.contains("summer holiday"));
        }

        @Test
        void listNotesBySelectedPriorityReturnsNoNotesWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.listNotesBySelectedPriority(1).toLowerCase().contains("no notes"));
        }

        @Test
        void listNotesBySelectedPriorityReturnsNoNotesByPriorityWhenNoNotesOfThatPriorityExist() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (3 notes), 5 (1 note)
            assertEquals(6, notes.numberOfNotes());
            String priority2String = notes.listNotesBySelectedPriority(2).toLowerCase();
            assertTrue(priority2String.contains("no notes"));
            assertTrue(priority2String.contains("2"));
        }

        @Test
        void listNotesBySelectedPriorityReturnsAllNotesByPriorityWhenNotesOfThatPriorityExist() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (3 notes), 5 (1 note)
            assertEquals(6, notes.numberOfNotes());
            String priority1String = notes.listNotesBySelectedPriority(1).toLowerCase();
            assertTrue(priority1String.contains("1 note"));
            assertTrue(priority1String.contains("priority 1"));
            assertTrue(priority1String.contains("summer holiday"));
            assertFalse(priority1String.contains("gym"));
            assertFalse(priority1String.contains("learning java"));
            assertFalse(priority1String.contains("code app"));
            assertFalse(priority1String.contains("test app"));
            assertFalse(priority1String.contains("deploy app"));

            String priority4String = notes.listNotesBySelectedPriority(4).toLowerCase();
            assertTrue(priority4String.contains("3 note"));
            assertTrue(priority4String.contains("priority 4"));
            assertTrue(priority4String.contains("gym"));
            assertTrue(priority4String.contains("code app"));
            assertTrue(priority4String.contains("test app"));
            assertFalse(priority4String.contains("learning java"));
            assertFalse(priority4String.contains("deploy app"));
            assertFalse(priority4String.contains("summer holiday"));
        }

        @Test
        void listTodoItemsReturnsNoNotesStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.listTodoItems().toLowerCase().contains("no notes"));
        }

        @Test
        void listTodoItemsReturnsAllTodoItemsWhenArrayListHasNotesWithItems() {
            assertEquals(6, notes.numberOfNotes());
            assertEquals(6, notes.numberOfTodoItems());

            String todoItems = notes.listTodoItems().toLowerCase();

            //javaWork object (two to do items)
            assertTrue(todoItems.contains("learning java"));
            assertTrue(todoItems.contains("study objects and classes"));
            assertTrue(todoItems.contains("study persistence"));
            assertFalse(todoItems.contains("study arraylists"));

            //gymHobbyArchived object (all completed)
            assertFalse(todoItems.contains("gym"));
            assertFalse(todoItems.contains("swimming"));
            assertFalse(todoItems.contains("cycling"));

            //summerHoliday object (four to do items)
            assertTrue(todoItems.contains("summer holiday"));
            assertTrue(todoItems.contains("book flights"));
            assertTrue(todoItems.contains("book hotel"));
            assertTrue(todoItems.contains("book transfers to"));
            assertTrue(todoItems.contains("book transfers from"));
        }


        @Test
        void listItemStatusByCategoryReturnsNoNotesWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.listItemStatusByCategory("Work").toLowerCase().contains("no notes"));
        }

        @Test
        void listItemStatusByCategoryReturnsCorrectNumbersWhenThatCategoryExist() {

            String categoryString = notes.listItemStatusByCategory("Holiday").toLowerCase();
            assertTrue(categoryString.contains("number completed: 0"));
            assertTrue(categoryString.contains("number todo: 4"));
            assertTrue(categoryString.contains("book flights"));
            assertTrue(categoryString.contains("book hotel"));
            assertTrue(categoryString.contains("book transfers to airport"));
            assertTrue(categoryString.contains("book transfers from airport"));

            categoryString = notes.listItemStatusByCategory("Work").toLowerCase();
            assertTrue(categoryString.contains("number completed: 3"));
            assertTrue(categoryString.contains("number todo: 2"));

            categoryString = notes.listItemStatusByCategory("Hobby").toLowerCase();
            assertTrue(categoryString.contains("number completed: 2"));
            assertTrue(categoryString.contains("number todo: 0"));
        }

        @Test
        void listItemStatusByCategoryReturnsCorrectNumbersWhenThatCategoryHasNoNotes() {
            String categoryString = notes.listItemStatusByCategory("College").toLowerCase();
            assertTrue(categoryString.contains("number completed: 0"));
            assertTrue(categoryString.contains("number todo: 0"));
        }

    }

    @Nested
    class FindingSearchingMethods {

        @Test
        void findNoteReturnsNoteWhenIndexIsValid() {
            assertEquals(6, notes.numberOfNotes());
            assertEquals(javaWork, notes.findNote(0));
            assertEquals(deployApp, notes.findNote(5));
        }

        @Test
        void findNoteReturnsNullWhenIndexIsInValid() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertNull(emptyNotes.findNote(0));

            assertEquals(6, notes.numberOfNotes());
            assertNull(notes.findNote(-1));
            assertNull(notes.findNote(6));
        }

        @Test
        void searchNotesByTitleReturnsNoNotesWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.searchNotesByTitle("a").toLowerCase().contains("no notes"));
        }

        @Test
        void searchNotesByTitleReturnsNoNotesFoundWhenArrayListHasNoNotesMatchingSearchTitle() {
            assertEquals(6, notes.numberOfNotes());
            assertTrue(notes.searchNotesByTitle("absolutely no notes have this string").toLowerCase().contains("no notes"));
        }

        @Test
        void searchNotesByTitleReturnsNotesFoundWhenArrayListHasNotesMatchingSearchTitle() {
            String searchNotes = notes.searchNotesByTitle("Holiday").toLowerCase();
            assertTrue(searchNotes.contains("summer holiday"));
            assertFalse(searchNotes.contains("learning java"));
            assertFalse(searchNotes.contains("gym"));

            String searchNotesMultipleHits = notes.searchNotesByTitle("App").toLowerCase();
            assertTrue(searchNotesMultipleHits.contains("code app"));
            assertTrue(searchNotesMultipleHits.contains("test app"));
            assertTrue(searchNotesMultipleHits.contains("deploy app"));
            assertFalse(searchNotesMultipleHits.contains("learning java"));
            assertFalse(searchNotesMultipleHits.contains("gym"));
        }

        @Test
        void searchItemByDescriptionReturnsNoItemsWhenArrayListIsEmpty() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.searchItemByDescription("a").toLowerCase().contains("no notes"));
        }

        @Test
        void searchItemByDescriptionReturnsNoItemsFoundWhenArrayListHasNoItemsMatchingSearchDescription() {
            assertEquals(0, emptyNotes.numberOfNotes());
            assertTrue(emptyNotes.searchItemByDescription("absolutely no items have this string").toLowerCase().contains("no notes"));
        }

        @Test
        void searchItemByDescriptionReturnsItemsFoundWhenArrayListHasItemsMatchingSearchDescription() {
            String searchItems = notes.searchItemByDescription("Cycling").toLowerCase();
            assertTrue(searchItems.contains("cycling 30km"));
            assertFalse(searchItems.contains("swimming"));

            String searchItemsMultipleHits = notes.searchItemByDescription("Book").toLowerCase();
            assertTrue(searchItemsMultipleHits.contains("book flights"));
            assertTrue(searchItemsMultipleHits.contains("book transfers from"));
            assertTrue(searchItemsMultipleHits.contains("book transfers to"));
            assertTrue(searchItemsMultipleHits.contains("book hotel"));
            assertFalse(searchItemsMultipleHits.contains("study"));
            assertFalse(searchItemsMultipleHits.contains("code the"));
        }
    }

    @Nested
    class HelperMethods {
        @Test
        void notesSizeReturnsTrueForValidIndex() {
            assertTrue(notes.isValidIndex(0));
            assertTrue(notes.isValidIndex(notes.numberOfNotes() -1));
        }

        @Test
        void notesSizeReturnsFalseForInValidIndex() {
            assertFalse(emptyNotes.isValidIndex(0));
            assertFalse(emptyNotes.isValidIndex(1));
            assertFalse(notes.isValidIndex(-1));
            assertFalse(notes.isValidIndex(notes.numberOfNotes()));
            //todo

        }
    }

}
