package by.bsuir.kostyademens.currencyexchange.service;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.dto.ExchangeDto;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CurrencyExchangeService {
    public Currency baseCurrency;
    public Currency targetCurrency;
    public final CurrencyDao currencyDao = new CurrencyDao();
    public final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    public ExchangeDto exchangeDto = new ExchangeDto();

    public ExchangeRate exchangeRate = new ExchangeRate();

    public ExchangeDto directExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        mapValuesToCurrency(baseCurrencyCode, targetCurrencyCode, amount, baseCurrencyCode + targetCurrencyCode);
        exchangeDto.setConvertedAmount(exchangeRate.getRate().multiply(amount));
        return exchangeDto;
    }


    public ExchangeDto inverseExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        mapValuesToCurrency(baseCurrencyCode, targetCurrencyCode, amount, targetCurrencyCode + baseCurrencyCode);
        exchangeDto.setConvertedAmount(exchangeRate.getRate().divide(amount, 7, RoundingMode.DOWN));
        return exchangeDto;
    }

    public ExchangeDto crossRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);

        List<ExchangeRate> baseCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(baseCurrencyCode);
        List<ExchangeRate> targetCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(targetCurrencyCode);

        for (ExchangeRate baseRate : baseCurrencyList) {
            for (ExchangeRate targetRate : targetCurrencyList) {
                if (baseRate.getBaseCurrency().getId().equals(targetRate.getBaseCurrency().getId())) {
                    BigDecimal rate = targetRate.getRate().divide(baseRate.getRate(), 7, RoundingMode.DOWN);
                    exchangeDto.setBaseCurrency(baseRate.getTargetCurrency());
                    exchangeDto.setTargetCurrency(targetRate.getTargetCurrency());
                    exchangeDto.setRate(rate);
                    exchangeDto.setAmount(amount);
                    exchangeDto.setConvertedAmount(rate.multiply(amount));
                    return exchangeDto;
                }
            }
        }
        return exchangeDto;
    }

    public ExchangeDto chooseCurrencyConversionRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
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
        exchangeDto.setBaseCurrency(baseCurrency);
        exchangeDto.setTargetCurrency(targetCurrency);
        exchangeDto.setRate(exchangeRate.getRate());
        exchangeDto.setAmount(amount);
    }
}
