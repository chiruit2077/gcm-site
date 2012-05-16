package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.UsuarioVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("usuario")
public interface UsuarioService extends RemoteService {
	public List<Usuario> lista(String nome) throws Exception;
	public UsuarioVO salva(UsuarioVO usuarioVO) throws Exception;
	public UsuarioVO getVO(Usuario usuario);
}