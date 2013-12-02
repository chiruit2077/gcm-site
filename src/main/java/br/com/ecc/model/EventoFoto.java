package br.com.ecc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_EVENTOFOTO", sequenceName="SQ_EVENTOFOTO")
@NamedQueries({
	@NamedQuery(name="eventoFoto.porEvento", 
		query="select u from EventoFoto u where u.evento = :evento"),
	@NamedQuery(name="eventoFoto.deletePorEvento",
		query="delete from EventoFoto u where u.evento = :evento"),
	@NamedQuery(name="eventoFoto.deletePorEventoNotIn",
		query="delete from EventoFoto u where u.evento = :evento and u not in (:lista)")
})
public class EventoFoto extends _WebBaseEntity {
	private static final long serialVersionUID = -8676454864854908170L;

	@Id
	@GeneratedValue(generator="SQ_EVENTOFOTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="evento")
	private Evento evento;

	private Integer idArquivoDigital;

	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdArquivoDigital() {
		return idArquivoDigital;
	}
	public void setIdArquivoDigital(Integer idArquivoDigital) {
		this.idArquivoDigital = idArquivoDigital;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}