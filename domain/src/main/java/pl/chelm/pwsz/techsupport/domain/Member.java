package pl.chelm.pwsz.techsupport.domain;

/* TODO: Implement caching for members. */
/* TODO: Implement persistency for members. */
/* TODO: Implement basic authenticaton for members. */

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
		return datasource.read(id.intValue( ));
	}

	private static Data readFromDatabase (String name, String password)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.read(name, password);
	}

	private static int writeToDatabase (String name, String password)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.create(name, password);
	}

	private static Member newInstance (String name, String password)
	{
		int id = Member.writeToDatabase(name, password);
		Member member = new Member (id, name);
		Member.cache(member);
		return member;
	}

	public static Member getInstance (String name, String password)
	{
		Data data = Member.readFromDatabase(name, password);
		if (data != null)
		{
			Identificator<Member> id = new Identificator<Member> (data.<Integer>get(Integer.class, "id"));
			Member member = Member.readFromCache(id);
			if (member != null)
			{
				return member;
			}
			member = new Member (id, name);
			Member.cache(member);
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

	private Member (int id, String name)
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