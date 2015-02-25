package br.com.ecc.client.ui.sistema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.MapWidget;
import br.com.ecc.client.util.DateUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.client.util.NavegadorUtil;
import br.com.ecc.model.Agenda;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Recado;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoAgendaEventoEnum;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;
import br.com.ecc.model.vo.AniversarianteVO;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings.Click;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.controls.ControlPosition;
import com.google.gwt.maps.client.controls.MapTypeControlOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class InicioSistemaView extends BaseView<InicioSistemaPresenter> implements InicioSistemaPresenter.Display {

	@UiTemplate("InicioSistemaView.ui.xml")
	interface InicioSistemaViewUiBinder extends UiBinder<Widget, InicioSistemaView> {}
	private InicioSistemaViewUiBinder uiBinder = GWT.create(InicioSistemaViewUiBinder.class);

	private List<Agenda> listaAgenda;

	@UiField HorizontalPanel googleMapItem;

	@UiField Image logoImage;
	@UiField Image casalImage;
	@UiField Label casalLabel;
	@UiField FlowPanel convidadosFlowPanel;
	@UiField VerticalPanel areaAgendaVerticalPanel;
	@UiField VerticalPanel areaInfoVerticalPanel;
	@UiField HorizontalPanel areaAniversarioHorizontalPanel;
	
	@UiField DialogBox editaAgendaDialogBox;
	@UiField Calendar agendaCalendar;
	@UiField Button semanalButton;
	@UiField Button mensalButton;
	@UiField Button addAgendaButton;
	@UiField DateBox dateBox;
	@UiField DateBox dataInicioDateBox;
	@UiField DateBox dataFimDateBox;
	@UiField Button salvarAgendaButton;
	@UiField Button excluirAgendaButton;
	@UiField ListBox tipoAgendaLitBox;
	@UiField TextBox tituloAgendaTextBox;
	@UiField TextBox enderecoTextBox;
	@UiField TextBox bairroTextBox;
	@UiField TextBox cepTextBox;
	@UiField TextBox cidadeTextBox;
	@UiField TextBox estadoTextBox;
	@UiField(provided = true) SuggestBox casalResponsavelSuggestBox;
	private final GenericEntitySuggestOracle casalResponsavelSuggest = new GenericEntitySuggestOracle();
	
	@UiField VerticalPanel aniversarioPessoaVerticalPanel;
	@UiField VerticalPanel aniversarioCasalVerticalPanel;
	@UiField FlowPanel aniversarioPessoaFlowPanel;
	@UiField FlowPanel aniversarioCasalFlowPanel;

	@UiField VerticalPanel areaConvidadosVerticalPanel;
	@UiField VerticalPanel convidadosVerticalPanel;
	
	//apresentcacao
	@UiField DialogBox apresentacaoDialogBox;
	@UiField HorizontalPanel casalApresentacaoHorizontalPanel;
	@UiField Label casalApresentacaoLabel;
	@UiField HorizontalPanel casalPadrinhoHorizontalPanel;
	@UiField Label casalPadrinhoLabel;
	@UiField TextBox keyTextBox;
	@UiField Label labelCarregando;

	private Recado entidadeEditada;
	private Agenda entidadeAgendaEditada;
	DateTimeFormat dfRecado = DateTimeFormat.getFormat("dd-MMM HH:mm");
	DateTimeFormat dfNiver = DateTimeFormat.getFormat("E, dd-MMM");
	Date ultimoRecado = null;
	Boolean imagemLida=false, aniversariantesLidos=false;
	
	private List<Casal> listaConvidados = new ArrayList<Casal>();
	
	private ClickHandler ch = new ClickHandler() {
		@Override
		public void onClick(ClickEvent arg0) {
			keyTextBox.setFocus(true);
		}
	};

	@SuppressWarnings("deprecation")
	public InicioSistemaView() {
		casalResponsavelSuggest.setMinimoCaracteres(2);
		casalResponsavelSuggest.setSuggestQuery("casal.porNomeLike");
		casalResponsavelSuggestBox = new SuggestBox(casalResponsavelSuggest);
		
		initWidget(uiBinder.createAndBindUi(this));

		ListBoxUtil.populate(tipoAgendaLitBox, false, TipoAgendaEventoEnum.values());
		
		Window.enableScrolling(false);

		Date hoje = new Date();

		dataInicioDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		dataInicioDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		dataInicioDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
            	dataFimDateBox.setValue(DateUtil.addHoras(event.getValue(), 1));
            }
        });
		dataFimDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		dataFimDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		dateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy")));
		dateBox.setValue(hoje);
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
            	agendaCalendar.setDate(event.getValue());
            }
        });

		agendaCalendar.addOpenHandler(new OpenHandler<Appointment>(){
			  @Override
			  public void onOpen(OpenEvent<Appointment> event) {
			     Agenda agenda = getAgenda(event.getTarget().getId());
			     if( agenda != null ) editaAgenda(agenda);
			  }
		});

		agendaCalendar.setView(CalendarViews.MONTH);
		CalendarSettings settings = new CalendarSettings();
		settings.setIntervalsPerHour(4);
		settings.setEnableDragDrop(false);
		settings.setTimeBlockClickNumber(Click.Double);
		agendaCalendar.setSettings(settings);
		agendaCalendar.setDate(hoje);
		agendaCalendar.scrollToHour(hoje.getHours());
		
		keyTextBox.addDomHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        switch (event.getNativeKeyCode()) {
		        case KeyCodes.KEY_ENTER: 
		        	apresentaProximo(true);
		        	break;
		        case KeyCodes.KEY_RIGHT: 
		        	apresentaProximo(true);
		        	break;
		        case KeyCodes.KEY_DOWN:
		        	apresentaProximo(true);
		        	break;
		        case KeyCodes.KEY_PAGEDOWN:
		        	apresentaProximo(true);
		        	break;
		        	
		        case KeyCodes.KEY_LEFT: 
		        	apresentaProximo(false);
		        	break;
		        case KeyCodes.KEY_UP: 
		        	apresentaProximo(false);
		        	break;
		        case KeyCodes.KEY_PAGEUP:
		        	apresentaProximo(false);
		        	break;
		        	
		        case KeyCodes.KEY_ESCAPE: 
		        	apresentacaoDialogBox.hide();
		        	break;
		        	
		        case KeyCodes.KEY_HOME: 
		        	indiceApresentacao=0;
		        	apresentaCasal();
		        	break;
			    case KeyCodes.KEY_END: 
		        	indiceApresentacao=listaConvidados.size()-1;
		        	if(indiceApresentacao<0){
		        		indiceApresentacao=0;
		        	}
		        	apresentaCasal();
		        	break;
		        }
		    }
		}, KeyDownEvent.getType());
	}
	public Agenda getAgenda(String id) {
		for (Agenda agenda : getListaAgenda()) {
			if( agenda.getId() != null && agenda.getId().toString().equals(id))
				return agenda;
		}
		return null;
	}

	private MapWidget googleMap;

	@UiHandler("apresenterConvidadosButton")
	public void apresenterConvidadosButtonButtonClickHandler(ClickEvent event){
		apresentacaoDialogBox.setHeight((getWindowHeight()-10)+"px");
		apresentacaoDialogBox.setWidth((getWindowWidth()-10)+"px");
		apresentacaoDialogBox.center();
		apresentacaoDialogBox.setPopupPosition(5, 5);
		apresentacaoDialogBox.show();
		if(listaControleImagens.size()==0){
			casalApresentacaoHorizontalPanel.setHeight((getWindowHeight()-40)+"px");
			preparaApresentacao();
		} else {
			indiceApresentacao=0;
			apresentaCasal();
		}
	}
	
	Integer indiceApresentacao = 0;
	Boolean apresentacaoCarregada = false;
	public void apresentaProximo(boolean avanca){
		if(avanca){
			indiceApresentacao++;
		} else {
			indiceApresentacao--;
		}
		if(indiceApresentacao<0){
			indiceApresentacao=0;
		} else if (indiceApresentacao>=listaConvidados.size()){
			indiceApresentacao=listaConvidados.size()-1;
		}
		apresentaCasal();
	}
	
	private void apresentaCasal(){
		Casal casal = listaConvidados.get(indiceApresentacao);
		for (ControlImagemVO vo : listaControleImagens) {
			if(casal!=null && vo.getCasal()!=null && casal.getId().equals(vo.getCasal().getId())){
				vo.getImagemCasal().setVisible(true);
				vo.getImagemPadrinho().setVisible(true);
				
				casalApresentacaoHorizontalPanel.clear();
				casalApresentacaoHorizontalPanel.add(vo.getImagemCasal());
				
				casalPadrinhoHorizontalPanel.clear();
				casalPadrinhoHorizontalPanel.add(vo.getImagemPadrinho());
				
				casalApresentacaoLabel.setText(casal.getApelidos("e"));
				if(casal.getCasalPadrinho()!=null){
					casalPadrinhoLabel.setText(casal.getCasalPadrinho().getApelidos("e"));
				}
			} else {
				vo.getImagemCasal().setVisible(false);
				vo.getImagemPadrinho().setVisible(false);
			}
		}
		keyTextBox.setFocus(true);
	}
	
	List<ControlImagemVO> listaControleImagens = new ArrayList<ControlImagemVO>();
	Timer timerApresentacao = null;
	private void preparaApresentacao(){
		indiceApresentacao = 0;
		for (Casal casal : listaConvidados) {
			if(casal!=null){
				ControlImagemVO controlImagemVO = new ControlImagemVO();
				controlImagemVO.setCasal(casal);
				listaControleImagens.add(controlImagemVO);
				//convidado
				final Image casalApresentacaoImage = new Image();
				controlImagemVO.setImagemCasal(casalApresentacaoImage);
				casalApresentacaoImage.setVisible(false);
				if(casal.getIdArquivoDigital()!=null){
					casalApresentacaoImage.setUrl(NavegadorUtil.makeUrl("downloadArquivoDigital?id=" + casal.getIdArquivoDigital()));
					casalApresentacaoImage.addLoadHandler(new LoadHandler() {
						@Override
						public void onLoad(LoadEvent arg0) {
							casalApresentacaoImage.setHeight((getWindowHeight()-40)+"px");
							casalApresentacaoImage.setWidth("auto");
						}
					});
				} else {
					casalApresentacaoImage.setUrl("images/casal.jpg");
				}
				casalApresentacaoImage.addClickHandler(ch);
				
				//padrinho
				final Image casalPadrinhoImage = new Image();
				controlImagemVO.setImagemPadrinho(casalPadrinhoImage);
				casalPadrinhoImage.setVisible(false);
				if(casal.getCasalPadrinho()!=null){
					if(casal.getCasalPadrinho().getIdArquivoDigital()!=null){
						casalPadrinhoImage.setUrl(NavegadorUtil.makeUrl("downloadArquivoDigital?id=" + casal.getCasalPadrinho().getIdArquivoDigital()));
						casalPadrinhoImage.addLoadHandler(new LoadHandler() {
							@Override
							public void onLoad(LoadEvent arg0) {
								casalPadrinhoImage.setHeight("20%");
								casalPadrinhoImage.setWidth("auto");
								casalPadrinhoImage.setStyleName("logoPadrinho");
							}
						});
					} else {
						casalPadrinhoImage.setUrl("images/casal.jpg");
					}
				} else {
					casalPadrinhoImage.setUrl("images/casal.jpg");
				}
				casalPadrinhoImage.addClickHandler(ch);
			}
		}
		labelCarregando.setText("Carregando...");
		timerApresentacao = new Timer() {
			@Override
			public void run() {
				timerApresentacao.cancel();
				indiceApresentacao = 0;
				labelCarregando.setText(null);
				labelCarregando.setVisible(false);
				apresentaCasal();
			}

		};
		timerApresentacao.schedule(3000);
	}
	
	@UiHandler("semanalButton")
	public void semanalButtonClickHandler(ClickEvent event){
		agendaCalendar.setView(CalendarViews.DAY, 7);
	}

	@UiHandler("mensalButton")
	public void mensalButtonClickHandler(ClickEvent event){
		agendaCalendar.setView(CalendarViews.MONTH);
	}

	@UiHandler("hojeButton")
	public void hojeButtonClickHandler(ClickEvent event){
		Date hoje = new Date();
		dateBox.setValue(hoje);
		agendaCalendar.setDate(hoje);
	}
	@UiHandler("addAgendaButton")
	public void addAgendaButtonClickHandler(ClickEvent event){
		if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
			editaAgenda(null);
		}
	}

	public void editaAgenda(Agenda agenda){
		limpaCamposAgenda();
		if(agenda==null){
			entidadeAgendaEditada = new Agenda();
			entidadeAgendaEditada.setEncontro(presenter.getEncontroSelecionado());
			editaAgendaDialogBox.center();
			editaAgendaDialogBox.show();
			dataInicioDateBox.setFocus(true);
		} else {
			entidadeAgendaEditada = agenda;
			casalResponsavelSuggestBox.setText(agenda.getCasalResponsavel().toString());
			casalResponsavelSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
			casalResponsavelSuggest.getListaEntidades().add(agenda.getCasalResponsavel());
			dataInicioDateBox.setValue(agenda.getDataInicio());
			dataFimDateBox.setValue(agenda.getDataFim());
			tituloAgendaTextBox.setText(agenda.getTitulo());
			ListBoxUtil.setItemSelected(tipoAgendaLitBox, agenda.getTipo().getNome());
			enderecoTextBox.setValue(agenda.getEndereco());
			bairroTextBox.setValue(agenda.getBairro());
			cepTextBox.setValue(agenda.getCep());
			cidadeTextBox.setValue(agenda.getCidade());
			estadoTextBox.setValue(agenda.getEstado());
			editaAgendaDialogBox.center();
			editaAgendaDialogBox.show();
			tituloAgendaTextBox.setFocus(true);
			loadMapApi();
		}
	}

	@UiHandler("fecharAgendaButton")
	public void fecharAgendaButtonClickHandler(ClickEvent event){
		editaAgendaDialogBox.hide();
	}

	@UiHandler("excluirAgendaButton")
	public void excluirAgendaButtonClickHandler(ClickEvent event){
		editaAgendaDialogBox.hide();
		presenter.excluiAgenda(entidadeAgendaEditada);
	}

	@UiHandler("salvarAgendaButton")
	public void salvarAgendaButtonClickHandler(ClickEvent event){
		Casal casal = null;
		if(!casalResponsavelSuggestBox.getValue().equals("")){
			casal = (Casal)ListUtil.getEntidadePorNome(casalResponsavelSuggest.getListaEntidades(), casalResponsavelSuggestBox.getValue());
		}
		if(casal==null){
			Window.alert("Informe o nome do casal responsavel pela Agenda");
			casalResponsavelSuggestBox.setFocus(true);
			return;
		}
		entidadeAgendaEditada.setCasalResponsavel(casal);
		entidadeAgendaEditada.setDataInicio(dataInicioDateBox.getValue());
		entidadeAgendaEditada.setDataFim(dataFimDateBox.getValue());
		entidadeAgendaEditada.setTitulo(tituloAgendaTextBox.getText());
		entidadeAgendaEditada.setTipo((TipoAgendaEventoEnum)ListBoxUtil.getItemSelected(tipoAgendaLitBox, TipoAgendaEventoEnum.values()));
		entidadeAgendaEditada.setEndereco(enderecoTextBox.getValue());
		entidadeAgendaEditada.setBairro(bairroTextBox.getValue());
		entidadeAgendaEditada.setCep(cepTextBox.getValue());
		entidadeAgendaEditada.setCidade(cidadeTextBox.getValue());
		entidadeAgendaEditada.setEstado(estadoTextBox.getValue());
		editaAgendaDialogBox.hide();
		presenter.salvarAgenda(entidadeAgendaEditada);
	}

	public void limpaCamposAgenda(){
		casalResponsavelSuggestBox.setValue(null);
		tituloAgendaTextBox.setText(null);
		dataInicioDateBox.setValue(null);
		dataFimDateBox.setValue(null);
		ListBoxUtil.setItemSelected(tipoAgendaLitBox, TipoAgendaEventoEnum.REUNIAOORACAO.getNome());
	}

	@Override
	public void createScroll() {
		if(imagemLida && aniversariantesLidos){
			if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
				areaInfoVerticalPanel.setVisible(true);
				areaAgendaVerticalPanel.setVisible(true);
//				areaRecadoVerticalPanel.setVisible(true);
				//areaEventosVerticalPanel.setVisible(true);
				areaAniversarioHorizontalPanel.setVisible(true);
				areaConvidadosVerticalPanel.setVisible(true);
			}
			if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
					presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
				addAgendaButton.setVisible(true);
			}
			try {
				iniciaScroll();
			} catch (Exception e) {}
		}
	}
	private static native void iniciaScroll()/*-{
		try {
			$wnd.createScroll();
		} catch(e){}
	}-*/;

	@Override
	public void adjustWindowSize() {
		defineTamanho();
	}

	protected void defineTamanho() {
		int h = this.getWindowHeight();
		if(presenter!=null && presenter.getDadosLoginVO()!=null && presenter.getDadosLoginVO().getCasal()!=null){
			if(presenter.getDadosLoginVO().getCasal().getIdArquivoDigital()!=null){
				h = h - casalImage.getHeight() - 9;
			}
		} else {
		}
		if(h>128){
			aniversarioPessoaFlowPanel.setHeight((h-140) + "px");
			aniversarioCasalFlowPanel.setHeight((h-140) + "px");
		}
		convidadosFlowPanel.setHeight((this.getWindowHeight()-140) + "px");
		imagemLida = true;
		createScroll();
	}

	@Override
	public String getDisplayTitle() {
		return "Bem vindo ao Sistema ECC BRASIL";
	}

	@Override
	public void reset() {
	}

	@Override
	public void init() {
		if(presenter.getDadosLoginVO().getCasal()!=null){
			if(presenter.getDadosLoginVO().getCasal().getGrupo()!=null &&
			   presenter.getDadosLoginVO().getCasal().getGrupo().getIdArquivoDigital()!=null){
				logoImage.setUrl(NavegadorUtil.makeUrl("downloadArquivoDigital?id="+presenter.getDadosLoginVO().getCasal().getGrupo().getIdArquivoDigital()));
			}
			if(presenter.getDadosLoginVO().getCasal().getIdArquivoDigital()!=null){
				casalImage.setUrl(NavegadorUtil.makeUrl("downloadArquivoDigital?id="+presenter.getDadosLoginVO().getCasal().getIdArquivoDigital()));
				logoImage.setVisible(true);
			} else {
				logoImage.setVisible(false);
			}
			if(presenter.getDadosLoginVO().getCasal().getSituacao().equals(TipoSituacaoEnum.ATIVO)){
				casalLabel.setText(presenter.getDadosLoginVO().getCasal().getApelidos("e"));
			} else {
				casalLabel.setText(presenter.getDadosLoginVO().getUsuario().getPessoa().getNome());
			}
		}
		Timer resizeTimer = new Timer(){

			@Override
			public void run() {
				defineTamanho();
			}
		};
		resizeTimer.schedule(1000);
	}

	public void limpaCampos(){
	}

	@Override
	public void populaAniversariantes(List<AniversarianteVO> listaAniversarioPessoa, List<AniversarianteVO> listaAniversarioCasal) {
		//aniversariantes
		int i=0;
		aniversarioPessoaVerticalPanel.clear();
		for (AniversarianteVO niver : listaAniversarioPessoa) {
			insereAniversario(aniversarioPessoaVerticalPanel, niver,  i % 2 == 0);
			i++;
		}
		//aniversario de casamento
		i=0;
		aniversarioCasalVerticalPanel.clear();
		for (AniversarianteVO niver : listaAniversarioCasal) {
			insereAniversario(aniversarioCasalVerticalPanel, niver,  i % 2 == 0);
			i++;
		}
		aniversariantesLidos = true;
	}
	private void insereAniversario(VerticalPanel niverVerticalPanel, final AniversarianteVO niver, boolean par) {
		HorizontalPanel mainHP = new HorizontalPanel();
		mainHP.setWidth("100%");
		mainHP.setSpacing(2);
		if(niver.getHoje()){
			mainHP.setStyleName("simple-Row simple-RowBkOrange");
		} else {
			if(par){
				mainHP.setStyleName("simple-Row simple-OddRow");
			} else {
				mainHP.setStyleName("simple-Row simple-EvenRow");
			}
		}
		if(niver.getCasal().getIdArquivoDigital()!=null){
			Image imagem = new Image(NavegadorUtil.makeUrl("downloadArquivoDigital?thumb=true&id=" + niver.getCasal().getIdArquivoDigital()));
			imagem.setWidth("30px");
			imagem.setHeight("auto");
			mainHP.add(imagem);
			mainHP.setCellWidth(imagem, "30px");
			imagem.addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(LoadEvent arg0) {
					createScroll();
				}
			});
		}

		VerticalPanel aniversarianteVP = new VerticalPanel();
		aniversarianteVP.setWidth("100%");
		mainHP.add(aniversarianteVP);

		String origem="", data="";
		if(niver.getPessoa()!=null){
			if(niver.getPessoa().getId().equals(niver.getCasal().getEle().getId())){
				origem = niver.getCasal().getEle().getApelido() + " da " + niver.getCasal().getEla().getApelido();
			} else {
				origem = niver.getCasal().getEla().getApelido() + " do " + niver.getCasal().getEle().getApelido();
			}
			data = dfNiver.format(niver.getPessoa().getNascimento()).toUpperCase();
		} else {
			origem = niver.getCasal().getApelidos("e");
			data = dfNiver.format(niver.getCasal().getCasamento()).toUpperCase();
		}

		//linea 1
		HorizontalPanel dadosHP = new HorizontalPanel();
		dadosHP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dadosHP.setWidth("100%");

		Label label = new Label(origem);
		label.setStyleName("label-boldRed");
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		dadosHP.add(label);

		label = new Label();
		if(niver.getCasal().getGrupo()!=null){
			label.setText(niver.getCasal().getGrupo().getNome());
		} else {
			label.setText("*Sem grupo");
		}
		label.setStyleName("label-italicBlue");
		dadosHP.add(label);

		aniversarianteVP.add(dadosHP);

		//linea 2
		dadosHP = new HorizontalPanel();
		dadosHP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dadosHP.setWidth("100%");

		label = new Label(data);
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		dadosHP.add(label);
		
		HorizontalPanel hpSpacer = new HorizontalPanel();
		hpSpacer.setWidth("8px");
		mainHP.add(hpSpacer);
		mainHP.setCellWidth(hpSpacer, "8px");

		aniversarianteVP.add(dadosHP);

		niverVerticalPanel.add(mainHP);
	}

	@Override
	public void populaConvidados(List<Casal> listaConvidados) {
		this.listaConvidados = listaConvidados;
		int i=0;
		convidadosVerticalPanel.clear();
		for (Casal convidado : listaConvidados) {
			insereConvidado(convidadosVerticalPanel, convidado,  i % 2 == 0);
			i++;
		}
	}

	private void insereConvidado(VerticalPanel convidadoVerticalPanel, Casal convidado, boolean par){
		VerticalPanel mainVP = new VerticalPanel();
		if(par){
			mainVP.setStyleName("simple-Row simple-OddRow");
		} else {
			mainVP.setStyleName("simple-Row simple-EvenRow");
		}
		mainVP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainVP.setWidth("100%");
		mainVP.setSpacing(2);
		if(convidado.getIdArquivoDigital()!=null && !convidado.getIdArquivoDigital().equals(0)){
			Image imagem = new Image(NavegadorUtil.makeUrl("downloadArquivoDigital?id=" + convidado.getIdArquivoDigital() + "&thumb=true"));
			imagem.setWidth("90%");
			imagem.setHeight("auto");
			mainVP.add(imagem);
			imagem.addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(LoadEvent arg0) {
					createScroll();
				}
			});
		}

		HorizontalPanel hpNomes = new HorizontalPanel();
		hpNomes.setWidth("100%");

		Label label = new Label(convidado.getApelidos("e"));
		label.setStyleName("label-boldRed");
		label.setWidth("100%");
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hpNomes.add(label);

		if(convidado.getCasalPadrinho()!=null){
			label = new Label(convidado.getCasalPadrinho().getApelidos("e"));
			label.setStyleName("label-italicBlue");
			label.setWidth("100%");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			hpNomes.add(label);
		}

		HorizontalPanel hpSpacer = new HorizontalPanel();
		hpSpacer.setWidth("8px");
		hpNomes.add(hpSpacer);
		hpNomes.setCellWidth(hpSpacer, "8px");


		mainVP.add(hpNomes);

		convidadoVerticalPanel.add(mainVP);
		createScroll();
	}

	@Override
	public void populaAgenda(List<Agenda> listaAgenda) {
		setListaAgenda(listaAgenda);
		agendaCalendar.suspendLayout();
		agendaCalendar.clearAppointments();
		for (Agenda agenda : listaAgenda) {
			if (agenda.getTipo().equals(TipoAgendaEventoEnum.ENCONTRO)){
				Appointment appt = new Appointment();
				appt.setStart(agenda.getDataInicio());
				appt.setEnd(agenda.getDataFim());
				appt.setAllDay(true);
				appt.setTitle(agenda.getDescricao());
				appt.setStyle(agenda.getTipo().getCor());
				appt.setId("0");
				agendaCalendar.addAppointment(appt);
			}else{
				Appointment appt = new Appointment();
				appt.setStart(agenda.getDataInicio());
				appt.setEnd(agenda.getDataFim());
				appt.setTitle(agenda.getDescricao());
				appt.setStyle(agenda.getTipo().getCor());
				appt.setDescription(agenda.getDescricao());
				appt.setLocation(agenda.getEnderecoFull());
				appt.setId(agenda.getId().toString());
				agendaCalendar.addAppointment(appt);
			}
		}
		agendaCalendar.resumeLayout();
	}

	public List<Agenda> getListaAgenda() {
		return listaAgenda;
	}

	public void setListaAgenda(List<Agenda> listaAgenda) {
		this.listaAgenda = listaAgenda;
	}

	// CARREGANDO API DO GOOGLE
	private void loadMapApi() {
		boolean sensor = true;
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
		loadLibraries.add(LoadLibrary.DRAWING);
		loadLibraries.add(LoadLibrary.GEOMETRY);
		loadLibraries.add(LoadLibrary.PLACES);

		Runnable onLoadLibraries = new Runnable() {
			public void run() {
				drawMapa();
			}
		};
		LoadApi.go(onLoadLibraries, loadLibraries, sensor);
	}

	//
	private void drawMapa(){
		MapTypeControlOptions mapTypeControlOptions = MapTypeControlOptions.newInstance();
		mapTypeControlOptions.setPosition(ControlPosition.TOP_RIGHT);
		mapTypeControlOptions.setMapTypeIds(MapTypeId.values());

		MapOptions options = MapOptions.newInstance();
		options.setMapTypeControlOptions(mapTypeControlOptions);
		options.setZoom(MapWidget.ZOOM_LOGRADOURO);

		googleMapItem.clear();
		googleMap = new MapWidget(options);
		googleMap.setSize("400px", "400px");
		googleMap.resize();
		googleMap.setVisible(false);
		googleMapItem.add(googleMap);
		drawLogradouro();
	}

	private void drawLogradouro(){
		if (googleMap != null && !getEndereco().equals("")) {
			Geocoder geocoder = Geocoder.newInstance();
			GeocoderRequest geocoderRequest = GeocoderRequest.newInstance();
			geocoderRequest.setAddress(getEndereco());
			geocoder.geocode(geocoderRequest, new GeocoderRequestHandler() {
				@Override
				public void onCallback(JsArray<GeocoderResult> results,
						GeocoderStatus status) {
					if (status.equals(GeocoderStatus.OK)){
						GeocoderResult geocoderResult = results.get(0);
						LatLng position = geocoderResult.getGeometry().getLocation();
						googleMap.setVisible(true);
						googleMap.setSize("400px", "400px");
						googleMap.resize();
						MarkerOptions optionsMarker = MarkerOptions.newInstance();
						optionsMarker.setPosition(position);
						Marker marker = Marker.newInstance(optionsMarker);
						marker.setMap(googleMap);
						googleMap.getOverlayMapTypes().clear();
						googleMap.setZoom(MapWidget.ZOOM_LOGRADOURO);
						googleMap.setCenter(position);
					}else {
						googleMap.setVisible(false);
					}
				}
			});
		}
	}

	@UiHandler({"enderecoTextBox","bairroTextBox","cidadeTextBox","estadoTextBox"})
	public void enderecoChangeEvent(ChangeEvent event){
		drawLogradouro();
	}

	private String getEndereco() {
		StringBuffer buffer = new StringBuffer();
		if (enderecoTextBox.getValue()!=null)
			buffer.append(" " + enderecoTextBox.getValue());
		if (bairroTextBox.getValue()!=null)
			buffer.append(" " + bairroTextBox.getValue());
		if (cidadeTextBox.getValue()!=null)
			buffer.append(" " + cidadeTextBox.getValue());
		if (estadoTextBox.getValue()!=null)
			buffer.append(" " + estadoTextBox.getValue());
		return buffer.toString().trim();
	}
	
	private class ControlImagemVO {
		Casal casal;
		Image imagemCasal;
		Image imagemPadrinho;
		
		public Casal getCasal() {
			return casal;
		}
		public void setCasal(Casal casal) {
			this.casal = casal;
		}
		public Image getImagemCasal() {
			return imagemCasal;
		}
		public void setImagemCasal(Image imagemCasal) {
			this.imagemCasal = imagemCasal;
		}
		public Image getImagemPadrinho() {
			return imagemPadrinho;
		}
		public void setImagemPadrinho(Image imagemPadrinho) {
			this.imagemPadrinho = imagemPadrinho;
		}
	}
	
}