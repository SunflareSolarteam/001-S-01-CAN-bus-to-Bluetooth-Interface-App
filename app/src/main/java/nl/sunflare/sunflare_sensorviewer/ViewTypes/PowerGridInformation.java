package nl.sunflare.sunflare_sensorviewer.ViewTypes;

public class PowerGridInformation {

    private String label;
    private String value = "...";
    private String type = "...";

    public PowerGridInformation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
