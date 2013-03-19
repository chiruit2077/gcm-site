package br.com.ecc.client.ui.sistema.cadastro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Papel;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.AgrupamentoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AgrupamentoView extends BaseView<AgrupamentoPresenter> implements AgrupamentoPresenter.Display {

	@UiTemplate("AgrupamentoView.ui.xml")
	interface AgrupamentoViewUiBinder extends UiBinder<Widget, AgrupamentoView> {}
	private AgrupamentoViewUiBinder uiBinder = GWT.create(AgrupamentoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField TextBox nomeTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;

	@UiField CheckBox completoCheckBox;

	@UiField FlowPanel formularioFlowPanel;

	@UiField(provided=true) FlexTable agrupamentoFlexTable;
	private FlexTableUtil agrupamentoTableUtil = new FlexTableUtil();

	@UiField Label itemMembroTotal;
	@UiField(provided=true) FlexTable membroFlexTable;
	private FlexTableUtil membroTableUtil = new FlexTableUtil();

	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();

	@UiField(provided = true) SuggestBox pessoaSuggestBox;
	private final GenericEntitySuggestOracle pessoaSuggest = new GenericEntitySuggestOracle();

	@UiField ListBox tipoListBox;
	@UiField ListBox tipoAtividadeListBox;
	@UiField ListBox atividadeListBox;
	@UiField ListBox papelPadraoListBox;
	@UiField ListBox tipoInscricaoListBox;

	@UiField RadioButton grupoRadioButton;
	@UiField RadioButton encontroRadioButton;
	@UiField HTMLPanel encontroMembroHTMLPanel;
	@UiField HTMLPanel tipoAtividadeHTMLPanel;

	private Casal casalEditado;
	private Pessoa pessoaEditada;
	private List<Atividade> listaAtividades;
	private List<Papel> listaPapel;

	public AgrupamentoView() {
		criaTabela();
		criaTabelaMembro();

		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);

		pessoaSuggest.setMinimoCaracteres(2);
		pessoaSuggest.setSuggestQuery("pessoa.porNomeLike");
		pessoaSuggestBox = new SuggestBox(pessoaSuggest);

		casalSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				casalEditado = null;
				pessoaEditada = null;
				tipoInscricaoListBox.setSelectedIndex(0);
				if(!casalSuggestBox.getValue().equals("")){
					casalEditado = (Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue());
					pessoaSuggestBox.setValue(null);
				}
			}
		});
		pessoaSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				pessoaEditada = null;
				casalEditado = null;
				tipoInscricaoListBox.setSelectedIndex(0);
				if(!pessoaSuggestBox.getValue().equals("")){
					pessoaEditada = (Pessoa)ListUtil.getEntidadePorNome(pessoaSuggest.getListaEntidades(), pessoaSuggestBox.getValue());
					casalSuggestBox.setValue(null);
				}
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());

		ListBoxUtil.populate(tipoListBox, false, TipoInscricaoCasalEnum.values());
		ListBoxUtil.populate(tipoAtividadeListBox, true, TipoAtividadeEnum.values());
		formularioFlowPanel.setHeight((this.getWindowHeight() - 150) + "px");
	}

	private void criaTabela() {
		agrupamentoFlexTable = new FlexTable();
		agrupamentoFlexTable.setStyleName("portal-formSmall");
		agrupamentoTableUtil.initialize(agrupamentoFlexTable);

		agrupamentoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		agrupamentoTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		agrupamentoTableUtil.addColumn("Tipo", "100", HasHorizontalAlignment.ALIGN_LEFT);
		agrupamentoTableUtil.addColumn("Qtde", "50", HasHorizontalAlignment.ALIGN_CENTER);
	}

	private void criaTabelaMembro() {
		membroFlexTable = new FlexTable();
		membroFlexTable.setStyleName("portal-formSmall");
		membroTableUtil.initialize(membroFlexTable);

		membroTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		membroTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
		membroTableUtil.addColumn("Papel", "100", HasHorizontalAlignment.ALIGN_LEFT);
		membroTableUtil.addColumn("Rotulo", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}

	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		if(encontroRadioButton.getValue() && presenter.getEncontroSelecionado()==null){
			Window.alert("Encontro não selecionado");
			return;
		}
		edita(null);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		TipoInscricaoCasalEnum tipo = (TipoInscricaoCasalEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoCasalEnum.values());
		if (tipo == null){
			Window.alert("Escolha o Tipo!");
			return;
		}
		TipoAtividadeEnum tipoAtividade = (TipoAtividadeEnum) ListBoxUtil.getItemSelected(tipoAtividadeListBox, TipoAtividadeEnum.values());
		Atividade atividade = (Atividade) ListBoxUtil.getItemSelected(atividadeListBox, getListaAtividades());
		presenter.getAgrupamentoVO().getAgrupamento().setGrupo(null);
		presenter.getAgrupamentoVO().getAgrupamento().setEncontro(null);
		if(grupoRadioButton.getValue()){
			presenter.getAgrupamentoVO().getAgrupamento().setGrupo(presenter.getGrupoSelecionado());
		} else {
			presenter.getAgrupamentoVO().getAgrupamento().setEncontro(presenter.getEncontroSelecionado());
		}
		presenter.getAgrupamentoVO().getAgrupamento().setNome(nomeTextBox.getValue());
		presenter.getAgrupamentoVO().getAgrupamento().setTipo(tipo);
		presenter.getAgrupamentoVO().getAgrupamento().setAtividade(atividade);
		presenter.getAgrupamentoVO().getAgrupamento().setTipoAtividade(tipoAtividade);
		presenter.salvar();
	}
	private void edita(Agrupamento agrupamento) {
		limpaCampos();
		if(agrupamento == null){
			presenter.setAgrupamentoVO(new AgrupamentoVO());
			presenter.getAgrupamentoVO().setAgrupamento(new Agrupamento());
			presenter.getAgrupamentoVO().setListaMembros(new ArrayList<AgrupamentoMembro>());
			presenter.getAgrupamentoVO().getAgrupamento().setTipo(TipoInscricaoCasalEnum.ENCONTRISTA);
			for(TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
				if (TipoInscricaoCasalEnum.getPorInscricaoCasal(tipo).equals(TipoInscricaoCasalEnum.ENCONTRISTA))
					tipoInscricaoListBox.addItem(tipo.getNome());
			}
			ListBoxUtil.setItemSelected(tipoListBox, TipoInscricaoCasalEnum.ENCONTRISTA.getNome());
			LabelTotalUtil.setTotal(itemMembroTotal, 0, "membro", "membros", "");
			if (encontroRadioButton.getValue()){
				tipoAtividadeHTMLPanel.setVisible(true);
				membroTableUtil.setColumnVisible(2, true);
			}
		} else {
			presenter.getVO(agrupamento);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}

	public void limpaCampos(){
		tipoInscricaoListBox.clear();
		tipoInscricaoListBox.addItem("");
		tipoInscricaoListBox.addItem("Todos os inscritos pelo Tipo");
		tipoAtividadeListBox.setSelectedIndex(-1);
		atividadeListBox.setSelectedIndex(-1);
		papelPadraoListBox.setSelectedIndex(-1);
		nomeTextBox.setValue(null);
		membroTableUtil.clearData();
	}

	@UiHandler("tipoListBox")
	public void tipoListBoxChangeHandler(ChangeEvent event){
		TipoInscricaoCasalEnum tipoCasal = (TipoInscricaoCasalEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoCasalEnum.values());
		tipoInscricaoListBox.clear();
		tipoInscricaoListBox.addItem("");
		tipoInscricaoListBox.addItem("Todos os inscritos pelo Tipo");
		for(TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
			if (TipoInscricaoCasalEnum.getPorInscricaoCasal(tipo).equals(tipoCasal))
				tipoInscricaoListBox.addItem(tipo.getNome());
		}
		if (tipoCasal.equals(TipoInscricaoCasalEnum.AFILHADO)){
			tipoAtividadeHTMLPanel.setVisible(false);
			membroTableUtil.setColumnVisible(2, false);
			for (AgrupamentoMembro membro: presenter.getAgrupamentoVO().getListaMembros()) {
				membro.setPapel(null);
			}
		}
		else{
			if (encontroRadioButton.getValue()){
				tipoAtividadeHTMLPanel.setVisible(true);
				membroTableUtil.setColumnVisible(2, true);
			}
		}
	}

	@UiHandler("papelPadraoListBox")
	public void papelPadraoListBoxChangeHandler(ChangeEvent event){
		Papel papel = (Papel) ListBoxUtil.getItemSelected(papelPadraoListBox, getListaPapel());
		if (papel!=null){
			for (AgrupamentoMembro membro: presenter.getAgrupamentoVO().getListaMembros()) {
				membro.setPapel(papel);
			}
			populaMembros();
		}
		papelPadraoListBox.setSelectedIndex(0);
	}

	public void defineCampos(AgrupamentoVO agrupamentoVO){
		nomeTextBox.setValue(agrupamentoVO.getAgrupamento().getNome());
		if (agrupamentoVO.getAgrupamento().getTipo() != null){
			ListBoxUtil.setItemSelected(tipoListBox, agrupamentoVO.getAgrupamento().getTipo().getNome());
			if (agrupamentoVO.getAgrupamento().getTipo().equals(TipoInscricaoCasalEnum.AFILHADO))
				tipoAtividadeHTMLPanel.setVisible(false);
			else{
				if (encontroRadioButton.getValue())
					tipoAtividadeHTMLPanel.setVisible(true);
				if (agrupamentoVO.getAgrupamento().getTipoAtividade()!=null)
					ListBoxUtil.setItemSelected(tipoAtividadeListBox, agrupamentoVO.getAgrupamento().getTipoAtividade().getNome());
				if (agrupamentoVO.getAgrupamento().getAtividade()!=null)
					ListBoxUtil.setItemSelected(atividadeListBox, agrupamentoVO.getAgrupamento().getAtividade().getNome());
				for(TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
					if (TipoInscricaoCasalEnum.getPorInscricaoCasal(tipo).equals(agrupamentoVO.getAgrupamento().getTipo()))
						tipoInscricaoListBox.addItem(tipo.getNome());
				}
			}

		}
		populaMembros();
	}

	@Override
	public String getDisplayTitle() {
		return "Cadastro de Agrupamentos";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		agrupamentoTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<AgrupamentoVO> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "agrupamento", "agrupamentos", "");
		agrupamentoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (AgrupamentoVO vo: lista) {
			final Agrupamento agrupamento = vo.getAgrupamento();

			Object dados[] = new Object[4];

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(agrupamento);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este agrupamento ?")){
						presenter.excluir(agrupamento);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);

			dados[0] = hp;
			dados[1] = agrupamento.getNome();
			if (agrupamento.getTipo()!= null)
				dados[2] = agrupamento.getTipo().getNome();
			else
				dados[2] = "";
			dados[3] = vo.getListaMembros().size();
			agrupamentoTableUtil.addRow(dados,row+1);
			row++;
		}
		agrupamentoTableUtil.applyDataRowStyles();
	}
	public void populaMembros() {
		final boolean completo = completoCheckBox.getValue();
		Collections.sort(presenter.getAgrupamentoVO().getListaMembros(), new Comparator<AgrupamentoMembro>() {
			@Override
			public int compare(AgrupamentoMembro o1, AgrupamentoMembro o2) {
				String s1="", s2="";
				if(o1.getCasal()==null){
					if(completo){
						s1 = o1.getPessoa().toString();
					} else {
						s1 = o1.getPessoa().getApelido();
					}
				} else {
					if(completo){
						s1 = o1.getCasal().toString();
					} else {
						s1 = o1.getCasal().getApelidos("e");
					}
				}
				if(o2.getCasal()==null){
					if(completo){
						s2 = o2.getPessoa().toString();
					} else {
						s2 = o2.getPessoa().getApelido();
					}
				} else {
					if(completo){
						s2 = o2.getCasal().toString();
					} else {
						s2 = o2.getCasal().getApelidos("e");
					}
				}
				return s1.compareTo(s2);
			}
		});
		membroTableUtil.clearData();
		int row = 0;
		Image excluir;
		HorizontalPanel hp;
		for (final AgrupamentoMembro membro: presenter.getAgrupamentoVO().getListaMembros()) {
			Object dados[] = new Object[4];

			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este membro ?")){
						presenter.getAgrupamentoVO().getListaMembros().remove(membro);
						populaMembros();
					}
				}
			});

			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(excluir);

			dados[0] = hp;
			if(completo){
				dados[1] = membro.getCasal()==null?membro.getPessoa().toString():membro.getCasal().toString();
			} else {
				dados[1] = membro.getCasal()==null?membro.getPessoa().getApelido():membro.getCasal().getApelidos("e");
			}
			final ListBox listBox = new ListBox();
			ListBoxUtil.populate(listBox, true, getListaPapel());
			if (membro.getPapel()!=null)
				ListBoxUtil.setItemSelected(listBox, membro.getPapel().toString());
			listBox.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					membro.setPapel((Papel) ListBoxUtil.getItemSelected(listBox,getListaPapel()));
				}
			});
			dados[2] = listBox;
			TextBox textBox = new TextBox();
			textBox.setSize("195px", "100%");
			textBox.setMaxLength(50);
			textBox.setText(membro.getRotulo());
			textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					membro.setRotulo(event.getValue());
				}
			});
			dados[3] = textBox;
			membroTableUtil.addRow(dados,row+1);
			row++;
		}
		LabelTotalUtil.setTotal(itemMembroTotal, row, "membro", "membros", "");
		membroTableUtil.setColumnVisible(2,false);
		if (encontroRadioButton.getValue() && !presenter.getAgrupamentoVO().getAgrupamento().getTipo().equals(TipoInscricaoCasalEnum.AFILHADO))
			membroTableUtil.setColumnVisible(2,true);
		membroTableUtil.applyDataRowStyles();
	}

	@Override
	public void setVO(AgrupamentoVO vo) {
		defineCampos(vo);
	}

	@UiHandler("adicionarMembroButton")
	public void adicionarMembroButtonClickHandler(ClickEvent event){
		TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoInscricaoListBox, TipoInscricaoEnum.values());
		TipoInscricaoCasalEnum tipoCasal = (TipoInscricaoCasalEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoCasalEnum.values());
		AgrupamentoMembro membro = null;
		if(pessoaEditada!=null){
			membro = new AgrupamentoMembro();
			membro.setPessoa(pessoaEditada);
			if(!buscaMembro(membro)){
				presenter.getAgrupamentoVO().getListaMembros().add(membro);
			}
			pessoaSuggestBox.setValue(null);
		} else if (casalEditado!=null){
			membro = new AgrupamentoMembro();
			membro.setCasal(casalEditado);
			if(!buscaMembro(membro)){
				presenter.getAgrupamentoVO().getListaMembros().add(membro);
			}
			casalSuggestBox.setValue(null);
		} else if (presenter.getListaInscricoes().size()>0){
			for (EncontroInscricao ei : presenter.getListaInscricoes()) {
				if((tipoInscricaoListBox.getSelectedIndex()==1 && TipoInscricaoCasalEnum.getPorInscricaoCasal(ei.getTipo()).equals(tipoCasal) ) ||
						(tipo!=null && ei.getTipo().equals(tipo))){
					membro = new AgrupamentoMembro();
					if(ei.getCasal()!=null){
						membro.setCasal(ei.getCasal());
					} else {
						membro.setPessoa(ei.getPessoa());
					}
					if(!buscaMembro(membro)){
						presenter.getAgrupamentoVO().getListaMembros().add(membro);
					}
				}
			}
		}
		if(membro!=null){
			populaMembros();
		}
	}

	private boolean buscaMembro(AgrupamentoMembro membro){
		for (AgrupamentoMembro m : presenter.getAgrupamentoVO().getListaMembros()) {
			if(m.getCasal()!=null && membro.getCasal()!=null && m.getCasal().getId().equals(membro.getCasal().getId())){
				return true;
			}
			if(m.getPessoa()!=null && membro.getPessoa()!=null && m.getPessoa().getId().equals(membro.getPessoa().getId())){
				return true;
			}
		}
		return false;
	}

	@UiHandler("completoCheckBox")
	public void completoCheckBoxClickHandler(ClickEvent event){
		populaMembros();
	}

	@UiHandler("excluirMembroButton")
	public void excluirMembroButtonClickHandler(ClickEvent event){
		if(Window.confirm("Deseja excluir todos os membros deste agrupamento?")){
			presenter.getAgrupamentoVO().setListaMembros(new ArrayList<AgrupamentoMembro>());
			populaMembros();
		}
	}
	@UiHandler("grupoRadioButton")
	public void grupoRadioButtonClickHandler(ClickEvent event){
		agrupamentoTableUtil.clearData();
		itemTotal.setText("Nenhum item adicionado");
		if(grupoRadioButton.getValue()){
			encontroMembroHTMLPanel.setVisible(false);
			tipoAtividadeHTMLPanel.setVisible(false);
			presenter.buscaAgrupamentos(presenter.getGrupoSelecionado());
		} else {
			encontroMembroHTMLPanel.setVisible(true);
			presenter.buscaAgrupamentos(presenter.getEncontroSelecionado());
		}
	}

	@UiHandler("encontroRadioButton")
	public void encontroRadioButtonClickHandler(ClickEvent event){
		grupoRadioButtonClickHandler(null);
	}

	@Override
	public void repopula() {
		if(grupoRadioButton.getValue()){
			presenter.buscaAgrupamentos(presenter.getGrupoSelecionado());
		} else {
			presenter.buscaAgrupamentos(presenter.getEncontroSelecionado());
		}

	}

	@Override
	public void populaAtividades(List<Atividade> lista) {
		setListaAtividades(lista);
		ListBoxUtil.populate(atividadeListBox, true, lista);

	}

	@Override
	public void populaPapeis(List<Papel> lista) {
		setListaPapel(lista);
		ListBoxUtil.populate(papelPadraoListBox, true, lista);

	}

	public List<Papel> getListaPapel() {
		return listaPapel;
	}

	public void setListaPapel(List<Papel> listaPapel) {
		this.listaPapel = listaPapel;
	}

	public List<Atividade> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<Atividade> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

}