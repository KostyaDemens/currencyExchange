package by.bsuir.kostyademens.currencyexchange.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ObjectRenderer {

    public static void rendererResponse(HttpServletResponse resp, Object object) throws IOException {
        String json = new Gson().toJson(object);
        PrintWriter pw = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        pw.print(json);
        pw.flush();
    }
}
