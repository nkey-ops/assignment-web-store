<%--
  Created by IntelliJ IDEA.
  User: saloh
  Date: 2023-10-05
  Time: 22:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to Online Shopping Center</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #f4f4f4;
        }

        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .actions {
            display: flex;
            justify-content: space-between;
        }

        button {
            padding: 10px 15px;
            border: none;
            background-color: #007BFF;
            color: white;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Welcome to the online shopping center</h1>

    <% if (request.getAttribute("message") != null) { %>
    <p><%= request.getAttribute("message") %></p>
    <% } %>

    <form action="RegisterServlet" method="post">
        <label for="chooseUsername">Choose a username:</label>
        <input type="text" id="chooseUsername" name="chooseUsername" required><br><br>

        <label for="choosePassword">Choose a password:</label>
        <input type="password" id="choosePassword" name="choosePassword" required><br><br>

        <div class="actions">

            <button type="submit" name="action" value="signup">Sign Up</button>
        </div>
    </form>
</div>
</body>
</html>

