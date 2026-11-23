package com.contoso.orders;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(urlPatterns = "/orders")
public class OrderServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(OrderServlet.class);
    private final OrderDao dao = new OrderDao();

    @Override
    public void init() throws ServletException {
        try {
            dao.init();
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize the orders table.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("orders", dao.findAll());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOG.error("Failed to load orders", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String customer = req.getParameter("customer");
        String item = req.getParameter("item");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        try {
            dao.save(new Order(customer, item, quantity));
            LOG.info("Order received: " + customer + " / " + item + " x " + quantity);
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (SQLException e) {
            LOG.error("Failed to save the order", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
