package br.com.ecc.server;

import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.ParametrosRedirecionamentoVO;

public class SessionHelper {

	private static final String USUARIO = "usuario";
	private static final String PARAMETROS = "parametros";
	private static final String UPLOAD_FILE = "uploadFile";

	public static Usuario getUsuario(HttpSession session){
		return (Usuario) session.getAttribute(USUARIO);
	}
	public static void setUsuario(HttpSession session, Usuario user){
		session.setAttribute(USUARIO, user);
	}
	@SuppressWarnings("unchecked")
	public static List<UploadedFile> getUploadFile(HttpSession session){
		return (List<UploadedFile>) session.getAttribute(UPLOAD_FILE);
	}
	public static void setUploadFile(HttpSession session, List<UploadedFile> uploadFile){
		session.setAttribute(UPLOAD_FILE, uploadFile);
	}
	public static ParametrosRedirecionamentoVO getParametros(HttpSession session){
		return (ParametrosRedirecionamentoVO) session.getAttribute(PARAMETROS);
	}
	public static void setParametros(HttpSession session, ParametrosRedirecionamentoVO parametros){
		session.setAttribute(PARAMETROS, parametros);
	}
}