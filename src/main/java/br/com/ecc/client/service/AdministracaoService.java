package br.com.ecc.client.service;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("autenticacao")
public interface AdministracaoService extends RemoteService {
	public DadosLoginVO login(String email, String senha) throws Exception;
	public void logout() throws Exception;
	public DadosLoginVO getDadosLogin() throws Exception;
	public Boolean reenviarSenha(String email) throws Exception;
	public Usuario salvaUsuario(Usuario usuario, Boolean senhaAlterada) throws Exception;
	public Usuario salvaUsuarioLogado(Usuario usuario, Boolean senhaAlterada) throws Exception;
	public List<Grupo> listaGrupos() throws Exception;
}