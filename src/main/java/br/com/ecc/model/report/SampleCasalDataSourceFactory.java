package br.com.ecc.model.report;

import java.util.Vector;

import br.com.ecc.model.vo.PlanilhaEncontroInscricaoVO;

public class SampleCasalDataSourceFactory {

	public static  Vector<PlanilhaEncontroInscricaoVO>  createBeanCollection() {
		Vector<PlanilhaEncontroInscricaoVO> coll = new Vector<PlanilhaEncontroInscricaoVO>();

		PlanilhaEncontroInscricaoVO casal = new PlanilhaEncontroInscricaoVO();
		/*casal.setEle("FULANO DE TAL");
		casal.setEla("FULANA DE TAL");
		casal.setTipo("FULANO");
		casal.setPapel("FULANA");
		casal.setQtde(1);*/
		coll.add(casal);

		return coll;
	}
}
