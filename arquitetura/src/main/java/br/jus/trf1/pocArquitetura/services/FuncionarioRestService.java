package br.jus.trf1.pocArquitetura.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.jus.trf1.pocArquitetura.dao.FuncionarioDAO;
import br.jus.trf1.pocArquitetura.entidades.Acumulador;
import br.jus.trf1.pocArquitetura.entidades.Funcionario;
import br.jus.trf1.pocArquitetura.util.Util;

@Path("/")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class FuncionarioRestService {

	@Inject
	private Acumulador acumulador;

	@Inject
	FuncionarioDAO funcionarioDAO;

	@Resource
	ManagedExecutorService managedExecutorService;

	@POST
	@Path("funcionarios")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response simpleRest(Funcionario funcionario) {
		Funcionario func = funcionarioDAO.save(funcionario);
		return Response.status(200).entity(func).build();
	}
	
	@PUT
	@Path("funcionarios")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response atualizar(Funcionario funcionario) {
		Funcionario func = funcionarioDAO.merge(funcionario);
		return Response.status(200).entity(func).build();
	}

	@GET
	@Path("funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Funcionario getFuncionario(@PathParam("matricula") String matricula) {

		return funcionarioDAO.find(Funcionario.class, matricula);
	}
	
	@GET
	@Path("cache/funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFuncionarioCache(@PathParam("matricula") String matricula, @Context Request request) {
		 ZoneId zone = ZoneId.of("Brazil/East");
		LocalDateTime hoje = LocalDateTime.now(zone);
		LocalDateTime hojeMais2Minutos = hoje.plus(1, ChronoUnit.MINUTES);
		System.out.println(hoje);
		System.out.println(hojeMais2Minutos);
		Funcionario funcionario = funcionarioDAO.find(Funcionario.class, matricula);
		if(funcionario==null){
			funcionario = new Funcionario();
		}
		EntityTag tag = new EntityTag(Integer.toString(funcionario.hashCode()));
		
		
		CacheControl cc = new CacheControl();
		cc.setMaxAge(60);
		cc.setPrivate(true);
		cc.setNoCache(false);
		cc.setSMaxAge(60);
		
		ResponseBuilder builder = request.evaluatePreconditions(tag);
		if(builder!= null){
			Date asDate = Util.asDate(hojeMais2Minutos);
			System.out.println(asDate);
			builder.expires(asDate);
			builder.cacheControl(cc);
			return builder.build();
		}
		
		
		 builder = Response.ok(funcionario, "application/json");
		
		Date asDate = Util.asDate(hojeMais2Minutos);
		System.out.println(asDate);
		builder.expires(asDate);
		builder.cacheControl(cc);
		builder.tag(tag);
		return builder.build();
	}

	@GET
	@Path("async/funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@Asynchronous
	public void getFuncionarioAsyncro(@PathParam("matricula") final String matricula,
			@Suspended final AsyncResponse asyncResponse) {
		
		String initialThread = Thread.currentThread().getName();
		System.out.println(
				"Quantidade: " + acumulador.getQuantidade() + " Thread Requisicao: " + initialThread + " in action...");

		if (acumulador.getQuantidade() > 100) {
			asyncResponse.cancel();
			System.out.println("Requisicao Cancelada.");
		} else {
			acumulador.soma();
			final Future<?> atividade = managedExecutorService.submit(new Runnable() {
				@Override
				public void run() {
					String initialThread = Thread.currentThread().getName();
					System.out.println("Thread Assincrona: " + initialThread + " in action...");
					heavyLifting();
					asyncResponse.resume(funcionarioDAO.find(Funcionario.class, matricula));
					System.out.println("Thread Assincrona: " + initialThread + " in complete...");
				}

				private String heavyLifting() {
					try {
						Thread.sleep(new Random().nextInt(2000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "RESULT";
				}
			});

			asyncResponse.register(new CompletionCallback() {
				@Override
				public void onComplete(Throwable throwable) {
					if (throwable == null) {
						// Everything is good. Response has been successfully
						// dispatched to client
						System.out.println("Everything is good. Response has been successfully");
						acumulador.subtrai();
					} else {
						// An error has occurred during request processing
						System.out.println("An error has occurred during request processing");
						acumulador.subtrai();
					}
				}
			}, new ConnectionCallback() {
				public void onDisconnect(AsyncResponse disconnected) {
					// Connection lost or closed by the client!
					System.out.println("Connection lost or closed by the client!");
					atividade.cancel(true);
					acumulador.subtrai();
				}
			});
		}
		asyncResponse.setTimeout(40, TimeUnit.SECONDS);

		System.out.println("Thread Requisicao: " + initialThread + " in complete...");

	}
	
	
}
