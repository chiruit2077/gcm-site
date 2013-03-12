package br.com.ecc.server.service.secretaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.service.secretaria.EncontroRelatoriosSecretariaService;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.tipo.TipoArquivoEnum;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.ArquivoDigitalSalvarCommand;
import br.com.ecc.server.command.EncontroRelatoriosSecretariaImprimirCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroRelatoriosSecretariaServiceImpl extends SecureRemoteServiceServlet implements EncontroRelatoriosSecretariaService {

	private static final long serialVersionUID = 895888786302857306L;
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioRomantico(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaCasal(listaCasal);
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer geraCSV(Encontro encontro, String name) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();

		if (lista.size()>0){

			String dados = "Codigo;Ele;Ela;Ele Completo;Ela Completo;Padrinho;Madrinha;Quarto;";

			for (EncontroInscricao encontroInscricao : lista) {
				dados+= "\n" + encontroInscricao.getCodigo() + ";";
				dados+= encontroInscricao.getCasal().getEle().getApelido().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getEla().getApelido().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getEle().getNome().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getEla().getNome().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getCasalPadrinho().getEle().getNome().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getCasalPadrinho().getEla().getNome().toUpperCase() + ";" ;
				GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
				cmdQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotelInscricao");
				cmdQuarto.addParameter("encontro", encontro);
				cmdQuarto.addParameter("encontroinscricao1", encontroInscricao);
				List<EncontroHotelQuarto> quartos = cmdQuarto.call();
				if (quartos.size()==1){
					dados+= quartos.get(0).getQuarto().getNumeroQuarto().toUpperCase() + ";" ;
				}else
					dados+= ";";
			}

			ArquivoDigitalSalvarCommand cmdArquivo = injector.getInstance(ArquivoDigitalSalvarCommand.class);
			ArquivoDigital arquivoDigital = new ArquivoDigital();
			arquivoDigital.setNomeArquivo(name);
			arquivoDigital.setDados(dados.getBytes());
			arquivoDigital.setMimeType("text/csv");
			arquivoDigital.setTipo(TipoArquivoEnum.ARQUIVO);
			arquivoDigital.setTamanho(arquivoDigital.getDados().length);
			cmdArquivo.setArquivoDigital(arquivoDigital);
			ArquivoDigital call = cmdArquivo.call();
			return call.getId();
		}
		return null;
	}

}