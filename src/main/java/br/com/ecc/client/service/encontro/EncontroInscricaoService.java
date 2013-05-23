package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroInscricao")
public interface EncontroInscricaoService extends RemoteService {
	public List<EncontroInscricao> lista(Encontro encontro) throws Exception;
	public List<EncontroInscricaoVO> listaVO(Encontro encontro) throws Exception;
	public void exclui(EncontroInscricao encontroInscricao) throws Exception;
	public EncontroInscricaoVO salva(EncontroInscricaoVO encontroInscricaoVO) throws Exception;
	public EncontroInscricaoVO getVO(EncontroInscricao encontroInscricao) throws Exception;
	public EncontroInscricaoVO getVO(Encontro encontro, Casal casal) throws Exception;
	public List<EncontroInscricaoPagamento> listaPagamentos(Encontro encontroSelecionado) throws Exception;
	public void salvaPagamento(EncontroInscricaoPagamento pagamento) throws Exception;
}