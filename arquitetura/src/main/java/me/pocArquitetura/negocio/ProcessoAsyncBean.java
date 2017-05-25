package me.pocArquitetura.negocio;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.container.AsyncResponse;

import me.pocArquitetura.dao.FuncionarioDAO;
import me.pocArquitetura.entidades.Funcionario;

@Named
public class ProcessoAsyncBean implements Runnable, Bean {
	@Inject
	private FuncionarioBean funcionarioBean;
	private String matricula;
	private AsyncResponse asyncResponse;
	
	
	public ProcessoAsyncBean(String matricula, AsyncResponse asyncResponse) {
		this.matricula = matricula;
		this.asyncResponse = asyncResponse;
	}
	
	@Override
	public void run() {
		String initialThread = Thread.currentThread().getName();
		System.out.println("Thread Assincrona: " + initialThread + " in action...");
		heavyLifting();
		asyncResponse.resume(funcionarioBean.recuperarFuncionarioPorMatricula(new Funcionario(matricula)));
		//Funcionario f = funcionarioDAO.find(Funcionario.class, matricula);
		//asyncResponse.resume(new Funcionario());
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
