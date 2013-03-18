package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroAtividadeInscricao")
public interface EncontroAtividadeInscricaoService extends RemoteService {
	public List<EncontroAtividadeInscricao> lista(Encontro encontro) throws Exception;
	public List<EncontroAtividadeInscricao> listaFiltrado(Encontro encontro, EncontroPeriodo encontroPeriodo, TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum) throws Exception;
	public void exclui(EncontroAtividadeInscricao encontroAtividadeInscricao) throws Exception;
	public EncontroAtividadeInscricao salva(EncontroAtividadeInscricao encontroAtividadeInscricao) throws Exception;
	void salvaInscricoes(Encontro encontro, EncontroAtividade encontroAtividade, EncontroInscricao encontroInscricao, List<EncontroAtividadeInscricao> listaParticipantes) throws Exception;
	public Integer imprimePlanilha(Encontro encontro, EncontroPeriodo encontroPeriodo, TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum, Boolean exportarExcel) throws Exception;
}