package me.pocArquitetura.services;

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

import me.pocArquitetura.dao.FuncionarioDAO;
import me.pocArquitetura.entidades.Acumulador;
import me.pocArquitetura.entidades.Funcionario;
import me.pocArquitetura.util.Util;

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
	public Response salvar(Funcionario funcionario) {
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
	
	/**
	 * Antes de realizar o update do BD verifica se o objeto foi realmente alterado
	 * @param funcionario
	 * @param request
	 * @return
	 */
	@PUT
	@Path("cache/funcionarios")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response atualizarComCache(Funcionario funcionario, @Context Request request) {
		
		Funcionario func = funcionarioDAO.find(Funcionario.class, funcionario.getMatricula());
		EntityTag tag = new EntityTag(Integer.toString(func.hashCode()));
		//Verifica se o objeto recebido no request é igual o objeto que está no BD
		ResponseBuilder builder = request.evaluatePreconditions(tag);
		
		if(builder!=null){
			return Response.status(204).build();
		}
		
		func = funcionarioDAO.merge(funcionario);
		return Response.status(200).entity(func).build();
	}

	@GET
	@Path("funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Funcionario getFuncionario(@PathParam("matricula") String matricula) {

		return funcionarioDAO.find(Funcionario.class, matricula);
	}
	/**
	 * Utiliza estratégia de cache para evitar consulta ao BD desnecessária
	 * @param matricula
	 * @param request
	 * @return
	 */
	@GET
	@Path("cache/funcionario/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFuncionarioCache(@PathParam("matricula") String matricula, @Context Request request) {
		ZoneId zone = ZoneId.of("Brazil/East");
		LocalDateTime hoje = LocalDateTime.now(zone);
		LocalDateTime hojeMais2Minutos = hoje.plus(1, ChronoUnit.MINUTES);
		
		Funcionario funcionario = funcionarioDAO.find(Funcionario.class, matricula);
		
		if (funcionario == null) {
			funcionario = new Funcionario();
		}
		EntityTag tag = new EntityTag(Integer.toString(funcionario.hashCode()));
		
		
		CacheControl cc = new CacheControl();
		cc.setMaxAge(60); //Define a idade mínima da resposta json no cache (nesse caso 60 segundos)
		cc.setPrivate(true);
		cc.setNoCache(false);
		cc.setSMaxAge(60);
		
		//verifica se o cache está válido
		ResponseBuilder builder = request.evaluatePreconditions(tag);
		// se for diferente de null o cache está válido
		if (builder != null) {
			//se cache válido devolve codigo 304 e revalida o cache por mais 2 minutos 
			Date asDate = Util.asDate(hojeMais2Minutos);
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
	/**
	 * Implementa serviço assincrono. Permite melhor gestão das requisições.
	 * @param matricula
	 * @param asyncResponse
	 */
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
