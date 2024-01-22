package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static by.bsuir.kostyademens.currencyexchange.dao.JDBCConnector.connection;


public class CurrencyDao {

    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM currencies";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Currency currency = new Currency();

                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setFullName(resultSet.getString("fullName"));

                currencies.add(currency);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }

    public Currency getCurrencyByCode(String code) {
        Currency currency = new Currency();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?");
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setFullName(resultSet.getString("fullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

    public Currency addCurrency(String code, String fullName, String sign) {
        Currency currency = new Currency();
        PreparedStatement statement;
        try {
            String sql = "INSERT INTO currencies (code, fullName, sign) VALUES (?,?,?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, code);
            statement.setString(2, fullName);
            statement.setString(3, sign);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException();
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    currency.setCode(code);
                    currency.setFullName(fullName);
                    currency.setSign(sign);
                    currency.setId(id);

                } else {
                    throw new SQLException();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

    public boolean isCodeExists(String code) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM currencies WHERE code = ?")) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
