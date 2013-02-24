package br.com.ecc.client.ui.sistema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.richtext.RichTextToolbar;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Agenda;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Recado;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoAgendaEventoEnum;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoRecadoEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;
import br.com.ecc.model.vo.AniversarianteVO;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.bradrydzewski.gwt.calendar.client.event.DateRequestEvent;
import com.bradrydzewski.gwt.calendar.client.event.DateRequestHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class InicioSistemaView extends BaseView<InicioSistemaPresenter> implements InicioSistemaPresenter.Display {

	@UiTemplate("InicioSistemaView.ui.xml")
	interface InicioSistemaViewUiBinder extends UiBinder<Widget, InicioSistemaView> {}
	private InicioSistemaViewUiBinder uiBinder = GWT.create(InicioSistemaViewUiBinder.class);


	@UiField Image logoImage;
	@UiField Image casalImage;
	@UiField Label casalLabel;
	@UiField VerticalPanel recadosVerticalPanel;
	@UiField FlowPanel recadosFlowPanel;
	@UiField Anchor verLidosAnchor;
	@UiField Anchor esconderLidosAnchor;
	@UiField VerticalPanel areaRecadoVerticalPanel;
	@UiField VerticalPanel areaAgendaVerticalPanel;
	@UiField VerticalPanel areaInfoVerticalPanel;
	@UiField HorizontalPanel areaAniversarioHorizontalPanel;
	@UiField Calendar agendaCalendar;
	@UiField Button semanalButton;
	@UiField Button mensalButton;
	@UiField Button addEventoButton;
	@UiField DateBox dateBox;

	@UiField DialogBox editaDialogBox;
	@UiField HTMLPanel tipoHTMLPanel;
	@UiField(provided=true) RichTextArea mensagemRichTextArea;
	@UiField(provided=true) RichTextToolbar mensagemRichTextToolbar;

	@UiField Image casalRecadoImage;
	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();
	@UiField Label casalRecadoLabel;
	@UiField ListBox tipoLitBox;

	@UiField DialogBox todosDialogBox;
	@UiField FlowPanel formularioFlowPanel;
	@UiField VerticalPanel recadosTodosVerticalPanel;

	@UiField VerticalPanel aniversarioPessoaVerticalPanel;
	@UiField VerticalPanel aniversarioCasalVerticalPanel;
	@UiField FlowPanel aniversarioPessoaFlowPanel;
	@UiField FlowPanel aniversarioCasalFlowPanel;

	@UiField VerticalPanel areaConvidadosVerticalPanel;
	@UiField FlowPanel convidadosFlowPanel;
	@UiField VerticalPanel convidadosVerticalPanel;

	private Recado entidadeEditada;
	DateTimeFormat dfRecado = DateTimeFormat.getFormat("dd-MMM HH:mm");
	DateTimeFormat dfNiver = DateTimeFormat.getFormat("E, dd-MMM");
	Date ultimoRecado = null;
	Boolean imagemLida=false, aniversariantesLidos=false;

	@SuppressWarnings("deprecation")
	public InicioSistemaView() {
		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);

		mensagemRichTextArea = new RichTextArea();
		mensagemRichTextToolbar = new RichTextToolbar(mensagemRichTextArea);

		initWidget(uiBinder.createAndBindUi(this));

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
							casalRecadoImage.setUrl("eccweb/downloadArquivoDigital?thumb=true&id="+casal.getIdArquivoDigital());
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

		formularioFlowPanel.setHeight((this.getWindowHeight() - 150) + "px");

		Date hoje = new Date();

		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.setValue(hoje);
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
            	agendaCalendar.setDate(event.getValue());
            }
        });

		agendaCalendar.addDateRequestHandler(new DateRequestHandler<Date>(){
			public void onDateRequested(DateRequestEvent<Date> event) {
				Window.alert("requested: " + event.getTarget() + " " + ((Element)event.getClicked()).getInnerText());
			}
        });

		agendaCalendar.setSize("100%","600px");
		agendaCalendar.setView(CalendarViews.MONTH);
		CalendarSettings settings = new CalendarSettings();
		settings.setIntervalsPerHour(4);
		settings.setEnableDragDrop(false);
		agendaCalendar.setSettings(settings);
		agendaCalendar.scrollToHour(hoje.getHours());

		agendaCalendar.setDate(hoje);

		Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                resizeTimer.schedule(500);
            }
        });

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
            	if (areaAgendaVerticalPanel.getElement().getClientHeight() > 0 )
            		agendaCalendar.setHeight(areaAgendaVerticalPanel.getElement().getClientHeight() - 85 + "px");
            }
		});
	}

	private int height = -1;

	private Timer resizeTimer = new Timer() {
        @Override
        public void run() {
            int newHeight = Window.getClientHeight();
            if (newHeight != height) {
                height = newHeight;
                agendaCalendar.setHeight(height - 85 + "px");
                agendaCalendar.doSizing();
                agendaCalendar.doLayout();
            }
        }
    };


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

	@Override
	public void createScroll() {
		if(imagemLida && aniversariantesLidos){
			if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
				areaInfoVerticalPanel.setVisible(true);
				areaAgendaVerticalPanel.setVisible(true);
				areaRecadoVerticalPanel.setVisible(true);
				areaAniversarioHorizontalPanel.setVisible(true);
				areaConvidadosVerticalPanel.setVisible(true);
			}
			if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
				addEventoButton.setVisible(true);
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
			aniversarioPessoaFlowPanel.setHeight((h-128) + "px");
			aniversarioCasalFlowPanel.setHeight((h-128) + "px");
		}

		imagemLida = true;
		createScroll();
	}

	@Override
	public String getDisplayTitle() {
		return "Bem vindo ao sistema";
	}

	@Override
	public void reset() {
	}

	@Override
	public void init() {
		if(presenter.getDadosLoginVO().getCasal()!=null){
			if(presenter.getDadosLoginVO().getCasal().getGrupo()!=null &&
			   presenter.getDadosLoginVO().getCasal().getGrupo().getIdArquivoDigital()!=null){
				logoImage.setUrl("eccweb/downloadArquivoDigital?id="+presenter.getDadosLoginVO().getCasal().getGrupo().getIdArquivoDigital());
			}
			if(presenter.getDadosLoginVO().getCasal().getIdArquivoDigital()!=null){
				casalImage.setUrl("eccweb/downloadArquivoDigital?id="+presenter.getDadosLoginVO().getCasal().getIdArquivoDigital());
				logoImage.setVisible(true);
			} else {
				logoImage.setVisible(false);
			}
			if(presenter.getDadosLoginVO().getCasal().getSituacao().equals(TipoSituacaoEnum.ATIVO)){
				casalLabel.setText(presenter.getDadosLoginVO().getCasal().getApelidos("e"));
			} else {
				casalLabel.setText(presenter.getDadosLoginVO().getUsuario().getPessoa().getNome());
			}
		} else {
//			aniversarioPessoaFlowPanel.setHeight(((this.getWindowHeight() - 120)/2)-2 + "px");
//			aniversarioCasalFlowPanel.setHeight(((this.getWindowHeight() - 120)/2)-2 + "px");
		}
		recadosFlowPanel.setHeight(((this.getWindowHeight() - 600 ) - 94) + "px");
		convidadosFlowPanel.setHeight((this.getWindowHeight() - 94) + "px");
	}

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
				casalRecadoImage.setUrl("eccweb/downloadArquivoDigital?id="+recado.getCasal().getIdArquivoDigital());
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

	public void limpaCampos(){
		casalSuggestBox.setValue(null);
		mensagemRichTextArea.setText(null);
		casalRecadoImage.setUrl("");
		ListBoxUtil.setItemSelected(tipoLitBox, TipoRecadoEnum.ENCONTRISTA.getNome());
	}

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
			Image imagem = new Image("eccweb/downloadArquivoDigital?thumb=true&id=" + recado.getCasalOrigem().getIdArquivoDigital());
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
			Image imagem = new Image("eccweb/downloadArquivoDigital?thumb=true&id=" + niver.getCasal().getIdArquivoDigital());
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
			Image imagem = new Image("eccweb/downloadArquivoDigital?id=" + convidado.getIdArquivoDigital());
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
		agendaCalendar.suspendLayout();
		for (Agenda agenda : listaAgenda) {
			if (agenda.getTipo().equals(TipoAgendaEventoEnum.ENCONTRO)){
				Appointment appt = new Appointment();
				appt.setStart(agenda.getDataInicio());
				appt.setEnd(agenda.getDataFim());
				appt.setAllDay(true);
				appt.setTitle(agenda.getDescricao());
				appt.setStyle(agenda.getTipo().getCor());
				agendaCalendar.addAppointment(appt);
			}else{
				Appointment appt = new Appointment();
				appt.setStart(agenda.getDataInicio());
				appt.setEnd(agenda.getDataFim());
				appt.setTitle(agenda.getDescricao());
				appt.setStyle(agenda.getTipo().getCor());
				appt.setDescription(agenda.getDescricao());
				appt.setLocation(agenda.getEnderecoFull());
				agendaCalendar.addAppointment(appt);
			}
		}
		agendaCalendar.resumeLayout();

	}

}