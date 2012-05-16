package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Usuario;
import br.com.ecc.model.UsuarioAcesso;

public class UsuarioVO implements Serializable {
	private static final long serialVersionUID = 7809966659456781691L;
	
	private Usuario usuario;
	private List<UsuarioAcesso> listaAcessos;

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<UsuarioAcesso> getListaAcessos() {
		return listaAcessos;
	}
	public void setListaAcessos(List<UsuarioAcesso> listaAcessos) {
		this.listaAcessos = listaAcessos;
	}
}