package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroServiceAsync {
	public void lista(Grupo grupo, AsyncCallback<List<Encontro>> callback);
	void salvaVO(EncontroVO encontroVO, Boolean copiaUltimo, EncontroVO ultimoVO, AsyncCallback<Void> callback);
	public void exclui(Encontro encontro, AsyncCallback<Void> asyncCallback);
	public void getVO(Encontro encontro, Boolean ignorarAfilhados, AsyncCallback<EncontroVO> callback);
	public void listaInscricoes(Encontro encontroSelecionado, Boolean exibeRecusados, AsyncCallback<List<EncontroInscricao>> callback);
	public void listaPeriodos(Encontro encontro,AsyncCallback<List<EncontroPeriodo>> callback);
}