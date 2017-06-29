package me.pocArquitetura.entidades;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListTest {

	@Test
	public void test() {
		List<Long> lista = new ArrayList<Long>();
		lista.add(0, 1L);
		lista.remove(0);
		lista.add(0, 1L);
		lista.remove(0);
		lista.add(0, 2L);
		lista.remove(0);
		lista.add(0, 3L);
		lista.remove(0);
		lista.add(0, 4L);
		lista.add(1, 1L);
		lista.remove(1);
		lista.add(1, 1L);
		lista.add(2, 1L);
		System.out.println(lista);
		assertEquals(lista.size(), 3);
		
	}

}
