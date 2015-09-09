package pl.chelm.pwsz.techsupport.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Comment
extends Object
implements Identifiable<Comment>
{
	Comment 
	(
		  Identificator<Comment> id
		, Date                   when
		, Comment                parent
		, Member                 author
		, String                 content
	)
	{
		this.id             = id;
		this.commentedIssue = parent.getIssue( );
		this.when           = when;
		this.author         = author;
		this.content        = content;
		this.parent         = parent;
	}

	Comment 
	(
		  Identificator<Comment> id
		, Date                   when
		, Issue                  commentedIssue
		, Member                 author
		, String                 content
	)
	{
		this.id             = id;
		this.commentedIssue = commentedIssue;
		this.when           = when;
		this.author         = author;
		this.content        = content;
		this.parent         = null;
	}

	private final Identificator<Comment> id;

	@Override
	public Identificator<Comment> getId ( )
	{
		return this.id;
	}

	private final Issue commentedIssue;

	public Issue getIssue ( )
	{
		return this.commentedIssue;
	}

	private final Date when;

	public Date getDatePosted ( )
	{
		return this.when;
	}

	private final Member author;

	public Member getAuthor ( )
	{
		return this.author;
	}

	private final String content; 

	public String getContent ( )
	{
		return this.content;
	}

	private final Comment parent;

	public Comment getParent ( )
	{
		return this.parent;
	}

	public Comment responde (Member responder, String message)
	{
		return Comment.newInstance(this, responder, message);
	}
}