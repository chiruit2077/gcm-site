package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;

public class EncontroMonitorVO implements Serializable {

	private static final long serialVersionUID = -8774043481335957488L;

	private Encontro encontro;
	private EncontroAtividade atividade1;
	private EncontroAtividade atividade2;
	private List<EncontroAtividade> proximasAtividades;
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public EncontroAtividade getAtividade1() {
		return atividade1;
	}
	public void setAtividade1(EncontroAtividade atividade1) {
		this.atividade1 = atividade1;
	}
	public EncontroAtividade getAtividade2() {
		return atividade2;
	}
	public void setAtividade2(EncontroAtividade atividade2) {
		this.atividade2 = atividade2;
	}
	public List<EncontroAtividade> getProximasAtividades() {
		return proximasAtividades;
	}
	public void setProximasAtividades(List<EncontroAtividade> proximasAtividades) {
		this.proximasAtividades = proximasAtividades;
	}



}