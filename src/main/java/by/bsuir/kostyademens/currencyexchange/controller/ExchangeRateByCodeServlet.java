package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.exceptions.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static by.bsuir.kostyademens.currencyexchange.utils.ObjectRenderer.rendererResponse;
import static by.bsuir.kostyademens.currencyexchange.utils.ErrorRenderer.sendError;

@WebServlet("/exchangeRate/*")
public class ExchangeRateByCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        CurrencyDao currencyDao = new CurrencyDao();

        if (path.isEmpty()) {
            sendError(resp,400, "Валютная пара отстутсвтует в адресе");
            return;
        } else if (!currencyDao.isCodeExists(path.substring(0, 3)) || !currencyDao.isCodeExists(path.substring(3, 6))) {
            sendError(resp, 500, "Валюта с кодом: " + path.substring(0, 3) + " или с кодом: " + path.substring(3, 6) + " не существует");
            return;
        } else if (!exchangeRateDao.isExchangeRateExists(currencyDao.getCurrencyByCode(path.substring(0, 3)).getId(), currencyDao.getCurrencyByCode(path.substring(3, 6)).getId())) {
            sendError(resp,404, "Такой валютной пары не существует");
            return;
        }

        try {
            ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(path);
            rendererResponse(resp, exchangeRate);

        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюта не найдена");
            System.out.println(e.getMessage());
        }

    }


    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        String rate = req.getParameter("rate");

        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        CurrencyDao currencyDao = new CurrencyDao();

        if (rate == null) {
            sendError(resp,400, "Отсутствует нужное поле формы");
            return;
        } else if (!exchangeRateDao.isExchangeRateExists(currencyDao.getCurrencyByCode(path.substring(0, 3)).getId(), currencyDao.getCurrencyByCode(path.substring(3, 6)).getId())) {
            sendError(resp,404, "Такой валютной пары не существует");
            return;
        }

        try {
            ExchangeRate exchangeRate = exchangeRateDao.changeExchangeRate(path, new BigDecimal(rate));
            rendererResponse(resp, exchangeRate);
        } catch (CurrencyNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        } else {
            this.doGet(req, resp);
        }

    }


}
