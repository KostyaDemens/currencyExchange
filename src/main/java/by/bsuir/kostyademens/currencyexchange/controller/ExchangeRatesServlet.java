package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyExchangeDao;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyExchangeDao currencyExchangeDao = new CurrencyExchangeDao();
        List<ExchangeRate> exchangeRates = currencyExchangeDao.getListOfExchangeRates();
        JSONArray jsonArray = new JSONArray(exchangeRates);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");

            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
                resp.sendError(400);
                return;
            }

            CurrencyExchangeDao currencyExchangeDao = new CurrencyExchangeDao();
            ExchangeRate exchangeRate = currencyExchangeDao.addExchangeRate(baseCurrencyCode, targetCurrencyCode, Float.parseFloat(rate));

            JSONObject jsonObject = new JSONObject(exchangeRate);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println(jsonObject);
        }
}
