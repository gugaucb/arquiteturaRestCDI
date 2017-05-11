package me.pocArquitetura.dao;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.transaction.Transactional;

import me.pocArquitetura.entidades.Funcionario;

@Named
@RequestScoped
public class FuncionarioDAO extends GenericDaoJpa<Funcionario>{

	



}
