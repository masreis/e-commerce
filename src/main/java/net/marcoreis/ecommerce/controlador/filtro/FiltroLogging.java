package net.marcoreis.ecommerce.controlador.filtro;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FiltroLogging implements Serializable, Filter {
	private static final long serialVersionUID =
			1472782644963167647L;

	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		filterChain.doFilter(request, response);
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest =
					(HttpServletRequest) request;
		}
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
