package br.com.ecc.model.tipo;

import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;

public enum TipoAgendaEventoEnum {
	ENCONTRO("Encontro", AppointmentStyle.GREEN, 0),
	REUNIAOORACAO("Reunião Oração", AppointmentStyle.YELLOW, 1),
	ENSAIOBANDA("Ensaio Banda", AppointmentStyle.BLUE, 2),
	ENSAIOCOREOGRAFIA("Ensaio Coreografia", AppointmentStyle.BLUE, 3),
	REUNIAOGERAL("Reunião Geral", AppointmentStyle.RED, 4),
	REUNIAOPADRINHO("Reunião Padrinhos", AppointmentStyle.BROWN, 5),
	REUNIAOAPOIO("Reunião Apoio",AppointmentStyle.BROWN, 6),
	SANTACEIA("Santa Ceia",AppointmentStyle.GREEN, 7),
	MULTIRAO("Solteiros",AppointmentStyle.PURPLE, 8);

	private String nome;
	private Integer codigo;
	private AppointmentStyle cor;

	private TipoAgendaEventoEnum(String nome, AppointmentStyle cor, Integer codigo) {
		this.setCodigo(codigo);
		this.setCor(cor);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public AppointmentStyle getCor() {
		return cor;
	}
	public void setCor(AppointmentStyle cor) {
		this.cor = cor;
	}
}