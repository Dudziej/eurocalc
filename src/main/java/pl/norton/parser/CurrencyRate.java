package pl.norton.parser;

import java.math.BigDecimal;

public class CurrencyRate {

    private final String currency;
    private final BigDecimal rate;

    public CurrencyRate(String currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
