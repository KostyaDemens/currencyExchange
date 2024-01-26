package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.model.Currency;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.dao.JDBCConnector.connection;

public class CurrencyExchangeDao {

    public List<ExchangeRate> getListOfExchangeRates() {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        String SQL = "SELECT exch.id, cur.id , cur.code, cur.fullname, cur.sign, cur2.id AS target_id, cur2.code AS target_code, cur2.fullname AS target_fullname, cur2.sign AS target_sign, exch.rate\n" +
                "FROM exchangerates exch\n" +
                "INNER JOIN currencies cur ON exch.basecurrencyid = cur.id\n" +
                "INNER JOIN currencies cur2 ON exch.targetcurrencyid = cur2.id;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            while (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                Currency baseCurrency = new Currency();
                Currency targetCurrency = new Currency();

                exchangeRate.setId(resultSet.getLong("id"));

                exchangeRate.setRate(resultSet.getFloat("rate"));

                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);

                baseCurrency.setId(resultSet.getLong("id"));
                baseCurrency.setCode(resultSet.getString("code"));
                baseCurrency.setSign(resultSet.getString("sign"));
                baseCurrency.setFullName(resultSet.getString("fullname"));

                targetCurrency.setId(resultSet.getLong("target_id"));
                targetCurrency.setCode(resultSet.getString("target_code"));
                targetCurrency.setSign(resultSet.getString("target_sign"));
                targetCurrency.setFullName(resultSet.getString("target_fullname"));


                exchangeRateList.add(exchangeRate);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRateList;

    }

    public ExchangeRate getExchangeRateByCode(String path) {
        String baseCurrencyCode = path.substring(0, 3);
        String targetCurrencyCode = path.substring(3, 6);
        CurrencyDao currencyDao = new CurrencyDao();
        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
        String SQL = "SELECT * FROM exchangerates WHERE basecurrencyid = ? AND targetcurrencyid = ?";
        ExchangeRate exchangeRate = new ExchangeRate();

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setLong(1, baseCurrency.getId());
            statement.setLong(2, targetCurrency.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exchangeRate.setId(resultSet.getLong("id"));
                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);
                exchangeRate.setRate(resultSet.getFloat("rate"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }


    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, float rate) {
        ExchangeRate exchangeRate = new ExchangeRate();
        CurrencyDao currencyDao = new CurrencyDao();
        Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);

        if (isExchangeRatesExists(baseCurrency.getId(), targetCurrency.getId())) {

        }

        String SQL = "INSERT INTO exchangerates (basecurrencyid, targetcurrencyid, rate) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, baseCurrency.getId());
            preparedStatement.setLong(2, targetCurrency.getId());
            preparedStatement.setFloat(3, rate);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException();
            }

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
            e.printStackTrace();
        }
        return exchangeRate;
    }

    public boolean isExchangeRatesExists(long baseCurrencyId, long targetCurrencyId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM exchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?")) {
            preparedStatement.setLong(1, baseCurrencyId);
            preparedStatement.setLong(2, targetCurrencyId);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
