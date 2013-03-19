package br.com.ecc.server.service.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.util.StringUtil;
import br.com.ecc.core.mvp.infra.exception.WebValidationException;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.DadosLoginVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.SessionHelper;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.AutenticaUsuarioCommand;
import br.com.ecc.server.command.EnviaEmailCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;
import br.com.ecc.server.util.CriptoUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
@Transactional
public class AdministracaoServiceImpl extends SecureRemoteServiceServlet implements AdministracaoService {

	private static final long serialVersionUID = -8447158599567399455L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	public DadosLoginVO getDadosLogin() throws Exception {
		DadosLoginVO vo = new DadosLoginVO();

		Usuario usuario;

		//usuario
		HttpServletRequest req = getThreadLocalRequest();
		HttpSession session = req.getSession();
		if(session != null) {
			usuario = SessionHelper.getUsuario(session);

			if(usuario!=null){
				GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
				cmd.setClazz(Usuario.class);
				cmd.setId(usuario.getId());
				usuario = (Usuario) cmd.call();
			}

			vo.setUsuario(usuario);
			vo.setParametrosRedirecionamentoVO(SessionHelper.getParametros(session));
		}

		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		if(vo.getUsuario()!=null){
			cmd.setNamedQuery("casal.porPessoa");
			cmd.addParameter("pessoa", vo.getUsuario().getPessoa());
			cmd.setMaxResults(1);
			List<Casal> lista = cmd.call();
			if(lista.size()!=0){
				vo.setCasal(lista.get(0));
			}
		}
//		cmd = injector.getInstance(GetEntityListCommand.class);
//		cmd.setNamedQuery("grupo.todos");
//		vo.setListaGrupos(cmd.call());
//
//		cmd = injector.getInstance(GetEntityListCommand.class);
//		cmd.setNamedQuery("encontro.todos");
//		vo.setListaEncontros(cmd.call());

		return vo;
	}


	@Override
	public void logout() throws Exception {
		HttpServletRequest req = getThreadLocalRequest();
		HttpSession session = req.getSession();
		SessionHelper.setParametros(session, null);
		SessionHelper.setUsuario(session, null);
		getThreadLocalRequest().getSession(true).invalidate();
	}

	@Override
	public DadosLoginVO login(String email, String senha) throws Exception {
		if(!email.equals("") && !senha.equals("")){
			AutenticaUsuarioCommand cmd = injector.getInstance(AutenticaUsuarioCommand.class);
			cmd.setEmail(email);
			cmd.setSenha(senha);
			Usuario usuario = cmd.call();
			if(usuario!=null){
				registraUsuario(usuario);
				return getDadosLogin();
			}
		}
		throw new WebValidationException("Usuário  ou senha inválido", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean reenviarSenha(String email) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("usuario.porEmail");
		cmd.addParameter("email", email);
		cmd.setMaxResults(1);
		List<Usuario> lista = cmd.call();
		if(lista.size()>0){
			Usuario user = lista.get(0);
			user.setSenha(CriptoUtil.Codifica(StringUtil.randomString(10)));

			SaveEntityCommand cmdSave = injector.getInstance(SaveEntityCommand.class);
			cmdSave.setBaseEntity(user);
			user = (Usuario)cmdSave.call();

			StringBuffer texto = new StringBuffer();
			texto.append("<h3>Prezado usuário</h3><br>");
			texto.append("Seu cadastramento no sistema on-line foi encontrado.<br><br>");
			texto.append("Sua senha de acesso foi redefinida com sucesso.<br><br>");
			texto.append("Usuário: <b>"+user.getEmail()+"</b><br>");
			texto.append("Senha: <b>"+CriptoUtil.Decodifica(user.getSenha()) +"</b><br><br>");
			texto.append("[Altere sua senha dentro do ambiente do sistema]");

			EnviaEmailCommand cmdEmail = injector.getInstance(EnviaEmailCommand.class);
			cmdEmail.setAssunto("ECC - Alteração de senha");
			cmdEmail.setDestinatario(email);
			cmdEmail.setMensagem(new String(texto));
			cmdEmail.call();
			return true;
		}
		throw new WebValidationException("Usuário não encontrado.", null);
	}

	private Usuario registraUsuario(Usuario usuario){
		HttpServletRequest req = getThreadLocalRequest();
		HttpSession session = req.getSession(true);
		SessionHelper.setUsuario(session, usuario);

		return usuario;
	}

	@Override
	@Permissao(nomeOperacao="Salva usuário", operacao=Operacao.SALVAR)
	public Usuario salvaUsuario(Usuario usuario, Boolean senhaAlterada) throws Exception {
		if(senhaAlterada){
			usuario.setSenha(CriptoUtil.Codifica(usuario.getSenha()));
		}
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(usuario.getPessoa());
		usuario.setPessoa((Pessoa) cmd.call());

		cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(usuario);
		usuario = (Usuario)cmd.call();

		registraUsuario(usuario);

		return usuario;
	}

	@Override
	@Permissao(nomeOperacao="Salva usuario logado", operacao=Operacao.SALVAR)
	public Usuario salvaUsuarioLogado(Usuario usuario, Boolean senhaAlterada) throws Exception {
		if(senhaAlterada){
			GetEntityCommand cmdU = injector.getInstance(GetEntityCommand.class);
			cmdU.setClazz(Usuario.class);
			cmdU.setId(usuario.getId());
			Usuario verificando = (Usuario)cmdU.call();
			if(verificando.getSenha().equals(CriptoUtil.Codifica(usuario.getSenha()))){
				usuario.setSenha(CriptoUtil.Codifica(usuario.getSenhaCripto()));
			} else {
				throw new WebValidationException("Senha atual inválida.", null);
			}
		}

		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(usuario.getPessoa());
		usuario.setPessoa((Pessoa) cmd.call());

		cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(usuario);
		usuario = (Usuario)cmd.call();

		registraUsuario(usuario);
		return usuario;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> listaGrupos() throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("grupo.todos");
		return cmd.call();
	}

}