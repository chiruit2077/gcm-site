package br.com.ecc.client.service.encontro;

import java.util.Date;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.vo.EncontroMonitorVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroMonitor")
public interface EncontroMonitorService extends RemoteService {
	public EncontroMonitorVO getVO(Encontro encontro, Date referencia) throws Exception;
}