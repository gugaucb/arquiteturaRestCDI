package me.pocArquitetura.negocio;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import me.pocArquitetura.annotations.Monitoring;
import me.pocArquitetura.dao.FuncionarioDAO;
import me.pocArquitetura.entidades.Funcionario;
import me.pocArquitetura.negocio.exceptions.DadoExistenteException;
import me.pocArquitetura.negocio.exceptions.EntidadeNaoEncontradaException;

@Named
@RequestScoped
public class FuncionarioBean implements Bean {
	@Inject
	FuncionarioDAO funcionarioDAO;

	public Funcionario admitir(Funcionario funcionario) {
		if (funcionarioDAO.find(Funcionario.class, funcionario.getId()) == null) {
			Funcionario func = funcionarioDAO.save(funcionario);
			return func;
		} else {
			

			throw new DadoExistenteException(funcionario);
		}

	}
	
	public Funcionario alterarDadosCadastrais(Funcionario funcionario){
		if (funcionarioDAO.find(Funcionario.class, funcionario.getId()) != null) {
			Funcionario merge = funcionarioDAO.merge(funcionario);
			Funcionario funcionarioAtualizado = merge;
			return funcionarioAtualizado;
		}else{
			throw new EntidadeNaoEncontradaException(funcionario);
		}
		
	}
	@Monitoring
	public Funcionario recuperarFuncionarioPorMatricula(Funcionario funcionario){
		Funcionario funcionarioLocalizado = funcionarioDAO.find(Funcionario.class, funcionario.getMatricula());
		if(funcionarioLocalizado!=null){
			return funcionarioLocalizado;
		}else{
			throw new EntidadeNaoEncontradaException(funcionario);
		}
		
	}
}
