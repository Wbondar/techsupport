package pl.chelm.pwsz.techsupport.web;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public final class MemberCreateTest
extends TestCase
{
    protected void setUp ( ) 
    throws Exception
    {
        setBaseUrl("http://localhost:8080");
    }

    protected void tearDown ( ) 
    throws Exception
    {
    }

    public void testSignUp ( ) {
        beginAt("/index.jsp");
        assertFormPresent("sign_up");
        setTextField("username", "test");
        setTextField("password", "test123");
        submit( );
        assertResponseCode(201);
    }
}