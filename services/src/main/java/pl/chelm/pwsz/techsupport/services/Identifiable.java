package pl.chelm.pwsz.techsupport.services;

public interface Identifiable<T>
{
	public abstract Identificator<T> getId ( );
}