package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyExchangeDao;

import by.bsuir.kostyademens.currencyexchange.model.Exchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static by.bsuir.kostyademens.currencyexchange.utils.ObjectRenderer.rendererResponse;
import static by.bsuir.kostyademens.currencyexchange.utils.ErrorRenderer.sendError;

@WebServlet("/exchange/*")
public class CurrencyExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
            sendError(resp,400, "Отсутствует нужное поле формы");
            return;
        }

        CurrencyExchangeDao currencyExchangeDao = new CurrencyExchangeDao();
        Exchange exchange = currencyExchangeDao.exchangeCurrency(baseCurrencyCode, targetCurrencyCode, BigDecimal.valueOf(Long.parseLong(amount)));

        rendererResponse(resp, exchange);


    }
}
