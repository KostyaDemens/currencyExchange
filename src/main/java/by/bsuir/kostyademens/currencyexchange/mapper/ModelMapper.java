package by.bsuir.kostyademens.currencyexchange.mapper;

import by.bsuir.kostyademens.currencyexchange.dto.CurrencyDto;
import by.bsuir.kostyademens.currencyexchange.dto.ExchangeDto;
import by.bsuir.kostyademens.currencyexchange.dto.ExchangeRateDto;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ModelMapper {
    public CurrencyDto getCurrencyDTO(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getId());
        currencyDto.setCode(currency.getCode());
        currencyDto.setName(currency.getName());
        currencyDto.setSign(currency.getSign());
        return currencyDto;
    }

    public ExchangeRateDto getExchangeRateDTO(ExchangeRate exchangeRate) {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setId(exchangeRate.getId());
        exchangeRateDto.setBaseCurrency(exchangeRate.getBaseCurrency());
        exchangeRateDto.setTargetCurrency(exchangeRate.getTargetCurrency());
        exchangeRateDto.setRate(exchangeRate.getRate());
        return exchangeRateDto;
    }

    public ExchangeDto getExchangeDTO(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setBaseCurrency(baseCurrency);
        exchangeDto.setTargetCurrency(targetCurrency);
        exchangeDto.setRate(rate.setScale(6, RoundingMode.HALF_EVEN));
        exchangeDto.setAmount(amount.setScale(6, RoundingMode.HALF_EVEN));
        exchangeDto.setConvertedAmount(convertedAmount.setScale(6, RoundingMode.HALF_EVEN));
        return exchangeDto;
    }
}
