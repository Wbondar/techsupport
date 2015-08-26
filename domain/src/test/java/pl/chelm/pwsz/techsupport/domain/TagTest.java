package pl.chelm.pwsz.techsupport.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import pl.chelm.pwsz.techsupport.services.Identificator;

/**
 * Unit test for pl.chelm.pwsz.techsupport.domain.Tag.
 */
public class TagTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TagTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TagTest.class );
    }

    protected void setUp ( )
    throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
    }

    protected void tearDown ( ) throws Exception {}

    private final String titleOfATagExpected = "Test my instantiation.";

    public void testTagInstantiation ( )
    {
        Tag tag = Tag.getInstance(this.titleOfATagExpected);
        assertEquals(tag.getTitle(), this.titleOfATagExpected);
    }

    private final String titleOfATagToRetrieveByTitle = "Retrieve me by title.";

    public void testTagRetrievingByTitle ( )
    {
        Tag tag = Tag.getInstance(this.titleOfATagToRetrieveByTitle);
        Tag retrievedTag = Tag.getInstance(this.titleOfATagToRetrieveByTitle);
        assertEquals(tag, retrievedTag);
    }

    private final String titleOfATagToRetrieveById = "Retrieve me by ID.";

    public void testTagRetrievingById ( )
    {
        Tag tag = Tag.getInstance(this.titleOfATagToRetrieveById);
        Identificator<Tag> id = tag.getId( );
        Tag retrievedTag = Tag.getInstance(id);
        assertEquals(tag, retrievedTag);
    }
}
