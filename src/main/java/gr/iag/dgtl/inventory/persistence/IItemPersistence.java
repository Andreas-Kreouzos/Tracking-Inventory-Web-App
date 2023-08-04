package gr.iag.dgtl.inventory.persistence;

import gr.iag.dgtl.inventory.dto.Item;

import java.util.List;

/**
 * Defines the contract for the persistence of items
 *
 * @author kreouzosa
 */
public interface IItemPersistence {
    /**
     * Loads items from a JSON, HTML or CSV file
     *
     * @return a list of items, which can be empty if no items are found
     */
    List<Item> loadItems();

    /**
     * Saves the given list of items to JSON, HTML or CSV file
     *
     * @param items the list of items to be saved
     */
    void saveItems(List<Item> items);
}
