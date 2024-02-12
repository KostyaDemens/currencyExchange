package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyExchangeDao;

import by.bsuir.kostyademens.currencyexchange.model.Exchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;



@WebServlet("/exchange/*")
public class CurrencyExchangeServlet extends JSONServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
            sendError(resp,400, "Отсутствует нужное поле формы");
            return;
        }

        Exchange exchange = currencyExchangeDao.exchangeCurrency(baseCurrencyCode, targetCurrencyCode, BigDecimal.valueOf(Long.parseLong(amount)));

        sendResponse(resp, exchange);


    }
}
