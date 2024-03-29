package br.com.ecc.client.util;

import java.util.Random;


public class StringUtil {
	private StringUtil() {}

	private static final String UPPERCASE_ASCII =
		"AEIOU" // grave
		+ "AEIOUY" // acute
		+ "AEIOUY" // circumflex
		+ "AON" // tilde
		+ "AEIOUY" // umlaut
		+ "A" // ring
		+ "C" // cedilla
		+ "OU" // double acute
		;

	private static final String UPPERCASE_UNICODE =
		"\u00C0\u00C8\u00CC\u00D2\u00D9"
		+ "\u00C1\u00C9\u00CD\u00D3\u00DA\u00DD"
		+ "\u00C2\u00CA\u00CE\u00D4\u00DB\u0176"
		+ "\u00C3\u00D5\u00D1"
		+ "\u00C4\u00CB\u00CF\u00D6\u00DC\u0178"
		+ "\u00C5"
		+ "\u00C7"
		+ "\u0150\u0170"
		;

	public static String toUpperCaseSansAccent(String txt) {
		if (txt == null) {
			return null;
		}
		txt = txt.toUpperCase();
		StringBuilder sb = new StringBuilder();
		int n = txt.length();
		for (int i = 0; i < n; i++) {
			char c = txt.charAt(i);
			int pos = UPPERCASE_UNICODE.indexOf(c);
			if (pos > -1){
				sb.append(UPPERCASE_ASCII.charAt(pos));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static String repeate(String string, int quantidade){
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < quantidade; i++) {
			buffer.append(string);
		}
		return buffer.toString();
	}
	
	public static boolean empty(String string) {
		if(string==null){
			return true;
		}
		if(string.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean notEmpty(String string) {
		return !empty(string);
	}
	
	public static boolean notEmpty(Object complemento) {
		if(complemento ==null){
			return true;
		}
		return notEmpty(complemento.toString());
	}
	
	public static Integer string2Minutes(String string, String mask){
		if (string==null){
			string="";
		}
		try{
			int pos = mask.indexOf(":");
			string=string.replaceAll(":", "");
			int horas = Integer.valueOf(string.substring(0, pos));
			int minutos = Integer.valueOf(string.substring(pos));
			return horas*60 + minutos;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String minutes2String(Integer minutes, String mask){
		if (minutes==null){
			minutes = new Integer(0);
		}
		int horas = minutes / 60;
		int minutos = minutes - (horas * 60);
		int qtdeCharsHoras = mask.indexOf(":");
		String horasStr = String.valueOf(horas);
		horasStr = repeate("0", qtdeCharsHoras-horasStr.length()) + horasStr;
		String minutosStr = String.valueOf(minutos);
		if (minutosStr.length()==1){
			minutosStr = "0" + minutosStr;
		}
		return horasStr + ":" + minutosStr;
	}
	
	public static String right(String string, int chars){
		return string.substring(string.length() - chars);
	}
	
	public static Integer isInteger(String token) {
		try{
			return Integer.parseInt(token);
		}catch(NumberFormatException ex){
			return null;
		}
	}

	public static boolean empty(Object complemento) {
		if(complemento ==null){
			return true;
		}
		return empty(complemento.toString());
	}
	
	public static byte[] asByteArray(String string){
		byte[] bytes = new byte[string.length()];
		
		for (int i = 0; i < string.length(); i++) {
			bytes[i] = (byte)string.charAt(i);
		}
		return bytes;
	}


	public static String removeLeadingZeros(String codigo) {
		int count = 0;
		for(int i=codigo.length()-1;i>0;i--) {
			if(codigo.charAt(i) == '0') count++;
			else break;
		}

		return codigo.substring(0, codigo.length()-count);
	}

	
	public static String formatCPF(String cpf){
		if (cpf!=null && cpf.length()==11){
			return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
		}
		return cpf;
	}

	public static String formatCNPJ(String cnpj){
		if (cnpj!=null && cnpj.length()==14){
			return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
		}
		return cnpj;
	}
	
	public static String formatNumberUsingMask(String data, String mask) {
		StringBuilder sb = new StringBuilder();
		char search = '#';
		int i=0;
		int j=0;
		while(i<data.length()) {
			if(mask.charAt(j) != search) {
				sb.append(mask.charAt(j++));
				continue;
			}
			
			sb.append(data.charAt(i++));
			j++;
		}
		
		return sb.toString();
	}

    public static String format(String format, Object ... args) {
//    	return new Formatter().format(format, args).toString();
    	return null;
    }

	public static Integer asInteger(String attribute) {
		if(attribute==null || attribute.trim().equals("")){
			return null;
		}
		try{
			return Integer.parseInt(attribute);
		}catch(Exception e){
			return null;
		}
	}
	
	public static String strPad(String s, char c, int length) {  

		if (s.length() < length) {  
			int n = s.length() - length;  
			StringBuffer buffer = new StringBuffer();  
			for (int i = 0; i < n; i++) {  
				buffer.append(c);  
			}  
			buffer.append(s);  
			s = buffer.toString();  
		}   
		return s;  
	}
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random();
	
	public static String randomString(int len){
		StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
	}
	
	public static boolean isValidEmail(String email){
		return email.matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
	}
	
	public static boolean isCNPJ(String cnpj) {
		if (empty(cnpj)) {
			return false;
		}
		String str_cnpj = numbers(cnpj);
		if (allEqual(str_cnpj)) {
			return false;
		}
		if (str_cnpj.length() != 14) {
			return false;
		}
		int soma = 0, dig;
		String cnpj_calc = str_cnpj.substring(0, 12);

		if (str_cnpj.length() != 14) {
			return false;
		}

		char[] chr_cnpj = str_cnpj.toCharArray();

		/* Primeira parte */
		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}
		dig = 11 - (soma % 11);

		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		/* Segunda parte */
		soma = 0;
		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		return str_cnpj.equals(cnpj_calc);
	}

	
	public static String numbers(String str) {
		return empty(str) ? str : str.replaceAll("[^\\d]", "");
	}
	public static boolean allEqual(String str) {
		if (empty(str)) {
			return true;
		}
		char first = str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch != first) {
				return false;
			}
			first = ch;
		}
		return true;
	}
	public static boolean isCPF(String strCpf) {
		if (empty(strCpf)) {
			return false;
		}
		strCpf = numbers(strCpf);
		strCpf = strCpf.substring(Math.max(0, strCpf.length() - 11),
				strCpf.length());

		if (allEqual(strCpf)) {
			return false;
		}
		if (!strCpf.substring(0, 1).equals("")) {
			try {
				int d1, d2;
				int digito1, digito2, resto;
				int digitoCPF;
				String nDigResult;
				d1 = d2 = 0;
				digito1 = digito2 = resto = 0;

				for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
					digitoCPF = Integer.valueOf(
							strCpf.substring(nCount - 1, nCount)).intValue();

					// multiplique a ultima casa por 2 a seguinte por 3 a
					// seguinte por 4 e assim por diante.
					d1 = d1 + (11 - nCount) * digitoCPF;

					// para o segundo digito repita o procedimento incluindo o
					// primeiro digito calculado no passo anterior.
					d2 = d2 + (12 - nCount) * digitoCPF;
				}
				;

				// Primeiro resto da divisao por 11.
				resto = (d1 % 11);

				// Se o resultado for 0 ou 1 o digito � 0 caso contrario o
				// digito � 11 menos o resultado anterior.
				if (resto < 2) {
					digito1 = 0;
				} else {
					digito1 = 11 - resto;
				}

				d2 += 2 * digito1;

				// Segundo resto da divis�o por 11.
				resto = (d2 % 11);

				// Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o
				// digito � 11 menos o resultado anterior.
				if (resto < 2) {
					digito2 = 0;
				} else {
					digito2 = 11 - resto;
				}

				// Digito verificador do CPF que est� sendo validado.
				String nDigVerific = strCpf.substring(strCpf.length() - 2,
						strCpf.length());

				// Concatenando o primeiro resto com o segundo.
				nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

				// comparar o digito verificador do cpf com o primeiro resto + o
				// segundo resto.
				return nDigVerific.equals(nDigResult);
			} catch (Exception e) {
				System.err.println("Erro !" + e);
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isDocValido(String doc) {
		if(doc == null || "".equals(doc.trim())) {
			return false;
		}
		return isCNPJ(doc) || isCPF(doc);
	}
}