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

        final String pathToFindExchangeRate = baseCurrencyCode + targetCurrencyCode;


        if (currencyDao.isCodeExists(baseCurrencyCode) && currencyDao.isCodeExists(targetCurrencyCode)) {
            try {
                ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(pathToFindExchangeRate);
                if (exchangeRateDao.isExchangeRatesExists(exchangeRate.getBaseCurrency().getId(), exchangeRate.getTargetCurrency().getId())) {
                    currencyExchange.setBaseCurrency(baseCurrency);
                    currencyExchange.setTargetCurrency(targetCurrency);
                    currencyExchange.setRate(exchangeRate.getRate());
                    currencyExchange.setAmount(amount);
                    currencyExchange.setConvertedAmount(exchangeRate.getRate() * amount);
                }
            } catch (CurrencyNotFoundException e) {
                System.out.println("We don't have such exchange rate");
            }

        }
        return currencyExchange;
    }
}
