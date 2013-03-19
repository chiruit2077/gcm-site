package br.com.ecc.client.service;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdministracaoServiceAsync {
	void login(String email, String senha, AsyncCallback<DadosLoginVO> callback);
	void logout(AsyncCallback<Void> callback);
	void getDadosLogin(AsyncCallback<DadosLoginVO> callback);
	void reenviarSenha(String email, AsyncCallback<Boolean> callback);
	void salvaUsuario(Usuario usuario, Boolean senhaAlterada, AsyncCallback<Usuario> callback);
	void salvaUsuarioLogado(Usuario usuario, Boolean senhaAlterada, AsyncCallback<Usuario> callback);
	void listaGrupos(AsyncCallback<List<Grupo>> callback);
}