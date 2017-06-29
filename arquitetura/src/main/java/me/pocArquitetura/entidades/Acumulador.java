package me.pocArquitetura.entidades;

import javax.inject.Singleton;


@Singleton
public class Acumulador {
	
	private int i = 0;
	
	public int soma(){
		return i++;
	}
	
	public int subtrai(){
		return i--;
	}
	
	public int getQuantidade(){
		return i;
	}
}
