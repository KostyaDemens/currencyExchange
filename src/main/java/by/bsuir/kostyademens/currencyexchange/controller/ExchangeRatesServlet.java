package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dto.ExchangeRateDto;
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
import java.util.stream.Collectors;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends JSONServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        List<ExchangeRateDto> exchangeRateDto = exchangeRates.stream()
                .map(currencyMapper::getExchangeRateDTO)
                .collect(Collectors.toList());
        sendResponse(resp, exchangeRateDto);
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
            } else if (!rate.matches("\\d+(\\.\\d+)?")) {
                sendError(resp, 412, "Некорректное значение поля - " + rate);
                return;
            } else if (baseCurrencyCode.equals(targetCurrencyCode)) {
                sendError(resp, 412, "Две одинаковые валюты - " + baseCurrencyCode + "!");
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
            sendResponse(resp, 201, currencyMapper.getExchangeRateDTO(exchangeRate));
        } catch (DuplicateExchangeRateException e) {
            sendError(resp, 409, "Такая валютная пара уже существует");
            e.printStackTrace();
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюты с таким кодом нету в базе данных");
            e.printStackTrace();
        }
    }
}
