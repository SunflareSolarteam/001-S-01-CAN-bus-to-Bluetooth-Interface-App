package nl.sunflare.sunflare_sensorviewer.ViewTypes;

public class MPPT {
    private String title;
    private String mppt_in_value_1 = "...";
    private String mppt_in_value_2 = "...";
    private String mppt_in_value_3 = "...";
    private String mppt_out_value = "...";
    private String mppt_temp_value = "...";

    public MPPT(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getMppt_in_value_1() {
        return mppt_in_value_1;
    }

    public void setMppt_in_value_1(String mppt_in_value_1) {
        this.mppt_in_value_1 = mppt_in_value_1;
    }

    public String getMppt_in_value_2() {
        return mppt_in_value_2;
    }

    public void setMppt_in_value_2(String mppt_in_value_2) {
        this.mppt_in_value_2 = mppt_in_value_2;
    }

    public String getMppt_in_value_3() {
        return mppt_in_value_3;
    }

    public void setMppt_in_value_3(String mppt_in_value_3) {
        this.mppt_in_value_3 = mppt_in_value_3;
    }

    public String getMppt_out_value() {
        return mppt_out_value;
    }

    public void setMppt_out_value(String mppt_out_value) {
        this.mppt_out_value = mppt_out_value;
    }

    public String getMppt_temp_value() {
        return mppt_temp_value;
    }

    public void setMppt_temp_value(String mppt_temp_value) {
        this.mppt_temp_value = mppt_temp_value;
    }
}
