package com.contoso.orders;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            throw new ServletException("주문 테이블을 초기화하지 못했습니다.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("orders", dao.findAll());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOG.error("주문 조회 실패", e);
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
            LOG.info("주문 접수: " + customer + " / " + item + " x " + quantity);
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (SQLException e) {
            LOG.error("주문 저장 실패", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
