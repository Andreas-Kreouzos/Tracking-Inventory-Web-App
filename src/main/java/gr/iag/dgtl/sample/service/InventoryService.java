package gr.iag.dgtl.sample.service;

import gr.iag.dgtl.sample.dto.Item;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryService implements IInventoryService {

    @Override
    public List<Item> getItems() {
        return null;
    }

    @Override
    public void addItem(String name, String serialNumber, BigDecimal value) {

    }

    @Override
    public Optional<Item> getItemBySerialNumber(String serialNumber) {
        return Optional.empty();
    }

    @Override
    public void deleteItem(String serialNumber) {

    }
}
