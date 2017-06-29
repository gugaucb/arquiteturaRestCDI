package me.pocArquitetura.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Named
@RequestScoped
public class Instancias extends BaseEntity<Serializable> implements Serializable{
	@Transient
	private static final long serialVersionUID = 5834299037501917146L;
	@Id
	@GeneratedValue
	private Long id;
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy="instancias")
	private List<Instancia> listaInstancia = new ArrayList<Instancia>();
	
	/**
	 * 
	 * @param listaCaracteristicas - Foto das caracteristas monitoradas em um dado momento
	 */
	public void add(Instancia listaCaracteristicas) {
		getInstancias().add(listaCaracteristicas);
	}
	/**
	 * 
	 * @return A ultima instancia inserida.
	 */
	public Instancias getUltimaInstanciaEmInstancias(){
		if(getInstancias().isEmpty()){
			return null;
		}
		return subList(getInstancias().subList(getInstancias().size()-1, getInstancias().size()));
	}
	
	public Instancia getUltimaInstancia(){
		if(getInstancias().isEmpty()){
			return null;
		}
		return getInstancias().get(getInstancias().size()-1);
	}
	
	private Instancias subList(List<Instancia> subInstancias){
		Instancias instanciasTemp = new Instancias();
		instanciasTemp.addAll(subInstancias);
		return instanciasTemp;
		
	}
	
	private void addAll(List<Instancia> subInstancias) {
		this.getInstancias().addAll(subInstancias);
		
	}
	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isEmpty(){
		return getInstancias().isEmpty();
	}
	public List<Instancia> getListInstancias() {
		return getInstancias();
	}
	public int size() {
		return getInstancias().size();
	}
	public List<Instancia> getInstancias() {
		return listaInstancia;
	}
	public void setInstancias(List<Instancia> instancias) {
		this.listaInstancia = instancias;
	}
}
