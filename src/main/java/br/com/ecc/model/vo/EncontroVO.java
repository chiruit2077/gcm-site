package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroConviteResponsavel;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.tipo.TipoInscricaoEnum;

public class EncontroVO implements Serializable {
	private static final long serialVersionUID = -8116104530913020100L;

	private Encontro encontro;
	private List<EncontroPeriodo> listaPeriodo;
	private List<EncontroTotalizacaoVO> listaTotalizacao;
	private List<EncontroAtividade> listaEncontroAtividade;
	private List<EncontroInscricao> listaInscricao;
	private List<Casal> listaCoordenadores;
	private List<AgrupamentoVO> listaAgrupamentoVOEncontro;
	private List<EncontroOrganogramaVO> listaOrganogramaEncontroVO;
	private List<EncontroConviteResponsavel> listaResponsavelConvite;
	private Integer qtdeInscricao;
	private Integer qtdeInscricaoExterno;

	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public List<EncontroPeriodo> getListaPeriodo() {
		return listaPeriodo;
	}
	public void setListaPeriodo(List<EncontroPeriodo> listaPeriodo) {
		this.listaPeriodo = listaPeriodo;
	}
	public List<EncontroAtividade> getListaEncontroAtividade() {
		return listaEncontroAtividade;
	}
	public void setListaEncontroAtividade(
			List<EncontroAtividade> listaEncontroAtividade) {
		this.listaEncontroAtividade = listaEncontroAtividade;
	}
	public List<EncontroInscricao> getListaInscricao() {
		return listaInscricao;
	}
	public void setListaInscricao(List<EncontroInscricao> listaInscricao) {
		this.listaInscricao = listaInscricao;
	}
	public List<Casal> getListaCoordenadores() {
		return listaCoordenadores;
	}
	public void setListaCoordenadores(List<Casal> listaCoordenadores) {
		this.listaCoordenadores = listaCoordenadores;
	}
	public List<EncontroTotalizacaoVO> getListaTotalizacao() {
		return listaTotalizacao;
	}
	public void setListaTotalizacao(List<EncontroTotalizacaoVO> listaTotalizacao) {
		this.listaTotalizacao = listaTotalizacao;
	}
	public List<AgrupamentoVO> getListaAgrupamentoVOEncontro() {
		return listaAgrupamentoVOEncontro;
	}
	public void setListaAgrupamentoVOEncontro(
			List<AgrupamentoVO> listaAgrupamentoVOEncontro) {
		this.listaAgrupamentoVOEncontro = listaAgrupamentoVOEncontro;
	}
	public List<EncontroConviteResponsavel> getListaResponsavelConvite() {
		return listaResponsavelConvite;
	}
	public void setListaResponsavelConvite(List<EncontroConviteResponsavel> listaResponsavelConvite) {
		this.listaResponsavelConvite = listaResponsavelConvite;
	}
	public int getQuantidadeInscricao(){
		if (qtdeInscricao!=null) return qtdeInscricao;
		int qtde=0;
		if (getListaInscricao()!= null){
			for (EncontroInscricao inscricao : getListaInscricao()) {
				if (!inscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO) && !inscricao.getTipo().equals(TipoInscricaoEnum.DOACAO))
					qtde++;
			}
		}
		qtdeInscricao = qtde;
		return qtde;
	}
	public int getQuantidadeInscricaoExterno(){
		if (qtdeInscricaoExterno!=null) return qtdeInscricaoExterno;
		int qtde=0;
		if (getListaInscricao()!= null){
			for (EncontroInscricao inscricao : getListaInscricao()) {
				if (inscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO))
					qtde++;
			}
		}
		qtdeInscricaoExterno = qtde;
		return qtde;
	}

	public int getQuantidadeInscricaoTotal(){
		return getListaInscricao().size();
	}

	public List<EncontroOrganogramaVO> getListaOrganogramaEncontroVO() {
		return listaOrganogramaEncontroVO;
	}
	public void setListaOrganogramaEncontroVO(
			List<EncontroOrganogramaVO> listaOrganogramaEncontroVO) {
		this.listaOrganogramaEncontroVO = listaOrganogramaEncontroVO;
	}
}