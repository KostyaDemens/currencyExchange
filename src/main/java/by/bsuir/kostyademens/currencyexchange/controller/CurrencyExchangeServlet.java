package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyExchangeDao;

import by.bsuir.kostyademens.currencyexchange.model.CurrencyExchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchange/*")
public class CurrencyExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
            resp.sendError(400, "Отсутствует нужное поле формы");
            return;
        }

        CurrencyExchangeDao currencyExchangeDao = new CurrencyExchangeDao();
        CurrencyExchange currencyExchange = currencyExchangeDao.exchangeCurrency(baseCurrencyCode, targetCurrencyCode, Float.parseFloat(amount));

        resp.setContentType("application/json");
        JSONObject jsonObject = new JSONObject(currencyExchange);
        PrintWriter out = resp.getWriter();
        out.println(jsonObject);


    }
}
