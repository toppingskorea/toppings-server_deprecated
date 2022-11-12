package com.toppings.server.domain_global.config.security.jwt.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

public class ServletContextFilter extends GenericFilterBean {

	private final ServletContext servletContext;

	public ServletContextFilter(ServletContext servletContext) {this.servletContext = servletContext;}

	@Override
	public void doFilter(
		ServletRequest req,
		ServletResponse res,
		FilterChain chain
	) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (Objects.nonNull(request.getParameter("redirect_uri"))) {
			servletContext.setAttribute("redirectUri", request.getParameter("redirect_uri"));
		}
		chain.doFilter(request, response);
	}
}
