package ui.controller.products;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.repositories.CategoriesRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.UserModel;

/**
 * ProductCategoriesServlet
 */
@WebServlet(urlPatterns = "/products/categories")
public class ProductCategoriesServlet  extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private CategoriesRepository categoriesRepo;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        this.categoriesRepo = (CategoriesRepository) 
                             servletContext.getAttribute("categoriesRepository");

        if (this.categoriesRepo == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         UserModel user = (UserModel) req.getSession().getAttribute("user");

        if (user.getType() != UserModel.Type.ADMIN){
           resp.setStatus(403);
           resp.sendRedirect("forbidden.jsp");
        }

        String categorie = req.getParameter("name"); 

        if(categoriesRepo.existsByName(categorie)) {
            req.setAttribute("error", 
                    "Category already exists with name: " + categorie);
        }else {
            categoriesRepo.createCategory(categorie);
        }

        req.getRequestDispatcher("productForm.jsp").forward(req, resp);
    }



}
