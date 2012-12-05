package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.richtext.RichTextToolbar;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoMensagemEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;
import br.com.ecc.model.tipo.TipoVariavelEnum;
import br.com.ecc.model.vo.MensagemVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MensagemView extends BaseView<MensagemPresenter> implements MensagemPresenter.Display {

	@UiTemplate("MensagemView.ui.xml")
	interface MensagemViewUiBinder extends UiBinder<Widget, MensagemView> {}
	private MensagemViewUiBinder uiBinder = GWT.create(MensagemViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField ListBox tipoListBox;
	
	@UiField TextBox tituloTextBox;
	@UiField TextBox descricaoTextBox;
	@UiField(provided=true) RichTextArea mensagemRichTextArea;
	@UiField(provided=true) RichTextToolbar mensagemRichTextToolbar;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField CheckBox completoCheckBox;
	@UiField Label itemDestinatarioTotal;
	
	@UiField(provided=true) FlexTable mensagemFlexTable;
	private FlexTableUtil mensagemTableUtil = new FlexTableUtil();
	
	@UiField(provided=true) FlexTable destinatarioFlexTable;
	private FlexTableUtil destinatarioTableUtil = new FlexTableUtil();
	
	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();
	
	@UiField(provided = true) SuggestBox pessoaSuggestBox;
	private final GenericEntitySuggestOracle pessoaSuggest = new GenericEntitySuggestOracle();
	
	@UiField ListBox agrupamentoListBox;
	@UiField ListBox encontroListBox;
	@UiField ListBox variaveisListBox;
	
	@UiField RadioButton grupoRadioButton;
	@UiField RadioButton encontroRadioButton;
	@UiField HTMLPanel encontroHTMLPanel;

	@UiField ListBox tipoFiltroListBox;
	
	private List<Agrupamento> listaAgrupamento;
	
	private Casal casalEditado;
	private Pessoa pessoaEditada;
	
	private DateTimeFormat dfData = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm");
	private List<Encontro> listaEncontro;
	
	public MensagemView() {
		criaTabela();
		criaTabelaDestinatarios();
		
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
				agrupamentoListBox.setSelectedIndex(0);
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
				agrupamentoListBox.setSelectedIndex(0);
				if(!pessoaSuggestBox.getValue().equals("")){
					pessoaEditada = (Pessoa)ListUtil.getEntidadePorNome(pessoaSuggest.getListaEntidades(), pessoaSuggestBox.getValue());
					casalSuggestBox.setValue(null);
				}
			}
		});
		
		mensagemRichTextArea = new RichTextArea();
		mensagemRichTextToolbar = new RichTextToolbar(mensagemRichTextArea);
		
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.populate(variaveisListBox, true, TipoVariavelEnum.values());
		ListBoxUtil.populate(tipoListBox, false, TipoMensagemEnum.values());
		
		ListBoxUtil.populate(tipoFiltroListBox, true, TipoMensagemEnum.values());
	}
	
	private void criaTabela() {
		mensagemFlexTable = new FlexTable();
		mensagemFlexTable.setStyleName("portal-formSmall");
		mensagemTableUtil.initialize(mensagemFlexTable);
		
		mensagemTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		mensagemTableUtil.addColumn("Data", "120", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		mensagemTableUtil.addColumn("Tipo", "200", HasHorizontalAlignment.ALIGN_LEFT);
		mensagemTableUtil.addColumn("Encontro", "150", HasHorizontalAlignment.ALIGN_LEFT);
		mensagemTableUtil.addColumn("Descrição", "200", HasHorizontalAlignment.ALIGN_LEFT);
		mensagemTableUtil.addColumn("Titulo", "400", HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	private void criaTabelaDestinatarios() {
		destinatarioFlexTable = new FlexTable();
		destinatarioFlexTable.setStyleName("portal-formSmall");
		destinatarioTableUtil.initialize(destinatarioFlexTable);
		
		destinatarioTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		destinatarioTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
		destinatarioTableUtil.addColumn("Email", null, HasHorizontalAlignment.ALIGN_LEFT);
		destinatarioTableUtil.addColumn("Tipo", null, HasHorizontalAlignment.ALIGN_LEFT);
		destinatarioTableUtil.addColumn("Envio", "120", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		destinatarioTableUtil.addColumn("Confirmação", "120", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	
	private void salvarDados(){
		presenter.getMensagemVO().getMensagem().setGrupo(presenter.getGrupoSelecionado());
		presenter.getMensagemVO().getMensagem().setTitulo(tituloTextBox.getValue());
		presenter.getMensagemVO().getMensagem().setDescricao(descricaoTextBox.getValue());
		presenter.getMensagemVO().getMensagem().setMensagem(mensagemRichTextArea.getHTML().toCharArray());
		presenter.getMensagemVO().getMensagem().setTipoMensagem((TipoMensagemEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoMensagemEnum.values()));
		if(!presenter.getMensagemVO().getMensagem().getTipoMensagem().equals(TipoMensagemEnum.NORMAL)){
			presenter.getMensagemVO().getMensagem().setEncontro(presenter.getEncontroSelecionado());
		}
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		salvarDados();
		presenter.salvar();
	}
	@UiHandler("enviarButton")
	public void enviarButtonClickHandler(ClickEvent event){
		salvarDados();
		presenter.enviar(null, false);
	}
	private void edita(Mensagem mensagem) {
		limpaCampos();
		if(mensagem == null){
			presenter.setMensagemVO(new MensagemVO());
			presenter.getMensagemVO().setMensagem(new Mensagem());
			presenter.getMensagemVO().setListaDestinatarios(new ArrayList<MensagemDestinatario>());
		} else {
			presenter.buscaVO(mensagem);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		tituloTextBox.setFocus(true);
	}
	
	@Override
	public void setVO(MensagemVO mensagemVO){
		defineCampos(mensagemVO);
		populaDestinatarios();
	}
	
	private void populaDestinatarios() {
		final boolean completo = completoCheckBox.getValue();
		Collections.sort(presenter.getMensagemVO().getListaDestinatarios(), new Comparator<MensagemDestinatario>() {
			@Override
			public int compare(MensagemDestinatario o1, MensagemDestinatario o2) {
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
		destinatarioTableUtil.clearData();
		int row = 0, confirmacoes=0, envios=0;
		Image excluir, enviar;
		HorizontalPanel hp;
		for (final MensagemDestinatario destinatario: presenter.getMensagemVO().getListaDestinatarios()) {
			Object dados[] = new Object[6];
			
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.setTitle("Excluir este membro");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este membro ?")){
						presenter.getMensagemVO().getListaDestinatarios().remove(destinatario);
						populaDestinatarios();
					}
				}
			});
			
			enviar = new Image("images/email.png");
			enviar.setTitle("Enviar mensagem para este membro");
			enviar.setStyleName("portal-ImageCursor");
			enviar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja enviar esta mensagem para este membro apenas ?")){
						salvarDados();
						presenter.enviar(destinatario, true);
					}
				}
			});
			
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setSpacing(1);
			hp.add(enviar);
			hp.add(excluir);
			
			dados[0] = hp;
			if(completo){
				dados[1] = destinatario.getCasal()==null?destinatario.getPessoa().toString():destinatario.getCasal().toString();
			} else {
				dados[1] = destinatario.getCasal()==null?destinatario.getPessoa().getApelido():destinatario.getCasal().getApelidos("e");
			}
			dados[2] = destinatario.getCasal()==null?destinatario.getPessoa().getEmail():new HTML(destinatario.getCasal().getEmails("<br>"));
			dados[3] = destinatario.getCasal()!=null?destinatario.getCasal().getTipoCasal().getNome():null;
			if(destinatario.getDataEnvioStr()!=null){
				dados[4] = destinatario.getDataEnvioStr();
				envios++;
			}
			if(destinatario.getDataConfirmacaoStr()!=null){
				dados[5] = destinatario.getDataConfirmacaoStr();
				confirmacoes++;
			}
			destinatarioTableUtil.addRow(dados,row+1);
			row++;
		}
		destinatarioTableUtil.applyDataRowStyles();
		
		LabelTotalUtil.setTotal(itemDestinatarioTotal, presenter.getMensagemVO().getListaDestinatarios().size(), "destinatário", "destinatário", "");
		itemDestinatarioTotal.setText(itemDestinatarioTotal.getText() + " / " + envios + " enviadas " + " / " + confirmacoes + " confirmadas");
		
	}

	public void limpaCampos(){
		tituloTextBox.setValue(null);
		descricaoTextBox.setValue(null);		
		mensagemRichTextArea.setHTML("");
		encontroRadioButton.setValue(false);
		grupoRadioButton.setValue(true);
		grupoRadioButtonClickHandler(null);
		destinatarioTableUtil.clearData();
		itemDestinatarioTotal.setText(null);
		tipoListBox.setSelectedIndex(0);
	}

	public void defineCampos(MensagemVO mensagemVO){
		tituloTextBox.setValue(mensagemVO.getMensagem().getTitulo());
		descricaoTextBox.setValue(mensagemVO.getMensagem().getDescricao());
		if(mensagemVO.getMensagem().getTipoMensagem()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, mensagemVO.getMensagem().getTipoMensagem().toString());
		}
		if(mensagemVO.getMensagem().getMensagem()!=null){
			mensagemRichTextArea.setHTML(String.valueOf(mensagemVO.getMensagem().getMensagem()));
		}
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Mensagens";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		mensagemTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Mensagem> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "mensagem", "mensagens", "a");
		mensagemTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Mensagem mensagem: lista) {
			Object dados[] = new Object[6];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(mensagem);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir esta mensagem ?")){
						presenter.excluir(mensagem);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			
			dados[1] = dfData.format(mensagem.getData());
			if(mensagem.getTipoMensagem()!=null){
				dados[2] = mensagem.getTipoMensagem().getNome();
			}
			if(mensagem.getEncontro()!=null){
				dados[3] = mensagem.getEncontro().toString();
			}
			dados[4] = mensagem.getDescricao();
			dados[5] = mensagem.getTitulo();
			mensagemTableUtil.addRow(dados,row+1);
			row++;
		}
		mensagemTableUtil.applyDataRowStyles();
	}
	
	public Encontro getEncontroSelecionadoAgrupamento() {
		return (Encontro) ListBoxUtil.getItemSelected(encontroListBox, listaEncontro);
	}

	@Override
	public void populaAgrupamento(List<Agrupamento> result) {
		this.listaAgrupamento = result;
		if(grupoRadioButton.getValue()){
			agrupamentoListBox.clear();
			agrupamentoListBox.addItem("");
			agrupamentoListBox.addItem("Todos os encontristas");
			for(Agrupamento agrupamento : result) {
				agrupamentoListBox.addItem(agrupamento.toString());
			}
//			ListBoxUtil.populate(agrupamentoListBox, true, result);
		} else {
			agrupamentoListBox.clear();
			for(TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
				agrupamentoListBox.addItem(tipo.toString());
			}
			for(Agrupamento a : result) {
				agrupamentoListBox.addItem(a.toString());
			}
		}
	}
	
	@UiHandler("agrupamentoListBox")
	public void agrupamentoListBoxChangeHandler(ChangeEvent event) {
		presenter.setAgrupamentoVO(null);
		
		// Se TODOS OS ENCONTRISTAS foi selecionado
		if(grupoRadioButton.getValue()){
			if(agrupamentoListBox.getSelectedIndex()==1){
				presenter.getEncontristas();
			} else {
				Agrupamento agrupamento = (Agrupamento) ListBoxUtil.getItemSelected(agrupamentoListBox, listaAgrupamento);
				if(agrupamento!=null){
					presenter.getAgrupamentoVO(agrupamento);
				}
			}
		}
	}
	
	@UiHandler("adicionarDestinatarioButton")
	public void adicionarDestinatarioButtonClickHandler(ClickEvent event){
		MensagemDestinatario destinatario = null;
		if(pessoaEditada!=null){
			destinatario = new MensagemDestinatario();
			destinatario.setPessoa(pessoaEditada);
			if(!buscaDestinatario(destinatario)){
				presenter.getMensagemVO().getListaDestinatarios().add(destinatario);
			}
			pessoaSuggestBox.setValue(null);
		} else if (casalEditado!=null){
			destinatario = new MensagemDestinatario();
			destinatario.setCasal(casalEditado);
			if(!buscaDestinatario(destinatario)){
				presenter.getMensagemVO().getListaDestinatarios().add(destinatario);
			}
			casalSuggestBox.setValue(null);
		} else if (grupoRadioButton.getValue() && agrupamentoListBox.getSelectedIndex()>0){
			// Se TODOS OS ENCONTRISTAS foi selecionado
			if(agrupamentoListBox.getSelectedIndex()==1){
				for (Casal casal : presenter.getListaEncontristas()) {
					if(casal.getSituacao().equals(TipoSituacaoEnum.ATIVO) && casal.getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)) {
						destinatario = new MensagemDestinatario();
						destinatario.setCasal(casal);
						if(!buscaDestinatario(destinatario)){
							presenter.getMensagemVO().getListaDestinatarios().add(destinatario);
						}
					}
				}
			} else if(presenter.getAgrupamentoVO()!=null && presenter.getAgrupamentoVO().getListaMembros().size()>0){
				for (AgrupamentoMembro am : presenter.getAgrupamentoVO().getListaMembros()) {
					destinatario = new MensagemDestinatario();
					if(am.getCasal()!=null){
						destinatario.setCasal(am.getCasal());
					} else {
						destinatario.setPessoa(am.getPessoa());
					}
					if(!buscaDestinatario(destinatario)){
						presenter.getMensagemVO().getListaDestinatarios().add(destinatario);
					}
				}
			} 
		} else {
			TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(agrupamentoListBox, TipoInscricaoEnum.values());
			if(tipo!=null){
				for (EncontroInscricao inscricao : presenter.getListaInscricao()) {
					if(inscricao.getTipo().equals(tipo)){
						destinatario = new MensagemDestinatario();
						if(inscricao.getCasal()!=null){
							destinatario.setCasal(inscricao.getCasal());
						} else {
							destinatario.setPessoa(inscricao.getPessoa());
						}
						if(!buscaDestinatario(destinatario)){
							presenter.getMensagemVO().getListaDestinatarios().add(destinatario);
						}
					}
				}
			}
		}
		if(destinatario!=null){
			populaDestinatarios();
		}
	}
	
	@UiHandler("excluirDestinatarioButton")
	public void excluirDestinatarioButtonClickHandler(ClickEvent event){
		presenter.getMensagemVO().setListaDestinatarios(new ArrayList<MensagemDestinatario>());
		populaDestinatarios();
	}
	
	private boolean buscaDestinatario(MensagemDestinatario destinatario){
		for (MensagemDestinatario d : presenter.getMensagemVO().getListaDestinatarios()) {
			if(d.getCasal()!=null && destinatario.getCasal()!=null && d.getCasal().getId().equals(destinatario.getCasal().getId())){
				return true;
			}
			if(d.getPessoa()!=null && destinatario.getPessoa()!=null && d.getPessoa().getId().equals(destinatario.getPessoa().getId())){
				return true;
			}
		}
		return false;
	}
	
	@UiHandler("completoCheckBox")
	public void completoCheckBoxClickHandler(ClickEvent event){
		populaDestinatarios();
	}
	
	@UiHandler("grupoRadioButton")
	public void grupoRadioButtonClickHandler(ClickEvent event){
		agrupamentoListBox.clear();
		if(grupoRadioButton.getValue()){
			encontroHTMLPanel.setVisible(false);
			presenter.buscaAgrupamentos();
		} else {
			encontroHTMLPanel.setVisible(true);
			encontroListBoxChangeHandler(null);
		}
	}
	
	@UiHandler("encontroRadioButton")
	public void encontroRadioButtonClickHandler(ClickEvent event){
		grupoRadioButtonClickHandler(null);
	}
	
	@UiHandler("encontroListBox")
	public void encontroListBoxChangeHandler(ChangeEvent event) {
		if(getEncontroSelecionadoAgrupamento()!=null){
			showWaitMessage(true);
			presenter.buscaAgrupamentos(getEncontroSelecionadoAgrupamento());
		} else {
			agrupamentoListBox.clear();
		}
	}

	@Override
	public void populaEncontro(List<Encontro> lista) {
		this.listaEncontro = lista;
		ListBoxUtil.populate(encontroListBox, true, lista);
	}
	
	@UiHandler("variaveisListBox")
	public void variaveisListBoxChangeHandler(ChangeEvent event) {
		TipoVariavelEnum tipo = (TipoVariavelEnum) ListBoxUtil.getItemSelected(variaveisListBox, TipoVariavelEnum.values());
		if(variaveisListBox.getSelectedIndex()>0){
			String sHTML = mensagemRichTextArea.getHTML();
			sHTML += tipo.getTag();
			mensagemRichTextArea.setHTML(sHTML);
			variaveisListBox.setSelectedIndex(0);
		}
	}

	@Override
	public void init() {
		TipoMensagemEnum tipo = (TipoMensagemEnum) ListBoxUtil.getItemSelected(tipoFiltroListBox, TipoMensagemEnum.values());
		presenter.buscaMensagens(tipo);
	}
	
	@UiHandler("tipoFiltroListBox")
	public void tipoFiltroListChangeHandler(ChangeEvent event){
		init();
	}
}