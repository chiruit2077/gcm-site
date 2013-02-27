package br.com.ecc.client.service;

import java.util.Date;
import java.util.List;

import br.com.ecc.model.Agenda;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Recado;
import br.com.ecc.model.vo.InicioVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("recado")
public interface RecadoService extends RemoteService {
	public InicioVO listaInicial(Casal casal, Encontro encontro) throws Exception;
	public List<Recado> listaPorCasal(Casal casal, Boolean lido) throws Exception;
	public List<Recado> listaPorGrupo(Grupo grupo, Casal casal, Date inicio) throws Exception;
	public List<Recado> listaTodosCasal(Casal casal, Date inicio) throws Exception;
	public Recado salvar(Recado recado) throws Exception;
	public List<Agenda> salvarAgenda(Agenda agenda, Encontro encontro) throws Exception;
	public List<Agenda> excluiAgenda(Agenda agenda, Encontro encontro) throws Exception;
}