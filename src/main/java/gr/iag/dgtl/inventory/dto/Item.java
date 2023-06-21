package gr.iag.dgtl.inventory.dto;

import java.math.BigDecimal;

public record Item(
        String name,
        String serialNumber,
        BigDecimal value) {
}
