package by.bsuir.kostyademens.currencyexchange.dao;

import by.bsuir.kostyademens.currencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CurrencyDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
}
