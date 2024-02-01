package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/hi/*")
public class TestClass extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);

        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        List<ExchangeRate> exchangeRateList = exchangeRateDao.exchangeRateByTargetCurrency(path);

        JSONObject jsonObject = new JSONObject(exchangeRateList);
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();

        pw.println(jsonObject);
    }
}
