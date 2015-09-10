package pl.chelm.pwsz.techsupport.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

final class IssueFactory
extends CacheFactory<Issue, IssueDatasource>
{
	IssueFactory (Class<Issue> entitiesType, Class<IssueDatasource> datasourceType)
	{
		super(entitiesType, datasourceType);
	}

	IssueFactory ( )
	{
		this(Issue.class, IssueDatasource.class);
	}

	private long writeToDatabase (Member issuer, String title, String message)
	{
		IssueDatasource datasource = this.getDefaultDatasource( );
		return datasource.create(issuer.getId( ).longValue( ), title, message);
	}

	public Issue newInstance (Member issuer, String title, String message)
	{
		long id = this.writeToDatabase(issuer, title, message);
		Issue issue = new Issue (id, issuer, title, message);
		this.cache(issue);
		return issue;
	}

	private Collection<Data> readFromDatabaseByIssuer (Identificator<Member> idOfIssuer)
	{
		IssueDatasource datasource = this.getDefaultDatasource( );
		return datasource.readByIssuer (idOfIssuer.longValue( ));
	}

 	Issue getInstance (Data data)
	{
		Long idOfIssueValue = data.<Long>get(Long.class, "id");
		if (idOfIssueValue == null)
		{
			throw new RuntimeException ("Data is malformed.");
		}
		Identificator<Issue> idOfIssue = new Identificator<Issue> (idOfIssueValue);
		Issue issue = this.readFromCache(idOfIssue);
		if (issue == null)
		{
			Identificator<Member> idOfIssuer = new Identificator<Member> (data.<Long>get(Long.class, "issuer_id"));;
			Member issuer = Member.getInstance(idOfIssuer);
			String title = data.<String>get(String.class, "title");
			String message = data.<String>get(String.class, "message");
			issue = new Issue (idOfIssue, issuer, title, message);
			this.cache(issue);
		}
		return issue;
	}

	public Set<Issue> getInstances (Member issuer)
	{
		Collection<Data> collectionOfData = this.readFromDatabaseByIssuer (issuer.getId( ));
		if (collectionOfData == null)
		{
			return null;
		}
		Set<Issue> setOfIssues = new HashSet<Issue> ( );
		for (Data data : collectionOfData)
		{
			Issue issue = this.getInstance(data);
			if (issue != null)
			{
				setOfIssues.add(issue);
			}
		}
		return Collections.<Issue>unmodifiableSet(setOfIssues);
	}
}