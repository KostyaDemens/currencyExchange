package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/currencies")
public class CurrenciesServlet extends JSONServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = currencyDao.getAllCurrencies();
        sendResponse(resp, currencies);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (code == null || name == null || sign == null) {
            sendError(resp, 400, "Отсутствует нужное поле формы");
            return;
        } else if (currencyDao.isCodeExists(code)) {
            sendError(resp, 409, "Валюта с таким кодом уже существует");
            return;
        }

        Currency currency = currencyDao.addCurrency(code, name, sign);

        sendResponse(resp, currency);


    }
}
