package br.com.ecc.server.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.Casal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroRelatoriosSecretariaImprimirCommand implements Callable<Integer>{

	@Inject Injector injector;
	@Inject EntityManager em;

	private List<Casal> listaCasal;
	private String titulo;

	@Override
	@Transactional
	public Integer call() throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("titulo", getTitulo());

		/*List<CasalItemVO> dadosRelatorio = new ArrayList<CasalItemVO>();
		CasalItemVO voEle, voEla;
		List<String> dadosItemEle = new ArrayList<String>();
		List<String> dadosItemEla = new ArrayList<String>();
		List<String> titulos = new ArrayList<String>();
		String valorCampo;
		int item = 0;
		for (Casal casal : listaCasal) {
			dadosItemEle = new ArrayList<String>();
			dadosItemEla = new ArrayList<String>();
			titulos = new ArrayList<String>();

			if(casalOpcaoRelatorioVO.getEmail()){
				titulos.add("Email");
				dadosItemEle.add(casal.getEle().getEmail());
				dadosItemEla.add(casal.getEla().getEmail());
			}
			if(casalOpcaoRelatorioVO.getEndereco()){
				titulos.add("Endereço");
				dadosItemEle.add(casal.getEndereco());
				dadosItemEla.add(casal.getBairro() + " - " + casal.getCidade() + " " + casal.getEstado());
			}
			if(casalOpcaoRelatorioVO.getTelefone()){
				titulos.add("Telefone");
				valorCampo = "";
				if(casal.getEle().getTelefone()!=null && !casal.getEle().getTelefone().equals("")){
					valorCampo = casal.getEle().getTelefone() + " ";
				}
				if(casal.getEle().getTelefoneCelular()!=null && !casal.getEle().getTelefoneCelular().equals("")){
					valorCampo += "Cel: " + casal.getEle().getTelefoneCelular();
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "";
				if(casal.getEla().getTelefone()!=null && !casal.getEla().getTelefone().equals("")){
					valorCampo = casal.getEla().getTelefone() + " ";
				}
				if(casal.getEla().getTelefoneCelular()!=null && !casal.getEla().getTelefoneCelular().equals("")){
					valorCampo += "Cel: " + casal.getEla().getTelefoneCelular();
				}
				dadosItemEla.add(valorCampo);
			}

			if(casalOpcaoRelatorioVO.getDocumento()){
				titulos.add("Documento");
				valorCampo = "";
				if(casal.getEle().getRg()!=null && !casal.getEle().getRg().equals("")){
					valorCampo = "RG: " + casal.getEle().getRg();
				}
				if(casal.getEle().getExpedidor()!=null  && !casal.getEle().getExpedidor().equals("")){
					valorCampo += " " + casal.getEle().getExpedidor();
				}
				if(casal.getEle().getCpf()!=null && !casal.getEle().getCpf().equals("")){
					valorCampo += "\nCPF: " + casal.getEle().getCpf();
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "";
				if(casal.getEla().getRg()!=null &&  !casal.getEla().getRg().equals("")){
					valorCampo = "RG: " + casal.getEla().getRg();
				}
				if(casal.getEla().getExpedidor()!=null  && !casal.getEla().getExpedidor().equals("")){
					valorCampo += " " + casal.getEla().getExpedidor();
				}
				if(casal.getEla().getCpf()!=null && !casal.getEla().getCpf().equals("")){
					valorCampo += "\nCPF: " + casal.getEla().getCpf();
				}
				dadosItemEla.add(valorCampo);
			}
			if(casalOpcaoRelatorioVO.getApelido()){
				titulos.add("Apelido");
				dadosItemEle.add(casal.getEle().getApelido());
				dadosItemEla.add(casal.getEla().getApelido());
			}
			if(casalOpcaoRelatorioVO.getTipo()){
				titulos.add("Tipo");
				dadosItemEle.add(casal.getTipoCasal().getNome());
				dadosItemEla.add(null);
			}

			if(casalOpcaoRelatorioVO.getAlergia()){
				titulos.add("Alérgico");
				valorCampo = "";
				if(casal.getEle().getAlergico()!=null){
					if(casal.getEle().getAlergico()){
						valorCampo = "Sim";
						if(casal.getEle().getAlergia()!=null){
							valorCampo += ": " + casal.getEle().getAlergia();
						}
					} else {
						valorCampo = "Não";
					}
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "";
				if(casal.getEla().getAlergico()!=null){
					if(casal.getEla().getAlergico()){
						valorCampo = "Sim";
						if(casal.getEla().getAlergia()!=null){
							valorCampo += ": " + casal.getEla().getAlergia();
						}
					} else {
						valorCampo = "Não";
					}
				}
				dadosItemEla.add(valorCampo);
			}
			if(casalOpcaoRelatorioVO.getVegetariano()){
				titulos.add("Veget.");
				valorCampo = "Não";
				if(casal.getEle().getVegetariano()!=null && casal.getEle().getVegetariano()){
					valorCampo = "Sim";
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "Não";
				if(casal.getEla().getVegetariano()!=null && casal.getEla().getVegetariano()){
					valorCampo = "Sim";
				}
				dadosItemEla.add(valorCampo);
			}

			if(casalOpcaoRelatorioVO.getDiabetico()){
				titulos.add("Diabético");
				valorCampo = "Não";
				if(casal.getEle().getDiabetico()!=null && casal.getEle().getDiabetico()){
					valorCampo = "Sim";
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "Não";
				if(casal.getEla().getDiabetico()!=null && casal.getEla().getDiabetico()){
					valorCampo = "Sim";
				}
				dadosItemEla.add(valorCampo);
			}

			voEle = new CasalItemVO();
			voEla = new CasalItemVO();
			voEle.setNome(casal.getEle().getNome());
			voEla.setNome(casal.getEla().getNome());
			voEle.setItem(item);
			voEla.setItem(item);
			int i=0;
			for (String titulo : titulos) {
				switch (i) {
				case 0:
					voEle.setTitulo1(titulo);
					voEle.setDado1(dadosItemEle.get(i));
					voEla.setDado1(dadosItemEla.get(i));
					break;
				case 1:
					voEle.setTitulo2(titulo);
					voEle.setDado2(dadosItemEle.get(i));
					voEla.setDado2(dadosItemEla.get(i));
					break;
				case 2:
					voEle.setTitulo3(titulo);
					voEle.setDado3(dadosItemEle.get(i));
					voEla.setDado3(dadosItemEla.get(i));
					break;
				case 3:
					voEle.setTitulo4(titulo);
					voEle.setDado4(dadosItemEle.get(i));
					voEla.setDado4(dadosItemEla.get(i));
					break;
				case 4:
					voEle.setTitulo5(titulo);
					voEle.setDado5(dadosItemEle.get(i));
					voEla.setDado5(dadosItemEla.get(i));
					break;
				case 5:
					voEle.setTitulo6(titulo);
					voEle.setDado6(dadosItemEle.get(i));
					voEla.setDado6(dadosItemEla.get(i));
					break;
				case 6:
					voEle.setTitulo7(titulo);
					voEle.setDado7(dadosItemEle.get(i));
					voEla.setDado7(dadosItemEla.get(i));
					break;
				case 7:
					voEle.setTitulo8(titulo);
					voEle.setDado8(dadosItemEle.get(i));
					voEla.setDado8(dadosItemEla.get(i));
					break;
				case 8:
					voEle.setTitulo9(titulo);
					voEle.setDado9(dadosItemEle.get(i));
					voEla.setDado9(dadosItemEla.get(i));
					break;
				case 9:
					voEle.setTitulo10(titulo);
					voEle.setDado10(dadosItemEle.get(i));
					voEla.setDado10(dadosItemEla.get(i));
					break;
				default:
					break;
				}
				i++;
			}
			dadosRelatorio.add(voEle);
			dadosRelatorio.add(voEla);
			item++;
		}

		if(dadosRelatorio.size()>0){
			ImprimirCommand cmd = injector.getInstance(ImprimirCommand.class);
			cmd.setDadosRelatorio(dadosRelatorio);
			cmd.setParametros(parametros);
			cmd.setReportPathName("/relatorios/");
			cmd.setReportFileName("listacasal.jrxml");
			cmd.setReportName("ListaCasal_"+new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime()));
			return cmd.call();
		} else {
			throw new WebException("Nenhum dado encontrado");
		}*/
		return 0;
	}

	public List<Casal> getListaCasal() {
		return listaCasal;
	}
	public void setListaCasal(List<Casal> listaCasal) {
		this.listaCasal = listaCasal;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}