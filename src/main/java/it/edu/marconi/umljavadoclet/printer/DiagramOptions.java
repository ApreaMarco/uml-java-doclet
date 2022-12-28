package it.edu.marconi.umljavadoclet.printer;

import it.edu.marconi.umljavadoclet.model.ModelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiagramOptions {
    public DiagramOptions() {
        addOption(LINETYPE, "polyline,spline,ortho", "ortho");
        addOption(DEPENDENCIES, "public,protected,package,private", "public");
        addOption(OUTPUT_MODEL, "true,false", "false");
        addOption(PUML_INCLUDE_FILE);
        addOption(EXCLUDE_CLASSES);
        addOption(EXCLUDE_PACKAGES);
    }

    private static final String LINETYPE = "linetype";
    private static final String DEPENDENCIES = "dependencies";
    private static final String OUTPUT_MODEL = "output-model";
    private static final String PUML_INCLUDE_FILE = "puml-include-file";
    private static final String EXCLUDE_CLASSES = "exclude-classes";
    private static final String EXCLUDE_PACKAGES = "exclude-packages";

    public enum LineType {SPLINE, POLYLINE, ORTHO}

    public enum Visibility {PUBLIC, PROTECTED, PACKAGE, PRIVATE}

    public LineType getLineType() {
        return LineType.valueOf(getOptionEnumValue(LINETYPE));
    }

    public Visibility getDependenciesVisibility() {
        return Visibility.valueOf(getOptionEnumValue(DEPENDENCIES));
    }

    public boolean isOutputModel() {
        return Objects.equals(getOptionValue(OUTPUT_MODEL), "true");
    }

    public String getPumlIncludeFile() {
        return getOptionValue(PUML_INCLUDE_FILE);
    }

    public boolean hasPumlIncludeFile() {
        return getPumlIncludeFile() != null && getPumlIncludeFile().length() > 0;
    }

    public List<String> getExcludedClasses() {
        return getOptionCsvValue(EXCLUDE_CLASSES);
    }

    public boolean isExcludedClass(ModelClass modelClass) {
        return getExcludedClasses().contains(modelClass.fullNameWithoutParameters());
    }

    public List<String> getExcludedPackages() {
        return getOptionCsvValue(EXCLUDE_PACKAGES);
    }

    public boolean isExcludedPackage(ModelClass modelClass) {
        return getExcludedPackages().contains(modelClass.packageName());
    }

    public void set(String[][] docletOptions) {
        for (String[] docletOption : docletOptions) {
            String docletName = docletOption[0];
            DiagramOption option = getOptionForDocletName(docletName);
            if (option != null) {
                String docletValue = docletOption[1];
                option.setValue(docletValue);
            }
        }
    }

    public boolean isValidOption(String docletName) {
        return getOptionForDocletName(docletName) != null;
    }

    public int getOptionLength(String docletName) {
        DiagramOption option = getOptionForDocletName(docletName);
        return option != null ? option.getLength() : 0;
    }

    public String checkOption(String[] setting) {
        String docletName = setting[0];
        DiagramOption option = getOptionForDocletName(docletName);
        if (option == null) {
            return "Invalid option " + docletName;
        }
        String value = setting[1];
        if (!option.isValidValue(value)) {
            return "Invalid value " + value + " for option " + docletName + "; valid values are " + option.getValidValues();
        }
        return null;
    }

    public String getOptionValuesAsString() {
        StringBuilder result = new StringBuilder();
        for (DiagramOption option : options) {
            result.append(option.getName()).append("=").append(option.getValue()).append(" ");
        }
        return result.toString();
    }

    private void addOption(String name) {
        DiagramOption option = new DiagramOption(name, 2);
        options.add(option);
    }

    private void addOption(String name, String validValues, String defaultValue) {
        DiagramOption option = new DiagramOption(name, validValues, defaultValue, 2);
        options.add(option);
    }

    private DiagramOption getOption(String name) {
        for (DiagramOption option : options) {
            if (option.getName().equals(name)) {
                return option;
            }
        }
        return null;
    }

    private DiagramOption getOptionForDocletName(String nameWithHyphen) {
        String name = nameWithHyphen.substring(1);
        return getOption(name);
    }

    private String getOptionValue(String name) {
        DiagramOption option = getOption(name);
        return option != null ? option.getValue() : null;
    }

    private List<String> getOptionCsvValue(String name) {
        DiagramOption option = getOption(name);
        return option != null ? option.getCsvValues() : new ArrayList<>();
    }

    private String getOptionEnumValue(String name) {
        String value = getOptionValue(name);
        return value != null ? value.toUpperCase().replace("-", "_") : null;
    }

    @Override
    public String toString() {
        return "DiagramOptions{" +
                "options=" + options +
                '}';
    }

    private final List<DiagramOption> options = new ArrayList<>();
}