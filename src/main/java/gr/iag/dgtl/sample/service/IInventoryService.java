package gr.iag.dgtl.sample.service;

import gr.iag.dgtl.sample.dto.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * IInventoryService is responsible for arranging the items inside the inventory
 */
public interface IInventoryService {

    List<Item> getItems();
    void addItem(String name, String serialNumber, BigDecimal value);
    Optional<Item> getItemBySerialNumber(String serialNumber);
    void deleteItem(String serialNumber);
}
