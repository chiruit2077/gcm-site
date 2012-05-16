package br.com.ecc.client.ui.sistema.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.UsuarioAcesso;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.vo.UsuarioVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UsuarioView extends BaseView<UsuarioPresenter> implements UsuarioPresenter.Display {

	@UiTemplate("UsuarioView.ui.xml")
	interface UsuarioViewUiBinder extends UiBinder<Widget, UsuarioView> {}
	private UsuarioViewUiBinder uiBinder = GWT.create(UsuarioViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField Label itemAcessoTotal;
	
	@UiField TextBox nomeFiltoTextBox;
	@UiField Label nomeLabel;
	@UiField ListBox tipoListBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	
	
	@UiField(provided=true) FlexTable usuarioFlexTable;
	private FlexTableUtil usuarioTableUtil = new FlexTableUtil();

	private UsuarioVO entidadeEditada;
	
	@UiField(provided=true) FlexTable acessoFlexTable;
	private FlexTableUtil acessoTableUtil = new FlexTableUtil();
	
	public UsuarioView() {
		criaTabela();
		criaTabelaAcessos();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.getItemSelected(tipoListBox, TipoNivelUsuarioEnum.values());
	}
	
	private void criaTabela() {
		usuarioFlexTable = new FlexTable();
		usuarioFlexTable.setStyleName("portal-formSmall");
		usuarioTableUtil.initialize(usuarioFlexTable);
		
		usuarioTableUtil.addColumn("", "0", HasHorizontalAlignment.ALIGN_CENTER);
		usuarioTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		usuarioTableUtil.addColumn("Tipo", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	private void criaTabelaAcessos() {
		acessoFlexTable = new FlexTable();
		acessoFlexTable.setStyleName("portal-formSmall");
		acessoTableUtil.initialize(acessoFlexTable);
		
		acessoTableUtil.addColumn("", "0", HasHorizontalAlignment.ALIGN_CENTER);
		acessoTableUtil.addColumn("Janela", "300", HasHorizontalAlignment.ALIGN_LEFT);
		acessoTableUtil.addColumn("Acesso", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		entidadeEditada.getUsuario().setNivel((TipoNivelUsuarioEnum)ListBoxUtil.getItemSelected(tipoListBox, TipoNivelUsuarioEnum.values()));
		presenter.salvar(entidadeEditada);
	}
	private void edita(Usuario usuario) {
		limpaCampos();
		if(usuario == null){
			entidadeEditada = new UsuarioVO();
			entidadeEditada.setUsuario(new Usuario());
			entidadeEditada.setListaAcessos(new ArrayList<UsuarioAcesso>());
		} else {
			presenter.getVO(usuario);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		tipoListBox.setFocus(true);
	}
	
	public void limpaCampos(){
		ListBoxUtil.setItemSelected(tipoListBox, TipoNivelUsuarioEnum.NORMAL.getNome());
		nomeLabel.setText(null);		
	}

	public void defineCampos(UsuarioVO usuarioVO){
		if(usuarioVO.getUsuario().getNivel()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, usuarioVO.getUsuario().getNivel().getNome());
		}
		nomeLabel.setText(usuarioVO.getUsuario().getPessoa().getNome());
		populaAcessos();
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de usuários";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		usuarioTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Usuario> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "usuario", "usuarios", "");
		usuarioTableUtil.clearData();
		int row = 0;
		Image editar;
		HorizontalPanel hp;
		for (final Usuario usuario: lista) {
			Object dados[] = new Object[3];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(usuario);
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			
			dados[0] = hp;
			dados[1] = usuario.getPessoa().getNome();
			if(usuario.getNivel()!=null){
				dados[2] = usuario.getNivel().getNome();
			}
			usuarioTableUtil.addRow(dados,row+1);
			row++;
		}
		usuarioTableUtil.applyDataRowStyles();
	}

	@Override
	public void setVO(UsuarioVO vo) {
		entidadeEditada = vo;
		defineCampos(vo);
	}
	
	public void populaAcessos() {
		LabelTotalUtil.setTotal(itemAcessoTotal, entidadeEditada.getListaAcessos().size(), "acesso", "acessos", "");
		acessoTableUtil.clearData();
		int row = 0;
		Image editar;
		HorizontalPanel hp;
		UsuarioAcesso acesso;
		for (PresenterCodeEnum presenterCode : PresenterCodeEnum.values()) {
			acesso = null;
			for (UsuarioAcesso a: entidadeEditada.getListaAcessos()) {
				if(a.getPresenterCode().equals(presenterCode)){
					acesso = a;
				}
			}
			Object dados[] = new Object[3];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
//					editaAcesso(acesso);
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			
			dados[0] = hp;
			dados[1] = presenterCode.getNome();
			if(acesso!=null){
				dados[2] = acesso.getTipoAcesso().getNome();
			} else {
				dados[2] = "Sem acesso";
			}
			acessoTableUtil.addRow(dados,row+1);
			row++;
		}
		acessoTableUtil.applyDataRowStyles();
	}
}