package me.pocArquitetura.filter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import me.costa.gustavo.saad4jee.annotations.RobotDetect;
import me.costa.gustavo.saad4jee.enums.Comandos;
import me.costa.gustavo.saad4jee.interceptors.RobotDetectIntercept;

@Provider
@PreMatching
public class RobotDetectFilter1 implements ContainerRequestFilter {
	private final Logger LOGGER = Logger.getLogger( RobotDetectIntercept.class.getName() );
	
	@RobotDetect(comandos={Comandos.EmitirEvento, Comandos.EnviarTrap})
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOGGER.log(Level.INFO, "filter acionado");
	}
}
