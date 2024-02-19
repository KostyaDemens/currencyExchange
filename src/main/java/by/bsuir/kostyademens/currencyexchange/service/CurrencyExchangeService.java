package by.bsuir.kostyademens.currencyexchange.service;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.dto.ExchangeDto;
import by.bsuir.kostyademens.currencyexchange.mapper.ModelMapper;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CurrencyExchangeService {
    private final ModelMapper mapper = new ModelMapper();
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private ExchangeRate exchangeRate = new ExchangeRate();

    private ExchangeDto directExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        ExchangeDto exchangeDto = mapValuesToCurrency(baseCurrencyCode, targetCurrencyCode, amount, baseCurrencyCode + targetCurrencyCode);
        exchangeDto.setConvertedAmount(exchangeRate.getRate().multiply(amount));
        return exchangeDto;
    }


    private ExchangeDto inverseExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        ExchangeDto exchangeDto = mapValuesToCurrency(baseCurrencyCode, targetCurrencyCode, amount, targetCurrencyCode + baseCurrencyCode);
        exchangeDto.setConvertedAmount(exchangeRate.getRate().divide(amount, 7, RoundingMode.DOWN));
        return exchangeDto;
    }

    private ExchangeDto crossExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        ExchangeDto exchangeDto = new ExchangeDto();

        List<ExchangeRate> baseCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(baseCurrencyCode);
        List<ExchangeRate> targetCurrencyList = exchangeRateDao.getExchangeRatesByCurrency(targetCurrencyCode);

        for (ExchangeRate baseRate : baseCurrencyList) {
            for (ExchangeRate targetRate : targetCurrencyList) {
                if (baseRate.getBaseCurrency().getId().equals(targetRate.getBaseCurrency().getId())) {
                    BigDecimal rate = targetRate.getRate().divide(baseRate.getRate(), 7, RoundingMode.DOWN);
                    exchangeDto.setBaseCurrency(mapper.getCurrencyDTO(baseRate.getTargetCurrency()));
                    exchangeDto.setTargetCurrency(mapper.getCurrencyDTO(targetRate.getTargetCurrency()));
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
        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
        if (exchangeRateDao.isExchangeRateExists(baseCurrency.getId(), targetCurrency.getId())) {
            return directExchangeRate(baseCurrencyCode, targetCurrencyCode, amount);
        } else if (exchangeRateDao.isExchangeRateExists(targetCurrency.getId(), baseCurrency.getId())) {
            return inverseExchangeRate(targetCurrencyCode, baseCurrencyCode, amount);
        } else {
            return crossExchangeRate(baseCurrencyCode, targetCurrencyCode, amount);
        }

    }

    private ExchangeDto mapValuesToCurrency(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount, String path) {
        ExchangeDto exchangeDto = new ExchangeDto();
        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
        exchangeRate = exchangeRateDao.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
        exchangeDto.setBaseCurrency(mapper.getCurrencyDTO(baseCurrency));
        exchangeDto.setTargetCurrency(mapper.getCurrencyDTO(targetCurrency));
        exchangeDto.setRate(exchangeRate.getRate());
        exchangeDto.setAmount(amount);
        return exchangeDto;
    }
}
