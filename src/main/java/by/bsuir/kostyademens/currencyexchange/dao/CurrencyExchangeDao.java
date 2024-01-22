package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.dao.JDBCConnector.connection;

public class CurrencyExchangeDao {

    public List<ExchangeRate> getListOfExchangeRates() {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM exchangerates";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getLong("id"));
                exchangeRate.setBaseCurrencyId(resultSet.getLong("baseCurrencyId"));
                exchangeRate.setTargetCurrencyId(resultSet.getLong("targetCurrencyId"));
                exchangeRate.setRate(resultSet.getFloat("rate"));
                exchangeRateList.add(exchangeRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRateList;

    }
}
