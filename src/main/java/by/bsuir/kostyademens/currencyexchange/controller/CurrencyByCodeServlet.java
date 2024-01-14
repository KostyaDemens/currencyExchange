package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.CurrencyDao;
import by.bsuir.kostyademens.currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyByCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        CurrencyDao currencyDao = new CurrencyDao();
        Currency currency = currencyDao.getCurrencyByCode(path);
        JSONObject jsonObject = new JSONObject(currency);
        resp.setContentType("application/json");
        if (path.isEmpty()) {
            resp.sendError(400);
        } else if (jsonObject.isEmpty()) {
            resp.sendError(404);
        } else {
            PrintWriter out = resp.getWriter();
            out.println(jsonObject);
        }
    }
}

