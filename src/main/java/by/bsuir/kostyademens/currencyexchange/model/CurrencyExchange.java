package by.bsuir.kostyademens.currencyexchange.model;

public class CurrencyExchange {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private float rate;
    private float amount;
    private float convertedAmount;

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(float convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public CurrencyExchange(Currency baseCurrency, Currency targetCurrency, float rate, float amount, float convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyExchange() {
    }
}
