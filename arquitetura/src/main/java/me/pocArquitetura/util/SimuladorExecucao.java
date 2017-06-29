package me.pocArquitetura.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorExecucao {
	private static float porcentagemMenorProbabilidade = 0.01f;
	private long[] temposEmMillis = {1, 3, 11, 13, 2000};
	private List<Long> distribucaoProbabilisticaTempo = new ArrayList<Long>();
	private static SimuladorExecucao  simuladorExecucao;
	private SimuladorExecucao(){
		
	}
	
	public void recalcularDistribuicao(float porcentagem){
		gerarDistribuicaoTempos(porcentagem);
	}
	
	public static SimuladorExecucao getInstance(){
		if(simuladorExecucao==null){
			simuladorExecucao = new SimuladorExecucao();
			simuladorExecucao.gerarDistribuicaoTempos(porcentagemMenorProbabilidade);
		}
		return simuladorExecucao;
	}
	
	public void block(){
		try {
			Thread.sleep(gerarTempoAleatorio());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Long gerarTempoAleatorio(){
		int random = new Random().nextInt(distribucaoProbabilisticaTempo.size());
		Long tempo = 0L;
		if(random<=distribucaoProbabilisticaTempo.size()){
			tempo = distribucaoProbabilisticaTempo.get(random);
		}
		System.out.println("Tempo Aleatorio: "+tempo);
		return tempo;
	}
	
	private void gerarDistribuicaoTempos(float d) {
		int quantidadeIndividual = calcularQuantidadeIndividual(d);
		int quantidadeMenorProbabilidade = calcularMenorProbabilidade(quantidadeIndividual);
		for (int i = 0; i < quantidadeIndividual; i++) {
			distribucaoProbabilisticaTempo.add(temposEmMillis[0]);
			distribucaoProbabilisticaTempo.add(temposEmMillis[1]);
			distribucaoProbabilisticaTempo.add(temposEmMillis[2]);
			distribucaoProbabilisticaTempo.add(temposEmMillis[3]);
		}
		
		for (int i = 0; i < quantidadeMenorProbabilidade ; i++) {
			distribucaoProbabilisticaTempo.add(temposEmMillis[4]);
		}
	}

	private int calcularMenorProbabilidade(int quantidadeIndividual) {
		return 100-(quantidadeIndividual*temposEmMillis.length-1);
	}

	private int calcularQuantidadeIndividual(float d) {
		int quantidadeIndividual = 0;
		quantidadeIndividual = (100-Math.round((100*d)))/temposEmMillis.length;
		return quantidadeIndividual;
	}
	
	
}
