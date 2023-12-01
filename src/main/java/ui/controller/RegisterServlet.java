package ui.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.UsersService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet that manages user registration.
 */
@WebServlet("/RegisterServlet")

public class RegisterServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();
    private UsersService usersService;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.usersService = (UsersService) servletContext.getAttribute("usersService");

        if (this.usersService == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    /**
     * Handles the POST request for user registration.
     *
     * <p>
     * This method processes the username and password provided by the client. If the user
     * already exists, a message indicating the existence of the account is set. Otherwise,
     * the user is registered and a successful registration message is set.
     * </p>
     *
     * @param request  HttpServletRequest object containing client request information.
     * @param response HttpServletResponse object used to send the response to the client.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
                                     throws ServletException, IOException {
        // Retrieve the form parameters
        String username = request.getParameter("chooseUsername");
        String password = request.getParameter("choosePassword");

        if (username != null || password != null) {
             {
                 if (usersService.checkCredentials(username, password)) {
                     request.setAttribute("message", "You already have an account!");
                     request.getRequestDispatcher("/welcome.jsp").forward(request, response);

                 } else  {
                     usersService.createUser(username, password);

                     request.setAttribute("message", "Registration successful!");
                     request.getRequestDispatcher("/welcome.jsp").forward(request, response);
                 }
            }
    }
    }    }
