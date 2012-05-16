package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.model.tipo.TipoAcessoEnum;

@Entity
@SequenceGenerator(name="SQ_USUARIOACESSO", sequenceName="SQ_USUARIOACESSO")
@NamedQueries({
	@NamedQuery(name="usuarioAcesso.porUsuario", query="select u from UsuarioAcesso u where u.usuario = :usuario"),
	@NamedQuery(name="usuarioAcesso.deletePorUsuario", 
		query="delete from UsuarioAcesso u where u.usuario = :usuario"),
	@NamedQuery(name="usuarioAcesso.deletePorUsuarioNotIn", 
		query="delete from UsuarioAcesso u where u.usuario = :usuario and u not in (:lista)")
	})
public class UsuarioAcesso extends _WebBaseEntity {
	private static final long serialVersionUID = 6344322920680027869L;

	@Id
	@GeneratedValue(generator="SQ_USUARIOACESSO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="usuario")
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(length=50)
	private PresenterCodeEnum presenterCode;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoAcessoEnum tipoAcesso;
	
	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public PresenterCodeEnum getPresenterCode() {
		return presenterCode;
	}
	public void setPresenterCode(PresenterCodeEnum presenterCode) {
		this.presenterCode = presenterCode;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public TipoAcessoEnum getTipoAcesso() {
		return tipoAcesso;
	}
	public void setTipoAcesso(TipoAcessoEnum tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}
}