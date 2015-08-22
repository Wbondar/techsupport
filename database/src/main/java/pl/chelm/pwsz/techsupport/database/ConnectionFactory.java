package pl.chelm.pwsz.techsupport.database;

import java.sql.Connection;
import java.sql.SQLException;

interface ConnectionFactory
{
	public abstract Connection getConnection ( )
	throws SQLException;
}