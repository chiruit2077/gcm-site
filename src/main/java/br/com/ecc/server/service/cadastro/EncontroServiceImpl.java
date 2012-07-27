package br.com.ecc.server.service.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.EncontroVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroCarregaVOCommand;
import br.com.ecc.server.command.EncontroSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroServiceImpl extends SecureRemoteServiceServlet implements EncontroService {
	private static final long serialVersionUID = 8739734601403901436L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontros", operacao=Operacao.VISUALIZAR)
	public List<Encontro> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontro.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir encontro", operacao=Operacao.EXCLUIR)
	public void exclui(Encontro encontro) throws Exception {
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontro);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar encontro", operacao=Operacao.SALVAR)
	public void salva(EncontroVO encontroVO) throws Exception {
		EncontroSalvarCommand cmd = injector.getInstance(EncontroSalvarCommand.class);
		cmd.setEncontroVO(encontroVO);
		cmd.call();
	}

	@Override
	public EncontroVO getVO(Encontro encontro, Boolean ignorarAfilhados) throws Exception {
		EncontroCarregaVOCommand cmd = injector.getInstance(EncontroCarregaVOCommand.class);
		cmd.setEncontro(encontro);
		EncontroVO vo =  cmd.call();
		if(ignorarAfilhados){
			List<EncontroInscricao> lista = new ArrayList<EncontroInscricao>();
			for (EncontroInscricao ei : vo.getListaInscricao()) {
				if(!ei.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					lista.add(ei);
				}
			}
			vo.setListaInscricao(lista);
		}
		
		return vo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroInscricao> listaInscricoes(Encontro encontro, Boolean exibeRecusados) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		if(exibeRecusados==null || exibeRecusados){
			cmd.setNamedQuery("encontroInscricao.porEncontro");
		} else {
			cmd.setNamedQuery("encontroInscricao.porEncontroConfirmados");
		}
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}
}