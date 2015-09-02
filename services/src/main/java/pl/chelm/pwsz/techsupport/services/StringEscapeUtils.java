package pl.chelm.pwsz.techsupport.services;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.MySQLCodec.Mode;

/* TODO: Implement proper SQL and HTML encoding (escaping). */

public final class StringEscapeUtils
extends Object
{
	public static String escapeHtml4 (String input)
	{
		if (input == null)
		{
			return null;
		}
		return input;
		//return ESAPI.encoder( ).encodeForHTML(input);
	}

	private static final Codec CODEC = new MySQLCodec(MySQLCodec.Mode.STANDARD);

	public static String escapeSql (String input)
	{
		if (input == null)
		{
			return null;
		}
		return input;
		//return ESAPI.encoder( ).encodeForSQL(CODEC, input);
	}
}