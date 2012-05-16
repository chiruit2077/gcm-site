package br.com.ecc.client.service;

import br.com.ecc.model.ArquivoDigital;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("arquivoDigital")
public interface ArquivoDigitalService  extends RemoteService {
	public void removeArquivosDigitais(Integer... ids) throws Exception;
	public ArquivoDigital getInfo(Integer id) throws Exception;
}
