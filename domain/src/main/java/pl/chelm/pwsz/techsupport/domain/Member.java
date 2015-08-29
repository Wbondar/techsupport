package pl.chelm.pwsz.techsupport.domain;

import java.util.Set;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Member
extends Object
implements Identifiable<Member>
{
	private static void cache (Member member) 
	{
		Cache<Member> cache = CacheFactory.<Member>getInstance(Member.class);
		cache.put(member);
	}

	private static Member readFromCache (Identificator<Member> id)
	{
		Cache<Member> cache = CacheFactory.<Member>getInstance(Member.class);
		return cache.get(id);
	}

	private static Data readFromDatabase (Identificator<Member> id)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.read(id.longValue( ));
	}

	private static Data readFromDatabase (String name, String password)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.read(name, password);
	}

	private static long writeToDatabase (String name, String password)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.create(name, password);
	}

	private static Member newInstance (String name, String password)
	{
		Member member = null;
		try
		{
			long id = Member.writeToDatabase(name, password);
			member = new Member (id, name);
			Member.cache(member);
		} catch (Exception e) {
			return null;
		}
		return member;
	}

	public static Member getInstance (String name, String password)
	{
		Data data = Member.readFromDatabase(name, password);
		if (data != null)
		{
			Identificator<Member> id = new Identificator<Member> (data.<Long>get(Long.class, "id"));
			Member member = Member.readFromCache(id);
			if (member == null)
			{
				member = new Member (id, name);
				Member.cache(member);
			}
			return member;
		}
		return Member.newInstance(name, password);
	}
	
	public static Member getInstance (Identificator<Member> id)
	{
		Member member = Member.readFromCache(id);
		if (member == null)
		{
			Data data = Member.readFromDatabase(id);
			if (data == null)
			{
				return null;
			}
			member = new Member (id, data.<String>get(String.class, "name"));
			Member.cache(member);
		}
		return member;
	}

	private final String name;

	private Member (long id, String name)
	{
		this(new Identificator<Member> (id), name);
	}

	private Member (Identificator<Member> id, String name)
	{
		this.id   = id;
		this.name = name;
	}

	public String getName ( )
	{
		return this.name;
	}

	public Set<Issue> getIssues ( )
	{
		return Issue.getInstance(this);
	}

	private final Identificator<Member> id;

	public Identificator<Member> getId ( )
	{
		return this.id;
	}

	@Override
	public int hashCode ( )
	{
		return this.id.intValue( );
	}
}