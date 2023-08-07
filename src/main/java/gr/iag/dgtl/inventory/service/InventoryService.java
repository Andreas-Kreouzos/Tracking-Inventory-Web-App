package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.InventoryException;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;
import gr.iag.dgtl.inventory.mapper.InventoryExceptionMapper;
import gr.iag.dgtl.inventory.persistence.IItemPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class InventoryService implements IInventoryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InventoryService.class);

    private final IItemPersistence htmlPersistence;
    private final IItemPersistence jsonPersistence;
    private final IItemPersistence csvPersistence;
    private final List<Item> items;

    @Inject
    public InventoryService(@Named("HtmlItemPersistence") IItemPersistence htmlPersistence,
                            @Named("JsonItemPersistence") IItemPersistence jsonPersistence,
                            @Named("CsvItemPersistence") IItemPersistence csvPersistence) {
        this.htmlPersistence = htmlPersistence;
        this.jsonPersistence = jsonPersistence;
        this.csvPersistence = csvPersistence;
        this.items = new ArrayList<>();
        this.items.addAll(htmlPersistence.loadItems());
        this.items.addAll(jsonPersistence.loadItems());
        this.items.addAll(csvPersistence.loadItems());
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public void addItem(Item item) {
        try {
            if (item == null) {
                throw new InventoryException("Cannot add null item to inventory");
            }

            if(findItemBySerialNumber(item.getSerialNumber()) != null) {
                throw new InventoryException("Item with this serial number already exists in inventory");
            }

            items.add(item);
            jsonPersistence.saveItems(items);
            htmlPersistence.saveItems(items);
            csvPersistence.saveItems(items);
        } catch (Exception ex) {
            LOGGER.error("Error upon adding an Item with serialNumber={}", item.getSerialNumber(), ex);
            throw new InventoryException(InventoryExceptionMapper.DEFAULT_MESSAGE, ex);
        }
    }

    private Item findItemBySerialNumber(String serialNumber) {
        for (Item item : items) {
            if (item.getSerialNumber().equals(serialNumber)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Item getItemBySerialNumber(String serialNumber) {
        Item item = findItemBySerialNumber(serialNumber);
        if (item == null) {
            throw new ResourceNotFoundException("Item with serial number: " + serialNumber + " not found");
        }
        return item;
    }

    @Override
    public void deleteItem(String serialNumber) {
        try {
            Item item = getItemBySerialNumber(serialNumber);
            items.remove(item);
            htmlPersistence.saveItems(items);
            jsonPersistence.saveItems(items);
            csvPersistence.saveItems(items);
        } catch (Exception ex) {
            LOGGER.error("Error upon deleting an Item with serialNumber={}", serialNumber, ex);
            throw new InventoryException(InventoryExceptionMapper.DEFAULT_MESSAGE, ex);
        }
    }
}
