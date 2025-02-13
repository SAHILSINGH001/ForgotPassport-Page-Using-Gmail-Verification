package com.sahilsinghrajput.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uemail = request.getParameter("email");
        String upwd = request.getParameter("password");
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = null;
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailproject", "root", "sahil");
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE uemail = ?");
            pst.setString(1, uemail);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                if (rs.getString("upwd").equals(upwd)) {
                    session.setAttribute("name", rs.getString("uname"));
                    dispatcher = request.getRequestDispatcher("index.jsp"); // redirect to a welcome page
                } else {
                    request.setAttribute("status", "wrongPassword");
                    dispatcher = request.getRequestDispatcher("login.jsp");
                }
            } else {
                request.setAttribute("status", "userNotFound");
                dispatcher = request.getRequestDispatcher("login.jsp");
            }
            
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
