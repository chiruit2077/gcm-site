package br.com.ecc.server.command;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.Usuario;
import br.com.ecc.server.util.CriptoUtil;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class AutenticaUsuarioCommand implements Callable<Usuario>{

	@Inject EntityManager em;

	private String email;
	private String senha;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Usuario call() throws Exception {
		String criptPass = CriptoUtil.Codifica(senha);
		
		Query q = em.createNamedQuery("usuario.porEmail");
		q = q.setParameter("email", email);
		List<Usuario> resultados = q.getResultList();
		if (resultados.size() == 0) {
			throw new WebException("Usuário não encontrado.");
		}
		if (resultados.size() > 1) {
			throw new WebException("Email em duplicidade.");
		}
		if (resultados.size() == 1) {
			Usuario user = resultados.iterator().next();
			if (criptPass.equals(user.getSenha())) {
				user.setUltimoAcesso(new Date());
				return em.merge(user);
			} else {
				throw new WebException("Senha inválida.");
			}
		}
		throw new WebException("Usuário não encontrado.");
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getSenha() {
		return senha;
	}
}