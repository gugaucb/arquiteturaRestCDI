package me.pocArquitetura.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Named
@RequestScoped
public class Instancia extends BaseEntity<Serializable> implements Serializable, Cloneable {
	public static final int QUANT_POSICAO_INIT = 100;

	@Transient
	private static final long serialVersionUID = -3572746437024617004L;

	@Transient
	private Dicionario dicionario;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Instancias instancias;
	
	@ElementCollection
	@Column(name = "max", precision = 10, scale = 2)
	private List<Double> caracteristicas = new ArrayList<Double>(QUANT_POSICAO_INIT);

	public void setDicionario(Dicionario dicionario) {
		this.dicionario = dicionario;
	}
	
	@Override
	protected Instancia clone() throws CloneNotSupportedException {
		super.clone();
		Instancia instancia = new Instancia();
		instancia.id = this.id;
		instancia.caracteristicas = new ArrayList<Double>(this.caracteristicas);
		instancia.dicionario = this.dicionario;
		instancia.instancias = this.instancias;
		
		
		return instancia;
	}

	public void add(String caracteristica, Double value) {
		initList();
		if (dicionario != null) {
			Integer posicao = dicionario.getPosicao(caracteristica);
			caracteristicas.set(posicao, value+(posicao*10000));
		} else {
			System.out.println("Dicionario nulo");
		}
	}

	public void imprimir() {
		if (caracteristicas != null) {
			for (Double caract : caracteristicas) {
				System.out.print(caract + ",");
			}
		}
	}

	public List<Double> getCaracteristicas() {
		return caracteristicas;
	}
	
	
	@Override
	public Serializable getId() {
		return id;
	}

	private void initList() {
		if (caracteristicas.size() == 0) {
			for (int i = 0; i < QUANT_POSICAO_INIT; i++) {
				caracteristicas.add(0.0);
			}
		}
	}
	
	

	public Instancias getInstancias() {
		return instancias;
	}

	public void setInstancias(Instancias instancias) {
		this.instancias = instancias;
	}

}
