package hu.gamf.springlectureproject.models;

public class Currency {
    private String date;
    private String currency;

    public Currency(String date, String currency) {
        this.date = date;
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String Date) {
        this.date = Date;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "date='" + date + '\'' +
                ", currency=" + currency +
                '}';
    }
}
