package pl.chelm.pwsz.techsupport.domain;

public final class AccessRestrictedException 
extends RuntimeException
{
	public AccessRestrictedException (Member whoAttemptsToPerformAction, Action requiredAction)
	{
		super(whoAttemptsToPerformAction.getName( ) + " tried to perform unpermitted action.");
		this.culprit = whoAttemptsToPerformAction;
		this.restrictedAction = requiredAction;
	}

	private final Member culprit;

	public Member getCulprit ( )
	{
		return this.culprit;
	}

	private final Action restrictedAction;

	public Action getRestrictedAction ( )
	{
		return this.restrictedAction;
	}
}