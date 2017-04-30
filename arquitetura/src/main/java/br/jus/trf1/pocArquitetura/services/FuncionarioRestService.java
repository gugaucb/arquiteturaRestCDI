package br.jus.trf1.pocArquitetura.services;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.jus.trf1.pocArquitetura.dao.FuncionarioDAO;
import br.jus.trf1.pocArquitetura.entidades.Acumulador;
import br.jus.trf1.pocArquitetura.entidades.Funcionario;

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

	@GET
	@Path("funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Funcionario getFuncionario(@PathParam("matricula") String matricula) {

		return funcionarioDAO.find(Funcionario.class, matricula);
	}
	
	@GET
	@Path("cache/funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFuncionarioCache(@PathParam("matricula") String matricula) {
		CacheControl cc = new CacheControl();
		cc.setMaxAge(300);
		cc.setPrivate(true);
		cc.setNoCache(false);
		ResponseBuilder builder = Response.ok(funcionarioDAO.find(Funcionario.class, matricula), "application/json");
		builder.cacheControl(cc);
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
