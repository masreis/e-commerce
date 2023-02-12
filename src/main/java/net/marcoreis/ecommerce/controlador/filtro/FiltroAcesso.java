package net.marcoreis.ecommerce.controlador.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.marcoreis.ecommerce.controlador.LoginBean;
import net.marcoreis.ecommerce.entidades.Cliente;

@WebFilter(filterName = "FiltroAcesso",
		urlPatterns = { "/paginas/*" })
public class FiltroAcesso implements Filter {
	private static Logger logger = LogManager.getLogger("historico");

	public void init(FilterConfig filterConfig)
			throws ServletException {
	}

	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse =
				(HttpServletResponse) response;
		HttpServletRequest httpRequest =
				(HttpServletRequest) request;
		String reqURI = httpRequest.getRequestURI();
		boolean isLoggedIn = isLoggedIn(httpRequest);
		if (reqURI.indexOf("/paginas/") >= 0 && isLoggedIn)
			logger.info(String.valueOf(
					getUsuarioSessao(httpRequest).getEmail())
					+ " " + reqURI);
		if (reqURI.indexOf("/login.faces") >= 0
				|| reqURI.contains("javax.faces.resource")
				|| isLoggedIn) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(String.valueOf(
					request.getServletContext().getContextPath())
					+ "/paginas/login.faces");
		}
	}

	private boolean isLoggedIn(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			LoginBean bean = (LoginBean) session
					.getAttribute("loginBean");
			return bean.isLoggedIn();
		} catch (Exception e) {
			return false;
		}
	}

	private Cliente getUsuarioSessao(
			HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		LoginBean bean =
				(LoginBean) session.getAttribute("loginBean");
		return bean.getCliente();
	}

	public void destroy() {
	}
}
