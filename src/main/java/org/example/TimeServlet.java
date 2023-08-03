package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    public void init() {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/Users/alexa/IdeaProjects/servlet_cookies/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String dateTime;
        resp.setContentType("text/html; charset=utf-8");
        String timezone = req.getParameter("timezone");
        String cookies = req.getHeader("Cookie");
        DateTimeFormatter zonedFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss Z");
        if (timezone != null) {
            dateTime = ZonedDateTime.now().
                    withZoneSameInstant(ZoneId.of(timezone.replace(" ", "+"))).
                    format(zonedFormatter);
            resp.setHeader("Set-Cookie","lastTimezone="+timezone);
        }else{
            if(cookies==null){
                dateTime = ZonedDateTime.now().format(zonedFormatter);
            }else{
                String[] separateCookies = cookies.split("=");
                timezone = separateCookies[1];
                dateTime = ZonedDateTime.now().
                        withZoneSameInstant(ZoneId.of(timezone.replace(" ", "+"))).
                        format(zonedFormatter);
            }
        }
        Context context = new Context(
                req.getLocale(),
                Map.of("time",dateTime));

        engine.process("time",context,resp.getWriter());
        resp.getWriter().close();
    }

}
