package br.com.ecc.client.ui.sistema.tesouraria;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FichasView extends BaseView<FichasPresenter> implements FichasPresenter.Display {

	@UiTemplate("FichasView.ui.xml")
	interface FichasViewUiBinder extends UiBinder<Widget, FichasView> {}
	private FichasViewUiBinder uiBinder = GWT.create(FichasViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField Label totalLabel;

	@UiField Label nomeLabel;
	@UiField TextBox fichaTextBox;
	@UiField ListBox statusListBox;
	@UiField ListBox tipoListBox;
	@UiField TextBox observacaoTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	@UiField Button gerarButton;

	@UiField CheckBox exibeLiberadosCheckBox;
	@UiField CheckBox exibeReservadasCheckBox;

	@UiField(provided=true) FlexTable fichaFlexTable;
	private FlexTableUtil fichaTableUtil = new FlexTableUtil();

	private EncontroInscricaoFichaPagamento entidadeEditada;

	public FichasView() {
		criaTabela();

		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());

		ListBoxUtil.populate(tipoListBox, false, TipoInscricaoCasalEnum.values());
		ListBoxUtil.populate(statusListBox, false, TipoInscricaoFichaStatusEnum.values());
	}

	private void criaTabela() {
		fichaFlexTable = new FlexTable();
		fichaFlexTable.setStyleName("portal-formSmall");
		fichaTableUtil.initialize(fichaFlexTable);

		fichaTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		fichaTableUtil.addColumn("Ficha", "50", HasHorizontalAlignment.ALIGN_CENTER);
		fichaTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		fichaTableUtil.addColumn("Status", "50", HasHorizontalAlignment.ALIGN_CENTER);
		fichaTableUtil.addColumn("Observação", "200", HasHorizontalAlignment.ALIGN_LEFT);
		fichaTableUtil.addColumn("Tipo", "100", HasHorizontalAlignment.ALIGN_CENTER);
	}

	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null);
	}
	@UiHandler("gerarButton")
	public void gerarButtonClickHandler(ClickEvent event){
		presenter.geraFichasVagas();
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		TipoInscricaoFichaStatusEnum status = (TipoInscricaoFichaStatusEnum) ListBoxUtil.getItemSelected(statusListBox, TipoInscricaoFichaStatusEnum.values());
		TipoInscricaoCasalEnum tipo = (TipoInscricaoCasalEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoCasalEnum.values());
		if (tipo == null){
			Window.alert("Escolha o Tipo!");
			return;
		}
		if (status == null){
			Window.alert("Escolha o Status!");
			return;
		}
		if (fichaTextBox.getText()!= null && !fichaTextBox.getText().equals("")){
			EncontroInscricaoFichaPagamento ficha = getEncontroInscricaoFichaPagamento(presenter.getListaFichas(),Integer.parseInt(fichaTextBox.getText()));
			if (  ficha != null && !ficha.getId().equals(entidadeEditada.getId()) && !status.equals(TipoInscricaoFichaStatusEnum.LIBERADO) ){
				Window.alert("Ficha já cadastrada!");
				return;
			}else
				entidadeEditada.setFicha(Integer.parseInt(fichaTextBox.getText()));
		}else{
			Window.alert("Defina o número da Ficha!");
			return;
		}
		entidadeEditada.setTipo(tipo);
		entidadeEditada.setStatus(status);
		entidadeEditada.setObservacao(observacaoTextBox.getText());
		presenter.salvar(entidadeEditada);
	}
	private EncontroInscricaoFichaPagamento getEncontroInscricaoFichaPagamento(
			List<EncontroInscricaoFichaPagamento> lista, int i) {
		for (EncontroInscricaoFichaPagamento encontroInscricaoFichaPagamento : lista) {
			if (encontroInscricaoFichaPagamento.getFicha().equals(i) && !encontroInscricaoFichaPagamento.getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO))
				return encontroInscricaoFichaPagamento;
		}
		return null;
	}

	@UiHandler("exibeLiberadosCheckBox")
	public void exibeLiberadosCheckBoxClickHandler(ClickEvent event){
		populaEntidades(presenter.getListaFichas());
	}
	@UiHandler("exibeReservadasCheckBox")
	public void exibeReservadasCheckBoxClickHandler(ClickEvent event){
		populaEntidades(presenter.getListaFichas());
	}

	private void edita(EncontroInscricaoFichaPagamento ficha) {
		limpaCampos();
		if(ficha == null){
			entidadeEditada = new EncontroInscricaoFichaPagamento();
			entidadeEditada.setEncontro(presenter.getEncontroSelecionado());
		} else {
			entidadeEditada = ficha;
			defineCampos(ficha, true);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		tipoListBox.setFocus(true);
	}

	public void limpaCampos(){
		nomeLabel.setText(null);
		observacaoTextBox.setText(null);
		fichaTextBox.setText(null);
	}

	public void defineCampos(EncontroInscricaoFichaPagamento ficha, boolean combos){
		fichaTextBox.setText(ficha.getFicha().toString());
		observacaoTextBox.setText(ficha.getObservacao());
		if (ficha.getEncontroInscricao()!=null){
			if(ficha.getEncontroInscricao().getCasal()!=null){
				nomeLabel.setText(ficha.getEncontroInscricao().getCasal().getApelidos("e"));
			} else {
				nomeLabel.setText(ficha.getEncontroInscricao().getPessoa().getNome());
			}
		}
		entidadeEditada = ficha;
		if(combos){
			ListBoxUtil.setItemSelected(tipoListBox, ficha.getTipo().getNome());
			ListBoxUtil.setItemSelected(statusListBox, ficha.getStatus().getNome());
		}
	}

	@Override
	public String getDisplayTitle() {
		return "Controle de Fichas";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		fichaTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<EncontroInscricaoFichaPagamento> lista) {
		fichaTableUtil.clearData();
		int row = 0;
		Image editar;
		HorizontalPanel hp;
		int liberados = 0;
		int reservados = 0;
		int vagas = 0;
		for (final EncontroInscricaoFichaPagamento ficha: lista) {
			boolean exibeFicha = true;
			if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO)){
				liberados++;
				if (!exibeLiberadosCheckBox.getValue()){
					exibeFicha = false;
				}
			}
			if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.RESERVADO)){
				reservados++;
				if (!exibeReservadasCheckBox.getValue()){
					exibeFicha = false;
				}
			}
			if(exibeFicha){
				row++;
				Object dados[] = new Object[6];

				editar = new Image("images/edit.png");
				editar.setStyleName("portal-ImageCursor");
				editar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						edita(ficha);
					}
				});
				hp = new HorizontalPanel();
				hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				hp.setSpacing(1);
				hp.add(editar);

				dados[0] = hp;
				dados[1] = ficha.getFicha();
				if (ficha.getEncontroInscricao()!=null){
					if(ficha.getEncontroInscricao().getCasal()!=null){
						dados[2] = ficha.getEncontroInscricao().getCasal().getApelidos("e");
					} else {
						dados[2] = ficha.getEncontroInscricao().getPessoa().getNome();
					}
					if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.NORMAL))
						fichaTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialNormalGray");
					else if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO))
						fichaTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialBoldGreen");
					else if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.RESERVADO))
						fichaTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialBoldRed");
				}else{
					if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.RESERVADO)){
						dados[2] = "RESERVADO";
						fichaTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialBoldRed");
					}else if (ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.NORMAL)){
						dados[2] = "VAGO";
						fichaTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialNormalBlue");
						vagas++;
					}
				}
				dados[3] = ficha.getStatus().getNome();
				dados[4] = ficha.getObservacao();
				dados[5] = ficha.getTipo().getNome();

				fichaTableUtil.addRow(dados,row);
			}
		}
		LabelTotalUtil.setTotal(itemTotal, row, "ficha", "fichas", "");
		String totais = "";
		if(row==1 || row==0){
			totais += row + " ficha";
		} else {
			totais += row + " fichas";
		}
		if(vagas==1 || vagas==0){
			totais += " / " + vagas + " vaga";
		} else {
			totais += " / " + vagas + " vagas";
		}
		if(reservados==1 || reservados==0){
			totais += " / " + reservados + " reservada";
		} else {
			totais += " / " + reservados + " reservadas";
		}
		if(liberados==1 || liberados==0){
			totais += " / " + liberados + " liberada";
		} else {
			totais += " / " + liberados + " liberadas";
		}
		totalLabel.setText(totais);
		fichaTableUtil.applyDataRowStyles();
		showWaitMessage(false);
	}

}