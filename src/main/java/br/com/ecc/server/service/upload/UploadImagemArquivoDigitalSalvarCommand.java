package br.com.ecc.server.service.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.server.command.ArquivoDigitalSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.util.ImageUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class UploadImagemArquivoDigitalSalvarCommand implements Callable<Integer>{
	
	@Inject EntityManager em;
	@Inject Injector injector;
	
	private UploadedFile uploadedFile;
	private Boolean resize = false;

	@Override
	@Transactional
	public Integer call() throws Exception {
		File file = new File(uploadedFile.getCaminho() + uploadedFile.getNomeArquivo());
		byte[] dadosArquivo = read(file);
		if(resize){
			dadosArquivo = ImageUtil.scale(dadosArquivo, ImageUtil.TAMANHO_IMAGEM);
		}
		
		ArquivoDigital arquivoDigital;
		if(uploadedFile.getIdArquivoDigital()==null){
			arquivoDigital = new ArquivoDigital();
		} else {
			GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
			cmd.setClazz(ArquivoDigital.class);
			cmd.setId(uploadedFile.getIdArquivoDigital());
			arquivoDigital = (ArquivoDigital) cmd.call();
			if(arquivoDigital==null){
				arquivoDigital = new ArquivoDigital();
			}
		}
		arquivoDigital.setMimeType(uploadedFile.getTipo());
		arquivoDigital.setDados(dadosArquivo);
		if(arquivoDigital.getMimeType().toUpperCase().contains("IMAGE")){
			arquivoDigital.setThumb(ImageUtil.scale(dadosArquivo, ImageUtil.TAMANHO_THUMB));
		} else {
			arquivoDigital.setThumb(null);
		}
		arquivoDigital.setNomeArquivo(uploadedFile.getNomeArquivo());
		arquivoDigital.setDescricao(uploadedFile.getNomeArquivo());

		ArquivoDigitalSalvarCommand salvarCmd = injector.getInstance(ArquivoDigitalSalvarCommand.class);
		salvarCmd.setArquivoDigital(arquivoDigital);
		arquivoDigital = salvarCmd.call();
		
		File dir = file.getParentFile();
		file.delete();
		dir.delete();
		
		return arquivoDigital.getId();
	}
	
	
	
	public byte[] read(File file) throws IOException {
		ByteArrayOutputStream ous = null;
        InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ( (read = ios.read(buffer)) != -1 ) {
	            ous.write(buffer, 0, read);
	        }
	    } finally { 
	        try {
	             if( ous != null ){
	            	 ous.close();
	             }
	        } catch ( IOException e) {}

	        try {
	             if ( ios != null ){
	            	 ios.close();
	             }
	        } catch ( IOException e) {}
	    }
	    return ous.toByteArray();
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public Boolean getResize() {
		return resize;
	}
	public void setResize(Boolean resize) {
		this.resize = resize;
	}
}