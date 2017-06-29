package me.pocArquitetura.intercepts;

import java.time.Duration;
import java.time.Instant;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import me.pocArquitetura.annotations.ExcecaoEvent;
import me.pocArquitetura.annotations.MetodoEvent;
import me.pocArquitetura.annotations.Monitoring;
import me.pocArquitetura.entidades.DataSet;
import me.pocArquitetura.entidades.Dicionario;
import me.pocArquitetura.entidades.Instancia;

/**
 * Inteceptor aplicado aos metodos que possuem a annotation Monitoring. 
 * O objetivo é capturar as excecoes e tempo de execução do metodo para
 * identificar possiveis erros.
 * @author gugaucb
 *
 */

@Monitoring
@Interceptor
@Named
public class MonitoringIntercept {
	
	
	@Inject
	@ExcecaoEvent
    private Event<Throwable> excecaoMessageEvent;
	@Inject
	@MetodoEvent
	private Event<String> metodoMessageEvent;
	
	@Inject
	private Event<Instancia> salvarInstanciaMessageEvent;
	
	@Inject
	Dicionario dicionario;
	
	@AroundInvoke
	public Object around(InvocationContext jp) throws Throwable {
		Instancia instancia = new Instancia();
		instancia.setDicionario(dicionario);
		String metodo = jp.getMethod().getName();
		Instant start = Instant.now();
		try {
			return jp.proceed();
		}catch(Throwable t){
			instancia = DataSet.recebeEstimuloExcecaoV1(instancia, t.toString());
			//excecaoMessageEvent.fire(t);
			throw t;
		
		} finally {
			Instant end = Instant.now();
			instancia = DataSet.recebeEstimuloMetodoV1(instancia, metodo, Duration.between(start, end));
			//metodoMessageEvent.fire(metodo +";"+ Duration.between(start, end).toMillis());
			salvarInstanciaMessageEvent.fire(instancia);
		}

	}

}
