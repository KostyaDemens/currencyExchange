package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dto.CurrencyDto;
import by.bsuir.kostyademens.currencyexchange.exception.DuplicateCurrencyException;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/currencies")
public class CurrenciesServlet extends JSONServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = currencyService.getAllCurrencies();
        List<CurrencyDto> currencyDto = currencies.stream()
                .map(currencyMapper::getCurrencyDTO)
                .collect(Collectors.toList());
        sendResponse(resp, currencyDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");


        try {
            if (code == null || name == null || sign == null) {
                sendError(resp, 400, "Отсутствует нужное поле формы");
                return;
            } else if (!code.matches("^[A-Z]{3}$")) {
                sendError(resp, 412, "Некорректное значение поля - " + code);
                return;
            }
            Currency currency = currencyService.addCurrency(code, name, sign);
            sendResponse(resp, 201, currencyMapper.getCurrencyDTO(currency));
        } catch (DuplicateCurrencyException e) {
            sendError(resp, 409, "Валюта с таким кодом уже существует");
            e.printStackTrace();
        }
    }
}
