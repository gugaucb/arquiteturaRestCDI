package me.pocArquitetura.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Named
@ApplicationScoped
public class ApplicationResources {
	
	@PersistenceUnit
	private EntityManagerFactory factory;
	
	@Produces @RequestScoped
	public EntityManager create() {
		return factory.createEntityManager();
	}
	
	public void close(@Disposes EntityManager em) {
		em.close();
	}
	
	public EntityManagerFactory getFactory(){
		return factory;
	}
}
