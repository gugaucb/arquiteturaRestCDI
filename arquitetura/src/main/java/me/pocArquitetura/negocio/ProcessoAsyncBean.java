package me.pocArquitetura.negocio;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.container.AsyncResponse;

import me.pocArquitetura.dao.FuncionarioDAO;
import me.pocArquitetura.entidades.Funcionario;

@Named
public class ProcessoAsyncBean implements Runnable, Bean {

	private FuncionarioBean funcionarioBean;
	private String matricula;
	private AsyncResponse asyncResponse;
	
	
	public ProcessoAsyncBean(String matricula, AsyncResponse asyncResponse, FuncionarioBean funcionarioBean2) {
		this.funcionarioBean = funcionarioBean2;
		this.matricula = matricula;
		this.asyncResponse = asyncResponse;
	}
	
	@Override
	public void run() {
		String initialThread = Thread.currentThread().getName();
		System.out.println("Thread Assincrona: " + initialThread + " in action...");
		heavyLifting();
		System.out.println("passou heavy");
		Funcionario recuperarFuncionarioPorMatricula = funcionarioBean.recuperarFuncionarioPorMatricula(new Funcionario(matricula));
		System.out.println("Recuperou objeto");
		asyncResponse.resume(recuperarFuncionarioPorMatricula);
		System.out.println("async resume");
		//Funcionario f = funcionarioDAO.find(Funcionario.class, matricula);
		//asyncResponse.resume(new Funcionario());
		System.out.println("Thread Assincrona: " + initialThread + " in complete...");
		
	}
	
	private String heavyLifting() {
		try {
			System.out.println("Sleep");
			Thread.sleep(new Random().nextInt(2000));
			System.out.println("wake up");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "RESULT";
	}

}
