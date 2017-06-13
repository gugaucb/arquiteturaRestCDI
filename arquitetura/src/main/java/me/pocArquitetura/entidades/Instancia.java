package me.pocArquitetura.entidades;

public class Instancia {
	private Long[] instancias;
	
	public Instancia(int tamanho){
		instancias = new Long[tamanho];
	}
	
	public void add(int index, Long value){
		instancias[index] = value;
	}
	
	public void imprimir(){
		for (int i = 0; i < instancias.length; i++) {
			System.out.print(instancias[i]+",");
		}
		System.out.println("\n");
		
	}
}
