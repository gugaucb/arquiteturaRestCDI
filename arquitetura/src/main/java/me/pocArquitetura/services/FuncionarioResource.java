package me.pocArquitetura.services;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import me.pocArquitetura.annotations.Monitoring;
import me.pocArquitetura.entidades.Funcionario;
import me.pocArquitetura.negocio.FuncionarioBean;
import me.pocArquitetura.negocio.ProcessoAsyncBean;
import me.pocArquitetura.util.DateUtil;
import me.pocArquitetura.util.RestUtil;

@Path("/funcionarios")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class FuncionarioResource {
	@Inject
	FuncionarioBean funcionarioBean;

	@Resource
	ManagedExecutorService managedExecutorService;

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Monitoring
	public Response salvar(Funcionario funcionario, @Context UriInfo uriInfo) {
		Funcionario funcionarioCadastrado = funcionarioBean.admitir(funcionario);
		return Response.status(200).entity(funcionarioCadastrado).build();
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Monitoring
	public Response atualizar(Funcionario funcionario) {
		Funcionario funcionarioAlterado = funcionarioBean.alterarDadosCadastrais(funcionario);
		return Response.status(200).entity(funcionarioAlterado).build();
	}

	/**
	 * Antes de realizar o update do BD verifica se o objeto foi realmente
	 * alterado
	 * 
	 * @param funcionario
	 * @param request
	 * @return
	 */
	@PUT
	@Path("cache")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response atualizarComCache(Funcionario funcionario, @Context Request request) {
		ResponseBuilder builder = IsObjetoIgualAoExistente(funcionario, request);
		if (builder!=null) {
			return builder.status(204).build();
		}

		Funcionario funcionarioAlterado = funcionarioBean.alterarDadosCadastrais(funcionario);
		return Response.status(Status.OK).entity(funcionarioAlterado).build();
	}

	private ResponseBuilder IsObjetoIgualAoExistente(Funcionario funcionario, Request request) {
		Funcionario funcionarioRecuperado = funcionarioBean.recuperarFuncionarioPorMatricula(funcionario);
		if (funcionarioRecuperado != null) {
			EntityTag tag = new EntityTag(Integer.toString(funcionarioRecuperado.hashCode()));
			// Verifica se o objeto recebido no request Ã© igual o objeto que
			// estÃ¡ no BD
			ResponseBuilder builder = request.evaluatePreconditions(tag);
			// se for diferente de null o cache estÃ¡ vÃ¡lido
			return builder;
		} else {
			return null;
		}

	}

	@GET
	@Path("/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@Monitoring
	public Funcionario getFuncionario(
			@NotNull @Size(min = 5, max = 11, message = "A matricula deve ter entre 5 a 11 caracteres.") @PathParam("matricula") String matricula) {

		return funcionarioBean.recuperarFuncionarioPorMatricula(new Funcionario(matricula));
	}

	/**
	 * Utiliza estratÃ©gia de cache para evitar consulta ao BD desnecessÃ¡ria
	 * 
	 * @param matricula
	 * @param request
	 * @return
	 */
	@GET
	@Path("cache/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@Monitoring
	public Response getFuncionarioCache(
			@NotNull @Size(min = 5, max = 11, message = "A matricula deve ter entre 5 a 11 caracteres.") @PathParam("matricula") String matricula,
			@Context Request request) {

		Funcionario funcionario = new Funcionario(matricula);
		ResponseBuilder builder = IsObjetoIgualAoExistente(funcionario, request);
		if (builder != null) {
			builder.expires(DateUtil.somarData(2, ChronoUnit.MINUTES));
			builder.cacheControl(RestUtil.defineCacheDoRecurso(60));
			return builder.status(Status.NOT_MODIFIED).build();
		} else {
			EntityTag tag = new EntityTag(Integer.toString(funcionario.hashCode()));
			Funcionario funcionarioRecuperado = funcionarioBean.recuperarFuncionarioPorMatricula(funcionario);
			return Response.status(Status.OK).cacheControl(RestUtil.defineCacheDoRecurso(60)).tag(tag).entity(funcionarioRecuperado).build();
		}

	}
	
	/**
	 * Implementa serviÃ§o assincrono. Permite melhor gestÃ£o das requisiÃ§Ãµes.
	 * 
	 * @param matricula
	 * @param asyncResponse
	 */
	@GET
	@Path("async/{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@Asynchronous
	public void getFuncionarioAsyncro(
			@NotNull @Size(min = 5, max = 11, message = "A matricula deve ter entre 5 a 11 caracteres.") @PathParam("matricula") final String matricula,
			@Suspended final AsyncResponse asyncResponse) {

				asyncResponse.register(new CompletionCallback() {
					@Override
					public void onComplete(Throwable throwable) {
						if (throwable == null) {
							// Everything is good. Response has been successfully
							// dispatched to client
						} else {
							// An error has occurred during request processing
						}
					}
				}, new ConnectionCallback() {
					@Override
					public void onDisconnect(AsyncResponse disconnected) {
						// Connection lost or closed by the client!
					}
				});
			asyncResponse.setTimeout(60, TimeUnit.SECONDS);

			final Future<?> atividade = managedExecutorService.submit(new ProcessoAsyncBean(matricula, asyncResponse));
	}
}
