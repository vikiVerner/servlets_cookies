package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        resp.setContentType("text/html; charset=utf-8");

        String timezone = req.getParameter("timezone");

        DateTimeFormatter zonedFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss Z");

        String dateTime = ZonedDateTime.now().format(zonedFormatter);

        if (timezone != null) {
            dateTime = ZonedDateTime.now().
                    withZoneSameInstant(ZoneId.of(timezone.replace(" ", "+"))).
                    format(zonedFormatter);
        }

        resp.getWriter().write(dateTime);
        resp.getWriter().close();
    }


}
