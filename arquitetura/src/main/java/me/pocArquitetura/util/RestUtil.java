package me.pocArquitetura.util;

import javax.ws.rs.core.CacheControl;

public class RestUtil {
	public static CacheControl defineCacheDoRecurso(int segundos) {

		CacheControl cc = new CacheControl();
		cc.setMaxAge(60); // Define a idade mÃ­nima da resposta json no cache
							// (nesse caso 60 segundos)
		cc.setPrivate(true);
		cc.setNoCache(false);
		cc.setSMaxAge(60);
		return cc;
	}
}
