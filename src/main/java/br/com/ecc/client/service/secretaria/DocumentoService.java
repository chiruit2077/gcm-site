package br.com.ecc.client.service.secretaria;

import java.util.List;

import br.com.ecc.model.Documento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("documento")
public interface DocumentoService extends RemoteService {
	public List<Documento> lista(Grupo grupo, Encontro encontro, String textoFiltro) throws Exception;
	public Documento getDocumento(Integer id) throws Exception;
	public Documento salva(Documento documento) throws Exception;
	public void exclui(Documento documento) throws Exception;
}