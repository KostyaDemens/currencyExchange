package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dto.ErrorDto;
import by.bsuir.kostyademens.currencyexchange.mapper.ModelMapper;
import by.bsuir.kostyademens.currencyexchange.service.CurrencyService;
import by.bsuir.kostyademens.currencyexchange.service.ExchangeRateService;
import by.bsuir.kostyademens.currencyexchange.service.CurrencyExchangeService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class JSONServlet extends HttpServlet {

    protected ModelMapper currencyMapper = new ModelMapper();
    protected CurrencyService currencyService = new CurrencyService();
    protected ExchangeRateService exchangeRateService = new ExchangeRateService();
    protected CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
    private static final int DEFAULT_SUCCESS_STATUS = 200;


    protected void sendResponse(HttpServletResponse resp, Object obj) throws IOException {
        sendResponse(resp, DEFAULT_SUCCESS_STATUS, obj);
    }

    protected void sendResponse(HttpServletResponse resp, int statusCode, Object object) throws IOException {
        send(resp, statusCode, object);
    }

    protected void sendError(HttpServletResponse resp, int errorCode, String message) throws IOException {
        send(resp, errorCode, new ErrorDto(message));
    }

    private void send(HttpServletResponse resp, int statusCode, Object obj) throws IOException {
        String json = new Gson().toJson(obj);
        PrintWriter pw = resp.getWriter();
        resp.setStatus(statusCode);
        pw.print(json);
        pw.flush();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(405, "Patch method not supported");
    }
}
