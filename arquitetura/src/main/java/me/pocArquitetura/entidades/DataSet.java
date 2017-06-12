package me.pocArquitetura.entidades;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.pocArquitetura.util.EstatisticaAmbiente;

@Singleton
public class DataSet {
	
	@Inject
	Dicionario dicionario;
	
	private Map<String, Vector<Long>> dataSet = new HashMap<String, Vector<Long>>();
	
	
	public void recebeEstimuloMetodo(String metodo, Duration duration){
		 Vector<Long> instancias = getInstancia(metodo);
		instancias.add(dicionario.getPosicao(metodo), duration.toMillis());
		incluirCondicoesAmbientes(metodo, instancias);
		imprimeInstancias();
	}
	
	public void recebeEstimuloExcecao(String metodo, String excecao){
		 Vector<Long> instancias = getInstancia(metodo);
		instancias.add(dicionario.getPosicao(metodo+"-"+excecao), -1L);
		incluirCondicoesAmbientes(metodo, instancias);
		imprimeInstancias();
		
		
	}
	
	private void receberEstimuloAmbiente(String metodoOrigem, String metodo, Long valor){ 
		 Vector<Long> instancias = getInstancia(metodoOrigem);
		 instancias.add(dicionario.getPosicao(metodo), valor);
	}
	
	private Vector<Long> getInstancia(String metodo){
		Vector<Long> instancias = dataSet.get(metodo);
		if(instancias==null){
			instancias = new Vector<Long>(20);
			initVector(instancias);
			dataSet.put(metodo, instancias );
			return dataSet.get(metodo);
		}
		return instancias;
		
	}
	
	private void initVector(Vector<Long> instancias){
		for (int i = 0; i < instancias.capacity(); i++) {
			instancias.addElement(0L);
		}
	}
	
	private void incluirCondicoesAmbientes(String metodoOrigem, Vector<Long> instancias){
		receberEstimuloAmbiente(metodoOrigem, "AvailableProcessors", EstatisticaAmbiente.getAvailableProcessors()+0L);
		receberEstimuloAmbiente(metodoOrigem, "ElapsedTime", EstatisticaAmbiente.getElapsedTime()+0L);
		receberEstimuloAmbiente(metodoOrigem, "SystemLoadAverage", Long.parseLong(String.format("%.0f",EstatisticaAmbiente.getSystemLoadAverage())));
	}
	
	public void imprimeInstancias(){
		for (Map.Entry<String, Vector<Long>> entry : dataSet.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}
	}
	
}	
