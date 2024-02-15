package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dto.ExchangeDto;
import by.bsuir.kostyademens.currencyexchange.exception.CurrencyNotFoundException;
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

            ExchangeDto exchangeDto = currencyExchangeService.chooseCurrencyConversionRate(baseCurrencyCode, targetCurrencyCode, BigDecimal.valueOf(Long.parseLong(amount)));
            if (exchangeDto.getRate() == null) {
                sendError(resp, 404, "Валютной пары с таким кодом нету в базе данных");
            } else {
                sendResponse(resp, currencyMapper.getExchangeDTO(exchangeDto.getBaseCurrency(), exchangeDto.getTargetCurrency(), exchangeDto.getRate(), exchangeDto.getAmount(), exchangeDto.getConvertedAmount()));
            }

        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюта не найдена");
            e.printStackTrace();
        }


    }
}
