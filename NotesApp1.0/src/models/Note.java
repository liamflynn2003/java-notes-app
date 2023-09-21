package models;

import java.util.ArrayList;
import java.util.Objects;
/**
 * The Note class represents Note objects that can be contained in a Notes array list in NoteAPI.
 * Each note can contain an arraylist items containing Item objects.
 *
 * Every note has a title, priority, category and can be archived or active.
 * Archived notes cannot be updated in any way.
 * Active notes can have items added, or it's fields changed.
 *
 * The methods set the fields to their specified values,
 * pulls from and updates items in several ways,
 * checks the completion status of the note,
 * and generates a toString.
 *
 * @author Liam FLynn
 * @version 1.0
 */
public class Note {
    private int notePriority = 1;
    private String noteCategory = "";
    private boolean isNoteArchived = false;
    private String noteTitle;

    public Note(String noteTitle, int notePriority, String noteCategory) {
        setNoteTitle(noteTitle);
        setNotePriority(notePriority);
        setNoteCategory(noteCategory);
    }
    /**
     * This setter method ensures any given Priority is between 1 and 5.
     * If the value given is not between 1 and 5 then it is set to 1.
     * If no priority is given then it is set to 1.
     * @param notePriority The priority of an individual note, can only be an int from a range of 1 to 5.
     */
    public void setNotePriority(int notePriority) {
        if (util.Utilities.validRange(notePriority, 1, 5)) {
            this.notePriority = notePriority;
        } else {
            this.notePriority = 1;
        }
    }

    public int getNotePriority() {
        return notePriority;
    }


    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    private ArrayList<Item> items = new ArrayList<>();

    public String getNoteTitle() {
        return noteTitle;
    }
    /**
     * This setter method truncates any given note title String to 20 chars.
     * If this is an update to a title that already exists and the new String exceeds 20 chars then
     * no update will be made.
     *If no note title is given, then it will be saved as "No Title"
     * @param noteTitle The title of an individual note, a String of max 20 chars.
     */
    public void setNoteTitle(String noteTitle) {
        if (this.noteTitle == null) {
            this.noteTitle = util.Utilities.truncateString(noteTitle, 20);
        }
        if (noteTitle.length() <= 20) {
            this.noteTitle = noteTitle;
        }
    }

    /**
     * This setter method ensures any given Category is home, work, hobby, holiday or college.
     *If no priority is given then it is set to an empty string "".
     * @param noteCategory The category of an individual note, can only be a string of home, work, hobby, holiday or college.
     */
    public void setNoteCategory(String noteCategory) {
        if (Objects.equals(noteCategory, "Home") || Objects.equals(noteCategory, "Work") || Objects.equals(noteCategory, "Hobby") || Objects.equals(noteCategory, "Holiday") || Objects.equals(noteCategory, "College")) {
            this.noteCategory = noteCategory;
        }
    }

    public String getNoteCategory() {
        return noteCategory;
    }


    public boolean isNoteArchived() {
        return isNoteArchived;
    }

    public void setNoteArchived(boolean noteArchived) {
        isNoteArchived = noteArchived;
    }

    /**
     * This method adds an item object to the items array list within the note.
     * @param item an object of the item class.
     * @return the boolean result of the add
     */
    public boolean addItem(Item item) {
        return items.add(item);
    }
    /**
     * This method lists all items in a note.
     * @return a String that either tells the user no items have been added yet or lists all items in the note
     */
    public String listItems() {
        StringBuilder S = new StringBuilder();
        if (items.isEmpty()) {
            S = new StringBuilder("No items added" + "\n");
        }
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Item foundItem = findItem(i);
                S.append(i).append(": ").append(foundItem.toString());
            }
            return S.toString();
        }
        return S.toString();
    }
    /**
     * This method can update a pre-existing item in a note with new values.
     * @param index the index number of the item the user wants to update.
     * @param itemDescription the new item description that the user will enter.
     * @param itemCompleted the new completion status of the item that the user will choose.
     * @return the boolean result of the update depending on its success.
     */
    public boolean updateItem(int index, String itemDescription, boolean itemCompleted) {
        Item foundItem = findItem(index);
        if (foundItem != null) {
            if (itemDescription.length() < 50) {
                foundItem.setItemDescription(itemDescription);
            }
            foundItem.setItemCompleted(itemCompleted);
            return true;
        }
        return false;
    }
    /**
     * This method can delete an item from a note.
     * @param index the index number of the item the user wants to delete.
     * @return the item that was deleted, or null if an item of that index does not exist
     */
    public Item deleteItem(int index) {
        if (isValidIndex(index)) {
            models.Item foundItem = findItem(index);
            items.remove(index);
            return foundItem;
        }
        return null;
    }
    /**
     * This method can find a specific item from a note within the arraylist of items.
     * @param index the index number of the item the user wants to find.
     * @return the item that was found, or null if an item of that index does not exist
     */
    public Item findItem(int index) {
        if (isValidIndex(index)) {
            return items.get(index);
        }
        return null;
    }

    /**
     * This method checks if an item of a given index exists or not.
     * @param index the index number of the item
     * @return true if an item of the index number exists, false if not.
     */
    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < items.size());
    }
    /**
     * This method returns the number of items in a note
     * @return the number of items in a note.
     */
    public int numberOfItems() {
        return items.size();
    }
    /**
     * This method checks if a note is complete or not.
     * @return true if all items in the note are complete, false if one or more are to do, true if the note has no items
     */
    public boolean checkNoteCompletionStatus() {
        if (numberOfItems() != 0) {
            for (int i = 0; i < numberOfItems(); i++) {
                Item foundItem = findItem(i);
                if (!foundItem.isItemCompleted()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return notePriority == note.notePriority && isNoteArchived == note.isNoteArchived && Objects.equals(noteTitle, note.noteTitle) && Objects.equals(noteCategory, note.noteCategory) && Objects.equals(items, note.items);
    }
    /**
     * Generates a toString for a note containing all relevant values.
     * @return The note title, priority, category and completion status a user-friendly String.
     */
    public String toString() {
        String S;
        if (isNoteArchived()) {
            S = noteTitle + ", Priority=" + notePriority + ", Category= " + noteCategory + ", Archived=Y " + "\n"
                    + listItems();
        } else {
            S = noteTitle + ", Priority=" + notePriority + ", Category= " + noteCategory + ", Archived=N " + "\n"
                    + listItems();
        }
        return S;
    }
}