package me.pocArquitetura.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.costa.gustavo.saad4jee.daos.RobotDetectInstanciaDAO;
import me.costa.gustavo.saad4jee.entity.Instancia;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancia;
import me.costa.gustavo.saad4jee.ia.Anomalia;
import me.costa.gustavo.saad4jee.ia.RobotDectectAnomalia;



@Path("/ML")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class IAResouce {

	@Inject
	private Anomalia anomalia;
	
	@Inject RobotDectectAnomalia robotDectectAnomalia;

	@Resource
	private ManagedExecutorService managedExecutorService;
	
	@Inject
	RobotDetectInstanciaDAO robotDetectInstanciaDAO;

	private Future<?> atividade;

	@GET
	@Path("/treinar")
	@Produces(MediaType.APPLICATION_JSON)
	@Asynchronous
	public void treinarML(@Suspended final AsyncResponse asyncResponse) {

		atividade = managedExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				if (anomalia.buildModel()!=null){
					asyncResponse.resume("Treinado");
				}else{
					asyncResponse.resume("Não Treinado");
				}
			}
		});
		
		System.out.println("Atividade null? "+atividade==null);
		
		asyncResponse.register(new CompletionCallback() {
			@Override
			public void onComplete(Throwable throwable) {
				if (throwable == null) {
					// Everything is good. Response has been successfully
					// dispatched to client
					//asyncResponse.resume("treinado");
				} else {
					// An error has occurred during request processing
					// asyncResponse.resume(throwable);
				}
			}
		}, new ConnectionCallback() {
			@Override
			public void onDisconnect(AsyncResponse disconnected) {
				// Connection lost or closed by the client!
				atividade.cancel(true);
			}
		});
		 asyncResponse.setTimeout(10, TimeUnit.MINUTES);

	}

	
	@GET
	@Path("/treinar-robotdetect")
	@Produces(MediaType.APPLICATION_JSON)
	@Asynchronous
	public void treinarRobotDetectML(@Suspended final AsyncResponse asyncResponse) {
		final List<RobotDetectInstancia> instancias = robotDetectInstanciaDAO.findAll(RobotDetectInstancia.class);
		
		//tentando resolver o problema do LazyInitialization
		List<RobotDetectInstancia> instanciastemp = new ArrayList<RobotDetectInstancia>();
		instanciastemp.addAll(instancias);
		//tentando resolver o problema do LazyInitialization
		
		atividade = managedExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				robotDectectAnomalia.buildModel(instanciastemp);
				asyncResponse.resume("Treinado");
			}
		});
		
		System.out.println("Atividade null? "+atividade==null);
		
		asyncResponse.register(new CompletionCallback() {
			@Override
			public void onComplete(Throwable throwable) {
				if (throwable == null) {
					// Everything is good. Response has been successfully
					// dispatched to client
					//asyncResponse.resume("treinado");
				} else {
					// An error has occurred during request processing
					// asyncResponse.resume(throwable);
				}
			}
		}, new ConnectionCallback() {
			@Override
			public void onDisconnect(AsyncResponse disconnected) {
				// Connection lost or closed by the client!
				atividade.cancel(true);
			}
		});
		 asyncResponse.setTimeout(10, TimeUnit.MINUTES);

	}
	
	@GET
	@Path("/isanomalia")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isAnomalia() {
		return Response.ok().entity(anomalia.isAnomalia()).build();
	}
	
	@GET
	@Path("/isanomalia-robotdetect")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isAnomaliaRobotDetect() {
		return Response.ok().entity(anomalia.isAnomalia()).build();
	}
	
	@GET
	@Path("/zerarIA")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zerarIA() {
		anomalia.zerarIA();
		return Response.ok().build();
	}

	@GET
	@Path("/cancelar_treinamento")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelarTreinamento() {
		if (atividade != null) {
			return Response.ok().entity(atividade.cancel(false)).build();
		}
		return Response.status(503).entity("Treinamento nao iniciado.").build();
	}

}
