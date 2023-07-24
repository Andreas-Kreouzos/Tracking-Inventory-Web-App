package gr.iag.dgtl.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Objects;

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

    /**
     * Indicates whether some other object is "equal to" this one
     *
     * @param o The reference object with which to compare
     * @return {@code true} if this object is the same as the object argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return serialNumber.equals(item.serialNumber);
    }

    /**
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", value=" + value +
                '}';
    }
}
