package br.com.ecc.model.vo;

import java.io.Serializable;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Usuario;

public class DadosLoginVO implements Serializable {
	private static final long serialVersionUID = 2125793140585441550L;
	
	private Casal casal;
	private Usuario usuario;
	private ParametrosRedirecionamentoVO parametrosRedirecionamentoVO;
	
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public ParametrosRedirecionamentoVO getParametrosRedirecionamentoVO() {
		return parametrosRedirecionamentoVO;
	}
	public void setParametrosRedirecionamentoVO(
			ParametrosRedirecionamentoVO parametrosRedirecionamentoVO) {
		this.parametrosRedirecionamentoVO = parametrosRedirecionamentoVO;
	}
}