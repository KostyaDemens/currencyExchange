package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.bsuir.kostyademens.currencyexchange.utils.ObjectRenderer.rendererResponse;
import static by.bsuir.kostyademens.currencyexchange.utils.ErrorRenderer.sendError;

@WebServlet("/currency/*")
public class CurrencyByCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        CurrencyDao currencyDao = new CurrencyDao();


        if (path.isEmpty()) {
            sendError(resp, 400, "Код валюты отсутствует в адресе");
        } else if (!currencyDao.isCodeExists(path)) {
            sendError(resp, 404, "Валюта не найдена");
        } else {
            Currency currency = currencyDao.getCurrencyByCode(path);
            rendererResponse(resp, currency);
        }
    }
}
