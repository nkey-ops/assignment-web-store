import java.io.IOException;

import bo.services.UsersService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * AppFilter
 */
@WebFilter(urlPatterns = "/*")
public class AppFilter implements Filter {

	private ServletContext context;
    private UsersService uService;

	
	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("RequestLoggingFilter initialized");

        uService = (UsersService) fConfig.getServletContext()
                                         .getAttribute("usersService"); 

        if (this.uService  == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");

	}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/login";
        String regURI = request.getContextPath() + "/RegisterServlet";

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI) 
                            || request.getRequestURI().equals(regURI);

        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(loginURI);
        }
    }
}
