package by.bsuir.kostyademens.currencyexchange.service;

import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService extends ExchangeRateDao {

    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.getAllExchangeRates();
    }

    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        return exchangeRateDao.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

    public ExchangeRate getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) {
        return exchangeRateDao.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
    }

    public ExchangeRate changeExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        return exchangeRateDao.changeExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

}
