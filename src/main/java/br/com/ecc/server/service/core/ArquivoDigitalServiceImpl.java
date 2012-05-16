package br.com.ecc.server.service.core;

import java.util.Arrays;

import javax.persistence.EntityManager;

import br.com.ecc.client.service.ArquivoDigitalService;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.ArquivoDigitalGetInfoCommand;
import br.com.ecc.server.command.basico.DeleteEntitiesCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ArquivoDigitalServiceImpl extends SecureRemoteServiceServlet implements ArquivoDigitalService {

	private static final long serialVersionUID = 8044505760916856669L;
	@Inject Injector injector;
	@Inject
	Provider<EntityManager> em;
	
	@Override
	public void removeArquivosDigitais(Integer... ids) throws Exception {
		DeleteEntitiesCommand deleteCmd = injector.getInstance(DeleteEntitiesCommand.class);
		deleteCmd.setClazz(ArquivoDigital.class);
		deleteCmd.setIds(Arrays.asList(ids));
		deleteCmd.call();
	}

	@Override
	public ArquivoDigital getInfo(Integer id) throws Exception {
		ArquivoDigitalGetInfoCommand getInfoCmd = injector.getInstance(ArquivoDigitalGetInfoCommand.class);
		getInfoCmd.setId(id);
		return getInfoCmd.call();
	}
	
}
