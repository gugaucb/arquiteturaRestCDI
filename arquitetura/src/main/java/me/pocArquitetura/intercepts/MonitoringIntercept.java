package me.pocArquitetura.intercepts;

import java.time.Duration;
import java.time.Instant;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import me.pocArquitetura.annotations.Monitoring;
import me.pocArquitetura.negocio.MonitoringBean;

/**
 * Inteceptor aplicado aos metodos que possuem a annotation Monitoring. 
 * O objetivo é capturar as excecoes e tempo de execução do metodo para
 * identificar possiveis erros.
 * @author gugaucb
 *
 */

@Monitoring
@Interceptor
public class MonitoringIntercept {
	
	@Inject
	MonitoringBean monitoringBean;
	
	@AroundInvoke
	public Object around(InvocationContext jp) throws Throwable {
		String metodo = jp.getMethod().getName();
		Instant start = Instant.now();
		try {
			return jp.proceed();
		}catch(Throwable t){
			monitoringBean.recebeEstimuloExcecao(metodo, t);
			throw t;
		
		} finally {
			Instant end = Instant.now();
			monitoringBean.recebeEstimuloMetodo(metodo, Duration.between(start, end));
		}

	}

}
