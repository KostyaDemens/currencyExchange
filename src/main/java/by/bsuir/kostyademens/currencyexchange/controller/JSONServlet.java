package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.dao.CurrencyExchangeDao;
import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.model.Error;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class JSONServlet extends HttpServlet {

    protected CurrencyDao currencyDao = new CurrencyDao();
    protected CurrencyExchangeDao currencyExchangeDao = new CurrencyExchangeDao();
    protected ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

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
        if (method.equals("POST")) {
            resp.setStatus(201);
            this.doPost(req, resp);
        } else if (method.equals("GET")) {
            this.doGet(req, resp);
        }
    }
}
