package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.exception.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.exception.DuplicateExchangeRateException;
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
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        sendResponse(resp, exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("base_code");
        String targetCurrencyCode = req.getParameter("target_code");
        String rate = req.getParameter("rate");


        try {
            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
                sendError(resp, 400, "Отсутствует нужное поле формы");
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
            sendResponse(resp, exchangeRate);
        } catch (DuplicateExchangeRateException e) {
            sendError(resp, 409, "Такая валютная пара уже существует");
            e.printStackTrace();
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюты с таким кодом нету в базе данных");
            e.printStackTrace();
        }
    }
}
