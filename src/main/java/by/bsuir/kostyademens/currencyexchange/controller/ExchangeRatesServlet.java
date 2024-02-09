package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.exceptions.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.exceptions.DuplicateExchangeRateException;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.utils.ObjectRenderer.rendererResponse;
import static by.bsuir.kostyademens.currencyexchange.utils.ErrorRenderer.sendError;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAllExchangeRates();
        rendererResponse(resp, exchangeRates);
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

        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        try {
            ExchangeRate exchangeRate = exchangeRateDao.addExchangeRate(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
            resp.setStatus(201);
            rendererResponse(resp, exchangeRate);
        } catch (DuplicateExchangeRateException e) {
            sendError(resp, 409, "Такая валютная пара уже существует");
            System.out.println(e.getMessage());
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюта не найдена");
            System.out.println(e.getMessage());
        }
    }
}
