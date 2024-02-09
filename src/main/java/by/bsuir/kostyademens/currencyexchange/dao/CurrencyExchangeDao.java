package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.exceptions.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.Exchange;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CurrencyExchangeDao {
    private static final CurrencyDao currencyDao = new CurrencyDao();
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();


    public Exchange exchangeCurrency(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        Exchange exchange = new Exchange();


        if (currencyDao.isCodeExists(baseCurrencyCode) && currencyDao.isCodeExists(targetCurrencyCode)) {

            Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
            Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);

            ExchangeRate exchangeRate;
            if (exchangeRateDao.isExchangeRateExists(baseCurrency.getId(), targetCurrency.getId())) {
                try {
                    exchangeRate = exchangeRateDao.getExchangeRateByCode(baseCurrencyCode + targetCurrencyCode);
                } catch (CurrencyNotFoundException e) {
                    throw new RuntimeException(e);
                }
                exchange.setBaseCurrency(baseCurrency);
                exchange.setTargetCurrency(targetCurrency);
                exchange.setRate(exchangeRate.getRate());
                exchange.setAmount(amount);
                exchange.setConvertedAmount(exchangeRate.getRate().multiply(amount));
            } else if (exchangeRateDao.isExchangeRateExists(targetCurrency.getId(), baseCurrency.getId())) {
                exchangeRate = exchangeRateDao.getExchangeRateByCode(targetCurrencyCode + baseCurrencyCode);
                exchange.setBaseCurrency(baseCurrency);
                exchange.setTargetCurrency(targetCurrency);
                exchange.setRate(exchangeRate.getRate());
                exchange.setAmount(amount);
                exchange.setConvertedAmount(exchangeRate.getRate().divide(amount, 7, RoundingMode.DOWN));
            } else {
                    BigDecimal rate;
                    List<ExchangeRate> baseCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(baseCurrencyCode);
                    List<ExchangeRate> targetCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(targetCurrencyCode);

                    for (ExchangeRate baseRate : baseCurrencyList) {
                        for (ExchangeRate targetRate : targetCurrencyList) {
                            if (baseRate.getBaseCurrency().getId().equals(targetRate.getBaseCurrency().getId())) {
                                rate = targetRate.getRate().divide(baseRate.getRate(), 7, RoundingMode.DOWN);
                                exchange.setBaseCurrency(baseRate.getTargetCurrency());
                                exchange.setTargetCurrency(targetRate.getTargetCurrency());
                                exchange.setRate(rate);
                                exchange.setAmount(amount);
                                exchange.setConvertedAmount(rate.multiply(amount));
                                return exchange;
                            }
                        }
                    }
                }
            }
        return exchange;
    }
}
