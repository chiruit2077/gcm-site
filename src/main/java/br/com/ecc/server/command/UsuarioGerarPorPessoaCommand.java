package br.com.ecc.server.command;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.client.util.StringUtil;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.server.util.CriptoUtil;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class UsuarioGerarPorPessoaCommand implements Callable<Void>{

	@Inject EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Void call() throws Exception {
		Query q = em.createNamedQuery("pessoa.semUsuario");
		List<Pessoa> lista = q.getResultList();
		
		Usuario usuario;
		for (Pessoa pessoa : lista) {
			usuario = new Usuario();
			usuario.setNivel(TipoNivelUsuarioEnum.NORMAL);
			usuario.setPessoa(pessoa);
			usuario.setSenha(CriptoUtil.Codifica(StringUtil.randomString(10)));
			em.merge(usuario);
		}
		return null;
	}

}