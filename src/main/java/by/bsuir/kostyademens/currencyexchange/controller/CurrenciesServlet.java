package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
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

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao currencyDao = new CurrencyDao();
        List<Currency> currencies = currencyDao.getAllCurrencies();
        JSONArray jsonArray = new JSONArray(currencies);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String fullName = req.getParameter("fullName");
        String sign = req.getParameter("sign");

        CurrencyDao currencyDao = new CurrencyDao();


        if (code == null || fullName == null || sign == null) {
            resp.sendError(400, "Отсутствует нужное поле формы");
            return;
        } else if (currencyDao.isCodeExists(code)) {
            resp.sendError(409, "Валюта с таким кодом уже существует");
            return;
        }

        Currency currency = currencyDao.addCurrency(code, fullName, sign);

        JSONObject jsonObject = new JSONObject(currency);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonObject);



    }
}
