package com.pravin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(DeleteServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("delete from Users where id=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        request.setAttribute("users", new LoginServlet().getAllUsers(connection));
        RequestDispatcher view = request.getRequestDispatcher("/home.jsp");
        view.forward(request, response);
    }

}
