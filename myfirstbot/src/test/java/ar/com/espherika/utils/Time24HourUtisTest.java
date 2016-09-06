package ar.com.espherika.utils;

import  static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;




public class Time24HourUtisTest {

	
	@Test
	public void testIncreas() throws ParseException{
		String hora=Time24HoursUtils.increase("01:10", 1);
		
		assertEquals("02:10", hora);
		
	}
}
