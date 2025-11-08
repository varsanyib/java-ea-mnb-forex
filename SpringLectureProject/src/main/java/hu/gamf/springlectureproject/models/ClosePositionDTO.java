package hu.gamf.springlectureproject.models;

public class ClosePositionDTO {
    private int tradeId;

    public int getTradeId() {
        return tradeId;
    }
    public String getTradeIdInString() {
        return String.valueOf(tradeId);
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public ClosePositionDTO() {
    }
}
