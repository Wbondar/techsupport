package pl.chelm.pwsz.techsupport.services;

import de.rtner.security.auth.spi.SimplePBKDF2;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple PasswordHandler.
 */
public class PasswordHandlerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PasswordHandlerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PasswordHandlerTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testPasswordHandler ( )
    {
        String myPassword = "myPassword";
        String myHash = PasswordHandler.hash(myPassword);
        boolean success = PasswordHandler.validate(myPassword, myHash);
        assertTrue(success);
    }
}
