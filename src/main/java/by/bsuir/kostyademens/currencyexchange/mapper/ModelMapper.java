package by.bsuir.kostyademens.currencyexchange.mapper;

import by.bsuir.kostyademens.currencyexchange.dto.CurrencyDto;
import by.bsuir.kostyademens.currencyexchange.dto.ExchangeRateDto;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;


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
        exchangeRateDto.setBaseCurrency(getCurrencyDTO(exchangeRate.getBaseCurrency()));
        exchangeRateDto.setTargetCurrency(getCurrencyDTO(exchangeRate.getTargetCurrency()));
        exchangeRateDto.setRate(exchangeRate.getRate());
        return exchangeRateDto;
    }

}
