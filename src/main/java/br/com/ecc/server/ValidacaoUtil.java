package br.com.ecc.server;

import java.util.List;

import br.com.ecc.core.mvp.infra.exception.WebMessage;

public class ValidacaoUtil {

	public static void validaEmpty(Object valor, String key, String nomeAtributo, List<WebMessage> erros){
		if(valor==null){
			String msg =null;
			if(nomeAtributo!=null){
				msg = "O campo '"+nomeAtributo+"' não pode ser nulo ou vazio";
			}else{
				msg = "O campo não pode ser nulo ou vazio";
			}
			erros.add(new WebMessage(key,msg));
		}
	}
	public static void validaDocumento(String valor, String key, String nomeAtributo, List<WebMessage> erros){
		String documento = valor.replaceAll("\\D", "");
		if(!isDocumento(documento)){
			String msg =null;
			if(nomeAtributo!=null){
				msg = "O campo '"+nomeAtributo+"' não é um CPF ou CNPJ válido";
			}else{
				msg = "O campo documento não é um CPF ou CNPJ válido";
			}
			erros.add(new WebMessage(key,msg));
		}
		
	}
	
	public static boolean isDocumento(String documento){
		if(documento!=null){
			//Verifica se todos os digitos do documento são iguais
			if(documento.matches("(.)(\\1)*")){
				return false;
			}
			return isValidCPF(documento) || isValidCNPJ(documento);
		}
		return false;
	}
	
	
	private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	private static int calcularDigito(String str, int[] peso) {
	int soma = 0;
	for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
		digito = Integer.parseInt(str.substring(indice,indice+1));
		soma += digito*peso[peso.length-str.length()+indice];
	}
	soma = 11 - soma % 11;
	return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCPF(String cpf) {
		if ((cpf==null) || (cpf.length()!=11)) {
			return false;
		}
		Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
	}

	public static boolean isValidCNPJ(String cnpj) {
		if ((cnpj==null)||(cnpj.length()!=14)) {
			return false;
		}
		Integer digito1 = calcularDigito(cnpj.substring(0,12), pesoCNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0,12) + digito1, pesoCNPJ);
		return cnpj.equals(cnpj.substring(0,12) + digito1.toString() + digito2.toString());
	}
}
