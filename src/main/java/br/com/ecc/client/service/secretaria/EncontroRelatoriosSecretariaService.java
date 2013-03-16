package br.com.ecc.client.service.secretaria;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroRelatoriosSecretaria")
public interface EncontroRelatoriosSecretariaService extends RemoteService {
	public Integer imprimeRelatorioRomantico(Encontro encontro) throws Exception;
	public Integer geraCSV(Encontro encontro, String name) throws Exception;
	public Integer imprimeRelatorioAgrupamento(Encontro encontro, Agrupamento agrupamento)	throws Exception;
	public Integer imprimeRelatorioOnibus(Encontro encontro) throws Exception;
	public Integer imprimeRelatorioAlbum(Encontro encontro) throws Exception;
	public Integer imprimeRelatorioNecessidadesEspeciais(Encontro encontro) throws Exception;
	public Integer imprimeRelatorioDiabeticosVegetarianos(Encontro encontro) throws Exception;
	public Integer imprimeRelatorioHotelAfilhados(EncontroHotel encontroHotel) throws Exception;
	public Integer imprimeRelatorioHotelEncontristas(EncontroHotel encontroHotel) throws Exception;
	public Integer imprimeRelatorioRecepcaoInicial(Encontro encontro) throws Exception;
	public Integer imprimeRelatorioRecepcaoFinal(Encontro encontro) throws Exception;
	public Integer imprimeRelatorioOracaoAmor(Encontro encontro) throws Exception;
}