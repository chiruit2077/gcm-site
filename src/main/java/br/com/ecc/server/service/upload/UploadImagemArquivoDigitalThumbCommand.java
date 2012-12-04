package br.com.ecc.server.service.upload;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;
import br.com.ecc.server.util.ImageUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class UploadImagemArquivoDigitalThumbCommand implements Callable<Void>{
	
	@Inject EntityManager em;
	@Inject Injector injector;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Void call() throws Exception {
		byte[] dadosArquivo;
		boolean grava;
		SaveEntityCommand cmdSave = injector.getInstance(SaveEntityCommand.class);
		
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setQuery("select o from ArquivoDigital o ");
		List<ArquivoDigital> resultado = cmd.call();
		int i=1;
		for (ArquivoDigital ad : resultado) {
			System.out.println("Processando arquivo digital: " + i++ + " / " + resultado.size());
			grava = false;
			dadosArquivo = ad.getDados();
			if(ad.getTamanho()>300000){
				ad.setDados(ImageUtil.scale(dadosArquivo, ImageUtil.TAMANHO_IMAGEM));
				ad.setTamanho(ad.getDados().length);
				grava = true;
			}
			if(ad.getThumb()==null){
				ad.setThumb(ImageUtil.scale(dadosArquivo, ImageUtil.TAMANHO_THUMB));
				grava = true;
			}
			if(grava){
				cmdSave = injector.getInstance(SaveEntityCommand.class);
				cmdSave.setBaseEntity(ad);
				cmdSave.call();
			}
		}
		return null;
	}
	
}