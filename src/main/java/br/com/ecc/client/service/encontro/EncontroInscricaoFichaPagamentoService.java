package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroInscricaoFichaPagamento")
public interface EncontroInscricaoFichaPagamentoService extends RemoteService {
	public List<EncontroInscricaoFichaPagamento> listaFichas(Encontro encontroSelecionado) throws Exception;
	public void salvaFicha(EncontroInscricaoFichaPagamento ficha) throws Exception;
	public void geraFichasVagas(Encontro encontroSelecionado) throws Exception;
}