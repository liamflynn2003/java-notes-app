package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
/**
 * The NoteAPI class contains and manages a Notes array list.
 *
 * Through its management of notes, it can also pull from
 * and check the items array within each note in several ways.
 *
 * The methods get info from and updates notes in several ways,
 * can archive a note or all active notes with items completed,
 * return the number of notes, archived notes, active notes or items,
 * list notes in specific orders or of specific categories or priorities,
 * search for notes by their titles,
 * or search for items by their descriptions.
 *
 * @author Liam FLynn
 * @version 1.0
 */
public class NoteAPI {
    private ArrayList<models.Note> notes = new ArrayList<>();
    /**
     * This method checks if a note of a given index exists or not.
     * @param index the index number of the note
     * @return true if a note of the index number exists, false if not.
     */
    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < notes.size());
    }
    /**
     * This method can find a specific note within the arraylist of notes.
     * @param index the index number of the note the user wants to find.
     * @return the note that was found, or null if an item of that index does not exist
     */
    public models.Note findNote(int index) {
        if (isValidIndex(index)) {
            return notes.get(index);
        }
        return null;
    }
    /**
     * This method adds a note object to the notes array list within the noteAPI.
     * @param note an object of the note class.
     * @return the boolean result of the add
     */
    public boolean add(models.Note note) {
        return notes.add(note);
    }
    /**
     * This method can update a pre-existing note with new values.
     * @param indexToUpdate the index number of the note the user wants to update.
     * @param noteTitle the new title of the note that the user will enter.
     * @param notePriority the new note priority of the note that the user will choose.
     * @param noteCategory the new note category that the user will choose.
     * @return the boolean result of the update depending on its success.
     */
    public boolean updateNote(int indexToUpdate, String noteTitle, int notePriority, String noteCategory) {
        models.Note foundNote = findNote(indexToUpdate);
        if (foundNote != null) {
            foundNote.setNoteTitle(noteTitle);
            foundNote.setNotePriority(notePriority);
            foundNote.setNoteCategory(noteCategory);
            return true;
        }
        return false;
    }
    /**
     * This method can delete an item from a note.
     * @param indexToDelete the index number of the note the user wants to delete.
     * @return the note that was deleted, or null if a note of that index does not exist
     */
    public models.Note deleteNote(int indexToDelete) {
        if (isValidIndex(indexToDelete)) {
            models.Note foundNote = findNote(indexToDelete);
            for (int z = 0; z < foundNote.numberOfItems(); z++) {
                foundNote.deleteItem(z);
            }
            notes.remove(indexToDelete);
            return foundNote;
        }
            return null;
    }

    /**
     * This method can archive a note.
     * @param indexToArchive the index number of the note the user wants to archive.
     * @return true if the note was archived, false if the note is already archived or index does not exist
     */
    public boolean archiveNote(int indexToArchive) {
        if (isValidIndex(indexToArchive)) {
            if (!notes.get(indexToArchive).isNoteArchived() && notes.get(indexToArchive).checkNoteCompletionStatus()) {
                notes.get(indexToArchive).setNoteArchived(true);
                return true;
            }
        }
        return false;
    }
    /**
     * This method can archive all notes that have all items complete.
     * @return string that tells the user if there are no notes, no active notes or displays all notes that were archived if successful.
     */
    public String archiveNotesWithAllItemsComplete() {
        String S = "";
        if (numberOfNotes() == 0) {
            S = "No notes stored";
            return S;
        }
        if (numberOfActiveNotes() == 0) {
            S = "No active notes stored";
            return S;
        }
        if (numberOfNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (!foundNote.isNoteArchived() && (foundNote.checkNoteCompletionStatus() || foundNote.numberOfItems() == 0)) {
                    archiveNote(i);
                    S = String.valueOf(foundNote);
                }
            }
            return S;
        }
        return S;
    }

    /**
     * This method returns the number of notes stored in noteAPI.
     * @return the number of notes
     */
    public int numberOfNotes() {
        return notes.size();
    }
    /**
     * This method returns the number of archived notes.
     * @return the number of archived notes
     */
    public int numberOfArchivedNotes() {
        int x = 0;
        for (int i = 0; i < notes.size(); i++) {
            models.Note foundNote = findNote(i);
            if (foundNote.isNoteArchived()) {
                x++;
            }
        }
        return x;
    }
    /**
     * This method returns the number of active notes.
     * @return the number of active notes
     */
    public int numberOfActiveNotes() {
        int x = 0;
        for (int i = 0; i < notes.size(); i++) {
            models.Note foundNote = findNote(i);
            if (!foundNote.isNoteArchived()) {
                x++;
            }
        }
        return x;
    }
    /**
     * This method returns the number of notes that are of a specific category.
     * @param Category the category of which the number of notes of this category will be counted
     * @return the number of notes of a category chosen by the user
     */
    public int numberOfNotesByCategory(String Category) {
        int x = 0;
        for (models.Note note : notes) {
            if (Objects.equals(note.getNoteCategory(), Category)) {
                x++;
            }
        }
        return x;
    }
    /**
     * This method returns the number of notes that are of a specific priority.
     * @param Priority the priority of which the number of notes of this priority will be counted
     * @return the number of notes of a priority chosen by the user
     */
    public int numberOfNotesByPriority(int Priority) {
        int x = 0;
        for (models.Note note : notes) {
            if (note.getNotePriority() == Priority) {
                x++;
            }
        }
        return x;
    }
    /**
     * This method returns the number of items that are in all notes.
     * @return the total number of items in all notes.
     */
    public int numberOfItems() {
        int x = 0;
        for (int i = 0; i < notes.size(); i++) {
            models.Note foundNote = findNote(i);
            x += foundNote.numberOfItems();
        }
        return x;
    }
    /**
     * This method returns the number of complete items that are in all notes.
     * @return the total number of complete items in all notes.
     */
    public int numberOfCompleteItems() {
        int x = 0;
        for (int i = 0; i < notes.size(); i++) {
            models.Note foundNote = findNote(i);
            for (int z = 0; z < foundNote.numberOfItems(); z++) {
                models.Item foundItem = foundNote.findItem(z);

                if (foundItem.isItemCompleted()) {
                    x++;
                }
            }
        }
        return x;
    }
    /**
     * This method returns the number of to do items that are in all notes.
     * @return the total number of to do items in all notes.
     */
    public int numberOfTodoItems() {
        int x = 0;
        for (int i = 0; i < notes.size(); i++) {
            models.Note foundNote = findNote(i);

            for (int z = 0; z < foundNote.numberOfItems(); z++) {
                models.Item foundItem = foundNote.findItem(z);

                if (!foundItem.isItemCompleted()) {
                    x++;
                }
            }
        }
        return x;
    }
    /**
     * This method lists all notes.
     * @return a String that either tells the user no notes have been stored yet or lists all notes.
     */
    public String listAllNotes() {
        String S = "";
        if (numberOfNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (foundNote.isValidIndex(i)) {
                    S = i + "" + foundNote + "\n";
                    return S;
                }
            }
        }
        if (numberOfNotes() == 0) {
            S = "No notes stored.";
            return S;
        }
        return S;
    }
    /**
     * This method lists all active notes.
     * @return a String that either tells the user no notes have been stored yet, no active notes have been stored yet
     * or lists all active notes.
     */
    public String listActiveNotes() {
        StringBuilder S = new StringBuilder();
        if (numberOfActiveNotes() == 0) {
            S = new StringBuilder("No active notes stored.");
            return S.toString();
        }
        if (numberOfActiveNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (!foundNote.isNoteArchived()) {
                    S.append("Index Number: ").append(i);
                    S.append(" ").append(foundNote);
                }
            }
            return S.toString();
        }
        return S.toString();
    }
    /**
     * This method lists all archived notes.
     * @return a String that either tells the user no notes have been stored yet, no archived notes have been stored yet
     * or lists all archived notes.
     */
    public String listArchivedNotes() {
        StringBuilder S = new StringBuilder();
        if (numberOfArchivedNotes() == 0) {
            S = new StringBuilder("No archived notes stored.");
            return S.toString();
        }
        if (numberOfArchivedNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (foundNote.isNoteArchived()) {
                    S.append("Index Number: ").append(i);
                    S.append(" ").append(foundNote);
                }
            }
            return S.toString();
        }
        return S.toString();
    }
    /**
     * This method returns the all notes that are of a specific category.
     * @param category all notes containing this category string will be returned
     * @return a String that either tells the user no notes have been stored yet, no notes of this category have been stored yet
     *        or lists all notes of the given category.
     */
    public String listNotesBySelectedCategory(String category) {
        String S = "";
        if (numberOfNotes() == 0) {
            S = "No notes stored.";
            return S;
        }
        if (numberOfNotesByCategory(category) == 0) {
            S = "No notes with category" + category;
            return S;
        }

        if (numberOfNotesByCategory(category) != 0)
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (Objects.equals(foundNote.getNoteCategory(), category)) {
                    S = foundNote.toString();
                    return S;
                }
            }
        return S;
    }
    /**
     * This method returns the all notes that are of a specific priority.
     * @param priority all notes containing this priority will be returned
     * @return a String that either tells the user no notes have been stored yet, no notes of this priority have been stored yet
     *        or lists all notes of the given priority.
     */
    public String listNotesBySelectedPriority(int priority) {
        String S = "";
        if (numberOfNotes() == 0) {
            S = "No notes stored.";
            return S;
        }
        if (numberOfNotesByPriority(priority) == 0) {
            S = "Priority" + priority + " has no notes";
            return S;
        }
        if (numberOfNotesByPriority(priority) != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (foundNote.getNotePriority() == priority) {
                    S= i + " note of priority " + priority;
                    S += foundNote.toString();
                }
            }
            return S;
        }
        return S;
    }
    /**
     * This method lists all to do items.
     * @return a String that either tells the user no notes have been stored yet, no to do items currently exist
     * or lists all to do items.
     */
    public String listTodoItems() {
        String S = "";

        if (numberOfNotes() == 0) {
            S = "No notes stored.";
            return S;
        }
        if(numberOfNotes()!=0) {
            if (numberOfTodoItems() != 0) {
                for (int i = 0; i < notes.size(); i++) {
                    models.Note foundNote = findNote(i);
                    for (int z = 0; z < foundNote.numberOfItems(); z++) {
                        models.Item foundItem = foundNote.findItem(z);
                        if (!foundItem.isItemCompleted()) {
                            S = foundNote.getNoteTitle() + foundItem;
                            return S;
                        }
                    }
                }
            }
            if(numberOfTodoItems()==0){
                S = "No todo items.";
                return S;
            }
        }
        return S;
    }
    /**
     * This method lists the completion status of all notes of a specific category chosen by the user.
     * @param category completion status of all notes of this category will be returned.
     * @return a String that either tells the user no notes have been stored yet, no notes of this category have been stored yet
     *        , no items exist in any notes of this category yet, or lists all notes of the given category
     */
    public String listItemStatusByCategory(String category) {
        StringBuilder S = new StringBuilder();
        if (numberOfNotes() == 0) {
            S = new StringBuilder("No notes stored yet.");
            return S.toString();
        }
        if (numberOfNotesByCategory(category) == 0) {
            S = new StringBuilder("number completed: 0" + "\n" + "number todo: 0");
            return S.toString();
        }

        if (numberOfNotesByCategory(category) != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                StringBuilder S1 = new StringBuilder();
                StringBuilder S2 = new StringBuilder();
                if (Objects.equals(foundNote.getNoteCategory(), category)) {
                    int x = 0;
                    int y = 0;
                     for (int z = 0; z < foundNote.numberOfItems(); z++) {
                        models.Item foundItem = foundNote.findItem(z);
                        if (foundItem.isItemCompleted()) {
                            x++;
                            S1.append(foundItem);
                        }

                        if (!foundItem.isItemCompleted()) {
                            y++;
                            S2.append(foundItem);
                        }
                         S1 = new StringBuilder("number completed: " + x + "\n");
                         S2 = new StringBuilder("number todo: " + y + "\n");
                         S.append(S1);
                         S.append(S2);
                    }
                }
            }
        }
        return S.toString();
    }
    /**
     * This method returns notes containing a given title
     * @param searchString all notes with this title will be returned
     * @return a String that either tells the user no notes have been stored yet, no notes were found for the given title,
     * or returns the note of the given title.
     */
    public String searchNotesByTitle(String searchString) {
        String S = "";
        if (numberOfNotes() == 0) {
            S = "No notes stored.";
        }
        if (numberOfNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                if (Objects.equals(foundNote.getNoteTitle(), searchString)) {
                    S = "Index Number: " + i
                            + foundNote;
                }
                if (S.equals("")) {
                    S = "No notes found for: " + searchString;
                }
            }
        }
        return S;
    }
    /**
     * This method returns items containing a given description.
     * @param searchString all items with this description will be returned.
     * @return a String that either tells the user no notes have been stored yet, no items were found for the given description,
     * or returns the items containing the given description.
     */
    public String searchItemByDescription(String searchString) {
        String S = "";
        if (numberOfNotes() == 0) {
            S = "No notes stored.";
        }
        if (numberOfNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                models.Note foundNote = findNote(i);
                for (int z = 0; z < foundNote.numberOfItems(); z++) {
                    models.Item foundItem = foundNote.findItem(z);
                    if (Objects.equals(foundItem.getItemDescription(), searchString)) {
                        S = i + foundNote.getNoteTitle() + foundItem;
                        return S;
                    }
                }
            }
            if (S.equals("")) {
                S = "No items found containing: " + searchString;
            }
        }
        return S;
    }
    /**
     * This method loads a note saved as a file.
     */
    public void load() throws Exception {
        Class<?>[] classes = new Class[]{models.Note.class};
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("notes.xml"));
        notes = (ArrayList<models.Note>) is.readObject();
        is.close();
    }
    /**
     * This method saves the current notes as a file "notes.xml"".
     */
    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out =
                xstream.createObjectOutputStream(new FileWriter("notes.xml"));
        out.writeObject(notes);
        out.close();
    }
}
