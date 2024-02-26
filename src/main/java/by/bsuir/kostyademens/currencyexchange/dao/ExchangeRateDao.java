package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.exception.*;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.dao.JDBCConnector.connection;

public class ExchangeRateDao {

    private static ExchangeRate exchangeRate = new ExchangeRate();
    private static final CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRate> getAllExchangeRates() {
        String SQL = "SELECT exch.id, cur.id AS base_id, cur.code, cur.fullname, cur.sign, cur2.id AS target_id, cur2.code AS target_code, cur2.fullname AS target_fullname, cur2.sign AS target_sign, exch.rate " +
                "FROM exchangerates exch " +
                "INNER JOIN currencies cur ON exch.basecurrencyid = cur.id " +
                "INNER JOIN currencies cur2 ON exch.targetcurrencyid = cur2.id;";

        try (Statement statement = connection.createStatement()) {
            List<ExchangeRate> exchangeRateList = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                exchangeRate = new ExchangeRate();
                Currency baseCurrency = new Currency();
                Currency targetCurrency = new Currency();

                exchangeRate.setId(resultSet.getLong("id"));

                exchangeRate.setRate(resultSet.getBigDecimal("rate"));

                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);

                baseCurrency.setId(resultSet.getLong("base_id"));
                baseCurrency.setCode(resultSet.getString("code"));
                baseCurrency.setSign(resultSet.getString("sign"));
                baseCurrency.setName(resultSet.getString("fullname"));

                targetCurrency.setId(resultSet.getLong("target_id"));
                targetCurrency.setCode(resultSet.getString("target_code"));
                targetCurrency.setSign(resultSet.getString("target_sign"));
                targetCurrency.setName(resultSet.getString("target_fullname"));

                exchangeRateList.add(exchangeRate);
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ExchangeRate getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) {
        String SQL = "SELECT * FROM exchangerates WHERE basecurrencyid = ? AND targetcurrencyid = ?";

        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setLong(1, baseCurrency.getId());
            statement.setLong(2, targetCurrency.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exchangeRate.setId(resultSet.getLong("id"));
                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);
                exchangeRate.setRate(resultSet.getBigDecimal("rate"));
            } else {
                throw new ExchangeRateNotFoundException("Exchange rate is not exists");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRate;
    }


    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String SQL = "INSERT INTO exchangerates (basecurrencyid, targetcurrencyid, rate) VALUES (?,?,?)";

        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, baseCurrency.getId());
            preparedStatement.setLong(2, targetCurrency.getId());
            preparedStatement.setBigDecimal(3, rate);

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    exchangeRate.setId(id);
                    exchangeRate.setBaseCurrency(baseCurrency);
                    exchangeRate.setTargetCurrency(targetCurrency);
                    exchangeRate.setRate(rate);
                }
            }


        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new DuplicateExchangeRateException("Currency with such code already exists");
            } else {
                throw new RuntimeException(e);
            }
        }
        return exchangeRate;
    }

    public boolean isExchangeRateExists(long baseCurrencyId, long targetCurrencyId) {
        String SQL = "SELECT * FROM exchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setLong(1, baseCurrencyId);
            preparedStatement.setLong(2, targetCurrencyId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ExchangeRate changeExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String SQL = "UPDATE exchangeRates SET rate = ? WHERE id = ?";

        ExchangeRate exchangeRate = getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setBigDecimal(1, rate);
            statement.setLong(2, exchangeRate.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                exchangeRate.setRate(rate);
            } else {
                throw new ExchangeRateNotFoundException("Exchange rate is not exists");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRate;
    }

    public List<ExchangeRate> getExchangeRatesByCurrency(String path) {
        String SQL = "SELECT * FROM exchangerates WHERE targetcurrencyid = ?";

        Currency targetCurrency = currencyDao.getCurrencyByCode(path);

        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setLong(1, targetCurrency.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getLong("id"));
                exchangeRate.setTargetCurrency(targetCurrency);
                exchangeRate.setRate(resultSet.getBigDecimal("rate"));
                exchangeRate.setBaseCurrency(currencyDao.getCurrencyById(resultSet.getLong("basecurrencyid")));
                exchangeRates.add(exchangeRate);
            }

            if (exchangeRates.isEmpty()) {
                throw new ExchangeRateNotFoundException("Exchange rate is not exists");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRates;
    }
}
