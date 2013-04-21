package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.Usuario;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroConvite")
public interface EncontroConviteService extends RemoteService {
	public List<EncontroConvite> lista(Encontro encontro) throws Exception;
	public void exclui(EncontroConvite encontroConvite, Usuario usuarioAtual) throws Exception;
	public EncontroConvite salva(EncontroConvite encontroConvite, Usuario usuarioAtual) throws Exception;
}