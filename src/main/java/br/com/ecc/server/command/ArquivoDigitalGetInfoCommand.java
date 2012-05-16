package br.com.ecc.server.command;

import java.util.List;

import javax.persistence.Query;

import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.tipo.TipoArquivoEnum;
import br.com.ecc.server.command.basico.ECCBaseCommand;

public class ArquivoDigitalGetInfoCommand extends ECCBaseCommand<ArquivoDigital> {

	private static final long serialVersionUID = -3149869378541933645L;

	private Integer id;
	
	@Override
	public ArquivoDigital call() throws Exception {

		ArquivoDigital arquivoDigital = null;
		try{
			Query query = getEntityManager().createQuery("select o.id, o.nomeArquivo, o.mimeType, o.tamanho, o.descricao, o.tipo from ArquivoDigital o where o.id = " + id);
			
			@SuppressWarnings("unchecked")
			List<Object[]> resultado = query.getResultList();
			for (Object[] objects : resultado) {
				arquivoDigital = new ArquivoDigital();
				arquivoDigital.setId((Integer) objects[0]);
				arquivoDigital.setNomeArquivo((String) objects[1]);
				arquivoDigital.setMimeType((String) objects[2]);
				arquivoDigital.setTamanho((Integer) objects[3]);
				arquivoDigital.setDescricao((String) objects[4]);
				arquivoDigital.setTipo((TipoArquivoEnum) objects[5]);
			}
		}catch (Exception e) {
			arquivoDigital = null;
			System.out.println(e.getMessage());
		}
		return arquivoDigital;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}