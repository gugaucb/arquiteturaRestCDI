package me.pocArquitetura.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

public class EstatisticaAmbiente {

	private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();
	private static RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

	public static int getAvailableProcessors() {
		return operatingSystemMXBean.getAvailableProcessors();
	}

	public static double getSystemLoadAverage() {
		return operatingSystemMXBean.getSystemLoadAverage();
	}

	public static long getPrevUpTime() {
		return runtimeMXBean.getStartTime();
	}

	public static long getElapsedTime() {
		return getUptime() - getPrevUpTime();
	}

	public static long getUptime() {
		return runtimeMXBean.getUptime();
	}

}
