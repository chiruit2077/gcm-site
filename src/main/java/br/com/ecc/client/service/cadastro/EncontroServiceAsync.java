package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroServiceAsync {
	public void lista(Grupo grupo, AsyncCallback<List<Encontro>> callback);
	public void salva(EncontroVO encontroVO, AsyncCallback<Void> callback);
	public void exclui(Encontro encontro, AsyncCallback<Void> asyncCallback);
	public void getVO(Encontro encontro, Boolean ignorarAfilhados, AsyncCallback<EncontroVO> callback);
	public void listaInscricoes(Encontro encontroSelecionado, AsyncCallback<List<EncontroInscricao>> callback);
}