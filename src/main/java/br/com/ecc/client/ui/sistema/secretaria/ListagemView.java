package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Casal;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.CasalOpcaoRelatorioVO;
import br.com.ecc.model.vo.CasalParamVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListagemView extends BaseView<ListagemPresenter> implements ListagemPresenter.Display {

	@UiTemplate("ListagemView.ui.xml")
	interface ListagemViewUiBinder extends UiBinder<Widget, ListagemView> {}
	private ListagemViewUiBinder uiBinder = GWT.create(ListagemViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField Label itemListagemTotal;
	@UiField VerticalPanel centralPanel;
	
	@UiField(provided=true) FlexTable casalFlexTable;
	private FlexTableUtil casalTableUtil = new FlexTableUtil();
	
	@UiField(provided=true) FlexTable casalListagemFlexTable;
	private FlexTableUtil casalListagemTableUtil = new FlexTableUtil();
	
	@UiField TextBox nomeTextBox;
	@UiField ListBox agrupamentoListBox;
	@UiField ListBox tipoInscricaoListBox;
	@UiField ListBox tipoListBox;
	
	@UiField TextBox tituloTextBox;
	@UiField CheckBox apelidoCheckBox;
	@UiField CheckBox emailCheckBox;
	@UiField CheckBox enderecoCheckBox;
	@UiField CheckBox documentoCheckBox;
	@UiField CheckBox telefoneCheckBox;
	@UiField CheckBox alergiaCheckBox;
	@UiField CheckBox vegetarianoCheckBox;
	@UiField CheckBox diabeticoCheckBox;
	@UiField CheckBox tipoCheckBox;
	
	@UiField DialogBox emailDialogBox;
	@UiField TextArea emailTextArea;
	
	private List<Agrupamento> listaAgrupamento;
	private List<Casal> listaCasalPesquisa = new ArrayList<Casal>();

	public ListagemView() {
		criaTabela();
		criaTabelaListagem();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());

		tipoInscricaoListBox.addItem("");
		tipoInscricaoListBox.addItem("Todos os inscritos");
		for (TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
			tipoInscricaoListBox.addItem(tipo.toString());
		}
		
		tipoListBox.addItem("");
		for (TipoCasalEnum tipo : TipoCasalEnum.values()) {
			tipoListBox.addItem(tipo.toString());
		}
		
		nomeTextBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				tipoInscricaoListBox.setSelectedIndex(0);
				int keyCode = event.getNativeKeyCode();
				if (keyCode == KeyCodes.KEY_ENTER) {
					buscarButtonClickHandler(null);
				}
			}
		});
	}
	
	private void criaTabela() {
		casalFlexTable = new FlexTable();
		casalFlexTable.setStyleName("portal-formSmall");
		casalTableUtil.initialize(casalFlexTable);
		
		casalTableUtil.addColumn("", "10", HasHorizontalAlignment.ALIGN_CENTER);
		casalTableUtil.addColumn("Apelidos", "140", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Nomes", "230", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Emails", "200", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Telefone", "150", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Residencial", "100", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("A", "30", HasHorizontalAlignment.ALIGN_CENTER);
		casalTableUtil.addColumn("D", "30", HasHorizontalAlignment.ALIGN_CENTER);
		casalTableUtil.addColumn("V", "30", HasHorizontalAlignment.ALIGN_CENTER);
		casalTableUtil.addColumn("Tipo", "50", HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	private void criaTabelaListagem() {
		casalListagemFlexTable = new FlexTable();
		casalListagemFlexTable.setStyleName("portal-formSmall");
		casalListagemTableUtil.initialize(casalListagemFlexTable);
		
		casalListagemTableUtil.addColumn("", "10", HasHorizontalAlignment.ALIGN_CENTER);
		casalListagemTableUtil.addColumn("Apelidos", "140", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Nomes", "230", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Emails", "200", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Telefone", "150", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("Residencial", "100", HasHorizontalAlignment.ALIGN_LEFT);
		casalListagemTableUtil.addColumn("A", "30", HasHorizontalAlignment.ALIGN_CENTER);
		casalListagemTableUtil.addColumn("D", "30", HasHorizontalAlignment.ALIGN_CENTER);
		casalListagemTableUtil.addColumn("V", "30", HasHorizontalAlignment.ALIGN_CENTER);
		casalListagemTableUtil.addColumn("Tipo", "50", HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	@UiHandler("buscarButton")
	public void buscarButtonClickHandler(ClickEvent event){
		CasalParamVO vo = new CasalParamVO();
		vo.setGrupo(presenter.getGrupoSelecionado());
		vo.setNome(nomeTextBox.getValue());
		vo.setAgrupamento((Agrupamento) ListBoxUtil.getItemSelected(agrupamentoListBox, listaAgrupamento));
		vo.setTipoInscricao((TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoInscricaoListBox, TipoInscricaoEnum.values()));
		vo.setTipoCasal((TipoCasalEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoCasalEnum.values()));
		vo.setTodosInscritos(false);
		if(tipoInscricaoListBox.getSelectedIndex()==1){
			vo.setTodosInscritos(true);
		}
		if(vo.getTipoInscricao()!=null || vo.getTodosInscritos()){
			vo.setEncontro(presenter.getEncontroSelecionado());
		}
		presenter.buscaCasais(vo);
	}
	
	@UiHandler("limparButton")
	public void limparButtonClickHandler(ClickEvent event){
		casalTableUtil.clearData();
	}
	
	@UiHandler("limparListagemButton")
	public void limparListagemButtonClickHandler(ClickEvent event){
		casalListagemTableUtil.clearData();
		presenter.setListaCasal(new ArrayList<Casal>());
	}
	
	@UiHandler("imprimirButton")
	public void imprimirButtonClickHandler(ClickEvent event){
		CasalOpcaoRelatorioVO casalOpcaoRelatorioVO = new CasalOpcaoRelatorioVO();
		casalOpcaoRelatorioVO.setTitulo(tituloTextBox.getValue());
		casalOpcaoRelatorioVO.setAlergia(alergiaCheckBox.getValue());
		casalOpcaoRelatorioVO.setApelido(apelidoCheckBox.getValue());
		casalOpcaoRelatorioVO.setDiabetico(diabeticoCheckBox.getValue());
		casalOpcaoRelatorioVO.setDocumento(documentoCheckBox.getValue());
		casalOpcaoRelatorioVO.setEmail(emailCheckBox.getValue());
		casalOpcaoRelatorioVO.setEndereco(enderecoCheckBox.getValue());
		casalOpcaoRelatorioVO.setTelefone(telefoneCheckBox.getValue());
		casalOpcaoRelatorioVO.setVegetariano(vegetarianoCheckBox.getValue());
		casalOpcaoRelatorioVO.setTipo(tipoCheckBox.getValue());
		presenter.imprimir(casalOpcaoRelatorioVO);
	}
	
	@UiHandler("emailButton")
	public void emailButtonClickHandler(ClickEvent event){
		String emails = "";
		for (Casal casal : presenter.getListaCasal()) {
			if(!emails.equals("")){
				emails += ", ";	
			}
			emails += casal.getEmails(", ");
		}
		emailTextArea.setValue(emails);
		emailDialogBox.center();
		emailDialogBox.show();
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		emailDialogBox.hide();
	}
	
	
	@UiHandler("adicionarButton")
	public void adicionarButtonClickHandler(ClickEvent event){
		for (Casal casal : listaCasalPesquisa) {
			if(presenter.getListaCasal().indexOf(casal)<0){
				presenter.getListaCasal().add(casal);
			}
		}
		populaEntidades(presenter.getListaCasal(), true);
	}
	
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	
	
	@Override
	public void init(){
		nomeTextBox.setFocus(true);
	}
	
	@Override
	public String getDisplayTitle() {
		return "Listagem de Casais";
	}

	@Override
	public void reset() {
	}
	
	@Override
	public void populaEntidades(List<Casal> lista, final Boolean listagem) {
		Collections.sort(lista, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getApelidos("").compareTo(o2.getApelidos(""));
			}
		});
		if(listagem){
			presenter.setListaCasal(lista);
			LabelTotalUtil.setTotal(itemListagemTotal, lista.size(), "casal", "casais", "");
			casalListagemTableUtil.clearData();
		} else {
			listaCasalPesquisa = lista;
			LabelTotalUtil.setTotal(itemTotal, lista.size(), "casal", "casais", "");
			casalTableUtil.clearData();
		}
		int row = 0;
		Image imagem;
		HorizontalPanel hp;
		String t = "";
		for (final Casal casal: lista) {
			Object dados[] = new Object[10];
			
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			if(listagem){
				imagem = new Image("images/delete.png");
				imagem.setTitle("Excluir este casal");
			} else {
				imagem = new Image("images/add.png");
				imagem.setTitle("Adicionar este casal");
			}
			imagem.setStyleName("portal-ImageCursor");
			imagem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(listagem){
						presenter.getListaCasal().remove(casal);
						populaEntidades(presenter.getListaCasal(), true);
					} else {
						if(presenter.getListaCasal().indexOf(casal)<0){
							presenter.getListaCasal().add(casal);
							populaEntidades(presenter.getListaCasal(), true);
						} else {
							Window.alert("Casal já adicionado");
						}
					}
				}
			});
			hp.add(imagem);
			
			dados[0] = hp;
			dados[1] = casal.getApelidos("e");
			
			t = "";
			if(casal.getEle()!=null){
				t = casal.getEle().getNome()+ "<br>" ;
			}
			if(casal.getEla()!=null){
				t += casal.getEla().getNome();
			}
			dados[2] = new HTML(t);
			
			t = "";
			if(casal.getEle()!=null){
				t = (casal.getEle().getEmail()==null?"&nbsp;":casal.getEle().getEmail()) + "<br>";
			}
			if(casal.getEla()!=null){
				t = (casal.getEla().getEmail()==null?"&nbsp;":casal.getEla().getEmail());
			}
			dados[3] = new HTML(t);
			t = "";
			if(casal.getEle()!=null){
				t = (casal.getEle().getTelefoneCelular()==null?"&nbsp;":casal.getEle().getTelefoneCelular()) + "<br>";
			}
			if(casal.getEla()!=null){
				t = (casal.getEla().getTelefoneCelular()==null?"&nbsp;":casal.getEla().getTelefoneCelular());
			}
			dados[4] = new HTML(t);
			dados[5] = casal.getTelefone();
			t = "";
			if(casal.getEle()!=null){
				t = (casal.getEle().getAlergico()!=null&&casal.getEle().getAlergico()?"Sim":"Não") + "<br>";
			}
			if(casal.getEla()!=null){
				t = (casal.getEla().getAlergico()!=null&&casal.getEla().getAlergico()?"Sim":"Não");
			}
			dados[6] = new HTML(t);
			t = "";
			if(casal.getEle()!=null){
				t = (casal.getEle().getDiabetico()!=null&&casal.getEle().getDiabetico()?"Sim":"Não") + "<br>";
			}
			if(casal.getEla()!=null){
				t = (casal.getEla().getDiabetico()!=null&&casal.getEla().getDiabetico()?"Sim":"Não");
			}
			dados[7] = new HTML(t);
			t = "";
			if(casal.getEle()!=null){
				t = (casal.getEle().getVegetariano()!=null&&casal.getEle().getVegetariano()?"Sim":"Não") + "<br>";
			}
			if(casal.getEla()!=null){
				t = (casal.getEla().getVegetariano()!=null&&casal.getEla().getVegetariano()?"Sim":"Não");
			}
			dados[8] = new HTML(t);
			dados[9] = casal.getTipoCasal()==null?"":casal.getTipoCasal().getNome(); 
			if(listagem){
				casalListagemTableUtil.addRow(dados,row+1);
			} else {
				casalTableUtil.addRow(dados,row+1);
			}
			row++;
		}
		if(listagem){
			casalListagemTableUtil.applyDataRowStyles();
		} else {
			casalTableUtil.applyDataRowStyles();
		}
	}

	@Override
	public void populaAgrupamento(List<Agrupamento> result) {
		this.listaAgrupamento = result;
		agrupamentoListBox.clear();
		agrupamentoListBox.addItem("");
		for(Agrupamento agrupamento : result) {
			agrupamentoListBox.addItem(agrupamento.toString());
		}
	}
	
	@UiHandler("agrupamentoListBox")
	public void agrupamentoListBoxChangeHandler(ChangeEvent event) {
		nomeTextBox.setValue(null);
		tipoInscricaoListBox.setSelectedIndex(0);
	}
	
	@UiHandler("tipoInscricaoListBox")
	public void tipoInscricaoListBoxChangeHandler(ChangeEvent event) {
		agrupamentoListBox.setSelectedIndex(0);
		nomeTextBox.setValue(null);
	}
}