package me.pocArquitetura.services;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.keycloak.authorization.client.AuthzClient;

@Path("/keycloak")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class AuthzRestService {
	/*@Resource
	AuthzClient authzClient;*/
	
	
	@GET
	@Path("authz")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autenticacao() {
		//Keycloak keycloak = Keycloak.getInstance("http://localhost:8080/auth/", "trf1", "gugaucb@gmail.com", "123456", "arquitetura");
		//System.out.println(keycloak.tokenManager().getAccessTokenString());
		
		return Response.status(200).entity(obtainAllEntitlements()).build();
	}
	
	private static String obtainAllEntitlements() {
        /*// create a new instance based on the configuration defined in keycloak-authz.json
        AuthzClient authzClient = AuthzClient.create();

        // obtian a Entitlement API Token in order to get access to the Entitlement API.
        // this token is just an access token issued to a client on behalf of an user with a scope kc_entitlement
        String eat = getEntitlementAPIToken(authzClient);

        // send the entitlement request to the server in order to obtain a RPT with all permissions granted to the user
        EntitlementResponse response = authzClient.entitlement(eat).getAll("hello-world-authz-service");
        String rpt = response.getRpt();

        System.out.println("You got a RPT: " + rpt);*/

        // now you can use the RPT to access protected resources on the resource server
        //return eat;
		return ""; 
    }
	
/*	private static String getEntitlementAPIToken(AuthzClient authzClient) {
        return authzClient.obtainAccessToken("alice", "alice").getToken();
    }*/
}
