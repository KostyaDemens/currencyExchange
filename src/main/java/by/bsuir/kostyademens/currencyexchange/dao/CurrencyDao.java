package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.dao.JDBCConnector.connection;


public class CurrencyDao {

    public List<Currency> getAllCurrencies() {
        String SQL = "SELECT * FROM currencies";
        List<Currency> currencies = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            while (resultSet.next()) {
                Currency currency = new Currency();

                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setFullName(resultSet.getString("fullName"));

                currencies.add(currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return currencies;
    }

    public Currency getCurrencyByCode(String code) throws RuntimeException {
        Currency currency = new Currency();
        String SQL = "SELECT * FROM currencies WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setFullName(resultSet.getString("fullName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return currency;
    }

    public Currency addCurrency(String code, String fullName, String sign) {
        Currency currency = new Currency();
        String SQL = "INSERT INTO currencies (code, fullName, sign) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

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
            throw new RuntimeException();
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
            throw new RuntimeException();
        }
        return false;
    }

    public Currency getCurrencyById(long id) {
        Currency currency = new Currency();
        String SQL = "SELECT * FROM currencies WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setFullName(resultSet.getString("fullName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return currency;
    }
}
