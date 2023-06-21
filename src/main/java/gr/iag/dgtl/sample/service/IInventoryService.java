package gr.iag.dgtl.sample.service;

import gr.iag.dgtl.sample.dto.Item;

import java.math.BigDecimal;
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
     * @param name the name of the Item
     * @param serialNumber the serial number of the Item
     * @param value the value in $ of the Item
     */
    void addItem(String name, String serialNumber, BigDecimal value);

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
