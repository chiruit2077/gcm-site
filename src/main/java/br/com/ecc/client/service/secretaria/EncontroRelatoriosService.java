package br.com.ecc.client.service.secretaria;

import br.com.ecc.model.Encontro;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroRelatorios")
public interface EncontroRelatoriosService extends RemoteService {
	public Integer imprimeRelatorioRomantico(Encontro encontro) throws Exception;
	public Integer geraCSV(Encontro encontro, String name) throws Exception;
}