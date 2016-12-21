package com.bank.account;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes={ApplicationTestConfiguration.class})
public abstract class AccountBaseTest extends AbstractJUnit4SpringContextTests {
	
	public static final double DOUBLE_DELTA = 0d;

}