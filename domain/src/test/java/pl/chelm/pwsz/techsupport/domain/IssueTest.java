package pl.chelm.pwsz.techsupport.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import pl.chelm.pwsz.techsupport.services.Identificator;
import pl.chelm.pwsz.techsupport.services.RandomString;

/**
 * Unit test for pl.chelm.pwsz.techsupport.domain.Issue.
 */
public class IssueTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */

    private static final String USERNAME = RandomString.getInstance(9);
    private static final String PASSWORD = RandomString.getInstance(36);

    public IssueTest( String testName )
    throws Exception
    {
        super( testName );
        Class.forName("com.mysql.jdbc.Driver");
        Member member = Member.getInstance(IssueTest.USERNAME, IssueTest.PASSWORD);
        if (member == null)
        {
            member = Member.newInstance(IssueTest.USERNAME, IssueTest.PASSWORD);
        }
        this.issuer = member;
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( IssueTest.class );
    }

 
    private final Member issuer;

    protected void setUp ( )
    throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
    }

    protected void tearDown ( ) throws Exception {}

    private final String title = "Title of a test issue.";
    private final String message = "Content of a test issue.";

    public void testIssueInstantiation ( )
    {       
        Issue issue = Issue.newInstance(this.issuer, this.title, this.message);

        assertEquals(issue.getTitle( ), this.title);
        assertEquals(issue.getMessage( ), this.message);
        assertEquals(issue.getIssuer( ), this.issuer);

        Identificator<Issue> id = issue.getId( );
        Issue retrievedIssue = Issue.getInstance(id);

        assertEquals(issue, retrievedIssue);
    }

    private final String titleOfManipulatedIssue = "Manipulate my tags.";
    private final String messageOfManipulatedIssue = "Description of a manipulation.";

    private final String titleOfTag = "Manipulate me.";

    public void testIssueTagManipulation ( )
    {
        Issue issue = Issue.newInstance(this.issuer, this.titleOfManipulatedIssue, this.messageOfManipulatedIssue);
        Tag tag = Tag.getInstance(this.titleOfTag);
        issue.assignTag(this.issuer, tag);
        assertTrue(issue.containsTag(tag));
        issue.unassignTag(this.issuer, tag);
        assertFalse(issue.containsTag(tag));
    }

    public void testCommentingOnIssue ( )
    {
        Issue issue = Issue.newInstance(this.issuer, "Comment on me.", "Comment on me.");
        Comment comment = issue.comment(this.issuer, "I commented on you.");
        assertTrue(issue.getComments( ).contains(comment));
    }
}
