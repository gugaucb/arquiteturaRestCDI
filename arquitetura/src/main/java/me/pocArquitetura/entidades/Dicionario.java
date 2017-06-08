package me.pocArquitetura.entidades;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class Dicionario {
	Map<String, Integer> listaMetodos = new HashMap<String, Integer>();
	
	
	public Integer getPosicao(String metodo){
		Integer posicao = listaMetodos.get(metodo);
		if(posicao==null){
			Integer proximo = listaMetodos.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
			listaMetodos.put(metodo, ++proximo);
			return proximo;
		}
		return posicao;
	}
	
}
