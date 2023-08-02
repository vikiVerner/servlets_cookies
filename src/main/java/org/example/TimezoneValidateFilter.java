package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        if(request.getParameter("timezone")!=null){
            String timezone = request.getParameter("timezone");
            if(isValidTimezone(timezone.replace(" ","+"))){
                chain.doFilter(request, response);
            }else{
                response.setStatus(400);
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write("Invalid timezone");
                response.getWriter().close();
            }
        }else{
            chain.doFilter(request, response);
        }
    }

    private static boolean isValidTimezone(String timezone) {
        if (timezone.matches("^UTC[+-](1[0-4]|[0-9])$")) {
            String numericOffset = timezone.substring(4);
            try {
                int offsetValue = Integer.parseInt(numericOffset);
                ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(offsetValue));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
