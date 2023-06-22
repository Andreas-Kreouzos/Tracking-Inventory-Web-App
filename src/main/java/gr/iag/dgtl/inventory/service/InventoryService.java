package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryService implements IInventoryService {

    private final List<Item> items = new ArrayList<>();

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public Optional<Item> getItemBySerialNumber(String serialNumber) {
        return Optional.empty();
    }

    @Override
    public void deleteItem(String serialNumber) {

    }
}
