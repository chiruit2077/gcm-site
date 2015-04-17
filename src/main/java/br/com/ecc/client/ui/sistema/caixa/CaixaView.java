package br.com.ecc.client.ui.sistema.caixa;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.Caixa;
import br.com.ecc.model.CaixaItem;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.tipo.TipoItemPatrimonioEnum;
import br.com.ecc.model.vo.CaixaVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.Widget;

public class CaixaView extends BaseView<CaixaPresenter> implements CaixaPresenter.Display {

	@UiTemplate("CaixaView.ui.xml")
	interface CaixaViewUiBinder extends UiBinder<Widget, CaixaView> {
	}

	private CaixaViewUiBinder uiBinder = GWT.create(CaixaViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField TextBox nomeTextBox;
	@UiField TextBox etiquetaTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField Tree patrimonioTree;

	@UiField FlowPanel formularioFlowPanel;

	@UiField(provided = true) FlexTable caixaFlexTable;
	private FlexTableUtil caixaTableUtil = new FlexTableUtil();

	@UiField Label itemTotal;
	@UiField Label itemPatrimonioTotal;
	@UiField(provided = true) FlexTable itemFlexTable;
	private FlexTableUtil itemTableUtil = new FlexTableUtil();

	private List<ItemPatrimonio> listaItemPatrimonio;

	public CaixaView() {
		criaTabela();
		criaTabelaMembro();

		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());

		formularioFlowPanel.setHeight((this.getWindowHeight() - 300) + "px");
	}

	private void criaTabela() {
		caixaFlexTable = new FlexTable();
		caixaFlexTable.setStyleName("portal-formSmall");
		caixaTableUtil.initialize(caixaFlexTable);

		caixaTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		caixaTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		caixaTableUtil.addColumn("Etiqueta", "100",	HasHorizontalAlignment.ALIGN_LEFT);
	}

	private void criaTabelaMembro() {
		itemFlexTable = new FlexTable();
		itemFlexTable.setStyleName("portal-formSmall");
		itemTableUtil.initialize(itemFlexTable);

		itemTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		itemTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
		itemTableUtil.addColumn("Qtde Existente", "110", HasHorizontalAlignment.ALIGN_CENTER);
		itemTableUtil.addColumn("Qtde na Caixa", "110", HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	@Override
	public void populaEntidadesTree(List<ItemPatrimonio> lista) {
		this.listaItemPatrimonio = lista;
//		LabelTotalUtil.setTotal(itemTotal, lista.size(), "item", "itens", "");
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
				if(itemPatrimonio.getTipo().equals(TipoItemPatrimonioEnum.ITEM)){
					if(verificaItem(itemPatrimonio)){
						CaixaItem caixaItem = new CaixaItem();
						caixaItem.setCaixa(presenter.getCaixaVO().getCaixa());
						caixaItem.setItemPatrimonio(itemPatrimonio);
						presenter.getCaixaVO().getListaCaixaItem().add(caixaItem);
					}
				} else {
					adicionaFilhos(itemPatrimonio);
				}
				populaItens();
			}

			private void adicionaFilhos(ItemPatrimonio itemPatrimonio) {
				CaixaItem caixaItem;
				for (ItemPatrimonio it : listaItemPatrimonio) {
					if(it.getPai()!=null){
						if(it.getPai().getId().equals(itemPatrimonio.getId())){
							if(it.getTipo().equals(TipoItemPatrimonioEnum.ITEM)){
								if(verificaItem(it)){
									caixaItem = new CaixaItem();
									caixaItem.setCaixa(presenter.getCaixaVO().getCaixa());
									caixaItem.setItemPatrimonio(it);
									presenter.getCaixaVO().getListaCaixaItem().add(caixaItem);
								}
							} else{ 
								adicionaFilhos(it);
							}
						}
					}
				}
			}
		});
		
		hp.add(adicionar);
		
		hp.add(new HTML("&nbsp;&nbsp;" + itemPatrimonio.getNome()));
		
		itemTree = new TreeItem(hp);
		
		return itemTree;
	}
	
	public boolean verificaItem(ItemPatrimonio itemPatrimonio){
		for (CaixaItem item : presenter.getCaixaVO().getListaCaixaItem()) {
			if(item.getItemPatrimonio().getId().equals(itemPatrimonio.getId())){
				return false;
			}
		}
		return true;
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event) {
		editaDialogBox.hide();
	}

	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event) {
		if (presenter.getEncontroSelecionado() == null) {
			Window.alert("Encontro não selecionado");
			return;
		}
		edita(null);
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event) {
		presenter.fechar();
	}

	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event) {
		presenter.getCaixaVO().getCaixa().setGrupo(presenter.getGrupoSelecionado());
		presenter.getCaixaVO().getCaixa().setNome(nomeTextBox.getValue());
		presenter.getCaixaVO().getCaixa().setEtiqueta(etiquetaTextBox.getValue());
		presenter.salvar();
	}

	private void edita(Caixa caixa) {
		limpaCampos();
		if (caixa == null) {
			presenter.setCaixaVO(new CaixaVO());
			presenter.getCaixaVO().setCaixa(new Caixa());
			presenter.getCaixaVO().setListaCaixaItem(new ArrayList<CaixaItem>());

			
			
			LabelTotalUtil.setTotal(itemPatrimonioTotal, 0, "item", "itens", "");

			itemTableUtil.setColumnVisible(2, true);

		} else {
			presenter.getVO(caixa);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}

	public void limpaCampos() {
		nomeTextBox.setValue(null);
		etiquetaTextBox.setValue(null);
		itemTableUtil.clearData();
	}

	public void defineCampos(CaixaVO caixaVO) {
		nomeTextBox.setValue(caixaVO.getCaixa().getNome());
		etiquetaTextBox.setValue(caixaVO.getCaixa().getEtiqueta());

		populaItens();
	}

	@Override
	public String getDisplayTitle() {
		return "Cadastro de Caixas";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		caixaTableUtil.clearData();
	}

	@Override
	public void populaEntidades(List<Caixa> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "caixa", "caixas", "");
		caixaTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (Caixa vo : lista) {
			final Caixa caixa = vo;

			Object dados[] = new Object[3];

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(caixa);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if (Window.confirm("Deseja excluir esta caixa ?")) {
						presenter.excluir(caixa);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);

			dados[0] = hp;
			
			if (caixa.getNome() != null){
				dados[1] = caixa.getNome();
			}else{
				dados[1] = "";
			}
			
			if (caixa.getEtiqueta() != null){
				dados[2] = caixa.getEtiqueta();
			}else{
				dados[2] = "";
			}
			
			caixaTableUtil.addRow(dados, row + 1);
			row++;
		}
		caixaTableUtil.applyDataRowStyles();
	}

	public void populaItens() {

		itemTableUtil.clearData();
		int row = 0;
		Image excluir;
		HorizontalPanel hp;
		for (final CaixaItem item : presenter.getCaixaVO().getListaCaixaItem()) {
			Object dados[] = new Object[4];

			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if (Window.confirm("Deseja excluir este item ?")) {
						presenter.getCaixaVO().getListaCaixaItem().remove(item);
						populaItens();
					}
				}
			});

			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(excluir);

			dados[0] = hp;

			dados[1] = item.getItemPatrimonio().getNome();

			if(item.getItemPatrimonio().getQtdeExistente()!=null){
				dados[2] = item.getItemPatrimonio().getQtdeExistente().toString();
			}else
				dados[2] = "";
			
			final NumberTextBox textBox = new NumberTextBox();
			textBox.setSize("100px", "100%");
			textBox.setAlignment(TextAlignment.CENTER);
			textBox.setMaxLength(3);
			if(item.getQuantidade() !=null){
				textBox.setText(item.getQuantidade().toString());
			}
			dados[3] = textBox;
			
			textBox.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent arg0) {
					if(textBox.getNumber()!=null){
						item.setQuantidade(textBox.getNumber().intValue());
					}
				}
			});

			itemTableUtil.addRow(dados, row + 1);
			row++;
		}
		LabelTotalUtil.setTotal(itemPatrimonioTotal, row, "item", "itens", "");
		itemTableUtil.applyDataRowStyles();
	}

	@Override
	public void setVO(CaixaVO vo) {
		defineCampos(vo);
	}

	@UiHandler("excluirItemButton")
	public void excluirItemButtonClickHandler(ClickEvent event) {
		if (Window.confirm("Deseja excluir todos os itens desta caixa?")) {
			presenter.getCaixaVO().setListaCaixaItem(new ArrayList<CaixaItem>());
			populaItens();
		}
	}

}