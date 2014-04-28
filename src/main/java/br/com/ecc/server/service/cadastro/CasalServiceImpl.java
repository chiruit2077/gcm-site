package br.com.ecc.server.service.cadastro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.service.cadastro.CasalService;
import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalListaSorteio;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.vo.CasalOpcaoRelatorioVO;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.CasalVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.SessionHelper;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.CasalListaImprimirCommand;
import br.com.ecc.server.command.CasalSalvarCommand;
import br.com.ecc.server.command.UsuarioGerarPorPessoaCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class CasalServiceImpl extends SecureRemoteServiceServlet implements CasalService {
	private static final long serialVersionUID = -6986089366658250854L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar casals", operacao=Operacao.VISUALIZAR)
	public List<Casal> lista(CasalParamVO casalParamVO) throws Exception {
		List<Casal> listaCasal = new ArrayList<Casal>();
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		if(casalParamVO.getAgrupamento()!=null) {
			cmd.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmd.addParameter("agrupamento", casalParamVO.getAgrupamento());
			List<AgrupamentoMembro> lista = cmd.call();
			for (AgrupamentoMembro agrupamentoMembro : lista) {
				listaCasal.add(agrupamentoMembro.getCasal());
			}
		} else if(casalParamVO.getEncontro()!=null) {
			cmd.setNamedQuery("encontroInscricao.porEncontro");
			cmd.addParameter("encontro", casalParamVO.getEncontro());
			List<EncontroInscricao> lista = cmd.call();
			boolean ok;
			for (EncontroInscricao ei : lista) {
				ok = false;
				if(casalParamVO.getTodosInscritos()){
					ok = true;
				} else if(casalParamVO.getTipoInscricao()!=null && casalParamVO.getTipoInscricao().equals(ei.getTipo())){
					ok = true;
				}
				if(ok && ei.getCasal()!=null && ei.getTipoConfirmacao()!=null && ei.getTipoConfirmacao().equals(TipoConfirmacaoEnum.CONFIRMADO)){
					listaCasal.add(ei.getCasal());
				}
			}

		}  else {
			if(casalParamVO.getNome()==null){
				cmd.setNamedQuery("casal.porGrupo");
			} else {
				cmd.setNamedQuery("casal.porGrupoNomeLike");
				cmd.addParameter("key", "%" + casalParamVO.getNome() + "%");
			}
			cmd.addParameter("grupo", casalParamVO.getGrupo());
			listaCasal =  cmd.call();
		}
		if(casalParamVO.getTipoCasal()!=null){
			List<Casal> listaCasalLocal = new ArrayList<Casal>();
			for (Casal casal : listaCasal) {
				if(casal.getTipoCasal().equals(casalParamVO.getTipoCasal())){
					listaCasalLocal.add(casal);
				}
			}
			listaCasal = listaCasalLocal;
		}
		if(casalParamVO.getSituacao()!=null){
			for (int i = 0; i < listaCasal.size(); i++) {
				if(listaCasal.get(i).getSituacao()==null || !listaCasal.get(i).getSituacao().equals(casalParamVO.getSituacao())){
					listaCasal.remove(i);
					i--;
				}
			}
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getApelidos("e").compareTo(o2.getApelidos("e"));
			}
		});
		return listaCasal;
	}

	@Override
	@Permissao(nomeOperacao="Excluir casal", operacao=Operacao.EXCLUIR)
	public void exclui(Casal casal) throws Exception {
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(casal);
		cmd.call();
	}
	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Buscar VO", operacao=Operacao.VISUALIZAR)
	public CasalVO getVO(Casal casal) throws Exception {
		CasalVO vo = new CasalVO();
		vo.setCasal(casal);

		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("casalContato.porCasal");
		cmd.addParameter("casal", casal);
		vo.setListaContatos(cmd.call());

		return vo;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Salvar casal VO", operacao=Operacao.SALVAR)
	public CasalVO salva(CasalVO casalVO) throws Exception {
		boolean ok = false;
		Usuario usuario = SessionHelper.getUsuario(getThreadLocalRequest().getSession());
		if(usuario.getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			ok = true;
		} else {
			GetEntityListCommand cmdE = injector.getInstance(GetEntityListCommand.class);
			cmdE.setNamedQuery("casal.porPessoa");
			cmdE.addParameter("pessoa", usuario.getPessoa());
			List<Casal> lc = cmdE.call();
			if(lc.size()>0){
				if(casalVO.getCasal().getId().equals(lc.get(0).getId())){
					ok = true;
				}
			}
		}
		if(!ok){
			throw new WebException("Erro ao gravar dados - Acesso negado");
		}
		CasalSalvarCommand cmd = injector.getInstance(CasalSalvarCommand.class);
		cmd.setIgnorarCotatos(false);
		cmd.setCasalVO(casalVO);
		cmd.setUsuario(usuario);
		casalVO = cmd.call();

		if(casalVO.getCasal().getTipoCasal()!=null && !casalVO.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
			UsuarioGerarPorPessoaCommand cmdGeraUsuarios = injector.getInstance(UsuarioGerarPorPessoaCommand.class);
			cmdGeraUsuarios.call();
		}
		return casalVO;
	}

	@Override
	@Permissao(nomeOperacao="Salvar casal", operacao=Operacao.SALVAR)
	public Casal salva(Casal casal) throws Exception {
		Usuario usuario = SessionHelper.getUsuario(getThreadLocalRequest().getSession());

		CasalVO casalVO = new CasalVO();
		casalVO.setCasal(casal);
		CasalSalvarCommand cmd = injector.getInstance(CasalSalvarCommand.class);
		cmd.setCasalVO(casalVO);
		cmd.setIgnorarCotatos(true);
		cmd.setUsuario(usuario);
		casalVO = cmd.call();

		//if(casalVO.getCasal().getTipoCasal()!=null && !casalVO.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
		UsuarioGerarPorPessoaCommand cmdGeraUsuarios = injector.getInstance(UsuarioGerarPorPessoaCommand.class);
		cmdGeraUsuarios.call();
		//}

		return casalVO.getCasal();
	}

	@Override
	public Integer imprimeLista(List<Casal> listaCasal, CasalOpcaoRelatorioVO casalOpcaoRelatorioVO) throws Exception {
		CasalListaImprimirCommand cmd = injector.getInstance(CasalListaImprimirCommand.class);
		cmd.setListaCasal(listaCasal);
		cmd.setCasalOpcaoRelatorioVO(casalOpcaoRelatorioVO);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CasalVO> listaSorteio(Encontro encontro) throws Exception {
		List<CasalVO> lvo = new ArrayList<CasalVO>();
		
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("casal.porGrupoAtivo");
		cmd.addParameter("grupo", encontro.getGrupo());
		List<Casal> listaCasal = cmd.call();

		cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("casalListaSorteio.porEncontro");
		cmd.addParameter("encontro", encontro);
		List<CasalListaSorteio> lista = cmd.call();
		

		Encontro encontroAnterior = null;
		cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontro.porGrupo");
		cmd.addParameter("grupo", encontro.getGrupo());
		List<Encontro> listaEncontro = cmd.call();
		for (int i = 0; i < listaEncontro.size(); i++) {
			if(listaEncontro.get(i).getId().equals(encontro.getId())){
				if(i+1<=listaEncontro.size()){
					encontroAnterior = listaEncontro.get(i+1);
					break;
				}
			}
		}
		
		if(encontroAnterior!=null){
			cmd = injector.getInstance(GetEntityListCommand.class);
			cmd.setNamedQuery("encontroInscricao.porEncontroConfirmados");
			cmd.addParameter("encontro", encontroAnterior);
			List<EncontroInscricao> listaE = cmd.call();
			
			CasalVO vo;
			for (Casal casal : listaCasal) {
				vo = new CasalVO();
				vo.setCasal(casal);
				lvo.add(vo);
				for (EncontroInscricao encontroInscricao : listaE) {
					if(encontroInscricao.getCasal()!=null && encontroInscricao.getCasal().getId().equals(casal.getId())){
						vo.setUltimaParticipacao(encontroInscricao.getTipo());
						break;
					}
				}
				for (CasalListaSorteio cs : lista) {
					if(cs.getCasal().getId().equals(casal.getId())){
						vo.setCasalSorteio(cs);
						break;
					}
				}
			}
		}
		return lvo;
	}

	@Override
	public CasalListaSorteio salvarSorteio(Encontro encontro, Casal casal, TipoInscricaoEnum tipo) throws Exception {
		CasalListaSorteio cs = new CasalListaSorteio();
		cs.setEncontro(encontro);
		cs.setCasal(casal);
		cs.setTipo(tipo);

		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(cs);
		return (CasalListaSorteio) cmd.call();
		
	}

	@Override
	public void excluirSorteio(CasalListaSorteio casal) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(casal);
		cmd.call();
	}

	@Override
	public Integer imprimeListaSorteio(List<CasalListaSorteio> listaCasal) throws Exception {
		return null;
	}

}