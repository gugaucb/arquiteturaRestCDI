package me.pocArquitetura.entidades;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class Funcionario extends BaseEntity<Serializable> implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3477095598618294634L;
	@XmlElement
	@Id
	@NotNull
	@Size(min = 5, max = 11, message = "A matricula deve ter entre 5 a 11 caracteres.")
	private String matricula;
	@XmlElement
	@NotNull
	private String nome;

	public Funcionario() {
		super();
	}

	public Funcionario(String matricula, String nome) {
		super();
		this.matricula = matricula;
		this.nome = nome;
	}

	public Funcionario(String matricula) {
		this.matricula = matricula;
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
		return matricula;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.matricula, this.nome);
	}
}
