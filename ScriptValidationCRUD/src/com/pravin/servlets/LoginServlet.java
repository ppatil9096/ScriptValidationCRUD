package com.pravin.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.pravin.dblog4j.util.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String errorMsg = null;

        if (email == null || email.equals("")) {
            errorMsg = "Email could not be Empty";
        }
        if (password == null || password.equals("")) {
            errorMsg = "Password could not be Empty";
        }

        if (errorMsg != null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.html");
            PrintWriter printWriter = response.getWriter();
            printWriter.println("<font color=red >" + errorMsg + "</font>");
            dispatcher.include(request, response);
        } else {

            Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                preparedStatement = connection.prepareStatement("select id,name,email,password,country from registration.Users where email=? and password=? limit 1 ");
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();

                if (resultSet != null && resultSet.next()) {
                    User user = new User(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("country"));
                    logger.info("User found with details :: " + user);
                    HttpSession httpSession = request.getSession();
                    httpSession.setAttribute("User", user);
                    List<User> list = new LoginServlet().getAllUsers(connection);
                    request.setAttribute("users", list);
                    RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
                    rd.forward(request, response);
                } else {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.html");
                    PrintWriter printWriter = response.getWriter();
                    logger.info("User not found with email :: " + email);
                    printWriter.println("<font color=red > No User Found with this email, please register first </font>");
                    dispatcher.include(request, response);
                }
            } catch (SQLException e) {
                logger.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            }
        }
    }

    public List<User> getAllUsers(Connection connection) throws ServletException {
        List<User> users = new ArrayList<User>();

        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement("select * from registration.Users");
            rs = preparedStatement.executeQuery();
            while (null != rs && rs.next()) {
                User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("email"), rs.getString("country"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        return users;
    }

}
