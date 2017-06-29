package me.pocArquitetura.schedules;

import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import me.pocArquitetura.dao.DicionarioDAO;
import me.pocArquitetura.dao.InstanciasDAO;
import me.pocArquitetura.entidades.DataSet;
import me.pocArquitetura.entidades.Dicionario;
import me.pocArquitetura.entidades.Instancias;

@Singleton
@Startup
public class Monitoramento {
	@Inject
	private InstanciasDAO instanciasDAO;

	@Inject
	private DicionarioDAO dicionarioDao;

	@Inject
	private DataSet dataSet;

	@Inject
	private Dicionario dicionario;

	private boolean salvo = false;

	@Schedule(second = "1", minute = "*/1", hour = "*", info = "Report every 1 Minutes", persistent = false)
	public void salvarEstimulos() {
		dataSet.calcularMedia();
		dataSet.somarMinuto();
		//TODO Refatorar
		System.out.println("Salvo"+salvo);
		if (!salvo) {
			salvo=true;
			List<Dicionario> dicionarios = dicionarioDao.findAll(Dicionario.class);
			if(dicionarios != null && !dicionarios.isEmpty()){
				dicionario = dicionarios.get(0);
			}
			if(dicionario.getListaMetodos()==null || dicionario.getListaMetodos().isEmpty()){
				instanciasDAO.save(dataSet.getInstancias());
				dicionarioDao.save(dicionario);
				System.out.println("Monitoramento.salvarEstimulos()");
			}
		} else {
			instanciasDAO.merge(dataSet.getInstancias());
			dicionarioDao.merge(dicionario);
			System.out.println("Monitoramento.AtualizarEstimulos() - Total Instancias: "+dataSet.getInstancias().size());
		}
		System.out.println("Salvo"+salvo);
	}

}
