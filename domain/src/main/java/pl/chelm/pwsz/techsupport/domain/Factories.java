package pl.chelm.pwsz.techsupport.domain;

import java.lang.reflect.Constructor;

import pl.chelm.pwsz.techsupport.services.*;

public final class Factories
extends Object
{
	private static final SingletoneMapping mapping = SingletoneMappingFactory.newInstance ( );

	public static <T extends Object & Factory> T getInstance (Class<T> typeOfAFactory)
	{
		T factory = Factories.mapping.<T>get(typeOfAFactory);
		if (factory == null)
		{
			try
			{
				Constructor<T> constructor = typeOfAFactory.getDeclaredConstructor( );
				factory = constructor.newInstance( );
				Factories.mapping.<T>put(typeOfAFactory, factory);
			} catch (Exception e) {
				throw new FactoryException ("Failed to get factory.", e);
			}
		}
		return factory;
	}

	private Factories ( ) {}
}