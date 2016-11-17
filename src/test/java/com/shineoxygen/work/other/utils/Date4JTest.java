package com.shineoxygen.work.other.utils;

import org.junit.Test;

import hirondelle.date4j.DateTime;

public class Date4JTest {
	@Test
	public void compareDate() {
		DateTime dateAndTime2 = new DateTime("2010-11-19 23:59:59");
		DateTime dateAndTime = new DateTime("2010-01-19 23:59:59");
		System.out.println(dateAndTime2.compareTo(dateAndTime));
	}
}
