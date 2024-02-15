package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.exception.CurrencyNotFoundException;
import by.bsuir.kostyademens.currencyexchange.exception.ExchangeRateNotFoundException;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet("/exchangeRate/*")
public class ExchangeRateByCodeServlet extends JSONServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);

        try {
            if (path.isEmpty()) {
                sendError(resp, 400, "Валютная пара отстутсвтует в адресе");
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCode(path);
            sendResponse(resp, currencyMapper.getExchangeRateDTO(exchangeRate));
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюты с таким кодом нету в базе данных");
            e.printStackTrace();
        } catch (ExchangeRateNotFoundException e) {
            sendError(resp, 404, "Валютной пары с таким кодом нету в базе данных");
            e.printStackTrace();
        }

    }


    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        String rate = req.getParameter("rate");


        try {
            if (rate == null) {
                sendError(resp, 400, "Отсутствует нужное поле формы");
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.changeExchangeRate(path, new BigDecimal(rate));
            sendResponse(resp, currencyMapper.getExchangeRateDTO(exchangeRate));
        } catch (ExchangeRateNotFoundException e) {
            sendError(resp, 404, "Валютной пары с таким кодом нету в базе данных");
            e.printStackTrace();
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 404, "Валюты с таким кодом нету в базе данных");
            e.printStackTrace();
        }

    }

}
