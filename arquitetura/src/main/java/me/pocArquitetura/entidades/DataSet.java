package me.pocArquitetura.entidades;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataSet {
	
	@Inject
	Dicionario dicionario;
	
	private Map<String, Vector<Long>> dataSet = new HashMap<String, Vector<Long>>();
	
	
	public void recebeEstimuloMetodo(String metodo, Duration duration){
		 Vector<Long> instancias = getInstancia(metodo);
		instancias.add(dicionario.getPosicao(metodo), duration.toMillis());
	}
	
	public void recebeEstimuloExcecao(String metodo, String excecao){
		 Vector<Long> instancias = getInstancia(metodo);
		instancias.add(dicionario.getPosicao(metodo+"-"+excecao), -1L);
	}
	
	private Vector<Long> getInstancia(String metodo){
		Vector<Long> instancias = dataSet.get(metodo);
		if(instancias==null){
			dataSet.put(metodo, new Vector<Long>(20) );
			return dataSet.get(metodo);
		}
		return instancias;
		
	}
}	
