package br.com.ecc.servlet;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.server.command.ArquivoDigitalSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.service.core.multipart.FileRequest;
import br.com.ecc.server.service.core.multipart.MultipartRequest;
import br.com.ecc.server.util.ImageUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class UploadArquivoDigitalServlet extends BaseUploadServlet {

	private static final long serialVersionUID = 800508960523259729L;
	@Inject Injector injector;
	
	@Override
	@Transactional
	public String doPost(MultipartRequest multipartRequest) throws Exception {
		ArquivoDigital arquivoDigital = null;
		FileRequest file = multipartRequest.getFile("uploadItem");
		String descricao = multipartRequest.getParam("textItem");
		String isDecorate = multipartRequest.getParam("decorateOutputItem");
		String arquivoIdParam = multipartRequest.getParam("hiddenIdItem");
		if (file != null && descricao != null) {
			try {
				byte[] dadosArquivo = file.getContent();
				if(arquivoIdParam != null && !"".equals(arquivoIdParam.trim())) {
					GetEntityCommand getCommand = injector.getInstance(GetEntityCommand.class);
					getCommand.setClazz(ArquivoDigital.class);
					getCommand.setId(new Integer(arquivoIdParam));
					arquivoDigital = (ArquivoDigital) getCommand.call();
				} else {
					arquivoDigital = new ArquivoDigital();
				}
				arquivoDigital.setMimeType(file.getContentType());
				arquivoDigital.setTamanho(file.getContent().length);
				arquivoDigital.setDados(dadosArquivo);
				if(file.getContentType().toUpperCase().contains("IMAGE")){
					arquivoDigital.setThumb(ImageUtil.scale(dadosArquivo,ImageUtil.TAMANHO_THUMB));
				} else {
					arquivoDigital.setThumb(null);
				}
				arquivoDigital.setNomeArquivo(file.getFileName());
				arquivoDigital.setDescricao(descricao);
				if (isDecorate != null && !"".equals(isDecorate)){
					if (isDecorate.toLowerCase().equals("false")){
						decorateOutput = false;
					}
				}
				

				ArquivoDigitalSalvarCommand salvarCmd = injector.getInstance(ArquivoDigitalSalvarCommand.class);
				salvarCmd.setArquivoDigital(arquivoDigital);

				arquivoDigital = salvarCmd.call();
			} catch (Exception e) {
				throw new WebException("Erro ao salvar o arquivo digital: " + e.getMessage(), e);
			}
		} else {
			throw new WebException("Os atributos uploadItem e textItem não podem ser nulos! uploadItem="+file+", textItem="+descricao);
		}
		return arquivoDigital.getId().toString();
	}
	
}
