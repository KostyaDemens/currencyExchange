package by.bsuir.kostyademens.currencyexchange.utils;

import by.bsuir.kostyademens.currencyexchange.model.Error;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ErrorRenderer {
    public static void sendError(HttpServletResponse resp, int errorCode, String message) throws IOException {
        String json = new Gson().toJson(new Error(message));
        PrintWriter pw = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(errorCode);
        pw.print(json);
        pw.flush();

    }
}
