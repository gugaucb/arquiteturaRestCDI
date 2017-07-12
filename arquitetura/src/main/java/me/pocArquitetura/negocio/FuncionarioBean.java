package me.pocArquitetura.negocio;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import me.costa.gustavo.saad4jee.annotations.Monitoring;
import me.costa.gustavo.saad4jee.annotations.RoboDetectEvent;
import me.costa.gustavo.saad4jee.annotations.SalvarRobotInstanciaEvent;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancia;
import me.costa.gustavo.saad4jee.interceptors.RobotDetectIntercept;
import me.pocArquitetura.dao.FuncionarioDAO;
import me.pocArquitetura.entidades.Funcionario;
import me.pocArquitetura.negocio.exceptions.DadoExistenteException;
import me.pocArquitetura.negocio.exceptions.EntidadeNaoEncontradaException;

@Named
@RequestScoped
public class FuncionarioBean implements Bean {
	@Inject
	FuncionarioDAO funcionarioDAO;
	
	private final Logger LOGGER = Logger.getLogger(FuncionarioBean.class.getName());

	public Funcionario admitir(Funcionario funcionario) {
		if (funcionarioDAO.find(Funcionario.class, funcionario.getId()) == null) {
			Funcionario func = funcionarioDAO.save(funcionario);
			return func;
		} else {
			

			throw new DadoExistenteException(funcionario);
		}

	}
	
	public void mensagemRobotDetect(@Observes @RoboDetectEvent String mensagem) {
		LOGGER.log(Level.INFO, mensagem);
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
