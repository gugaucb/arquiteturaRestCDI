package me.pocArquitetura.negocio;

import java.time.Duration;

import javax.inject.Inject;

import me.pocArquitetura.entidades.DataSet;
import me.pocArquitetura.util.TrapSenderVersion2;

public class MonitoringBean implements Bean {
	@Inject
	DataSet dataSet;
	
	@Inject
	TrapSenderVersion2 trapSenderVersion2;
	
	
	
	public void recebeEstimuloExcecao(String metodo, Throwable t){
		dataSet.recebeEstimuloExcecao(metodo, t.toString());
		trapSenderVersion2.sendTrap_Version2(t, metodo);
		
	}
	
	public void recebeEstimuloMetodo(String metodo, Duration duration){
		dataSet.recebeEstimuloMetodo(metodo, duration);
	}
	
}
