package br.leg.rr.al.supleg.filters;

import br.leg.rr.al.supleg.models.Usuario;
import br.leg.rr.al.supleg.enums.TipoUsuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/pages/*")
public class NavegacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpSession httpSession = ((HttpServletRequest) request).getSession();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!httpServletRequest.getRequestURI().contains("login.xhtml")) {

            Usuario u = (Usuario) httpSession.getAttribute("usuario");
            
            if (u == null) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.xhtml");
            } else {
                
                Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
                
                if (usuario.getTipo().equals(TipoUsuario.RELATORIO) && (httpServletRequest.getRequestURI().contains("superadmin") || httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("user"))) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/common/roteiro.xhtml");
                } else if (usuario.getTipo().equals(TipoUsuario.OPERADOR) && (httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("superadmin"))) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/common/roteiro.xhtml");
                } else if (usuario.getTipo().equals(TipoUsuario.ADMIN) && httpServletRequest.getRequestURI().contains("superadmin")) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/common/roteiro.xhtml");
                } else {
                    chain.doFilter(request, response);
                }

            }
        } else {
            chain.doFilter(request, response);
        }
    }

}
