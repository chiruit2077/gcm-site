package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalContato;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.vo.CasalVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class CasalSalvarCommand implements Callable<CasalVO>{

	@Inject EntityManager em;

	private CasalVO casalVO;
	private Boolean ignorarCotatos;
	private Usuario usuario;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public CasalVO call() throws Exception {
		Query q;
		Casal casal = casalVO.getCasal();

		//verificação de segurança pra que o tipo não seja alterado opr outro que não seja o administrador
		usuario = em.find(Usuario.class, usuario.getId());
		if(usuario.getNivel().equals(TipoNivelUsuarioEnum.NORMAL)){
			if(casal.getId()==null){
				casal.setTipoCasal(TipoCasalEnum.CONVIDADO);
			} else {
				Casal casalOriginal = em.find(Casal.class, casal.getId());
				casal.setTipoCasal(casalOriginal.getTipoCasal());
			}
		}

		casal.setEle(em.merge(casal.getEle()));
		casal.setEla(em.merge(casal.getEla()));

		q = em.createNamedQuery("casal.porPessoa");
		q.setParameter("pessoa", usuario.getPessoa());
		q.setMaxResults(1);
		List<Casal> lista = q.getResultList();
		if(lista.size()!=0){
			if(usuario.getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) || 
			   usuario.getNivel().equals(TipoNivelUsuarioEnum.ROOT) || 
			   casal.getId()==null || (casal.getId()!=null && casal.getId().equals(lista.get(0).getId()))){
				casal.setAtualizacaoCadastro(new Date());
			}
		}

		casalVO.setCasal(em.merge(casal));

		//contatos
		if(!ignorarCotatos){
			List<CasalContato> novaLista = new ArrayList<CasalContato>();
			if(casalVO.getListaContatos()!=null){
				for (CasalContato contato : casalVO.getListaContatos()) {
					if(contato.getId()!=null){
						novaLista.add(contato);
					}
				}
			}
			if(novaLista.size()>0){
				q = em.createNamedQuery("casalContato.deletePorCasalNotIn");
				q.setParameter("casal", casalVO.getCasal());
				q.setParameter("lista", novaLista);
				q.executeUpdate();
			} else {
				q = em.createNamedQuery("casalContato.deletePorCasal");
				q.setParameter("casal", casalVO.getCasal());
				q.executeUpdate();
			}
			if(casalVO.getListaContatos()!=null){
				CasalContato contato = null;
				for (int i=0; i<casalVO.getListaContatos().size(); i++) {
					contato = casalVO.getListaContatos().get(i);
					contato.setCasal(casalVO.getCasal());
					casalVO.getListaContatos().set(i, em.merge(contato));
				}
			}
		}

		q = em.createNamedQuery("arquivoDigital.limpaLixo");
		q.executeUpdate();

		return casalVO;
	}

	public CasalVO getCasalVO() {
		return casalVO;
	}
	public void setCasalVO(CasalVO casalVO) {
		this.casalVO = casalVO;
	}
	public Boolean getIgnorarCotatos() {
		return ignorarCotatos;
	}
	public void setIgnorarCotatos(Boolean ignorarCotatos) {
		this.ignorarCotatos = ignorarCotatos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}