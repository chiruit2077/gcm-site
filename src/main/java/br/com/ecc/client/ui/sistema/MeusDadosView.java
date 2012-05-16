package br.com.ecc.client.ui.sistema;

import java.util.ArrayList;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.upload.UploadArquivoDigital;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Usuario;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoCasalEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class MeusDadosView extends BaseView<MeusDadosPresenter> implements MeusDadosPresenter.Display {
	
	@UiTemplate("MeusDadosView.ui.xml")
	interface HomeViewUiBinder extends UiBinder<Widget, MeusDadosView> {}
	private HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField TextBox emailTextBox;
	@UiField TextBox nomeTextBox;
	@UiField TextBox apelidoTextBox;
	@UiField DateBox dataDateBox;
	@UiField HTMLPanel padrinhoHTMLPanel;
	
	@UiField Image casalImage;
	@UiField UploadArquivoDigital logotipoUploadArquivoDigital;
	
	@UiField Button senhaButton;
	@UiField Button salvarButton;
	
	@UiField DialogBox editaDialogBox;
	@UiField PasswordTextBox senhaTextBox;
	@UiField PasswordTextBox novaSenhaTextBox;
	@UiField PasswordTextBox confirmacaoTextBox;
	@UiField Button okButton;
	@UiField Button cancelButton;
	
	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();
	
	private Usuario usuario;

	
	public MeusDadosView() {
		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);
		
		this.initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		
		dataDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		dataDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		casalSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!casalSuggestBox.getValue().equals("")){
					Casal casalEditado = (Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue());
					presenter.getCasal().setCasalPadrinho(casalEditado);
				}
			}
		});
		casalSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				if(casalSuggestBox.getValue().equals("")){
					presenter.getCasal().setCasalPadrinho(null);
				}
			}
		});
	}

	@UiHandler("senhaButton")
	public void senhaButtonClickHandler(ClickEvent event){
		senhaTextBox.setValue(null);
		novaSenhaTextBox.setValue(null);
		confirmacaoTextBox.setValue(null);
		editaDialogBox.center();
		editaDialogBox.show();
		senhaTextBox.setFocus(true);
	}

	@Override
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		nomeTextBox.setValue(usuario.getNome());
		emailTextBox.setValue(usuario.getEmail());
		apelidoTextBox.setValue(usuario.getApelido());
		dataDateBox.setValue(usuario.getPessoa().getNascimento());
		apelidoTextBox.setFocus(true);
		
		if(presenter.getCasal()!=null){
			casalImage.setUrl("eccweb/downloadArquivoDigital?id="+presenter.getCasal().getIdArquivoDigital());
			if(presenter.getCasal().getCasalPadrinho()!=null){
				casalSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
				casalSuggest.getListaEntidades().add(presenter.getCasal().getCasalPadrinho());
				casalSuggestBox.setText(presenter.getCasal().getCasalPadrinho().toString());
			}
			if(presenter.getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
				padrinhoHTMLPanel.setVisible(true);
			}
		}
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		
		emailTextBox.setValue(emailTextBox.getValue().trim());
		if(!emailTextBox.getValue().matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$")){
			Window.alert("Endereço eletrônico inválido");
			emailTextBox.setFocus(true);
			return;
		}
		//usuario
		usuario.getPessoa().setNome(nomeTextBox.getValue());
		usuario.getPessoa().setEmail(emailTextBox.getValue());
		usuario.getPessoa().setApelido(apelidoTextBox.getValue());
		usuario.getPessoa().setNascimento(dataDateBox.getValue());
		presenter.salvar(usuario, false);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("cancelButton")
	public void cancelButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("okButton")
	public void okButtonClickHandler(ClickEvent event){
		if(!novaSenhaTextBox.getValue().equals(confirmacaoTextBox.getValue())){
			Window.alert("A nova senha não confere");
			novaSenhaTextBox.setValue(null);
			confirmacaoTextBox.setValue(null);
			return;
		}
		usuario.setSenha(senhaTextBox.getValue());
		usuario.setSenhaCripto(novaSenhaTextBox.getValue());
		presenter.salvar(usuario, true);
		editaDialogBox.hide();
	}
	
	@Override
	public String getDisplayTitle() {
		return "Meus dados";
	}

	@Override
	public void reset() {
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public void defineFoto() {
		casalImage.setUrl("eccweb/downloadArquivoDigital?id="+logotipoUploadArquivoDigital.getIdArquivoDigital());
		presenter.getCasal().setIdArquivoDigital(logotipoUploadArquivoDigital.getIdArquivoDigital());
	}
}