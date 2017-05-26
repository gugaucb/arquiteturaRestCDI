package me.pocArquitetura.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {
	private static ZoneId zone = ZoneId.of("Brazil/East");
	
	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(zone).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(zone).toInstant());
	}
	
	public static Date createData(Long timeInMiliseconds){
		return new Date(timeInMiliseconds);
	}
	
	public static Date somarData(int quantidade, ChronoUnit unidade) {
		ZoneId zone = ZoneId.of("Brazil/East");
		LocalDateTime hoje = LocalDateTime.now(zone);
		LocalDateTime hojeMais2Minutos = hoje.plus(quantidade, unidade);
		Date asDate = DateUtil.asDate(hojeMais2Minutos);
		return asDate;
	}
}
