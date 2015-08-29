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
	private static Data readFromDatabase (Identificator<Comment> id)
	{
		CommentDatasource datasource = DatasourceFactory.<CommentDatasource>getInstance(CommentDatasource.class);
		return datasource.read(id.longValue( ));
	}

	private static Comment readFromCache (Identificator<Comment> id)
	{
		Cache<Comment> cache = CacheFactory.<Comment>getInstance(Comment.class);
		return cache.get(id);
	}

	public static Comment getInstance (Identificator<Comment> id)
	{
		Comment comment = Comment.readFromCache(id);
		if (comment == null)
		{
			Data data = Comment.readFromDatabase(id);
			if (data == null)
			{
				return null;
			}
			Comment parent = null;
			Issue issueToCommentOn = null;
			Identificator<Comment> idOfCommentToRespondeTo = new Identificator<Comment> (data.<Long>get(Long.class, "parent_id"));
			if (idOfCommentToRespondeTo != null)
			{
				parent = Comment.getInstance(idOfCommentToRespondeTo);
			} else {
				Identificator<Issue> idOfIssueToCommentOn = new Identificator<Issue> (data.<Long>get(Long.class, "issue_id"));
				issueToCommentOn = Issue.getInstance(idOfIssueToCommentOn);
			}
			Date when = data.<Date>get(Date.class, "posted_at");
			Identificator<Member> idOfAuthor = new Identificator<Member> (data.<Long>get(Long.class, "author_id"));
			Member author = Member.getInstance(idOfAuthor);
			String content = data.<String>get(String.class, "content");
			if (parent == null)
			{
				comment = new Comment (id, when, issueToCommentOn, author, content);
			} else {
				comment = new Comment (id, when, parent, author, content);
			}		
			Comment.cache(comment);
		}
		return comment;
	}

	private static Data writeToDatabase (Identificator<Issue> idOfIssue, Identificator<Member> idOfAuthor, String content)
	{
		CommentDatasource datasource = DatasourceFactory.<CommentDatasource>getInstance(CommentDatasource.class);
		return datasource.create(idOfIssue.longValue( ), idOfAuthor.longValue( ), content);
	}

	private static void cache (Comment commentToBeCached)
	{
		Cache<Comment> cache = CacheFactory.<Comment>getInstance(Comment.class);
		cache.put(commentToBeCached);
	}

	public static Comment newInstance (Issue issueToCommentOn, Member author, String content)
	{
		Data data = Comment.writeToDatabase(issueToCommentOn.getId( ), author.getId( ), content);
		Comment comment = Comment.getInstance(data);
		return comment;
	}

	private static Data writeResponseToDatabase (Identificator<Comment> idOfParent, Identificator<Member> idOfAuthor, String content)
	{
		CommentDatasource datasource = DatasourceFactory.<CommentDatasource>getInstance(CommentDatasource.class);
		return datasource.createResponse(idOfParent.longValue( ), idOfAuthor.longValue( ), content);
	}

	public static Comment newInstance (Comment parent, Member author, String content)
	{
		Data data = Comment.writeResponseToDatabase(parent.getId( ), author.getId( ), content);
		Comment comment = Comment.getInstance(data);
		return comment;
	}

	public static Comment getInstance (Data data)
	{
		if (data == null)
		{
			throw new RuntimeException ("Data is missing.");
		}
		Long idValue = data.<Long>get(Long.class, "id");
		Identificator<Comment> id = new Identificator<Comment> (idValue);

		Comment comment = Comment.readFromCache(id);
		if (comment != null)
		{
			return comment;
		}

		Long authorIdValue = data.<Long>get(Long.class, "author_id");
		Identificator<Member> authorId = new Identificator<Member> (authorIdValue);
		Member author = Member.getInstance(authorId);
		if (author == null)
		{
			throw new RuntimeException ("Author is missing.");
		}
		Date when = data.<Date>get(Date.class, "posted_at");
		if (when == null)
		{
			throw new RuntimeException ("Publication date is missing.");
		}
		String content = data.<String>get(String.class, "content");
		if (content == null)
		{
			throw new RuntimeException ("Content of a comment is missing.");
		}

		Comment parent = null;
		Long parentIdValue = data.<Long>get(Long.class, "parent_id");
		if (parentIdValue != null && parentIdValue > 0)
		{
			Identificator<Comment> parentId = new Identificator<Comment> (parentIdValue);
			parent = Comment.getInstance(parentId);
		}
		if (parent != null)
		{
			comment = new Comment (id, when, parent, author, content);
			Comment.cache(comment);
		} else {
			Long issueIdValue = data.<Long>get(Long.class, "issue_id");
			Identificator<Issue> issueId = new Identificator<Issue> (issueIdValue);
			Issue issue = Issue.getInstance(issueId);
			if (issue == null)
			{
				throw new RuntimeException ("Issue is missing.");
			}
			comment = new Comment (id, when, issue, author, content);
			Comment.cache(comment);
		}
		return comment;
	}

	public static Set<Comment> getInstance (Issue issue)
	{
		CommentDatasource datasource = DatasourceFactory.<CommentDatasource>getInstance(CommentDatasource.class);
		Collection<Data> collectionOfData = datasource.readIssue(issue.getId( ).longValue( ));
		if (collectionOfData == null)
		{
			return null;
		}
		Set<Comment> setOfComments = new HashSet<Comment> ( );
		for (Data data : collectionOfData)
		{
			Comment comment = Comment.getInstance(data);
			setOfComments.add(comment);
		}
		return Collections.<Comment>unmodifiableSet(setOfComments);
	}

	private Comment 
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

	private Comment 
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