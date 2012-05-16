package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroFila;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroFila")
public interface EncontroFilaService extends RemoteService {
	public List<EncontroFila> lista(Encontro encontro) throws Exception;
	public void exclui(EncontroFila encontroFila) throws Exception;
	public EncontroFila salva(EncontroFila encontroFila) throws Exception;
}