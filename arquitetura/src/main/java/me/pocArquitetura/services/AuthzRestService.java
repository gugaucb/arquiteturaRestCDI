package me.pocArquitetura.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;

@Path("/keycloak")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class AuthzRestService { 
	@GET
	@Path("authz")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autenticacao() {
		Keycloak keycloak = Keycloak.getInstance("http://localhost:8080/auth/", "trf1", "gugaucb@gmail.com", "123456", "arquitetura");
		System.out.println(keycloak.tokenManager().getAccessTokenString());
		return Response.status(200).entity(keycloak.tokenManager().getAccessTokenString()).build();
	}
}
