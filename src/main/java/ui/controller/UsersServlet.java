package ui.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.UsersService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.UserModel;

@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {
  private static Logger logger = LogManager.getLogger();
  private UsersService usersService;

  public void init() throws ServletException {
    ServletContext servletContext = getServletContext();
    this.usersService = (UsersService) servletContext.getAttribute("usersService");

    if (this.usersService == null) {
      throw new RuntimeException("Dependency wasn't found in the context");
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    logger.debug("Get all users page was requested");
    UserModel user = (UserModel) req.getSession().getAttribute("user");

    if (user.getType() != UserModel.Type.ADMIN) {
      resp.setStatus(403);
      resp.sendRedirect("forbidden.jsp");
    }

    List<UserModel> users = usersService.getAllUsers();

    req.setAttribute("users", users);
    req.getRequestDispatcher("users.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.debug("User update was requested");
    UserModel user = (UserModel) req.getSession().getAttribute("user");

    if (user.getType() != UserModel.Type.ADMIN) {
      resp.setStatus(403);
      resp.sendRedirect("forbidden.jsp");
    }

    String username = req.getParameter("name");
    UserModel.Type type = UserModel.Type.valueOf((String) req.getParameter("type"));

    usersService.updateUserType(username, type);

    resp.sendRedirect("/users");
  }
}
