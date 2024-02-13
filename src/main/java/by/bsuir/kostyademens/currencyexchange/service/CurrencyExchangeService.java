package by.bsuir.kostyademens.currencyexchange.service;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.Exchange;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CurrencyExchangeService {
    public Currency baseCurrency;
    public Currency targetCurrency;
    public final CurrencyDao currencyDao = new CurrencyDao();
    public final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    public Exchange exchange = new Exchange();

    public ExchangeRate exchangeRate = new ExchangeRate();

    public Exchange directExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        mapValuesToCurrency(baseCurrencyCode, targetCurrencyCode, amount, baseCurrencyCode + targetCurrencyCode);
        exchange.setConvertedAmount(exchangeRate.getRate().multiply(amount));
        return exchange;
    }


    public Exchange inverseExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        mapValuesToCurrency(baseCurrencyCode, targetCurrencyCode, amount, targetCurrencyCode + baseCurrencyCode);
        exchange.setConvertedAmount(exchangeRate.getRate().divide(amount, 7, RoundingMode.DOWN));
        return exchange;
    }

    public Exchange crossRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);

        List<ExchangeRate> baseCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(baseCurrencyCode);
        List<ExchangeRate> targetCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(targetCurrencyCode);

        for (ExchangeRate baseRate : baseCurrencyList) {
            for (ExchangeRate targetRate : targetCurrencyList) {
                if (baseRate.getBaseCurrency().getId().equals(targetRate.getBaseCurrency().getId())) {
                    BigDecimal rate = targetRate.getRate().divide(baseRate.getRate(), 7, RoundingMode.DOWN);
                    exchange.setBaseCurrency(baseRate.getTargetCurrency());
                    exchange.setTargetCurrency(targetRate.getTargetCurrency());
                    exchange.setRate(rate);
                    exchange.setAmount(amount);
                    exchange.setConvertedAmount(rate.multiply(amount));
                    return exchange;
                }
            }
        }
        return exchange;
    }

    public Exchange chooseCurrencyConversionRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
        if (exchangeRateDao.isExchangeRateExists(baseCurrency.getId(), targetCurrency.getId())) {
            return directExchangeRate(baseCurrencyCode, targetCurrencyCode, amount);
        } else if (exchangeRateDao.isExchangeRateExists(targetCurrency.getId(), baseCurrency.getId())) {
            return inverseExchangeRate(baseCurrencyCode, targetCurrencyCode, amount);
        } else {
            return crossRate(baseCurrencyCode, targetCurrencyCode, amount);
        }

    }

    private void mapValuesToCurrency(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount, String path) {
        baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
        exchangeRate = exchangeRateDao.getExchangeRateByCode(path);
        exchange.setBaseCurrency(baseCurrency);
        exchange.setTargetCurrency(targetCurrency);
        exchange.setRate(exchangeRate.getRate());
        exchange.setAmount(amount);
    }
}
