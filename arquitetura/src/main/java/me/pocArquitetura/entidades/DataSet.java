package me.pocArquitetura.entidades;

import java.time.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.pocArquitetura.dao.InstanciasDAO;
import me.pocArquitetura.util.EstatisticaAmbiente;

/**
 * 
 * 
 * https://github.com/PacktPublishing/Machine-Learning-in-Java/blob/master/MLJ-Chapter7/src/Anomaly.java
 */

@Singleton
public class DataSet {
	@Inject
	InstanciasDAO instanciasDao;

	@Inject
	private Dicionario dicionario;
	//private Instancia listaCaracteristicas = new Instancia();
	private Instancias instancias = new Instancias();

	private static double mediaRequisicoesPorMinuto = 0.0;
	private static double mediaExcecoesPorMinuto = 0.0;
	private static double quantRequisicoesPorMinuto = 0.0;
	private static double quantExcecoesPorMinuto = 0.0;
	private static double quantDeMinuto = 1.0;

	public void zerarIA(){
		//listaCaracteristicas = new Instancia();
		instancias = new Instancias();
		quantRequisicoesPorMinuto = 0.0;
		quantExcecoesPorMinuto = 0.0;
		quantDeMinuto = 1.0;
		mediaRequisicoesPorMinuto = 0.0;
		mediaExcecoesPorMinuto = 0.0;
	}

	public static Instancia recebeEstimuloMetodoV1(Instancia instancia, String metodo, Duration duration) {
		instancia.add(metodo, Double.parseDouble(duration.toMillis() + ""));
		incluirRequisicoesPorMinuto(instancia, metodo);
		return instancia;
	}
	
	public void salvarInstancia(Instancia instancia){
		instancias.add(instancia);
		imprimeInstancias(instancia);
		dicionario.imprimir();
	}
	public void recebeEstimuloMetodo(String metodo, Duration duration) {
		//TODO Refatorar. Paleativo já que um atributo Transient não pode ser injetado.

		Instancia instancia = new Instancia();
		instancia.setDicionario(dicionario);
		instancia.add(metodo, Double.parseDouble(duration.toMillis() + ""));
		//getListaCaracteristicas().setDicionario(dicionario);
		//getListaCaracteristicas().add(metodo, Double.parseDouble(duration.toMillis() + ""));
		//incluirCondicoesAmbientes();
		incluirRequisicoesPorMinuto(instancia, metodo);
			/*try {
				instancias.add(getListaCaracteristicas().clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		instancias.add(instancia);
		imprimeInstancias(instancia);
		dicionario.imprimir();
	}

	public static Instancia recebeEstimuloExcecaoV1(Instancia instancia, String caracteristica) {
		incluirExcecoesPorMinuto(instancia, caracteristica);
		instancia.add(caracteristica, -1.0);
		return instancia;
	}
	
	public void recebeEstimuloExcecao(String caracteristica) {
		Instancia instancia = new Instancia();
		instancia.setDicionario(dicionario);
		incluirExcecoesPorMinuto(instancia, caracteristica);
		//TODO Refatorar. Paleativo já que um atributo Transient não pode ser injetado.
	/*	getListaCaracteristicas().setDicionario(dicionario);
		getListaCaracteristicas().add(caracteristica, -1.0);*/
		//incluirCondicoesAmbientes();
		/*try {
			instancias.add(getListaCaracteristicas().clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		instancia.add(caracteristica, -1.0);
		instancias.add(instancia);
		imprimeInstancias(instancia);
		dicionario.imprimir();
	}

	private static void receberEstimuloAmbiente(Instancia instancia, String caracteristica, Double valor) {
		instancia.add(caracteristica, valor);
	}
	
	private static double contarQuantRequisicoesPorMinuto(){
		return ++quantRequisicoesPorMinuto;
	}
	private static double contarQuantExcecoesPorMinuto(){
		return ++quantExcecoesPorMinuto;
	}
	
	
	private static void incluirRequisicoesPorMinuto(Instancia instancia, String metodo){
		contarQuantRequisicoesPorMinuto();
		receberEstimuloAmbiente(instancia, metodo+" - MediaRequisicoesPorMinuto", mediaRequisicoesPorMinuto);
	}
	private static void incluirExcecoesPorMinuto(Instancia instancia,String excecao){
		contarQuantExcecoesPorMinuto();
		receberEstimuloAmbiente(instancia, excecao+" - MediaExcecoesPorMinuto", mediaExcecoesPorMinuto);
	}
	
	private void incluirCondicoesAmbientes(Instancia instancia) {
		receberEstimuloAmbiente(instancia, "AvailableProcessors", EstatisticaAmbiente.getAvailableProcessors() + 0L);
		receberEstimuloAmbiente(instancia, "SystemLoadAverage",
				Double.parseDouble(String.format("%.0f", EstatisticaAmbiente.getSystemLoadAverage())));
		//receberEstimuloAmbiente(instancia, "AvailableMemory", EstatisticaAmbiente.getAvailableMemory());
		receberEstimuloAmbiente(instancia, "getFreeMemory", EstatisticaAmbiente.getFreeMemory());
		receberEstimuloAmbiente(instancia, "getMaximumAvailableMemory", EstatisticaAmbiente.getMaximumAvailableMemory());
		receberEstimuloAmbiente(instancia, "getUsedMemory", EstatisticaAmbiente.getUsedMemory());
	}
	
	private void zeraQuantContadorPorMinuto(){
		quantExcecoesPorMinuto = 0;
		quantRequisicoesPorMinuto = 0;
	}
	public void somarMinuto(){
		quantDeMinuto++;
		zeraQuantContadorPorMinuto();
	}
	
	public void calcularMedia(){
		mediaRequisicoesPorMinuto = quantRequisicoesPorMinuto/quantDeMinuto;
		mediaExcecoesPorMinuto = quantExcecoesPorMinuto/quantDeMinuto;
	}
	
	public static void imprimeInstancias(Instancia instancia) {
		instancia.imprimir();
	}

	public Instancias getInstancias() {
		return instancias;
	}

	public void setInstancias(Instancias instancias) {
		this.instancias = instancias;

	}

	/*public Instancia getListaCaracteristicas() {
		return listaCaracteristicas;
	}

	public void setListaCaracteristicas(Instancia listaCaracteristicas) {
		this.listaCaracteristicas = listaCaracteristicas;
	}*/

}
