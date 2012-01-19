/**
 * 
 */
package it.freax.fpm.test.main;

import it.freax.fpm.compiler.ebnf.Ebnf;
import it.freax.fpm.util.ErrorHandler;
import it.freax.fpm.util.Streams;
import it.freax.fpm.util.exceptions.ParseException;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author kLeZ-hAcK
 * 
 */
public class TestTree
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Streams str = Streams.getOne("conf/java.ebnf");
		try
		{
			InputStream is = str.getResource();
			if (is != null)
			{
				String content = str.read();
				Ebnf reader = new Ebnf(content);
				reader.read();
				reader.buildTokens();
				reader.buildRegex();

				// System.out.println(reader.getEbnf());
				// Dictionary<MapEntry<String, String>, List<EbnfToken>> tokens = reader.getTokdict();
				// for (MapEntry<MapEntry<String, String>, List<EbnfToken>> entry : tokens)
				// {
				// 	System.out.println(entry.getValue());
				// }
				System.out.println(reader.getRegex());
			}
		}
		catch (FileNotFoundException e)
		{
			ErrorHandler.getOne(TestTree.class).handle(e);
		}
		catch (ParseException e)
		{
			ErrorHandler.getOne(TestTree.class).handle(e);
		}
	}
}
