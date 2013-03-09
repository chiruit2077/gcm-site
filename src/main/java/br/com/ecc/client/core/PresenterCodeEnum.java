package br.com.ecc.client.core;

import br.com.ecc.client.ui.home.HomePresenter;
import br.com.ecc.client.ui.sistema.InicioSistemaPresenter;
import br.com.ecc.client.ui.sistema.MeusDadosPresenter;
import br.com.ecc.client.ui.sistema.cadastro.AgrupamentoPresenter;
import br.com.ecc.client.ui.sistema.cadastro.AtividadePresenter;
import br.com.ecc.client.ui.sistema.cadastro.CasalPresenter;
import br.com.ecc.client.ui.sistema.cadastro.EncontroPresenter;
import br.com.ecc.client.ui.sistema.cadastro.GrupoPresenter;
import br.com.ecc.client.ui.sistema.cadastro.HotelPresenter;
import br.com.ecc.client.ui.sistema.cadastro.PapelPresenter;
import br.com.ecc.client.ui.sistema.cadastro.RestauranteLayoutPresenter;
import br.com.ecc.client.ui.sistema.cadastro.RestaurantePresenter;
import br.com.ecc.client.ui.sistema.encontro.DistribuicaoOrganogramaPresenter;
import br.com.ecc.client.ui.sistema.encontro.EncontroConvitePresenter;
import br.com.ecc.client.ui.sistema.encontro.EncontroInscricaoPresenter;
import br.com.ecc.client.ui.sistema.encontro.EncontroOrganogramaPresenter;
import br.com.ecc.client.ui.sistema.encontro.PlanilhaPresenter;
import br.com.ecc.client.ui.sistema.hotelaria.DistribuicaoQuartosPresenter;
import br.com.ecc.client.ui.sistema.hotelaria.EncontroHotelPresenter;
import br.com.ecc.client.ui.sistema.patrimonio.ItemPatrimonioPresenter;
import br.com.ecc.client.ui.sistema.secretaria.DistribuicaoRestaurantePresenter;
import br.com.ecc.client.ui.sistema.secretaria.ListagemPresenter;
import br.com.ecc.client.ui.sistema.secretaria.MensagemPresenter;
import br.com.ecc.client.ui.sistema.tesouraria.FichasPresenter;
import br.com.ecc.client.ui.sistema.tesouraria.PagamentosPresenter;
import br.com.ecc.core.mvp.presenter.Presenter;

@SuppressWarnings("rawtypes")
public enum PresenterCodeEnum  {
	//gerais
	HOME(0,1, HomePresenter.class, "Página inicial", false),
	SISTEMA(0,2, InicioSistemaPresenter.class, "Sistema", true),
	MEUS_DADOS(0,3, MeusDadosPresenter.class, "Meus Dados", true),

	GRUPO(2,1, GrupoPresenter.class, "Cadastro de Grupos", true),
	CASAL(2,2, CasalPresenter.class, "Cadastro de Casais", true),

	ENCONTRO(2,3, EncontroPresenter.class, "Cadastro de Encontros", true),
	ATIVIDADE(2,4, AtividadePresenter.class, "Cadastro de Atividades de Encontros", true),
	PAPEL(2,5, PapelPresenter.class, "Cadastro de Papeis", true),
	HOTEL(2,17, HotelPresenter.class, "Cadastro de Hoteis", true),
	RESTAURANTE(2,23, RestaurantePresenter.class, "Cadastro de Restaurantes", true),
	RESTAURANTE_LAYOUTS(2,24, RestauranteLayoutPresenter.class, "Layouts de Restaurantes", true),

	ENCONTRO_CONVITE(2,8, EncontroConvitePresenter.class, "Convites ao encontro", true),
	ENCONTRO_INSCRICAO(2,6, EncontroInscricaoPresenter.class, "Inscrição ao encontro", true),
	ENCONTRO_PLANILHA(2,7, PlanilhaPresenter.class, "Planilha do encontro", true),

	AGRUPAMENTO(2,9, AgrupamentoPresenter.class, "Agrupamentos", true),
	MENSAGEM(2,10, MensagemPresenter.class, "Mensagens", true),
	LISTAGEM(2,14, ListagemPresenter.class, "Listagem de casais", true),
	RESTAURANTE_ENCONTRO(2,25, EncontroHotelPresenter.class, "Restaurantes do Encontro", true),
	RESTAURANTE_DISTRIBUICAO(2,20, DistribuicaoRestaurantePresenter.class, "Distribuição dos Restaurantes", true),

	ITEM_PATRIMONIO(2,12, ItemPatrimonioPresenter.class, "Itens do patrimonio", true),

	TESOURARIA_PAGAMENTOS(2,13, PagamentosPresenter.class, "Pagamentos", true),
	TESOURARIA_FICHAS(2,15, FichasPresenter.class, "Fichas", true),

	HOTEL_ENCONTRO(2,18, EncontroHotelPresenter.class, "Hoteis do Encontro", true),
	HOTEL_DISTRIBUICAO(2,19, DistribuicaoQuartosPresenter.class, "Distribuição dos Quartos", true),

	ORGANOGRAMA_ENCONTRO(2,21, EncontroOrganogramaPresenter.class, "Organogramas do Encontro", true),
	ORGANOGRAMA_DISTRIBUICAO(2,22, DistribuicaoOrganogramaPresenter.class, "Distribuição dos Organogramas", true)
	;


	private Integer parentId;
	private Integer id;
	private String nome;
	private Boolean requireAutentication;
	private Class<? extends Presenter> presenter;

	@SuppressWarnings("unused")
	private Integer codigo;

	private PresenterCodeEnum(Integer parentId, Integer id, Class<? extends Presenter> presenter, String nome, Boolean requireAutentication) {
		this.parentId = parentId;
		this.id = id;
		this.presenter = presenter;
		this.nome = nome;
		this.requireAutentication = requireAutentication;
	}
	public void setPresenter(Class<? extends Presenter> presenter) {
		this.presenter = presenter;
	}
	public Class<? extends Presenter> getPresenter() {
		return presenter;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigo() {
		return parentId * 1000 + id;
	}
	public Boolean getRequireAutentication() {
		return requireAutentication;
	}
	public void setRequireAutentication(Boolean requireAutentication) {
		this.requireAutentication = requireAutentication;
	}

	public static PresenterCodeEnum getPresenterPorCodigo(Integer codigo){
		for (PresenterCodeEnum item : PresenterCodeEnum.values()) {
			if(item.getCodigo().equals(codigo)){
				return item;
			}
		}
		return null;
	}
}