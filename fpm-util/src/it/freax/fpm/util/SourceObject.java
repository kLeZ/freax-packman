/**
 * 
 */
package it.freax.fpm.util;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Creates a dynamic source code file object
 * This is an example of how we can prepare a dynamic java source code for
 * compilation.
 * This class reads the java code from a string and prepares a JavaFileObject
 */
public class SourceObject extends SimpleJavaFileObject
{
	private String qualifiedName;
	private String sourceCode;

	/**
	 * Converts the name to an URI, as that is the format expected by
	 * JavaFileObject
	 * 
	 * @param fully
	 *            qualified name given to the class file
	 * @param code
	 *            the source code string
	 */
	public SourceObject(String name, String code)
	{
		super(URI.create(String.format("string:///%s%s", name.replaceAll("\\.", "/"), Kind.SOURCE.extension)), Kind.SOURCE);
		qualifiedName = name;
		sourceCode = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
	{
		return sourceCode;
	}

	public String getQualifiedName()
	{
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName)
	{
		this.qualifiedName = qualifiedName;
	}

	public String getSourceCode()
	{
		return sourceCode;
	}

	public void setSourceCode(String sourceCode)
	{
		this.sourceCode = sourceCode;
	}
}
