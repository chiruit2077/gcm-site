package br.com.ecc.client.ui.sistema.secretaria;

import java.util.Collections;
import java.util.Comparator;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.CasalVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListagemSorteioView extends BaseView<ListagemSorteioPresenter> implements ListagemSorteioPresenter.Display {

	@UiTemplate("ListagemSorteioView.ui.xml")
	interface ListagemSorteioViewUiBinder extends UiBinder<Widget, ListagemSorteioView> {}
	private ListagemSorteioViewUiBinder uiBinder = GWT.create(ListagemSorteioViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField VerticalPanel centralPanel;
	@UiField ListBox ordemListBox;

	@UiField(provided=true) FlexTable casalListagemFlexTable;
	private FlexTableUtil casalListagemTableUtil = new FlexTableUtil();

	public ListagemSorteioView() {
		criaTabelaListagem();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		
		ordemListBox.addItem("Apelido");
		ordemListBox.addItem("Nome");
		ordemListBox.addItem("Emails");
		ordemListBox.addItem("Ult.Encontro");
		ordemListBox.setSelectedIndex(0);
		ordemListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				populaEntidades();
			}
		});
	}

	private void criaTabelaListagem() {
		casalListagemFlexTable = new FlexTable();
		casalListagemFlexTable.setStyleName("portal-formSmall");
		casalListagemTableUtil.initialize(casalListagemFlexTable);

		casalListagemTableUtil.addColumn("Apelidos", "140", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Nomes", null, HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Emails", "200", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Telefone", "150", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Residencial", "100", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Tipo", "80", HasHorizontalAlignment.ALIGN_CENTER);
		casalListagemTableUtil.addColumn("Ult.Encontro", "80", HasHorizontalAlignment.ALIGN_CENTER);
	}

	@UiHandler("imprimirButton")
	public void imprimirButtonClickHandler(ClickEvent event){
		//presenter.imprimir(listaCasalSelecionado);
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@Override
	public String getDisplayTitle() {
		return "Intenção de participação no próximo encontro";
	}

	@Override
	public void reset() {
	}

	@Override
	public void populaEntidades() {
		Collections.sort(presenter.getListaCasal(), new Comparator<CasalVO>() {
			@Override
			public int compare(CasalVO o1, CasalVO o2) {
				if(ordemListBox.getSelectedIndex()==0){
					return o1.getCasal().getApelidos("").compareTo(o2.getCasal().getApelidos(""));
				} else if(ordemListBox.getSelectedIndex()==1){
					return o1.getCasal().toString().compareTo(o2.getCasal().toString());
				} else if(ordemListBox.getSelectedIndex()==2){
					if(o1.getUltimaParticipacao()==null) return -1;
					if(o2.getUltimaParticipacao()==null) return -1;
					return o1.getUltimaParticipacao().toString().compareTo(o2.getUltimaParticipacao().toString());
				} else {
					return o1.getCasal().getEmails("").compareTo(o2.getCasal().getEmails(""));
				}
			}
		});
		casalListagemTableUtil.clearData();
		int rowA = 0;
		String t = "";
		for (final CasalVO casalVO: presenter.getListaCasal()) {
			Object dados[] = new Object[7];

			final int row = rowA;
			
			dados[0] = casalVO.getCasal().getApelidos("e");

			t = "";
			if(casalVO.getCasal().getEle()!=null){
				t = casalVO.getCasal().getEle().getNome()+ "<br>" ;
			}
			if(casalVO.getCasal().getEla()!=null){
				t += casalVO.getCasal().getEla().getNome();
			}
			dados[1] = new HTML(t);

			t = "";
			if(casalVO.getCasal().getEle()!=null){
				t = ((casalVO.getCasal().getEle().getEmail()!=null && !casalVO.getCasal().getEle().getEmail().equals(""))?casalVO.getCasal().getEle().getEmail():"&nbsp;") + "<br>";
			}
			if(casalVO.getCasal().getEla()!=null){
				t += ((casalVO.getCasal().getEla().getEmail()!=null && !casalVO.getCasal().getEla().getEmail().equals(""))?casalVO.getCasal().getEla().getEmail():"&nbsp;");
			}
			dados[2] = new HTML(t);
			t = "";
			if(casalVO.getCasal().getEle()!=null){
				t = ((casalVO.getCasal().getEle().getTelefoneCelular()!=null && !casalVO.getCasal().getEle().getTelefoneCelular().equals(""))?casalVO.getCasal().getEle().getTelefoneCelular():"&nbsp;") + "<br>";
			}
			if(casalVO.getCasal().getEla()!=null){
				t += ((casalVO.getCasal().getEla().getTelefoneCelular()!=null && !casalVO.getCasal().getEla().getTelefoneCelular().equals(""))?casalVO.getCasal().getEla().getTelefoneCelular():"&nbsp;");
			}
			dados[3] = new HTML(t);
			dados[4] = casalVO.getCasal().getTelefone();
			
			final ListBox tipoInscricao = new ListBox();
			tipoInscricao.addItem("");
			tipoInscricao.addItem(TipoInscricaoEnum.PADRINHO.toString());
			tipoInscricao.addItem(TipoInscricaoEnum.APOIO.toString());
			dados[5] = tipoInscricao;
			
			
			if(casalVO.getUltimaParticipacao()!=null){
				dados[6] = casalVO.getUltimaParticipacao().toString();
				casalListagemTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialBkYellow");
			} else {
				dados[6] = "Não foram";
			}
			
			if(casalVO.getCasalSorteio()!=null){
				casalListagemTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialBkOrange");
				if(casalVO.getCasalSorteio().getTipo().equals(TipoInscricaoEnum.PADRINHO)){
					tipoInscricao.setSelectedIndex(1);
				} else if(casalVO.getCasalSorteio().getTipo().equals(TipoInscricaoEnum.APOIO)){
					tipoInscricao.setSelectedIndex(2);
				}
			}
			tipoInscricao.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent arg0) {
					if(tipoInscricao.getSelectedIndex()==1){
						presenter.salvar(casalVO.getCasal(), TipoInscricaoEnum.PADRINHO);
						casalListagemTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialBkOrange");
					} else if(tipoInscricao.getSelectedIndex()==2){
						presenter.salvar(casalVO.getCasal(), TipoInscricaoEnum.APOIO);
						casalListagemTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialBkOrange");
					} else {
						presenter.excluir(casalVO.getCasalSorteio());
						casalListagemTableUtil.removeRowSpecialStyle(row+1, "FlexTable-RowSpecialBkOrange");
					}
					if(casalVO.getUltimaParticipacao()!=null){
						casalListagemTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialBkYellow");
					}
				}
			});
			casalListagemTableUtil.addRow(dados,row+1);
			rowA++;
		}
		casalListagemTableUtil.applyDataRowStyles();
	}
}