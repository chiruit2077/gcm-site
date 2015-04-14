package br.com.ecc.client.ui.sistema.patrimonio;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.ItemPatrimonioAtividade;
import br.com.ecc.model.tipo.TipoItemPatrimonioEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;
import br.com.ecc.model.vo.ItemPatrimonioVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ItemPatrimonioView extends BaseView<ItemPatrimonioPresenter> implements ItemPatrimonioPresenter.Display {

	@UiTemplate("ItemPatrimonioView.ui.xml")
	interface ItemPatrimonioViewUiBinder extends UiBinder<Widget, ItemPatrimonioView> {}
	private ItemPatrimonioViewUiBinder uiBinder = GWT.create(ItemPatrimonioViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField TextBox nomeTextBox;
	@UiField NumberTextBox qtdeExistenteTextBox;
	@UiField NumberTextBox qtdeNecessariaTextBox;
	@UiField ListBox situacaoListBox;
	@UiField ListBox tipoListBox;
	@UiField ListBox atividadeListBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField Label itemAtividadeTotal;
	@UiField HTMLPanel itemHTMLPanel;
	
	@UiField(provided=true) FlexTable atividadeFlexTable;
	private FlexTableUtil atividadeTableUtil = new FlexTableUtil();
	
	@UiField Tree patrimonioTree;
	
	private ItemPatrimonioVO entidadeEditada;
	private List<Atividade> listaAtividade;
	
	public ItemPatrimonioView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		
		ListBoxUtil.populate(situacaoListBox, false, TipoSituacaoEnum.values());
		ListBoxUtil.populate(tipoListBox, false, TipoItemPatrimonioEnum.values());
		
	}
	
	private void criaTabela() {
		atividadeFlexTable = new FlexTable();
		atividadeFlexTable.setStyleName("portal-formSmall");
		atividadeTableUtil.initialize(atividadeFlexTable);
		
		atividadeTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		atividadeTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	
	@UiHandler("atividadeListBox")
	public void atividadeListBoxChange(ChangeEvent event){
		Atividade atividade = (Atividade) ListBoxUtil.getItemSelected(atividadeListBox, listaAtividade);
		for (ItemPatrimonioAtividade ia : entidadeEditada.getListaItemPatrimonioAtividade()) {
			if(ia.getAtividade().equals(atividade)){
				Window.alert("Atividade já adicionada");
				return;
			}
		}
		ItemPatrimonioAtividade ipa = new ItemPatrimonioAtividade();
		ipa.setAtividade(atividade);
		entidadeEditada.getListaItemPatrimonioAtividade().add(ipa);
		populaTabelaAtividades();
	}
	
	@UiHandler("tipoListBox")
	public void tipoListBoxChange(ChangeEvent event){
		exibeCampos();
	}

	private void exibeCampos() {
		if(((TipoItemPatrimonioEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoItemPatrimonioEnum.values())).getNome().equals(TipoItemPatrimonioEnum.ITEM.getNome())){
			itemHTMLPanel.setVisible(true);
		}else{
			itemHTMLPanel.setVisible(false);
		}
		editaDialogBox.center();
	}
	
	
	
	private void populaTabelaAtividades() {
		
		atividadeTableUtil.clearData();
		int row = 0;
		Image excluir;
		for (final ItemPatrimonioAtividade atividade: entidadeEditada.getListaItemPatrimonioAtividade()) {
			Object dados[] = new Object[2];
			
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.setTitle("Excluir esta atividade");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir esta atividade ?")){
						entidadeEditada.getListaItemPatrimonioAtividade().remove(atividade);
						populaTabelaAtividades();
					}
				}
			});
			
			dados[0] = excluir;
			dados[1] = atividade.getAtividade().getNome();
			
			atividadeTableUtil.addRow(dados,row+1);
			row++;
		}
		atividadeTableUtil.applyDataRowStyles();
		
		LabelTotalUtil.setTotal(itemAtividadeTotal, entidadeEditada.getListaItemPatrimonioAtividade().size(), "item", "itens", "");
//		itemDestinatarioTotal.setText(itemDestinatarioTotal.getText() + " / " + envios + " enviadas " + " / " + confirmacoes + " confirmadas");
		
	}
	
	
	@Override
	public void populaAtividades(List<Atividade> listaAtividade) {
		ListBoxUtil.populate(atividadeListBox, true, listaAtividade);
		this.listaAtividade = listaAtividade;
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null, null);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		entidadeEditada.getItemPatrimonio().setGrupo(presenter.getGrupoSelecionado());
		entidadeEditada.getItemPatrimonio().setNome(nomeTextBox.getValue());
		
		entidadeEditada.getItemPatrimonio().setSituacao((TipoSituacaoEnum) ListBoxUtil.getItemSelected(situacaoListBox, TipoSituacaoEnum.values()));
		entidadeEditada.getItemPatrimonio().setTipo((TipoItemPatrimonioEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoItemPatrimonioEnum.values()));
		entidadeEditada.getItemPatrimonio().setQtdeExistente(qtdeExistenteTextBox.getValue());
		entidadeEditada.getItemPatrimonio().setQtdeNecessaria(qtdeNecessariaTextBox.getValue());
		
		presenter.salvar(entidadeEditada);
	}
	private void edita(ItemPatrimonio pai, ItemPatrimonio itemPatrimonio) {
		limpaCampos();
		if(itemPatrimonio == null){
			entidadeEditada = new ItemPatrimonioVO();
			entidadeEditada.setItemPatrimonio(new ItemPatrimonio());
			entidadeEditada.getItemPatrimonio().setPai(pai);
			entidadeEditada.setListaItemPatrimonioAtividade(new ArrayList<ItemPatrimonioAtividade>());
		} else {
			presenter.getItemPatrimonioVO(itemPatrimonio);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}
	
	public void limpaCampos(){
		nomeTextBox.setValue(null);
		atividadeTableUtil.clearData();
		qtdeExistenteTextBox.setValue(null);
		qtdeNecessariaTextBox.setValue(null);
	}

	public void defineCampos(ItemPatrimonioVO itemPatrimonioVO){
		if(itemPatrimonioVO.getItemPatrimonio().getTipo()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, itemPatrimonioVO.getItemPatrimonio().getTipo().getNome());
		}
		nomeTextBox.setValue(itemPatrimonioVO.getItemPatrimonio().getNome());
		if(itemPatrimonioVO.getItemPatrimonio().getTipo()!=null){
			ListBoxUtil.setItemSelected(situacaoListBox, itemPatrimonioVO.getItemPatrimonio().getSituacao().getNome());
		}
		qtdeExistenteTextBox.setValue(itemPatrimonioVO.getItemPatrimonio().getQtdeExistente());
		qtdeNecessariaTextBox.setValue(itemPatrimonioVO.getItemPatrimonio().getQtdeNecessaria());
	}
	
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de itens de patrimônio";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		patrimonioTree.clear();
	}
	
	@Override
	public void populaEntidades(List<ItemPatrimonio> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "item", "itens", "");
		patrimonioTree.clear();
		for (ItemPatrimonio ip : lista) {
			if(ip.getPai()==null){
				addItemTreeChildren(null, lista, ip);
			}
		}
	}
	
	private void addItemTreeChildren(TreeItem treeItem, List<ItemPatrimonio> lista, ItemPatrimonio itemPatrimonio){
		TreeItem item;
		item = criaItem(itemPatrimonio);
		if(treeItem==null){
			patrimonioTree.addItem(item);
		} else {
			treeItem.addItem(item);
		}
		if(itemPatrimonio.getTipo().equals(TipoItemPatrimonioEnum.AGRUPAMENTO)){
			for (ItemPatrimonio ip : lista) {
				if (ip.getPai()!=null && ip.getPai().getId().equals(itemPatrimonio.getId())){
					addItemTreeChildren(item, lista, ip);
				}
			}
		}
	}
	private TreeItem criaItem(final ItemPatrimonio itemPatrimonio){
		TreeItem itemTree;
		HorizontalPanel hp = new HorizontalPanel();
		//hp.setWidth("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hp.setSpacing(0);
		Image adicionar = new Image("images/add.png");
		adicionar.setTitle("Adicionar um item neste grupo");
		adicionar.setStyleName("portal-ImageCursor");
		adicionar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				edita(itemPatrimonio, null);
			}
		});
		Image editar = new Image("images/edit.png");
		editar.setTitle("Editar este item");
		editar.setStyleName("portal-ImageCursor");
		editar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				edita(null, itemPatrimonio);
			}
		});
		Image excluir = new Image("images/delete.png");
		excluir.setTitle("Excluir este item");
		excluir.setStyleName("portal-ImageCursor");
		excluir.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if(Window.confirm("Deseja excluir este item?")){
					presenter.excluir(itemPatrimonio);
				}
			}
		});
		if(itemPatrimonio.getTipo().equals(TipoItemPatrimonioEnum.AGRUPAMENTO)){
			hp.add(adicionar);
		}
		hp.add(editar);
		hp.add(excluir);
//		hp.add(new HTML("&nbsp;&nbsp;" + itemPatrimonio.getNome()+"&nbsp;&nbsp;&nbsp;&nbsp;("+ itemPatrimonio.getSituacao()+")"));
		hp.add(new HTML("&nbsp;&nbsp;" + itemPatrimonio.getNome()));
		
		itemTree = new TreeItem(hp);
//		patrimonioTree.addItem(itemTree);
		
		return itemTree;
	}

	@Override
	public void setVO(ItemPatrimonioVO itemPatrimonioVO) {
		entidadeEditada = itemPatrimonioVO;
		defineCampos(itemPatrimonioVO);
		populaTabelaAtividades();
		exibeCampos();
		
	}
}