package pl.chelm.pwsz.techsupport.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import pl.chelm.pwsz.techsupport.database.Data;
import pl.chelm.pwsz.techsupport.database.DatasourceFactory;
import pl.chelm.pwsz.techsupport.database.IssueDatasource;

import static pl.chelm.pwsz.techsupport.services.StringEscapeUtils.escapeSql;

public final class IssueSearcher
extends Object
{
	public static IssueSearcher getInstance ( )
	{
		return new IssueSearcher ( );
	}

	private IssueSearcher ( )
	{

	}

	private Date after = null;

	public void issuedAfter (Date givenDate)
	{
		if (givenDate == null)
		{
			return;
		}
		if (this.before != null)
		{
			if (givenDate.after(this.before))
			{
				this.after = this.before;
				this.before = givenDate;
				return;
			}
		}
		this.after = givenDate;
	}

	private Date before = null;

	public void issuedBefore (Date givenDate)
	{
		if (givenDate == null)
		{
			return;
		}
		if (this.after != null)
		{
			if (givenDate.before(this.after))
			{
				this.before = this.after;
				this.after = givenDate;
				return;
			}
		}
		this.before = givenDate;
	}

	private Set<String> mentionings = new HashSet<String> ( );

	public void mentions (String stringToSearch)
	{
		if (!stringToSearch.isEmpty( ) && stringToSearch != null)
		{
			this.mentionings.add(escapeSql(stringToSearch));
		}
	}

	private Set<Tag> containsTags = new HashSet<Tag> ( );

	public void containsTag (Tag tagToSearch)
	{
		this.containsTags.add(tagToSearch);
	}

	private Member issuer = null;

	public void issuedBy (Member issuer)
	{
		this.issuer = issuer;
	}

	private boolean conjunction = true;

	public void any ( )
	{
		this.conjunction = false;
	}

	public void all ( )
	{
		this.conjunction = true;
	}

	public Collection<Issue> search ( )
	{
		List<String> predicates = new ArrayList<String> ( );
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (this.after == null && this.before != null)
		{
			predicates.add(" view_issue.created_at <= '" + dateFormat.format(this.before) + "'");
		} else if (this.after == null && this.before == null) {
			/* Do nothing. */
		} else if (this.after != null && this.before != null) {
			predicates.add(" view_issue.created_at BETWEEN '" + dateFormat.format(this.after) + "' AND '" + dateFormat.format(this.before) + "'");
		} else if (this.after != null && this.before == null) {
			predicates.add(" view_issue.created_at >= '" + dateFormat.format(this.after) + "'");
		}
		if (this.mentionings != null && !this.mentionings.isEmpty( ))
		{
			StringBuilder stringBuilder = new StringBuilder ( );
			int quantityOfMentionings = 0;
			for (String mentioning : this.mentionings)
			{
				stringBuilder.append(" view_issue.title LIKE '%" + mentioning + "%' ");
				quantityOfMentionings++;
				if (quantityOfMentionings < this.mentionings.size( ))
				{
					stringBuilder.append("OR ");
				}
			}
			/*stringBuilder.append(" ESCAPE '!' ");*/
			predicates.add(stringBuilder.toString( ));
		}
		if (this.issuer != null)
		{
			predicates.add(" view_issue.issuer_id = " + this.issuer.getId( ).toString( ) + " ");
		}
		StringBuilder queryBuilder = new StringBuilder ("SELECT DISTINCT view_issue.* FROM view_issue ");
		if (this.containsTags != null && !this.containsTags.isEmpty( ))
		{
			queryBuilder.append("JOIN view_tag_usage ON view_issue.id = view_tag_usage.issue_id ");
			StringBuilder stringBuilder = new StringBuilder ( );
			stringBuilder.append(" view_tag_usage.id IN (");
			int quantityOfTags = 0;
			for (Tag tag : this.containsTags)
			{
				stringBuilder.append(tag.getId( ));
				quantityOfTags++;
				if (quantityOfTags < this.containsTags.size( ))
				{
					stringBuilder.append(", ");
				}
			}
			stringBuilder.append(")");
			predicates.add(stringBuilder.toString( ));
		}
		if (!predicates.isEmpty( ))
		{
			String operation;
			if (this.conjunction)
			{
				operation = " AND ";
			} else {
				operation = " OR ";
			}
			int quantityOfChunks = 0;
			queryBuilder.append(" WHERE ");
			for (String chunkOfPredicate : predicates)
			{
				queryBuilder.append(chunkOfPredicate);
				quantityOfChunks++;
				if (quantityOfChunks < predicates.size( ))
				{
					queryBuilder.append(operation);
				}
			}
		}
		queryBuilder.append(";");
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		Collection<Data> collectionOfData = datasource.read(queryBuilder.toString( ));
		if (collectionOfData == null)
		{
			return null;
		}
		List<Issue> issues = new ArrayList<Issue> ( );
		for (Data data : collectionOfData)
		{
			Issue issue = Issue.getInstance(data);
			issues.add(issue);
		}
		return (Collection<Issue>)issues;
	}
}