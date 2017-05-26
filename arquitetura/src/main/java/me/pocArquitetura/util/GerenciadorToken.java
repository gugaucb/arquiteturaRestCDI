package me.pocArquitetura.util;



import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.pocArquitetura.constantes.Constantes;

/**
 * Classe de gerenciamento de tokens: gera��o, valida��o e recupera��o de dados
 * @author wallace
 * @since 14-05-2017
 *
 */
public class GerenciadorToken {
	
	private static SignatureAlgorithm algoritmoAssinatura = SignatureAlgorithm.HS256;
	
	private String fraseSecreta;
	
	private String[] dadosDoUsuario;
	
	private int tempoExpiracao;

	
	/**
	 * 
	 * @return nova instancia de {@link GerenciadorToken}
	 */
	public static GerenciadorToken novaInstancia(){
		return new GerenciadorToken();
	}
	
	/**
	 * Configura a frase secreta
	 * @param fraseSecreta
	 * @return
	 */
	public GerenciadorToken useEssaFraseSecreta(String fraseSecreta) {
		this.fraseSecreta = fraseSecreta;
		return this;
	}

	/**
	 * Configura os dados do usuario
	 * @param dadosDoUsuario
	 * @return
	 */
	public GerenciadorToken comEssesDados(String[] dadosDoUsuario) {
		this.dadosDoUsuario = dadosDoUsuario;
		return this;
	}
	
	/**
	 * @param tempoExpiracaoTokenMinutos
	 * @return
	 */
	public GerenciadorToken expireDaquiA(int tempoExpiracaoTokenMinutos) {
		this.tempoExpiracao = tempoExpiracaoTokenMinutos;
		return this;
	}
	

	public String gereToken() throws IllegalArgumentException { 
		
		validarAtributos();
		
		return montarToken(LocalDateTime.now().plusMinutes(tempoExpiracao), criptografar(dadosDoUsuario.toString()));
	}

	private void validarAtributos() {
		validarFraseSecreta();
		
		if (this.tempoExpiracao == 0){
			throw new IllegalArgumentException("Tempo de expira��o � obrigat�rio!");
		}
		
		if (this.dadosDoUsuario == null || this.dadosDoUsuario.length == 0 ){
			throw new IllegalArgumentException("Dados do usu�rio s�o obrigat�rios!");
		}
	}

	private void validarFraseSecreta() {
		if (this.fraseSecreta == null || this.fraseSecreta.isEmpty()){
			throw new IllegalArgumentException("Frase secreta � obrigat�ria!");
		}
	}

	private String montarToken(LocalDateTime dataExpiracao, byte[] dadosCriptografados) {
		System.out.println(dataExpiracao);
		JwtBuilder builder = Jwts.builder()
						.setIssuedAt(DateUtil.asDate(LocalDate.now()))
						.setSubject(new String(dadosCriptografados))
						.setExpiration(DateUtil.asDate( dataExpiracao ) )
						.signWith(algoritmoAssinatura, getSigningKey());
		
		return builder.compact();
	}

	private static byte[] criptografar(String fraseSecreta) {
		return Base64.getEncoder().encode(fraseSecreta.getBytes());
	}
	
	public Boolean esseTokenEValido(String token) throws TokenInvalidoException {
		validarFraseSecreta();
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(getSigningKey())
					.parseClaimsJws(token)
					.getBody();
			return true;
		} catch(Exception e){		
			throw new TokenInvalidoException("Token Inv�lido");	
		}
	}
	
	
	public String devolvaMeOsDadosDoUsuario(String token) throws Exception {
		validarFraseSecreta();
		try {
			
			Claims claims = Jwts.parser()
					.setSigningKey(getSigningKey())
					.parseClaimsJws(token).getBody();
			return claims.toString(); // new String( Base64.getDecoder().decode(claims.getSubject()) );
		} catch(Exception e){
			throw e;
		}
	}

	private Key getSigningKey() {
		return new SecretKeySpec(this.fraseSecreta.getBytes(), GerenciadorToken.algoritmoAssinatura.getJcaName());
	}

	public static void main(String[] args) throws Exception {
		String token = GerenciadorToken.novaInstancia()
				.useEssaFraseSecreta(Constantes.FRASE_SECRETA)
				.comEssesDados(new String[]{"45454", "Wallace"})
				.expireDaquiA(Constantes.TEMPO_EXPIRACAO_TOKEN_MINUTOS)
				.gereToken();
		//System.out.println(token);
	
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTQ3MzA4MDAsInN1YiI6IlcweHFZWFpoTG14aGJtY3VVM1J5YVc1bk8wQTNNamt4WXpFNFpnPT0iLCJleHAiOjE0OTQ3NzM4NDJ9.IbZlOSeW6JEQjM057YtcvdphOrgARa_dvS76W6ZH_t4";
		
		//Boolean tokenValido = GerenciadorToken.isTokenValido(token, Constantes.FRASE_SECRETA);
		
		System.out.println(GerenciadorToken.novaInstancia().useEssaFraseSecreta(Constantes.FRASE_SECRETA).devolvaMeOsDadosDoUsuario(token));
		
		//System.out.println(tokenValido);
		
	}
	
}
