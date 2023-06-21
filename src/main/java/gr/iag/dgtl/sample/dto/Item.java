package gr.iag.dgtl.sample.dto;

import java.math.BigDecimal;

public record Item(
        String name,
        String serialNumber,
        BigDecimal value) {
}
