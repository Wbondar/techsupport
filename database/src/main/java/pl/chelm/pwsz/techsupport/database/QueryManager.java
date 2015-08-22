package pl.chelm.pwsz.techsupport.database;

import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;

interface QueryManager
{
	public abstract CallableStatement prepareCall (String sql)
	throws SQLException;

	public abstract PreparedStatement prepareStatement (String sql)
	throws SQLException;
}