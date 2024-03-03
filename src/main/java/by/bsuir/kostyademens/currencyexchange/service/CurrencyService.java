package by.bsuir.kostyademens.currencyexchange.service;


import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.model.Currency;

import java.util.List;

public class CurrencyService extends CurrencyDao {

    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<Currency> getAllCurrencies() {
        return currencyDao.getAllCurrencies();
    }

    public Currency addCurrency(String code, String name, String sign) {
        return currencyDao.addCurrency(code, name, sign);
    }

    public Currency getCurrencyByCode(String path) {
        return currencyDao.getCurrencyByCode(path);
    }
}
