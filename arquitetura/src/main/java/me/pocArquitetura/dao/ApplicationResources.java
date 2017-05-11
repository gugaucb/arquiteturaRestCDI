package me.pocArquitetura.dao;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Startup
@Singleton
@TransactionManagement(value=TransactionManagementType.BEAN)
public class ApplicationResources {
	
	public static EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("forge-default");

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        em.close();
    }

	
	
}
