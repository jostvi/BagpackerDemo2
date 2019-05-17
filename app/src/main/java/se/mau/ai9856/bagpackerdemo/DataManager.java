package se.mau.ai9856.bagpackerdemo;

public class DataManager {
    private String startData;
    private String stopData;

    public DataManager(){

    }
    public void setStartData (String startData) {
        this.startData = startData;
    }

    public void setStopData (String stopData) {
        this.stopData = stopData;
    }

    public String getStartData(){
        return this.startData;
    }

    public String getStopData(){
        return this.stopData;
    }


}
