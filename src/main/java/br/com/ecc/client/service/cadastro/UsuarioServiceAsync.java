package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.UsuarioVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UsuarioServiceAsync {
	void lista(String nome, AsyncCallback<List<Usuario>> callback);
	void getVO(Usuario usuario, AsyncCallback<UsuarioVO> callback);
	void salva(UsuarioVO usuarioVO, AsyncCallback<UsuarioVO> callback);
}