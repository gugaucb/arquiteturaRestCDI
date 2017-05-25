package me.pocArquitetura.negocio.exceptions;

import java.io.Serializable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.pocArquitetura.entidades.BaseEntity;

public class DadoExistenteException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6891005792193180602L;
	
	public DadoExistenteException(BaseEntity<Serializable> entidade) {
		
		super(Response.status(Status.FOUND).entity(entidade).build());
		
		
	}
}
