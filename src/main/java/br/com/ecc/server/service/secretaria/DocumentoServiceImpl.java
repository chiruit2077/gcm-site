package br.com.ecc.server.service.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.service.secretaria.DocumentoService;
import br.com.ecc.model.Documento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoDocumentoEnum;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DocumentoServiceImpl extends SecureRemoteServiceServlet implements DocumentoService {
	private static final long serialVersionUID = -4960615036506533172L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar documentos", operacao=Operacao.VISUALIZAR)
	public List<Documento> lista(Grupo grupo, Encontro encontro) throws Exception {
		List<Documento> lista = new ArrayList<Documento>();
		
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		if(encontro!=null){
			cmd.setNamedQuery("documento.listarPorGrupoEncontro");
			cmd.addParameter("encontro", encontro);
		} else {
			cmd.setNamedQuery("documento.listarPorGrupo");
		}
		cmd.addParameter("grupo", grupo);
		List<Object[]> resultado = cmd.call();
		
		Documento documento;
		for (Object[] o : resultado) {
			documento = new Documento();
			// u.id, u.titulo, u.data, u.tipoDocumento, u.encontro
			documento.setId((Integer)o[0]);
			documento.setTitulo((String)o[1]);
			documento.setData((Date)o[2]);
			documento.setTipoDocumento((TipoDocumentoEnum)o[3]);
			if(encontro!=null){
				documento.setEncontro((Encontro)o[4]);
			}
			lista.add(documento);
		}
		
		return lista;
		
	}

	@Override
	@Permissao(nomeOperacao="Excluir documento", operacao=Operacao.EXCLUIR)
	public void exclui(Documento documento) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(documento);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar documento", operacao=Operacao.SALVAR)
	public Documento salva(Documento documento) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(documento);
		return (Documento) cmd.call();
	}
	
	@Override
	public Documento getDocumento(Integer id) throws Exception {
		GetEntityCommand cmdE = injector.getInstance(GetEntityCommand.class);
		cmdE.setClazz(Documento.class);
		cmdE.setId(id);
		return (Documento) cmdE.call();
	}
}