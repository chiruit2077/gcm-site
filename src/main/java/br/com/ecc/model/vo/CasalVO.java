package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalContato;
import br.com.ecc.model.CasalListaSorteio;
import br.com.ecc.model.tipo.TipoInscricaoEnum;

public class CasalVO implements Serializable {
	private static final long serialVersionUID = 3250470243324419391L;
	
	private Casal casal;
	private CasalListaSorteio casalSorteio;
	private TipoInscricaoEnum ultimaParticipacao;
	private List<CasalContato> listaContatos;
	
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public List<CasalContato> getListaContatos() {
		return listaContatos;
	}
	public void setListaContatos(List<CasalContato> listaContatos) {
		this.listaContatos = listaContatos;
	}
	public TipoInscricaoEnum getUltimaParticipacao() {
		return ultimaParticipacao;
	}
	public void setUltimaParticipacao(TipoInscricaoEnum ultimaParticipacao) {
		this.ultimaParticipacao = ultimaParticipacao;
	}
	public CasalListaSorteio getCasalSorteio() {
		return casalSorteio;
	}
	public void setCasalSorteio(CasalListaSorteio casalSorteio) {
		this.casalSorteio = casalSorteio;
	}
}