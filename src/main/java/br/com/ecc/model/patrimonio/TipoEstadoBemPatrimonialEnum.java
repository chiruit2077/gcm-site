package br.com.ecc.model.patrimonio;

public enum TipoEstadoBemPatrimonialEnum {
		NOVO("Novo",0),
		USADO("Usado",1);

		private String nome;
		private Integer codigo;

		private TipoEstadoBemPatrimonialEnum(String nome, Integer codigo) {
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
