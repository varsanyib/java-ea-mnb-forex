package hu.gamf.springlectureproject.models;

public class OpenPositionDTO {
    private String instrument;
    private int units;

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}


