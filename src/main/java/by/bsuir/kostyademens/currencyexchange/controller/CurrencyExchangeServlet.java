package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.exception.CurrencyNotFoundException;
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


        try {
            if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
                sendError(resp, 400, "Отсутствует нужное поле формы");
                return;
            }

            Exchange exchange = currencyExchangeService.chooseCurrencyConversionRate(baseCurrencyCode, targetCurrencyCode, BigDecimal.valueOf(Long.parseLong(amount)));
            if (exchange.getRate() == null) {
                sendError(resp, 404, "Валютной пары с таким кодом нету в базе данных");
            } else {
                sendResponse(resp, exchange);
            }

        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюта не найдена");
            e.printStackTrace();
        }


    }
}
