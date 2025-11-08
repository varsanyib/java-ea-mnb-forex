package hu.gamf.springlectureproject.models;

public class Currency {
    private String date;
    private Double currency;

    public Currency(String date, Double currency) {
        this.date = date;
        this.currency = currency;
    }

    public Currency(String date, String currency) {
        this.date = date;
        this.currency = Double.valueOf(currency.replace(",", "."));
    }

    public String getDate() {
        return date;
    }
    public void setDate(String Date) {
        this.date = Date;
    }

    public Double getCurrency() {
        return currency;
    }
    public void setCurrency(Double currency) {
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
