package pl.chelm.pwsz.techsupport.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.Identificator;

final class TagAssignation
extends Object
{
	private final static int QUANTITY_OF_DIGITS_IN_HASH_CODE          = 100000;
	private final static int QUANTITY_OF_DIGITS_IN_ASSIGNER_HASH_CODE = 10000;
	private final static int QUANTITY_OF_DIGITS_IN_ISSUE_HASH_CODE    = 1000;
	private final static int QUANTITY_OF_DIGITS_IN_TAG_HASH_CODE      = 100;

	private static void writeAssignationToDatabase (Identificator<Member> idOfAssigner, Identificator<Issue> idOfIssue, Identificator<Tag> idOfTag)
	{
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		datasource.assignTag(idOfIssue.intValue( ), idOfAssigner.intValue( ), idOfTag.intValue( ));
	}

	private static void writeUnassignationToDatabase (Identificator<Member> idOfAssigner, Identificator<Issue> idOfIssue, Identificator<Tag> idOfTag)
	{
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		datasource.unassignTag(idOfIssue.intValue( ), idOfAssigner.intValue( ), idOfTag.intValue( ));
	}

	public static TagAssignation newInstance (Member assigner, Issue issueToAssignTagTo, Tag tagToBeAssigned)
	{
		try
		{
			TagAssignation.writeAssignationToDatabase(assigner.getId( ), issueToAssignTagTo.getId( ), tagToBeAssigned.getId( ));
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
		TagAssignation tagAssignation = new TagAssignation (assigner, issueToAssignTagTo, tagToBeAssigned);
		return tagAssignation;
	}

	public static void destroyInstance (Member unassigner, Issue issue, Tag tag)
	{
		TagAssignation.writeUnassignationToDatabase(unassigner.getId( ), issue.getId( ), tag.getId( ));
	}

	public static Set<Tag> getTagsAssignedTo (Issue issue)
	{
		TagDatasource datasource = DatasourceFactory.<TagDatasource>getInstance(TagDatasource.class);
		Collection<Data> collectionOfData = datasource.readAssignedTo(issue.getId( ).intValue( ));
		if (collectionOfData == null)
		{
			return Collections.<Tag>emptySet( );
		}
		Set<Tag> setOfTags = new HashSet<Tag> ( );
		for (Data data : collectionOfData)
		{
			Tag tag = Tag.getInstance(data);
			setOfTags.add(tag);
		}
		return Collections.<Tag>unmodifiableSet(setOfTags);
	}

	private TagAssignation (Member assigner, Issue issueToAssignTagTo, Tag tagToBeAssigned)
	{
		this.assigner   = assigner;
		this.issue      = issueToAssignTagTo;
		this.tag        = tagToBeAssigned;
		this.hashCode = 
			TagAssignation.QUANTITY_OF_DIGITS_IN_HASH_CODE 
			+ TagAssignation.QUANTITY_OF_DIGITS_IN_ASSIGNER_HASH_CODE * assigner.hashCode( ) 
			+ TagAssignation.QUANTITY_OF_DIGITS_IN_ISSUE_HASH_CODE * issue.hashCode( ) 
			+ TagAssignation.QUANTITY_OF_DIGITS_IN_TAG_HASH_CODE * tag.hashCode( )
		;
	}

	private final Member assigner;

	public Member getAssigner ( )
	{
		return this.assigner;
	}

	private final Issue issue;

	public Issue getIssue ( )
	{
		return this.issue;
	}

	private final Tag tag;

	public Tag getTag ( )
	{
		return this.tag;
	}

	@Override
	public boolean equals (Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (o instanceof TagAssignation)
		{
			if (o == this)
			{
				return true;
			}
			return o.hashCode( ) == this.hashCode( );
		}
		return false;
	}

	private final int hashCode;

	@Override
	public int hashCode ( )
	{
		return this.hashCode;
	}
}