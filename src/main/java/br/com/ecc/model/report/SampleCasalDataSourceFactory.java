package br.com.ecc.model.report;

import java.util.Vector;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Pessoa;

public class SampleCasalDataSourceFactory {

	public static  Vector<Casal>  createBeanCollection() {
		Vector<Casal> coll = new Vector<Casal>();

		Casal casal = new Casal();
		casal.setEle(new Pessoa());
		casal.setEla(new Pessoa());
		casal.getEle().setNome("FULANO DE TAL");
		casal.getEla().setNome("FULANA DE TAL");
		casal.getEle().setApelido("FULANO");
		casal.getEla().setApelido("FULANA");
		coll.add(casal);

		casal = new Casal();
		casal.setEle(new Pessoa());
		casal.setEla(new Pessoa());
		casal.getEle().setNome("CICLANO DE TAL");
		casal.getEla().setNome("CICLANA DE TAL");
		casal.getEle().setApelido("CICLANO");
		casal.getEla().setApelido("CICLANA");
		coll.add(casal);

		return coll;
	}
}
