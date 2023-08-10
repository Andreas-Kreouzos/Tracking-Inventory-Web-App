package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.InventoryException;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;

import java.util.List;

/**
 * IInventoryService is responsible for arranging the items inside the inventory
 */
public interface IInventoryService {

    /**
     * Returns a list of all items in the inventory
     * @return A new list containing all items in the inventory
     */
    List<Item> getItems();

    /**
     * Adds an item into inventory
     * @param item The item to add to the inventory
     * @throws InventoryException if the item is null or a duplicate
     */
    void addItem(Item item);

    /**
     * Removes the item with the provided serial number from the inventory
     * @param serialNumber the serial number of the item to remove
     * @throws InventoryException if an error occurs during removal
     * @throws ResourceNotFoundException if no item with the provided serial number exists
     */
    void deleteItem(String serialNumber);
}
