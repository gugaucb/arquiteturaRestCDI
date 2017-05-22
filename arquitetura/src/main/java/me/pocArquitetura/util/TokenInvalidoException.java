package me.pocArquitetura.util;

/**
 * Classe a ser usada na invalidação do token
 * @author wallace
 *
 */
public class TokenInvalidoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TokenInvalidoException() {
		super();
	}
	
	public TokenInvalidoException(String mensagem){
		super(mensagem);
	}

}
