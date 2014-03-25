package br.com.ecc.server.guice;

import br.com.ecc.server.service.cadastro.AgrupamentoServiceImpl;
import br.com.ecc.server.service.cadastro.AtividadeServiceImpl;
import br.com.ecc.server.service.cadastro.CasalServiceImpl;
import br.com.ecc.server.service.cadastro.EncontroServiceImpl;
import br.com.ecc.server.service.cadastro.GrupoServiceImpl;
import br.com.ecc.server.service.cadastro.HotelServiceImpl;
import br.com.ecc.server.service.cadastro.OrganogramaServiceImpl;
import br.com.ecc.server.service.cadastro.PapelServiceImpl;
import br.com.ecc.server.service.cadastro.PessoaServiceImpl;
import br.com.ecc.server.service.cadastro.RestauranteServiceImpl;
import br.com.ecc.server.service.core.AdministracaoServiceImpl;
import br.com.ecc.server.service.core.ArquivoDigitalServiceImpl;
import br.com.ecc.server.service.core.EmailServiceImpl;
import br.com.ecc.server.service.core.EntidadeGenericaServiceImpl;
import br.com.ecc.server.service.core.RecadoServiceImpl;
import br.com.ecc.server.service.core.ResourceReaderServiceImpl;
import br.com.ecc.server.service.encontro.EncontroAtividadeInscricaoServiceImpl;
import br.com.ecc.server.service.encontro.EncontroAtividadeServiceImpl;
import br.com.ecc.server.service.encontro.EncontroConviteResponsavelServiceImpl;
import br.com.ecc.server.service.encontro.EncontroConviteServiceImpl;
import br.com.ecc.server.service.encontro.EncontroFilaServiceImpl;
import br.com.ecc.server.service.encontro.EncontroHotelServiceImpl;
import br.com.ecc.server.service.encontro.EncontroInscricaoFichaPagamentoServiceImpl;
import br.com.ecc.server.service.encontro.EncontroInscricaoServiceImpl;
import br.com.ecc.server.service.encontro.EncontroMonitorServiceImpl;
import br.com.ecc.server.service.encontro.EncontroOrganogramaServiceImpl;
import br.com.ecc.server.service.encontro.EncontroRestauranteServiceImpl;
import br.com.ecc.server.service.patrimonio.ItemPatrimonioServiceImpl;
import br.com.ecc.server.service.redirecionamento.ConfirmacaoServlet;
import br.com.ecc.server.service.redirecionamento.FichaServlet;
import br.com.ecc.server.service.redirecionamento.InscricaoServlet;
import br.com.ecc.server.service.redirecionamento.PlanilhaServlet;
import br.com.ecc.server.service.secretaria.DocumentoServiceImpl;
import br.com.ecc.server.service.secretaria.EncontroRelatoriosSecretariaServiceImpl;
import br.com.ecc.server.service.secretaria.MensagemServiceImpl;
import br.com.ecc.server.service.upload.UploadArquivoServlet;
import br.com.ecc.server.service.upload.UploadProgressServiceImpl;
import br.com.ecc.servlet.DownloadArquivoDigitalServlet;
import br.com.ecc.servlet.DownloadResourceServlet;
import br.com.ecc.servlet.ReportServlet;
import br.com.ecc.servlet.UploadArquivoDigitalServlet;

import com.google.inject.servlet.ServletModule;

public class ECCServletModule extends ServletModule {
	private static final String contextPath = "/eccweb/";

	@Override
	public void configureServlets() {
		this.serve(contextPath + "pessoa").with(PessoaServiceImpl.class);
		this.serve(contextPath + "grupo").with(GrupoServiceImpl.class);
		this.serve(contextPath + "casal").with(CasalServiceImpl.class);
		this.serve(contextPath + "encontro").with(EncontroServiceImpl.class);
		this.serve(contextPath + "atividade").with(AtividadeServiceImpl.class);
		this.serve(contextPath + "papel").with(PapelServiceImpl.class);
		this.serve(contextPath + "agrupamento").with(AgrupamentoServiceImpl.class);
		this.serve(contextPath + "mensagem").with(MensagemServiceImpl.class);
		this.serve(contextPath + "documento").with(DocumentoServiceImpl.class);
		this.serve(contextPath + "itemPatrimonio").with(ItemPatrimonioServiceImpl.class);

		this.serve(contextPath + "hotel").with(HotelServiceImpl.class);
		this.serve(contextPath + "restaurante").with(RestauranteServiceImpl.class);

		this.serve(contextPath + "encontroInscricao").with(EncontroInscricaoServiceImpl.class);
		this.serve(contextPath + "encontroAtividade").with(EncontroAtividadeServiceImpl.class);
		this.serve(contextPath + "encontroAtividadeInscricao").with(EncontroAtividadeInscricaoServiceImpl.class);
		this.serve(contextPath + "encontroInscricaoFichaPagamento").with(EncontroInscricaoFichaPagamentoServiceImpl.class);
		this.serve(contextPath + "encontroHotel").with(EncontroHotelServiceImpl.class);
		this.serve(contextPath + "encontroRestaurante").with(EncontroRestauranteServiceImpl.class);
		this.serve(contextPath + "encontroRelatoriosSecretaria").with(EncontroRelatoriosSecretariaServiceImpl.class);
		this.serve(contextPath + "encontroMonitor").with(EncontroMonitorServiceImpl.class);

		this.serve(contextPath + "encontroConvite").with(EncontroConviteServiceImpl.class);
		this.serve(contextPath + "encontroFila").with(EncontroFilaServiceImpl.class);
		this.serve(contextPath + "encontroConviteResponsavel").with(EncontroConviteResponsavelServiceImpl.class);

		// core
		this.serve(contextPath + "arquivoDigital").with(ArquivoDigitalServiceImpl.class);
		this.serve(contextPath + "autenticacao").with(AdministracaoServiceImpl.class);
		this.serve(contextPath + "recado").with(RecadoServiceImpl.class);
		this.serve(contextPath + "uploadArquivoDigital").with(UploadArquivoDigitalServlet.class);
		this.serve(contextPath + "downloadArquivoDigital").with(DownloadArquivoDigitalServlet.class);
		this.serve(contextPath + "email").with(EmailServiceImpl.class);
		this.serve(contextPath + "readresources").with(ResourceReaderServiceImpl.class);
		this.serve(contextPath + "entidadegenerica").with(EntidadeGenericaServiceImpl.class);

		// servlets
		this.serve(contextPath + "report").with(ReportServlet.class);
		this.serve(contextPath + "dr").with(DownloadResourceServlet.class);
		this.serve(contextPath + "uploadArquivo").with(UploadArquivoServlet.class);
		this.serve(contextPath + "uploadprogress").with(UploadProgressServiceImpl.class);

		// servlets de redirecionamento
		this.serve(contextPath + "ficha").with(FichaServlet.class);
		this.serve(contextPath + "confirmacao").with(ConfirmacaoServlet.class);
		this.serve(contextPath + "inscricao").with(InscricaoServlet.class);
		this.serve(contextPath + "planilha").with(PlanilhaServlet.class);

		// organograma
		this.serve(contextPath + "organograma").with(OrganogramaServiceImpl.class);
		this.serve(contextPath + "encontroOrganograma").with(EncontroOrganogramaServiceImpl.class);
	}
}