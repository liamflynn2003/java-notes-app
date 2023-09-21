package models;


import java.util.Objects;

/**
 * The Item class represents Item objects that can be contained in an Items array list in a Note.
 *It contains a description of the item, set my the user of the notes application,
 *and states whether the item is completed or to do.
 *
 * The methods set the item description to the specified lengths depending
 * on the situation and return true or false depending on the item's
 * to do or complete status.
 *
 * @author Liam FLynn
 * @version 1.0
 */
public class Item {

    private String itemDescription;
    private boolean isItemCompleted = false;
    /**
     *Takes in a description of the item and creates an item with that as its description.
     *
     * @param itemDescription The description of an individual item, a String of max 50 chars
     */
    public Item(String itemDescription) {
        setItemDescription(itemDescription);
    }

    /**
     * Takes in a description of the item and creates an item with that as its description.
     * Also takes a true or false value of whether the item is complete or not.
     *
     * @param itemDescription The description of an individual item, a String of max 50 chars
     * @param isItemCompleted A boolean which returns true if the item is completed, and false if not.
     */

    public Item(String itemDescription, boolean isItemCompleted) {
        setItemDescription(itemDescription);
        setItemCompleted(isItemCompleted);
    }

    /**
     * This setter method truncates any given item description String to 50 chars.
     * If this is an update to a description that already exists and the new String exceeds 50 chars then
     * no update will be made.
     *If no item description is given, then it will be saved as "No Description"
     * @param itemDescription The description of an individual item, a String of max 50 chars
     */
    public void setItemDescription(String itemDescription) {
        if(this.itemDescription==null) {
            this.itemDescription = util.Utilities.truncateString(itemDescription, 50);
        }
        if(itemDescription == null) {
            this.itemDescription = "No Description";
        }
        if(itemDescription!=null) {
            if (itemDescription.length() <= 50) {
                this.itemDescription = itemDescription;
            }
        }
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public boolean isItemCompleted() {
        return isItemCompleted;
    }

    public void setItemCompleted(boolean itemCompleted) {
        isItemCompleted = itemCompleted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return isItemCompleted == item.isItemCompleted && Objects.equals(itemDescription, item.itemDescription);
    }

    @Override

    /**
     * Generates a string that contains the item description and it's completion status
     * after checking if the item is complete or not and thus outputting
     * as COMPLETE or TODO.
     *
     * @return The item description and it's completion status as a string
     */

    public String toString() {
        String S;
        if (isItemCompleted) {
            S = "[Complete]";
        } else {
            S = "[TODO]";
        }
        return itemDescription + ". " + S;
    }
}
