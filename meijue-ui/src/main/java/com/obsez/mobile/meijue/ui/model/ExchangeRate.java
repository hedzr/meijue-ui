package com.obsez.mobile.meijue.ui.model;

import java.math.BigDecimal;

public class ExchangeRate {

    private String from;
    private String to;
    private BigDecimal rate;
    private Integer timestamp;

    // For tests only
    public ExchangeRate(final String from, final String to, final BigDecimal rate, final Integer timestamp) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

}
