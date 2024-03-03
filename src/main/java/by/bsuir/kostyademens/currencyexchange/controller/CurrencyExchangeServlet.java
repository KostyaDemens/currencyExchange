package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dto.ExchangeDto;
import by.bsuir.kostyademens.currencyexchange.exception.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.exception.ExchangeRateNotFoundException;
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
            } else if (!amount.matches("\\d+(\\.\\d+)?")) {
                sendError(resp, 412, "Некорректное значение поля - " + amount);
                return;
            }

            ExchangeDto exchangeDto = currencyExchangeService.convertCurrency(baseCurrencyCode, targetCurrencyCode, new BigDecimal(amount));
            sendResponse(resp, exchangeDto);

        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюта не найдена");
            e.printStackTrace();
        } catch (ExchangeRateNotFoundException e) {
            sendError(resp, 404, "Валютной пары с таким кодом нету в базе данных");
            e.printStackTrace();
        }

    }
}
