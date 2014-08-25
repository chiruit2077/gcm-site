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
		
		String none ="-";
		List<CasalItemVO> dadosRelatorio = new ArrayList<CasalItemVO>();
		CasalItemVO voEle, voEla;
		List<String> dadosItemEle = new ArrayList<String>();
		List<String> dadosItemEla = new ArrayList<String>();
		List<String> titulos = new ArrayList<String>();
		String valorCampo;
		int item = 0;
		for (Casal casal : listaCasal) {
			System.out.println(casal.toString());
			dadosItemEle = new ArrayList<String>();
			dadosItemEla = new ArrayList<String>();
			titulos = new ArrayList<String>();
			
			if(casalOpcaoRelatorioVO.getEmail()!=null && casalOpcaoRelatorioVO.getEmail()){
				titulos.add("Email");
				if(casal.getEle()!=null){
					dadosItemEle.add(casal.getEle().getEmail());
				} else {
					dadosItemEle.add(none);
				}
				if(casal.getEla()!=null){
					dadosItemEla.add(casal.getEla().getEmail());
				}else {
					dadosItemEla.add(none);
				}
			}
			if(casalOpcaoRelatorioVO.getEndereco()!=null && casalOpcaoRelatorioVO.getEndereco()){
				titulos.add("Endereço");
				dadosItemEle.add(casal.getEndereco());
				dadosItemEla.add(casal.getBairro() + " - " + casal.getCidade() + " " + casal.getEstado());
			}
			if(casalOpcaoRelatorioVO.getTelefone()!=null && casalOpcaoRelatorioVO.getTelefone()){
				titulos.add("Telefone");
				valorCampo = "";
				if(casal.getEle()!=null){
					if(casal.getEle().getTelefone()!=null && !casal.getEle().getTelefone().equals("")){
						valorCampo = casal.getEle().getTelefone() + " ";
					}
					if(casal.getEle().getTelefoneCelular()!=null && !casal.getEle().getTelefoneCelular().equals("")){
						valorCampo += "Cel: " + casal.getEle().getTelefoneCelular();
					}
				} else {
					valorCampo = none;
				}
				dadosItemEle.add(valorCampo);
				
				valorCampo = "";
				if(casal.getEla()!=null){
					if(casal.getEla().getTelefone()!=null && !casal.getEla().getTelefone().equals("")){
						valorCampo = casal.getEla().getTelefone() + " ";
					}
					if(casal.getEla().getTelefoneCelular()!=null && !casal.getEla().getTelefoneCelular().equals("")){
						valorCampo += "Cel: " + casal.getEla().getTelefoneCelular();
					}
				} else {
					valorCampo = none;
				}
				dadosItemEla.add(valorCampo);
			}
			
			if(casalOpcaoRelatorioVO.getDocumento()!=null && casalOpcaoRelatorioVO.getDocumento()){
				titulos.add("Documento");
				valorCampo = "";
				if(casal.getEle()!=null){
					if(casal.getEle().getRg()!=null && !casal.getEle().getRg().equals("")){
						valorCampo = "RG: " + casal.getEle().getRg();
					}
					if(casal.getEle().getExpedidor()!=null  && !casal.getEle().getExpedidor().equals("")){
						valorCampo += " " + casal.getEle().getExpedidor();
					}
					if(casal.getEle().getCpf()!=null && !casal.getEle().getCpf().equals("")){
						valorCampo += "\nCPF: " + casal.getEle().getCpf();
					}
				} else {
					valorCampo = none;
				}
				dadosItemEle.add(valorCampo);
				
				
				valorCampo = "";
				if(casal.getEla()!=null){
					if(casal.getEla().getRg()!=null &&  !casal.getEla().getRg().equals("")){
						valorCampo = "RG: " + casal.getEla().getRg();
					}
					if(casal.getEla().getExpedidor()!=null  && !casal.getEla().getExpedidor().equals("")){
						valorCampo += " " + casal.getEla().getExpedidor();
					}
					if(casal.getEla().getCpf()!=null && !casal.getEla().getCpf().equals("")){
						valorCampo += "\nCPF: " + casal.getEla().getCpf();
					}
				} else {
					valorCampo = none;
				}
				dadosItemEla.add(valorCampo);
			}
			if(casalOpcaoRelatorioVO.getApelido()!=null && casalOpcaoRelatorioVO.getApelido()){
				titulos.add("Apelido");
				if(casal.getEle()!=null){
					dadosItemEle.add(casal.getEle().getApelido());
				} else {
					dadosItemEle.add(none);
				}
				if(casal.getEla()!=null){
					dadosItemEla.add(casal.getEla().getApelido());
				} else {
					dadosItemEla.add(none);
				}
			}
			if(casalOpcaoRelatorioVO.getTipo()){
				titulos.add("Tipo");
				if(casal.getTipoCasal()!=null){
					dadosItemEle.add(casal.getTipoCasal().getNome());
				}
				dadosItemEla.add(null);
			}
			
			if(casalOpcaoRelatorioVO.getAlergia()!=null && casalOpcaoRelatorioVO.getAlergia()){
				titulos.add("Alérgico");
				valorCampo = "";
				if(casal.getEle()!=null){
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
				} else {
					valorCampo = none;
				}
				dadosItemEle.add(valorCampo);
				
				valorCampo = "";
				if(casal.getEla()!=null){
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
				} else {
					valorCampo = none;
				}
				dadosItemEla.add(valorCampo);
			}
			
			if(casalOpcaoRelatorioVO.getVegetariano()!=null && casalOpcaoRelatorioVO.getVegetariano()){
				titulos.add("Veget.");
				valorCampo = "Não";
				if(casal.getEle()!=null){
					if(casal.getEle().getVegetariano()!=null && casal.getEle().getVegetariano()){
						valorCampo = "Sim";
					}
				} else {
					valorCampo = none;
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "Não";
				if(casal.getEla()!=null){
					if(casal.getEla().getVegetariano()!=null && casal.getEla().getVegetariano()){
						valorCampo = "Sim";
					}
				} else {
					valorCampo = none;
				}
				dadosItemEla.add(valorCampo);
			}
			
			if(casalOpcaoRelatorioVO.getDiabetico()!=null && casalOpcaoRelatorioVO.getDiabetico()){
				titulos.add("Diabético");
				valorCampo = "Não";
				if(casal.getEle()!=null){
					if(casal.getEle().getDiabetico()!=null && casal.getEle().getDiabetico()){
						valorCampo = "Sim";
					}
				} else {
					valorCampo = none;
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "Não";
				if(casal.getEla()!=null){
					if(casal.getEla().getDiabetico()!=null && casal.getEla().getDiabetico()){
						valorCampo = "Sim";
					}
				} else {
					valorCampo = none;
				}
				dadosItemEla.add(valorCampo);
			}
			if(casalOpcaoRelatorioVO.getHipertenso()!=null && casalOpcaoRelatorioVO.getHipertenso()){
				titulos.add("Hipertenso");
				valorCampo = "Não";
				if(casal.getEle()!=null){
					if(casal.getEle().getDiabetico()!=null && casal.getEle().getHipertenso()){
						valorCampo = "Sim";
					}
				} else {
					valorCampo = none;
				}
				dadosItemEle.add(valorCampo);

				valorCampo = "Não";
				if(casal.getEla()!=null){
					if(casal.getEla().getDiabetico()!=null && casal.getEla().getHipertenso()){
						valorCampo = "Sim";
					}
				} else {
					valorCampo = none;
				}
				dadosItemEla.add(valorCampo);
			}
			
			voEle = new CasalItemVO();
			voEla = new CasalItemVO();
			if(casal.getEle()!=null){
				voEle.setNome(casal.getEle().getNome());
			} else {
				voEle.setNome(none);
			}
			if(casal.getEla()!=null){
				voEla.setNome(casal.getEla().getNome());
			} else {
				voEla.setNome(none);
			}
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