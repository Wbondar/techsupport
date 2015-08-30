package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class PolishLocaleEnforcer
extends Object
implements Filter
{
	private final static Locale LOCALE = new Locale ("pl");

	@Override
	public void init (FilterConfig config)
	throws ServletException
	{
		
	}

	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
	throws ServletException, IOException
	{
		response.setLocale(PolishLocaleEnforcer.LOCALE);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy ( ) { }
}