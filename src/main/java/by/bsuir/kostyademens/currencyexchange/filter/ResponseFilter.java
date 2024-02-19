package by.bsuir.kostyademens.currencyexchange.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;


import java.io.IOException;

@WebFilter(filterName = "ResponseFilter", urlPatterns = {"/*"})
public class ResponseFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        chain.doFilter(request, response);
    }

}
