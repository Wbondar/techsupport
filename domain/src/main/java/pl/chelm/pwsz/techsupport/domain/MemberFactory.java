package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

final class MemberFactory
extends CacheFactory<Member, MemberDatasource>
{
	MemberFactory (Class<Member> entitiesType, Class<MemberDatasource> datasourceType)
	{
		super(entitiesType, datasourceType);
	}

	MemberFactory ( )
	{
		this(Member.class, MemberDatasource.class);
	}

	private Data readFromDatabase (String name, String password)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.read(name, password);
	}

	private long writeToDatabase (String name, String password)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.create(name, password);
	}

	public Member newInstance (String name, String password)
	{
		long id = this.writeToDatabase(name, password);
		if (id < 1)
		{
			return null;
		}
		Member member = new Member (id, name);
		this.cache(member);
		return member;
	}

	private Member getInstance (Data data)
	{
		Identificator<Member> id = new Identificator<Member> (data.<Long>get(Long.class, "id"));
		Member member = this.readFromCache(id);
		if (member == null)
		{
			String name = data.<String>get(String.class, "name");
			member = new Member (id, name);
			this.cache(member);			
		}
		return member;
	}

	private Data readFromDatabase (String name)
	{
		MemberDatasource datasource = DatasourceFactory.<MemberDatasource>getInstance(MemberDatasource.class);
		return datasource.read(name);
	}

	public Member getInstance (String name)
	{
		Data data = this.readFromDatabase(name);
		if (data == null)
		{
			return null;
		}
		return this.getInstance(data);
	}

	public Member getInstance (String name, String password)
	{
		Data data = this.readFromDatabase(name, password);
		if (data == null)
		{
			return null;
		}
		return this.getInstance(data);
	}
}