package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroInscricaoServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroInscricao>> callback);
	void salva(EncontroInscricaoVO encontroInscricaoVO, AsyncCallback<EncontroInscricaoVO> callback);
	void exclui(EncontroInscricao encontroInscricao, AsyncCallback<Void> asyncCallback);
	void getVO(EncontroInscricao encontroInscricao, AsyncCallback<EncontroInscricaoVO> callback);
	void getVO(Encontro encontro, Casal casal, AsyncCallback<EncontroInscricaoVO> callback);
	void listaPagamentos(Encontro encontroSelecionado, AsyncCallback<List<EncontroInscricaoPagamento>> callback);
	void salvaPagamento(EncontroInscricaoPagamento pagamento, AsyncCallback<Void> callback);
	void listaVO(Encontro encontro, AsyncCallback<List<EncontroInscricaoVO>> callback);
}