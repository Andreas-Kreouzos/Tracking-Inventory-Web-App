package gr.iag.dgtl.inventory.persistence;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Defines the contract for the persistence of items
 * @author kreouzosa
 */
public interface IItemPersistence {
    /**
     * Loads items from a JSON, HTML or CSV file
     * @return a list of all items, which can be empty if no items are found
     */
    List<Item> loadItems();

    /**
     * Loads an item from a JSON, HTML or CSV file by using its serial number
     * @param serialNumber the serial number of the item to retrieve
     * @return the item with the provided serial number
     * @throws ResourceNotFoundException if no item with the provided serial number exists in files.
     */
    Item getItemBySerialNumber(String serialNumber);

    /**
     * Saves the given list of items to JSON, HTML or CSV file
     * @param items the list of items to be saved
     */
    void saveItems(List<Item> items);
}
