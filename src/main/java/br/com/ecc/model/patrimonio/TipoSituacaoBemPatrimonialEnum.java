package br.com.ecc.model.patrimonio;

public enum TipoSituacaoBemPatrimonialEnum {
		ATIVO("Ativo", 0),
		BAIXADO("Baixado",1),
		MANUTENCAO("Em Manutenção",2),
		NAO_LOCALIZADO("Não Localizado",3),
		CEDIDO("Cedido",4);

		private String nome;

		private Integer codigo;

		private TipoSituacaoBemPatrimonialEnum(String nome, Integer codigo) {
			this.setNome(nome);
			this.setCodigo(codigo);
		}

		@Override
		public String toString() {
			if (getNome()!=null){
				return getNome();
			}
			return super.toString();
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

}
