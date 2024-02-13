package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.model.Error;
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

    protected CurrencyService currencyService = new CurrencyService();
    protected ExchangeRateService exchangeRateService = new ExchangeRateService();
    protected CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();

    protected void sendResponse(HttpServletResponse resp, Object object) throws IOException {
        String json = new Gson().toJson(object);
        PrintWriter pw = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        pw.print(json);
        pw.flush();
    }

    protected void sendError(HttpServletResponse resp, int errorCode, String message) throws IOException {
        String json = new Gson().toJson(new Error(message));
        PrintWriter pw = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(errorCode);
        pw.print(json);
        pw.flush();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        switch (method) {
            case "POST":
                resp.setStatus(201);
                this.doPost(req, resp);
                break;
            case "GET":
                this.doGet(req, resp);
                break;
            case "PATCH":
                this.doPatch(req, resp);
                break;
            default:
                super.service(req, resp);
                break;
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(405, "Patch method not supported");
    }
}
