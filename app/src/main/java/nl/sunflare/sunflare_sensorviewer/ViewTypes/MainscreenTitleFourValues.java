package nl.sunflare.sunflare_sensorviewer.ViewTypes;

public class MainscreenTitleFourValues {

    private String title;
    private String bigValue= "...";
    private String smallValueOne = "...";
    private String smallValueTwo= "...";
    private String smallValueThree= "...";

    public MainscreenTitleFourValues(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBigValue() {
        return bigValue;
    }

    public void setBigValue(String bigValue) {
        this.bigValue = bigValue;
    }

    public String getSmallValueOne() {
        return smallValueOne;
    }

    public void setSmallValueOne(String smallValueOne) {
        this.smallValueOne = smallValueOne;
    }

    public String getSmallValueTwo() {
        return smallValueTwo;
    }

    public void setSmallValueTwo(String smallValueTwo) {
        this.smallValueTwo = smallValueTwo;
    }

    public String getSmallValueThree() {
        return smallValueThree;
    }

    public void setSmallValueThree(String smallValueThree) {
        this.smallValueThree = smallValueThree;
    }
}
