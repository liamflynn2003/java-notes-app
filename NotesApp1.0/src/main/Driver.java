package main;

import controllers.NoteAPI;
import models.Note;

/**
 * This class runs the application and handles the menu and the user inputs.
 * @author Liam Flynn
 * @version 1.0
 */
public class Driver {

    private final NoteAPI noteAPI = new NoteAPI();

    public static void main(String[] args) throws Exception {
        new Driver();
    }

    public Driver() throws Exception {
        runMenu();
    }

    private int mainMenu() {
        return util.ScannerInput.readNextInt("""
                               
                ------------------------------------------------------------------
                |                         NOTE KEEPER APP                        |
                ------------------------------------------------------------------
                |  NOTE MENU                                                     |
                |   1) Add a note                                                |
                |   2) List all notes (all, active, archived)                    |
                |   3) Update a note                                             |
                |   4) Delete a note                                             |
                |   5) Archive a note                                            |
                ------------------------------------------------------------------
                |   ITEM MENU                                                    |
                |   6) Add an item to a note                                     |
                |   7) Update item description on a note                         |
                |   8) Delete an item from a note                                |
                |   9) Mark an item as complete/todo                             |
                ------------------------------------------------------------------
                | REPORT MENU FOR NOTES                                          |
                |   10) All notes and their items (active & archived)            |
                |   11) Archive notes whose items are all complete               |
                |   12) All notes within a selected Category                     |
                |   13) All notes within a selected Priority                     |
                |   14) Search for all notes (by note title)                     |
                ------------------------------------------------------------------
                | REPORT MENU FOR ITEMS                                          |
                |   15) All items that are todo (with note title)                |
                |   16) Overall number of items todo/complete                    |
                |   17) Todo/completed items by specific category                |
                |   18) Search for all items (by item description)               |
                ------------------------------------------------------------------
                 |   SETTINGS MENU                                               |
                |   19) Save                                                     |
                |   20) Load                                                     |
                |   0) Exit                                                     |
                ------------------------------------------------------------------
                ==>>""");
    }

    private void runMenu() throws Exception {
        int option = mainMenu();

        while (option != 0) {

            switch (option) {
                case 1 -> addNote();
                case 2 -> viewNotes();
                case 3 -> updateNote();
                case 4 -> deleteNote();
                case 5 -> archiveNote();
                case 6 -> addItemToNote();
                case 7 -> updateItemDescInNote();
                case 8 -> deleteItemFromNote();
                case 9 -> markCompletionOfItem();
                case 10 -> printActiveAndArchivedReport();
                case 11 -> archiveNotesWithAllItemsComplete();
                case 12 -> printNotesBySelectedCategory();
                case 13 -> printNotesByPriority();
                case 14 -> searchNotesByTitle();
                case 15 -> printAllTodoItems();
                case 16 -> printOverallItemsTodoComplete();
                case 17 -> printItemCompletionStatusByCategory();
                case 18 -> searchItemsByDescription();
                case 19 -> save();
                case 20 -> load();
                default -> System.out.println("Invalid option entered: " + option);
            }

            util.ScannerInput.readNextLine("\nPress enter key to continue...");

            option = mainMenu();
        }

        System.out.println("Exiting...bye");
        System.exit(0);
    }

    private void addNote() {

        String noteTitle = util.ScannerInput.readNextLine("Enter the Note Title:  ");
        int priority = util.ScannerInput.readNextInt("Enter the Note Priority:  ");
        String noteCategory = util.ScannerInput.readNextLine("Enter the Note Category:  ");

        boolean isAdded = noteAPI.add(new Note(noteTitle, priority, noteCategory));
        if (isAdded) {
            System.out.println("Note Added Successfully");
        } else {
            System.out.println("No Note Added");
        }
    }

    private void viewNotes() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            int option = util.ScannerInput.readNextInt("""
                     ---------------------------
                    |   1) View ALL Notes      |
                    |   2) View Active Notes   |
                    |   3) View Archived Notes |
                    ----------------------------
                    ==>>""");


            if (option != 0) {
                switch (option) {
                    case 1 -> printAllNotes();
                    case 2 -> printActiveNotes();
                    case 3 -> printArchivedNotes();
                    default -> System.out.println("Invalid option entered: " + option);
                }
            }
        }
    }

    private void printAllNotes() {
        System.out.println(noteAPI.numberOfNotes() + " Active and Archived Note(s)");
        System.out.println(noteAPI.listActiveNotes() + "");
        System.out.println(noteAPI.listArchivedNotes());
    }

    private void printActiveNotes() {
        System.out.println(noteAPI.numberOfActiveNotes() + " Active Note(s):" + "\n");
        System.out.println(noteAPI.listActiveNotes());
    }

    private void printArchivedNotes() {
        System.out.println(noteAPI.numberOfArchivedNotes() + " Archived Note(s):" + "\n");
        System.out.println(noteAPI.listArchivedNotes());
    }

    private void updateNote() {
        noteAPI.listAllNotes();
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {

            int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to update:");

            if (noteAPI.isValidIndex(indexNumber)) {
                String noteTitle = util.ScannerInput.readNextLine("Enter the new Note Title:  ");
                int priority = util.ScannerInput.readNextInt("Enter the new Note Priority:  ");
                String noteCategory = util.ScannerInput.readNextLine("Enter the new Note Category:  ");
                noteAPI.updateNote(indexNumber, noteTitle, priority, noteCategory);
            }

            if (!noteAPI.isValidIndex(indexNumber)) {
                System.out.println("This is not a valid index number.");
            }
        }
    }

    private void deleteNote() {
        noteAPI.listAllNotes();
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {

            int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to delete:");
            boolean isDeleted = false;
            if (noteAPI.isValidIndex(indexNumber)) {
                noteAPI.deleteNote(indexNumber);
                isDeleted = true;
            }
            if (isDeleted) {
                System.out.println("Note Deleted Successfully.");
            } else {
                System.out.println("No Note Deleted.");
            }
            if (!noteAPI.isValidIndex(indexNumber)) {
                System.out.println("This is not a valid index number.");
            }
        }
    }

    private void archiveNote() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }


        if (noteAPI.numberOfNotes() != 0) {

            if (noteAPI.numberOfActiveNotes() == 0) {
                System.out.println("There are no active notes!");
            }

            if (noteAPI.numberOfActiveNotes() != 0) {
                System.out.println("List of Active Notes:" +
                        noteAPI.listActiveNotes());

                int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to archive:");

                if (noteAPI.isValidIndex(indexNumber)) {
                    noteAPI.archiveNote(indexNumber);
                }

                if (!noteAPI.isValidIndex(indexNumber)) {
                    System.out.println("This is not a valid index number.");
                }
            }
        }
    }

    private void addItemToNote() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }

        if (noteAPI.numberOfNotes() != 0) {

            if (noteAPI.numberOfActiveNotes() == 0) {
                System.out.println("There are no active notes!");
            }

            if (noteAPI.numberOfActiveNotes() != 0) {
                System.out.println("List of Active Notes:" +
                        noteAPI.listActiveNotes());

                int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to add an item to:");

                if (noteAPI.isValidIndex(indexNumber)) {
                    String itemDescription = util.ScannerInput.readNextLine("Enter the Item Description: ");
                    boolean isAdded = noteAPI.findNote(indexNumber).addItem(new models.Item(itemDescription));
                    if (isAdded) {
                        System.out.println("Item Added Successfully");
                    } else {
                        System.out.println("No Item Added");
                    }
                }

                if (!noteAPI.isValidIndex(indexNumber)) {
                    System.out.println("This is not a valid index number.");
                }
            }
        }
    }

    private void updateItemDescInNote() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }

        if (noteAPI.numberOfNotes() != 0) {

            if (noteAPI.numberOfActiveNotes() == 0) {
                System.out.println("There are no active notes!");
            }

            if (noteAPI.numberOfActiveNotes() != 0) {
                System.out.println("List of Active Notes:" +
                        noteAPI.listActiveNotes());

                int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to update an item in:");

                if (noteAPI.isValidIndex(indexNumber)) {
                    if (noteAPI.findNote(indexNumber).numberOfItems() != 0) {
                        int itemIndexNumber = util.ScannerInput.readNextInt("Enter the index number of the item you wish to update: ");
                        String itemDescription = util.ScannerInput.readNextLine("Enter the new Item Description: ");
                        boolean status = noteAPI.findNote(indexNumber).findItem(itemIndexNumber).isItemCompleted();
                        boolean isAdded = noteAPI.findNote(indexNumber).updateItem(itemIndexNumber, itemDescription,status);
                        if (isAdded) {
                            System.out.println("Item Updated Successfully");
                        } else {
                            System.out.println("No Item Updated");
                        }
                    }

                    if (noteAPI.findNote(indexNumber).numberOfItems() == 0) {
                        System.out.println("This Note has no items.");
                    }
                }
                if (!noteAPI.isValidIndex(indexNumber)) {
                    System.out.println("This is not a valid index number.");
                }
            }
        }


    }

    private void deleteItemFromNote() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            if (noteAPI.numberOfActiveNotes() == 0) {
                System.out.println("There are no active notes!");
            }
            if (noteAPI.numberOfActiveNotes() != 0) {
                System.out.println("List of Active Notes:" +
                        noteAPI.listActiveNotes());
                int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to delete an item in:");
                if (noteAPI.isValidIndex(indexNumber)) {
                    if (noteAPI.findNote(indexNumber).numberOfItems() != 0) {
                        int itemIndexNumber = util.ScannerInput.readNextInt("Enter the index number of the item you wish to delete:");
                        boolean isDeleted = false;
                        if (noteAPI.findNote(indexNumber).isValidIndex(itemIndexNumber)) {
                            noteAPI.findNote(indexNumber).deleteItem(itemIndexNumber);
                            isDeleted = true;
                        }
                        if (isDeleted) {
                            System.out.println("Note Deleted Successfully.");
                        } else {
                            System.out.println("No Note Deleted.");
                        }
                        if (!noteAPI.isValidIndex(itemIndexNumber)) {
                            System.out.println("This is not a valid index number.");
                        }
                    }
                }

                if (noteAPI.findNote(indexNumber).numberOfItems() == 0) {
                    System.out.println("This Note has no items.");
                }
                if (!noteAPI.isValidIndex(indexNumber)) {
                    System.out.println("This is not a valid index number.");
                }
            }

        }
    }

    public void markCompletionOfItem() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            if (noteAPI.numberOfActiveNotes() == 0) {
                System.out.println("There are no active notes!");
            }
            if (noteAPI.numberOfActiveNotes() != 0) {
                System.out.println("List of Active Notes:" +
                        noteAPI.listActiveNotes());
                int indexNumber = util.ScannerInput.readNextInt("Enter the index number of the note you wish to change the status of an item in:");
                if (noteAPI.isValidIndex(indexNumber)) {
                    if (noteAPI.findNote(indexNumber).numberOfItems() != 0) {
                        int itemIndexNumber = util.ScannerInput.readNextInt("Enter the index number of the item you wish to mark as complete or todo:");
                        int change = 1;
                        if (noteAPI.findNote(indexNumber).isValidIndex(itemIndexNumber)) {
                            char statusChange = util.ScannerInput.readNextChar("Enter the new item status: y for complete or n for todo:)");
                            if (util.Utilities.YNtoBoolean(statusChange)) {
                                noteAPI.findNote(indexNumber).findItem(itemIndexNumber).setItemCompleted(true);
                                change = 2;
                            }
                            if (!util.Utilities.YNtoBoolean(statusChange)) {
                                noteAPI.findNote(indexNumber).findItem(itemIndexNumber).setItemCompleted(false);
                                change = 3;
                            }
                        }
                        if (change == 2) {
                            System.out.println("Item Status changed to Complete.");
                        } else if (change == 3) {
                            System.out.println("Item Status changed to TODO.");
                        }
                        if (!noteAPI.isValidIndex(indexNumber)) {
                            System.out.println("This is not a valid index number.");
                        }
                        if (change == 1) {
                            System.out.println("Item Status change was unsuccessful.");
                        }
                    }
                }

                if (noteAPI.findNote(indexNumber).numberOfItems() == 0) {
                    System.out.println("This Note has no items.");
                }
                if (!noteAPI.isValidIndex(indexNumber)) {
                    System.out.println("This is not a valid index number.");
                }
            }

        }
    }

    public void printActiveAndArchivedReport() {
        System.out.println(noteAPI.numberOfActiveNotes() + " Active Note(s):");
        printActiveNotes();
        System.out.println("-----------------------------------------");
        System.out.println(noteAPI.numberOfArchivedNotes() + "Archived Note(s):");
        printArchivedNotes();
    }

    public void archiveNotesWithAllItemsComplete() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            if (noteAPI.numberOfActiveNotes() == 0) {
                System.out.println("There are no active notes!");
            }
            if (noteAPI.numberOfActiveNotes() != 0) {
                noteAPI.archiveNotesWithAllItemsComplete();
            }
        }
    }

    public void printNotesBySelectedCategory() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            String category = util.ScannerInput.readNextLine("Enter the category of notes you want to see:");
            if (noteAPI.listNotesBySelectedCategory(category) == null) {
                System.out.println("No notes of that category stored yet.");
            }
            noteAPI.listNotesBySelectedCategory(category);
        }
    }

    public void printNotesByPriority() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            int priority = util.ScannerInput.readNextInt("Enter the priority of notes you want to see:");
            if (noteAPI.listNotesBySelectedPriority(priority) == null) {
                System.out.println("No notes of that priority stored yet.");
            }
            noteAPI.listNotesBySelectedPriority(priority);
        }
    }

    public void searchNotesByTitle() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            String title = util.ScannerInput.readNextLine("Enter the title of the note you want to see:");
            if (noteAPI.searchNotesByTitle(title) == null) {
                System.out.println("No note with that title stored currently.");
            }
            noteAPI.searchNotesByTitle(title);
        }
    }

    public void printAllTodoItems() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0) {
            noteAPI.listTodoItems();
            if (noteAPI.listTodoItems() == null) {
                System.out.println("No Todo Items!");
            }
        }
    }

    public void printOverallItemsTodoComplete() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0 && noteAPI.numberOfItems() == 0) {
            System.out.println("No Items Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0 && noteAPI.numberOfItems() != 0) {
            System.out.println("Number of todo items: " + noteAPI.numberOfTodoItems());
            System.out.println("Number of complete items: " + noteAPI.numberOfCompleteItems());
        }
    }

    public void printItemCompletionStatusByCategory() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0 && noteAPI.numberOfItems() == 0) {
            System.out.println("No Items Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0 && noteAPI.numberOfItems() != 0) {
            String category = util.ScannerInput.readNextLine("Enter the category of the note's which contain the items you want to see:");
            noteAPI.listItemStatusByCategory(category);
        }
    }

    public void searchItemsByDescription() {
        if (noteAPI.numberOfNotes() == 0) {
            System.out.println("No Notes Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0 && noteAPI.numberOfItems() == 0) {
            System.out.println("No Items Stored Yet");
        }
        if (noteAPI.numberOfNotes() != 0 && noteAPI.numberOfItems() != 0) {
            String description = util.ScannerInput.readNextLine("Enter the description of the item which you want to see:");
            noteAPI.searchItemByDescription(description);
        }
    }

    public void save() throws Exception {
        noteAPI.save();
    }

    public void load() throws Exception {
        noteAPI.load();
    }
}