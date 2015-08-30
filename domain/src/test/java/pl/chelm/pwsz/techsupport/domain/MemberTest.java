package pl.chelm.pwsz.techsupport.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import pl.chelm.pwsz.techsupport.services.Identificator;
import pl.chelm.pwsz.techsupport.services.RandomString;

/**
 * Unit test for pl.chelm.pwsz.techsupport.domain.Member.
 */
public class MemberTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MemberTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MemberTest.class );
    }

    protected void setUp ( )
    throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
    }

    protected void tearDown ( ) throws Exception {}

    private final String username = RandomString.getInstance(9);
    private final String password = RandomString.getInstance(36);

    public void testAuthentication ( )
    {
        String correctPassword = this.password;
        String wrongPassword = this.password + "lol";
        Member member = Member.newInstance(this.username, correctPassword);
        assertTrue (member != null);
        member = Member.getInstance(this.username, correctPassword);
        assertTrue (member != null);
        member = Member.getInstance(this.username, wrongPassword);
        assertTrue (member == null);
    }
}
