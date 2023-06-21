package gr.iag.dgtl.sample;

import java.io.Serializable;

public class SampleDto implements Serializable {
    private String value;

    public SampleDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
