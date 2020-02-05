package nl.sunflare.sunflare_sensorviewer.ViewTypes;

public class PowerGridCell {
    private String title;
    private String powergrid_in_value_1 = "...";
    private String powergrid_in_value_2 = "...";

    public PowerGridCell(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getPowergrid_in_value_1() {
        return powergrid_in_value_1;
    }

    public void setPowergrid_in_value_1(String powergrid_in_value_1) {
        this.powergrid_in_value_1 = powergrid_in_value_1;
    }

    public String getPowergrid_in_value_2() {
        return powergrid_in_value_2;
    }

    public void setPowergrid_in_value_2(String powergrid_in_value_2) {
        this.powergrid_in_value_2 = powergrid_in_value_2;
    }
}
