package br.com.ecc.client.ui.sistema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.MapWidget;
import br.com.ecc.client.ui.component.upload.UploadImagePreview;
import br.com.ecc.client.ui.component.upload.UploadImagemItem;
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
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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
import com.google.gwt.user.client.ui.Anchor;
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
//	@UiField VerticalPanel recadosVerticalPanel;
//	@UiField FlowPanel recadosFlowPanel;
//	@UiField Anchor verLidosAnchor;
//	@UiField Anchor esconderLidosAnchor;
	// @UiField VerticalPanel areaRecadoVerticalPanel;
	@UiField VerticalPanel areaAgendaVerticalPanel;
	@UiField VerticalPanel areaInfoVerticalPanel;
	@UiField HorizontalPanel areaAniversarioHorizontalPanel;
	
	
	@UiField VerticalPanel areaEventosVerticalPanel;
	@UiField Anchor addEventoAnchor;
	@UiField FlowPanel eventosFlowPanel;
	@UiField VerticalPanel eventosVerticalPanel;

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
	
	@UiField DialogBox editaEventoDialogBox;
	@UiField TextBox descricaoEventoTextBox;
	@UiField FlowPanel fotosFlowPanel;
	@UiField VerticalPanel fotosEventosVerticalPanel;
	
	@UiField DialogBox imageDialogBox;
	@UiField UploadImagePreview uploadImagePreview;
	@UiField Button selecionarUploadButton;
	@UiField Button aceitarUploadButton;
	@UiField Button cancelarUploadButton;


//	@UiField DialogBox editaDialogBox;
//	@UiField HTMLPanel tipoHTMLPanel;
//	@UiField(provided=true) RichTextArea mensagemRichTextArea;
//	@UiField(provided=true) RichTextToolbar mensagemRichTextToolbar;
//
//	@UiField Image casalRecadoImage;
//	@UiField(provided = true) SuggestBox casalSuggestBox;
//	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();
//	@UiField Label casalRecadoLabel;
//	@UiField ListBox tipoLitBox;
//
//	@UiField DialogBox todosDialogBox;
//	@UiField FlowPanel formularioFlowPanel;
//	@UiField VerticalPanel recadosTodosVerticalPanel;

	@UiField VerticalPanel aniversarioPessoaVerticalPanel;
	@UiField VerticalPanel aniversarioCasalVerticalPanel;
	@UiField FlowPanel aniversarioPessoaFlowPanel;
	@UiField FlowPanel aniversarioCasalFlowPanel;

	@UiField VerticalPanel areaConvidadosVerticalPanel;
	@UiField VerticalPanel convidadosVerticalPanel;

	private Recado entidadeEditada;
	private Agenda entidadeAgendaEditada;
	DateTimeFormat dfRecado = DateTimeFormat.getFormat("dd-MMM HH:mm");
	DateTimeFormat dfNiver = DateTimeFormat.getFormat("E, dd-MMM");
	Date ultimoRecado = null;
	Boolean imagemLida=false, aniversariantesLidos=false;

	@SuppressWarnings("deprecation")
	public InicioSistemaView() {
		/*
		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);

		mensagemRichTextArea = new RichTextArea();
		mensagemRichTextToolbar = new RichTextToolbar(mensagemRichTextArea);
		 */
		
		casalResponsavelSuggest.setMinimoCaracteres(2);
		casalResponsavelSuggest.setSuggestQuery("casal.porNomeLike");
		casalResponsavelSuggestBox = new SuggestBox(casalResponsavelSuggest);
		
		initWidget(uiBinder.createAndBindUi(this));

		ListBoxUtil.populate(tipoAgendaLitBox, false, TipoAgendaEventoEnum.values());
		
		/*
		ListBoxUtil.populate(tipoLitBox, false, TipoRecadoEnum.values());

		casalSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				casalRecadoImage.setUrl("");
				if(!casalSuggestBox.getValue().equals("")){
					Casal casal = (Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue());
					if(casal.getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
						Window.alert("Este é um casal convidado e não pode receber recados");
						casalSuggestBox.setValue(null);
						casalSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
						casalRecadoImage.setUrl("");
						casalSuggestBox.setFocus(true);
						return;
					} else {
						if(casal.getIdArquivoDigital()!=null){
							casalRecadoImage.setUrl(NavegadorUtil.makeUrl("downloadArquivoDigital?thumb=true&id="+casal.getIdArquivoDigital()));
						}
					}
				}
			}
		});
		casalSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				if(casalSuggestBox.getValue().equals("")){
					casalRecadoImage.setUrl("");
				}
			}
		});
		casalImage.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent arg0) {
				defineTamanho();
			}
		});
		*/

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


		Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                resizeTimer.schedule(500);
            }
        });

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
            	resizeTimer.schedule(500);
            }
		});
	}

	public Agenda getAgenda(String id) {
		for (Agenda agenda : getListaAgenda()) {
			if( agenda.getId() != null && agenda.getId().toString().equals(id))
				return agenda;
		}
		return null;
	}

	private int height = -1;
	private int width = -1;

	private Timer resizeTimer = new Timer() {
        @Override
        public void run() {
        	int ajusteHeight=110;
            int newHeight = (int) (InicioSistemaView.this.getWindowHeight()*0.4);
            int newWidth = (int) (InicioSistemaView.this.getWindowWidth()-620);
            //967px
            if (newHeight != height || newWidth != width) {
            	height = newHeight;
            	width = newWidth;
            	areaAgendaVerticalPanel.setWidth(width+"px");
            	areaAgendaVerticalPanel.setHeight(height + "px");
                agendaCalendar.setHeight(height+"px");
                agendaCalendar.setWidth(width+"px");
                agendaCalendar.doSizing();
                agendaCalendar.doLayout();
//                areaRecadoVerticalPanel.setWidth(width+"px");
//                areaRecadoVerticalPanel.setHeight(((InicioSistemaView.this.getWindowHeight()-height)-ajusteHeight) + "px");
                
                areaEventosVerticalPanel.setWidth(width+"px");
                areaEventosVerticalPanel.setHeight(((InicioSistemaView.this.getWindowHeight()-height)-ajusteHeight) + "px");
                
//                recadosVerticalPanel.setWidth((width-5)+"px");
//                recadosVerticalPanel.setHeight(((InicioSistemaView.this.getWindowHeight()-height)-ajusteHeight) + "px");
//                recadosFlowPanel.setWidth((width-5)+"px");
//        		recadosFlowPanel.setHeight(((InicioSistemaView.this.getWindowHeight()-height)-ajusteHeight) + "px");
            }
        }
    };

	private MapWidget googleMap;


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
			if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
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
		//defineTamanho();
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
		defineTamanho();
	}
/*
	@Override
	public void populaRecados(List<Recado> listaRecados) {
		populaObjetoRecados(listaRecados, recadosVerticalPanel, true);
	}
	@UiHandler("addRecadoAnchor")
	public void addRecadoAnchorClickHandler(ClickEvent event){
		if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
			edita(null);
		}
	}
	@UiHandler("verLidosAnchor")
	public void verLidosAnchorClickHandler(ClickEvent event){
		esconderLidosAnchor.setVisible(true);
		verLidosAnchor.setVisible(false);
		presenter.setVerTodos(true);
		presenter.lista(false);
	}
	@UiHandler("esconderLidosAnchor")
	public void esconderLidosAnchorClickHandler(ClickEvent event){
		esconderLidosAnchor.setVisible(false);
		verLidosAnchor.setVisible(true);
		presenter.setVerTodos(false);
		presenter.lista(false);
	}

	public void edita(Recado recado){
		limpaCampos();
		if(recado==null){
			entidadeEditada = new Recado();
			casalSuggestBox.setVisible(true);
			casalRecadoLabel.setVisible(false);
			tipoHTMLPanel.setVisible(true);
		} else {
			entidadeEditada = recado;
			casalSuggestBox.setText(recado.getCasal().toString());
			casalRecadoLabel.setText(recado.getCasal().toString());
			casalSuggestBox.setVisible(false);
			casalSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
			casalSuggest.getListaEntidades().add(recado.getCasal());
			casalRecadoLabel.setVisible(true);
			if(recado.getCasal().getIdArquivoDigital()!=null){
				casalRecadoImage.setUrl(NavegadorUtil.makeUrl("downloadArquivoDigital?id="+recado.getCasal().getIdArquivoDigital()));
			}
			tipoHTMLPanel.setVisible(false);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		mensagemRichTextArea.setFocus(true);
		casalSuggestBox.setFocus(true);
	}

	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("enviarButton")
	public void enviarButtonClickHandler(ClickEvent event){
		Casal casal = null;
		if(!casalSuggestBox.getValue().equals("")){
			casal = (Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue());
		}
		if(casal==null){
			Window.alert("Informe o nome do casal ao que deseja enviar o recado");
			casalSuggestBox.setFocus(true);
			return;
		}
		entidadeEditada.setCasal(casal);
		entidadeEditada.setCasalOrigem(presenter.getDadosLoginVO().getCasal());
		entidadeEditada.setPessoaOrigem(presenter.getDadosLoginVO().getUsuario().getPessoa());
		entidadeEditada.setMensagem(mensagemRichTextArea.getHTML().toCharArray());
		entidadeEditada.setTipo((TipoRecadoEnum)ListBoxUtil.getItemSelected(tipoLitBox, TipoRecadoEnum.values()));
		presenter.salvar(entidadeEditada);
	}
*/
	public void limpaCampos(){
		/*
		casalSuggestBox.setValue(null);
		mensagemRichTextArea.setText(null);
		casalRecadoImage.setUrl("");
		ListBoxUtil.setItemSelected(tipoLitBox, TipoRecadoEnum.ENCONTRISTA.getNome());
		*/
	}

	/*
	@Override
	public void fechaRecado() {
		editaDialogBox.hide();
	}
	@UiHandler("verTodosAnchor")
	public void verTodosAnchorClickHandler(ClickEvent event){
		presenter.listaRecadosTodos(ultimoRecado);
		todosDialogBox.center();
		todosDialogBox.show();
	}
	@UiHandler("fecharTodosButton")
	public void fecharTodosButtonClickHandler(ClickEvent event){
		ultimoRecado = null;
		recadosTodosVerticalPanel.clear();
		todosDialogBox.hide();
	}
	@UiHandler("maisTodosButton")
	public void maisTodosButtonClickHandler(ClickEvent event){
		presenter.listaRecadosTodos(ultimoRecado);
	}
	@Override
	public void populaRecadosPorGrupo(List<Recado> listaRecados) {
		if(listaRecados.size()>0){
			ultimoRecado = listaRecados.get(listaRecados.size()-1).getData();
		}
		populaObjetoRecados(listaRecados, recadosTodosVerticalPanel, false);
		createScroll();
	}

	private void populaObjetoRecados(List<Recado> listaRecados, VerticalPanel recadosVerticalPanel, boolean limpa){
		if(limpa){
			recadosVerticalPanel.clear();
		}
		if(listaRecados.size()==0){
			Label label = new Label("Nenhum recado encontrado");
			label.setWidth("100%");
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			recadosVerticalPanel.add(label);
		} else {
			int i=0;
			boolean go;
			for (final Recado recado : listaRecados) {
				go = true;
				for (Recado r : listaRecados) {
					if(r.getPai()!=null && r.getPai().getId().equals(recado.getId()) ){
						go = false;
					}
				}
				if(go){
					if(insereRecado(recadosVerticalPanel, recado,  i % 2 == 0, true)){
						i++;
					}
				}
			}
		}
		createScroll();
	}

	private Boolean insereRecado(VerticalPanel recadosVerticalPanel, final Recado recado, boolean par, boolean original) {
		boolean ok = true;
		if(recado.getPai()!=null){
			if(recado.getPai().getLido()){
				return false;
			}
			ok = insereRecado(recadosVerticalPanel, recado.getPai(), par, false);
		}
		if(recado.getMensagem()==null){
			return false;
		}
		if(!ok){
			return ok;
		}
		HorizontalPanel mainHP = new HorizontalPanel();
		mainHP.setWidth("100%");
		mainHP.setSpacing(2);
		if(par){
			mainHP.setStyleName("simple-Row simple-OddRow");
		} else {
			mainHP.setStyleName("simple-Row simple-EvenRow");
		}
		if(recado.getPai()!=null){
			HorizontalPanel hp3 = new HorizontalPanel();
			hp3.setWidth("15px");
			mainHP.add(hp3);
		}
		if(recado.getCasal().getIdArquivoDigital()!=null){
			Image imagem = new Image(NavegadorUtil.makeUrl("downloadArquivoDigital?thumb=true&id=" + recado.getCasalOrigem().getIdArquivoDigital()));
			imagem.setWidth("60px");
			imagem.setHeight("auto");
			mainHP.add(imagem);
			mainHP.setCellWidth(imagem, "60px");
			imagem.addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(LoadEvent arg0) {
					createScroll();
				}
			});
		}

		VerticalPanel recadoVP = new VerticalPanel();
		recadoVP.setWidth("100%");
		mainHP.add(recadoVP);

		String origem="";
		if(recado.getCasalOrigem().getSituacao().equals(TipoSituacaoEnum.ATIVO)){
			origem = recado.getCasalOrigem().getApelidos("e");
		} else {
			origem = recado.getPessoaOrigem().getNome();
		}

		HorizontalPanel hpSpacer;

		//linea 1
		HorizontalPanel dadosHP = new HorizontalPanel();
		dadosHP.setWidth("100%");

		HorizontalPanel hp2 = new HorizontalPanel();

		Label label = new Label(origem);
		label.setStyleName("label-boldRed");
		hp2.add(label);

		hpSpacer = new HorizontalPanel();
		hpSpacer.setWidth("3px");
		hp2.add(hpSpacer);

		label = new Label(" > ");
		label.setStyleName("label-bold");
		hp2.add(label);

		hpSpacer = new HorizontalPanel();
		hpSpacer.setWidth("3px");
		hp2.add(hpSpacer);

		label = new Label(recado.getCasal().getApelidos("e"));
		label.setStyleName("label-boldBlue");
		hp2.add(label);

		dadosHP.add(hp2);

		label = new Label(dfRecado.format(recado.getData()).toUpperCase());
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dadosHP.add(label);
		dadosHP.setCellWidth(label, "100px");

		recadoVP.add(dadosHP);

		//linea 2
		recadoVP.add(new HTML(String.valueOf(recado.getMensagem())));

		//linea 3
		if(original || presenter.getDadosLoginVO().getCasal().getId().equals(recado.getCasal().getId())){
			hp2 = new HorizontalPanel();
			Anchor responder, esconder;
			if(presenter.getDadosLoginVO().getCasal().getId().equals(recado.getCasal().getId()) && !recado.getLido()){
				if(!recado.getLido()){
					esconder = new Anchor("Lido");
					esconder.setStyleName("portal-anchor");
					esconder.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							recado.setLido(true);
							presenter.salvar(recado);
							((Anchor)arg0.getSource()).setVisible(false);
						}
					});
					hp2.add(esconder);
				}
				hpSpacer = new HorizontalPanel();
				hpSpacer.setWidth("3px");
				hp2.add(hpSpacer);
			}
			if(!presenter.getDadosLoginVO().getCasal().getId().equals(recado.getCasalOrigem().getId())){
				responder = new Anchor("Responder");
				responder.setStyleName("portal-anchor");
				responder.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						Recado r = new Recado();
						r.setCasal(recado.getCasalOrigem());
						r.setPai(recado);
						r.setTipo(recado.getTipo());
						edita(r);
					}
				});
				hp2.add(responder);
			}
			recadoVP.add(hp2);
		}
		mainHP.add(recadoVP);

		hpSpacer = new HorizontalPanel();
		hpSpacer.setWidth("8px");
		mainHP.add(hpSpacer);
		mainHP.setCellWidth(hpSpacer, "8px");

		recadosVerticalPanel.add(mainHP);
		return ok;
	}
*/
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
		/*
		if(niver.getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
			Anchor recado = new Anchor("Enviar recado");
			recado.setStyleName("portal-anchor");
			recado.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					Recado r = new Recado();
					r.setCasal(niver.getCasal());
					edita(r);
				}
			});
			dadosHP.add(recado);
		}
		*/
		HorizontalPanel hpSpacer = new HorizontalPanel();
		hpSpacer.setWidth("8px");
		mainHP.add(hpSpacer);
		mainHP.setCellWidth(hpSpacer, "8px");

		aniversarianteVP.add(dadosHP);

		niverVerticalPanel.add(mainHP);
	}

	@Override
	public void populaConvidados(List<Casal> listaConvidados) {
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
		//buffer.append(" BRASIL");
		return buffer.toString().trim();
	}
	
	
	@UiHandler("addEventoAnchor")
	public void addEventoAnchorClickHandler(ClickEvent event){
		editaEventoDialogBox.center();
		editaEventoDialogBox.show();
	}
	
	@UiHandler("salvarEventoButton")
	public void salvarEventoButtonClickHandler(ClickEvent event){
		editaEventoDialogBox.hide();
	}
	
	@UiHandler("fecharEventoButton")
	public void fecharEventoButtonClickHandler(ClickEvent event){
		editaEventoDialogBox.hide();
	}
	
	@UiHandler("addFotoEventoButton")
	public void addFotoEventoButtonClickHandler(ClickEvent event){
		fotosEventosVerticalPanel.clear();
		descricaoEventoTextBox.setValue(null);
		
		
		uploadImagePreview.setMultiple(true);
		uploadImagePreview.clear();
		imageDialogBox.center();
		imageDialogBox.show();
		uploadImagePreview.adicionaImage();
	}
	
	
	@UiHandler("selecionarUploadButton")
	public void selecionarUploadButtonClickHandler(ClickEvent event){
		uploadImagePreview.clear();
		uploadImagePreview.adicionaImage();
	}

	@UiHandler("aceitarUploadButton")
	public void aceitarUploadButtonClickHandler(ClickEvent event){
		fotosEventosVerticalPanel.clear();
		Image image;
		int i=0;
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		for (UploadImagemItem up : uploadImagePreview.getListaImagens()) {
			if(i % 3 ==0){
				hp = new HorizontalPanel();
				fotosEventosVerticalPanel.add(hp);
				hp.setWidth("100%");
			}
			image = new Image(NavegadorUtil.makeUrl("downloadArquivoDigital?id=" + up.getIdArquivoDigital() + "&thumb=true"));
			image.setWidth("200px");
			image.setHeight("auto");
			hp.add(image);
			hp.add(new Label("Foto1"));
			i++;
		}
		imageDialogBox.hide();
	}

	@UiHandler("cancelarUploadButton")
	public void cancelarUploadButtonClickHandler(ClickEvent event){
		imageDialogBox.hide();
	}
	
}