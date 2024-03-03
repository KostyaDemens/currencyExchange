package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.exception.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.exception.DuplicateCurrencyException;
import by.bsuir.kostyademens.currencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.dao.JDBCConnector.connection;


public class CurrencyDao {

    public List<Currency> getAllCurrencies() {
        String SQL = "SELECT * FROM currencies";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL);
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                Currency currency = new Currency();

                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setName(resultSet.getString("fullName"));

                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency getCurrencyByCode(String code) {
        String SQL = "SELECT * FROM currencies WHERE code = ?";

        Currency currency = new Currency();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setName(resultSet.getString("fullName"));
            } else {
                throw new CurrencyNotFoundException("Currency with such code is not exists");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }

    public Currency addCurrency(String code, String fullName, String sign) {
        String SQL = "INSERT INTO currencies (code, fullName, sign) VALUES (?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, code);
            statement.setString(2, fullName);
            statement.setString(3, sign);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Currency currency = new Currency();
                    long id = generatedKeys.getLong(1);
                    currency.setCode(code);
                    currency.setName(fullName);
                    currency.setSign(sign);
                    currency.setId(id);
                    return currency;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new DuplicateCurrencyException("Currency with such code already exists");
            } else {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Currency getCurrencyById(long id) {
        String SQL = "SELECT * FROM currencies WHERE id = ?";
        Currency currency = new Currency();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setSign(resultSet.getString("sign"));
                currency.setName(resultSet.getString("fullName"));
            } else {
                throw new CurrencyNotFoundException("Currency with such id is not exists");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }
}
