package br.com.ecc.server.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.Casal;
import br.com.ecc.model.vo.CasalItemVO;
import br.com.ecc.model.vo.CasalOpcaoRelatorioVO;
import br.com.ecc.server.command.basico.ImprimirCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class CasalListaImprimirCommand implements Callable<Integer>{

	@Inject Injector injector;
	@Inject EntityManager em;

	private List<Casal> listaCasal;
	private CasalOpcaoRelatorioVO casalOpcaoRelatorioVO;

	@Override
	@Transactional
	public Integer call() throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("titulo", casalOpcaoRelatorioVO.getTitulo());
		
		List<CasalItemVO> dadosRelatorio = new ArrayList<CasalItemVO>();
		CasalItemVO vo;
		List<String> dadosItemEle = new ArrayList<String>();
		List<String> dadosItemEla = new ArrayList<String>();
		List<String> titulos = new ArrayList<String>();
		String valorCampo;
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
				dadosItemEla.add(null);
			}
			if(casalOpcaoRelatorioVO.getTelefone()){
				titulos.add("Telefone");
				valorCampo = "";
				if(casal.getEle().getTelefone()!=null){
					valorCampo = casal.getEle().getTelefone();
				}
				if(casal.getEle().getTelefoneCelular()!=null){
					valorCampo += " Cel: " + casal.getEle().getTelefoneCelular();
				}
				dadosItemEle.add(valorCampo);
				
				valorCampo = "";
				if(casal.getEla().getTelefone()!=null){
					valorCampo = casal.getEla().getTelefone();
				}
				if(casal.getEla().getTelefoneCelular()!=null){
					valorCampo += " Cel: " + casal.getEla().getTelefoneCelular();
				}
				dadosItemEla.add(valorCampo);
			}
			
			if(casalOpcaoRelatorioVO.getDocumento()){
				titulos.add("Documento");
				valorCampo = "";
				if(casal.getEle().getRg()!=null){
					valorCampo = "RG: " + casal.getEle().getRg();
				}
				if(casal.getEle().getExpedidor()!=null){
					valorCampo += " " + casal.getEle().getExpedidor();
				}
				if(casal.getEle().getCpf()!=null){
					valorCampo += " CPF: " + casal.getEle().getCpf();
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "";
				if(casal.getEla().getRg()!=null){
					valorCampo = "RG: " + casal.getEla().getRg();
				}
				if(casal.getEla().getExpedidor()!=null){
					valorCampo += " " + casal.getEla().getExpedidor();
				}
				if(casal.getEla().getCpf()!=null){
					valorCampo += " CPF: " + casal.getEla().getCpf();
				}
				dadosItemEla.add(valorCampo);
			}
			if(casalOpcaoRelatorioVO.getApelido()){
				titulos.add("Apelido");
				dadosItemEle.add(casal.getEle().getApelido());
				dadosItemEla.add(casal.getEla().getApelido());
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
				titulos.add("Vegetariano");
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
			
			vo = new CasalItemVO();
			vo.setNomeEle(casal.getEle().getNome());
			vo.setNomeEla(casal.getEla().getNome());
			int i=0;
			for (String titulo : titulos) {
				switch (i) {
				case 0:
					vo.setTitulo1(titulo);
					vo.setDado1Ele(dadosItemEle.get(i));
					vo.setDado1Ela(dadosItemEla.get(i));
					break;
				case 1:
					vo.setTitulo2(titulo);
					vo.setDado2Ele(dadosItemEle.get(i));
					vo.setDado2Ela(dadosItemEla.get(i));
					break;
				case 2:
					vo.setTitulo3(titulo);
					vo.setDado3Ele(dadosItemEle.get(i));
					vo.setDado3Ela(dadosItemEla.get(i));
					break;
				case 3:
					vo.setTitulo4(titulo);
					vo.setDado4Ele(dadosItemEle.get(i));
					vo.setDado4Ela(dadosItemEla.get(i));
					break;
				case 4:
					vo.setTitulo5(titulo);
					vo.setDado5Ele(dadosItemEle.get(i));
					vo.setDado5Ela(dadosItemEla.get(i));
					break;
				case 5:
					vo.setTitulo6(titulo);
					vo.setDado6Ele(dadosItemEle.get(i));
					vo.setDado6Ela(dadosItemEla.get(i));
					break;
				case 6:
					vo.setTitulo7(titulo);
					vo.setDado7Ele(dadosItemEle.get(i));
					vo.setDado7Ela(dadosItemEla.get(i));
					break;
				case 7:
					vo.setTitulo8(titulo);
					vo.setDado8Ele(dadosItemEle.get(i));
					vo.setDado8Ela(dadosItemEla.get(i));
					break;
				case 8:
					vo.setTitulo9(titulo);
					vo.setDado9Ele(dadosItemEle.get(i));
					vo.setDado9Ela(dadosItemEla.get(i));
					break;
				case 9:
					vo.setTitulo10(titulo);
					vo.setDado10Ele(dadosItemEle.get(i));
					vo.setDado10Ela(dadosItemEla.get(i));
					break;
				default:
					break;
				}
				i++;
			}
			dadosRelatorio.add(vo);
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
		}
	}
	
	public List<Casal> getListaCasal() {
		return listaCasal;
	}
	public void setListaCasal(List<Casal> listaCasal) {
		this.listaCasal = listaCasal;
	}

	public CasalOpcaoRelatorioVO getCasalOpcaoRelatorioVO() {
		return casalOpcaoRelatorioVO;
	}

	public void setCasalOpcaoRelatorioVO(CasalOpcaoRelatorioVO casalOpcaoRelatorioVO) {
		this.casalOpcaoRelatorioVO = casalOpcaoRelatorioVO;
	}
}