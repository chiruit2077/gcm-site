package br.com.ecc.client.core.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface QuartoHtmlResource extends ClientBundle {

		 public static QuartoHtmlResource INSTANCE = GWT.create(QuartoHtmlResource.class);

		 @Source("modelodistribuicao1.html")
		 public TextResource getModeloDistribuicao1();

}
