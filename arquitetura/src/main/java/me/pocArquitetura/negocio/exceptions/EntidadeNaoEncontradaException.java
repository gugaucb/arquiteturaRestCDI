package me.pocArquitetura.negocio.exceptions;

import java.io.Serializable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.pocArquitetura.entidades.BaseEntity;

public class EntidadeNaoEncontradaException extends WebApplicationException {
	public EntidadeNaoEncontradaException(BaseEntity<Serializable> entidade) {
		super(Response.status(Status.NOT_FOUND).entity(entidade).build());
	}
}
