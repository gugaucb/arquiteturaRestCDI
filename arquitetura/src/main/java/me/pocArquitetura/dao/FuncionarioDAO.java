package me.pocArquitetura.dao;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.transaction.Transactional;

import me.pocArquitetura.entidades.Funcionario;

@Stateless
@Default
@Transactional
public class FuncionarioDAO extends GenericDaoJpa<Funcionario>{

	



}
