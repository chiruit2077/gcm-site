package br.com.ecc.client.service.encontro;

import java.util.Date;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.vo.EncontroMonitorVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroMonitorServiceAsync {
	void getVO(Encontro encontro, Date date, AsyncCallback<EncontroMonitorVO> callback);
}
