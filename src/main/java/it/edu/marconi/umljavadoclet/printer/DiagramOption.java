package it.edu.marconi.umljavadoclet.printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiagramOption {
    public DiagramOption(String name, int length) {
        this.name = name;
        this.length = length;
        this.validValues = null;
        this.defaultValue = null;
    }

    public DiagramOption(String name, String validValues, String defaultValue, int length) {
        this.name = name;
        this.validValues = new ArrayList<>();
        String[] parts = validValues.split(",");
        for (String part : parts) {
            this.validValues.add(part.trim());
        }
        this.defaultValue = defaultValue;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public List<String> getValidValues() {
        return validValues;
    }

    public boolean isValidValue(String value) {
        return validValues == null || validValues.contains(value);
    }

    public int getLength() {
        return length;
    }

    public String getValue() {
        return value != null ? value : defaultValue;
    }

    public List<String> getCsvValues() {
        List<String> values = new ArrayList<>();
        String value = getValue();
        if (value != null && value.length() > 0) {
            String[] parts = value.split(",");
            Collections.addAll(values, parts);
        }
        return values;
    }

    public void setValue(String value) {
        if (validValues != null) {
            for (String validValue : validValues) {
                if (validValue.equalsIgnoreCase(value)) {
                    this.value = validValue;
                    return;
                }
            }
        } else {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "DiagramOption{" +
                "name='" + name + '\'' +
                ", validValues=" + validValues +
                ", defaultValue='" + defaultValue + '\'' +
                ", length=" + length +
                ", value='" + value + '\'' +
                '}';
    }

    private final String name;
    private final List<String> validValues;
    private final String defaultValue;
    private final int length;
    private String value;
}