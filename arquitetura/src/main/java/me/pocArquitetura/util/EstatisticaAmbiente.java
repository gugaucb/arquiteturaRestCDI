package me.pocArquitetura.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

public class EstatisticaAmbiente {

	private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();
	private static RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	private static  Runtime runtime = Runtime.getRuntime();
	public static final int MB = 1024*1024;

	public static Double getAvailableProcessors() {
		return Double.parseDouble(operatingSystemMXBean.getAvailableProcessors()+"");
	}

	public static double getSystemLoadAverage() {
		return Double.parseDouble(operatingSystemMXBean.getSystemLoadAverage()+"");
	}

	public static Double getPrevUpTime() {
		return Double.parseDouble(runtimeMXBean.getStartTime()+"");
	}


	public static Double getUptime() {
		return Double.parseDouble(runtimeMXBean.getUptime()+"");
	}
	
	public static Double getUsedMemory(){
		return Double.parseDouble((getAvailableMemory() - getFreeMemory())+"");
	}
	public static Double getFreeMemory(){
		return Double.parseDouble((runtime.freeMemory()/MB)+"");
	}
	public static Double getAvailableMemory(){
		return Double.parseDouble((runtime.totalMemory()/MB)+"");
	}
	
	public static Double getMaximumAvailableMemory(){
		return Double.parseDouble((runtime.freeMemory()/MB)+"");
	}
	
	
	
	public static void imprimir(){
		System.out.println("getAvailableProcessors: "+getAvailableProcessors());
		System.out.println("getPrevUpTime: "+getPrevUpTime());
		System.out.println("SystemLoadAverage: "+getSystemLoadAverage());
		System.out.println("getUptime: "+getUptime());
		System.out.println("getUsedMemory: "+getUsedMemory()+" MB");
		System.out.println("getFreeMemory: "+getFreeMemory()+" MB");
		System.out.println("getAvailableMemory: "+getAvailableMemory()+" MB");
		System.out.println("getMaximumAvailableMemory: "+getMaximumAvailableMemory()+" MB");
	}
	
}
