package gr.iag.dgtl.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * Contains the input fields of an Item request.
 */
public class Item {

    @NotBlank
    private String name;
    @NotBlank
    private String serialNumber;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal value;

    public Item() {
    }

    public Item(String name,
                String serialNumber,
                BigDecimal value) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


}
