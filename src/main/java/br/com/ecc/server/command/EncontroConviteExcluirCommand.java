package br.com.ecc.server.command;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.Usuario;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroConviteExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	@Inject Injector inject;
	private EncontroConvite encontroConvite;
	private Usuario usuarioAtual;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Void call() throws Exception {

		/*
		EncontroInscricaoVO voafilhado = getEncontroInscricaoVO(encontroConvite.getCasalConvidado());
		if (voafilhado!=null){
			EncontroInscricaoExcluirCommand cmd = inject.getInstance(EncontroInscricaoExcluirCommand.class);
			cmd.setEncontroInscricao(voafilhado.getEncontroInscricao());
			cmd.call();
		}
		*/


		if(encontroConvite.getOrdem()!=null){
			Query q = em.createNamedQuery("encontroConvite.porEncontro");
			q.setParameter("encontro", encontroConvite.getEncontro());
			List<EncontroConvite> listaConvite = q.getResultList();

			boolean achei = false;
			for (EncontroConvite ec : listaConvite) {
				if(ec.getId().equals(encontroConvite.getId())){
					achei = true;
				} else {
					if(achei){
						if(ec.getEncontroFila().getId().equals(encontroConvite.getEncontroFila().getId())){
							ec.setOrdem(ec.getOrdem()-1);
							em.merge(ec);
						}
					}
				}
			}
		}
		em.remove(em.merge(encontroConvite));
		return null;
	}

	/*
	@SuppressWarnings("unchecked")
	private EncontroInscricaoVO getEncontroInscricaoVO(Casal casal) throws Exception {
		Query q = em.createNamedQuery("encontroInscricao.porEncontroCasal");
		q.setParameter("encontro", encontroConvite.getEncontro());
		q.setParameter("casal", encontroConvite.getCasal());
		List<EncontroInscricao> result = q.getResultList();
		if (result.size()>0){
			EncontroInscricaoCarregaVOCommand cmd = inject.getInstance(EncontroInscricaoCarregaVOCommand.class);
			cmd.setEncontroInscricao(result.get(0));
			return cmd.call();
		}
		return null;
	}
	*/

	public EncontroConvite getEncontroConvite() {
		return encontroConvite;
	}
	public void setEncontroConvite(EncontroConvite encontroConvite) {
		this.encontroConvite = encontroConvite;
	}

	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}

	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
}