package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.exceptions.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.exceptions.DuplicateExchangeRateException;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends JSONServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAllExchangeRates();
        sendResponse(resp, exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            sendError(resp, 400, "Отсутствует нужное поле формы");
            return;
        }


        try {
            ExchangeRate exchangeRate = exchangeRateDao.addExchangeRate(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
            sendResponse(resp, exchangeRate);
        } catch (DuplicateExchangeRateException e) {
            sendError(resp, 409, "Такая валютная пара уже существует");
            e.printStackTrace();
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюта не найдена");
            e.printStackTrace();
        }
    }
}
