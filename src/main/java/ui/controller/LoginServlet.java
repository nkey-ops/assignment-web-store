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
import jakarta.servlet.http.HttpSession;

/**
 * Servlet that manages user authentication and login process.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();
    private UsersService usersService;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.usersService = (UsersService) servletContext.getAttribute("usersService");

        if (this.usersService == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("welcome.jsp").forward(req, resp);
    }




    /**
     * Handles the POST request for user login.
     *
     * @param request  HttpServletRequest object containing client request
     *                 information
     * @param response HttpServletResponse object used to send the response to the
     *                 client
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null || password != null) {
            if (usersService.checkCredentials(username, password)) {
                logger.debug("User logged in:" + username);

                HttpSession session = request.getSession();
                // Successful login
                session.setAttribute("user", usersService.getUserByName(username));
                response.sendRedirect("/products");
            } else {
                // Login failed
                request.setAttribute("message", "Invalid username or password! Create a new account");
                request.getRequestDispatcher("/newRegistering.jsp").forward(request, response);
            }

        }

    }
}
