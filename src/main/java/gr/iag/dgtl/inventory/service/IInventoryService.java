package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;

import java.util.List;
import java.util.Optional;

/**
 * IInventoryService is responsible for arranging the items inside the inventory
 */
public interface IInventoryService {

    /**
     * Through this method, a list of items gets returned
     * @return the List of Items
     */
    List<Item> getItems();

    /**
     * Adds an Item into inventory
     * @param item the item itself
     */
    void addItem(Item item);

    /**
     * Gets an Item from the inventory by using the serial number
     * @param serialNumber the serial number of the Item
     * @return an Optional of the Item
     */
    Optional<Item> getItemBySerialNumber(String serialNumber);

    /**
     * Deletes an Item from the inventory by using the serial number
     * @param serialNumber the serial number of the Item
     */
    void deleteItem(String serialNumber);
}
