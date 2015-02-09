package br.com.ecc.client.ui.sistema.tesouraria;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.NavegadorUtil;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
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
import com.google.gwt.user.client.ui.Widget;

public class DoacaoView extends BaseView<DoacaoPresenter> implements DoacaoPresenter.Display {

	@UiTemplate("DoacaoView.ui.xml")
	interface DoacaoViewUiBinder extends UiBinder<Widget, DoacaoView> {}
	private DoacaoViewUiBinder uiBinder = GWT.create(DoacaoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField Label emailLabel;

	@UiField Label participanteLabel;

	@UiField ListBox tipoInscricaoListBox;

	@UiField Label tipoLabel;
	@UiField Label codigoNumberTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Image addDetalheImage;
	
	@UiField DialogBox pagamentoDialogBox;

	@UiField(provided=true) FlexTable encontroInscricaoFlexTable;
	private FlexTableUtil encontroInscricaoTableUtil = new FlexTableUtil();

	@UiField(provided=true) FlexTable detalheFlexTable;
	private FlexTableUtil detalheTableUtil = new FlexTableUtil();

	NumberFormat dfCurrency = NumberFormat.getCurrencyFormat();

	private EncontroInscricaoVO entidadeEditada;
	@SuppressWarnings("unused")
	private EncontroInscricaoPagamentoDetalhe entidadeEditadaDetalhe;
	@SuppressWarnings("unused")
	private List<EncontroInscricaoVO> listaEncontroVO;

	public DoacaoView() {
		criaTabela();
		criaTabelaDetalhe();

		initWidget(uiBinder.createAndBindUi(this));

		tituloFormularioLabel.setText(getDisplayTitle());

		tipoInscricaoListBox.addItem(TipoInscricaoEnum.AFILHADO.toString());
		tipoInscricaoListBox.addItem("Encontrista");
		tipoInscricaoListBox.setSelectedIndex(0);
	}

	private void criaTabela() {
		encontroInscricaoFlexTable = new FlexTable();
		encontroInscricaoFlexTable.setStyleName("portal-formSmall");
		encontroInscricaoTableUtil.initialize(encontroInscricaoFlexTable);

		encontroInscricaoTableUtil.addColumn("", "10px", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Código", "50px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		encontroInscricaoTableUtil.addColumn("Tipo", "80px", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Nome", "500px", HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoTableUtil.addColumn("Doações", "120px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		encontroInscricaoTableUtil.addColumn("Efetuadas", "120px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		encontroInscricaoTableUtil.addColumn("Último pagamento", "120px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
	}
	private void criaTabelaDetalhe() {
		detalheFlexTable = new FlexTable();
		detalheFlexTable.setStyleName("portal-formSmall");
		detalheTableUtil.initialize(detalheFlexTable);

		detalheTableUtil.addColumn("", "10px", HasHorizontalAlignment.ALIGN_CENTER);
		detalheTableUtil.addColumn("Valor Unit.", "100px", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
		detalheTableUtil.addColumn("Data", "50px", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
	}
	@Override
	public void init(){
		if(presenter.getDadosLoginVO().getUsuario()==null) return;
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
		/*
		entidadeEditada.getEncontroInscricao().setEncontro(presenter.getEncontroSelecionado());
		entidadeEditada.getEncontroInscricao().setCasal(casalEditado);
		entidadeEditada.getEncontroInscricao().setPessoa(pessoaEditada);
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			entidadeEditada.getEncontroInscricao().setTipo((TipoInscricaoEnum)ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values()));
		}
		entidadeEditada.getEncontroInscricao().setCodigo(null);
		if(codigoNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontroInscricao().setCodigo(codigoNumberTextBox.getNumber().intValue());
		}
		//salvando dados do pagamento
		entidadeEditada.getEncontroInscricao().setDataMaximaParcela(dataMaxParcelaDateBox.getValue());
		entidadeEditada.getEncontroInscricao().setEsconderPlanoPagamento(esconderPagamentoCheckBox.getValue());
		entidadeEditada.getEncontroInscricao().setHospedagemParticular(hospedagemParticularCheckBox.getValue());
		entidadeEditada.getEncontroInscricao().setTipoConfirmacao((TipoConfirmacaoEnum) ListBoxUtil.getItemSelected(confirmacaoListBox, TipoConfirmacaoEnum.values()));
		entidadeEditada.setMarcaFichaPreenchida(marcaPreenchimentoFichaCheckBox.getValue());

		if(!verificaDadosPagamento()){
			if(!Window.confirm("Não foram definidas suas opções de pagamento.\nDeseja sair mesmo assim?")){
				return;
			}
		}
		entidadeEditada.setMantemValores(true);
		*/
		presenter.salvar(entidadeEditada);
	}

	/*
	@Override
	public void edita(EncontroInscricao encontroInscricao) {
		limpaCampos();
		presenter.getVO(encontroInscricao);
	}
	 */
	public void limpaCampos(){
		tipoLabel.setVisible(false);
		tipoLabel.setText(null);
		emailLabel.setText(null);
		participanteLabel.setText(null);
		detalheTableUtil.clearData();
	}
	/*
	public void defineCampos(EncontroInscricaoVO encontroInscricaoVO){
		entidadeEditada = encontroInscricaoVO;
		pessoaEditada = encontroInscricaoVO.getEncontroInscricao().getPessoa();
		casalEditado = encontroInscricaoVO.getEncontroInscricao().getCasal();
		if(casalEditado!=null){
			casalSuggestBox.setValue(casalEditado.toString());
			participanteLabel.setText(casalEditado.toString());
			emailLabel.setText(casalEditado.getEmails(","));
			pessoaHTMLPanel.setVisible(false);
		} else {
			pessoaSuggestBox.setValue(pessoaEditada.getNome());
			participanteLabel.setText(pessoaEditada.getNome());
			emailLabel.setText(pessoaEditada.getEmail());
			casalHTMLPanel.setVisible(false);
		}
		codigoNumberTextBox.setNumber(encontroInscricaoVO.getEncontroInscricao().getCodigo());
		if(encontroInscricaoVO.getEncontroInscricao().getTipo()!=null){
			tipoLabel.setText(encontroInscricaoVO.getEncontroInscricao().getTipo().getNome());
			ListBoxUtil.setItemSelected(tipoListBox, encontroInscricaoVO.getEncontroInscricao().getTipo().getNome());
			buscaMensagem(encontroInscricaoVO.getEncontroInscricao().getTipo());
		}
		esconderPagamentoCheckBox.setValue(encontroInscricaoVO.getEncontroInscricao().getEsconderPlanoPagamento());
		hospedagemParticularCheckBox.setValue(encontroInscricaoVO.getEncontroInscricao().getHospedagemParticular());
		dataMaxParcelaDateBox.setValue(encontroInscricaoVO.getEncontroInscricao().getDataMaximaParcela());
		if (!encontroInscricaoVO.getEncontroInscricao().getEsconderPlanoPagamento()){
			tabPanel.getTabWidget(1).getParent().setVisible(true);
			valorHTMLPanel.setVisible(true);
		}
		if (encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.AFILHADO)){
			hospedagemParticularCheckBox.setVisible(false);
		}

		//iniciando componente
		dadosPagamentoComponent.setUsuario(presenter.getDadosLoginVO().getUsuario());
		dadosPagamentoComponent.setCasal(presenter.getDadosLoginVO().getCasal());
		dadosPagamentoComponent.setEncontroInscricaoVO(encontroInscricaoVO);

		setDadosFicha(encontroInscricaoVO.getEncontroInscricao());

		if(entidadeEditada.getEncontroInscricao().getTipoConfirmacao()!=null){
			ListBoxUtil.setItemSelected(confirmacaoListBox, entidadeEditada.getEncontroInscricao().getTipoConfirmacao().getNome());
		}

		boolean atualiza = encontroInscricaoVO.isAtualizaValores();
		if (atualiza) {
			if (Window.confirm("Deseja atualizar os valor para esta inscrição?")){
				encontroInscricaoVO.geraPagamentoDetalhe();
				encontroInscricaoVO.defineParcelasPosiveis();
				encontroInscricaoVO.geraParcelas();
			}else
				atualiza = false;
		}
		populaDetalhes(encontroInscricaoVO.getListaPagamentoDetalhe(), atualiza);
	}
*/

	@Override
	public String getDisplayTitle() {
		return "Controle de Doações";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		encontroInscricaoTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<EncontroInscricaoVO> lista) {
		listaEncontroVO = lista;
		Collections.sort(lista, new Comparator<EncontroInscricaoVO>() {
			@Override
			public int compare(EncontroInscricaoVO o1, EncontroInscricaoVO o2) {
				String s1="", s2="";
				if(o1.getEncontroInscricao().getCodigo()!=null) s1 = o1.getEncontroInscricao().getCodigo().toString();
				if(o2.getEncontroInscricao().getCodigo()!=null) s2 = o2.getEncontroInscricao().getCodigo().toString();
				if (o1.getEncontroInscricao().getTipo().equals(o2.getEncontroInscricao().getTipo())){
					if(o1.getEncontroInscricao().getCasal()!=null){
						s1 = s1 + o1.getEncontroInscricao().getCasal().toString();
					} else {
						s1 = s1 + o1.getEncontroInscricao().getPessoa().toString();
					}
					if(o2.getEncontroInscricao().getCasal()!=null){
						s2 = s2 + o2.getEncontroInscricao().getCasal().toString();
					} else {
						s2 = s2 + o2.getEncontroInscricao().getPessoa().toString();
					}
				} else {
					s1 = o1.getEncontroInscricao().getTipo().getCodigo() + s1;
					s2 = o2.getEncontroInscricao().getTipo().getCodigo() + s2;
				}
				return s1.compareTo(s2);
			}
		});

		boolean inscricaoAfilhado = false;
		if(tipoInscricaoListBox.getSelectedIndex()==0){
			inscricaoAfilhado = true;
		}

		encontroInscricaoTableUtil.clearData();
		int row = 0;
		Image editar;
		HorizontalPanel hp;
		boolean exibeLinha;
		for (EncontroInscricaoVO encontroInscricaoVO: lista) {
			final EncontroInscricao encontroInscricao = encontroInscricaoVO.getEncontroInscricao();
			exibeLinha = true;
			if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO) && !inscricaoAfilhado){
				exibeLinha = false;
			} else if(!encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO) && inscricaoAfilhado){
				exibeLinha = false;
			}
			if(exibeLinha){
				row++;
				Object dados[] = new Object[7];
				hp = new HorizontalPanel();
				hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				hp.setSpacing(1);

				editar = new Image(NavegadorUtil.makeUrlResource("images/edit.png"));
				editar.setTitle("Editar as informações das doações");
				editar.setStyleName("portal-ImageCursor");
				editar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						//edita(encontroInscricao);
					}
				});
				hp.add(editar);

				dados[0] = hp;
				if (encontroInscricao.getFichaPagamento()!=null){
					if (encontroInscricao.getFichaPagamento().getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO))
						dados[1] = encontroInscricao.getFichaPagamento().toString() + "-L";
					else
						dados[1] = encontroInscricao.getFichaPagamento().toString();
				}else
					dados[1] = encontroInscricao.getCodigo();
				dados[2] = encontroInscricao.getTipo().getNome();
				dados[3] = encontroInscricao.getCasal()==null?encontroInscricao.getPessoa().getNome():encontroInscricao.getCasal().toString();

				/*
				if(encontroInscricao.getMensagemDestinatario()!=null){
					dados[4] = encontroInscricao.getMensagemDestinatario().getDataEnvioStr();
					dados[5] = encontroInscricao.getMensagemDestinatario().getDataConfirmacaoStr();
				}
				dados[6] = encontroInscricao.getDataPrenchimentoFichaStr();

				if(encontroInscricao.getTipoConfirmacao()!=null){
					dados[7] = encontroInscricao.getTipoConfirmacao().getNome();
				} else {
					dados[7] = TipoConfirmacaoEnum.CONFIRMADO.getNome();
				}
				*/
				encontroInscricaoTableUtil.addRow(dados,row);
			}
		}
		LabelTotalUtil.setTotal(itemTotal, row, "inscrição", "inscrições", "a");
		encontroInscricaoTableUtil.applyDataRowStyles();
		showWaitMessage(false);
	}
/*
	@Override
	public void setVO(EncontroInscricaoVO vo) {
		//defineCampos(vo);
		editaDialogBox.center();
		editaDialogBox.show();
	}
	*/

	/*
	public void populaDetalhes(List<EncontroInscricaoPagamentoDetalhe> lista, boolean atualiza) {
		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			podeEditar = true;
		}
		detalheTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroInscricaoPagamentoDetalhe detalhe: lista) {
			Object dados[] = new Object[7];
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			if(podeEditar && detalhe.getEditavel()){
				editar = new Image(NavegadorUtil.makeUrlResource("images/edit.png"));
				editar.setTitle("Editar este detalhe");
				editar.setStyleName("portal-ImageCursor");
				editar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						editaDetalhe(detalhe);
					}
				});
				hp.add(editar);
			}

			if(podeEditar && detalhe.getEditavel()){
				excluir = new Image(NavegadorUtil.makeUrlResource("images/delete.png"));
				excluir.setTitle("Excluir este detalhe");
				excluir.setStyleName("portal-ImageCursor");
				excluir.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						if(Window.confirm("Deseja excluir este detalhamento ?")){
							entidadeEditada.getListaPagamentoDetalhe().remove(detalhe);
							populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
						}
					}
				});
				hp.add(excluir);
			}

			dados[0] = hp;
			if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.AVULSO))
				dados[1] = detalhe.getDescricao();
			else
				dados[1] = detalhe.getTipoDetalhe().getNome();

			dados[2] = detalhe.getTipoLancamento().getNome();

			if( detalhe.getValorUnitario()!=null){
				dados[3] = dfCurrency.format(detalhe.getValorUnitario());
			}
			if( detalhe.getQuantidade()!=null){
				dados[4] = detalhe.getQuantidade().toString();
			}
			if( detalhe.getValor()!=null){
				dados[5] = dfCurrency.format(detalhe.getValor());
			}
			if( detalhe.getEncontroInscricaoOutra()!=null){
				dados[6] = detalhe.getEncontroInscricaoOutra().toStringApelidos();
			}
			detalheTableUtil.addRow(dados,row+1);
			row++;
		}
		detalheTableUtil.applyDataRowStyles();
	}

	public void limpaCamposDetalhe(){
		descricaoDetalheTextBox.setValue(null);
		valorUnitarioDetalheNumberTextBox.setValue(null);
		quantidadeDetalheNumberTextBox.setNumber(1);
		valorDetalheNumberTextBox.setValue(null);
		inscricaoOutraSuggestBox.setVisible(false);
		labelOutraInscricao.setVisible(false);
		casalRadio.setVisible(false);
		pessoaRadio.setVisible(false);
		casalRadio.setValue(true);
		labelOutraInscricao.setText("Casal:");
		inscricaoOutraSuggestBox.setValue(null);
		TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values());
		if ((tipo != null) && (tipo.equals(TipoInscricaoEnum.DOACAO) || tipo.equals(TipoInscricaoEnum.EXTERNO))){
			descricaoDetalheTextBox.setEnabled(false);
			descricaoDetalheTextBox.setValue(TipoPagamentoDetalheEnum.OUTRAINSCRICAO.toString());
			tipoDetalheListBox.clear();
			tipoDetalheListBox.addItem(TipoPagamentoDetalheEnum.OUTRAINSCRICAO.toString());
			tipoLancamentoListBox.clear();
			tipoLancamentoListBox.addItem(TipoPagamentoLancamentoEnum.DEBITO.toString());
		}else
			ListBoxUtil.populate(tipoDetalheListBox, false, TipoPagamentoDetalheEnum.values());
		ListBoxUtil.populate(tipoLancamentoListBox, false, TipoPagamentoLancamentoEnum.values());
	}
	public void defineCamposDetalhe(EncontroInscricaoPagamentoDetalhe detalhe){
		if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.AVULSO)){
			descricaoDetalheTextBox.setValue(detalhe.getDescricao());
			descricaoDetalheTextBox.setEnabled(true);
		}
		else{
			descricaoDetalheTextBox.setValue(detalhe.getTipoDetalhe().getNome());
			descricaoDetalheTextBox.setEnabled(false);
		}
		valorUnitarioDetalheNumberTextBox.setNumber(detalhe.getValorUnitario());
		quantidadeDetalheNumberTextBox.setNumber(detalhe.getQuantidade());
		quantidadeDetalheNumberTextBox.setEnabled(detalhe.getTipoDetalhe().getQuantidade());
		valorDetalheNumberTextBox.setNumber(detalhe.getValor());
		ListBoxUtil.setItemSelected(tipoDetalheListBox, detalhe.getTipoDetalhe().toString());
		ListBoxUtil.setItemSelected(tipoLancamentoListBox, detalhe.getTipoLancamento().toString());
		if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO)){
			inscricaoOutraSuggestBox.setVisible(true);
			labelOutraInscricao.setVisible(true);
			casalRadio.setVisible(true);
			pessoaRadio.setVisible(true);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("encontro", presenter.getEncontroSelecionado());
			inscricaoOutraSuggest.setQueryParams(params);
			inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
			if(detalhe.getEncontroInscricaoOutra() != null){
				inscricaoOutraSuggestBox.setValue(detalhe.getEncontroInscricaoOutra().toString());
				inscricaoOutraSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
				inscricaoOutraSuggest.getListaEntidades().add(detalhe.getEncontroInscricaoOutra());
			}
		}
	}

	public void editaDetalhe(EncontroInscricaoPagamentoDetalhe detalhe) {
		limpaCamposDetalhe();
		if(detalhe == null){
			entidadeEditadaDetalhe = new EncontroInscricaoPagamentoDetalhe();
			entidadeEditadaDetalhe.setEncontroInscricao(entidadeEditada.getEncontroInscricao());
			entidadeEditadaDetalhe.setEditavel(true);
			TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values());
			if ((tipo != null) && (tipo.equals(TipoInscricaoEnum.DOACAO) || tipo.equals(TipoInscricaoEnum.EXTERNO)) ){
				inscricaoOutraSuggestBox.setVisible(true);
				labelOutraInscricao.setVisible(true);
				casalRadio.setVisible(true);
				pessoaRadio.setVisible(true);
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("encontro", presenter.getEncontroSelecionado());
				inscricaoOutraSuggest.setQueryParams(params);
				inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
				ListBoxUtil.setItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.OUTRAINSCRICAO.toString());
			}
			else
				ListBoxUtil.setItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.AVULSO.toString());
			ListBoxUtil.setItemSelected(tipoLancamentoListBox, TipoPagamentoLancamentoEnum.DEBITO.toString());
		} else {
			if (!detalhe.getEditavel()) return;
			entidadeEditadaDetalhe = detalhe;
			defineCamposDetalhe(detalhe);
		}
		detalheDialogBox.center();
		detalheDialogBox.show();
		descricaoDetalheTextBox.setFocus(true);
	}
*/
	

	@UiHandler("addDetalheImage")
	public void addDetalheImageClickHandler(ClickEvent event){
		//editaDetalhe(null);
	}
	@UiHandler("fecharPagamentoButton")
	public void fecharDetalheButtonClickHandler(ClickEvent event){
		pagamentoDialogBox.hide();
	}
	@UiHandler("salvarPagamentoButton")
	public void salvarDetalheButtonClickHandler(ClickEvent event){
		/*
		EncontroInscricao inscricao = null;
		if(!inscricaoOutraSuggestBox.getValue().equals("")){
			inscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoOutraSuggest.getListaEntidades(), inscricaoOutraSuggestBox.getValue());
		}
		TipoPagamentoDetalheEnum tipoDetalhe = (TipoPagamentoDetalheEnum)ListBoxUtil.getItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.values());
		TipoPagamentoLancamentoEnum tipoLancamento = (TipoPagamentoLancamentoEnum)ListBoxUtil.getItemSelected(tipoLancamentoListBox, TipoPagamentoLancamentoEnum.values());
		if ((quantidadeDetalheNumberTextBox.getNumber() == null) || (valorUnitarioDetalheNumberTextBox.getNumber()==null) || (valorDetalheNumberTextBox.getNumber()==null) || (tipoDetalhe == null) || (tipoLancamento == null) )
			return;
		if (tipoDetalhe.equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) && ( (inscricao==null) || inscricao.equals(entidadeEditada.getEncontroInscricao())) && tipoLancamento.equals(TipoPagamentoLancamentoEnum.CREDITO))
			return;
		entidadeEditadaDetalhe.setEncontroInscricaoOutra(inscricao);
		entidadeEditadaDetalhe.setTipoDetalhe(tipoDetalhe);
		entidadeEditadaDetalhe.setTipoLancamento(tipoLancamento);
		entidadeEditadaDetalhe.setValorUnitario(new BigDecimal(valorUnitarioDetalheNumberTextBox.getNumber().doubleValue()));
		entidadeEditadaDetalhe.setQuantidade(quantidadeDetalheNumberTextBox.getNumber().intValue());
		entidadeEditadaDetalhe.setDescricao(descricaoDetalheTextBox.getValue());
		entidadeEditadaDetalhe.setValor(new BigDecimal(valorDetalheNumberTextBox.getNumber().doubleValue()));
		if(entidadeEditada.getListaPagamentoDetalhe()==null){
			entidadeEditada.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
		} else {
			entidadeEditada.getListaPagamentoDetalhe().remove(entidadeEditadaDetalhe);
		}
		entidadeEditada.getListaPagamentoDetalhe().add(entidadeEditadaDetalhe);
		detalheDialogBox.hide();
		populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
		*/
	}

}