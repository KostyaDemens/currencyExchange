package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static by.bsuir.kostyademens.currencyexchange.utils.ObjectRenderer.rendererResponse;
import static by.bsuir.kostyademens.currencyexchange.utils.ErrorRenderer.sendError;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao currencyDao = new CurrencyDao();
        List<Currency> currencies = currencyDao.getAllCurrencies();

        rendererResponse(resp, currencies);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");
        CurrencyDao currencyDao = new CurrencyDao();


        if (code == null || name == null || sign == null) {
            sendError(resp, 400, "Отсутствует нужное поле формы");
            return;
        } else if (currencyDao.isCodeExists(code)) {
            sendError(resp,409, "Валюта с таким кодом уже существует");
            return;
        }

        Currency currency = currencyDao.addCurrency(code, name, sign);
        resp.setStatus(201);

        rendererResponse(resp, currency);


    }
}
