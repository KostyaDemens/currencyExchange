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
            } else if (!path.matches("^[A-Z]{6}$")) {
                sendError(resp, 412, "Некорректное значение поля - " + path);
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCode(path.substring(0, 3), path.substring(3, 6));
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
        String code = req.getPathInfo().substring(1);
        String rate = req.getParameter("rate");


        try {
            if (code.isEmpty()) {
                sendError(resp, 400, "Валютная пара отсутствует в адресе");
                return;
            } else if (!code.matches("^[A-Z]{6}$")) {
                sendError(resp, 412, "Некорректное значение поля - " + code);
                return;
            } else if (rate == null) {
                sendError(resp, 400, "Отстутствует нужное поле формы");
                return;
            } else if (!rate.matches("\\d+(\\.\\d+)?")) {
                sendError(resp, 412, "Некорректное значение поля - " + rate);
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.changeExchangeRate(code.substring(0, 3), code.substring(3, 6), new BigDecimal(rate));
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
