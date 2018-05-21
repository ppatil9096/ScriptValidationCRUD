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

import org.apache.log4j.Logger;

import com.pravin.dblog4j.util.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(RegisterServlet.class);

    LoginServlet loginServlet = new LoginServlet();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        String id = "";
        String errorMsg = null;

        if (email == null || email.equals("")) {
            errorMsg = "Email ID/ ";
        }
        if (password == null || password.equals("")) {
            errorMsg = errorMsg + "Password/ ";
        }
        if (name == null || name.equals("")) {
            errorMsg = errorMsg + "Name/ ";
        }
        if (country == null || country.equals("")) {
            errorMsg = errorMsg + "Country ";
        }
        System.out.println(errorMsg);

        if (errorMsg != null) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "Can't be empty" + "</font>");
            rd.include(request, response);
        } else {
            User user = new User(id, name, password, email, country);
            Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
            new RegisterServlet().addUser(user, connection);
            logger.info("User registered/added with email=" + email);
            // forward to login page to login
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            PrintWriter out = response.getWriter();
            out.println("<font color=green>Registration successful, please login below.</font>");
            rd.include(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id;
        String name;
        String email;
        String password;
        String country;
        String errorMsg = "";

        id = request.getParameter("id");
        name = request.getParameter("name");
        email = request.getParameter("email");
        password = request.getParameter("password");
        country = request.getParameter("country");

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        User user = new User(id, name, password, email, country);

        if (email == null || email.equals("")) {
            errorMsg = "Email ID/ ";
        }
        if (password == null || password.equals("")) {
            errorMsg = errorMsg + "Password/ ";
        }
        if (name == null || name.equals("")) {
            errorMsg = errorMsg + "Name/ ";
        }
        if (country == null || country.equals("")) {
            errorMsg = errorMsg + "Country ";
        }
        System.out.println(errorMsg);
        if (errorMsg == null || errorMsg == "") {
            if (id == null || id.isEmpty()) {
                new RegisterServlet().addUser(user, connection);
            } else {
                user.setId(id);
                new RegisterServlet().updateUser(user, connection);
            }
            request.setAttribute("users", new RegisterServlet().getAllUsers(connection));
            RequestDispatcher view = request.getRequestDispatcher("/home.jsp");
            view.forward(request, response);
        } else {
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + " Can't be empty" + "</font>");
            RequestDispatcher view = request.getRequestDispatcher("/register.jsp");
            view.include(request, response);
        }
    }

    private void updateUser(User user, Connection connection) {
        try {

            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("update Users set name=?, email=?, password=?, country=? where id=?");
            // Parameters start with 1
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getCountry());
            preparedStatement.setString(5, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user, Connection connection) {
        try {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("insert into registration.Users(name,email,country, password) values (?,?,?,?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            logger.error("Database connection problem");
        }
    }

    public List<User> getAllUsers(Connection connection) throws ServletException {
        List<User> users = new ArrayList<User>();

        System.out.println(connection);
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement("select * from registration.Users");
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
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
