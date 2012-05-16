package br.com.ecc.client.ui.sistema.patrimonio;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.ItemPatrimonio;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
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
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField Tree patrimonioTree;
	
	private ItemPatrimonio entidadeEditada;
	
	public ItemPatrimonioView() {
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
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
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		entidadeEditada.setGrupo(presenter.getGrupoSelecionado());
		entidadeEditada.setNome(nomeTextBox.getValue());
		presenter.salvar(entidadeEditada);
	}
	private void edita(ItemPatrimonio itemPatrimonio) {
		limpaCampos();
		if(itemPatrimonio == null){
			entidadeEditada = new ItemPatrimonio();
		} else {
			entidadeEditada = itemPatrimonio;
			defineCampos(itemPatrimonio);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}
	
	public void limpaCampos(){
		nomeTextBox.setValue(null);
	}

	public void defineCampos(ItemPatrimonio itemPatrimonio){
		nomeTextBox.setValue(itemPatrimonio.getNome());
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
		addItemTreeChildren(null, lista, null);
	}
	
	private void addItemTreeChildren(TreeItem treeItem, List<ItemPatrimonio> lista, ItemPatrimonio itemPatrimonio){
		TreeItem item;
		for (ItemPatrimonio ip : lista) {
			if(ip.getPai()==null && itemPatrimonio==null){
				item = criaItem(ip);
				patrimonioTree.addItem(item);
				addItemTreeChildren(item, lista, ip);
			} else {
				if (ip.getPai()!=null && itemPatrimonio!=null && ip.getPai().getId().equals(itemPatrimonio.getId())){
					treeItem.addItem(criaItem(ip));
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
				ItemPatrimonio ip = new ItemPatrimonio();
				ip.setPai(itemPatrimonio);
				edita(ip);
			}
		});
		Image editar = new Image("images/edit.png");
		editar.setTitle("Editar este item");
		editar.setStyleName("portal-ImageCursor");
		editar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				edita(itemPatrimonio);
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
		
		hp.add(adicionar);
		hp.add(editar);
		hp.add(excluir);
		hp.add(new HTML("&nbsp;&nbsp;" + itemPatrimonio.getNome()));
		
		itemTree = new TreeItem(hp);
		patrimonioTree.addItem(itemTree);
		
		return itemTree;
	}
}