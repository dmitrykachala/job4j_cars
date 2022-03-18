package ru.job4j.cars.servlet;

import ru.job4j.cars.repository.AdRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeStatusServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        AdRepository store = AdRepository.getInstance();

            store.update(Integer.valueOf(req.getParameter("ad")));
    }
}
