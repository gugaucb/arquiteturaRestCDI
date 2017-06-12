package me.pocArquitetura.entidades;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class Dicionario {
	Map<String, Integer> listaMetodos = new HashMap<String, Integer>();
	
	
	public Integer getPosicao(String metodo){
		Integer posicao = listaMetodos.get(metodo);
		Integer proximo;
		if(posicao==null){
			if(!listaMetodos.isEmpty()){
				proximo = listaMetodos.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
			}else{
				proximo = 0;
				listaMetodos.put(metodo, proximo);
				
			}
			listaMetodos.put(metodo, ++proximo);
			return proximo;
		}
		return posicao;
	}
	
}

	