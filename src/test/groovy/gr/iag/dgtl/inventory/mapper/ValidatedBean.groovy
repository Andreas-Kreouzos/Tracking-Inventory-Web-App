package gr.iag.dgtl.inventory.mapper

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ValidatedBean {
    @NotBlank
    private String blank;

    @NotNull
    private Integer id;

    ValidatedBean() {
    }
}
