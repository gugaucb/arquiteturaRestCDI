package me.pocArquitetura.entidades;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.pocArquitetura.util.EstatisticaAmbiente;

@Singleton
public class DataSet {
	
	private static final int QUANT_LIMITE_METODOS = 100;

	@Inject
	Dicionario dicionario;
	
	//private List<Long> instancias = new ArrayList<Long>(QUANT_LIMITE_METODOS);
	private Instancia instancias = new Instancia(QUANT_LIMITE_METODOS);
	
	public DataSet(){
		super();
		initVector();
	}
	
	public void recebeEstimuloMetodo(String metodo, Duration duration){
		Integer posicao = dicionario.getPosicao(metodo);
		instancias.add(posicao, duration.toMillis());
		incluirCondicoesAmbientes(metodo);
		imprimeInstancias();
		dicionario.imprimir();
	}
	
	public void recebeEstimuloExcecao(String metodo, String excecao){
		Integer posicao = dicionario.getPosicao(metodo+"-"+excecao);
		instancias.add(posicao, -1L);
		incluirCondicoesAmbientes(metodo);
		imprimeInstancias();
		dicionario.imprimir();
	}
	
	private void receberEstimuloAmbiente(String metodoOrigem, String metodo, Long valor){ 
		 Integer posicao = dicionario.getPosicao(metodo);
		instancias.add(posicao, valor);
	}
	
	
	private void initVector(){
		/*for (int i = 0; i < QUANT_LIMITE_METODOS; i++) {
			instancias.add(0L);
		}*/
	}
	
	private void incluirCondicoesAmbientes(String metodoOrigem){
		receberEstimuloAmbiente(metodoOrigem, "AvailableProcessors", EstatisticaAmbiente.getAvailableProcessors()+0L);
		receberEstimuloAmbiente(metodoOrigem, "ElapsedTime", EstatisticaAmbiente.getElapsedTime()+0L);
		receberEstimuloAmbiente(metodoOrigem, "SystemLoadAverage", Long.parseLong(String.format("%.0f",EstatisticaAmbiente.getSystemLoadAverage())));
	}
	
	public void imprimeInstancias(){
		instancias.imprimir();
	}
	
}	
