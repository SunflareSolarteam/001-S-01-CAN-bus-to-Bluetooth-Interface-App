package nl.sunflare.sunflare_sensorviewer.ViewTypes;

public class MainscreenTitleTwoValues {

    private String title;
    private String valueOne = "...";
    private String valueTwo = "...";

    public MainscreenTitleTwoValues(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValueOne() {
        return valueOne;
    }

    public void setValueOne(String valueOne) {
        this.valueOne = valueOne;
    }

    public String getValueTwo() {
        return valueTwo;
    }

    public void setValueTwo(String valueTwo) {
        this.valueTwo = valueTwo;
    }
}
