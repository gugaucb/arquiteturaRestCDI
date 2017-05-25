package me.pocArquitetura.services;

import java.util.Random;

import javax.ws.rs.container.AsyncResponse;

import me.pocArquitetura.dao.FuncionarioDAO;
import me.pocArquitetura.entidades.Funcionario;

public class ProcessoAsync implements Runnable {
	private FuncionarioDAO funcionarioDAO;
	private String matricula;
	private AsyncResponse asyncResponse;
	
	
	public ProcessoAsync(FuncionarioDAO funcionarioDAO, String matricula, AsyncResponse asyncResponse) {
		this.funcionarioDAO = funcionarioDAO;
		this.matricula = matricula;
		this.asyncResponse = asyncResponse;
	}
	
	@Override
	public void run() {
		String initialThread = Thread.currentThread().getName();
		System.out.println("Thread Assincrona: " + initialThread + " in action...");
		heavyLifting();
		//asyncResponse.resume(funcionarioDAO.find(Funcionario.class, matricula));
		//Funcionario f = funcionarioDAO.find(Funcionario.class, matricula);
		asyncResponse.resume(new Funcionario());
		System.out.println("Thread Assincrona: " + initialThread + " in complete...");
		
	}
	
	private String heavyLifting() {
		try {
			Thread.sleep(new Random().nextInt(2000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "RESULT";
	}

}
