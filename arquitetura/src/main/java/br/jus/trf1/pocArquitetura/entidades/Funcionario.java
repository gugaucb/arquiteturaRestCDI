package br.jus.trf1.pocArquitetura.entidades;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@Mapped(namespaceMap = @XmlNsMap(jsonName = "atom", namespace = "http://www.w3.org/2005/Atom"))
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class Funcionario extends BaseEntity{
	@XmlElement
	@Id
	private String matricula;
	@XmlElement
	private String nome;
	
	
	
	public Funcionario() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Funcionario(String matricula, String nome) {
		super();
		this.matricula = matricula;
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.matricula, this.nome);
	}
}
