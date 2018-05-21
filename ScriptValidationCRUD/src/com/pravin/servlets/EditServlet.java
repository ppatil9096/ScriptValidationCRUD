package com.pravin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.pravin.dblog4j.util.User;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(EditServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement("select * from registration.Users where id=?");
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            if (rs != null && rs.next()) {
                User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("email"), rs.getString("country"));
                request.setAttribute("user", user);
                RequestDispatcher view = request.getRequestDispatcher("/register.jsp");
                view.forward(request, response);
            }
        } catch (SQLException e) {
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
    }
}
