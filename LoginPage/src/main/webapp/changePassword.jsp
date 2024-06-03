<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
</head>
<body>
    <%
    String email = request.getParameter("email");
    String newPassword = request.getParameter("newPassword");
    String status = null;

    if (email != null && !email.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            // Establish database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailproject?useSSL=false","root","sahil");

            // Update password in the database
            String sql = "UPDATE users SET upwd = ? WHERE uemail = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, newPassword);
            pst.setString(2, email);

            // Execute SQL update
            int rowCount = pst.executeUpdate();

            // Check if update was successful
            if (rowCount > 0) {
                status = "success";
            } else {
                status = "failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = "error";
        } finally {
            // Close resources
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } else {
        status = "invalid";
    }

    // Display status message
    if (status != null) {
        if (status.equals("success")) {
            out.println("<p>Password changed successfully!</p>");
        } else if (status.equals("failed")) {
            out.println("<p>Failed to change password. Please try again.</p>");
        } else if (status.equals("error")) {
            out.println("<p>An error occurred. Please try again later.</p>");
        } else if (status.equals("invalid")) {
            out.println("<p>Invalid email or new password.</p>");
        }
    }
    %>
</body>
</html>
