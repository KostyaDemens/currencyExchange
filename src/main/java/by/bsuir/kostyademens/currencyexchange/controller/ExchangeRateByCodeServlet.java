package by.bsuir.kostyademens.currencyexchange.controller;

import by.bsuir.kostyademens.currencyexchange.dao.ExchangeRateDao;
import by.bsuir.kostyademens.currencyexchange.exceptions.CurrencyNotFoundException;
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
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

        try {
            ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(path);
            JSONObject jsonObject = new JSONObject(exchangeRate);
            resp.setContentType("application/json");
            if (path.isEmpty()) {
                resp.sendError(400);
            } else {
                PrintWriter pw = resp.getWriter();
                pw.println(jsonObject);
            }
        } catch (CurrencyNotFoundException e) {
            resp.sendError(404);
            System.out.println(e.getMessage());
        }
    }


    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        String rate = req.getParameter("rate");

        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();


        try {
            if (rate != null) {
                ExchangeRate exchangeRate = exchangeRateDao.changeExchangeRate(path, Float.parseFloat(rate));
                JSONObject jsonObject = new JSONObject(exchangeRate);
                resp.setContentType("application/json");
                PrintWriter printWriter = resp.getWriter();
                printWriter.println(jsonObject);

            } else {
                resp.sendError(400);
            }
        } catch (CurrencyNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            this.doGet(req, resp);
        }
        this.doPatch(req, resp);
    }


}
