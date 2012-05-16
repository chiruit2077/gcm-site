package br.com.ecc.server.service.redirecionamento;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@Singleton
public class FichaServlet extends RedirecionamentoServlet  {
	private static final long serialVersionUID = -1165158597712744726L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		super.doGet(req, resp);
	}
}