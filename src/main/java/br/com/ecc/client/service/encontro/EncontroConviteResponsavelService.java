package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConviteResponsavel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroConviteResponsavel")
public interface EncontroConviteResponsavelService extends RemoteService {
	public List<EncontroConviteResponsavel> lista(Encontro encontro) throws Exception;
	public void exclui(EncontroConviteResponsavel encontroConviteResponsavel) throws Exception;
	public EncontroConviteResponsavel salva(EncontroConviteResponsavel encontroConviteResponsavel) throws Exception;
}