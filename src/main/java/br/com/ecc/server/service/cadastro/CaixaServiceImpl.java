package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.CaixaService;
import br.com.ecc.model.Caixa;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.CaixaVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.CaixaExcluirCommand;
import br.com.ecc.server.command.CaixaSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class CaixaServiceImpl extends SecureRemoteServiceServlet implements CaixaService {

	private static final long serialVersionUID = -9155359156500054133L;
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar caixas por grupo", operacao=Operacao.VISUALIZAR)
	public List<Caixa> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("caixa.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir caixas", operacao=Operacao.EXCLUIR)
	public void exclui(Caixa caixa) throws Exception {
		CaixaExcluirCommand cmd = injector.getInstance(CaixaExcluirCommand.class);
		cmd.setCaixa(caixa);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar caixas", operacao=Operacao.SALVAR)
	public void salva(CaixaVO caixaVO) throws Exception {
		CaixaSalvarCommand cmd = injector.getInstance(CaixaSalvarCommand.class);
		cmd.setCaixaVO(caixaVO);
		cmd.call();
	}

	@Override
	@SuppressWarnings("unchecked")
	public CaixaVO getVO(Caixa caixa){
		CaixaVO vo = new CaixaVO();

		vo.setCaixa(caixa);

		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("caixaItem.porCaixa");
		cmd.addParameter("caixa", caixa);
		vo.setListaCaixaItem(cmd.call());

		return vo;
	}
}