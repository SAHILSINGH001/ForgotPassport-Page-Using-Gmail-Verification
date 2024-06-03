package com.sahilsinghrajput.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/validateOtp")
public class Validateotp extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int MAX_ATTEMPTS = 3;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int otp = Integer.parseInt(request.getParameter("otp"));
        HttpSession session = request.getSession();
        int otpValue = (int) session.getAttribute("otp");
        Integer attempts = (Integer) session.getAttribute("attempts");
        
        if (attempts == null) {
            attempts = 0;
        }
        
        RequestDispatcher dispatcher = null;

        if (otp == otpValue) {
            session.removeAttribute("attempts"); 
            dispatcher = request.getRequestDispatcher("newPassword.jsp");
            dispatcher.forward(request, response);
        } else {
            attempts++;
            session.setAttribute("attempts", attempts);
            if (attempts >= MAX_ATTEMPTS) {
                session.removeAttribute("attempts"); 
                response.sendRedirect("forgotPassword.jsp");
            } else {
                request.setAttribute("message", "Invalid OTP");
                dispatcher = request.getRequestDispatcher("EnterOtp.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}
