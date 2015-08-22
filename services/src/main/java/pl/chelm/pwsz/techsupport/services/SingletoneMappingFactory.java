package pl.chelm.pwsz.techsupport.services;

public final class SingletoneMappingFactory
{
	public static SingletoneMapping newInstance ( )
	{
		return new NativeSingletoneMapping ( );
	}

	private SingletoneMappingFactory ( ) {}
}