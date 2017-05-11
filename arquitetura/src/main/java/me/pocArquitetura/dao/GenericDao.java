package me.pocArquitetura.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import me.pocArquitetura.entidades.BaseEntity;

@Stateless
public class GenericDao<T extends BaseEntity> {
	@Inject
	private EntityManager em;
	public enum MatchMode { START, END, EXACT, ANYWHERE }
	
	
	public <T extends BaseEntity> T find(Class<T> classz, String id) {
		return em.find(classz, id);
	}
	
	public <T extends BaseEntity> T create(T entidade) {
		
		em.persist(entidade);
		return entidade;
	}
	public <T extends BaseEntity<?>> List<T> findByProperty(Class<T> clazz, String propertyName, String value, MatchMode matchMode) {
		ApplicationResources.emf.getCriteriaBuilder();
		
		return null;
	}
}
