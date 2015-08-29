package pl.chelm.pwsz.techsupport.web;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

import pl.chelm.pwsz.techsupport.services.RandomString;

public final class IssueTest
extends TestCase
{
    private static final String USERNAME = RandomString.getInstance(10);
    private static final String PASSWORD = RandomString.getInstance(10);

    protected void setUp ( ) 
    throws Exception
    {
        this.titleOfIssue = RandomString.getInstance(10);
        this.messageOfIssue = RandomString.getInstance(100);
        this.titleOfTag = RandomString.getInstance(10);
        this.messageOfComment = RandomString.getInstance(100);
        setBaseUrl("http://localhost:8080");
        beginAt("/");
        assertFormPresent("sign_up");
        setTextField("username", IssueTest.USERNAME);
        setTextField("password", IssueTest.PASSWORD);
        submit( );
    }

    protected void tearDown ( ) 
    throws Exception
    {
        beginAt("/");
        assertFormPresent("exit");
        submit( );
    }

    private String titleOfIssue = null;
    private String messageOfIssue = null;
    private String titleOfTag = null;
    private String messageOfComment = null;

    public void testPerform ( ) 
    {
        logIn ( );
        create ( );
        read ( );
        assignTag ( );
        unassignTag ( );
        comment ( );
    }

    void logIn ( )
    {
        beginAt("/");
        assertFormPresent("log_in");
        setTextField("username", IssueTest.USERNAME);
        setTextField("password", IssueTest.PASSWORD);
        submit( );
        assertResponseCode(200);
    }

    void create ( )
    {
        beginAt("/");
        assertFormPresent("issue_create");
        setTextField("title", this.titleOfIssue);
        setTextField("message", this.messageOfIssue);
        submit( );
        assertResponseCode(201);
    }

    void read ( )
    {
        beginAt ("/");
        assertLinkPresentWithExactText(this.titleOfIssue);
        clickLinkWithExactText(this.titleOfIssue);
        assertResponseCode(200);
    }

    void assignTag ( )
    {
        assertFormPresent("tag_assign");
        setTextField("tag_title", this.titleOfTag);
        submit ( );
        assertResponseCode(200);
    }

    void unassignTag ( )
    {
        assertFormPresent(this.titleOfTag.toLowerCase( ).replaceAll("\\s+","") + "_unassign");
        submit ( );
        assertResponseCode(200);
    }

    void comment ( )
    {
        assertFormPresent("comment");
        setTextField("message", this.messageOfComment);
        submit ( );
        assertResponseCode(201);
    }
}