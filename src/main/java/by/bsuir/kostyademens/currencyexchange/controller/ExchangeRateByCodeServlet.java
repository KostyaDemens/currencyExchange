package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyExchangeDao;
import by.bsuir.kostyademens.currencyexchange.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchangeRate/*")
public class ExchangeRateByCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        CurrencyExchangeDao currencyExchangeDao = new CurrencyExchangeDao();
        ExchangeRate exchangeRate = currencyExchangeDao.getExchangeRateByCode(path);
        JSONObject jsonObject = new JSONObject(exchangeRate);
        resp.setContentType("application/json");
        if (path.isEmpty()) {
            resp.sendError(400);
        } else if (exchangeRate.getId() == 0) {
            resp.sendError(404);
        } else {
            PrintWriter pw = resp.getWriter();
            pw.println(jsonObject);

        }
    }
}
