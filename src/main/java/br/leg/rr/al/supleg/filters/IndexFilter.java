package br.leg.rr.al.supleg.filters;

import br.leg.rr.al.supleg.models.Usuario;
import java.io.IOException;
import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/login.xhtml"})
@Priority(1)
public class IndexFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpSession httpSession = ((HttpServletRequest) request).getSession();
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            
            if (httpServletRequest.getRequestURI().contains("forms/login")) {
                try {
                    Usuario u = (Usuario) httpSession.getAttribute("usuario");
                    if (u != null) {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/relatorio-resumo.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                } catch (IOException | ServletException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (IOException | ServletException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
