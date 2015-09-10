package pl.chelm.pwsz.techsupport.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

final class CommentFactory
extends CacheFactory<Comment, CommentDatasource>
{
	CommentFactory (Class<Comment> entitiesType, Class<CommentDatasource> datasourceType)
	{
		super(entitiesType, datasourceType);
	}

	CommentFactory ( )
	{
		this(Comment.class, CommentDatasource.class);
	}

	/*public Comment getInstance (Identificator<Comment> id)
	{
		Comment comment = this.readFromCache(id);
		if (comment == null)
		{
			Data data = this.readFromDatabase(id);
			if (data == null)
			{
				return null;
			}
			Comment parent = null;
			Issue issueToCommentOn = null;
			Identificator<Comment> idOfCommentToRespondeTo = new Identificator<Comment> (data.<Long>get(Long.class, "parent_id"));
			if (idOfCommentToRespondeTo != null)
			{
				parent = this.getInstance(idOfCommentToRespondeTo);
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
			this.cache(comment);
		}
		return comment;
	}*/

	private Data writeToDatabase (Identificator<Issue> idOfIssue, Identificator<Member> idOfAuthor, String content)
	{
		CommentDatasource datasource = this.getDefaultDatasource( );
		return datasource.create(idOfIssue.longValue( ), idOfAuthor.longValue( ), content);
	}

	public Comment newInstance (Issue issueToCommentOn, Member author, String content)
	{
		Data data = this.writeToDatabase(issueToCommentOn.getId( ), author.getId( ), content);
		Comment comment = this.getInstance(data);
		return comment;
	}

	private Data writeResponseToDatabase (Identificator<Comment> idOfParent, Identificator<Member> idOfAuthor, String content)
	{
		CommentDatasource datasource = this.getDefaultDatasource( );
		return datasource.createResponse(idOfParent.longValue( ), idOfAuthor.longValue( ), content);
	}

	public Comment newInstance (Comment parent, Member author, String content)
	{
		Data data = this.writeResponseToDatabase(parent.getId( ), author.getId( ), content);
		Comment comment = this.getInstance(data);
		return comment;
	}

	public Comment getInstance (Data data)
	{
		if (data == null)
		{
			throw new RuntimeException ("Data is missing.");
		}
		Long idValue = data.<Long>get(Long.class, "id");
		Identificator<Comment> id = new Identificator<Comment> (idValue);

		Comment comment = this.readFromCache(id);
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
			parent = this.getInstance(parentId);
		}
		if (parent != null)
		{
			comment = new Comment (id, when, parent, author, content);
			this.cache(comment);
		} else {
			Long issueIdValue = data.<Long>get(Long.class, "issue_id");
			Identificator<Issue> issueId = new Identificator<Issue> (issueIdValue);
			Issue issue = Issue.getInstance(issueId);
			if (issue == null)
			{
				throw new RuntimeException ("Issue is missing.");
			}
			comment = new Comment (id, when, issue, author, content);
			this.cache(comment);
		}
		return comment;
	}

	public Set<Comment> getInstances (Issue issue)
	{
		CommentDatasource datasource = this.getDefaultDatasource( );
		Collection<Data> collectionOfData = datasource.readIssue(issue.getId( ).longValue( ));
		if (collectionOfData == null)
		{
			return null;
		}
		Set<Comment> setOfComments = new HashSet<Comment> ( );
		for (Data data : collectionOfData)
		{
			Comment comment = this.getInstance(data);
			setOfComments.add(comment);
		}
		return Collections.<Comment>unmodifiableSet(setOfComments);
	}
}