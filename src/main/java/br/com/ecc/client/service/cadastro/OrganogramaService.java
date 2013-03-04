package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Organograma;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("organograma")
public interface OrganogramaService extends RemoteService {
	public List<Organograma> lista() throws Exception;
	public void exclui(Organograma organograma) throws Exception;
	public Organograma salva(Organograma organograma) throws Exception;
}