package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalListaSorteio;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.CasalOpcaoRelatorioVO;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.CasalVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CasalServiceAsync {
	void lista(CasalParamVO casalParamVO, AsyncCallback<List<Casal>> callback);
	void getVO(Casal casal, AsyncCallback<CasalVO> callback);
	void salva(CasalVO casalVO, AsyncCallback<CasalVO> callback);
	void salva(Casal casal, AsyncCallback<Casal> callback);
	void exclui(Casal casal, AsyncCallback<Void> asyncCallback);
	void imprimeLista(List<Casal> listaCasal, CasalOpcaoRelatorioVO casalOpcaoRelatorioVO, AsyncCallback<Integer> callback);
	
	void listaSorteio(Encontro encontro, AsyncCallback<List<CasalVO>> callback);
	void salvarSorteio(Encontro encontro, Casal casal, TipoInscricaoEnum tipo, AsyncCallback<CasalListaSorteio> callback);
	void excluirSorteio(CasalListaSorteio casal, AsyncCallback<Void> callback);
	void imprimeListaSorteio(List<CasalListaSorteio> listaCasal, AsyncCallback<Integer> callback);
}