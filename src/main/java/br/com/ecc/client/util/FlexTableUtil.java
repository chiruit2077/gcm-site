package br.com.ecc.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.AutoHorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FlexTableUtil {

	private class Elemento {
		private Integer id;
		private Integer originalId;
		private Object[] listaWidgets;
		private List<String> listaEstilos;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Object[] getListaWidgets() {
			return listaWidgets;
		}
		public void setListaWidgets(Object[] listaWidgets) {
			this.listaWidgets = listaWidgets;
		}
		public List<String> getListaEstilos() {
			return listaEstilos;
		}
		public void setListaEstilos(List<String> listaEstilos) {
			this.listaEstilos = listaEstilos;
		}
		public Integer getOriginalId() {
			return originalId;
		}
		public void setOriginalId(Integer originalId) {
			this.originalId = originalId;
		}
	}
	
	private boolean enableSort = true;
	private Integer HeaderRowIndex = 0;
	private List<AutoHorizontalAlignmentConstant> listaAlinhamento = new ArrayList<AutoHorizontalAlignmentConstant>();
	private List<Elemento> listaElementos = new ArrayList<Elemento>();
	private List<Image> listaImagens = new ArrayList<Image>();
	private List<TipoColuna> listaTipos = new ArrayList<TipoColuna>();
	private List<String> listaFormatoData = new ArrayList<String>();
	
	public static enum TipoColuna { STRING, NUMBER, DATE };
	
	private enum Ordenacao { ASCENTENTE, DESCENDENTE };
	private Ordenacao ordenacao;
	private int ultimaColunaOrdenada = 0;
	private int columnCount = 0;
	private String serial;
	
	private FlexTable tabela;
	
	public  void initialize(FlexTable tabela) {
		serial = "T"+ tabela.hashCode();
		tabela.insertRow(HeaderRowIndex);
		tabela.getRowFormatter().addStyleName(HeaderRowIndex,"FlexTable-Header");
		tabela.addStyleName("FlexTable");
		this.tabela = tabela;
	}
	
	public  void addColumn(final Object columnHeading, String width, 
						   AutoHorizontalAlignmentConstant alinhamento ) {
		addColumn(columnHeading, width, alinhamento, TipoColuna.STRING, null);
	}
	public  void addColumn(final Object columnHeading, String width, AutoHorizontalAlignmentConstant alinhamento, TipoColuna tipo, String formatoData ) {
		columnCount++;
		if(tipo==null){
			tipo=TipoColuna.STRING;
		}
		listaTipos.add(tipo);
		listaAlinhamento.add(alinhamento);
		listaFormatoData.add(formatoData);
		final int cell = tabela.getCellCount(HeaderRowIndex);
		Widget widget;
		if(columnHeading==null || columnHeading.equals("")){
			widget = new HTML("&nbsp;");
		} else {
			widget = createCellWidget(columnHeading, null);
		}
		if (!(columnHeading instanceof Widget)){
			Label label = (Label)widget;
			label.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					sort(cell);
				}
			});
		}
		String html = "<table cellpadding='0' cellspacing='0' width='100%'>" +
				"<tr>" +
				"	<td id='one_" + serial + "_" + columnCount + "' width='95%' style='vertical-align: middle;'></td>" +
				"	<td id='two_" + serial + "_" + columnCount + "' width='14px' style='padding-top: 2px;padding-right: 2px;' align='center'></td>" +
				"</tr>" +
		  "</table>";
		HTMLPanel hp = new HTMLPanel(html);
		Image img = new Image("images/table/blank.png");
		listaImagens.add(img);
		hp.add(widget, "one_" + serial + "_"  + columnCount + "");
		hp.add(img, "two_" +  serial + "_" +  columnCount + "");
				
		if(cell==0){
			hp.addStyleName("FlexTable-ColumnLabel-FirstColumn");
		} else {
			hp.addStyleName("FlexTable-ColumnLabel");
		}
		
		tabela.setWidget(HeaderRowIndex, cell, hp);
		tabela.getCellFormatter().setWidth(HeaderRowIndex, cell, width);
	}

	public  Widget createCellWidget(Object cellObject, AutoHorizontalAlignmentConstant alinhamento) {
		Widget widget = null;
		if (cellObject instanceof Widget){
			widget = (Widget) cellObject;
		} else {
			Label l = new Label(cellObject.toString());
			if(alinhamento!=null){
				l.setAutoHorizontalAlignment(alinhamento);
			}
			widget = l;
		}
		return widget;
	}
	public Integer addRow(Object[] cellObjects, int rowIndex) {
		return addRow(cellObjects, rowIndex, true);
	}
	public Integer addRow(Object[] cellObjects, int rowIndex, boolean original) {
		Elemento elemento = new Elemento();
		if(original){
			elemento.setOriginalId(rowIndex);
		}
		elemento.setId(rowIndex);
		elemento.setListaWidgets(cellObjects);
		listaElementos.add(elemento);
		
		Widget widget;
		for (int cell = 0; cell < cellObjects.length; cell++) {
			if(cellObjects[cell]==null){
				cellObjects[cell]="";
			}
			if(cell>listaAlinhamento.size()-1){
				widget = createCellWidget(cellObjects[cell], HasHorizontalAlignment.ALIGN_LEFT);
			} else {
				widget = createCellWidget(cellObjects[cell], listaAlinhamento.get(cell));
			}
			tabela.setWidget(rowIndex, cell, widget);
			if(cell>0){
				tabela.getCellFormatter().addStyleName(rowIndex,cell,"FlexTable-Cell");
			} else {
				tabela.getCellFormatter().addStyleName(rowIndex,cell,"FlexTable-Cell-FirstColumn");
			}
		}
		return rowIndex++;
	}

	public  void applyDataRowStyles() {
		HTMLTable.RowFormatter rf = tabela.getRowFormatter();
		for (int row = 1; row < tabela.getRowCount(); ++row) {
			rf.addStyleName(row,"FlexTable-Row");
			if ((row % 2) != 0) {
				rf.addStyleName(row, "FlexTable-OddRow");
			}
			else {
				rf.addStyleName(row, "FlexTable-EvenRow");
			}
		}
	}
	public  void clearData(){
		clearData(true);
	}
	public  void clearData(boolean limpaSort){
		if(limpaSort){
			for (Image img : listaImagens) {
				img.setUrl("images/table/blank.png");
			}
		}
		listaElementos = new ArrayList<Elemento>();
		while (tabela.getRowCount()>1){
			tabela.removeRow(1);
		}
	}
	
	private void sort(final int cell){
		if(!enableSort){
			return;
		}
		for (Image img : listaImagens) {
			img.setUrl("images/table/blank.png");
		}
		Image img = listaImagens.get(cell);
		final TipoColuna tipo = listaTipos.get(cell);
		final String formatoData = listaFormatoData.get(cell)==null?"dd-MM-yyyy":listaFormatoData.get(cell);
		if(cell != ultimaColunaOrdenada){
			ordenacao = Ordenacao.ASCENTENTE;
			img.setUrl("images/table/up.png");
		} else {
			if(ordenacao==null){
				ordenacao = Ordenacao.ASCENTENTE;
				img.setUrl("images/table/up.png");
			} else {
				if(ordenacao.equals(Ordenacao.ASCENTENTE)){
					ordenacao = Ordenacao.DESCENDENTE;
					img.setUrl("images/table/down.png");
				} else {
					ordenacao = Ordenacao.ASCENTENTE;
					img.setUrl("images/table/up.png");
				}
			}
		}
		ultimaColunaOrdenada = cell;
		ArrayList<Elemento> novaListaElementos = new ArrayList<Elemento>();
		novaListaElementos.addAll(listaElementos);
		final DateTimeFormat formatter = DateTimeFormat.getFormat(formatoData);
		Collections.sort(novaListaElementos, new Comparator<Elemento>() {
			@Override
			public int compare(Elemento o1, Elemento o2) {
				if(!(o1.getListaWidgets()[cell] instanceof Widget)){
					if(tipo.equals(TipoColuna.NUMBER)){
						try {
							Double l1 = Double.parseDouble(o1.getListaWidgets()[cell].toString());
							Double l2 = Double.parseDouble(o2.getListaWidgets()[cell].toString());
							if(ordenacao.equals(Ordenacao.ASCENTENTE)){
								return l1.compareTo(l2);
							} else {
								return l2.compareTo(l1);
							}
						} catch (Exception e) {
							return 0;
						}
					} else if(tipo.equals(TipoColuna.DATE)){
						try {
							Date l1 = (Date)formatter.parse(o1.getListaWidgets()[cell].toString());
							Date l2 = (Date)formatter.parse(o2.getListaWidgets()[cell].toString());
							if(ordenacao.equals(Ordenacao.ASCENTENTE)){
								return l1.compareTo(l2);
							} else {
								return l2.compareTo(l1);
							}
						} catch (Exception e) {
							return 0;
						}
					} else {
						String l1 = o1.getListaWidgets()[cell].toString();
						String l2 = o2.getListaWidgets()[cell].toString();
						if(ordenacao.equals(Ordenacao.ASCENTENTE)){
							return l1.compareTo(l2);
						} else {
							return l2.compareTo(l1);
						}
					}
				}
				return 0;
			}
		});
		
		clearData(false);
		int rowIndex = 1;
		for (Elemento element : novaListaElementos) {
			addRow(element.getListaWidgets(), rowIndex, false);
			for (Elemento elementoNovo : listaElementos) {
				if(elementoNovo.getId().equals(rowIndex)){
					elementoNovo.setOriginalId(element.getOriginalId());
					break;
				}
			}
			if(element.getListaEstilos()!=null){
				for (String estilo : element.getListaEstilos()) {
					setRowSpecialStyle(rowIndex, estilo);
				}
			}
			rowIndex++;
		}
		applyDataRowStyles();
	}

	public void setRowSpecialStyle(Integer row, String style) {
		HTMLTable.RowFormatter rf = tabela.getRowFormatter();
		rf.addStyleName(row, style);
		for (Elemento elemento : listaElementos) {
			if(elemento.getId().equals(row)){
				if(elemento.getListaEstilos()==null){
					elemento.setListaEstilos(new ArrayList<String>());
				}
				elemento.getListaEstilos().add(style);
				break;
			}
		}
	}
	
	public void setColumnVisible(int Col, boolean b){
		for(int i=0; i< tabela.getRowCount(); i++) {
			tabela.getCellFormatter().setVisible(i, Col, b);
		}
	}
	
	public Integer getRowById(Integer id){
		for (Elemento elemento : listaElementos) {
			if(elemento.getOriginalId().equals(id)){
				return elemento.getId();
			}
		}
		return 0;
	}

	public boolean isEnableSort() {
		return enableSort;
	}
	public void setEnableSort(boolean enableSort) {
		this.enableSort = enableSort;
	}
}