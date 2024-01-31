package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.exceptions.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.CurrencyExchange;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

public class CurrencyExchangeDao {
    private static final CurrencyDao currencyDao = new CurrencyDao();
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();


    public CurrencyExchange exchangeCurrency(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        CurrencyExchange currencyExchange = new CurrencyExchange();

        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);


        if (currencyDao.isCodeExists(baseCurrencyCode) && currencyDao.isCodeExists(targetCurrencyCode)) {
            ExchangeRate exchangeRate;
            if (exchangeRateDao.isExchangeRatesExists(baseCurrency.getId(), targetCurrency.getId())) {
                try {
                    exchangeRate = exchangeRateDao.getExchangeRateByCode(baseCurrencyCode + targetCurrencyCode);
                } catch (CurrencyNotFoundException e) {
                    throw new RuntimeException(e);
                }
                currencyExchange.setBaseCurrency(baseCurrency);
                currencyExchange.setTargetCurrency(targetCurrency);
                currencyExchange.setRate(exchangeRate.getRate());
                currencyExchange.setAmount(amount);
                currencyExchange.setConvertedAmount(exchangeRate.getRate() * amount);
            } else {
                try {
                    exchangeRate = exchangeRateDao.getExchangeRateByCode(targetCurrencyCode + baseCurrencyCode);
                } catch (CurrencyNotFoundException e) {
                    throw new RuntimeException();
                }
                currencyExchange.setBaseCurrency(baseCurrency);
                currencyExchange.setTargetCurrency(targetCurrency);
                currencyExchange.setRate(exchangeRate.getRate());
                currencyExchange.setAmount(amount);
                currencyExchange.setConvertedAmount(exchangeRate.getRate() / amount);
            }
        }
        return currencyExchange;
    }
}
