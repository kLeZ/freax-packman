package it.freax.fpm.test.main;
// $ANTLR 3.4 Java.g 2012-05-06 21:45:25

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created
 *          elementValuePair and elementValuePairs rules, then used them in the
 *          annotation rule.  Allows it to recognize annotation references with
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which
 *          has the Identifier portion in it, the parser would fail on constants in
 *          annotation definitions because it expected two identifiers.
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *         
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 *
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *  Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *  and forVarControl to use variableModifier* not 'final'? (annotation)?
 *
 *  Version 1.0.5 -- Terence, June 21, 2007
 *  --a[i].foo didn't work. Fixed unaryExpression
 *
 *  Version 1.0.6 -- John Ridgway, March 17, 2008
 *      Made "assert" a switchable keyword like "enum".
 *      Fixed compilationUnit to disallow "annotation importDeclaration ...".
 *      Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more
 *          places.
 *      Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 *          modifiers or variableModifiers, as appropriate.
 *      Renamed "bound" to "typeBound" to better match language in the JLS.
 *      Added "memberDeclaration" which rewrites to methodDeclaration or
 *      fieldDeclaration and pulled type into memberDeclaration.  So we parse
 *          type and then move on to decide whether we're dealing with a field
 *          or a method.
 *      Modified "constructorDeclaration" to use "constructorBody" instead of
 *          "methodBody".  constructorBody starts with explicitConstructorInvocation,
 *          then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 *          out of expressions allowed me to simplify "primary".
 *      Changed variableDeclarator to simplify it.
 *      Changed type to use classOrInterfaceType, thus simplifying it; of course
 *          I then had to add classOrInterfaceType, but it is used in several
 *          places.
 *      Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 *      Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 *      Changed annotationTypeElementRest to use normalClassDeclaration and
 *          normalInterfaceDeclaration rather than classDeclaration and
 *          interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 *      Split localVariableDeclaration into localVariableDeclarationStatement
 *          (includes the terminating semi-colon) and localVariableDeclaration.
 *          This allowed me to use localVariableDeclaration in "forInit" clauses,
 *           simplifying them.
 *      Changed switchBlockStatementGroup to use multiple labels.  This adds an
 *          ambiguity, but if one uses appropriately greedy parsing it yields the
 *           parse that is closest to the meaning of the switch statement.
 *      Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 *      Added semantic predicates to test for shift operations rather than other
 *          things.  Thus, for instance, the string "< <" will never be treated
 *          as a left-shift operator.
 *      In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation,
 *          which are illegal.
 *      Moved "nonWildcardTypeArguments into innerCreator.
 *      Removed 'super' superSuffix from explicitGenericInvocation, since that
 *          is only used in explicitConstructorInvocation at the beginning of a
 *           constructorBody.  (This is part of the simplification of expressions
 *           mentioned earlier.)
 *      Simplified primary (got rid of those things that are only used in
 *          explicitConstructorInvocation).
 *      Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 *          led to an ambiguity.
 *
 *      This grammar successfully parses every .java file in the JDK 1.5 source
 *          tree (excluding those whose file names include '-', which are not
 *          valid Java compilation units).
 *
 *  Known remaining problems:
 *      "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 *      "Letter" should be "a character for which the method
 *      Character.isJavaIdentifierStart(int) returns true."  A "Java
 *      letter-or-digit is a character for which the method
 *      Character.isJavaIdentifierPart(int) returns true."
 */
@SuppressWarnings({ "all", "warnings", "unchecked" })
public class JavaParser extends Parser
{
	public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ABSTRACT", "AMP", "AMPAMP", "AMPEQ", "ASSERT", "BANG", "BANGEQ", "BAR", "BARBAR", "BAREQ", "BOOLEAN", "BREAK", "BYTE", "CARET", "CARETEQ", "CASE", "CATCH", "CHAR", "CHARLITERAL", "CLASS", "COLON", "COMMA", "COMMENT", "CONST", "CONTINUE", "DEFAULT", "DO", "DOT", "DOUBLE", "DOUBLELITERAL", "DoubleSuffix", "ELLIPSIS", "ELSE", "ENUM", "EQ", "EQEQ", "EXTENDS", "EscapeSequence", "Exponent", "FALSE", "FINAL", "FINALLY", "FLOAT", "FLOATLITERAL", "FOR", "FloatSuffix", "GOTO", "GT", "HexDigit", "HexPrefix", "IDENTIFIER", "IF", "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "INTLITERAL", "IdentifierPart", "IdentifierStart", "IntegerNumber", "LBRACE", "LBRACKET", "LINE_COMMENT", "LONG", "LONGLITERAL", "LPAREN", "LT", "LongSuffix", "MONKEYS_AT", "NATIVE", "NEW", "NULL", "NonIntegerNumber", "PACKAGE", "PERCENT", "PERCENTEQ", "PLUS", "PLUSEQ", "PLUSPLUS", "PRIVATE", "PROTECTED", "PUBLIC", "QUES", "RBRACE", "RBRACKET", "RETURN", "RPAREN", "SEMI", "SHORT", "SLASH", "SLASHEQ", "STAR", "STAREQ", "STATIC", "STRICTFP", "STRINGLITERAL", "SUB", "SUBEQ", "SUBSUB", "SUPER", "SWITCH", "SYNCHRONIZED", "SurrogateIdentifer", "THIS", "THROW", "THROWS", "TILDE", "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WHILE", "WS" };

	public static final int EOF = -1;
	public static final int ABSTRACT = 4;
	public static final int AMP = 5;
	public static final int AMPAMP = 6;
	public static final int AMPEQ = 7;
	public static final int ASSERT = 8;
	public static final int BANG = 9;
	public static final int BANGEQ = 10;
	public static final int BAR = 11;
	public static final int BARBAR = 12;
	public static final int BAREQ = 13;
	public static final int BOOLEAN = 14;
	public static final int BREAK = 15;
	public static final int BYTE = 16;
	public static final int CARET = 17;
	public static final int CARETEQ = 18;
	public static final int CASE = 19;
	public static final int CATCH = 20;
	public static final int CHAR = 21;
	public static final int CHARLITERAL = 22;
	public static final int CLASS = 23;
	public static final int COLON = 24;
	public static final int COMMA = 25;
	public static final int COMMENT = 26;
	public static final int CONST = 27;
	public static final int CONTINUE = 28;
	public static final int DEFAULT = 29;
	public static final int DO = 30;
	public static final int DOT = 31;
	public static final int DOUBLE = 32;
	public static final int DOUBLELITERAL = 33;
	public static final int DoubleSuffix = 34;
	public static final int ELLIPSIS = 35;
	public static final int ELSE = 36;
	public static final int ENUM = 37;
	public static final int EQ = 38;
	public static final int EQEQ = 39;
	public static final int EXTENDS = 40;
	public static final int EscapeSequence = 41;
	public static final int Exponent = 42;
	public static final int FALSE = 43;
	public static final int FINAL = 44;
	public static final int FINALLY = 45;
	public static final int FLOAT = 46;
	public static final int FLOATLITERAL = 47;
	public static final int FOR = 48;
	public static final int FloatSuffix = 49;
	public static final int GOTO = 50;
	public static final int GT = 51;
	public static final int HexDigit = 52;
	public static final int HexPrefix = 53;
	public static final int IDENTIFIER = 54;
	public static final int IF = 55;
	public static final int IMPLEMENTS = 56;
	public static final int IMPORT = 57;
	public static final int INSTANCEOF = 58;
	public static final int INT = 59;
	public static final int INTERFACE = 60;
	public static final int INTLITERAL = 61;
	public static final int IdentifierPart = 62;
	public static final int IdentifierStart = 63;
	public static final int IntegerNumber = 64;
	public static final int LBRACE = 65;
	public static final int LBRACKET = 66;
	public static final int LINE_COMMENT = 67;
	public static final int LONG = 68;
	public static final int LONGLITERAL = 69;
	public static final int LPAREN = 70;
	public static final int LT = 71;
	public static final int LongSuffix = 72;
	public static final int MONKEYS_AT = 73;
	public static final int NATIVE = 74;
	public static final int NEW = 75;
	public static final int NULL = 76;
	public static final int NonIntegerNumber = 77;
	public static final int PACKAGE = 78;
	public static final int PERCENT = 79;
	public static final int PERCENTEQ = 80;
	public static final int PLUS = 81;
	public static final int PLUSEQ = 82;
	public static final int PLUSPLUS = 83;
	public static final int PRIVATE = 84;
	public static final int PROTECTED = 85;
	public static final int PUBLIC = 86;
	public static final int QUES = 87;
	public static final int RBRACE = 88;
	public static final int RBRACKET = 89;
	public static final int RETURN = 90;
	public static final int RPAREN = 91;
	public static final int SEMI = 92;
	public static final int SHORT = 93;
	public static final int SLASH = 94;
	public static final int SLASHEQ = 95;
	public static final int STAR = 96;
	public static final int STAREQ = 97;
	public static final int STATIC = 98;
	public static final int STRICTFP = 99;
	public static final int STRINGLITERAL = 100;
	public static final int SUB = 101;
	public static final int SUBEQ = 102;
	public static final int SUBSUB = 103;
	public static final int SUPER = 104;
	public static final int SWITCH = 105;
	public static final int SYNCHRONIZED = 106;
	public static final int SurrogateIdentifer = 107;
	public static final int THIS = 108;
	public static final int THROW = 109;
	public static final int THROWS = 110;
	public static final int TILDE = 111;
	public static final int TRANSIENT = 112;
	public static final int TRUE = 113;
	public static final int TRY = 114;
	public static final int VOID = 115;
	public static final int VOLATILE = 116;
	public static final int WHILE = 117;
	public static final int WS = 118;

	// delegates
	public Parser[] getDelegates()
	{
		return new Parser[] {};
	}

	// delegators

	public JavaParser(TokenStream input)
	{
		this(input, new RecognizerSharedState());
	}

	public JavaParser(TokenStream input, RecognizerSharedState state)
	{
		super(input, state);
		this.state.ruleMemo = new HashMap[381 + 1];

	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor)
	{
		this.adaptor = adaptor;
	}

	public TreeAdaptor getTreeAdaptor()
	{
		return adaptor;
	}

	public String[] getTokenNames()
	{
		return JavaParser.tokenNames;
	}

	public String getGrammarFileName()
	{
		return "Java.g";
	}

	public static class compilationUnit_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "compilationUnit"
	// Java.g:298:1: compilationUnit : ( ( annotations )? packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* ;
	public final JavaParser.compilationUnit_return compilationUnit() throws RecognitionException
	{
		JavaParser.compilationUnit_return retval = new JavaParser.compilationUnit_return();
		retval.start = input.LT(1);

		int compilationUnit_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.annotations_return annotations1 = null;

		JavaParser.packageDeclaration_return packageDeclaration2 = null;

		JavaParser.importDeclaration_return importDeclaration3 = null;

		JavaParser.typeDeclaration_return typeDeclaration4 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 1)) { return retval; }

			// Java.g:299:5: ( ( ( annotations )? packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
			// Java.g:299:9: ( ( annotations )? packageDeclaration )? ( importDeclaration )* ( typeDeclaration )*
			{
				root_0 = (CommonTree) adaptor.nil();

				// Java.g:299:9: ( ( annotations )? packageDeclaration )?
				int alt2 = 2;
				int LA2_0 = input.LA(1);

				if ((LA2_0 == MONKEYS_AT))
				{
					int LA2_1 = input.LA(2);

					if ((synpred2_Java()))
					{
						alt2 = 1;
					}
				}
				else if ((LA2_0 == PACKAGE))
				{
					alt2 = 1;
				}
				switch (alt2)
				{
					case 1:
					// Java.g:299:13: ( annotations )? packageDeclaration
					{
						// Java.g:299:13: ( annotations )?
						int alt1 = 2;
						int LA1_0 = input.LA(1);

						if ((LA1_0 == MONKEYS_AT))
						{
							alt1 = 1;
						}
						switch (alt1)
						{
							case 1:
							// Java.g:299:14: annotations
							{
								pushFollow(FOLLOW_annotations_in_compilationUnit100);
								annotations1 = annotations();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, annotations1.getTree());
								}

							}
								break;

						}

						pushFollow(FOLLOW_packageDeclaration_in_compilationUnit129);
						packageDeclaration2 = packageDeclaration();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, packageDeclaration2.getTree());
						}

					}
						break;

				}

				// Java.g:303:9: ( importDeclaration )*
				loop3:
				do
				{
					int alt3 = 2;
					int LA3_0 = input.LA(1);

					if ((LA3_0 == IMPORT))
					{
						alt3 = 1;
					}

					switch (alt3)
					{
						case 1:
						// Java.g:303:10: importDeclaration
						{
							pushFollow(FOLLOW_importDeclaration_in_compilationUnit151);
							importDeclaration3 = importDeclaration();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, importDeclaration3.getTree());
							}

						}
							break;

						default:
							break loop3;
					}
				}
				while (true);

				// Java.g:305:9: ( typeDeclaration )*
				loop4:
				do
				{
					int alt4 = 2;
					int LA4_0 = input.LA(1);

					if (((LA4_0 == ABSTRACT) || (LA4_0 == BOOLEAN) || (LA4_0 == BYTE) || (LA4_0 == CHAR) || (LA4_0 == CLASS) || (LA4_0 == DOUBLE) || (LA4_0 == ENUM) || (LA4_0 == FINAL) || (LA4_0 == FLOAT) || (LA4_0 == IDENTIFIER) || ((LA4_0 >= INT) && (LA4_0 <= INTERFACE)) || (LA4_0 == LONG) || (LA4_0 == LT) || ((LA4_0 >= MONKEYS_AT) && (LA4_0 <= NATIVE)) || ((LA4_0 >= PRIVATE) && (LA4_0 <= PUBLIC)) || ((LA4_0 >= SEMI) && (LA4_0 <= SHORT)) || ((LA4_0 >= STATIC) && (LA4_0 <= STRICTFP)) || (LA4_0 == SYNCHRONIZED) || (LA4_0 == TRANSIENT) || ((LA4_0 >= VOID) && (LA4_0 <= VOLATILE))))
					{
						alt4 = 1;
					}

					switch (alt4)
					{
						case 1:
						// Java.g:305:10: typeDeclaration
						{
							pushFollow(FOLLOW_typeDeclaration_in_compilationUnit173);
							typeDeclaration4 = typeDeclaration();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, typeDeclaration4.getTree());
							}

						}
							break;

						default:
							break loop4;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 1, compilationUnit_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "compilationUnit"

	public static class packageDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "packageDeclaration"
	// Java.g:309:1: packageDeclaration : 'package' qualifiedName ';' ;
	public final JavaParser.packageDeclaration_return packageDeclaration() throws RecognitionException
	{
		JavaParser.packageDeclaration_return retval = new JavaParser.packageDeclaration_return();
		retval.start = input.LT(1);

		int packageDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal5 = null;
		Token char_literal7 = null;
		JavaParser.qualifiedName_return qualifiedName6 = null;

		CommonTree string_literal5_tree = null;
		CommonTree char_literal7_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 2)) { return retval; }

			// Java.g:310:5: ( 'package' qualifiedName ';' )
			// Java.g:310:9: 'package' qualifiedName ';'
			{
				root_0 = (CommonTree) adaptor.nil();

				string_literal5 = (Token) match(input, PACKAGE, FOLLOW_PACKAGE_in_packageDeclaration204);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal5_tree = (CommonTree) adaptor.create(string_literal5);
					adaptor.addChild(root_0, string_literal5_tree);
				}

				pushFollow(FOLLOW_qualifiedName_in_packageDeclaration206);
				qualifiedName6 = qualifiedName();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, qualifiedName6.getTree());
				}

				char_literal7 = (Token) match(input, SEMI, FOLLOW_SEMI_in_packageDeclaration216);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal7_tree = (CommonTree) adaptor.create(char_literal7);
					adaptor.addChild(root_0, char_literal7_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 2, packageDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "packageDeclaration"

	public static class importDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "importDeclaration"
	// Java.g:314:1: importDeclaration : ( 'import' ( 'static' )? IDENTIFIER '.' '*' ';' | 'import' ( 'static' )? IDENTIFIER ( '.' IDENTIFIER )+ ( '.' '*' )? ';' );
	public final JavaParser.importDeclaration_return importDeclaration() throws RecognitionException
	{
		JavaParser.importDeclaration_return retval = new JavaParser.importDeclaration_return();
		retval.start = input.LT(1);

		int importDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal8 = null;
		Token string_literal9 = null;
		Token IDENTIFIER10 = null;
		Token char_literal11 = null;
		Token char_literal12 = null;
		Token char_literal13 = null;
		Token string_literal14 = null;
		Token string_literal15 = null;
		Token IDENTIFIER16 = null;
		Token char_literal17 = null;
		Token IDENTIFIER18 = null;
		Token char_literal19 = null;
		Token char_literal20 = null;
		Token char_literal21 = null;

		CommonTree string_literal8_tree = null;
		CommonTree string_literal9_tree = null;
		CommonTree IDENTIFIER10_tree = null;
		CommonTree char_literal11_tree = null;
		CommonTree char_literal12_tree = null;
		CommonTree char_literal13_tree = null;
		CommonTree string_literal14_tree = null;
		CommonTree string_literal15_tree = null;
		CommonTree IDENTIFIER16_tree = null;
		CommonTree char_literal17_tree = null;
		CommonTree IDENTIFIER18_tree = null;
		CommonTree char_literal19_tree = null;
		CommonTree char_literal20_tree = null;
		CommonTree char_literal21_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 3)) { return retval; }

			// Java.g:315:5: ( 'import' ( 'static' )? IDENTIFIER '.' '*' ';' | 'import' ( 'static' )? IDENTIFIER ( '.' IDENTIFIER )+ ( '.' '*' )? ';' )
			int alt9 = 2;
			int LA9_0 = input.LA(1);

			if ((LA9_0 == IMPORT))
			{
				int LA9_1 = input.LA(2);

				if ((LA9_1 == STATIC))
				{
					int LA9_2 = input.LA(3);

					if ((LA9_2 == IDENTIFIER))
					{
						int LA9_3 = input.LA(4);

						if ((LA9_3 == DOT))
						{
							int LA9_4 = input.LA(5);

							if ((LA9_4 == STAR))
							{
								alt9 = 1;
							}
							else if ((LA9_4 == IDENTIFIER))
							{
								alt9 = 2;
							}
							else
							{
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								NoViableAltException nvae = new NoViableAltException("", 9, 4, input);

								throw nvae;

							}
						}
						else
						{
							if (state.backtracking > 0)
							{
								state.failed = true;
								return retval;
							}
							NoViableAltException nvae = new NoViableAltException("", 9, 3, input);

							throw nvae;

						}
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 9, 2, input);

						throw nvae;

					}
				}
				else if ((LA9_1 == IDENTIFIER))
				{
					int LA9_3 = input.LA(3);

					if ((LA9_3 == DOT))
					{
						int LA9_4 = input.LA(4);

						if ((LA9_4 == STAR))
						{
							alt9 = 1;
						}
						else if ((LA9_4 == IDENTIFIER))
						{
							alt9 = 2;
						}
						else
						{
							if (state.backtracking > 0)
							{
								state.failed = true;
								return retval;
							}
							NoViableAltException nvae = new NoViableAltException("", 9, 4, input);

							throw nvae;

						}
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 9, 3, input);

						throw nvae;

					}
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 9, 1, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 9, 0, input);

				throw nvae;

			}
			switch (alt9)
			{
				case 1:
				// Java.g:315:9: 'import' ( 'static' )? IDENTIFIER '.' '*' ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal8 = (Token) match(input, IMPORT, FOLLOW_IMPORT_in_importDeclaration237);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal8_tree = (CommonTree) adaptor.create(string_literal8);
						adaptor.addChild(root_0, string_literal8_tree);
					}

					// Java.g:316:9: ( 'static' )?
					int alt5 = 2;
					int LA5_0 = input.LA(1);

					if ((LA5_0 == STATIC))
					{
						alt5 = 1;
					}
					switch (alt5)
					{
						case 1:
						// Java.g:316:10: 'static'
						{
							string_literal9 = (Token) match(input, STATIC, FOLLOW_STATIC_in_importDeclaration249);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal9_tree = (CommonTree) adaptor.create(string_literal9);
								adaptor.addChild(root_0, string_literal9_tree);
							}

						}
							break;

					}

					IDENTIFIER10 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_importDeclaration270);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER10_tree = (CommonTree) adaptor.create(IDENTIFIER10);
						adaptor.addChild(root_0, IDENTIFIER10_tree);
					}

					char_literal11 = (Token) match(input, DOT, FOLLOW_DOT_in_importDeclaration272);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal11_tree = (CommonTree) adaptor.create(char_literal11);
						adaptor.addChild(root_0, char_literal11_tree);
					}

					char_literal12 = (Token) match(input, STAR, FOLLOW_STAR_in_importDeclaration274);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal12_tree = (CommonTree) adaptor.create(char_literal12);
						adaptor.addChild(root_0, char_literal12_tree);
					}

					char_literal13 = (Token) match(input, SEMI, FOLLOW_SEMI_in_importDeclaration284);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal13_tree = (CommonTree) adaptor.create(char_literal13);
						adaptor.addChild(root_0, char_literal13_tree);
					}

				}
					break;
				case 2:
				// Java.g:320:9: 'import' ( 'static' )? IDENTIFIER ( '.' IDENTIFIER )+ ( '.' '*' )? ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal14 = (Token) match(input, IMPORT, FOLLOW_IMPORT_in_importDeclaration301);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal14_tree = (CommonTree) adaptor.create(string_literal14);
						adaptor.addChild(root_0, string_literal14_tree);
					}

					// Java.g:321:9: ( 'static' )?
					int alt6 = 2;
					int LA6_0 = input.LA(1);

					if ((LA6_0 == STATIC))
					{
						alt6 = 1;
					}
					switch (alt6)
					{
						case 1:
						// Java.g:321:10: 'static'
						{
							string_literal15 = (Token) match(input, STATIC, FOLLOW_STATIC_in_importDeclaration313);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal15_tree = (CommonTree) adaptor.create(string_literal15);
								adaptor.addChild(root_0, string_literal15_tree);
							}

						}
							break;

					}

					IDENTIFIER16 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_importDeclaration334);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER16_tree = (CommonTree) adaptor.create(IDENTIFIER16);
						adaptor.addChild(root_0, IDENTIFIER16_tree);
					}

					// Java.g:324:9: ( '.' IDENTIFIER )+
					int cnt7 = 0;
					loop7:
					do
					{
						int alt7 = 2;
						int LA7_0 = input.LA(1);

						if ((LA7_0 == DOT))
						{
							int LA7_1 = input.LA(2);

							if ((LA7_1 == IDENTIFIER))
							{
								alt7 = 1;
							}

						}

						switch (alt7)
						{
							case 1:
							// Java.g:324:10: '.' IDENTIFIER
							{
								char_literal17 = (Token) match(input, DOT, FOLLOW_DOT_in_importDeclaration345);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal17_tree = (CommonTree) adaptor.create(char_literal17);
									adaptor.addChild(root_0, char_literal17_tree);
								}

								IDENTIFIER18 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_importDeclaration347);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									IDENTIFIER18_tree = (CommonTree) adaptor.create(IDENTIFIER18);
									adaptor.addChild(root_0, IDENTIFIER18_tree);
								}

							}
								break;

							default:
								if (cnt7 >= 1)
								{
									break loop7;
								}
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								EarlyExitException eee = new EarlyExitException(7, input);
								throw eee;
						}
						cnt7++;
					}
					while (true);

					// Java.g:326:9: ( '.' '*' )?
					int alt8 = 2;
					int LA8_0 = input.LA(1);

					if ((LA8_0 == DOT))
					{
						alt8 = 1;
					}
					switch (alt8)
					{
						case 1:
						// Java.g:326:10: '.' '*'
						{
							char_literal19 = (Token) match(input, DOT, FOLLOW_DOT_in_importDeclaration369);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal19_tree = (CommonTree) adaptor.create(char_literal19);
								adaptor.addChild(root_0, char_literal19_tree);
							}

							char_literal20 = (Token) match(input, STAR, FOLLOW_STAR_in_importDeclaration371);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal20_tree = (CommonTree) adaptor.create(char_literal20);
								adaptor.addChild(root_0, char_literal20_tree);
							}

						}
							break;

					}

					char_literal21 = (Token) match(input, SEMI, FOLLOW_SEMI_in_importDeclaration392);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal21_tree = (CommonTree) adaptor.create(char_literal21);
						adaptor.addChild(root_0, char_literal21_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 3, importDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "importDeclaration"

	public static class qualifiedImportName_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "qualifiedImportName"
	// Java.g:331:1: qualifiedImportName : IDENTIFIER ( '.' IDENTIFIER )* ;
	public final JavaParser.qualifiedImportName_return qualifiedImportName() throws RecognitionException
	{
		JavaParser.qualifiedImportName_return retval = new JavaParser.qualifiedImportName_return();
		retval.start = input.LT(1);

		int qualifiedImportName_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER22 = null;
		Token char_literal23 = null;
		Token IDENTIFIER24 = null;

		CommonTree IDENTIFIER22_tree = null;
		CommonTree char_literal23_tree = null;
		CommonTree IDENTIFIER24_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 4)) { return retval; }

			// Java.g:332:5: ( IDENTIFIER ( '.' IDENTIFIER )* )
			// Java.g:332:9: IDENTIFIER ( '.' IDENTIFIER )*
			{
				root_0 = (CommonTree) adaptor.nil();

				IDENTIFIER22 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_qualifiedImportName412);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER22_tree = (CommonTree) adaptor.create(IDENTIFIER22);
					adaptor.addChild(root_0, IDENTIFIER22_tree);
				}

				// Java.g:333:9: ( '.' IDENTIFIER )*
				loop10:
				do
				{
					int alt10 = 2;
					int LA10_0 = input.LA(1);

					if ((LA10_0 == DOT))
					{
						alt10 = 1;
					}

					switch (alt10)
					{
						case 1:
						// Java.g:333:10: '.' IDENTIFIER
						{
							char_literal23 = (Token) match(input, DOT, FOLLOW_DOT_in_qualifiedImportName423);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal23_tree = (CommonTree) adaptor.create(char_literal23);
								adaptor.addChild(root_0, char_literal23_tree);
							}

							IDENTIFIER24 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_qualifiedImportName425);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								IDENTIFIER24_tree = (CommonTree) adaptor.create(IDENTIFIER24);
								adaptor.addChild(root_0, IDENTIFIER24_tree);
							}

						}
							break;

						default:
							break loop10;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 4, qualifiedImportName_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "qualifiedImportName"

	public static class typeDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeDeclaration"
	// Java.g:337:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
	public final JavaParser.typeDeclaration_return typeDeclaration() throws RecognitionException
	{
		JavaParser.typeDeclaration_return retval = new JavaParser.typeDeclaration_return();
		retval.start = input.LT(1);

		int typeDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal26 = null;
		JavaParser.classOrInterfaceDeclaration_return classOrInterfaceDeclaration25 = null;

		CommonTree char_literal26_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 5)) { return retval; }

			// Java.g:338:5: ( classOrInterfaceDeclaration | ';' )
			int alt11 = 2;
			int LA11_0 = input.LA(1);

			if (((LA11_0 == ABSTRACT) || (LA11_0 == BOOLEAN) || (LA11_0 == BYTE) || (LA11_0 == CHAR) || (LA11_0 == CLASS) || (LA11_0 == DOUBLE) || (LA11_0 == ENUM) || (LA11_0 == FINAL) || (LA11_0 == FLOAT) || (LA11_0 == IDENTIFIER) || ((LA11_0 >= INT) && (LA11_0 <= INTERFACE)) || (LA11_0 == LONG) || (LA11_0 == LT) || ((LA11_0 >= MONKEYS_AT) && (LA11_0 <= NATIVE)) || ((LA11_0 >= PRIVATE) && (LA11_0 <= PUBLIC)) || (LA11_0 == SHORT) || ((LA11_0 >= STATIC) && (LA11_0 <= STRICTFP)) || (LA11_0 == SYNCHRONIZED) || (LA11_0 == TRANSIENT) || ((LA11_0 >= VOID) && (LA11_0 <= VOLATILE))))
			{
				alt11 = 1;
			}
			else if ((LA11_0 == SEMI))
			{
				alt11 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 11, 0, input);

				throw nvae;

			}
			switch (alt11)
			{
				case 1:
				// Java.g:338:9: classOrInterfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration456);
					classOrInterfaceDeclaration25 = classOrInterfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classOrInterfaceDeclaration25.getTree());
					}

				}
					break;
				case 2:
				// Java.g:339:9: ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal26 = (Token) match(input, SEMI, FOLLOW_SEMI_in_typeDeclaration466);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal26_tree = (CommonTree) adaptor.create(char_literal26);
						adaptor.addChild(root_0, char_literal26_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 5, typeDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeDeclaration"

	public static class classOrInterfaceDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classOrInterfaceDeclaration"
	// Java.g:342:1: classOrInterfaceDeclaration : ( classDeclaration | interfaceDeclaration );
	public final JavaParser.classOrInterfaceDeclaration_return classOrInterfaceDeclaration() throws RecognitionException
	{
		JavaParser.classOrInterfaceDeclaration_return retval = new JavaParser.classOrInterfaceDeclaration_return();
		retval.start = input.LT(1);

		int classOrInterfaceDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.classDeclaration_return classDeclaration27 = null;

		JavaParser.interfaceDeclaration_return interfaceDeclaration28 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 6)) { return retval; }

			// Java.g:343:5: ( classDeclaration | interfaceDeclaration )
			int alt12 = 2;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA12_1 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA12_2 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA12_3 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA12_4 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA12_5 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA12_6 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA12_7 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA12_8 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA12_9 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA12_10 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA12_11 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA12_12 = input.LA(2);

					if ((synpred12_Java()))
					{
						alt12 = 1;
					}
					else if ((true))
					{
						alt12 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 12, 12, input);

						throw nvae;

					}
				}
					break;
				case CLASS:
				case ENUM:
				{
					alt12 = 1;
				}
					break;
				case INTERFACE:
				{
					alt12 = 2;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 12, 0, input);

					throw nvae;

			}

			switch (alt12)
			{
				case 1:
				// Java.g:343:10: classDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration487);
					classDeclaration27 = classDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classDeclaration27.getTree());
					}

				}
					break;
				case 2:
				// Java.g:344:9: interfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration497);
					interfaceDeclaration28 = interfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, interfaceDeclaration28.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 6, classOrInterfaceDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classOrInterfaceDeclaration"

	public static class modifiers_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "modifiers"
	// Java.g:348:1: modifiers : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )* ;
	public final JavaParser.modifiers_return modifiers() throws RecognitionException
	{
		JavaParser.modifiers_return retval = new JavaParser.modifiers_return();
		retval.start = input.LT(1);

		int modifiers_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal30 = null;
		Token string_literal31 = null;
		Token string_literal32 = null;
		Token string_literal33 = null;
		Token string_literal34 = null;
		Token string_literal35 = null;
		Token string_literal36 = null;
		Token string_literal37 = null;
		Token string_literal38 = null;
		Token string_literal39 = null;
		Token string_literal40 = null;
		JavaParser.annotation_return annotation29 = null;

		CommonTree string_literal30_tree = null;
		CommonTree string_literal31_tree = null;
		CommonTree string_literal32_tree = null;
		CommonTree string_literal33_tree = null;
		CommonTree string_literal34_tree = null;
		CommonTree string_literal35_tree = null;
		CommonTree string_literal36_tree = null;
		CommonTree string_literal37_tree = null;
		CommonTree string_literal38_tree = null;
		CommonTree string_literal39_tree = null;
		CommonTree string_literal40_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 7)) { return retval; }

			// Java.g:349:5: ( ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )* )
			// Java.g:350:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )*
			{
				root_0 = (CommonTree) adaptor.nil();

				// Java.g:350:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )*
				loop13:
				do
				{
					int alt13 = 13;
					switch (input.LA(1))
					{
						case MONKEYS_AT:
						{
							int LA13_2 = input.LA(2);

							if ((LA13_2 == IDENTIFIER))
							{
								alt13 = 1;
							}

						}
							break;
						case PUBLIC:
						{
							alt13 = 2;
						}
							break;
						case PROTECTED:
						{
							alt13 = 3;
						}
							break;
						case PRIVATE:
						{
							alt13 = 4;
						}
							break;
						case STATIC:
						{
							alt13 = 5;
						}
							break;
						case ABSTRACT:
						{
							alt13 = 6;
						}
							break;
						case FINAL:
						{
							alt13 = 7;
						}
							break;
						case NATIVE:
						{
							alt13 = 8;
						}
							break;
						case SYNCHRONIZED:
						{
							alt13 = 9;
						}
							break;
						case TRANSIENT:
						{
							alt13 = 10;
						}
							break;
						case VOLATILE:
						{
							alt13 = 11;
						}
							break;
						case STRICTFP:
						{
							alt13 = 12;
						}
							break;

					}

					switch (alt13)
					{
						case 1:
						// Java.g:350:10: annotation
						{
							pushFollow(FOLLOW_annotation_in_modifiers532);
							annotation29 = annotation();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, annotation29.getTree());
							}

						}
							break;
						case 2:
						// Java.g:351:9: 'public'
						{
							string_literal30 = (Token) match(input, PUBLIC, FOLLOW_PUBLIC_in_modifiers542);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal30_tree = (CommonTree) adaptor.create(string_literal30);
								adaptor.addChild(root_0, string_literal30_tree);
							}

						}
							break;
						case 3:
						// Java.g:352:9: 'protected'
						{
							string_literal31 = (Token) match(input, PROTECTED, FOLLOW_PROTECTED_in_modifiers552);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal31_tree = (CommonTree) adaptor.create(string_literal31);
								adaptor.addChild(root_0, string_literal31_tree);
							}

						}
							break;
						case 4:
						// Java.g:353:9: 'private'
						{
							string_literal32 = (Token) match(input, PRIVATE, FOLLOW_PRIVATE_in_modifiers562);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal32_tree = (CommonTree) adaptor.create(string_literal32);
								adaptor.addChild(root_0, string_literal32_tree);
							}

						}
							break;
						case 5:
						// Java.g:354:9: 'static'
						{
							string_literal33 = (Token) match(input, STATIC, FOLLOW_STATIC_in_modifiers572);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal33_tree = (CommonTree) adaptor.create(string_literal33);
								adaptor.addChild(root_0, string_literal33_tree);
							}

						}
							break;
						case 6:
						// Java.g:355:9: 'abstract'
						{
							string_literal34 = (Token) match(input, ABSTRACT, FOLLOW_ABSTRACT_in_modifiers582);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal34_tree = (CommonTree) adaptor.create(string_literal34);
								adaptor.addChild(root_0, string_literal34_tree);
							}

						}
							break;
						case 7:
						// Java.g:356:9: 'final'
						{
							string_literal35 = (Token) match(input, FINAL, FOLLOW_FINAL_in_modifiers592);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal35_tree = (CommonTree) adaptor.create(string_literal35);
								adaptor.addChild(root_0, string_literal35_tree);
							}

						}
							break;
						case 8:
						// Java.g:357:9: 'native'
						{
							string_literal36 = (Token) match(input, NATIVE, FOLLOW_NATIVE_in_modifiers602);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal36_tree = (CommonTree) adaptor.create(string_literal36);
								adaptor.addChild(root_0, string_literal36_tree);
							}

						}
							break;
						case 9:
						// Java.g:358:9: 'synchronized'
						{
							string_literal37 = (Token) match(input, SYNCHRONIZED, FOLLOW_SYNCHRONIZED_in_modifiers612);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal37_tree = (CommonTree) adaptor.create(string_literal37);
								adaptor.addChild(root_0, string_literal37_tree);
							}

						}
							break;
						case 10:
						// Java.g:359:9: 'transient'
						{
							string_literal38 = (Token) match(input, TRANSIENT, FOLLOW_TRANSIENT_in_modifiers622);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal38_tree = (CommonTree) adaptor.create(string_literal38);
								adaptor.addChild(root_0, string_literal38_tree);
							}

						}
							break;
						case 11:
						// Java.g:360:9: 'volatile'
						{
							string_literal39 = (Token) match(input, VOLATILE, FOLLOW_VOLATILE_in_modifiers632);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal39_tree = (CommonTree) adaptor.create(string_literal39);
								adaptor.addChild(root_0, string_literal39_tree);
							}

						}
							break;
						case 12:
						// Java.g:361:9: 'strictfp'
						{
							string_literal40 = (Token) match(input, STRICTFP, FOLLOW_STRICTFP_in_modifiers642);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal40_tree = (CommonTree) adaptor.create(string_literal40);
								adaptor.addChild(root_0, string_literal40_tree);
							}

						}
							break;

						default:
							break loop13;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 7, modifiers_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "modifiers"

	public static class variableModifiers_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "variableModifiers"
	// Java.g:366:1: variableModifiers : ( 'final' | annotation )* ;
	public final JavaParser.variableModifiers_return variableModifiers() throws RecognitionException
	{
		JavaParser.variableModifiers_return retval = new JavaParser.variableModifiers_return();
		retval.start = input.LT(1);

		int variableModifiers_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal41 = null;
		JavaParser.annotation_return annotation42 = null;

		CommonTree string_literal41_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 8)) { return retval; }

			// Java.g:367:5: ( ( 'final' | annotation )* )
			// Java.g:367:9: ( 'final' | annotation )*
			{
				root_0 = (CommonTree) adaptor.nil();

				// Java.g:367:9: ( 'final' | annotation )*
				loop14:
				do
				{
					int alt14 = 3;
					int LA14_0 = input.LA(1);

					if ((LA14_0 == FINAL))
					{
						alt14 = 1;
					}
					else if ((LA14_0 == MONKEYS_AT))
					{
						alt14 = 2;
					}

					switch (alt14)
					{
						case 1:
						// Java.g:367:13: 'final'
						{
							string_literal41 = (Token) match(input, FINAL, FOLLOW_FINAL_in_variableModifiers674);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal41_tree = (CommonTree) adaptor.create(string_literal41);
								adaptor.addChild(root_0, string_literal41_tree);
							}

						}
							break;
						case 2:
						// Java.g:368:13: annotation
						{
							pushFollow(FOLLOW_annotation_in_variableModifiers688);
							annotation42 = annotation();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, annotation42.getTree());
							}

						}
							break;

						default:
							break loop14;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 8, variableModifiers_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "variableModifiers"

	public static class classDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classDeclaration"
	// Java.g:373:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
	public final JavaParser.classDeclaration_return classDeclaration() throws RecognitionException
	{
		JavaParser.classDeclaration_return retval = new JavaParser.classDeclaration_return();
		retval.start = input.LT(1);

		int classDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.normalClassDeclaration_return normalClassDeclaration43 = null;

		JavaParser.enumDeclaration_return enumDeclaration44 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 9)) { return retval; }

			// Java.g:374:5: ( normalClassDeclaration | enumDeclaration )
			int alt15 = 2;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA15_1 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA15_2 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA15_3 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA15_4 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA15_5 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA15_6 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA15_7 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA15_8 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA15_9 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA15_10 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA15_11 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA15_12 = input.LA(2);

					if ((synpred27_Java()))
					{
						alt15 = 1;
					}
					else if ((true))
					{
						alt15 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 15, 12, input);

						throw nvae;

					}
				}
					break;
				case CLASS:
				{
					alt15 = 1;
				}
					break;
				case ENUM:
				{
					alt15 = 2;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 15, 0, input);

					throw nvae;

			}

			switch (alt15)
			{
				case 1:
				// Java.g:374:9: normalClassDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration724);
					normalClassDeclaration43 = normalClassDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, normalClassDeclaration43.getTree());
					}

				}
					break;
				case 2:
				// Java.g:375:9: enumDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_enumDeclaration_in_classDeclaration734);
					enumDeclaration44 = enumDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, enumDeclaration44.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 9, classDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classDeclaration"

	public static class normalClassDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "normalClassDeclaration"
	// Java.g:378:1: normalClassDeclaration : modifiers 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody ;
	public final JavaParser.normalClassDeclaration_return normalClassDeclaration() throws RecognitionException
	{
		JavaParser.normalClassDeclaration_return retval = new JavaParser.normalClassDeclaration_return();
		retval.start = input.LT(1);

		int normalClassDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal46 = null;
		Token IDENTIFIER47 = null;
		Token string_literal49 = null;
		Token string_literal51 = null;
		JavaParser.modifiers_return modifiers45 = null;

		JavaParser.typeParameters_return typeParameters48 = null;

		JavaParser.type_return type50 = null;

		JavaParser.typeList_return typeList52 = null;

		JavaParser.classBody_return classBody53 = null;

		CommonTree string_literal46_tree = null;
		CommonTree IDENTIFIER47_tree = null;
		CommonTree string_literal49_tree = null;
		CommonTree string_literal51_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 10)) { return retval; }

			// Java.g:379:5: ( modifiers 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody )
			// Java.g:379:9: modifiers 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_normalClassDeclaration754);
				modifiers45 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers45.getTree());
				}

				string_literal46 = (Token) match(input, CLASS, FOLLOW_CLASS_in_normalClassDeclaration757);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal46_tree = (CommonTree) adaptor.create(string_literal46);
					adaptor.addChild(root_0, string_literal46_tree);
				}

				IDENTIFIER47 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_normalClassDeclaration759);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER47_tree = (CommonTree) adaptor.create(IDENTIFIER47);
					adaptor.addChild(root_0, IDENTIFIER47_tree);
				}

				// Java.g:380:9: ( typeParameters )?
				int alt16 = 2;
				int LA16_0 = input.LA(1);

				if ((LA16_0 == LT))
				{
					alt16 = 1;
				}
				switch (alt16)
				{
					case 1:
					// Java.g:380:10: typeParameters
					{
						pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration770);
						typeParameters48 = typeParameters();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeParameters48.getTree());
						}

					}
						break;

				}

				// Java.g:382:9: ( 'extends' type )?
				int alt17 = 2;
				int LA17_0 = input.LA(1);

				if ((LA17_0 == EXTENDS))
				{
					alt17 = 1;
				}
				switch (alt17)
				{
					case 1:
					// Java.g:382:10: 'extends' type
					{
						string_literal49 = (Token) match(input, EXTENDS, FOLLOW_EXTENDS_in_normalClassDeclaration792);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal49_tree = (CommonTree) adaptor.create(string_literal49);
							adaptor.addChild(root_0, string_literal49_tree);
						}

						pushFollow(FOLLOW_type_in_normalClassDeclaration794);
						type50 = type();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, type50.getTree());
						}

					}
						break;

				}

				// Java.g:384:9: ( 'implements' typeList )?
				int alt18 = 2;
				int LA18_0 = input.LA(1);

				if ((LA18_0 == IMPLEMENTS))
				{
					alt18 = 1;
				}
				switch (alt18)
				{
					case 1:
					// Java.g:384:10: 'implements' typeList
					{
						string_literal51 = (Token) match(input, IMPLEMENTS, FOLLOW_IMPLEMENTS_in_normalClassDeclaration816);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal51_tree = (CommonTree) adaptor.create(string_literal51);
							adaptor.addChild(root_0, string_literal51_tree);
						}

						pushFollow(FOLLOW_typeList_in_normalClassDeclaration818);
						typeList52 = typeList();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeList52.getTree());
						}

					}
						break;

				}

				pushFollow(FOLLOW_classBody_in_normalClassDeclaration851);
				classBody53 = classBody();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, classBody53.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 10, normalClassDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "normalClassDeclaration"

	public static class typeParameters_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeParameters"
	// Java.g:390:1: typeParameters : '<' typeParameter ( ',' typeParameter )* '>' ;
	public final JavaParser.typeParameters_return typeParameters() throws RecognitionException
	{
		JavaParser.typeParameters_return retval = new JavaParser.typeParameters_return();
		retval.start = input.LT(1);

		int typeParameters_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal54 = null;
		Token char_literal56 = null;
		Token char_literal58 = null;
		JavaParser.typeParameter_return typeParameter55 = null;

		JavaParser.typeParameter_return typeParameter57 = null;

		CommonTree char_literal54_tree = null;
		CommonTree char_literal56_tree = null;
		CommonTree char_literal58_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 11)) { return retval; }

			// Java.g:391:5: ( '<' typeParameter ( ',' typeParameter )* '>' )
			// Java.g:391:9: '<' typeParameter ( ',' typeParameter )* '>'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal54 = (Token) match(input, LT, FOLLOW_LT_in_typeParameters872);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal54_tree = (CommonTree) adaptor.create(char_literal54);
					adaptor.addChild(root_0, char_literal54_tree);
				}

				pushFollow(FOLLOW_typeParameter_in_typeParameters886);
				typeParameter55 = typeParameter();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, typeParameter55.getTree());
				}

				// Java.g:393:13: ( ',' typeParameter )*
				loop19:
				do
				{
					int alt19 = 2;
					int LA19_0 = input.LA(1);

					if ((LA19_0 == COMMA))
					{
						alt19 = 1;
					}

					switch (alt19)
					{
						case 1:
						// Java.g:393:14: ',' typeParameter
						{
							char_literal56 = (Token) match(input, COMMA, FOLLOW_COMMA_in_typeParameters901);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal56_tree = (CommonTree) adaptor.create(char_literal56);
								adaptor.addChild(root_0, char_literal56_tree);
							}

							pushFollow(FOLLOW_typeParameter_in_typeParameters903);
							typeParameter57 = typeParameter();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, typeParameter57.getTree());
							}

						}
							break;

						default:
							break loop19;
					}
				}
				while (true);

				char_literal58 = (Token) match(input, GT, FOLLOW_GT_in_typeParameters928);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal58_tree = (CommonTree) adaptor.create(char_literal58);
					adaptor.addChild(root_0, char_literal58_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 11, typeParameters_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeParameters"

	public static class typeParameter_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeParameter"
	// Java.g:398:1: typeParameter : IDENTIFIER ( 'extends' typeBound )? ;
	public final JavaParser.typeParameter_return typeParameter() throws RecognitionException
	{
		JavaParser.typeParameter_return retval = new JavaParser.typeParameter_return();
		retval.start = input.LT(1);

		int typeParameter_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER59 = null;
		Token string_literal60 = null;
		JavaParser.typeBound_return typeBound61 = null;

		CommonTree IDENTIFIER59_tree = null;
		CommonTree string_literal60_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 12)) { return retval; }

			// Java.g:399:5: ( IDENTIFIER ( 'extends' typeBound )? )
			// Java.g:399:9: IDENTIFIER ( 'extends' typeBound )?
			{
				root_0 = (CommonTree) adaptor.nil();

				IDENTIFIER59 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_typeParameter948);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER59_tree = (CommonTree) adaptor.create(IDENTIFIER59);
					adaptor.addChild(root_0, IDENTIFIER59_tree);
				}

				// Java.g:400:9: ( 'extends' typeBound )?
				int alt20 = 2;
				int LA20_0 = input.LA(1);

				if ((LA20_0 == EXTENDS))
				{
					alt20 = 1;
				}
				switch (alt20)
				{
					case 1:
					// Java.g:400:10: 'extends' typeBound
					{
						string_literal60 = (Token) match(input, EXTENDS, FOLLOW_EXTENDS_in_typeParameter959);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal60_tree = (CommonTree) adaptor.create(string_literal60);
							adaptor.addChild(root_0, string_literal60_tree);
						}

						pushFollow(FOLLOW_typeBound_in_typeParameter961);
						typeBound61 = typeBound();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeBound61.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 12, typeParameter_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeParameter"

	public static class typeBound_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeBound"
	// Java.g:405:1: typeBound : type ( '&' type )* ;
	public final JavaParser.typeBound_return typeBound() throws RecognitionException
	{
		JavaParser.typeBound_return retval = new JavaParser.typeBound_return();
		retval.start = input.LT(1);

		int typeBound_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal63 = null;
		JavaParser.type_return type62 = null;

		JavaParser.type_return type64 = null;

		CommonTree char_literal63_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 13)) { return retval; }

			// Java.g:406:5: ( type ( '&' type )* )
			// Java.g:406:9: type ( '&' type )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_type_in_typeBound993);
				type62 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type62.getTree());
				}

				// Java.g:407:9: ( '&' type )*
				loop21:
				do
				{
					int alt21 = 2;
					int LA21_0 = input.LA(1);

					if ((LA21_0 == AMP))
					{
						alt21 = 1;
					}

					switch (alt21)
					{
						case 1:
						// Java.g:407:10: '&' type
						{
							char_literal63 = (Token) match(input, AMP, FOLLOW_AMP_in_typeBound1004);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal63_tree = (CommonTree) adaptor.create(char_literal63);
								adaptor.addChild(root_0, char_literal63_tree);
							}

							pushFollow(FOLLOW_type_in_typeBound1006);
							type64 = type();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, type64.getTree());
							}

						}
							break;

						default:
							break loop21;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 13, typeBound_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeBound"

	public static class enumDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "enumDeclaration"
	// Java.g:412:1: enumDeclaration : modifiers ( 'enum' ) IDENTIFIER ( 'implements' typeList )? enumBody ;
	public final JavaParser.enumDeclaration_return enumDeclaration() throws RecognitionException
	{
		JavaParser.enumDeclaration_return retval = new JavaParser.enumDeclaration_return();
		retval.start = input.LT(1);

		int enumDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal66 = null;
		Token IDENTIFIER67 = null;
		Token string_literal68 = null;
		JavaParser.modifiers_return modifiers65 = null;

		JavaParser.typeList_return typeList69 = null;

		JavaParser.enumBody_return enumBody70 = null;

		CommonTree string_literal66_tree = null;
		CommonTree IDENTIFIER67_tree = null;
		CommonTree string_literal68_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 14)) { return retval; }

			// Java.g:413:5: ( modifiers ( 'enum' ) IDENTIFIER ( 'implements' typeList )? enumBody )
			// Java.g:413:9: modifiers ( 'enum' ) IDENTIFIER ( 'implements' typeList )? enumBody
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_enumDeclaration1038);
				modifiers65 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers65.getTree());
				}

				// Java.g:414:9: ( 'enum' )
				// Java.g:414:10: 'enum'
				{
					string_literal66 = (Token) match(input, ENUM, FOLLOW_ENUM_in_enumDeclaration1050);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal66_tree = (CommonTree) adaptor.create(string_literal66);
						adaptor.addChild(root_0, string_literal66_tree);
					}

				}

				IDENTIFIER67 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_enumDeclaration1071);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER67_tree = (CommonTree) adaptor.create(IDENTIFIER67);
					adaptor.addChild(root_0, IDENTIFIER67_tree);
				}

				// Java.g:417:9: ( 'implements' typeList )?
				int alt22 = 2;
				int LA22_0 = input.LA(1);

				if ((LA22_0 == IMPLEMENTS))
				{
					alt22 = 1;
				}
				switch (alt22)
				{
					case 1:
					// Java.g:417:10: 'implements' typeList
					{
						string_literal68 = (Token) match(input, IMPLEMENTS, FOLLOW_IMPLEMENTS_in_enumDeclaration1082);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal68_tree = (CommonTree) adaptor.create(string_literal68);
							adaptor.addChild(root_0, string_literal68_tree);
						}

						pushFollow(FOLLOW_typeList_in_enumDeclaration1084);
						typeList69 = typeList();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeList69.getTree());
						}

					}
						break;

				}

				pushFollow(FOLLOW_enumBody_in_enumDeclaration1105);
				enumBody70 = enumBody();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, enumBody70.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 14, enumDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "enumDeclaration"

	public static class enumBody_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "enumBody"
	// Java.g:423:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' ;
	public final JavaParser.enumBody_return enumBody() throws RecognitionException
	{
		JavaParser.enumBody_return retval = new JavaParser.enumBody_return();
		retval.start = input.LT(1);

		int enumBody_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal71 = null;
		Token char_literal73 = null;
		Token char_literal75 = null;
		JavaParser.enumConstants_return enumConstants72 = null;

		JavaParser.enumBodyDeclarations_return enumBodyDeclarations74 = null;

		CommonTree char_literal71_tree = null;
		CommonTree char_literal73_tree = null;
		CommonTree char_literal75_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 15)) { return retval; }

			// Java.g:424:5: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' )
			// Java.g:424:9: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal71 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_enumBody1130);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal71_tree = (CommonTree) adaptor.create(char_literal71);
					adaptor.addChild(root_0, char_literal71_tree);
				}

				// Java.g:425:9: ( enumConstants )?
				int alt23 = 2;
				int LA23_0 = input.LA(1);

				if (((LA23_0 == IDENTIFIER) || (LA23_0 == MONKEYS_AT)))
				{
					alt23 = 1;
				}
				switch (alt23)
				{
					case 1:
					// Java.g:425:10: enumConstants
					{
						pushFollow(FOLLOW_enumConstants_in_enumBody1141);
						enumConstants72 = enumConstants();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, enumConstants72.getTree());
						}

					}
						break;

				}

				// Java.g:427:9: ( ',' )?
				int alt24 = 2;
				int LA24_0 = input.LA(1);

				if ((LA24_0 == COMMA))
				{
					alt24 = 1;
				}
				switch (alt24)
				{
					case 1:
					// Java.g:427:9: ','
					{
						char_literal73 = (Token) match(input, COMMA, FOLLOW_COMMA_in_enumBody1163);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal73_tree = (CommonTree) adaptor.create(char_literal73);
							adaptor.addChild(root_0, char_literal73_tree);
						}

					}
						break;

				}

				// Java.g:428:9: ( enumBodyDeclarations )?
				int alt25 = 2;
				int LA25_0 = input.LA(1);

				if ((LA25_0 == SEMI))
				{
					alt25 = 1;
				}
				switch (alt25)
				{
					case 1:
					// Java.g:428:10: enumBodyDeclarations
					{
						pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody1176);
						enumBodyDeclarations74 = enumBodyDeclarations();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, enumBodyDeclarations74.getTree());
						}

					}
						break;

				}

				char_literal75 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_enumBody1198);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal75_tree = (CommonTree) adaptor.create(char_literal75);
					adaptor.addChild(root_0, char_literal75_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 15, enumBody_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "enumBody"

	public static class enumConstants_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "enumConstants"
	// Java.g:433:1: enumConstants : enumConstant ( ',' enumConstant )* ;
	public final JavaParser.enumConstants_return enumConstants() throws RecognitionException
	{
		JavaParser.enumConstants_return retval = new JavaParser.enumConstants_return();
		retval.start = input.LT(1);

		int enumConstants_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal77 = null;
		JavaParser.enumConstant_return enumConstant76 = null;

		JavaParser.enumConstant_return enumConstant78 = null;

		CommonTree char_literal77_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 16)) { return retval; }

			// Java.g:434:5: ( enumConstant ( ',' enumConstant )* )
			// Java.g:434:9: enumConstant ( ',' enumConstant )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_enumConstant_in_enumConstants1218);
				enumConstant76 = enumConstant();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, enumConstant76.getTree());
				}

				// Java.g:435:9: ( ',' enumConstant )*
				loop26:
				do
				{
					int alt26 = 2;
					int LA26_0 = input.LA(1);

					if ((LA26_0 == COMMA))
					{
						int LA26_1 = input.LA(2);

						if (((LA26_1 == IDENTIFIER) || (LA26_1 == MONKEYS_AT)))
						{
							alt26 = 1;
						}

					}

					switch (alt26)
					{
						case 1:
						// Java.g:435:10: ',' enumConstant
						{
							char_literal77 = (Token) match(input, COMMA, FOLLOW_COMMA_in_enumConstants1229);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal77_tree = (CommonTree) adaptor.create(char_literal77);
								adaptor.addChild(root_0, char_literal77_tree);
							}

							pushFollow(FOLLOW_enumConstant_in_enumConstants1231);
							enumConstant78 = enumConstant();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, enumConstant78.getTree());
							}

						}
							break;

						default:
							break loop26;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 16, enumConstants_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "enumConstants"

	public static class enumConstant_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "enumConstant"
	// Java.g:443:1: enumConstant : ( annotations )? IDENTIFIER ( arguments )? ( classBody )? ;
	public final JavaParser.enumConstant_return enumConstant() throws RecognitionException
	{
		JavaParser.enumConstant_return retval = new JavaParser.enumConstant_return();
		retval.start = input.LT(1);

		int enumConstant_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER80 = null;
		JavaParser.annotations_return annotations79 = null;

		JavaParser.arguments_return arguments81 = null;

		JavaParser.classBody_return classBody82 = null;

		CommonTree IDENTIFIER80_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 17)) { return retval; }

			// Java.g:444:5: ( ( annotations )? IDENTIFIER ( arguments )? ( classBody )? )
			// Java.g:444:9: ( annotations )? IDENTIFIER ( arguments )? ( classBody )?
			{
				root_0 = (CommonTree) adaptor.nil();

				// Java.g:444:9: ( annotations )?
				int alt27 = 2;
				int LA27_0 = input.LA(1);

				if ((LA27_0 == MONKEYS_AT))
				{
					alt27 = 1;
				}
				switch (alt27)
				{
					case 1:
					// Java.g:444:10: annotations
					{
						pushFollow(FOLLOW_annotations_in_enumConstant1265);
						annotations79 = annotations();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, annotations79.getTree());
						}

					}
						break;

				}

				IDENTIFIER80 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_enumConstant1286);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER80_tree = (CommonTree) adaptor.create(IDENTIFIER80);
					adaptor.addChild(root_0, IDENTIFIER80_tree);
				}

				// Java.g:447:9: ( arguments )?
				int alt28 = 2;
				int LA28_0 = input.LA(1);

				if ((LA28_0 == LPAREN))
				{
					alt28 = 1;
				}
				switch (alt28)
				{
					case 1:
					// Java.g:447:10: arguments
					{
						pushFollow(FOLLOW_arguments_in_enumConstant1297);
						arguments81 = arguments();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, arguments81.getTree());
						}

					}
						break;

				}

				// Java.g:449:9: ( classBody )?
				int alt29 = 2;
				int LA29_0 = input.LA(1);

				if ((LA29_0 == LBRACE))
				{
					alt29 = 1;
				}
				switch (alt29)
				{
					case 1:
					// Java.g:449:10: classBody
					{
						pushFollow(FOLLOW_classBody_in_enumConstant1319);
						classBody82 = classBody();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, classBody82.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 17, enumConstant_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "enumConstant"

	public static class enumBodyDeclarations_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "enumBodyDeclarations"
	// Java.g:455:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
	public final JavaParser.enumBodyDeclarations_return enumBodyDeclarations() throws RecognitionException
	{
		JavaParser.enumBodyDeclarations_return retval = new JavaParser.enumBodyDeclarations_return();
		retval.start = input.LT(1);

		int enumBodyDeclarations_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal83 = null;
		JavaParser.classBodyDeclaration_return classBodyDeclaration84 = null;

		CommonTree char_literal83_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 18)) { return retval; }

			// Java.g:456:5: ( ';' ( classBodyDeclaration )* )
			// Java.g:456:9: ';' ( classBodyDeclaration )*
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal83 = (Token) match(input, SEMI, FOLLOW_SEMI_in_enumBodyDeclarations1360);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal83_tree = (CommonTree) adaptor.create(char_literal83);
					adaptor.addChild(root_0, char_literal83_tree);
				}

				// Java.g:457:9: ( classBodyDeclaration )*
				loop30:
				do
				{
					int alt30 = 2;
					int LA30_0 = input.LA(1);

					if (((LA30_0 == ABSTRACT) || (LA30_0 == BOOLEAN) || (LA30_0 == BYTE) || (LA30_0 == CHAR) || (LA30_0 == CLASS) || (LA30_0 == DOUBLE) || (LA30_0 == ENUM) || (LA30_0 == FINAL) || (LA30_0 == FLOAT) || (LA30_0 == IDENTIFIER) || ((LA30_0 >= INT) && (LA30_0 <= INTERFACE)) || (LA30_0 == LBRACE) || (LA30_0 == LONG) || (LA30_0 == LT) || ((LA30_0 >= MONKEYS_AT) && (LA30_0 <= NATIVE)) || ((LA30_0 >= PRIVATE) && (LA30_0 <= PUBLIC)) || ((LA30_0 >= SEMI) && (LA30_0 <= SHORT)) || ((LA30_0 >= STATIC) && (LA30_0 <= STRICTFP)) || (LA30_0 == SYNCHRONIZED) || (LA30_0 == TRANSIENT) || ((LA30_0 >= VOID) && (LA30_0 <= VOLATILE))))
					{
						alt30 = 1;
					}

					switch (alt30)
					{
						case 1:
						// Java.g:457:10: classBodyDeclaration
						{
							pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1372);
							classBodyDeclaration84 = classBodyDeclaration();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, classBodyDeclaration84.getTree());
							}

						}
							break;

						default:
							break loop30;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 18, enumBodyDeclarations_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "enumBodyDeclarations"

	public static class interfaceDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "interfaceDeclaration"
	// Java.g:461:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
	public final JavaParser.interfaceDeclaration_return interfaceDeclaration() throws RecognitionException
	{
		JavaParser.interfaceDeclaration_return retval = new JavaParser.interfaceDeclaration_return();
		retval.start = input.LT(1);

		int interfaceDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.normalInterfaceDeclaration_return normalInterfaceDeclaration85 = null;

		JavaParser.annotationTypeDeclaration_return annotationTypeDeclaration86 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 19)) { return retval; }

			// Java.g:462:5: ( normalInterfaceDeclaration | annotationTypeDeclaration )
			int alt31 = 2;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA31_1 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA31_2 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA31_3 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA31_4 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA31_5 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA31_6 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA31_7 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA31_8 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA31_9 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA31_10 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA31_11 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA31_12 = input.LA(2);

					if ((synpred43_Java()))
					{
						alt31 = 1;
					}
					else if ((true))
					{
						alt31 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 31, 12, input);

						throw nvae;

					}
				}
					break;
				case INTERFACE:
				{
					alt31 = 1;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 31, 0, input);

					throw nvae;

			}

			switch (alt31)
			{
				case 1:
				// Java.g:462:9: normalInterfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1403);
					normalInterfaceDeclaration85 = normalInterfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, normalInterfaceDeclaration85.getTree());
					}

				}
					break;
				case 2:
				// Java.g:463:9: annotationTypeDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1413);
					annotationTypeDeclaration86 = annotationTypeDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, annotationTypeDeclaration86.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 19, interfaceDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "interfaceDeclaration"

	public static class normalInterfaceDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "normalInterfaceDeclaration"
	// Java.g:466:1: normalInterfaceDeclaration : modifiers 'interface' IDENTIFIER ( typeParameters )? ( 'extends' typeList )? interfaceBody ;
	public final JavaParser.normalInterfaceDeclaration_return normalInterfaceDeclaration() throws RecognitionException
	{
		JavaParser.normalInterfaceDeclaration_return retval = new JavaParser.normalInterfaceDeclaration_return();
		retval.start = input.LT(1);

		int normalInterfaceDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal88 = null;
		Token IDENTIFIER89 = null;
		Token string_literal91 = null;
		JavaParser.modifiers_return modifiers87 = null;

		JavaParser.typeParameters_return typeParameters90 = null;

		JavaParser.typeList_return typeList92 = null;

		JavaParser.interfaceBody_return interfaceBody93 = null;

		CommonTree string_literal88_tree = null;
		CommonTree IDENTIFIER89_tree = null;
		CommonTree string_literal91_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 20)) { return retval; }

			// Java.g:467:5: ( modifiers 'interface' IDENTIFIER ( typeParameters )? ( 'extends' typeList )? interfaceBody )
			// Java.g:467:9: modifiers 'interface' IDENTIFIER ( typeParameters )? ( 'extends' typeList )? interfaceBody
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_normalInterfaceDeclaration1437);
				modifiers87 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers87.getTree());
				}

				string_literal88 = (Token) match(input, INTERFACE, FOLLOW_INTERFACE_in_normalInterfaceDeclaration1439);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal88_tree = (CommonTree) adaptor.create(string_literal88);
					adaptor.addChild(root_0, string_literal88_tree);
				}

				IDENTIFIER89 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1441);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER89_tree = (CommonTree) adaptor.create(IDENTIFIER89);
					adaptor.addChild(root_0, IDENTIFIER89_tree);
				}

				// Java.g:468:9: ( typeParameters )?
				int alt32 = 2;
				int LA32_0 = input.LA(1);

				if ((LA32_0 == LT))
				{
					alt32 = 1;
				}
				switch (alt32)
				{
					case 1:
					// Java.g:468:10: typeParameters
					{
						pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration1452);
						typeParameters90 = typeParameters();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeParameters90.getTree());
						}

					}
						break;

				}

				// Java.g:470:9: ( 'extends' typeList )?
				int alt33 = 2;
				int LA33_0 = input.LA(1);

				if ((LA33_0 == EXTENDS))
				{
					alt33 = 1;
				}
				switch (alt33)
				{
					case 1:
					// Java.g:470:10: 'extends' typeList
					{
						string_literal91 = (Token) match(input, EXTENDS, FOLLOW_EXTENDS_in_normalInterfaceDeclaration1474);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal91_tree = (CommonTree) adaptor.create(string_literal91);
							adaptor.addChild(root_0, string_literal91_tree);
						}

						pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration1476);
						typeList92 = typeList();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeList92.getTree());
						}

					}
						break;

				}

				pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration1497);
				interfaceBody93 = interfaceBody();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, interfaceBody93.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 20, normalInterfaceDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "normalInterfaceDeclaration"

	public static class typeList_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeList"
	// Java.g:475:1: typeList : type ( ',' type )* ;
	public final JavaParser.typeList_return typeList() throws RecognitionException
	{
		JavaParser.typeList_return retval = new JavaParser.typeList_return();
		retval.start = input.LT(1);

		int typeList_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal95 = null;
		JavaParser.type_return type94 = null;

		JavaParser.type_return type96 = null;

		CommonTree char_literal95_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 21)) { return retval; }

			// Java.g:476:5: ( type ( ',' type )* )
			// Java.g:476:9: type ( ',' type )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_type_in_typeList1517);
				type94 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type94.getTree());
				}

				// Java.g:477:9: ( ',' type )*
				loop34:
				do
				{
					int alt34 = 2;
					int LA34_0 = input.LA(1);

					if ((LA34_0 == COMMA))
					{
						alt34 = 1;
					}

					switch (alt34)
					{
						case 1:
						// Java.g:477:10: ',' type
						{
							char_literal95 = (Token) match(input, COMMA, FOLLOW_COMMA_in_typeList1528);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal95_tree = (CommonTree) adaptor.create(char_literal95);
								adaptor.addChild(root_0, char_literal95_tree);
							}

							pushFollow(FOLLOW_type_in_typeList1530);
							type96 = type();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, type96.getTree());
							}

						}
							break;

						default:
							break loop34;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 21, typeList_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeList"

	public static class classBody_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classBody"
	// Java.g:481:1: classBody : '{' ( classBodyDeclaration )* '}' ;
	public final JavaParser.classBody_return classBody() throws RecognitionException
	{
		JavaParser.classBody_return retval = new JavaParser.classBody_return();
		retval.start = input.LT(1);

		int classBody_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal97 = null;
		Token char_literal99 = null;
		JavaParser.classBodyDeclaration_return classBodyDeclaration98 = null;

		CommonTree char_literal97_tree = null;
		CommonTree char_literal99_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 22)) { return retval; }

			// Java.g:482:5: ( '{' ( classBodyDeclaration )* '}' )
			// Java.g:482:9: '{' ( classBodyDeclaration )* '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal97 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_classBody1561);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal97_tree = (CommonTree) adaptor.create(char_literal97);
					adaptor.addChild(root_0, char_literal97_tree);
				}

				// Java.g:483:9: ( classBodyDeclaration )*
				loop35:
				do
				{
					int alt35 = 2;
					int LA35_0 = input.LA(1);

					if (((LA35_0 == ABSTRACT) || (LA35_0 == BOOLEAN) || (LA35_0 == BYTE) || (LA35_0 == CHAR) || (LA35_0 == CLASS) || (LA35_0 == DOUBLE) || (LA35_0 == ENUM) || (LA35_0 == FINAL) || (LA35_0 == FLOAT) || (LA35_0 == IDENTIFIER) || ((LA35_0 >= INT) && (LA35_0 <= INTERFACE)) || (LA35_0 == LBRACE) || (LA35_0 == LONG) || (LA35_0 == LT) || ((LA35_0 >= MONKEYS_AT) && (LA35_0 <= NATIVE)) || ((LA35_0 >= PRIVATE) && (LA35_0 <= PUBLIC)) || ((LA35_0 >= SEMI) && (LA35_0 <= SHORT)) || ((LA35_0 >= STATIC) && (LA35_0 <= STRICTFP)) || (LA35_0 == SYNCHRONIZED) || (LA35_0 == TRANSIENT) || ((LA35_0 >= VOID) && (LA35_0 <= VOLATILE))))
					{
						alt35 = 1;
					}

					switch (alt35)
					{
						case 1:
						// Java.g:483:10: classBodyDeclaration
						{
							pushFollow(FOLLOW_classBodyDeclaration_in_classBody1573);
							classBodyDeclaration98 = classBodyDeclaration();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, classBodyDeclaration98.getTree());
							}

						}
							break;

						default:
							break loop35;
					}
				}
				while (true);

				char_literal99 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_classBody1595);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal99_tree = (CommonTree) adaptor.create(char_literal99);
					adaptor.addChild(root_0, char_literal99_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 22, classBody_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classBody"

	public static class interfaceBody_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "interfaceBody"
	// Java.g:488:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' ;
	public final JavaParser.interfaceBody_return interfaceBody() throws RecognitionException
	{
		JavaParser.interfaceBody_return retval = new JavaParser.interfaceBody_return();
		retval.start = input.LT(1);

		int interfaceBody_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal100 = null;
		Token char_literal102 = null;
		JavaParser.interfaceBodyDeclaration_return interfaceBodyDeclaration101 = null;

		CommonTree char_literal100_tree = null;
		CommonTree char_literal102_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 23)) { return retval; }

			// Java.g:489:5: ( '{' ( interfaceBodyDeclaration )* '}' )
			// Java.g:489:9: '{' ( interfaceBodyDeclaration )* '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal100 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_interfaceBody1615);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal100_tree = (CommonTree) adaptor.create(char_literal100);
					adaptor.addChild(root_0, char_literal100_tree);
				}

				// Java.g:490:9: ( interfaceBodyDeclaration )*
				loop36:
				do
				{
					int alt36 = 2;
					int LA36_0 = input.LA(1);

					if (((LA36_0 == ABSTRACT) || (LA36_0 == BOOLEAN) || (LA36_0 == BYTE) || (LA36_0 == CHAR) || (LA36_0 == CLASS) || (LA36_0 == DOUBLE) || (LA36_0 == ENUM) || (LA36_0 == FINAL) || (LA36_0 == FLOAT) || (LA36_0 == IDENTIFIER) || ((LA36_0 >= INT) && (LA36_0 <= INTERFACE)) || (LA36_0 == LONG) || (LA36_0 == LT) || ((LA36_0 >= MONKEYS_AT) && (LA36_0 <= NATIVE)) || ((LA36_0 >= PRIVATE) && (LA36_0 <= PUBLIC)) || ((LA36_0 >= SEMI) && (LA36_0 <= SHORT)) || ((LA36_0 >= STATIC) && (LA36_0 <= STRICTFP)) || (LA36_0 == SYNCHRONIZED) || (LA36_0 == TRANSIENT) || ((LA36_0 >= VOID) && (LA36_0 <= VOLATILE))))
					{
						alt36 = 1;
					}

					switch (alt36)
					{
						case 1:
						// Java.g:490:10: interfaceBodyDeclaration
						{
							pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody1627);
							interfaceBodyDeclaration101 = interfaceBodyDeclaration();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, interfaceBodyDeclaration101.getTree());
							}

						}
							break;

						default:
							break loop36;
					}
				}
				while (true);

				char_literal102 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_interfaceBody1649);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal102_tree = (CommonTree) adaptor.create(char_literal102);
					adaptor.addChild(root_0, char_literal102_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 23, interfaceBody_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "interfaceBody"

	public static class classBodyDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classBodyDeclaration"
	// Java.g:495:1: classBodyDeclaration : ( ';' | ( 'static' )? block | memberDecl );
	public final JavaParser.classBodyDeclaration_return classBodyDeclaration() throws RecognitionException
	{
		JavaParser.classBodyDeclaration_return retval = new JavaParser.classBodyDeclaration_return();
		retval.start = input.LT(1);

		int classBodyDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal103 = null;
		Token string_literal104 = null;
		JavaParser.block_return block105 = null;

		JavaParser.memberDecl_return memberDecl106 = null;

		CommonTree char_literal103_tree = null;
		CommonTree string_literal104_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 24)) { return retval; }

			// Java.g:496:5: ( ';' | ( 'static' )? block | memberDecl )
			int alt38 = 3;
			switch (input.LA(1))
			{
				case SEMI:
				{
					alt38 = 1;
				}
					break;
				case STATIC:
				{
					int LA38_2 = input.LA(2);

					if ((LA38_2 == LBRACE))
					{
						alt38 = 2;
					}
					else if (((LA38_2 == ABSTRACT) || (LA38_2 == BOOLEAN) || (LA38_2 == BYTE) || (LA38_2 == CHAR) || (LA38_2 == CLASS) || (LA38_2 == DOUBLE) || (LA38_2 == ENUM) || (LA38_2 == FINAL) || (LA38_2 == FLOAT) || (LA38_2 == IDENTIFIER) || ((LA38_2 >= INT) && (LA38_2 <= INTERFACE)) || (LA38_2 == LONG) || (LA38_2 == LT) || ((LA38_2 >= MONKEYS_AT) && (LA38_2 <= NATIVE)) || ((LA38_2 >= PRIVATE) && (LA38_2 <= PUBLIC)) || (LA38_2 == SHORT) || ((LA38_2 >= STATIC) && (LA38_2 <= STRICTFP)) || (LA38_2 == SYNCHRONIZED) || (LA38_2 == TRANSIENT) || ((LA38_2 >= VOID) && (LA38_2 <= VOLATILE))))
					{
						alt38 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 38, 2, input);

						throw nvae;

					}
				}
					break;
				case LBRACE:
				{
					alt38 = 2;
				}
					break;
				case ABSTRACT:
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case CLASS:
				case DOUBLE:
				case ENUM:
				case FINAL:
				case FLOAT:
				case IDENTIFIER:
				case INT:
				case INTERFACE:
				case LONG:
				case LT:
				case MONKEYS_AT:
				case NATIVE:
				case PRIVATE:
				case PROTECTED:
				case PUBLIC:
				case SHORT:
				case STRICTFP:
				case SYNCHRONIZED:
				case TRANSIENT:
				case VOID:
				case VOLATILE:
				{
					alt38 = 3;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 38, 0, input);

					throw nvae;

			}

			switch (alt38)
			{
				case 1:
				// Java.g:496:9: ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal103 = (Token) match(input, SEMI, FOLLOW_SEMI_in_classBodyDeclaration1669);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal103_tree = (CommonTree) adaptor.create(char_literal103);
						adaptor.addChild(root_0, char_literal103_tree);
					}

				}
					break;
				case 2:
				// Java.g:497:9: ( 'static' )? block
				{
					root_0 = (CommonTree) adaptor.nil();

					// Java.g:497:9: ( 'static' )?
					int alt37 = 2;
					int LA37_0 = input.LA(1);

					if ((LA37_0 == STATIC))
					{
						alt37 = 1;
					}
					switch (alt37)
					{
						case 1:
						// Java.g:497:10: 'static'
						{
							string_literal104 = (Token) match(input, STATIC, FOLLOW_STATIC_in_classBodyDeclaration1680);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal104_tree = (CommonTree) adaptor.create(string_literal104);
								adaptor.addChild(root_0, string_literal104_tree);
							}

						}
							break;

					}

					pushFollow(FOLLOW_block_in_classBodyDeclaration1702);
					block105 = block();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, block105.getTree());
					}

				}
					break;
				case 3:
				// Java.g:500:9: memberDecl
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration1712);
					memberDecl106 = memberDecl();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, memberDecl106.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 24, classBodyDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classBodyDeclaration"

	public static class memberDecl_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "memberDecl"
	// Java.g:503:1: memberDecl : ( fieldDeclaration | methodDeclaration | classDeclaration | interfaceDeclaration );
	public final JavaParser.memberDecl_return memberDecl() throws RecognitionException
	{
		JavaParser.memberDecl_return retval = new JavaParser.memberDecl_return();
		retval.start = input.LT(1);

		int memberDecl_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.fieldDeclaration_return fieldDeclaration107 = null;

		JavaParser.methodDeclaration_return methodDeclaration108 = null;

		JavaParser.classDeclaration_return classDeclaration109 = null;

		JavaParser.interfaceDeclaration_return interfaceDeclaration110 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 25)) { return retval; }

			// Java.g:504:5: ( fieldDeclaration | methodDeclaration | classDeclaration | interfaceDeclaration )
			int alt39 = 4;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA39_1 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA39_2 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA39_3 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA39_4 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA39_5 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA39_6 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA39_7 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA39_8 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA39_9 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA39_10 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA39_11 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA39_12 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else if ((synpred54_Java()))
					{
						alt39 = 3;
					}
					else if ((true))
					{
						alt39 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 12, input);

						throw nvae;

					}
				}
					break;
				case IDENTIFIER:
				{
					int LA39_13 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 13, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA39_14 = input.LA(2);

					if ((synpred52_Java()))
					{
						alt39 = 1;
					}
					else if ((synpred53_Java()))
					{
						alt39 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 39, 14, input);

						throw nvae;

					}
				}
					break;
				case LT:
				case VOID:
				{
					alt39 = 2;
				}
					break;
				case CLASS:
				case ENUM:
				{
					alt39 = 3;
				}
					break;
				case INTERFACE:
				{
					alt39 = 4;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 39, 0, input);

					throw nvae;

			}

			switch (alt39)
			{
				case 1:
				// Java.g:504:10: fieldDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_fieldDeclaration_in_memberDecl1733);
					fieldDeclaration107 = fieldDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, fieldDeclaration107.getTree());
					}

				}
					break;
				case 2:
				// Java.g:505:10: methodDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_methodDeclaration_in_memberDecl1744);
					methodDeclaration108 = methodDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, methodDeclaration108.getTree());
					}

				}
					break;
				case 3:
				// Java.g:506:10: classDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classDeclaration_in_memberDecl1755);
					classDeclaration109 = classDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classDeclaration109.getTree());
					}

				}
					break;
				case 4:
				// Java.g:507:10: interfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl1766);
					interfaceDeclaration110 = interfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, interfaceDeclaration110.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 25, memberDecl_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "memberDecl"

	public static class methodDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "methodDeclaration"
	// Java.g:511:1: methodDeclaration : ( modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' | modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( block | ';' ) );
	public final JavaParser.methodDeclaration_return methodDeclaration() throws RecognitionException
	{
		JavaParser.methodDeclaration_return retval = new JavaParser.methodDeclaration_return();
		retval.start = input.LT(1);

		int methodDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER113 = null;
		Token string_literal115 = null;
		Token char_literal117 = null;
		Token char_literal120 = null;
		Token string_literal124 = null;
		Token IDENTIFIER125 = null;
		Token char_literal127 = null;
		Token char_literal128 = null;
		Token string_literal129 = null;
		Token char_literal132 = null;
		JavaParser.modifiers_return modifiers111 = null;

		JavaParser.typeParameters_return typeParameters112 = null;

		JavaParser.formalParameters_return formalParameters114 = null;

		JavaParser.qualifiedNameList_return qualifiedNameList116 = null;

		JavaParser.explicitConstructorInvocation_return explicitConstructorInvocation118 = null;

		JavaParser.blockStatement_return blockStatement119 = null;

		JavaParser.modifiers_return modifiers121 = null;

		JavaParser.typeParameters_return typeParameters122 = null;

		JavaParser.type_return type123 = null;

		JavaParser.formalParameters_return formalParameters126 = null;

		JavaParser.qualifiedNameList_return qualifiedNameList130 = null;

		JavaParser.block_return block131 = null;

		CommonTree IDENTIFIER113_tree = null;
		CommonTree string_literal115_tree = null;
		CommonTree char_literal117_tree = null;
		CommonTree char_literal120_tree = null;
		CommonTree string_literal124_tree = null;
		CommonTree IDENTIFIER125_tree = null;
		CommonTree char_literal127_tree = null;
		CommonTree char_literal128_tree = null;
		CommonTree string_literal129_tree = null;
		CommonTree char_literal132_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 26)) { return retval; }

			// Java.g:512:5: ( modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' | modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( block | ';' ) )
			int alt49 = 2;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA49_1 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA49_2 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA49_3 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA49_4 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA49_5 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA49_6 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA49_7 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA49_8 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA49_9 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA49_10 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA49_11 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA49_12 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 12, input);

						throw nvae;

					}
				}
					break;
				case LT:
				{
					int LA49_13 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 13, input);

						throw nvae;

					}
				}
					break;
				case IDENTIFIER:
				{
					int LA49_14 = input.LA(2);

					if ((synpred59_Java()))
					{
						alt49 = 1;
					}
					else if ((true))
					{
						alt49 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 49, 14, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				case VOID:
				{
					alt49 = 2;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 49, 0, input);

					throw nvae;

			}

			switch (alt49)
			{
				case 1:
				// Java.g:514:10: modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_modifiers_in_methodDeclaration1804);
					modifiers111 = modifiers();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, modifiers111.getTree());
					}

					// Java.g:515:9: ( typeParameters )?
					int alt40 = 2;
					int LA40_0 = input.LA(1);

					if ((LA40_0 == LT))
					{
						alt40 = 1;
					}
					switch (alt40)
					{
						case 1:
						// Java.g:515:10: typeParameters
						{
							pushFollow(FOLLOW_typeParameters_in_methodDeclaration1815);
							typeParameters112 = typeParameters();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, typeParameters112.getTree());
							}

						}
							break;

					}

					IDENTIFIER113 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_methodDeclaration1836);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER113_tree = (CommonTree) adaptor.create(IDENTIFIER113);
						adaptor.addChild(root_0, IDENTIFIER113_tree);
					}

					pushFollow(FOLLOW_formalParameters_in_methodDeclaration1846);
					formalParameters114 = formalParameters();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, formalParameters114.getTree());
					}

					// Java.g:519:9: ( 'throws' qualifiedNameList )?
					int alt41 = 2;
					int LA41_0 = input.LA(1);

					if ((LA41_0 == THROWS))
					{
						alt41 = 1;
					}
					switch (alt41)
					{
						case 1:
						// Java.g:519:10: 'throws' qualifiedNameList
						{
							string_literal115 = (Token) match(input, THROWS, FOLLOW_THROWS_in_methodDeclaration1857);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal115_tree = (CommonTree) adaptor.create(string_literal115);
								adaptor.addChild(root_0, string_literal115_tree);
							}

							pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration1859);
							qualifiedNameList116 = qualifiedNameList();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, qualifiedNameList116.getTree());
							}

						}
							break;

					}

					char_literal117 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_methodDeclaration1880);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal117_tree = (CommonTree) adaptor.create(char_literal117);
						adaptor.addChild(root_0, char_literal117_tree);
					}

					// Java.g:522:9: ( explicitConstructorInvocation )?
					int alt42 = 2;
					switch (input.LA(1))
					{
						case LT:
						{
							alt42 = 1;
						}
							break;
						case THIS:
						{
							int LA42_2 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case LPAREN:
						{
							int LA42_3 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case SUPER:
						{
							int LA42_4 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case IDENTIFIER:
						{
							int LA42_5 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case CHARLITERAL:
						case DOUBLELITERAL:
						case FALSE:
						case FLOATLITERAL:
						case INTLITERAL:
						case LONGLITERAL:
						case NULL:
						case STRINGLITERAL:
						case TRUE:
						{
							int LA42_6 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case NEW:
						{
							int LA42_7 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case BOOLEAN:
						case BYTE:
						case CHAR:
						case DOUBLE:
						case FLOAT:
						case INT:
						case LONG:
						case SHORT:
						{
							int LA42_8 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
						case VOID:
						{
							int LA42_9 = input.LA(2);

							if ((synpred57_Java()))
							{
								alt42 = 1;
							}
						}
							break;
					}

					switch (alt42)
					{
						case 1:
						// Java.g:522:10: explicitConstructorInvocation
						{
							pushFollow(FOLLOW_explicitConstructorInvocation_in_methodDeclaration1892);
							explicitConstructorInvocation118 = explicitConstructorInvocation();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, explicitConstructorInvocation118.getTree());
							}

						}
							break;

					}

					// Java.g:524:9: ( blockStatement )*
					loop43:
					do
					{
						int alt43 = 2;
						int LA43_0 = input.LA(1);

						if (((LA43_0 == ABSTRACT) || ((LA43_0 >= ASSERT) && (LA43_0 <= BANG)) || ((LA43_0 >= BOOLEAN) && (LA43_0 <= BYTE)) || ((LA43_0 >= CHAR) && (LA43_0 <= CLASS)) || (LA43_0 == CONTINUE) || (LA43_0 == DO) || ((LA43_0 >= DOUBLE) && (LA43_0 <= DOUBLELITERAL)) || (LA43_0 == ENUM) || ((LA43_0 >= FALSE) && (LA43_0 <= FINAL)) || ((LA43_0 >= FLOAT) && (LA43_0 <= FOR)) || ((LA43_0 >= IDENTIFIER) && (LA43_0 <= IF)) || ((LA43_0 >= INT) && (LA43_0 <= INTLITERAL)) || (LA43_0 == LBRACE) || ((LA43_0 >= LONG) && (LA43_0 <= LT)) || ((LA43_0 >= MONKEYS_AT) && (LA43_0 <= NULL)) || (LA43_0 == PLUS) || ((LA43_0 >= PLUSPLUS) && (LA43_0 <= PUBLIC)) || (LA43_0 == RETURN) || ((LA43_0 >= SEMI) && (LA43_0 <= SHORT)) || ((LA43_0 >= STATIC) && (LA43_0 <= SUB)) || ((LA43_0 >= SUBSUB) && (LA43_0 <= SYNCHRONIZED)) || ((LA43_0 >= THIS) && (LA43_0 <= THROW)) || ((LA43_0 >= TILDE) && (LA43_0 <= WHILE))))
						{
							alt43 = 1;
						}

						switch (alt43)
						{
							case 1:
							// Java.g:524:10: blockStatement
							{
								pushFollow(FOLLOW_blockStatement_in_methodDeclaration1914);
								blockStatement119 = blockStatement();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, blockStatement119.getTree());
								}

							}
								break;

							default:
								break loop43;
						}
					}
					while (true);

					char_literal120 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_methodDeclaration1935);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal120_tree = (CommonTree) adaptor.create(char_literal120);
						adaptor.addChild(root_0, char_literal120_tree);
					}

				}
					break;
				case 2:
				// Java.g:527:9: modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( block | ';' )
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_modifiers_in_methodDeclaration1945);
					modifiers121 = modifiers();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, modifiers121.getTree());
					}

					// Java.g:528:9: ( typeParameters )?
					int alt44 = 2;
					int LA44_0 = input.LA(1);

					if ((LA44_0 == LT))
					{
						alt44 = 1;
					}
					switch (alt44)
					{
						case 1:
						// Java.g:528:10: typeParameters
						{
							pushFollow(FOLLOW_typeParameters_in_methodDeclaration1956);
							typeParameters122 = typeParameters();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, typeParameters122.getTree());
							}

						}
							break;

					}

					// Java.g:530:9: ( type | 'void' )
					int alt45 = 2;
					int LA45_0 = input.LA(1);

					if (((LA45_0 == BOOLEAN) || (LA45_0 == BYTE) || (LA45_0 == CHAR) || (LA45_0 == DOUBLE) || (LA45_0 == FLOAT) || (LA45_0 == IDENTIFIER) || (LA45_0 == INT) || (LA45_0 == LONG) || (LA45_0 == SHORT)))
					{
						alt45 = 1;
					}
					else if ((LA45_0 == VOID))
					{
						alt45 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 45, 0, input);

						throw nvae;

					}
					switch (alt45)
					{
						case 1:
						// Java.g:530:10: type
						{
							pushFollow(FOLLOW_type_in_methodDeclaration1978);
							type123 = type();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, type123.getTree());
							}

						}
							break;
						case 2:
						// Java.g:531:13: 'void'
						{
							string_literal124 = (Token) match(input, VOID, FOLLOW_VOID_in_methodDeclaration1992);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal124_tree = (CommonTree) adaptor.create(string_literal124);
								adaptor.addChild(root_0, string_literal124_tree);
							}

						}
							break;

					}

					IDENTIFIER125 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_methodDeclaration2012);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER125_tree = (CommonTree) adaptor.create(IDENTIFIER125);
						adaptor.addChild(root_0, IDENTIFIER125_tree);
					}

					pushFollow(FOLLOW_formalParameters_in_methodDeclaration2022);
					formalParameters126 = formalParameters();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, formalParameters126.getTree());
					}

					// Java.g:535:9: ( '[' ']' )*
					loop46:
					do
					{
						int alt46 = 2;
						int LA46_0 = input.LA(1);

						if ((LA46_0 == LBRACKET))
						{
							alt46 = 1;
						}

						switch (alt46)
						{
							case 1:
							// Java.g:535:10: '[' ']'
							{
								char_literal127 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_methodDeclaration2033);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal127_tree = (CommonTree) adaptor.create(char_literal127);
									adaptor.addChild(root_0, char_literal127_tree);
								}

								char_literal128 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_methodDeclaration2035);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal128_tree = (CommonTree) adaptor.create(char_literal128);
									adaptor.addChild(root_0, char_literal128_tree);
								}

							}
								break;

							default:
								break loop46;
						}
					}
					while (true);

					// Java.g:537:9: ( 'throws' qualifiedNameList )?
					int alt47 = 2;
					int LA47_0 = input.LA(1);

					if ((LA47_0 == THROWS))
					{
						alt47 = 1;
					}
					switch (alt47)
					{
						case 1:
						// Java.g:537:10: 'throws' qualifiedNameList
						{
							string_literal129 = (Token) match(input, THROWS, FOLLOW_THROWS_in_methodDeclaration2057);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal129_tree = (CommonTree) adaptor.create(string_literal129);
								adaptor.addChild(root_0, string_literal129_tree);
							}

							pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration2059);
							qualifiedNameList130 = qualifiedNameList();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, qualifiedNameList130.getTree());
							}

						}
							break;

					}

					// Java.g:539:9: ( block | ';' )
					int alt48 = 2;
					int LA48_0 = input.LA(1);

					if ((LA48_0 == LBRACE))
					{
						alt48 = 1;
					}
					else if ((LA48_0 == SEMI))
					{
						alt48 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 48, 0, input);

						throw nvae;

					}
					switch (alt48)
					{
						case 1:
						// Java.g:540:13: block
						{
							pushFollow(FOLLOW_block_in_methodDeclaration2114);
							block131 = block();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, block131.getTree());
							}

						}
							break;
						case 2:
						// Java.g:541:13: ';'
						{
							char_literal132 = (Token) match(input, SEMI, FOLLOW_SEMI_in_methodDeclaration2128);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal132_tree = (CommonTree) adaptor.create(char_literal132);
								adaptor.addChild(root_0, char_literal132_tree);
							}

						}
							break;

					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 26, methodDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "methodDeclaration"

	public static class fieldDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "fieldDeclaration"
	// Java.g:546:1: fieldDeclaration : modifiers type variableDeclarator ( ',' variableDeclarator )* ';' ;
	public final JavaParser.fieldDeclaration_return fieldDeclaration() throws RecognitionException
	{
		JavaParser.fieldDeclaration_return retval = new JavaParser.fieldDeclaration_return();
		retval.start = input.LT(1);

		int fieldDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal136 = null;
		Token char_literal138 = null;
		JavaParser.modifiers_return modifiers133 = null;

		JavaParser.type_return type134 = null;

		JavaParser.variableDeclarator_return variableDeclarator135 = null;

		JavaParser.variableDeclarator_return variableDeclarator137 = null;

		CommonTree char_literal136_tree = null;
		CommonTree char_literal138_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 27)) { return retval; }

			// Java.g:547:5: ( modifiers type variableDeclarator ( ',' variableDeclarator )* ';' )
			// Java.g:547:9: modifiers type variableDeclarator ( ',' variableDeclarator )* ';'
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_fieldDeclaration2160);
				modifiers133 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers133.getTree());
				}

				pushFollow(FOLLOW_type_in_fieldDeclaration2170);
				type134 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type134.getTree());
				}

				pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration2180);
				variableDeclarator135 = variableDeclarator();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableDeclarator135.getTree());
				}

				// Java.g:550:9: ( ',' variableDeclarator )*
				loop50:
				do
				{
					int alt50 = 2;
					int LA50_0 = input.LA(1);

					if ((LA50_0 == COMMA))
					{
						alt50 = 1;
					}

					switch (alt50)
					{
						case 1:
						// Java.g:550:10: ',' variableDeclarator
						{
							char_literal136 = (Token) match(input, COMMA, FOLLOW_COMMA_in_fieldDeclaration2191);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal136_tree = (CommonTree) adaptor.create(char_literal136);
								adaptor.addChild(root_0, char_literal136_tree);
							}

							pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration2193);
							variableDeclarator137 = variableDeclarator();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, variableDeclarator137.getTree());
							}

						}
							break;

						default:
							break loop50;
					}
				}
				while (true);

				char_literal138 = (Token) match(input, SEMI, FOLLOW_SEMI_in_fieldDeclaration2214);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal138_tree = (CommonTree) adaptor.create(char_literal138);
					adaptor.addChild(root_0, char_literal138_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 27, fieldDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "fieldDeclaration"

	public static class variableDeclarator_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "variableDeclarator"
	// Java.g:555:1: variableDeclarator : IDENTIFIER ( '[' ']' )* ( '=' variableInitializer )? ;
	public final JavaParser.variableDeclarator_return variableDeclarator() throws RecognitionException
	{
		JavaParser.variableDeclarator_return retval = new JavaParser.variableDeclarator_return();
		retval.start = input.LT(1);

		int variableDeclarator_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER139 = null;
		Token char_literal140 = null;
		Token char_literal141 = null;
		Token char_literal142 = null;
		JavaParser.variableInitializer_return variableInitializer143 = null;

		CommonTree IDENTIFIER139_tree = null;
		CommonTree char_literal140_tree = null;
		CommonTree char_literal141_tree = null;
		CommonTree char_literal142_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 28)) { return retval; }

			// Java.g:556:5: ( IDENTIFIER ( '[' ']' )* ( '=' variableInitializer )? )
			// Java.g:556:9: IDENTIFIER ( '[' ']' )* ( '=' variableInitializer )?
			{
				root_0 = (CommonTree) adaptor.nil();

				IDENTIFIER139 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_variableDeclarator2234);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER139_tree = (CommonTree) adaptor.create(IDENTIFIER139);
					adaptor.addChild(root_0, IDENTIFIER139_tree);
				}

				// Java.g:557:9: ( '[' ']' )*
				loop51:
				do
				{
					int alt51 = 2;
					int LA51_0 = input.LA(1);

					if ((LA51_0 == LBRACKET))
					{
						alt51 = 1;
					}

					switch (alt51)
					{
						case 1:
						// Java.g:557:10: '[' ']'
						{
							char_literal140 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_variableDeclarator2245);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal140_tree = (CommonTree) adaptor.create(char_literal140);
								adaptor.addChild(root_0, char_literal140_tree);
							}

							char_literal141 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_variableDeclarator2247);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal141_tree = (CommonTree) adaptor.create(char_literal141);
								adaptor.addChild(root_0, char_literal141_tree);
							}

						}
							break;

						default:
							break loop51;
					}
				}
				while (true);

				// Java.g:559:9: ( '=' variableInitializer )?
				int alt52 = 2;
				int LA52_0 = input.LA(1);

				if ((LA52_0 == EQ))
				{
					alt52 = 1;
				}
				switch (alt52)
				{
					case 1:
					// Java.g:559:10: '=' variableInitializer
					{
						char_literal142 = (Token) match(input, EQ, FOLLOW_EQ_in_variableDeclarator2269);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal142_tree = (CommonTree) adaptor.create(char_literal142);
							adaptor.addChild(root_0, char_literal142_tree);
						}

						pushFollow(FOLLOW_variableInitializer_in_variableDeclarator2271);
						variableInitializer143 = variableInitializer();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, variableInitializer143.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 28, variableDeclarator_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "variableDeclarator"

	public static class interfaceBodyDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "interfaceBodyDeclaration"
	// Java.g:566:1: interfaceBodyDeclaration : ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' );
	public final JavaParser.interfaceBodyDeclaration_return interfaceBodyDeclaration() throws RecognitionException
	{
		JavaParser.interfaceBodyDeclaration_return retval = new JavaParser.interfaceBodyDeclaration_return();
		retval.start = input.LT(1);

		int interfaceBodyDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal148 = null;
		JavaParser.interfaceFieldDeclaration_return interfaceFieldDeclaration144 = null;

		JavaParser.interfaceMethodDeclaration_return interfaceMethodDeclaration145 = null;

		JavaParser.interfaceDeclaration_return interfaceDeclaration146 = null;

		JavaParser.classDeclaration_return classDeclaration147 = null;

		CommonTree char_literal148_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 29)) { return retval; }

			// Java.g:567:5: ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' )
			int alt53 = 5;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA53_1 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA53_2 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA53_3 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA53_4 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA53_5 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA53_6 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA53_7 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA53_8 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA53_9 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA53_10 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA53_11 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA53_12 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else if ((synpred70_Java()))
					{
						alt53 = 3;
					}
					else if ((synpred71_Java()))
					{
						alt53 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 12, input);

						throw nvae;

					}
				}
					break;
				case IDENTIFIER:
				{
					int LA53_13 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 13, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA53_14 = input.LA(2);

					if ((synpred68_Java()))
					{
						alt53 = 1;
					}
					else if ((synpred69_Java()))
					{
						alt53 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 53, 14, input);

						throw nvae;

					}
				}
					break;
				case LT:
				case VOID:
				{
					alt53 = 2;
				}
					break;
				case INTERFACE:
				{
					alt53 = 3;
				}
					break;
				case CLASS:
				case ENUM:
				{
					alt53 = 4;
				}
					break;
				case SEMI:
				{
					alt53 = 5;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 53, 0, input);

					throw nvae;

			}

			switch (alt53)
			{
				case 1:
				// Java.g:568:9: interfaceFieldDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration2310);
					interfaceFieldDeclaration144 = interfaceFieldDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, interfaceFieldDeclaration144.getTree());
					}

				}
					break;
				case 2:
				// Java.g:569:9: interfaceMethodDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration2320);
					interfaceMethodDeclaration145 = interfaceMethodDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, interfaceMethodDeclaration145.getTree());
					}

				}
					break;
				case 3:
				// Java.g:570:9: interfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration2330);
					interfaceDeclaration146 = interfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, interfaceDeclaration146.getTree());
					}

				}
					break;
				case 4:
				// Java.g:571:9: classDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classDeclaration_in_interfaceBodyDeclaration2340);
					classDeclaration147 = classDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classDeclaration147.getTree());
					}

				}
					break;
				case 5:
				// Java.g:572:9: ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal148 = (Token) match(input, SEMI, FOLLOW_SEMI_in_interfaceBodyDeclaration2350);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal148_tree = (CommonTree) adaptor.create(char_literal148);
						adaptor.addChild(root_0, char_literal148_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 29, interfaceBodyDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "interfaceBodyDeclaration"

	public static class interfaceMethodDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "interfaceMethodDeclaration"
	// Java.g:575:1: interfaceMethodDeclaration : modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' ;
	public final JavaParser.interfaceMethodDeclaration_return interfaceMethodDeclaration() throws RecognitionException
	{
		JavaParser.interfaceMethodDeclaration_return retval = new JavaParser.interfaceMethodDeclaration_return();
		retval.start = input.LT(1);

		int interfaceMethodDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal152 = null;
		Token IDENTIFIER153 = null;
		Token char_literal155 = null;
		Token char_literal156 = null;
		Token string_literal157 = null;
		Token char_literal159 = null;
		JavaParser.modifiers_return modifiers149 = null;

		JavaParser.typeParameters_return typeParameters150 = null;

		JavaParser.type_return type151 = null;

		JavaParser.formalParameters_return formalParameters154 = null;

		JavaParser.qualifiedNameList_return qualifiedNameList158 = null;

		CommonTree string_literal152_tree = null;
		CommonTree IDENTIFIER153_tree = null;
		CommonTree char_literal155_tree = null;
		CommonTree char_literal156_tree = null;
		CommonTree string_literal157_tree = null;
		CommonTree char_literal159_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 30)) { return retval; }

			// Java.g:576:5: ( modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' )
			// Java.g:576:9: modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';'
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_interfaceMethodDeclaration2370);
				modifiers149 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers149.getTree());
				}

				// Java.g:577:9: ( typeParameters )?
				int alt54 = 2;
				int LA54_0 = input.LA(1);

				if ((LA54_0 == LT))
				{
					alt54 = 1;
				}
				switch (alt54)
				{
					case 1:
					// Java.g:577:10: typeParameters
					{
						pushFollow(FOLLOW_typeParameters_in_interfaceMethodDeclaration2381);
						typeParameters150 = typeParameters();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeParameters150.getTree());
						}

					}
						break;

				}

				// Java.g:579:9: ( type | 'void' )
				int alt55 = 2;
				int LA55_0 = input.LA(1);

				if (((LA55_0 == BOOLEAN) || (LA55_0 == BYTE) || (LA55_0 == CHAR) || (LA55_0 == DOUBLE) || (LA55_0 == FLOAT) || (LA55_0 == IDENTIFIER) || (LA55_0 == INT) || (LA55_0 == LONG) || (LA55_0 == SHORT)))
				{
					alt55 = 1;
				}
				else if ((LA55_0 == VOID))
				{
					alt55 = 2;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 55, 0, input);

					throw nvae;

				}
				switch (alt55)
				{
					case 1:
					// Java.g:579:10: type
					{
						pushFollow(FOLLOW_type_in_interfaceMethodDeclaration2403);
						type151 = type();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, type151.getTree());
						}

					}
						break;
					case 2:
					// Java.g:580:10: 'void'
					{
						string_literal152 = (Token) match(input, VOID, FOLLOW_VOID_in_interfaceMethodDeclaration2414);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal152_tree = (CommonTree) adaptor.create(string_literal152);
							adaptor.addChild(root_0, string_literal152_tree);
						}

					}
						break;

				}

				IDENTIFIER153 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration2434);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER153_tree = (CommonTree) adaptor.create(IDENTIFIER153);
					adaptor.addChild(root_0, IDENTIFIER153_tree);
				}

				pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaration2444);
				formalParameters154 = formalParameters();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, formalParameters154.getTree());
				}

				// Java.g:584:9: ( '[' ']' )*
				loop56:
				do
				{
					int alt56 = 2;
					int LA56_0 = input.LA(1);

					if ((LA56_0 == LBRACKET))
					{
						alt56 = 1;
					}

					switch (alt56)
					{
						case 1:
						// Java.g:584:10: '[' ']'
						{
							char_literal155 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_interfaceMethodDeclaration2455);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal155_tree = (CommonTree) adaptor.create(char_literal155);
								adaptor.addChild(root_0, char_literal155_tree);
							}

							char_literal156 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_interfaceMethodDeclaration2457);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal156_tree = (CommonTree) adaptor.create(char_literal156);
								adaptor.addChild(root_0, char_literal156_tree);
							}

						}
							break;

						default:
							break loop56;
					}
				}
				while (true);

				// Java.g:586:9: ( 'throws' qualifiedNameList )?
				int alt57 = 2;
				int LA57_0 = input.LA(1);

				if ((LA57_0 == THROWS))
				{
					alt57 = 1;
				}
				switch (alt57)
				{
					case 1:
					// Java.g:586:10: 'throws' qualifiedNameList
					{
						string_literal157 = (Token) match(input, THROWS, FOLLOW_THROWS_in_interfaceMethodDeclaration2479);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal157_tree = (CommonTree) adaptor.create(string_literal157);
							adaptor.addChild(root_0, string_literal157_tree);
						}

						pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration2481);
						qualifiedNameList158 = qualifiedNameList();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, qualifiedNameList158.getTree());
						}

					}
						break;

				}

				char_literal159 = (Token) match(input, SEMI, FOLLOW_SEMI_in_interfaceMethodDeclaration2494);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal159_tree = (CommonTree) adaptor.create(char_literal159);
					adaptor.addChild(root_0, char_literal159_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 30, interfaceMethodDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "interfaceMethodDeclaration"

	public static class interfaceFieldDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "interfaceFieldDeclaration"
	// Java.g:595:1: interfaceFieldDeclaration : modifiers type variableDeclarator ( ',' variableDeclarator )* ';' ;
	public final JavaParser.interfaceFieldDeclaration_return interfaceFieldDeclaration() throws RecognitionException
	{
		JavaParser.interfaceFieldDeclaration_return retval = new JavaParser.interfaceFieldDeclaration_return();
		retval.start = input.LT(1);

		int interfaceFieldDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal163 = null;
		Token char_literal165 = null;
		JavaParser.modifiers_return modifiers160 = null;

		JavaParser.type_return type161 = null;

		JavaParser.variableDeclarator_return variableDeclarator162 = null;

		JavaParser.variableDeclarator_return variableDeclarator164 = null;

		CommonTree char_literal163_tree = null;
		CommonTree char_literal165_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 31)) { return retval; }

			// Java.g:596:5: ( modifiers type variableDeclarator ( ',' variableDeclarator )* ';' )
			// Java.g:596:9: modifiers type variableDeclarator ( ',' variableDeclarator )* ';'
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_interfaceFieldDeclaration2516);
				modifiers160 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers160.getTree());
				}

				pushFollow(FOLLOW_type_in_interfaceFieldDeclaration2518);
				type161 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type161.getTree());
				}

				pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2520);
				variableDeclarator162 = variableDeclarator();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableDeclarator162.getTree());
				}

				// Java.g:597:9: ( ',' variableDeclarator )*
				loop58:
				do
				{
					int alt58 = 2;
					int LA58_0 = input.LA(1);

					if ((LA58_0 == COMMA))
					{
						alt58 = 1;
					}

					switch (alt58)
					{
						case 1:
						// Java.g:597:10: ',' variableDeclarator
						{
							char_literal163 = (Token) match(input, COMMA, FOLLOW_COMMA_in_interfaceFieldDeclaration2531);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal163_tree = (CommonTree) adaptor.create(char_literal163);
								adaptor.addChild(root_0, char_literal163_tree);
							}

							pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2533);
							variableDeclarator164 = variableDeclarator();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, variableDeclarator164.getTree());
							}

						}
							break;

						default:
							break loop58;
					}
				}
				while (true);

				char_literal165 = (Token) match(input, SEMI, FOLLOW_SEMI_in_interfaceFieldDeclaration2554);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal165_tree = (CommonTree) adaptor.create(char_literal165);
					adaptor.addChild(root_0, char_literal165_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 31, interfaceFieldDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "interfaceFieldDeclaration"

	public static class type_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "type"
	// Java.g:603:1: type : ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* );
	public final JavaParser.type_return type() throws RecognitionException
	{
		JavaParser.type_return retval = new JavaParser.type_return();
		retval.start = input.LT(1);

		int type_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal167 = null;
		Token char_literal168 = null;
		Token char_literal170 = null;
		Token char_literal171 = null;
		JavaParser.classOrInterfaceType_return classOrInterfaceType166 = null;

		JavaParser.primitiveType_return primitiveType169 = null;

		CommonTree char_literal167_tree = null;
		CommonTree char_literal168_tree = null;
		CommonTree char_literal170_tree = null;
		CommonTree char_literal171_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 32)) { return retval; }

			// Java.g:604:5: ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* )
			int alt61 = 2;
			int LA61_0 = input.LA(1);

			if ((LA61_0 == IDENTIFIER))
			{
				alt61 = 1;
			}
			else if (((LA61_0 == BOOLEAN) || (LA61_0 == BYTE) || (LA61_0 == CHAR) || (LA61_0 == DOUBLE) || (LA61_0 == FLOAT) || (LA61_0 == INT) || (LA61_0 == LONG) || (LA61_0 == SHORT)))
			{
				alt61 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 61, 0, input);

				throw nvae;

			}
			switch (alt61)
			{
				case 1:
				// Java.g:604:9: classOrInterfaceType ( '[' ']' )*
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classOrInterfaceType_in_type2575);
					classOrInterfaceType166 = classOrInterfaceType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classOrInterfaceType166.getTree());
					}

					// Java.g:605:9: ( '[' ']' )*
					loop59:
					do
					{
						int alt59 = 2;
						int LA59_0 = input.LA(1);

						if ((LA59_0 == LBRACKET))
						{
							alt59 = 1;
						}

						switch (alt59)
						{
							case 1:
							// Java.g:605:10: '[' ']'
							{
								char_literal167 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_type2586);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal167_tree = (CommonTree) adaptor.create(char_literal167);
									adaptor.addChild(root_0, char_literal167_tree);
								}

								char_literal168 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_type2588);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal168_tree = (CommonTree) adaptor.create(char_literal168);
									adaptor.addChild(root_0, char_literal168_tree);
								}

							}
								break;

							default:
								break loop59;
						}
					}
					while (true);

				}
					break;
				case 2:
				// Java.g:607:9: primitiveType ( '[' ']' )*
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_primitiveType_in_type2609);
					primitiveType169 = primitiveType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, primitiveType169.getTree());
					}

					// Java.g:608:9: ( '[' ']' )*
					loop60:
					do
					{
						int alt60 = 2;
						int LA60_0 = input.LA(1);

						if ((LA60_0 == LBRACKET))
						{
							alt60 = 1;
						}

						switch (alt60)
						{
							case 1:
							// Java.g:608:10: '[' ']'
							{
								char_literal170 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_type2620);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal170_tree = (CommonTree) adaptor.create(char_literal170);
									adaptor.addChild(root_0, char_literal170_tree);
								}

								char_literal171 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_type2622);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal171_tree = (CommonTree) adaptor.create(char_literal171);
									adaptor.addChild(root_0, char_literal171_tree);
								}

							}
								break;

							default:
								break loop60;
						}
					}
					while (true);

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 32, type_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "type"

	public static class classOrInterfaceType_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classOrInterfaceType"
	// Java.g:613:1: classOrInterfaceType : IDENTIFIER ( typeArguments )? ( '.' IDENTIFIER ( typeArguments )? )* ;
	public final JavaParser.classOrInterfaceType_return classOrInterfaceType() throws RecognitionException
	{
		JavaParser.classOrInterfaceType_return retval = new JavaParser.classOrInterfaceType_return();
		retval.start = input.LT(1);

		int classOrInterfaceType_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER172 = null;
		Token char_literal174 = null;
		Token IDENTIFIER175 = null;
		JavaParser.typeArguments_return typeArguments173 = null;

		JavaParser.typeArguments_return typeArguments176 = null;

		CommonTree IDENTIFIER172_tree = null;
		CommonTree char_literal174_tree = null;
		CommonTree IDENTIFIER175_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 33)) { return retval; }

			// Java.g:614:5: ( IDENTIFIER ( typeArguments )? ( '.' IDENTIFIER ( typeArguments )? )* )
			// Java.g:614:9: IDENTIFIER ( typeArguments )? ( '.' IDENTIFIER ( typeArguments )? )*
			{
				root_0 = (CommonTree) adaptor.nil();

				IDENTIFIER172 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_classOrInterfaceType2654);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER172_tree = (CommonTree) adaptor.create(IDENTIFIER172);
					adaptor.addChild(root_0, IDENTIFIER172_tree);
				}

				// Java.g:615:9: ( typeArguments )?
				int alt62 = 2;
				int LA62_0 = input.LA(1);

				if ((LA62_0 == LT))
				{
					int LA62_1 = input.LA(2);

					if (((LA62_1 == BOOLEAN) || (LA62_1 == BYTE) || (LA62_1 == CHAR) || (LA62_1 == DOUBLE) || (LA62_1 == FLOAT) || (LA62_1 == IDENTIFIER) || (LA62_1 == INT) || (LA62_1 == LONG) || (LA62_1 == QUES) || (LA62_1 == SHORT)))
					{
						alt62 = 1;
					}
				}
				switch (alt62)
				{
					case 1:
					// Java.g:615:10: typeArguments
					{
						pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2665);
						typeArguments173 = typeArguments();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeArguments173.getTree());
						}

					}
						break;

				}

				// Java.g:617:9: ( '.' IDENTIFIER ( typeArguments )? )*
				loop64:
				do
				{
					int alt64 = 2;
					int LA64_0 = input.LA(1);

					if ((LA64_0 == DOT))
					{
						alt64 = 1;
					}

					switch (alt64)
					{
						case 1:
						// Java.g:617:10: '.' IDENTIFIER ( typeArguments )?
						{
							char_literal174 = (Token) match(input, DOT, FOLLOW_DOT_in_classOrInterfaceType2687);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal174_tree = (CommonTree) adaptor.create(char_literal174);
								adaptor.addChild(root_0, char_literal174_tree);
							}

							IDENTIFIER175 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_classOrInterfaceType2689);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								IDENTIFIER175_tree = (CommonTree) adaptor.create(IDENTIFIER175);
								adaptor.addChild(root_0, IDENTIFIER175_tree);
							}

							// Java.g:618:13: ( typeArguments )?
							int alt63 = 2;
							int LA63_0 = input.LA(1);

							if ((LA63_0 == LT))
							{
								int LA63_1 = input.LA(2);

								if (((LA63_1 == BOOLEAN) || (LA63_1 == BYTE) || (LA63_1 == CHAR) || (LA63_1 == DOUBLE) || (LA63_1 == FLOAT) || (LA63_1 == IDENTIFIER) || (LA63_1 == INT) || (LA63_1 == LONG) || (LA63_1 == QUES) || (LA63_1 == SHORT)))
								{
									alt63 = 1;
								}
							}
							switch (alt63)
							{
								case 1:
								// Java.g:618:14: typeArguments
								{
									pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2704);
									typeArguments176 = typeArguments();

									state._fsp--;
									if (state.failed) { return retval; }
									if (state.backtracking == 0)
									{
										adaptor.addChild(root_0, typeArguments176.getTree());
									}

								}
									break;

							}

						}
							break;

						default:
							break loop64;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 33, classOrInterfaceType_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classOrInterfaceType"

	public static class primitiveType_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "primitiveType"
	// Java.g:623:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
	public final JavaParser.primitiveType_return primitiveType() throws RecognitionException
	{
		JavaParser.primitiveType_return retval = new JavaParser.primitiveType_return();
		retval.start = input.LT(1);

		int primitiveType_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set177 = null;

		CommonTree set177_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 34)) { return retval; }

			// Java.g:624:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
			// Java.g:
			{
				root_0 = (CommonTree) adaptor.nil();

				set177 = (Token) input.LT(1);

				if ((input.LA(1) == BOOLEAN) || (input.LA(1) == BYTE) || (input.LA(1) == CHAR) || (input.LA(1) == DOUBLE) || (input.LA(1) == FLOAT) || (input.LA(1) == INT) || (input.LA(1) == LONG) || (input.LA(1) == SHORT))
				{
					input.consume();
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, (CommonTree) adaptor.create(set177));
					}
					state.errorRecovery = false;
					state.failed = false;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					MismatchedSetException mse = new MismatchedSetException(null, input);
					throw mse;
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 34, primitiveType_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "primitiveType"

	public static class typeArguments_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeArguments"
	// Java.g:634:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
	public final JavaParser.typeArguments_return typeArguments() throws RecognitionException
	{
		JavaParser.typeArguments_return retval = new JavaParser.typeArguments_return();
		retval.start = input.LT(1);

		int typeArguments_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal178 = null;
		Token char_literal180 = null;
		Token char_literal182 = null;
		JavaParser.typeArgument_return typeArgument179 = null;

		JavaParser.typeArgument_return typeArgument181 = null;

		CommonTree char_literal178_tree = null;
		CommonTree char_literal180_tree = null;
		CommonTree char_literal182_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 35)) { return retval; }

			// Java.g:635:5: ( '<' typeArgument ( ',' typeArgument )* '>' )
			// Java.g:635:9: '<' typeArgument ( ',' typeArgument )* '>'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal178 = (Token) match(input, LT, FOLLOW_LT_in_typeArguments2841);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal178_tree = (CommonTree) adaptor.create(char_literal178);
					adaptor.addChild(root_0, char_literal178_tree);
				}

				pushFollow(FOLLOW_typeArgument_in_typeArguments2843);
				typeArgument179 = typeArgument();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, typeArgument179.getTree());
				}

				// Java.g:636:9: ( ',' typeArgument )*
				loop65:
				do
				{
					int alt65 = 2;
					int LA65_0 = input.LA(1);

					if ((LA65_0 == COMMA))
					{
						alt65 = 1;
					}

					switch (alt65)
					{
						case 1:
						// Java.g:636:10: ',' typeArgument
						{
							char_literal180 = (Token) match(input, COMMA, FOLLOW_COMMA_in_typeArguments2854);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal180_tree = (CommonTree) adaptor.create(char_literal180);
								adaptor.addChild(root_0, char_literal180_tree);
							}

							pushFollow(FOLLOW_typeArgument_in_typeArguments2856);
							typeArgument181 = typeArgument();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, typeArgument181.getTree());
							}

						}
							break;

						default:
							break loop65;
					}
				}
				while (true);

				char_literal182 = (Token) match(input, GT, FOLLOW_GT_in_typeArguments2878);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal182_tree = (CommonTree) adaptor.create(char_literal182);
					adaptor.addChild(root_0, char_literal182_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 35, typeArguments_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeArguments"

	public static class typeArgument_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeArgument"
	// Java.g:641:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
	public final JavaParser.typeArgument_return typeArgument() throws RecognitionException
	{
		JavaParser.typeArgument_return retval = new JavaParser.typeArgument_return();
		retval.start = input.LT(1);

		int typeArgument_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal184 = null;
		Token set185 = null;
		JavaParser.type_return type183 = null;

		JavaParser.type_return type186 = null;

		CommonTree char_literal184_tree = null;
		CommonTree set185_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 36)) { return retval; }

			// Java.g:642:5: ( type | '?' ( ( 'extends' | 'super' ) type )? )
			int alt67 = 2;
			int LA67_0 = input.LA(1);

			if (((LA67_0 == BOOLEAN) || (LA67_0 == BYTE) || (LA67_0 == CHAR) || (LA67_0 == DOUBLE) || (LA67_0 == FLOAT) || (LA67_0 == IDENTIFIER) || (LA67_0 == INT) || (LA67_0 == LONG) || (LA67_0 == SHORT)))
			{
				alt67 = 1;
			}
			else if ((LA67_0 == QUES))
			{
				alt67 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 67, 0, input);

				throw nvae;

			}
			switch (alt67)
			{
				case 1:
				// Java.g:642:9: type
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_type_in_typeArgument2898);
					type183 = type();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, type183.getTree());
					}

				}
					break;
				case 2:
				// Java.g:643:9: '?' ( ( 'extends' | 'super' ) type )?
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal184 = (Token) match(input, QUES, FOLLOW_QUES_in_typeArgument2908);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal184_tree = (CommonTree) adaptor.create(char_literal184);
						adaptor.addChild(root_0, char_literal184_tree);
					}

					// Java.g:644:9: ( ( 'extends' | 'super' ) type )?
					int alt66 = 2;
					int LA66_0 = input.LA(1);

					if (((LA66_0 == EXTENDS) || (LA66_0 == SUPER)))
					{
						alt66 = 1;
					}
					switch (alt66)
					{
						case 1:
						// Java.g:645:13: ( 'extends' | 'super' ) type
						{
							set185 = (Token) input.LT(1);

							if ((input.LA(1) == EXTENDS) || (input.LA(1) == SUPER))
							{
								input.consume();
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, (CommonTree) adaptor.create(set185));
								}
								state.errorRecovery = false;
								state.failed = false;
							}
							else
							{
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								MismatchedSetException mse = new MismatchedSetException(null, input);
								throw mse;
							}

							pushFollow(FOLLOW_type_in_typeArgument2976);
							type186 = type();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, type186.getTree());
							}

						}
							break;

					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 36, typeArgument_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeArgument"

	public static class qualifiedNameList_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "qualifiedNameList"
	// Java.g:652:1: qualifiedNameList : qualifiedName ( ',' qualifiedName )* ;
	public final JavaParser.qualifiedNameList_return qualifiedNameList() throws RecognitionException
	{
		JavaParser.qualifiedNameList_return retval = new JavaParser.qualifiedNameList_return();
		retval.start = input.LT(1);

		int qualifiedNameList_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal188 = null;
		JavaParser.qualifiedName_return qualifiedName187 = null;

		JavaParser.qualifiedName_return qualifiedName189 = null;

		CommonTree char_literal188_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 37)) { return retval; }

			// Java.g:653:5: ( qualifiedName ( ',' qualifiedName )* )
			// Java.g:653:9: qualifiedName ( ',' qualifiedName )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList3007);
				qualifiedName187 = qualifiedName();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, qualifiedName187.getTree());
				}

				// Java.g:654:9: ( ',' qualifiedName )*
				loop68:
				do
				{
					int alt68 = 2;
					int LA68_0 = input.LA(1);

					if ((LA68_0 == COMMA))
					{
						alt68 = 1;
					}

					switch (alt68)
					{
						case 1:
						// Java.g:654:10: ',' qualifiedName
						{
							char_literal188 = (Token) match(input, COMMA, FOLLOW_COMMA_in_qualifiedNameList3018);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal188_tree = (CommonTree) adaptor.create(char_literal188);
								adaptor.addChild(root_0, char_literal188_tree);
							}

							pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList3020);
							qualifiedName189 = qualifiedName();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, qualifiedName189.getTree());
							}

						}
							break;

						default:
							break loop68;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 37, qualifiedNameList_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "qualifiedNameList"

	public static class formalParameters_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "formalParameters"
	// Java.g:658:1: formalParameters : '(' ( formalParameterDecls )? ')' ;
	public final JavaParser.formalParameters_return formalParameters() throws RecognitionException
	{
		JavaParser.formalParameters_return retval = new JavaParser.formalParameters_return();
		retval.start = input.LT(1);

		int formalParameters_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal190 = null;
		Token char_literal192 = null;
		JavaParser.formalParameterDecls_return formalParameterDecls191 = null;

		CommonTree char_literal190_tree = null;
		CommonTree char_literal192_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 38)) { return retval; }

			// Java.g:659:5: ( '(' ( formalParameterDecls )? ')' )
			// Java.g:659:9: '(' ( formalParameterDecls )? ')'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal190 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_formalParameters3051);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal190_tree = (CommonTree) adaptor.create(char_literal190);
					adaptor.addChild(root_0, char_literal190_tree);
				}

				// Java.g:660:9: ( formalParameterDecls )?
				int alt69 = 2;
				int LA69_0 = input.LA(1);

				if (((LA69_0 == BOOLEAN) || (LA69_0 == BYTE) || (LA69_0 == CHAR) || (LA69_0 == DOUBLE) || (LA69_0 == FINAL) || (LA69_0 == FLOAT) || (LA69_0 == IDENTIFIER) || (LA69_0 == INT) || (LA69_0 == LONG) || (LA69_0 == MONKEYS_AT) || (LA69_0 == SHORT)))
				{
					alt69 = 1;
				}
				switch (alt69)
				{
					case 1:
					// Java.g:660:10: formalParameterDecls
					{
						pushFollow(FOLLOW_formalParameterDecls_in_formalParameters3062);
						formalParameterDecls191 = formalParameterDecls();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, formalParameterDecls191.getTree());
						}

					}
						break;

				}

				char_literal192 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_formalParameters3084);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal192_tree = (CommonTree) adaptor.create(char_literal192);
					adaptor.addChild(root_0, char_literal192_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 38, formalParameters_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "formalParameters"

	public static class formalParameterDecls_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "formalParameterDecls"
	// Java.g:665:1: formalParameterDecls : ( ellipsisParameterDecl | normalParameterDecl ( ',' normalParameterDecl )* | ( normalParameterDecl ',' )+ ellipsisParameterDecl );
	public final JavaParser.formalParameterDecls_return formalParameterDecls() throws RecognitionException
	{
		JavaParser.formalParameterDecls_return retval = new JavaParser.formalParameterDecls_return();
		retval.start = input.LT(1);

		int formalParameterDecls_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal195 = null;
		Token char_literal198 = null;
		JavaParser.ellipsisParameterDecl_return ellipsisParameterDecl193 = null;

		JavaParser.normalParameterDecl_return normalParameterDecl194 = null;

		JavaParser.normalParameterDecl_return normalParameterDecl196 = null;

		JavaParser.normalParameterDecl_return normalParameterDecl197 = null;

		JavaParser.ellipsisParameterDecl_return ellipsisParameterDecl199 = null;

		CommonTree char_literal195_tree = null;
		CommonTree char_literal198_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 39)) { return retval; }

			// Java.g:666:5: ( ellipsisParameterDecl | normalParameterDecl ( ',' normalParameterDecl )* | ( normalParameterDecl ',' )+ ellipsisParameterDecl )
			int alt72 = 3;
			switch (input.LA(1))
			{
				case FINAL:
				{
					int LA72_1 = input.LA(2);

					if ((synpred96_Java()))
					{
						alt72 = 1;
					}
					else if ((synpred98_Java()))
					{
						alt72 = 2;
					}
					else if ((true))
					{
						alt72 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 72, 1, input);

						throw nvae;

					}
				}
					break;
				case MONKEYS_AT:
				{
					int LA72_2 = input.LA(2);

					if ((synpred96_Java()))
					{
						alt72 = 1;
					}
					else if ((synpred98_Java()))
					{
						alt72 = 2;
					}
					else if ((true))
					{
						alt72 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 72, 2, input);

						throw nvae;

					}
				}
					break;
				case IDENTIFIER:
				{
					int LA72_3 = input.LA(2);

					if ((synpred96_Java()))
					{
						alt72 = 1;
					}
					else if ((synpred98_Java()))
					{
						alt72 = 2;
					}
					else if ((true))
					{
						alt72 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 72, 3, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA72_4 = input.LA(2);

					if ((synpred96_Java()))
					{
						alt72 = 1;
					}
					else if ((synpred98_Java()))
					{
						alt72 = 2;
					}
					else if ((true))
					{
						alt72 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 72, 4, input);

						throw nvae;

					}
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 72, 0, input);

					throw nvae;

			}

			switch (alt72)
			{
				case 1:
				// Java.g:666:9: ellipsisParameterDecl
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3104);
					ellipsisParameterDecl193 = ellipsisParameterDecl();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, ellipsisParameterDecl193.getTree());
					}

				}
					break;
				case 2:
				// Java.g:667:9: normalParameterDecl ( ',' normalParameterDecl )*
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3114);
					normalParameterDecl194 = normalParameterDecl();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, normalParameterDecl194.getTree());
					}

					// Java.g:668:9: ( ',' normalParameterDecl )*
					loop70:
					do
					{
						int alt70 = 2;
						int LA70_0 = input.LA(1);

						if ((LA70_0 == COMMA))
						{
							alt70 = 1;
						}

						switch (alt70)
						{
							case 1:
							// Java.g:668:10: ',' normalParameterDecl
							{
								char_literal195 = (Token) match(input, COMMA, FOLLOW_COMMA_in_formalParameterDecls3125);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal195_tree = (CommonTree) adaptor.create(char_literal195);
									adaptor.addChild(root_0, char_literal195_tree);
								}

								pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3127);
								normalParameterDecl196 = normalParameterDecl();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, normalParameterDecl196.getTree());
								}

							}
								break;

							default:
								break loop70;
						}
					}
					while (true);

				}
					break;
				case 3:
				// Java.g:670:9: ( normalParameterDecl ',' )+ ellipsisParameterDecl
				{
					root_0 = (CommonTree) adaptor.nil();

					// Java.g:670:9: ( normalParameterDecl ',' )+
					int cnt71 = 0;
					loop71:
					do
					{
						int alt71 = 2;
						switch (input.LA(1))
						{
							case FINAL:
							{
								int LA71_1 = input.LA(2);

								if ((synpred99_Java()))
								{
									alt71 = 1;
								}

							}
								break;
							case MONKEYS_AT:
							{
								int LA71_2 = input.LA(2);

								if ((synpred99_Java()))
								{
									alt71 = 1;
								}

							}
								break;
							case IDENTIFIER:
							{
								int LA71_3 = input.LA(2);

								if ((synpred99_Java()))
								{
									alt71 = 1;
								}

							}
								break;
							case BOOLEAN:
							case BYTE:
							case CHAR:
							case DOUBLE:
							case FLOAT:
							case INT:
							case LONG:
							case SHORT:
							{
								int LA71_4 = input.LA(2);

								if ((synpred99_Java()))
								{
									alt71 = 1;
								}

							}
								break;

						}

						switch (alt71)
						{
							case 1:
							// Java.g:670:10: normalParameterDecl ','
							{
								pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3149);
								normalParameterDecl197 = normalParameterDecl();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, normalParameterDecl197.getTree());
								}

								char_literal198 = (Token) match(input, COMMA, FOLLOW_COMMA_in_formalParameterDecls3159);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal198_tree = (CommonTree) adaptor.create(char_literal198);
									adaptor.addChild(root_0, char_literal198_tree);
								}

							}
								break;

							default:
								if (cnt71 >= 1)
								{
									break loop71;
								}
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								EarlyExitException eee = new EarlyExitException(71, input);
								throw eee;
						}
						cnt71++;
					}
					while (true);

					pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3181);
					ellipsisParameterDecl199 = ellipsisParameterDecl();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, ellipsisParameterDecl199.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 39, formalParameterDecls_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "formalParameterDecls"

	public static class normalParameterDecl_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "normalParameterDecl"
	// Java.g:676:1: normalParameterDecl : variableModifiers type IDENTIFIER ( '[' ']' )* ;
	public final JavaParser.normalParameterDecl_return normalParameterDecl() throws RecognitionException
	{
		JavaParser.normalParameterDecl_return retval = new JavaParser.normalParameterDecl_return();
		retval.start = input.LT(1);

		int normalParameterDecl_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER202 = null;
		Token char_literal203 = null;
		Token char_literal204 = null;
		JavaParser.variableModifiers_return variableModifiers200 = null;

		JavaParser.type_return type201 = null;

		CommonTree IDENTIFIER202_tree = null;
		CommonTree char_literal203_tree = null;
		CommonTree char_literal204_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 40)) { return retval; }

			// Java.g:677:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* )
			// Java.g:677:9: variableModifiers type IDENTIFIER ( '[' ']' )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_variableModifiers_in_normalParameterDecl3201);
				variableModifiers200 = variableModifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableModifiers200.getTree());
				}

				pushFollow(FOLLOW_type_in_normalParameterDecl3203);
				type201 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type201.getTree());
				}

				IDENTIFIER202 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_normalParameterDecl3205);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER202_tree = (CommonTree) adaptor.create(IDENTIFIER202);
					adaptor.addChild(root_0, IDENTIFIER202_tree);
				}

				// Java.g:678:9: ( '[' ']' )*
				loop73:
				do
				{
					int alt73 = 2;
					int LA73_0 = input.LA(1);

					if ((LA73_0 == LBRACKET))
					{
						alt73 = 1;
					}

					switch (alt73)
					{
						case 1:
						// Java.g:678:10: '[' ']'
						{
							char_literal203 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_normalParameterDecl3216);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal203_tree = (CommonTree) adaptor.create(char_literal203);
								adaptor.addChild(root_0, char_literal203_tree);
							}

							char_literal204 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_normalParameterDecl3218);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal204_tree = (CommonTree) adaptor.create(char_literal204);
								adaptor.addChild(root_0, char_literal204_tree);
							}

						}
							break;

						default:
							break loop73;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 40, normalParameterDecl_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "normalParameterDecl"

	public static class ellipsisParameterDecl_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "ellipsisParameterDecl"
	// Java.g:682:1: ellipsisParameterDecl : variableModifiers type '...' IDENTIFIER ;
	public final JavaParser.ellipsisParameterDecl_return ellipsisParameterDecl() throws RecognitionException
	{
		JavaParser.ellipsisParameterDecl_return retval = new JavaParser.ellipsisParameterDecl_return();
		retval.start = input.LT(1);

		int ellipsisParameterDecl_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal207 = null;
		Token IDENTIFIER208 = null;
		JavaParser.variableModifiers_return variableModifiers205 = null;

		JavaParser.type_return type206 = null;

		CommonTree string_literal207_tree = null;
		CommonTree IDENTIFIER208_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 41)) { return retval; }

			// Java.g:683:5: ( variableModifiers type '...' IDENTIFIER )
			// Java.g:683:9: variableModifiers type '...' IDENTIFIER
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_variableModifiers_in_ellipsisParameterDecl3249);
				variableModifiers205 = variableModifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableModifiers205.getTree());
				}

				pushFollow(FOLLOW_type_in_ellipsisParameterDecl3259);
				type206 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type206.getTree());
				}

				string_literal207 = (Token) match(input, ELLIPSIS, FOLLOW_ELLIPSIS_in_ellipsisParameterDecl3262);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal207_tree = (CommonTree) adaptor.create(string_literal207);
					adaptor.addChild(root_0, string_literal207_tree);
				}

				IDENTIFIER208 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_ellipsisParameterDecl3272);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER208_tree = (CommonTree) adaptor.create(IDENTIFIER208);
					adaptor.addChild(root_0, IDENTIFIER208_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 41, ellipsisParameterDecl_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "ellipsisParameterDecl"

	public static class explicitConstructorInvocation_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "explicitConstructorInvocation"
	// Java.g:689:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' );
	public final JavaParser.explicitConstructorInvocation_return explicitConstructorInvocation() throws RecognitionException
	{
		JavaParser.explicitConstructorInvocation_return retval = new JavaParser.explicitConstructorInvocation_return();
		retval.start = input.LT(1);

		int explicitConstructorInvocation_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set210 = null;
		Token char_literal212 = null;
		Token char_literal214 = null;
		Token string_literal216 = null;
		Token char_literal218 = null;
		JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments209 = null;

		JavaParser.arguments_return arguments211 = null;

		JavaParser.primary_return primary213 = null;

		JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments215 = null;

		JavaParser.arguments_return arguments217 = null;

		CommonTree set210_tree = null;
		CommonTree char_literal212_tree = null;
		CommonTree char_literal214_tree = null;
		CommonTree string_literal216_tree = null;
		CommonTree char_literal218_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 42)) { return retval; }

			// Java.g:690:5: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' )
			int alt76 = 2;
			switch (input.LA(1))
			{
				case LT:
				{
					alt76 = 1;
				}
					break;
				case THIS:
				{
					int LA76_2 = input.LA(2);

					if ((synpred103_Java()))
					{
						alt76 = 1;
					}
					else if ((true))
					{
						alt76 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 76, 2, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case CHARLITERAL:
				case DOUBLE:
				case DOUBLELITERAL:
				case FALSE:
				case FLOAT:
				case FLOATLITERAL:
				case IDENTIFIER:
				case INT:
				case INTLITERAL:
				case LONG:
				case LONGLITERAL:
				case LPAREN:
				case NEW:
				case NULL:
				case SHORT:
				case STRINGLITERAL:
				case TRUE:
				case VOID:
				{
					alt76 = 2;
				}
					break;
				case SUPER:
				{
					int LA76_4 = input.LA(2);

					if ((synpred103_Java()))
					{
						alt76 = 1;
					}
					else if ((true))
					{
						alt76 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 76, 4, input);

						throw nvae;

					}
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 76, 0, input);

					throw nvae;

			}

			switch (alt76)
			{
				case 1:
				// Java.g:690:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					// Java.g:690:9: ( nonWildcardTypeArguments )?
					int alt74 = 2;
					int LA74_0 = input.LA(1);

					if ((LA74_0 == LT))
					{
						alt74 = 1;
					}
					switch (alt74)
					{
						case 1:
						// Java.g:690:10: nonWildcardTypeArguments
						{
							pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation3294);
							nonWildcardTypeArguments209 = nonWildcardTypeArguments();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, nonWildcardTypeArguments209.getTree());
							}

						}
							break;

					}

					set210 = (Token) input.LT(1);

					if ((input.LA(1) == SUPER) || (input.LA(1) == THIS))
					{
						input.consume();
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, (CommonTree) adaptor.create(set210));
						}
						state.errorRecovery = false;
						state.failed = false;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						MismatchedSetException mse = new MismatchedSetException(null, input);
						throw mse;
					}

					pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation3352);
					arguments211 = arguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arguments211.getTree());
					}

					char_literal212 = (Token) match(input, SEMI, FOLLOW_SEMI_in_explicitConstructorInvocation3354);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal212_tree = (CommonTree) adaptor.create(char_literal212);
						adaptor.addChild(root_0, char_literal212_tree);
					}

				}
					break;
				case 2:
				// Java.g:697:9: primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_primary_in_explicitConstructorInvocation3365);
					primary213 = primary();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, primary213.getTree());
					}

					char_literal214 = (Token) match(input, DOT, FOLLOW_DOT_in_explicitConstructorInvocation3375);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal214_tree = (CommonTree) adaptor.create(char_literal214);
						adaptor.addChild(root_0, char_literal214_tree);
					}

					// Java.g:699:9: ( nonWildcardTypeArguments )?
					int alt75 = 2;
					int LA75_0 = input.LA(1);

					if ((LA75_0 == LT))
					{
						alt75 = 1;
					}
					switch (alt75)
					{
						case 1:
						// Java.g:699:10: nonWildcardTypeArguments
						{
							pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation3386);
							nonWildcardTypeArguments215 = nonWildcardTypeArguments();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, nonWildcardTypeArguments215.getTree());
							}

						}
							break;

					}

					string_literal216 = (Token) match(input, SUPER, FOLLOW_SUPER_in_explicitConstructorInvocation3407);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal216_tree = (CommonTree) adaptor.create(string_literal216);
						adaptor.addChild(root_0, string_literal216_tree);
					}

					pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation3417);
					arguments217 = arguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arguments217.getTree());
					}

					char_literal218 = (Token) match(input, SEMI, FOLLOW_SEMI_in_explicitConstructorInvocation3419);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal218_tree = (CommonTree) adaptor.create(char_literal218);
						adaptor.addChild(root_0, char_literal218_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 42, explicitConstructorInvocation_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "explicitConstructorInvocation"

	public static class qualifiedName_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "qualifiedName"
	// Java.g:705:1: qualifiedName : IDENTIFIER ( '.' IDENTIFIER )* ;
	public final JavaParser.qualifiedName_return qualifiedName() throws RecognitionException
	{
		JavaParser.qualifiedName_return retval = new JavaParser.qualifiedName_return();
		retval.start = input.LT(1);

		int qualifiedName_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER219 = null;
		Token char_literal220 = null;
		Token IDENTIFIER221 = null;

		CommonTree IDENTIFIER219_tree = null;
		CommonTree char_literal220_tree = null;
		CommonTree IDENTIFIER221_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 43)) { return retval; }

			// Java.g:706:5: ( IDENTIFIER ( '.' IDENTIFIER )* )
			// Java.g:706:9: IDENTIFIER ( '.' IDENTIFIER )*
			{
				root_0 = (CommonTree) adaptor.nil();

				IDENTIFIER219 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_qualifiedName3439);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER219_tree = (CommonTree) adaptor.create(IDENTIFIER219);
					adaptor.addChild(root_0, IDENTIFIER219_tree);
				}

				// Java.g:707:9: ( '.' IDENTIFIER )*
				loop77:
				do
				{
					int alt77 = 2;
					int LA77_0 = input.LA(1);

					if ((LA77_0 == DOT))
					{
						alt77 = 1;
					}

					switch (alt77)
					{
						case 1:
						// Java.g:707:10: '.' IDENTIFIER
						{
							char_literal220 = (Token) match(input, DOT, FOLLOW_DOT_in_qualifiedName3450);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal220_tree = (CommonTree) adaptor.create(char_literal220);
								adaptor.addChild(root_0, char_literal220_tree);
							}

							IDENTIFIER221 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_qualifiedName3452);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								IDENTIFIER221_tree = (CommonTree) adaptor.create(IDENTIFIER221);
								adaptor.addChild(root_0, IDENTIFIER221_tree);
							}

						}
							break;

						default:
							break loop77;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 43, qualifiedName_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "qualifiedName"

	public static class annotations_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotations"
	// Java.g:711:1: annotations : ( annotation )+ ;
	public final JavaParser.annotations_return annotations() throws RecognitionException
	{
		JavaParser.annotations_return retval = new JavaParser.annotations_return();
		retval.start = input.LT(1);

		int annotations_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.annotation_return annotation222 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 44)) { return retval; }

			// Java.g:712:5: ( ( annotation )+ )
			// Java.g:712:9: ( annotation )+
			{
				root_0 = (CommonTree) adaptor.nil();

				// Java.g:712:9: ( annotation )+
				int cnt78 = 0;
				loop78:
				do
				{
					int alt78 = 2;
					int LA78_0 = input.LA(1);

					if ((LA78_0 == MONKEYS_AT))
					{
						alt78 = 1;
					}

					switch (alt78)
					{
						case 1:
						// Java.g:712:10: annotation
						{
							pushFollow(FOLLOW_annotation_in_annotations3484);
							annotation222 = annotation();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, annotation222.getTree());
							}

						}
							break;

						default:
							if (cnt78 >= 1)
							{
								break loop78;
							}
							if (state.backtracking > 0)
							{
								state.failed = true;
								return retval;
							}
							EarlyExitException eee = new EarlyExitException(78, input);
							throw eee;
					}
					cnt78++;
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 44, annotations_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotations"

	public static class annotation_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotation"
	// Java.g:720:1: annotation : '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )? ;
	public final JavaParser.annotation_return annotation() throws RecognitionException
	{
		JavaParser.annotation_return retval = new JavaParser.annotation_return();
		retval.start = input.LT(1);

		int annotation_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal223 = null;
		Token char_literal225 = null;
		Token char_literal228 = null;
		JavaParser.qualifiedName_return qualifiedName224 = null;

		JavaParser.elementValuePairs_return elementValuePairs226 = null;

		JavaParser.elementValue_return elementValue227 = null;

		CommonTree char_literal223_tree = null;
		CommonTree char_literal225_tree = null;
		CommonTree char_literal228_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 45)) { return retval; }

			// Java.g:721:5: ( '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )? )
			// Java.g:721:9: '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )?
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal223 = (Token) match(input, MONKEYS_AT, FOLLOW_MONKEYS_AT_in_annotation3517);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal223_tree = (CommonTree) adaptor.create(char_literal223);
					adaptor.addChild(root_0, char_literal223_tree);
				}

				pushFollow(FOLLOW_qualifiedName_in_annotation3519);
				qualifiedName224 = qualifiedName();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, qualifiedName224.getTree());
				}

				// Java.g:722:9: ( '(' ( elementValuePairs | elementValue )? ')' )?
				int alt80 = 2;
				int LA80_0 = input.LA(1);

				if ((LA80_0 == LPAREN))
				{
					alt80 = 1;
				}
				switch (alt80)
				{
					case 1:
					// Java.g:722:13: '(' ( elementValuePairs | elementValue )? ')'
					{
						char_literal225 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_annotation3533);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal225_tree = (CommonTree) adaptor.create(char_literal225);
							adaptor.addChild(root_0, char_literal225_tree);
						}

						// Java.g:723:19: ( elementValuePairs | elementValue )?
						int alt79 = 3;
						int LA79_0 = input.LA(1);

						if ((LA79_0 == IDENTIFIER))
						{
							int LA79_1 = input.LA(2);

							if ((LA79_1 == EQ))
							{
								alt79 = 1;
							}
							else if ((((LA79_1 >= AMP) && (LA79_1 <= AMPAMP)) || ((LA79_1 >= BANGEQ) && (LA79_1 <= BARBAR)) || (LA79_1 == CARET) || (LA79_1 == DOT) || (LA79_1 == EQEQ) || (LA79_1 == GT) || (LA79_1 == INSTANCEOF) || (LA79_1 == LBRACKET) || ((LA79_1 >= LPAREN) && (LA79_1 <= LT)) || (LA79_1 == PERCENT) || (LA79_1 == PLUS) || (LA79_1 == PLUSPLUS) || (LA79_1 == QUES) || (LA79_1 == RPAREN) || (LA79_1 == SLASH) || (LA79_1 == STAR) || (LA79_1 == SUB) || (LA79_1 == SUBSUB)))
							{
								alt79 = 2;
							}
						}
						else if (((LA79_0 == BANG) || (LA79_0 == BOOLEAN) || (LA79_0 == BYTE) || ((LA79_0 >= CHAR) && (LA79_0 <= CHARLITERAL)) || ((LA79_0 >= DOUBLE) && (LA79_0 <= DOUBLELITERAL)) || (LA79_0 == FALSE) || ((LA79_0 >= FLOAT) && (LA79_0 <= FLOATLITERAL)) || (LA79_0 == INT) || (LA79_0 == INTLITERAL) || (LA79_0 == LBRACE) || ((LA79_0 >= LONG) && (LA79_0 <= LPAREN)) || (LA79_0 == MONKEYS_AT) || ((LA79_0 >= NEW) && (LA79_0 <= NULL)) || (LA79_0 == PLUS) || (LA79_0 == PLUSPLUS) || (LA79_0 == SHORT) || ((LA79_0 >= STRINGLITERAL) && (LA79_0 <= SUB)) || ((LA79_0 >= SUBSUB) && (LA79_0 <= SUPER)) || (LA79_0 == THIS) || (LA79_0 == TILDE) || (LA79_0 == TRUE) || (LA79_0 == VOID)))
						{
							alt79 = 2;
						}
						switch (alt79)
						{
							case 1:
							// Java.g:723:23: elementValuePairs
							{
								pushFollow(FOLLOW_elementValuePairs_in_annotation3560);
								elementValuePairs226 = elementValuePairs();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, elementValuePairs226.getTree());
								}

							}
								break;
							case 2:
							// Java.g:724:23: elementValue
							{
								pushFollow(FOLLOW_elementValue_in_annotation3584);
								elementValue227 = elementValue();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, elementValue227.getTree());
								}

							}
								break;

						}

						char_literal228 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_annotation3620);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal228_tree = (CommonTree) adaptor.create(char_literal228);
							adaptor.addChild(root_0, char_literal228_tree);
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 45, annotation_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotation"

	public static class elementValuePairs_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "elementValuePairs"
	// Java.g:730:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
	public final JavaParser.elementValuePairs_return elementValuePairs() throws RecognitionException
	{
		JavaParser.elementValuePairs_return retval = new JavaParser.elementValuePairs_return();
		retval.start = input.LT(1);

		int elementValuePairs_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal230 = null;
		JavaParser.elementValuePair_return elementValuePair229 = null;

		JavaParser.elementValuePair_return elementValuePair231 = null;

		CommonTree char_literal230_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 46)) { return retval; }

			// Java.g:731:5: ( elementValuePair ( ',' elementValuePair )* )
			// Java.g:731:9: elementValuePair ( ',' elementValuePair )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_elementValuePair_in_elementValuePairs3652);
				elementValuePair229 = elementValuePair();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, elementValuePair229.getTree());
				}

				// Java.g:732:9: ( ',' elementValuePair )*
				loop81:
				do
				{
					int alt81 = 2;
					int LA81_0 = input.LA(1);

					if ((LA81_0 == COMMA))
					{
						alt81 = 1;
					}

					switch (alt81)
					{
						case 1:
						// Java.g:732:10: ',' elementValuePair
						{
							char_literal230 = (Token) match(input, COMMA, FOLLOW_COMMA_in_elementValuePairs3663);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal230_tree = (CommonTree) adaptor.create(char_literal230);
								adaptor.addChild(root_0, char_literal230_tree);
							}

							pushFollow(FOLLOW_elementValuePair_in_elementValuePairs3665);
							elementValuePair231 = elementValuePair();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, elementValuePair231.getTree());
							}

						}
							break;

						default:
							break loop81;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 46, elementValuePairs_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "elementValuePairs"

	public static class elementValuePair_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "elementValuePair"
	// Java.g:736:1: elementValuePair : IDENTIFIER '=' elementValue ;
	public final JavaParser.elementValuePair_return elementValuePair() throws RecognitionException
	{
		JavaParser.elementValuePair_return retval = new JavaParser.elementValuePair_return();
		retval.start = input.LT(1);

		int elementValuePair_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER232 = null;
		Token char_literal233 = null;
		JavaParser.elementValue_return elementValue234 = null;

		CommonTree IDENTIFIER232_tree = null;
		CommonTree char_literal233_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 47)) { return retval; }

			// Java.g:737:5: ( IDENTIFIER '=' elementValue )
			// Java.g:737:9: IDENTIFIER '=' elementValue
			{
				root_0 = (CommonTree) adaptor.nil();

				IDENTIFIER232 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_elementValuePair3696);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER232_tree = (CommonTree) adaptor.create(IDENTIFIER232);
					adaptor.addChild(root_0, IDENTIFIER232_tree);
				}

				char_literal233 = (Token) match(input, EQ, FOLLOW_EQ_in_elementValuePair3698);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal233_tree = (CommonTree) adaptor.create(char_literal233);
					adaptor.addChild(root_0, char_literal233_tree);
				}

				pushFollow(FOLLOW_elementValue_in_elementValuePair3700);
				elementValue234 = elementValue();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, elementValue234.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 47, elementValuePair_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "elementValuePair"

	public static class elementValue_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "elementValue"
	// Java.g:740:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
	public final JavaParser.elementValue_return elementValue() throws RecognitionException
	{
		JavaParser.elementValue_return retval = new JavaParser.elementValue_return();
		retval.start = input.LT(1);

		int elementValue_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.conditionalExpression_return conditionalExpression235 = null;

		JavaParser.annotation_return annotation236 = null;

		JavaParser.elementValueArrayInitializer_return elementValueArrayInitializer237 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 48)) { return retval; }

			// Java.g:741:5: ( conditionalExpression | annotation | elementValueArrayInitializer )
			int alt82 = 3;
			switch (input.LA(1))
			{
				case BANG:
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case CHARLITERAL:
				case DOUBLE:
				case DOUBLELITERAL:
				case FALSE:
				case FLOAT:
				case FLOATLITERAL:
				case IDENTIFIER:
				case INT:
				case INTLITERAL:
				case LONG:
				case LONGLITERAL:
				case LPAREN:
				case NEW:
				case NULL:
				case PLUS:
				case PLUSPLUS:
				case SHORT:
				case STRINGLITERAL:
				case SUB:
				case SUBSUB:
				case SUPER:
				case THIS:
				case TILDE:
				case TRUE:
				case VOID:
				{
					alt82 = 1;
				}
					break;
				case MONKEYS_AT:
				{
					alt82 = 2;
				}
					break;
				case LBRACE:
				{
					alt82 = 3;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 82, 0, input);

					throw nvae;

			}

			switch (alt82)
			{
				case 1:
				// Java.g:741:9: conditionalExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_conditionalExpression_in_elementValue3720);
					conditionalExpression235 = conditionalExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, conditionalExpression235.getTree());
					}

				}
					break;
				case 2:
				// Java.g:742:9: annotation
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_annotation_in_elementValue3730);
					annotation236 = annotation();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, annotation236.getTree());
					}

				}
					break;
				case 3:
				// Java.g:743:9: elementValueArrayInitializer
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue3740);
					elementValueArrayInitializer237 = elementValueArrayInitializer();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, elementValueArrayInitializer237.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 48, elementValue_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "elementValue"

	public static class elementValueArrayInitializer_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "elementValueArrayInitializer"
	// Java.g:746:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' ;
	public final JavaParser.elementValueArrayInitializer_return elementValueArrayInitializer() throws RecognitionException
	{
		JavaParser.elementValueArrayInitializer_return retval = new JavaParser.elementValueArrayInitializer_return();
		retval.start = input.LT(1);

		int elementValueArrayInitializer_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal238 = null;
		Token char_literal240 = null;
		Token char_literal242 = null;
		Token char_literal243 = null;
		JavaParser.elementValue_return elementValue239 = null;

		JavaParser.elementValue_return elementValue241 = null;

		CommonTree char_literal238_tree = null;
		CommonTree char_literal240_tree = null;
		CommonTree char_literal242_tree = null;
		CommonTree char_literal243_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 49)) { return retval; }

			// Java.g:747:5: ( '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' )
			// Java.g:747:9: '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal238 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_elementValueArrayInitializer3760);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal238_tree = (CommonTree) adaptor.create(char_literal238);
					adaptor.addChild(root_0, char_literal238_tree);
				}

				// Java.g:748:9: ( elementValue ( ',' elementValue )* )?
				int alt84 = 2;
				int LA84_0 = input.LA(1);

				if (((LA84_0 == BANG) || (LA84_0 == BOOLEAN) || (LA84_0 == BYTE) || ((LA84_0 >= CHAR) && (LA84_0 <= CHARLITERAL)) || ((LA84_0 >= DOUBLE) && (LA84_0 <= DOUBLELITERAL)) || (LA84_0 == FALSE) || ((LA84_0 >= FLOAT) && (LA84_0 <= FLOATLITERAL)) || (LA84_0 == IDENTIFIER) || (LA84_0 == INT) || (LA84_0 == INTLITERAL) || (LA84_0 == LBRACE) || ((LA84_0 >= LONG) && (LA84_0 <= LPAREN)) || (LA84_0 == MONKEYS_AT) || ((LA84_0 >= NEW) && (LA84_0 <= NULL)) || (LA84_0 == PLUS) || (LA84_0 == PLUSPLUS) || (LA84_0 == SHORT) || ((LA84_0 >= STRINGLITERAL) && (LA84_0 <= SUB)) || ((LA84_0 >= SUBSUB) && (LA84_0 <= SUPER)) || (LA84_0 == THIS) || (LA84_0 == TILDE) || (LA84_0 == TRUE) || (LA84_0 == VOID)))
				{
					alt84 = 1;
				}
				switch (alt84)
				{
					case 1:
					// Java.g:748:10: elementValue ( ',' elementValue )*
					{
						pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer3771);
						elementValue239 = elementValue();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, elementValue239.getTree());
						}

						// Java.g:749:13: ( ',' elementValue )*
						loop83:
						do
						{
							int alt83 = 2;
							int LA83_0 = input.LA(1);

							if ((LA83_0 == COMMA))
							{
								int LA83_1 = input.LA(2);

								if (((LA83_1 == BANG) || (LA83_1 == BOOLEAN) || (LA83_1 == BYTE) || ((LA83_1 >= CHAR) && (LA83_1 <= CHARLITERAL)) || ((LA83_1 >= DOUBLE) && (LA83_1 <= DOUBLELITERAL)) || (LA83_1 == FALSE) || ((LA83_1 >= FLOAT) && (LA83_1 <= FLOATLITERAL)) || (LA83_1 == IDENTIFIER) || (LA83_1 == INT) || (LA83_1 == INTLITERAL) || (LA83_1 == LBRACE) || ((LA83_1 >= LONG) && (LA83_1 <= LPAREN)) || (LA83_1 == MONKEYS_AT) || ((LA83_1 >= NEW) && (LA83_1 <= NULL)) || (LA83_1 == PLUS) || (LA83_1 == PLUSPLUS) || (LA83_1 == SHORT) || ((LA83_1 >= STRINGLITERAL) && (LA83_1 <= SUB)) || ((LA83_1 >= SUBSUB) && (LA83_1 <= SUPER)) || (LA83_1 == THIS) || (LA83_1 == TILDE) || (LA83_1 == TRUE) || (LA83_1 == VOID)))
								{
									alt83 = 1;
								}

							}

							switch (alt83)
							{
								case 1:
								// Java.g:749:14: ',' elementValue
								{
									char_literal240 = (Token) match(input, COMMA, FOLLOW_COMMA_in_elementValueArrayInitializer3786);
									if (state.failed) { return retval; }
									if (state.backtracking == 0)
									{
										char_literal240_tree = (CommonTree) adaptor.create(char_literal240);
										adaptor.addChild(root_0, char_literal240_tree);
									}

									pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer3788);
									elementValue241 = elementValue();

									state._fsp--;
									if (state.failed) { return retval; }
									if (state.backtracking == 0)
									{
										adaptor.addChild(root_0, elementValue241.getTree());
									}

								}
									break;

								default:
									break loop83;
							}
						}
						while (true);

					}
						break;

				}

				// Java.g:751:12: ( ',' )?
				int alt85 = 2;
				int LA85_0 = input.LA(1);

				if ((LA85_0 == COMMA))
				{
					alt85 = 1;
				}
				switch (alt85)
				{
					case 1:
					// Java.g:751:13: ','
					{
						char_literal242 = (Token) match(input, COMMA, FOLLOW_COMMA_in_elementValueArrayInitializer3817);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal242_tree = (CommonTree) adaptor.create(char_literal242);
							adaptor.addChild(root_0, char_literal242_tree);
						}

					}
						break;

				}

				char_literal243 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_elementValueArrayInitializer3821);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal243_tree = (CommonTree) adaptor.create(char_literal243);
					adaptor.addChild(root_0, char_literal243_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 49, elementValueArrayInitializer_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "elementValueArrayInitializer"

	public static class annotationTypeDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotationTypeDeclaration"
	// Java.g:758:1: annotationTypeDeclaration : modifiers '@' 'interface' IDENTIFIER annotationTypeBody ;
	public final JavaParser.annotationTypeDeclaration_return annotationTypeDeclaration() throws RecognitionException
	{
		JavaParser.annotationTypeDeclaration_return retval = new JavaParser.annotationTypeDeclaration_return();
		retval.start = input.LT(1);

		int annotationTypeDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal245 = null;
		Token string_literal246 = null;
		Token IDENTIFIER247 = null;
		JavaParser.modifiers_return modifiers244 = null;

		JavaParser.annotationTypeBody_return annotationTypeBody248 = null;

		CommonTree char_literal245_tree = null;
		CommonTree string_literal246_tree = null;
		CommonTree IDENTIFIER247_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 50)) { return retval; }

			// Java.g:759:5: ( modifiers '@' 'interface' IDENTIFIER annotationTypeBody )
			// Java.g:759:9: modifiers '@' 'interface' IDENTIFIER annotationTypeBody
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_annotationTypeDeclaration3844);
				modifiers244 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers244.getTree());
				}

				char_literal245 = (Token) match(input, MONKEYS_AT, FOLLOW_MONKEYS_AT_in_annotationTypeDeclaration3846);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal245_tree = (CommonTree) adaptor.create(char_literal245);
					adaptor.addChild(root_0, char_literal245_tree);
				}

				string_literal246 = (Token) match(input, INTERFACE, FOLLOW_INTERFACE_in_annotationTypeDeclaration3856);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal246_tree = (CommonTree) adaptor.create(string_literal246);
					adaptor.addChild(root_0, string_literal246_tree);
				}

				IDENTIFIER247 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_annotationTypeDeclaration3866);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER247_tree = (CommonTree) adaptor.create(IDENTIFIER247);
					adaptor.addChild(root_0, IDENTIFIER247_tree);
				}

				pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration3876);
				annotationTypeBody248 = annotationTypeBody();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, annotationTypeBody248.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 50, annotationTypeDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotationTypeDeclaration"

	public static class annotationTypeBody_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotationTypeBody"
	// Java.g:766:1: annotationTypeBody : '{' ( annotationTypeElementDeclaration )* '}' ;
	public final JavaParser.annotationTypeBody_return annotationTypeBody() throws RecognitionException
	{
		JavaParser.annotationTypeBody_return retval = new JavaParser.annotationTypeBody_return();
		retval.start = input.LT(1);

		int annotationTypeBody_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal249 = null;
		Token char_literal251 = null;
		JavaParser.annotationTypeElementDeclaration_return annotationTypeElementDeclaration250 = null;

		CommonTree char_literal249_tree = null;
		CommonTree char_literal251_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 51)) { return retval; }

			// Java.g:767:5: ( '{' ( annotationTypeElementDeclaration )* '}' )
			// Java.g:767:9: '{' ( annotationTypeElementDeclaration )* '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal249 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_annotationTypeBody3897);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal249_tree = (CommonTree) adaptor.create(char_literal249);
					adaptor.addChild(root_0, char_literal249_tree);
				}

				// Java.g:768:9: ( annotationTypeElementDeclaration )*
				loop86:
				do
				{
					int alt86 = 2;
					int LA86_0 = input.LA(1);

					if (((LA86_0 == ABSTRACT) || (LA86_0 == BOOLEAN) || (LA86_0 == BYTE) || (LA86_0 == CHAR) || (LA86_0 == CLASS) || (LA86_0 == DOUBLE) || (LA86_0 == ENUM) || (LA86_0 == FINAL) || (LA86_0 == FLOAT) || (LA86_0 == IDENTIFIER) || ((LA86_0 >= INT) && (LA86_0 <= INTERFACE)) || (LA86_0 == LONG) || (LA86_0 == LT) || ((LA86_0 >= MONKEYS_AT) && (LA86_0 <= NATIVE)) || ((LA86_0 >= PRIVATE) && (LA86_0 <= PUBLIC)) || ((LA86_0 >= SEMI) && (LA86_0 <= SHORT)) || ((LA86_0 >= STATIC) && (LA86_0 <= STRICTFP)) || (LA86_0 == SYNCHRONIZED) || (LA86_0 == TRANSIENT) || ((LA86_0 >= VOID) && (LA86_0 <= VOLATILE))))
					{
						alt86 = 1;
					}

					switch (alt86)
					{
						case 1:
						// Java.g:768:10: annotationTypeElementDeclaration
						{
							pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody3909);
							annotationTypeElementDeclaration250 = annotationTypeElementDeclaration();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, annotationTypeElementDeclaration250.getTree());
							}

						}
							break;

						default:
							break loop86;
					}
				}
				while (true);

				char_literal251 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_annotationTypeBody3931);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal251_tree = (CommonTree) adaptor.create(char_literal251);
					adaptor.addChild(root_0, char_literal251_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 51, annotationTypeBody_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotationTypeBody"

	public static class annotationTypeElementDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotationTypeElementDeclaration"
	// Java.g:776:1: annotationTypeElementDeclaration : ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' );
	public final JavaParser.annotationTypeElementDeclaration_return annotationTypeElementDeclaration() throws RecognitionException
	{
		JavaParser.annotationTypeElementDeclaration_return retval = new JavaParser.annotationTypeElementDeclaration_return();
		retval.start = input.LT(1);

		int annotationTypeElementDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal258 = null;
		JavaParser.annotationMethodDeclaration_return annotationMethodDeclaration252 = null;

		JavaParser.interfaceFieldDeclaration_return interfaceFieldDeclaration253 = null;

		JavaParser.normalClassDeclaration_return normalClassDeclaration254 = null;

		JavaParser.normalInterfaceDeclaration_return normalInterfaceDeclaration255 = null;

		JavaParser.enumDeclaration_return enumDeclaration256 = null;

		JavaParser.annotationTypeDeclaration_return annotationTypeDeclaration257 = null;

		CommonTree char_literal258_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 52)) { return retval; }

			// Java.g:777:5: ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' )
			int alt87 = 7;
			switch (input.LA(1))
			{
				case MONKEYS_AT:
				{
					int LA87_1 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 1, input);

						throw nvae;

					}
				}
					break;
				case PUBLIC:
				{
					int LA87_2 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 2, input);

						throw nvae;

					}
				}
					break;
				case PROTECTED:
				{
					int LA87_3 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 3, input);

						throw nvae;

					}
				}
					break;
				case PRIVATE:
				{
					int LA87_4 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 4, input);

						throw nvae;

					}
				}
					break;
				case STATIC:
				{
					int LA87_5 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 5, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				{
					int LA87_6 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 6, input);

						throw nvae;

					}
				}
					break;
				case FINAL:
				{
					int LA87_7 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 7, input);

						throw nvae;

					}
				}
					break;
				case NATIVE:
				{
					int LA87_8 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 8, input);

						throw nvae;

					}
				}
					break;
				case SYNCHRONIZED:
				{
					int LA87_9 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 9, input);

						throw nvae;

					}
				}
					break;
				case TRANSIENT:
				{
					int LA87_10 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 10, input);

						throw nvae;

					}
				}
					break;
				case VOLATILE:
				{
					int LA87_11 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 11, input);

						throw nvae;

					}
				}
					break;
				case STRICTFP:
				{
					int LA87_12 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else if ((synpred119_Java()))
					{
						alt87 = 3;
					}
					else if ((synpred120_Java()))
					{
						alt87 = 4;
					}
					else if ((synpred121_Java()))
					{
						alt87 = 5;
					}
					else if ((synpred122_Java()))
					{
						alt87 = 6;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 12, input);

						throw nvae;

					}
				}
					break;
				case IDENTIFIER:
				{
					int LA87_13 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 13, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA87_14 = input.LA(2);

					if ((synpred117_Java()))
					{
						alt87 = 1;
					}
					else if ((synpred118_Java()))
					{
						alt87 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 87, 14, input);

						throw nvae;

					}
				}
					break;
				case CLASS:
				{
					alt87 = 3;
				}
					break;
				case INTERFACE:
				{
					alt87 = 4;
				}
					break;
				case ENUM:
				{
					alt87 = 5;
				}
					break;
				case SEMI:
				{
					alt87 = 7;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 87, 0, input);

					throw nvae;

			}

			switch (alt87)
			{
				case 1:
				// Java.g:777:9: annotationMethodDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration3953);
					annotationMethodDeclaration252 = annotationMethodDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, annotationMethodDeclaration252.getTree());
					}

				}
					break;
				case 2:
				// Java.g:778:9: interfaceFieldDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration3963);
					interfaceFieldDeclaration253 = interfaceFieldDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, interfaceFieldDeclaration253.getTree());
					}

				}
					break;
				case 3:
				// Java.g:779:9: normalClassDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration3973);
					normalClassDeclaration254 = normalClassDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, normalClassDeclaration254.getTree());
					}

				}
					break;
				case 4:
				// Java.g:780:9: normalInterfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration3983);
					normalInterfaceDeclaration255 = normalInterfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, normalInterfaceDeclaration255.getTree());
					}

				}
					break;
				case 5:
				// Java.g:781:9: enumDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration3993);
					enumDeclaration256 = enumDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, enumDeclaration256.getTree());
					}

				}
					break;
				case 6:
				// Java.g:782:9: annotationTypeDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration4003);
					annotationTypeDeclaration257 = annotationTypeDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, annotationTypeDeclaration257.getTree());
					}

				}
					break;
				case 7:
				// Java.g:783:9: ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal258 = (Token) match(input, SEMI, FOLLOW_SEMI_in_annotationTypeElementDeclaration4013);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal258_tree = (CommonTree) adaptor.create(char_literal258);
						adaptor.addChild(root_0, char_literal258_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 52, annotationTypeElementDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotationTypeElementDeclaration"

	public static class annotationMethodDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotationMethodDeclaration"
	// Java.g:786:1: annotationMethodDeclaration : modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';' ;
	public final JavaParser.annotationMethodDeclaration_return annotationMethodDeclaration() throws RecognitionException
	{
		JavaParser.annotationMethodDeclaration_return retval = new JavaParser.annotationMethodDeclaration_return();
		retval.start = input.LT(1);

		int annotationMethodDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER261 = null;
		Token char_literal262 = null;
		Token char_literal263 = null;
		Token string_literal264 = null;
		Token char_literal266 = null;
		JavaParser.modifiers_return modifiers259 = null;

		JavaParser.type_return type260 = null;

		JavaParser.elementValue_return elementValue265 = null;

		CommonTree IDENTIFIER261_tree = null;
		CommonTree char_literal262_tree = null;
		CommonTree char_literal263_tree = null;
		CommonTree string_literal264_tree = null;
		CommonTree char_literal266_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 53)) { return retval; }

			// Java.g:787:5: ( modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';' )
			// Java.g:787:9: modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';'
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_annotationMethodDeclaration4033);
				modifiers259 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers259.getTree());
				}

				pushFollow(FOLLOW_type_in_annotationMethodDeclaration4035);
				type260 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type260.getTree());
				}

				IDENTIFIER261 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_annotationMethodDeclaration4037);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER261_tree = (CommonTree) adaptor.create(IDENTIFIER261);
					adaptor.addChild(root_0, IDENTIFIER261_tree);
				}

				char_literal262 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_annotationMethodDeclaration4047);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal262_tree = (CommonTree) adaptor.create(char_literal262);
					adaptor.addChild(root_0, char_literal262_tree);
				}

				char_literal263 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_annotationMethodDeclaration4049);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal263_tree = (CommonTree) adaptor.create(char_literal263);
					adaptor.addChild(root_0, char_literal263_tree);
				}

				// Java.g:788:17: ( 'default' elementValue )?
				int alt88 = 2;
				int LA88_0 = input.LA(1);

				if ((LA88_0 == DEFAULT))
				{
					alt88 = 1;
				}
				switch (alt88)
				{
					case 1:
					// Java.g:788:18: 'default' elementValue
					{
						string_literal264 = (Token) match(input, DEFAULT, FOLLOW_DEFAULT_in_annotationMethodDeclaration4052);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal264_tree = (CommonTree) adaptor.create(string_literal264);
							adaptor.addChild(root_0, string_literal264_tree);
						}

						pushFollow(FOLLOW_elementValue_in_annotationMethodDeclaration4054);
						elementValue265 = elementValue();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, elementValue265.getTree());
						}

					}
						break;

				}

				char_literal266 = (Token) match(input, SEMI, FOLLOW_SEMI_in_annotationMethodDeclaration4083);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal266_tree = (CommonTree) adaptor.create(char_literal266);
					adaptor.addChild(root_0, char_literal266_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 53, annotationMethodDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotationMethodDeclaration"

	public static class block_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "block"
	// Java.g:793:1: block : '{' ( blockStatement )* '}' ;
	public final JavaParser.block_return block() throws RecognitionException
	{
		JavaParser.block_return retval = new JavaParser.block_return();
		retval.start = input.LT(1);

		int block_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal267 = null;
		Token char_literal269 = null;
		JavaParser.blockStatement_return blockStatement268 = null;

		CommonTree char_literal267_tree = null;
		CommonTree char_literal269_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 54)) { return retval; }

			// Java.g:794:5: ( '{' ( blockStatement )* '}' )
			// Java.g:794:9: '{' ( blockStatement )* '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal267 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_block4107);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal267_tree = (CommonTree) adaptor.create(char_literal267);
					adaptor.addChild(root_0, char_literal267_tree);
				}

				// Java.g:795:9: ( blockStatement )*
				loop89:
				do
				{
					int alt89 = 2;
					int LA89_0 = input.LA(1);

					if (((LA89_0 == ABSTRACT) || ((LA89_0 >= ASSERT) && (LA89_0 <= BANG)) || ((LA89_0 >= BOOLEAN) && (LA89_0 <= BYTE)) || ((LA89_0 >= CHAR) && (LA89_0 <= CLASS)) || (LA89_0 == CONTINUE) || (LA89_0 == DO) || ((LA89_0 >= DOUBLE) && (LA89_0 <= DOUBLELITERAL)) || (LA89_0 == ENUM) || ((LA89_0 >= FALSE) && (LA89_0 <= FINAL)) || ((LA89_0 >= FLOAT) && (LA89_0 <= FOR)) || ((LA89_0 >= IDENTIFIER) && (LA89_0 <= IF)) || ((LA89_0 >= INT) && (LA89_0 <= INTLITERAL)) || (LA89_0 == LBRACE) || ((LA89_0 >= LONG) && (LA89_0 <= LT)) || ((LA89_0 >= MONKEYS_AT) && (LA89_0 <= NULL)) || (LA89_0 == PLUS) || ((LA89_0 >= PLUSPLUS) && (LA89_0 <= PUBLIC)) || (LA89_0 == RETURN) || ((LA89_0 >= SEMI) && (LA89_0 <= SHORT)) || ((LA89_0 >= STATIC) && (LA89_0 <= SUB)) || ((LA89_0 >= SUBSUB) && (LA89_0 <= SYNCHRONIZED)) || ((LA89_0 >= THIS) && (LA89_0 <= THROW)) || ((LA89_0 >= TILDE) && (LA89_0 <= WHILE))))
					{
						alt89 = 1;
					}

					switch (alt89)
					{
						case 1:
						// Java.g:795:10: blockStatement
						{
							pushFollow(FOLLOW_blockStatement_in_block4118);
							blockStatement268 = blockStatement();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, blockStatement268.getTree());
							}

						}
							break;

						default:
							break loop89;
					}
				}
				while (true);

				char_literal269 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_block4139);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal269_tree = (CommonTree) adaptor.create(char_literal269);
					adaptor.addChild(root_0, char_literal269_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 54, block_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "block"

	public static class blockStatement_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "blockStatement"
	// Java.g:824:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );
	public final JavaParser.blockStatement_return blockStatement() throws RecognitionException
	{
		JavaParser.blockStatement_return retval = new JavaParser.blockStatement_return();
		retval.start = input.LT(1);

		int blockStatement_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.localVariableDeclarationStatement_return localVariableDeclarationStatement270 = null;

		JavaParser.classOrInterfaceDeclaration_return classOrInterfaceDeclaration271 = null;

		JavaParser.statement_return statement272 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 55)) { return retval; }

			// Java.g:825:5: ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement )
			int alt90 = 3;
			switch (input.LA(1))
			{
				case FINAL:
				{
					int LA90_1 = input.LA(2);

					if ((synpred125_Java()))
					{
						alt90 = 1;
					}
					else if ((synpred126_Java()))
					{
						alt90 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 90, 1, input);

						throw nvae;

					}
				}
					break;
				case MONKEYS_AT:
				{
					int LA90_2 = input.LA(2);

					if ((synpred125_Java()))
					{
						alt90 = 1;
					}
					else if ((synpred126_Java()))
					{
						alt90 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 90, 2, input);

						throw nvae;

					}
				}
					break;
				case IDENTIFIER:
				{
					int LA90_3 = input.LA(2);

					if ((synpred125_Java()))
					{
						alt90 = 1;
					}
					else if ((true))
					{
						alt90 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 90, 3, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA90_4 = input.LA(2);

					if ((synpred125_Java()))
					{
						alt90 = 1;
					}
					else if ((true))
					{
						alt90 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 90, 4, input);

						throw nvae;

					}
				}
					break;
				case ABSTRACT:
				case CLASS:
				case ENUM:
				case INTERFACE:
				case NATIVE:
				case PRIVATE:
				case PROTECTED:
				case PUBLIC:
				case STATIC:
				case STRICTFP:
				case TRANSIENT:
				case VOLATILE:
				{
					alt90 = 2;
				}
					break;
				case SYNCHRONIZED:
				{
					int LA90_11 = input.LA(2);

					if ((synpred126_Java()))
					{
						alt90 = 2;
					}
					else if ((true))
					{
						alt90 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 90, 11, input);

						throw nvae;

					}
				}
					break;
				case ASSERT:
				case BANG:
				case BREAK:
				case CHARLITERAL:
				case CONTINUE:
				case DO:
				case DOUBLELITERAL:
				case FALSE:
				case FLOATLITERAL:
				case FOR:
				case IF:
				case INTLITERAL:
				case LBRACE:
				case LONGLITERAL:
				case LPAREN:
				case NEW:
				case NULL:
				case PLUS:
				case PLUSPLUS:
				case RETURN:
				case SEMI:
				case STRINGLITERAL:
				case SUB:
				case SUBSUB:
				case SUPER:
				case SWITCH:
				case THIS:
				case THROW:
				case TILDE:
				case TRUE:
				case TRY:
				case VOID:
				case WHILE:
				{
					alt90 = 3;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 90, 0, input);

					throw nvae;

			}

			switch (alt90)
			{
				case 1:
				// Java.g:825:9: localVariableDeclarationStatement
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement4161);
					localVariableDeclarationStatement270 = localVariableDeclarationStatement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, localVariableDeclarationStatement270.getTree());
					}

				}
					break;
				case 2:
				// Java.g:826:9: classOrInterfaceDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement4171);
					classOrInterfaceDeclaration271 = classOrInterfaceDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classOrInterfaceDeclaration271.getTree());
					}

				}
					break;
				case 3:
				// Java.g:827:9: statement
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_statement_in_blockStatement4181);
					statement272 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement272.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 55, blockStatement_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "blockStatement"

	public static class localVariableDeclarationStatement_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "localVariableDeclarationStatement"
	// Java.g:831:1: localVariableDeclarationStatement : localVariableDeclaration ';' ;
	public final JavaParser.localVariableDeclarationStatement_return localVariableDeclarationStatement() throws RecognitionException
	{
		JavaParser.localVariableDeclarationStatement_return retval = new JavaParser.localVariableDeclarationStatement_return();
		retval.start = input.LT(1);

		int localVariableDeclarationStatement_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal274 = null;
		JavaParser.localVariableDeclaration_return localVariableDeclaration273 = null;

		CommonTree char_literal274_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 56)) { return retval; }

			// Java.g:832:5: ( localVariableDeclaration ';' )
			// Java.g:832:9: localVariableDeclaration ';'
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement4202);
				localVariableDeclaration273 = localVariableDeclaration();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, localVariableDeclaration273.getTree());
				}

				char_literal274 = (Token) match(input, SEMI, FOLLOW_SEMI_in_localVariableDeclarationStatement4212);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal274_tree = (CommonTree) adaptor.create(char_literal274);
					adaptor.addChild(root_0, char_literal274_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 56, localVariableDeclarationStatement_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "localVariableDeclarationStatement"

	public static class localVariableDeclaration_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "localVariableDeclaration"
	// Java.g:836:1: localVariableDeclaration : variableModifiers type variableDeclarator ( ',' variableDeclarator )* ;
	public final JavaParser.localVariableDeclaration_return localVariableDeclaration() throws RecognitionException
	{
		JavaParser.localVariableDeclaration_return retval = new JavaParser.localVariableDeclaration_return();
		retval.start = input.LT(1);

		int localVariableDeclaration_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal278 = null;
		JavaParser.variableModifiers_return variableModifiers275 = null;

		JavaParser.type_return type276 = null;

		JavaParser.variableDeclarator_return variableDeclarator277 = null;

		JavaParser.variableDeclarator_return variableDeclarator279 = null;

		CommonTree char_literal278_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 57)) { return retval; }

			// Java.g:837:5: ( variableModifiers type variableDeclarator ( ',' variableDeclarator )* )
			// Java.g:837:9: variableModifiers type variableDeclarator ( ',' variableDeclarator )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration4232);
				variableModifiers275 = variableModifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableModifiers275.getTree());
				}

				pushFollow(FOLLOW_type_in_localVariableDeclaration4234);
				type276 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type276.getTree());
				}

				pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration4244);
				variableDeclarator277 = variableDeclarator();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableDeclarator277.getTree());
				}

				// Java.g:839:9: ( ',' variableDeclarator )*
				loop91:
				do
				{
					int alt91 = 2;
					int LA91_0 = input.LA(1);

					if ((LA91_0 == COMMA))
					{
						alt91 = 1;
					}

					switch (alt91)
					{
						case 1:
						// Java.g:839:10: ',' variableDeclarator
						{
							char_literal278 = (Token) match(input, COMMA, FOLLOW_COMMA_in_localVariableDeclaration4255);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal278_tree = (CommonTree) adaptor.create(char_literal278);
								adaptor.addChild(root_0, char_literal278_tree);
							}

							pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration4257);
							variableDeclarator279 = variableDeclarator();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, variableDeclarator279.getTree());
							}

						}
							break;

						default:
							break loop91;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 57, localVariableDeclaration_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "localVariableDeclaration"

	public static class statement_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "statement"
	// Java.g:843:1: statement : ( block | ( 'assert' ) expression ( ':' expression )? ';' | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( 'else' statement )? | forstatement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( IDENTIFIER )? ';' | 'continue' ( IDENTIFIER )? ';' | expression ';' | IDENTIFIER ':' statement | ';' );
	public final JavaParser.statement_return statement() throws RecognitionException
	{
		JavaParser.statement_return retval = new JavaParser.statement_return();
		retval.start = input.LT(1);

		int statement_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal281 = null;
		Token char_literal283 = null;
		Token char_literal285 = null;
		Token string_literal286 = null;
		Token char_literal288 = null;
		Token char_literal290 = null;
		Token string_literal291 = null;
		Token string_literal294 = null;
		Token string_literal297 = null;
		Token string_literal300 = null;
		Token string_literal302 = null;
		Token char_literal304 = null;
		Token string_literal306 = null;
		Token char_literal308 = null;
		Token char_literal310 = null;
		Token string_literal311 = null;
		Token string_literal314 = null;
		Token char_literal316 = null;
		Token string_literal317 = null;
		Token char_literal319 = null;
		Token string_literal320 = null;
		Token IDENTIFIER321 = null;
		Token char_literal322 = null;
		Token string_literal323 = null;
		Token IDENTIFIER324 = null;
		Token char_literal325 = null;
		Token char_literal327 = null;
		Token IDENTIFIER328 = null;
		Token char_literal329 = null;
		Token char_literal331 = null;
		JavaParser.block_return block280 = null;

		JavaParser.expression_return expression282 = null;

		JavaParser.expression_return expression284 = null;

		JavaParser.expression_return expression287 = null;

		JavaParser.expression_return expression289 = null;

		JavaParser.parExpression_return parExpression292 = null;

		JavaParser.statement_return statement293 = null;

		JavaParser.statement_return statement295 = null;

		JavaParser.forstatement_return forstatement296 = null;

		JavaParser.parExpression_return parExpression298 = null;

		JavaParser.statement_return statement299 = null;

		JavaParser.statement_return statement301 = null;

		JavaParser.parExpression_return parExpression303 = null;

		JavaParser.trystatement_return trystatement305 = null;

		JavaParser.parExpression_return parExpression307 = null;

		JavaParser.switchBlockStatementGroups_return switchBlockStatementGroups309 = null;

		JavaParser.parExpression_return parExpression312 = null;

		JavaParser.block_return block313 = null;

		JavaParser.expression_return expression315 = null;

		JavaParser.expression_return expression318 = null;

		JavaParser.expression_return expression326 = null;

		JavaParser.statement_return statement330 = null;

		CommonTree string_literal281_tree = null;
		CommonTree char_literal283_tree = null;
		CommonTree char_literal285_tree = null;
		CommonTree string_literal286_tree = null;
		CommonTree char_literal288_tree = null;
		CommonTree char_literal290_tree = null;
		CommonTree string_literal291_tree = null;
		CommonTree string_literal294_tree = null;
		CommonTree string_literal297_tree = null;
		CommonTree string_literal300_tree = null;
		CommonTree string_literal302_tree = null;
		CommonTree char_literal304_tree = null;
		CommonTree string_literal306_tree = null;
		CommonTree char_literal308_tree = null;
		CommonTree char_literal310_tree = null;
		CommonTree string_literal311_tree = null;
		CommonTree string_literal314_tree = null;
		CommonTree char_literal316_tree = null;
		CommonTree string_literal317_tree = null;
		CommonTree char_literal319_tree = null;
		CommonTree string_literal320_tree = null;
		CommonTree IDENTIFIER321_tree = null;
		CommonTree char_literal322_tree = null;
		CommonTree string_literal323_tree = null;
		CommonTree IDENTIFIER324_tree = null;
		CommonTree char_literal325_tree = null;
		CommonTree char_literal327_tree = null;
		CommonTree IDENTIFIER328_tree = null;
		CommonTree char_literal329_tree = null;
		CommonTree char_literal331_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 58)) { return retval; }

			// Java.g:844:5: ( block | ( 'assert' ) expression ( ':' expression )? ';' | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( 'else' statement )? | forstatement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( IDENTIFIER )? ';' | 'continue' ( IDENTIFIER )? ';' | expression ';' | IDENTIFIER ':' statement | ';' )
			int alt98 = 17;
			switch (input.LA(1))
			{
				case LBRACE:
				{
					alt98 = 1;
				}
					break;
				case ASSERT:
				{
					int LA98_2 = input.LA(2);

					if ((synpred130_Java()))
					{
						alt98 = 2;
					}
					else if ((synpred132_Java()))
					{
						alt98 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 98, 2, input);

						throw nvae;

					}
				}
					break;
				case IF:
				{
					alt98 = 4;
				}
					break;
				case FOR:
				{
					alt98 = 5;
				}
					break;
				case WHILE:
				{
					alt98 = 6;
				}
					break;
				case DO:
				{
					alt98 = 7;
				}
					break;
				case TRY:
				{
					alt98 = 8;
				}
					break;
				case SWITCH:
				{
					alt98 = 9;
				}
					break;
				case SYNCHRONIZED:
				{
					alt98 = 10;
				}
					break;
				case RETURN:
				{
					alt98 = 11;
				}
					break;
				case THROW:
				{
					alt98 = 12;
				}
					break;
				case BREAK:
				{
					alt98 = 13;
				}
					break;
				case CONTINUE:
				{
					alt98 = 14;
				}
					break;
				case BANG:
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case CHARLITERAL:
				case DOUBLE:
				case DOUBLELITERAL:
				case FALSE:
				case FLOAT:
				case FLOATLITERAL:
				case INT:
				case INTLITERAL:
				case LONG:
				case LONGLITERAL:
				case LPAREN:
				case NEW:
				case NULL:
				case PLUS:
				case PLUSPLUS:
				case SHORT:
				case STRINGLITERAL:
				case SUB:
				case SUBSUB:
				case SUPER:
				case THIS:
				case TILDE:
				case TRUE:
				case VOID:
				{
					alt98 = 15;
				}
					break;
				case IDENTIFIER:
				{
					int LA98_22 = input.LA(2);

					if ((synpred148_Java()))
					{
						alt98 = 15;
					}
					else if ((synpred149_Java()))
					{
						alt98 = 16;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 98, 22, input);

						throw nvae;

					}
				}
					break;
				case SEMI:
				{
					alt98 = 17;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 98, 0, input);

					throw nvae;

			}

			switch (alt98)
			{
				case 1:
				// Java.g:844:9: block
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_block_in_statement4288);
					block280 = block();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, block280.getTree());
					}

				}
					break;
				case 2:
				// Java.g:846:9: ( 'assert' ) expression ( ':' expression )? ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					// Java.g:846:9: ( 'assert' )
					// Java.g:846:10: 'assert'
					{
						string_literal281 = (Token) match(input, ASSERT, FOLLOW_ASSERT_in_statement4312);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal281_tree = (CommonTree) adaptor.create(string_literal281);
							adaptor.addChild(root_0, string_literal281_tree);
						}

					}

					pushFollow(FOLLOW_expression_in_statement4332);
					expression282 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression282.getTree());
					}

					// Java.g:848:20: ( ':' expression )?
					int alt92 = 2;
					int LA92_0 = input.LA(1);

					if ((LA92_0 == COLON))
					{
						alt92 = 1;
					}
					switch (alt92)
					{
						case 1:
						// Java.g:848:21: ':' expression
						{
							char_literal283 = (Token) match(input, COLON, FOLLOW_COLON_in_statement4335);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal283_tree = (CommonTree) adaptor.create(char_literal283);
								adaptor.addChild(root_0, char_literal283_tree);
							}

							pushFollow(FOLLOW_expression_in_statement4337);
							expression284 = expression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, expression284.getTree());
							}

						}
							break;

					}

					char_literal285 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4341);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal285_tree = (CommonTree) adaptor.create(char_literal285);
						adaptor.addChild(root_0, char_literal285_tree);
					}

				}
					break;
				case 3:
				// Java.g:849:9: 'assert' expression ( ':' expression )? ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal286 = (Token) match(input, ASSERT, FOLLOW_ASSERT_in_statement4351);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal286_tree = (CommonTree) adaptor.create(string_literal286);
						adaptor.addChild(root_0, string_literal286_tree);
					}

					pushFollow(FOLLOW_expression_in_statement4354);
					expression287 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression287.getTree());
					}

					// Java.g:849:30: ( ':' expression )?
					int alt93 = 2;
					int LA93_0 = input.LA(1);

					if ((LA93_0 == COLON))
					{
						alt93 = 1;
					}
					switch (alt93)
					{
						case 1:
						// Java.g:849:31: ':' expression
						{
							char_literal288 = (Token) match(input, COLON, FOLLOW_COLON_in_statement4357);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal288_tree = (CommonTree) adaptor.create(char_literal288);
								adaptor.addChild(root_0, char_literal288_tree);
							}

							pushFollow(FOLLOW_expression_in_statement4359);
							expression289 = expression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, expression289.getTree());
							}

						}
							break;

					}

					char_literal290 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4363);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal290_tree = (CommonTree) adaptor.create(char_literal290);
						adaptor.addChild(root_0, char_literal290_tree);
					}

				}
					break;
				case 4:
				// Java.g:850:9: 'if' parExpression statement ( 'else' statement )?
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal291 = (Token) match(input, IF, FOLLOW_IF_in_statement4385);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal291_tree = (CommonTree) adaptor.create(string_literal291);
						adaptor.addChild(root_0, string_literal291_tree);
					}

					pushFollow(FOLLOW_parExpression_in_statement4387);
					parExpression292 = parExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, parExpression292.getTree());
					}

					pushFollow(FOLLOW_statement_in_statement4389);
					statement293 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement293.getTree());
					}

					// Java.g:850:38: ( 'else' statement )?
					int alt94 = 2;
					int LA94_0 = input.LA(1);

					if ((LA94_0 == ELSE))
					{
						int LA94_1 = input.LA(2);

						if ((synpred133_Java()))
						{
							alt94 = 1;
						}
					}
					switch (alt94)
					{
						case 1:
						// Java.g:850:39: 'else' statement
						{
							string_literal294 = (Token) match(input, ELSE, FOLLOW_ELSE_in_statement4392);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal294_tree = (CommonTree) adaptor.create(string_literal294);
								adaptor.addChild(root_0, string_literal294_tree);
							}

							pushFollow(FOLLOW_statement_in_statement4394);
							statement295 = statement();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, statement295.getTree());
							}

						}
							break;

					}

				}
					break;
				case 5:
				// Java.g:851:9: forstatement
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_forstatement_in_statement4416);
					forstatement296 = forstatement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, forstatement296.getTree());
					}

				}
					break;
				case 6:
				// Java.g:852:9: 'while' parExpression statement
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal297 = (Token) match(input, WHILE, FOLLOW_WHILE_in_statement4426);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal297_tree = (CommonTree) adaptor.create(string_literal297);
						adaptor.addChild(root_0, string_literal297_tree);
					}

					pushFollow(FOLLOW_parExpression_in_statement4428);
					parExpression298 = parExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, parExpression298.getTree());
					}

					pushFollow(FOLLOW_statement_in_statement4430);
					statement299 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement299.getTree());
					}

				}
					break;
				case 7:
				// Java.g:853:9: 'do' statement 'while' parExpression ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal300 = (Token) match(input, DO, FOLLOW_DO_in_statement4440);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal300_tree = (CommonTree) adaptor.create(string_literal300);
						adaptor.addChild(root_0, string_literal300_tree);
					}

					pushFollow(FOLLOW_statement_in_statement4442);
					statement301 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement301.getTree());
					}

					string_literal302 = (Token) match(input, WHILE, FOLLOW_WHILE_in_statement4444);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal302_tree = (CommonTree) adaptor.create(string_literal302);
						adaptor.addChild(root_0, string_literal302_tree);
					}

					pushFollow(FOLLOW_parExpression_in_statement4446);
					parExpression303 = parExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, parExpression303.getTree());
					}

					char_literal304 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4448);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal304_tree = (CommonTree) adaptor.create(char_literal304);
						adaptor.addChild(root_0, char_literal304_tree);
					}

				}
					break;
				case 8:
				// Java.g:854:9: trystatement
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_trystatement_in_statement4458);
					trystatement305 = trystatement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, trystatement305.getTree());
					}

				}
					break;
				case 9:
				// Java.g:855:9: 'switch' parExpression '{' switchBlockStatementGroups '}'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal306 = (Token) match(input, SWITCH, FOLLOW_SWITCH_in_statement4468);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal306_tree = (CommonTree) adaptor.create(string_literal306);
						adaptor.addChild(root_0, string_literal306_tree);
					}

					pushFollow(FOLLOW_parExpression_in_statement4470);
					parExpression307 = parExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, parExpression307.getTree());
					}

					char_literal308 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_statement4472);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal308_tree = (CommonTree) adaptor.create(char_literal308);
						adaptor.addChild(root_0, char_literal308_tree);
					}

					pushFollow(FOLLOW_switchBlockStatementGroups_in_statement4474);
					switchBlockStatementGroups309 = switchBlockStatementGroups();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, switchBlockStatementGroups309.getTree());
					}

					char_literal310 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_statement4476);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal310_tree = (CommonTree) adaptor.create(char_literal310);
						adaptor.addChild(root_0, char_literal310_tree);
					}

				}
					break;
				case 10:
				// Java.g:856:9: 'synchronized' parExpression block
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal311 = (Token) match(input, SYNCHRONIZED, FOLLOW_SYNCHRONIZED_in_statement4486);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal311_tree = (CommonTree) adaptor.create(string_literal311);
						adaptor.addChild(root_0, string_literal311_tree);
					}

					pushFollow(FOLLOW_parExpression_in_statement4488);
					parExpression312 = parExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, parExpression312.getTree());
					}

					pushFollow(FOLLOW_block_in_statement4490);
					block313 = block();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, block313.getTree());
					}

				}
					break;
				case 11:
				// Java.g:857:9: 'return' ( expression )? ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal314 = (Token) match(input, RETURN, FOLLOW_RETURN_in_statement4500);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal314_tree = (CommonTree) adaptor.create(string_literal314);
						adaptor.addChild(root_0, string_literal314_tree);
					}

					// Java.g:857:18: ( expression )?
					int alt95 = 2;
					int LA95_0 = input.LA(1);

					if (((LA95_0 == BANG) || (LA95_0 == BOOLEAN) || (LA95_0 == BYTE) || ((LA95_0 >= CHAR) && (LA95_0 <= CHARLITERAL)) || ((LA95_0 >= DOUBLE) && (LA95_0 <= DOUBLELITERAL)) || (LA95_0 == FALSE) || ((LA95_0 >= FLOAT) && (LA95_0 <= FLOATLITERAL)) || (LA95_0 == IDENTIFIER) || (LA95_0 == INT) || (LA95_0 == INTLITERAL) || ((LA95_0 >= LONG) && (LA95_0 <= LPAREN)) || ((LA95_0 >= NEW) && (LA95_0 <= NULL)) || (LA95_0 == PLUS) || (LA95_0 == PLUSPLUS) || (LA95_0 == SHORT) || ((LA95_0 >= STRINGLITERAL) && (LA95_0 <= SUB)) || ((LA95_0 >= SUBSUB) && (LA95_0 <= SUPER)) || (LA95_0 == THIS) || (LA95_0 == TILDE) || (LA95_0 == TRUE) || (LA95_0 == VOID)))
					{
						alt95 = 1;
					}
					switch (alt95)
					{
						case 1:
						// Java.g:857:19: expression
						{
							pushFollow(FOLLOW_expression_in_statement4503);
							expression315 = expression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, expression315.getTree());
							}

						}
							break;

					}

					char_literal316 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4508);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal316_tree = (CommonTree) adaptor.create(char_literal316);
						adaptor.addChild(root_0, char_literal316_tree);
					}

				}
					break;
				case 12:
				// Java.g:858:9: 'throw' expression ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal317 = (Token) match(input, THROW, FOLLOW_THROW_in_statement4518);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal317_tree = (CommonTree) adaptor.create(string_literal317);
						adaptor.addChild(root_0, string_literal317_tree);
					}

					pushFollow(FOLLOW_expression_in_statement4520);
					expression318 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression318.getTree());
					}

					char_literal319 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4522);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal319_tree = (CommonTree) adaptor.create(char_literal319);
						adaptor.addChild(root_0, char_literal319_tree);
					}

				}
					break;
				case 13:
				// Java.g:859:9: 'break' ( IDENTIFIER )? ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal320 = (Token) match(input, BREAK, FOLLOW_BREAK_in_statement4532);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal320_tree = (CommonTree) adaptor.create(string_literal320);
						adaptor.addChild(root_0, string_literal320_tree);
					}

					// Java.g:860:13: ( IDENTIFIER )?
					int alt96 = 2;
					int LA96_0 = input.LA(1);

					if ((LA96_0 == IDENTIFIER))
					{
						alt96 = 1;
					}
					switch (alt96)
					{
						case 1:
						// Java.g:860:14: IDENTIFIER
						{
							IDENTIFIER321 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_statement4547);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								IDENTIFIER321_tree = (CommonTree) adaptor.create(IDENTIFIER321);
								adaptor.addChild(root_0, IDENTIFIER321_tree);
							}

						}
							break;

					}

					char_literal322 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4564);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal322_tree = (CommonTree) adaptor.create(char_literal322);
						adaptor.addChild(root_0, char_literal322_tree);
					}

				}
					break;
				case 14:
				// Java.g:862:9: 'continue' ( IDENTIFIER )? ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal323 = (Token) match(input, CONTINUE, FOLLOW_CONTINUE_in_statement4574);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal323_tree = (CommonTree) adaptor.create(string_literal323);
						adaptor.addChild(root_0, string_literal323_tree);
					}

					// Java.g:863:13: ( IDENTIFIER )?
					int alt97 = 2;
					int LA97_0 = input.LA(1);

					if ((LA97_0 == IDENTIFIER))
					{
						alt97 = 1;
					}
					switch (alt97)
					{
						case 1:
						// Java.g:863:14: IDENTIFIER
						{
							IDENTIFIER324 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_statement4589);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								IDENTIFIER324_tree = (CommonTree) adaptor.create(IDENTIFIER324);
								adaptor.addChild(root_0, IDENTIFIER324_tree);
							}

						}
							break;

					}

					char_literal325 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4606);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal325_tree = (CommonTree) adaptor.create(char_literal325);
						adaptor.addChild(root_0, char_literal325_tree);
					}

				}
					break;
				case 15:
				// Java.g:865:9: expression ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_expression_in_statement4616);
					expression326 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression326.getTree());
					}

					char_literal327 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4619);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal327_tree = (CommonTree) adaptor.create(char_literal327);
						adaptor.addChild(root_0, char_literal327_tree);
					}

				}
					break;
				case 16:
				// Java.g:866:9: IDENTIFIER ':' statement
				{
					root_0 = (CommonTree) adaptor.nil();

					IDENTIFIER328 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_statement4634);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER328_tree = (CommonTree) adaptor.create(IDENTIFIER328);
						adaptor.addChild(root_0, IDENTIFIER328_tree);
					}

					char_literal329 = (Token) match(input, COLON, FOLLOW_COLON_in_statement4636);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal329_tree = (CommonTree) adaptor.create(char_literal329);
						adaptor.addChild(root_0, char_literal329_tree);
					}

					pushFollow(FOLLOW_statement_in_statement4638);
					statement330 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement330.getTree());
					}

				}
					break;
				case 17:
				// Java.g:867:9: ';'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal331 = (Token) match(input, SEMI, FOLLOW_SEMI_in_statement4648);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal331_tree = (CommonTree) adaptor.create(char_literal331);
						adaptor.addChild(root_0, char_literal331_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 58, statement_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "statement"

	public static class switchBlockStatementGroups_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "switchBlockStatementGroups"
	// Java.g:871:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
	public final JavaParser.switchBlockStatementGroups_return switchBlockStatementGroups() throws RecognitionException
	{
		JavaParser.switchBlockStatementGroups_return retval = new JavaParser.switchBlockStatementGroups_return();
		retval.start = input.LT(1);

		int switchBlockStatementGroups_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.switchBlockStatementGroup_return switchBlockStatementGroup332 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 59)) { return retval; }

			// Java.g:872:5: ( ( switchBlockStatementGroup )* )
			// Java.g:872:9: ( switchBlockStatementGroup )*
			{
				root_0 = (CommonTree) adaptor.nil();

				// Java.g:872:9: ( switchBlockStatementGroup )*
				loop99:
				do
				{
					int alt99 = 2;
					int LA99_0 = input.LA(1);

					if (((LA99_0 == CASE) || (LA99_0 == DEFAULT)))
					{
						alt99 = 1;
					}

					switch (alt99)
					{
						case 1:
						// Java.g:872:10: switchBlockStatementGroup
						{
							pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups4670);
							switchBlockStatementGroup332 = switchBlockStatementGroup();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, switchBlockStatementGroup332.getTree());
							}

						}
							break;

						default:
							break loop99;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 59, switchBlockStatementGroups_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "switchBlockStatementGroups"

	public static class switchBlockStatementGroup_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "switchBlockStatementGroup"
	// Java.g:875:1: switchBlockStatementGroup : switchLabel ( blockStatement )* ;
	public final JavaParser.switchBlockStatementGroup_return switchBlockStatementGroup() throws RecognitionException
	{
		JavaParser.switchBlockStatementGroup_return retval = new JavaParser.switchBlockStatementGroup_return();
		retval.start = input.LT(1);

		int switchBlockStatementGroup_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.switchLabel_return switchLabel333 = null;

		JavaParser.blockStatement_return blockStatement334 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 60)) { return retval; }

			// Java.g:876:5: ( switchLabel ( blockStatement )* )
			// Java.g:877:9: switchLabel ( blockStatement )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup4699);
				switchLabel333 = switchLabel();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, switchLabel333.getTree());
				}

				// Java.g:878:9: ( blockStatement )*
				loop100:
				do
				{
					int alt100 = 2;
					int LA100_0 = input.LA(1);

					if (((LA100_0 == ABSTRACT) || ((LA100_0 >= ASSERT) && (LA100_0 <= BANG)) || ((LA100_0 >= BOOLEAN) && (LA100_0 <= BYTE)) || ((LA100_0 >= CHAR) && (LA100_0 <= CLASS)) || (LA100_0 == CONTINUE) || (LA100_0 == DO) || ((LA100_0 >= DOUBLE) && (LA100_0 <= DOUBLELITERAL)) || (LA100_0 == ENUM) || ((LA100_0 >= FALSE) && (LA100_0 <= FINAL)) || ((LA100_0 >= FLOAT) && (LA100_0 <= FOR)) || ((LA100_0 >= IDENTIFIER) && (LA100_0 <= IF)) || ((LA100_0 >= INT) && (LA100_0 <= INTLITERAL)) || (LA100_0 == LBRACE) || ((LA100_0 >= LONG) && (LA100_0 <= LT)) || ((LA100_0 >= MONKEYS_AT) && (LA100_0 <= NULL)) || (LA100_0 == PLUS) || ((LA100_0 >= PLUSPLUS) && (LA100_0 <= PUBLIC)) || (LA100_0 == RETURN) || ((LA100_0 >= SEMI) && (LA100_0 <= SHORT)) || ((LA100_0 >= STATIC) && (LA100_0 <= SUB)) || ((LA100_0 >= SUBSUB) && (LA100_0 <= SYNCHRONIZED)) || ((LA100_0 >= THIS) && (LA100_0 <= THROW)) || ((LA100_0 >= TILDE) && (LA100_0 <= WHILE))))
					{
						alt100 = 1;
					}

					switch (alt100)
					{
						case 1:
						// Java.g:878:10: blockStatement
						{
							pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup4710);
							blockStatement334 = blockStatement();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, blockStatement334.getTree());
							}

						}
							break;

						default:
							break loop100;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 60, switchBlockStatementGroup_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "switchBlockStatementGroup"

	public static class switchLabel_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "switchLabel"
	// Java.g:882:1: switchLabel : ( 'case' expression ':' | 'default' ':' );
	public final JavaParser.switchLabel_return switchLabel() throws RecognitionException
	{
		JavaParser.switchLabel_return retval = new JavaParser.switchLabel_return();
		retval.start = input.LT(1);

		int switchLabel_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal335 = null;
		Token char_literal337 = null;
		Token string_literal338 = null;
		Token char_literal339 = null;
		JavaParser.expression_return expression336 = null;

		CommonTree string_literal335_tree = null;
		CommonTree char_literal337_tree = null;
		CommonTree string_literal338_tree = null;
		CommonTree char_literal339_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 61)) { return retval; }

			// Java.g:883:5: ( 'case' expression ':' | 'default' ':' )
			int alt101 = 2;
			int LA101_0 = input.LA(1);

			if ((LA101_0 == CASE))
			{
				alt101 = 1;
			}
			else if ((LA101_0 == DEFAULT))
			{
				alt101 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 101, 0, input);

				throw nvae;

			}
			switch (alt101)
			{
				case 1:
				// Java.g:883:9: 'case' expression ':'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal335 = (Token) match(input, CASE, FOLLOW_CASE_in_switchLabel4741);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal335_tree = (CommonTree) adaptor.create(string_literal335);
						adaptor.addChild(root_0, string_literal335_tree);
					}

					pushFollow(FOLLOW_expression_in_switchLabel4743);
					expression336 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression336.getTree());
					}

					char_literal337 = (Token) match(input, COLON, FOLLOW_COLON_in_switchLabel4745);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal337_tree = (CommonTree) adaptor.create(char_literal337);
						adaptor.addChild(root_0, char_literal337_tree);
					}

				}
					break;
				case 2:
				// Java.g:884:9: 'default' ':'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal338 = (Token) match(input, DEFAULT, FOLLOW_DEFAULT_in_switchLabel4755);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal338_tree = (CommonTree) adaptor.create(string_literal338);
						adaptor.addChild(root_0, string_literal338_tree);
					}

					char_literal339 = (Token) match(input, COLON, FOLLOW_COLON_in_switchLabel4757);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal339_tree = (CommonTree) adaptor.create(char_literal339);
						adaptor.addChild(root_0, char_literal339_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 61, switchLabel_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "switchLabel"

	public static class trystatement_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "trystatement"
	// Java.g:888:1: trystatement : 'try' block ( catches 'finally' block | catches | 'finally' block ) ;
	public final JavaParser.trystatement_return trystatement() throws RecognitionException
	{
		JavaParser.trystatement_return retval = new JavaParser.trystatement_return();
		retval.start = input.LT(1);

		int trystatement_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal340 = null;
		Token string_literal343 = null;
		Token string_literal346 = null;
		JavaParser.block_return block341 = null;

		JavaParser.catches_return catches342 = null;

		JavaParser.block_return block344 = null;

		JavaParser.catches_return catches345 = null;

		JavaParser.block_return block347 = null;

		CommonTree string_literal340_tree = null;
		CommonTree string_literal343_tree = null;
		CommonTree string_literal346_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 62)) { return retval; }

			// Java.g:889:5: ( 'try' block ( catches 'finally' block | catches | 'finally' block ) )
			// Java.g:889:9: 'try' block ( catches 'finally' block | catches | 'finally' block )
			{
				root_0 = (CommonTree) adaptor.nil();

				string_literal340 = (Token) match(input, TRY, FOLLOW_TRY_in_trystatement4778);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal340_tree = (CommonTree) adaptor.create(string_literal340);
					adaptor.addChild(root_0, string_literal340_tree);
				}

				pushFollow(FOLLOW_block_in_trystatement4780);
				block341 = block();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, block341.getTree());
				}

				// Java.g:890:9: ( catches 'finally' block | catches | 'finally' block )
				int alt102 = 3;
				int LA102_0 = input.LA(1);

				if ((LA102_0 == CATCH))
				{
					int LA102_1 = input.LA(2);

					if ((synpred153_Java()))
					{
						alt102 = 1;
					}
					else if ((synpred154_Java()))
					{
						alt102 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 102, 1, input);

						throw nvae;

					}
				}
				else if ((LA102_0 == FINALLY))
				{
					alt102 = 3;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 102, 0, input);

					throw nvae;

				}
				switch (alt102)
				{
					case 1:
					// Java.g:890:13: catches 'finally' block
					{
						pushFollow(FOLLOW_catches_in_trystatement4794);
						catches342 = catches();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, catches342.getTree());
						}

						string_literal343 = (Token) match(input, FINALLY, FOLLOW_FINALLY_in_trystatement4796);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal343_tree = (CommonTree) adaptor.create(string_literal343);
							adaptor.addChild(root_0, string_literal343_tree);
						}

						pushFollow(FOLLOW_block_in_trystatement4798);
						block344 = block();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, block344.getTree());
						}

					}
						break;
					case 2:
					// Java.g:891:13: catches
					{
						pushFollow(FOLLOW_catches_in_trystatement4812);
						catches345 = catches();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, catches345.getTree());
						}

					}
						break;
					case 3:
					// Java.g:892:13: 'finally' block
					{
						string_literal346 = (Token) match(input, FINALLY, FOLLOW_FINALLY_in_trystatement4826);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal346_tree = (CommonTree) adaptor.create(string_literal346);
							adaptor.addChild(root_0, string_literal346_tree);
						}

						pushFollow(FOLLOW_block_in_trystatement4828);
						block347 = block();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, block347.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 62, trystatement_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "trystatement"

	public static class catches_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "catches"
	// Java.g:896:1: catches : catchClause ( catchClause )* ;
	public final JavaParser.catches_return catches() throws RecognitionException
	{
		JavaParser.catches_return retval = new JavaParser.catches_return();
		retval.start = input.LT(1);

		int catches_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.catchClause_return catchClause348 = null;

		JavaParser.catchClause_return catchClause349 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 63)) { return retval; }

			// Java.g:897:5: ( catchClause ( catchClause )* )
			// Java.g:897:9: catchClause ( catchClause )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_catchClause_in_catches4859);
				catchClause348 = catchClause();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, catchClause348.getTree());
				}

				// Java.g:898:9: ( catchClause )*
				loop103:
				do
				{
					int alt103 = 2;
					int LA103_0 = input.LA(1);

					if ((LA103_0 == CATCH))
					{
						alt103 = 1;
					}

					switch (alt103)
					{
						case 1:
						// Java.g:898:10: catchClause
						{
							pushFollow(FOLLOW_catchClause_in_catches4870);
							catchClause349 = catchClause();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, catchClause349.getTree());
							}

						}
							break;

						default:
							break loop103;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 63, catches_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "catches"

	public static class catchClause_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "catchClause"
	// Java.g:902:1: catchClause : 'catch' '(' formalParameter ')' block ;
	public final JavaParser.catchClause_return catchClause() throws RecognitionException
	{
		JavaParser.catchClause_return retval = new JavaParser.catchClause_return();
		retval.start = input.LT(1);

		int catchClause_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal350 = null;
		Token char_literal351 = null;
		Token char_literal353 = null;
		JavaParser.formalParameter_return formalParameter352 = null;

		JavaParser.block_return block354 = null;

		CommonTree string_literal350_tree = null;
		CommonTree char_literal351_tree = null;
		CommonTree char_literal353_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 64)) { return retval; }

			// Java.g:903:5: ( 'catch' '(' formalParameter ')' block )
			// Java.g:903:9: 'catch' '(' formalParameter ')' block
			{
				root_0 = (CommonTree) adaptor.nil();

				string_literal350 = (Token) match(input, CATCH, FOLLOW_CATCH_in_catchClause4901);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal350_tree = (CommonTree) adaptor.create(string_literal350);
					adaptor.addChild(root_0, string_literal350_tree);
				}

				char_literal351 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_catchClause4903);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal351_tree = (CommonTree) adaptor.create(char_literal351);
					adaptor.addChild(root_0, char_literal351_tree);
				}

				pushFollow(FOLLOW_formalParameter_in_catchClause4905);
				formalParameter352 = formalParameter();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, formalParameter352.getTree());
				}

				char_literal353 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_catchClause4915);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal353_tree = (CommonTree) adaptor.create(char_literal353);
					adaptor.addChild(root_0, char_literal353_tree);
				}

				pushFollow(FOLLOW_block_in_catchClause4917);
				block354 = block();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, block354.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 64, catchClause_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "catchClause"

	public static class formalParameter_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "formalParameter"
	// Java.g:907:1: formalParameter : variableModifiers type IDENTIFIER ( '[' ']' )* ;
	public final JavaParser.formalParameter_return formalParameter() throws RecognitionException
	{
		JavaParser.formalParameter_return retval = new JavaParser.formalParameter_return();
		retval.start = input.LT(1);

		int formalParameter_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER357 = null;
		Token char_literal358 = null;
		Token char_literal359 = null;
		JavaParser.variableModifiers_return variableModifiers355 = null;

		JavaParser.type_return type356 = null;

		CommonTree IDENTIFIER357_tree = null;
		CommonTree char_literal358_tree = null;
		CommonTree char_literal359_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 65)) { return retval; }

			// Java.g:908:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* )
			// Java.g:908:9: variableModifiers type IDENTIFIER ( '[' ']' )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_variableModifiers_in_formalParameter4938);
				variableModifiers355 = variableModifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableModifiers355.getTree());
				}

				pushFollow(FOLLOW_type_in_formalParameter4940);
				type356 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type356.getTree());
				}

				IDENTIFIER357 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_formalParameter4942);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER357_tree = (CommonTree) adaptor.create(IDENTIFIER357);
					adaptor.addChild(root_0, IDENTIFIER357_tree);
				}

				// Java.g:909:9: ( '[' ']' )*
				loop104:
				do
				{
					int alt104 = 2;
					int LA104_0 = input.LA(1);

					if ((LA104_0 == LBRACKET))
					{
						alt104 = 1;
					}

					switch (alt104)
					{
						case 1:
						// Java.g:909:10: '[' ']'
						{
							char_literal358 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_formalParameter4953);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal358_tree = (CommonTree) adaptor.create(char_literal358);
								adaptor.addChild(root_0, char_literal358_tree);
							}

							char_literal359 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_formalParameter4955);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal359_tree = (CommonTree) adaptor.create(char_literal359);
								adaptor.addChild(root_0, char_literal359_tree);
							}

						}
							break;

						default:
							break loop104;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 65, formalParameter_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "formalParameter"

	public static class forstatement_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "forstatement"
	// Java.g:913:1: forstatement : ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement | 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement );
	public final JavaParser.forstatement_return forstatement() throws RecognitionException
	{
		JavaParser.forstatement_return retval = new JavaParser.forstatement_return();
		retval.start = input.LT(1);

		int forstatement_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal360 = null;
		Token char_literal361 = null;
		Token IDENTIFIER364 = null;
		Token char_literal365 = null;
		Token char_literal367 = null;
		Token string_literal369 = null;
		Token char_literal370 = null;
		Token char_literal372 = null;
		Token char_literal374 = null;
		Token char_literal376 = null;
		JavaParser.variableModifiers_return variableModifiers362 = null;

		JavaParser.type_return type363 = null;

		JavaParser.expression_return expression366 = null;

		JavaParser.statement_return statement368 = null;

		JavaParser.forInit_return forInit371 = null;

		JavaParser.expression_return expression373 = null;

		JavaParser.expressionList_return expressionList375 = null;

		JavaParser.statement_return statement377 = null;

		CommonTree string_literal360_tree = null;
		CommonTree char_literal361_tree = null;
		CommonTree IDENTIFIER364_tree = null;
		CommonTree char_literal365_tree = null;
		CommonTree char_literal367_tree = null;
		CommonTree string_literal369_tree = null;
		CommonTree char_literal370_tree = null;
		CommonTree char_literal372_tree = null;
		CommonTree char_literal374_tree = null;
		CommonTree char_literal376_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 66)) { return retval; }

			// Java.g:914:5: ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement | 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement )
			int alt108 = 2;
			int LA108_0 = input.LA(1);

			if ((LA108_0 == FOR))
			{
				int LA108_1 = input.LA(2);

				if ((synpred157_Java()))
				{
					alt108 = 1;
				}
				else if ((true))
				{
					alt108 = 2;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 108, 1, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 108, 0, input);

				throw nvae;

			}
			switch (alt108)
			{
				case 1:
				// Java.g:916:9: 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal360 = (Token) match(input, FOR, FOLLOW_FOR_in_forstatement5004);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal360_tree = (CommonTree) adaptor.create(string_literal360);
						adaptor.addChild(root_0, string_literal360_tree);
					}

					char_literal361 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_forstatement5006);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal361_tree = (CommonTree) adaptor.create(char_literal361);
						adaptor.addChild(root_0, char_literal361_tree);
					}

					pushFollow(FOLLOW_variableModifiers_in_forstatement5008);
					variableModifiers362 = variableModifiers();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, variableModifiers362.getTree());
					}

					pushFollow(FOLLOW_type_in_forstatement5010);
					type363 = type();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, type363.getTree());
					}

					IDENTIFIER364 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_forstatement5012);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER364_tree = (CommonTree) adaptor.create(IDENTIFIER364);
						adaptor.addChild(root_0, IDENTIFIER364_tree);
					}

					char_literal365 = (Token) match(input, COLON, FOLLOW_COLON_in_forstatement5014);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal365_tree = (CommonTree) adaptor.create(char_literal365);
						adaptor.addChild(root_0, char_literal365_tree);
					}

					pushFollow(FOLLOW_expression_in_forstatement5025);
					expression366 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression366.getTree());
					}

					char_literal367 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_forstatement5027);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal367_tree = (CommonTree) adaptor.create(char_literal367);
						adaptor.addChild(root_0, char_literal367_tree);
					}

					pushFollow(FOLLOW_statement_in_forstatement5029);
					statement368 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement368.getTree());
					}

				}
					break;
				case 2:
				// Java.g:920:9: 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal369 = (Token) match(input, FOR, FOLLOW_FOR_in_forstatement5061);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal369_tree = (CommonTree) adaptor.create(string_literal369);
						adaptor.addChild(root_0, string_literal369_tree);
					}

					char_literal370 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_forstatement5063);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal370_tree = (CommonTree) adaptor.create(char_literal370);
						adaptor.addChild(root_0, char_literal370_tree);
					}

					// Java.g:921:17: ( forInit )?
					int alt105 = 2;
					int LA105_0 = input.LA(1);

					if (((LA105_0 == BANG) || (LA105_0 == BOOLEAN) || (LA105_0 == BYTE) || ((LA105_0 >= CHAR) && (LA105_0 <= CHARLITERAL)) || ((LA105_0 >= DOUBLE) && (LA105_0 <= DOUBLELITERAL)) || ((LA105_0 >= FALSE) && (LA105_0 <= FINAL)) || ((LA105_0 >= FLOAT) && (LA105_0 <= FLOATLITERAL)) || (LA105_0 == IDENTIFIER) || (LA105_0 == INT) || (LA105_0 == INTLITERAL) || ((LA105_0 >= LONG) && (LA105_0 <= LPAREN)) || (LA105_0 == MONKEYS_AT) || ((LA105_0 >= NEW) && (LA105_0 <= NULL)) || (LA105_0 == PLUS) || (LA105_0 == PLUSPLUS) || (LA105_0 == SHORT) || ((LA105_0 >= STRINGLITERAL) && (LA105_0 <= SUB)) || ((LA105_0 >= SUBSUB) && (LA105_0 <= SUPER)) || (LA105_0 == THIS) || (LA105_0 == TILDE) || (LA105_0 == TRUE) || (LA105_0 == VOID)))
					{
						alt105 = 1;
					}
					switch (alt105)
					{
						case 1:
						// Java.g:921:18: forInit
						{
							pushFollow(FOLLOW_forInit_in_forstatement5083);
							forInit371 = forInit();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, forInit371.getTree());
							}

						}
							break;

					}

					char_literal372 = (Token) match(input, SEMI, FOLLOW_SEMI_in_forstatement5104);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal372_tree = (CommonTree) adaptor.create(char_literal372);
						adaptor.addChild(root_0, char_literal372_tree);
					}

					// Java.g:923:17: ( expression )?
					int alt106 = 2;
					int LA106_0 = input.LA(1);

					if (((LA106_0 == BANG) || (LA106_0 == BOOLEAN) || (LA106_0 == BYTE) || ((LA106_0 >= CHAR) && (LA106_0 <= CHARLITERAL)) || ((LA106_0 >= DOUBLE) && (LA106_0 <= DOUBLELITERAL)) || (LA106_0 == FALSE) || ((LA106_0 >= FLOAT) && (LA106_0 <= FLOATLITERAL)) || (LA106_0 == IDENTIFIER) || (LA106_0 == INT) || (LA106_0 == INTLITERAL) || ((LA106_0 >= LONG) && (LA106_0 <= LPAREN)) || ((LA106_0 >= NEW) && (LA106_0 <= NULL)) || (LA106_0 == PLUS) || (LA106_0 == PLUSPLUS) || (LA106_0 == SHORT) || ((LA106_0 >= STRINGLITERAL) && (LA106_0 <= SUB)) || ((LA106_0 >= SUBSUB) && (LA106_0 <= SUPER)) || (LA106_0 == THIS) || (LA106_0 == TILDE) || (LA106_0 == TRUE) || (LA106_0 == VOID)))
					{
						alt106 = 1;
					}
					switch (alt106)
					{
						case 1:
						// Java.g:923:18: expression
						{
							pushFollow(FOLLOW_expression_in_forstatement5124);
							expression373 = expression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, expression373.getTree());
							}

						}
							break;

					}

					char_literal374 = (Token) match(input, SEMI, FOLLOW_SEMI_in_forstatement5145);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal374_tree = (CommonTree) adaptor.create(char_literal374);
						adaptor.addChild(root_0, char_literal374_tree);
					}

					// Java.g:925:17: ( expressionList )?
					int alt107 = 2;
					int LA107_0 = input.LA(1);

					if (((LA107_0 == BANG) || (LA107_0 == BOOLEAN) || (LA107_0 == BYTE) || ((LA107_0 >= CHAR) && (LA107_0 <= CHARLITERAL)) || ((LA107_0 >= DOUBLE) && (LA107_0 <= DOUBLELITERAL)) || (LA107_0 == FALSE) || ((LA107_0 >= FLOAT) && (LA107_0 <= FLOATLITERAL)) || (LA107_0 == IDENTIFIER) || (LA107_0 == INT) || (LA107_0 == INTLITERAL) || ((LA107_0 >= LONG) && (LA107_0 <= LPAREN)) || ((LA107_0 >= NEW) && (LA107_0 <= NULL)) || (LA107_0 == PLUS) || (LA107_0 == PLUSPLUS) || (LA107_0 == SHORT) || ((LA107_0 >= STRINGLITERAL) && (LA107_0 <= SUB)) || ((LA107_0 >= SUBSUB) && (LA107_0 <= SUPER)) || (LA107_0 == THIS) || (LA107_0 == TILDE) || (LA107_0 == TRUE) || (LA107_0 == VOID)))
					{
						alt107 = 1;
					}
					switch (alt107)
					{
						case 1:
						// Java.g:925:18: expressionList
						{
							pushFollow(FOLLOW_expressionList_in_forstatement5165);
							expressionList375 = expressionList();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, expressionList375.getTree());
							}

						}
							break;

					}

					char_literal376 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_forstatement5186);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal376_tree = (CommonTree) adaptor.create(char_literal376);
						adaptor.addChild(root_0, char_literal376_tree);
					}

					pushFollow(FOLLOW_statement_in_forstatement5188);
					statement377 = statement();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, statement377.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 66, forstatement_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "forstatement"

	public static class forInit_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "forInit"
	// Java.g:929:1: forInit : ( localVariableDeclaration | expressionList );
	public final JavaParser.forInit_return forInit() throws RecognitionException
	{
		JavaParser.forInit_return retval = new JavaParser.forInit_return();
		retval.start = input.LT(1);

		int forInit_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.localVariableDeclaration_return localVariableDeclaration378 = null;

		JavaParser.expressionList_return expressionList379 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 67)) { return retval; }

			// Java.g:930:5: ( localVariableDeclaration | expressionList )
			int alt109 = 2;
			switch (input.LA(1))
			{
				case FINAL:
				case MONKEYS_AT:
				{
					alt109 = 1;
				}
					break;
				case IDENTIFIER:
				{
					int LA109_3 = input.LA(2);

					if ((synpred161_Java()))
					{
						alt109 = 1;
					}
					else if ((true))
					{
						alt109 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 109, 3, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA109_4 = input.LA(2);

					if ((synpred161_Java()))
					{
						alt109 = 1;
					}
					else if ((true))
					{
						alt109 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 109, 4, input);

						throw nvae;

					}
				}
					break;
				case BANG:
				case CHARLITERAL:
				case DOUBLELITERAL:
				case FALSE:
				case FLOATLITERAL:
				case INTLITERAL:
				case LONGLITERAL:
				case LPAREN:
				case NEW:
				case NULL:
				case PLUS:
				case PLUSPLUS:
				case STRINGLITERAL:
				case SUB:
				case SUBSUB:
				case SUPER:
				case THIS:
				case TILDE:
				case TRUE:
				case VOID:
				{
					alt109 = 2;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 109, 0, input);

					throw nvae;

			}

			switch (alt109)
			{
				case 1:
				// Java.g:930:9: localVariableDeclaration
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_localVariableDeclaration_in_forInit5208);
					localVariableDeclaration378 = localVariableDeclaration();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, localVariableDeclaration378.getTree());
					}

				}
					break;
				case 2:
				// Java.g:931:9: expressionList
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_expressionList_in_forInit5218);
					expressionList379 = expressionList();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expressionList379.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 67, forInit_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "forInit"

	public static class parExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "parExpression"
	// Java.g:934:1: parExpression : '(' expression ')' ;
	public final JavaParser.parExpression_return parExpression() throws RecognitionException
	{
		JavaParser.parExpression_return retval = new JavaParser.parExpression_return();
		retval.start = input.LT(1);

		int parExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal380 = null;
		Token char_literal382 = null;
		JavaParser.expression_return expression381 = null;

		CommonTree char_literal380_tree = null;
		CommonTree char_literal382_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 68)) { return retval; }

			// Java.g:935:5: ( '(' expression ')' )
			// Java.g:935:9: '(' expression ')'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal380 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_parExpression5238);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal380_tree = (CommonTree) adaptor.create(char_literal380);
					adaptor.addChild(root_0, char_literal380_tree);
				}

				pushFollow(FOLLOW_expression_in_parExpression5240);
				expression381 = expression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, expression381.getTree());
				}

				char_literal382 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_parExpression5242);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal382_tree = (CommonTree) adaptor.create(char_literal382);
					adaptor.addChild(root_0, char_literal382_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 68, parExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "parExpression"

	public static class expressionList_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "expressionList"
	// Java.g:938:1: expressionList : expression ( ',' expression )* ;
	public final JavaParser.expressionList_return expressionList() throws RecognitionException
	{
		JavaParser.expressionList_return retval = new JavaParser.expressionList_return();
		retval.start = input.LT(1);

		int expressionList_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal384 = null;
		JavaParser.expression_return expression383 = null;

		JavaParser.expression_return expression385 = null;

		CommonTree char_literal384_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 69)) { return retval; }

			// Java.g:939:5: ( expression ( ',' expression )* )
			// Java.g:939:9: expression ( ',' expression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_expression_in_expressionList5262);
				expression383 = expression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, expression383.getTree());
				}

				// Java.g:940:9: ( ',' expression )*
				loop110:
				do
				{
					int alt110 = 2;
					int LA110_0 = input.LA(1);

					if ((LA110_0 == COMMA))
					{
						alt110 = 1;
					}

					switch (alt110)
					{
						case 1:
						// Java.g:940:10: ',' expression
						{
							char_literal384 = (Token) match(input, COMMA, FOLLOW_COMMA_in_expressionList5273);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal384_tree = (CommonTree) adaptor.create(char_literal384);
								adaptor.addChild(root_0, char_literal384_tree);
							}

							pushFollow(FOLLOW_expression_in_expressionList5275);
							expression385 = expression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, expression385.getTree());
							}

						}
							break;

						default:
							break loop110;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 69, expressionList_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "expressionList"

	public static class expression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "expression"
	// Java.g:945:1: expression : conditionalExpression ( assignmentOperator expression )? ;
	public final JavaParser.expression_return expression() throws RecognitionException
	{
		JavaParser.expression_return retval = new JavaParser.expression_return();
		retval.start = input.LT(1);

		int expression_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.conditionalExpression_return conditionalExpression386 = null;

		JavaParser.assignmentOperator_return assignmentOperator387 = null;

		JavaParser.expression_return expression388 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 70)) { return retval; }

			// Java.g:946:5: ( conditionalExpression ( assignmentOperator expression )? )
			// Java.g:946:9: conditionalExpression ( assignmentOperator expression )?
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_conditionalExpression_in_expression5307);
				conditionalExpression386 = conditionalExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, conditionalExpression386.getTree());
				}

				// Java.g:947:9: ( assignmentOperator expression )?
				int alt111 = 2;
				int LA111_0 = input.LA(1);

				if (((LA111_0 == AMPEQ) || (LA111_0 == BAREQ) || (LA111_0 == CARETEQ) || (LA111_0 == EQ) || (LA111_0 == GT) || (LA111_0 == LT) || (LA111_0 == PERCENTEQ) || (LA111_0 == PLUSEQ) || (LA111_0 == SLASHEQ) || (LA111_0 == STAREQ) || (LA111_0 == SUBEQ)))
				{
					alt111 = 1;
				}
				switch (alt111)
				{
					case 1:
					// Java.g:947:10: assignmentOperator expression
					{
						pushFollow(FOLLOW_assignmentOperator_in_expression5318);
						assignmentOperator387 = assignmentOperator();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, assignmentOperator387.getTree());
						}

						pushFollow(FOLLOW_expression_in_expression5320);
						expression388 = expression();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, expression388.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 70, expression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "expression"

	public static class assignmentOperator_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "assignmentOperator"
	// Java.g:952:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' );
	public final JavaParser.assignmentOperator_return assignmentOperator() throws RecognitionException
	{
		JavaParser.assignmentOperator_return retval = new JavaParser.assignmentOperator_return();
		retval.start = input.LT(1);

		int assignmentOperator_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal389 = null;
		Token string_literal390 = null;
		Token string_literal391 = null;
		Token string_literal392 = null;
		Token string_literal393 = null;
		Token string_literal394 = null;
		Token string_literal395 = null;
		Token string_literal396 = null;
		Token string_literal397 = null;
		Token char_literal398 = null;
		Token char_literal399 = null;
		Token char_literal400 = null;
		Token char_literal401 = null;
		Token char_literal402 = null;
		Token char_literal403 = null;
		Token char_literal404 = null;
		Token char_literal405 = null;
		Token char_literal406 = null;
		Token char_literal407 = null;

		CommonTree char_literal389_tree = null;
		CommonTree string_literal390_tree = null;
		CommonTree string_literal391_tree = null;
		CommonTree string_literal392_tree = null;
		CommonTree string_literal393_tree = null;
		CommonTree string_literal394_tree = null;
		CommonTree string_literal395_tree = null;
		CommonTree string_literal396_tree = null;
		CommonTree string_literal397_tree = null;
		CommonTree char_literal398_tree = null;
		CommonTree char_literal399_tree = null;
		CommonTree char_literal400_tree = null;
		CommonTree char_literal401_tree = null;
		CommonTree char_literal402_tree = null;
		CommonTree char_literal403_tree = null;
		CommonTree char_literal404_tree = null;
		CommonTree char_literal405_tree = null;
		CommonTree char_literal406_tree = null;
		CommonTree char_literal407_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 71)) { return retval; }

			// Java.g:953:5: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' )
			int alt112 = 12;
			switch (input.LA(1))
			{
				case EQ:
				{
					alt112 = 1;
				}
					break;
				case PLUSEQ:
				{
					alt112 = 2;
				}
					break;
				case SUBEQ:
				{
					alt112 = 3;
				}
					break;
				case STAREQ:
				{
					alt112 = 4;
				}
					break;
				case SLASHEQ:
				{
					alt112 = 5;
				}
					break;
				case AMPEQ:
				{
					alt112 = 6;
				}
					break;
				case BAREQ:
				{
					alt112 = 7;
				}
					break;
				case CARETEQ:
				{
					alt112 = 8;
				}
					break;
				case PERCENTEQ:
				{
					alt112 = 9;
				}
					break;
				case LT:
				{
					alt112 = 10;
				}
					break;
				case GT:
				{
					int LA112_11 = input.LA(2);

					if ((LA112_11 == GT))
					{
						int LA112_12 = input.LA(3);

						if ((LA112_12 == GT))
						{
							alt112 = 11;
						}
						else if ((LA112_12 == EQ))
						{
							alt112 = 12;
						}
						else
						{
							if (state.backtracking > 0)
							{
								state.failed = true;
								return retval;
							}
							NoViableAltException nvae = new NoViableAltException("", 112, 12, input);

							throw nvae;

						}
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 112, 11, input);

						throw nvae;

					}
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 112, 0, input);

					throw nvae;

			}

			switch (alt112)
			{
				case 1:
				// Java.g:953:9: '='
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal389 = (Token) match(input, EQ, FOLLOW_EQ_in_assignmentOperator5352);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal389_tree = (CommonTree) adaptor.create(char_literal389);
						adaptor.addChild(root_0, char_literal389_tree);
					}

				}
					break;
				case 2:
				// Java.g:954:9: '+='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal390 = (Token) match(input, PLUSEQ, FOLLOW_PLUSEQ_in_assignmentOperator5362);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal390_tree = (CommonTree) adaptor.create(string_literal390);
						adaptor.addChild(root_0, string_literal390_tree);
					}

				}
					break;
				case 3:
				// Java.g:955:9: '-='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal391 = (Token) match(input, SUBEQ, FOLLOW_SUBEQ_in_assignmentOperator5372);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal391_tree = (CommonTree) adaptor.create(string_literal391);
						adaptor.addChild(root_0, string_literal391_tree);
					}

				}
					break;
				case 4:
				// Java.g:956:9: '*='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal392 = (Token) match(input, STAREQ, FOLLOW_STAREQ_in_assignmentOperator5382);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal392_tree = (CommonTree) adaptor.create(string_literal392);
						adaptor.addChild(root_0, string_literal392_tree);
					}

				}
					break;
				case 5:
				// Java.g:957:9: '/='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal393 = (Token) match(input, SLASHEQ, FOLLOW_SLASHEQ_in_assignmentOperator5392);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal393_tree = (CommonTree) adaptor.create(string_literal393);
						adaptor.addChild(root_0, string_literal393_tree);
					}

				}
					break;
				case 6:
				// Java.g:958:9: '&='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal394 = (Token) match(input, AMPEQ, FOLLOW_AMPEQ_in_assignmentOperator5402);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal394_tree = (CommonTree) adaptor.create(string_literal394);
						adaptor.addChild(root_0, string_literal394_tree);
					}

				}
					break;
				case 7:
				// Java.g:959:9: '|='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal395 = (Token) match(input, BAREQ, FOLLOW_BAREQ_in_assignmentOperator5412);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal395_tree = (CommonTree) adaptor.create(string_literal395);
						adaptor.addChild(root_0, string_literal395_tree);
					}

				}
					break;
				case 8:
				// Java.g:960:9: '^='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal396 = (Token) match(input, CARETEQ, FOLLOW_CARETEQ_in_assignmentOperator5422);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal396_tree = (CommonTree) adaptor.create(string_literal396);
						adaptor.addChild(root_0, string_literal396_tree);
					}

				}
					break;
				case 9:
				// Java.g:961:9: '%='
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal397 = (Token) match(input, PERCENTEQ, FOLLOW_PERCENTEQ_in_assignmentOperator5432);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal397_tree = (CommonTree) adaptor.create(string_literal397);
						adaptor.addChild(root_0, string_literal397_tree);
					}

				}
					break;
				case 10:
				// Java.g:962:10: '<' '<' '='
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal398 = (Token) match(input, LT, FOLLOW_LT_in_assignmentOperator5443);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal398_tree = (CommonTree) adaptor.create(char_literal398);
						adaptor.addChild(root_0, char_literal398_tree);
					}

					char_literal399 = (Token) match(input, LT, FOLLOW_LT_in_assignmentOperator5445);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal399_tree = (CommonTree) adaptor.create(char_literal399);
						adaptor.addChild(root_0, char_literal399_tree);
					}

					char_literal400 = (Token) match(input, EQ, FOLLOW_EQ_in_assignmentOperator5447);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal400_tree = (CommonTree) adaptor.create(char_literal400);
						adaptor.addChild(root_0, char_literal400_tree);
					}

				}
					break;
				case 11:
				// Java.g:963:10: '>' '>' '>' '='
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal401 = (Token) match(input, GT, FOLLOW_GT_in_assignmentOperator5458);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal401_tree = (CommonTree) adaptor.create(char_literal401);
						adaptor.addChild(root_0, char_literal401_tree);
					}

					char_literal402 = (Token) match(input, GT, FOLLOW_GT_in_assignmentOperator5460);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal402_tree = (CommonTree) adaptor.create(char_literal402);
						adaptor.addChild(root_0, char_literal402_tree);
					}

					char_literal403 = (Token) match(input, GT, FOLLOW_GT_in_assignmentOperator5462);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal403_tree = (CommonTree) adaptor.create(char_literal403);
						adaptor.addChild(root_0, char_literal403_tree);
					}

					char_literal404 = (Token) match(input, EQ, FOLLOW_EQ_in_assignmentOperator5464);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal404_tree = (CommonTree) adaptor.create(char_literal404);
						adaptor.addChild(root_0, char_literal404_tree);
					}

				}
					break;
				case 12:
				// Java.g:964:10: '>' '>' '='
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal405 = (Token) match(input, GT, FOLLOW_GT_in_assignmentOperator5475);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal405_tree = (CommonTree) adaptor.create(char_literal405);
						adaptor.addChild(root_0, char_literal405_tree);
					}

					char_literal406 = (Token) match(input, GT, FOLLOW_GT_in_assignmentOperator5477);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal406_tree = (CommonTree) adaptor.create(char_literal406);
						adaptor.addChild(root_0, char_literal406_tree);
					}

					char_literal407 = (Token) match(input, EQ, FOLLOW_EQ_in_assignmentOperator5479);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal407_tree = (CommonTree) adaptor.create(char_literal407);
						adaptor.addChild(root_0, char_literal407_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 71, assignmentOperator_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "assignmentOperator"

	public static class conditionalExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "conditionalExpression"
	// Java.g:968:1: conditionalExpression : conditionalOrExpression ( '?' expression ':' conditionalExpression )? ;
	public final JavaParser.conditionalExpression_return conditionalExpression() throws RecognitionException
	{
		JavaParser.conditionalExpression_return retval = new JavaParser.conditionalExpression_return();
		retval.start = input.LT(1);

		int conditionalExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal409 = null;
		Token char_literal411 = null;
		JavaParser.conditionalOrExpression_return conditionalOrExpression408 = null;

		JavaParser.expression_return expression410 = null;

		JavaParser.conditionalExpression_return conditionalExpression412 = null;

		CommonTree char_literal409_tree = null;
		CommonTree char_literal411_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 72)) { return retval; }

			// Java.g:969:5: ( conditionalOrExpression ( '?' expression ':' conditionalExpression )? )
			// Java.g:969:9: conditionalOrExpression ( '?' expression ':' conditionalExpression )?
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression5500);
				conditionalOrExpression408 = conditionalOrExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, conditionalOrExpression408.getTree());
				}

				// Java.g:970:9: ( '?' expression ':' conditionalExpression )?
				int alt113 = 2;
				int LA113_0 = input.LA(1);

				if ((LA113_0 == QUES))
				{
					alt113 = 1;
				}
				switch (alt113)
				{
					case 1:
					// Java.g:970:10: '?' expression ':' conditionalExpression
					{
						char_literal409 = (Token) match(input, QUES, FOLLOW_QUES_in_conditionalExpression5511);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal409_tree = (CommonTree) adaptor.create(char_literal409);
							adaptor.addChild(root_0, char_literal409_tree);
						}

						pushFollow(FOLLOW_expression_in_conditionalExpression5513);
						expression410 = expression();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, expression410.getTree());
						}

						char_literal411 = (Token) match(input, COLON, FOLLOW_COLON_in_conditionalExpression5515);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal411_tree = (CommonTree) adaptor.create(char_literal411);
							adaptor.addChild(root_0, char_literal411_tree);
						}

						pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression5517);
						conditionalExpression412 = conditionalExpression();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, conditionalExpression412.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 72, conditionalExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "conditionalExpression"

	public static class conditionalOrExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "conditionalOrExpression"
	// Java.g:974:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
	public final JavaParser.conditionalOrExpression_return conditionalOrExpression() throws RecognitionException
	{
		JavaParser.conditionalOrExpression_return retval = new JavaParser.conditionalOrExpression_return();
		retval.start = input.LT(1);

		int conditionalOrExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal414 = null;
		JavaParser.conditionalAndExpression_return conditionalAndExpression413 = null;

		JavaParser.conditionalAndExpression_return conditionalAndExpression415 = null;

		CommonTree string_literal414_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 73)) { return retval; }

			// Java.g:975:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
			// Java.g:975:9: conditionalAndExpression ( '||' conditionalAndExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression5548);
				conditionalAndExpression413 = conditionalAndExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, conditionalAndExpression413.getTree());
				}

				// Java.g:976:9: ( '||' conditionalAndExpression )*
				loop114:
				do
				{
					int alt114 = 2;
					int LA114_0 = input.LA(1);

					if ((LA114_0 == BARBAR))
					{
						alt114 = 1;
					}

					switch (alt114)
					{
						case 1:
						// Java.g:976:10: '||' conditionalAndExpression
						{
							string_literal414 = (Token) match(input, BARBAR, FOLLOW_BARBAR_in_conditionalOrExpression5559);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal414_tree = (CommonTree) adaptor.create(string_literal414);
								adaptor.addChild(root_0, string_literal414_tree);
							}

							pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression5561);
							conditionalAndExpression415 = conditionalAndExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, conditionalAndExpression415.getTree());
							}

						}
							break;

						default:
							break loop114;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 73, conditionalOrExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "conditionalOrExpression"

	public static class conditionalAndExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "conditionalAndExpression"
	// Java.g:980:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
	public final JavaParser.conditionalAndExpression_return conditionalAndExpression() throws RecognitionException
	{
		JavaParser.conditionalAndExpression_return retval = new JavaParser.conditionalAndExpression_return();
		retval.start = input.LT(1);

		int conditionalAndExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal417 = null;
		JavaParser.inclusiveOrExpression_return inclusiveOrExpression416 = null;

		JavaParser.inclusiveOrExpression_return inclusiveOrExpression418 = null;

		CommonTree string_literal417_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 74)) { return retval; }

			// Java.g:981:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
			// Java.g:981:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5592);
				inclusiveOrExpression416 = inclusiveOrExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, inclusiveOrExpression416.getTree());
				}

				// Java.g:982:9: ( '&&' inclusiveOrExpression )*
				loop115:
				do
				{
					int alt115 = 2;
					int LA115_0 = input.LA(1);

					if ((LA115_0 == AMPAMP))
					{
						alt115 = 1;
					}

					switch (alt115)
					{
						case 1:
						// Java.g:982:10: '&&' inclusiveOrExpression
						{
							string_literal417 = (Token) match(input, AMPAMP, FOLLOW_AMPAMP_in_conditionalAndExpression5603);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal417_tree = (CommonTree) adaptor.create(string_literal417);
								adaptor.addChild(root_0, string_literal417_tree);
							}

							pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5605);
							inclusiveOrExpression418 = inclusiveOrExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, inclusiveOrExpression418.getTree());
							}

						}
							break;

						default:
							break loop115;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 74, conditionalAndExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "conditionalAndExpression"

	public static class inclusiveOrExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "inclusiveOrExpression"
	// Java.g:986:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
	public final JavaParser.inclusiveOrExpression_return inclusiveOrExpression() throws RecognitionException
	{
		JavaParser.inclusiveOrExpression_return retval = new JavaParser.inclusiveOrExpression_return();
		retval.start = input.LT(1);

		int inclusiveOrExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal420 = null;
		JavaParser.exclusiveOrExpression_return exclusiveOrExpression419 = null;

		JavaParser.exclusiveOrExpression_return exclusiveOrExpression421 = null;

		CommonTree char_literal420_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 75)) { return retval; }

			// Java.g:987:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
			// Java.g:987:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5636);
				exclusiveOrExpression419 = exclusiveOrExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, exclusiveOrExpression419.getTree());
				}

				// Java.g:988:9: ( '|' exclusiveOrExpression )*
				loop116:
				do
				{
					int alt116 = 2;
					int LA116_0 = input.LA(1);

					if ((LA116_0 == BAR))
					{
						alt116 = 1;
					}

					switch (alt116)
					{
						case 1:
						// Java.g:988:10: '|' exclusiveOrExpression
						{
							char_literal420 = (Token) match(input, BAR, FOLLOW_BAR_in_inclusiveOrExpression5647);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal420_tree = (CommonTree) adaptor.create(char_literal420);
								adaptor.addChild(root_0, char_literal420_tree);
							}

							pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5649);
							exclusiveOrExpression421 = exclusiveOrExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, exclusiveOrExpression421.getTree());
							}

						}
							break;

						default:
							break loop116;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 75, inclusiveOrExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "inclusiveOrExpression"

	public static class exclusiveOrExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "exclusiveOrExpression"
	// Java.g:992:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
	public final JavaParser.exclusiveOrExpression_return exclusiveOrExpression() throws RecognitionException
	{
		JavaParser.exclusiveOrExpression_return retval = new JavaParser.exclusiveOrExpression_return();
		retval.start = input.LT(1);

		int exclusiveOrExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal423 = null;
		JavaParser.andExpression_return andExpression422 = null;

		JavaParser.andExpression_return andExpression424 = null;

		CommonTree char_literal423_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 76)) { return retval; }

			// Java.g:993:5: ( andExpression ( '^' andExpression )* )
			// Java.g:993:9: andExpression ( '^' andExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression5680);
				andExpression422 = andExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, andExpression422.getTree());
				}

				// Java.g:994:9: ( '^' andExpression )*
				loop117:
				do
				{
					int alt117 = 2;
					int LA117_0 = input.LA(1);

					if ((LA117_0 == CARET))
					{
						alt117 = 1;
					}

					switch (alt117)
					{
						case 1:
						// Java.g:994:10: '^' andExpression
						{
							char_literal423 = (Token) match(input, CARET, FOLLOW_CARET_in_exclusiveOrExpression5691);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal423_tree = (CommonTree) adaptor.create(char_literal423);
								adaptor.addChild(root_0, char_literal423_tree);
							}

							pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression5693);
							andExpression424 = andExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, andExpression424.getTree());
							}

						}
							break;

						default:
							break loop117;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 76, exclusiveOrExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "exclusiveOrExpression"

	public static class andExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "andExpression"
	// Java.g:998:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
	public final JavaParser.andExpression_return andExpression() throws RecognitionException
	{
		JavaParser.andExpression_return retval = new JavaParser.andExpression_return();
		retval.start = input.LT(1);

		int andExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal426 = null;
		JavaParser.equalityExpression_return equalityExpression425 = null;

		JavaParser.equalityExpression_return equalityExpression427 = null;

		CommonTree char_literal426_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 77)) { return retval; }

			// Java.g:999:5: ( equalityExpression ( '&' equalityExpression )* )
			// Java.g:999:9: equalityExpression ( '&' equalityExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_equalityExpression_in_andExpression5724);
				equalityExpression425 = equalityExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, equalityExpression425.getTree());
				}

				// Java.g:1000:9: ( '&' equalityExpression )*
				loop118:
				do
				{
					int alt118 = 2;
					int LA118_0 = input.LA(1);

					if ((LA118_0 == AMP))
					{
						alt118 = 1;
					}

					switch (alt118)
					{
						case 1:
						// Java.g:1000:10: '&' equalityExpression
						{
							char_literal426 = (Token) match(input, AMP, FOLLOW_AMP_in_andExpression5735);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal426_tree = (CommonTree) adaptor.create(char_literal426);
								adaptor.addChild(root_0, char_literal426_tree);
							}

							pushFollow(FOLLOW_equalityExpression_in_andExpression5737);
							equalityExpression427 = equalityExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, equalityExpression427.getTree());
							}

						}
							break;

						default:
							break loop118;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 77, andExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "andExpression"

	public static class equalityExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "equalityExpression"
	// Java.g:1004:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
	public final JavaParser.equalityExpression_return equalityExpression() throws RecognitionException
	{
		JavaParser.equalityExpression_return retval = new JavaParser.equalityExpression_return();
		retval.start = input.LT(1);

		int equalityExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set429 = null;
		JavaParser.instanceOfExpression_return instanceOfExpression428 = null;

		JavaParser.instanceOfExpression_return instanceOfExpression430 = null;

		CommonTree set429_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 78)) { return retval; }

			// Java.g:1005:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
			// Java.g:1005:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression5768);
				instanceOfExpression428 = instanceOfExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, instanceOfExpression428.getTree());
				}

				// Java.g:1006:9: ( ( '==' | '!=' ) instanceOfExpression )*
				loop119:
				do
				{
					int alt119 = 2;
					int LA119_0 = input.LA(1);

					if (((LA119_0 == BANGEQ) || (LA119_0 == EQEQ)))
					{
						alt119 = 1;
					}

					switch (alt119)
					{
						case 1:
						// Java.g:1007:13: ( '==' | '!=' ) instanceOfExpression
						{
							set429 = (Token) input.LT(1);

							if ((input.LA(1) == BANGEQ) || (input.LA(1) == EQEQ))
							{
								input.consume();
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, (CommonTree) adaptor.create(set429));
								}
								state.errorRecovery = false;
								state.failed = false;
							}
							else
							{
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								MismatchedSetException mse = new MismatchedSetException(null, input);
								throw mse;
							}

							pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression5845);
							instanceOfExpression430 = instanceOfExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, instanceOfExpression430.getTree());
							}

						}
							break;

						default:
							break loop119;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 78, equalityExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "equalityExpression"

	public static class instanceOfExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "instanceOfExpression"
	// Java.g:1014:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
	public final JavaParser.instanceOfExpression_return instanceOfExpression() throws RecognitionException
	{
		JavaParser.instanceOfExpression_return retval = new JavaParser.instanceOfExpression_return();
		retval.start = input.LT(1);

		int instanceOfExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal432 = null;
		JavaParser.relationalExpression_return relationalExpression431 = null;

		JavaParser.type_return type433 = null;

		CommonTree string_literal432_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 79)) { return retval; }

			// Java.g:1015:5: ( relationalExpression ( 'instanceof' type )? )
			// Java.g:1015:9: relationalExpression ( 'instanceof' type )?
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression5876);
				relationalExpression431 = relationalExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, relationalExpression431.getTree());
				}

				// Java.g:1016:9: ( 'instanceof' type )?
				int alt120 = 2;
				int LA120_0 = input.LA(1);

				if ((LA120_0 == INSTANCEOF))
				{
					alt120 = 1;
				}
				switch (alt120)
				{
					case 1:
					// Java.g:1016:10: 'instanceof' type
					{
						string_literal432 = (Token) match(input, INSTANCEOF, FOLLOW_INSTANCEOF_in_instanceOfExpression5887);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal432_tree = (CommonTree) adaptor.create(string_literal432);
							adaptor.addChild(root_0, string_literal432_tree);
						}

						pushFollow(FOLLOW_type_in_instanceOfExpression5889);
						type433 = type();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, type433.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 79, instanceOfExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "instanceOfExpression"

	public static class relationalExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "relationalExpression"
	// Java.g:1020:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
	public final JavaParser.relationalExpression_return relationalExpression() throws RecognitionException
	{
		JavaParser.relationalExpression_return retval = new JavaParser.relationalExpression_return();
		retval.start = input.LT(1);

		int relationalExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.shiftExpression_return shiftExpression434 = null;

		JavaParser.relationalOp_return relationalOp435 = null;

		JavaParser.shiftExpression_return shiftExpression436 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 80)) { return retval; }

			// Java.g:1021:5: ( shiftExpression ( relationalOp shiftExpression )* )
			// Java.g:1021:9: shiftExpression ( relationalOp shiftExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_shiftExpression_in_relationalExpression5920);
				shiftExpression434 = shiftExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, shiftExpression434.getTree());
				}

				// Java.g:1022:9: ( relationalOp shiftExpression )*
				loop121:
				do
				{
					int alt121 = 2;
					int LA121_0 = input.LA(1);

					if ((LA121_0 == LT))
					{
						int LA121_2 = input.LA(2);

						if (((LA121_2 == BANG) || (LA121_2 == BOOLEAN) || (LA121_2 == BYTE) || ((LA121_2 >= CHAR) && (LA121_2 <= CHARLITERAL)) || ((LA121_2 >= DOUBLE) && (LA121_2 <= DOUBLELITERAL)) || (LA121_2 == EQ) || (LA121_2 == FALSE) || ((LA121_2 >= FLOAT) && (LA121_2 <= FLOATLITERAL)) || (LA121_2 == IDENTIFIER) || (LA121_2 == INT) || (LA121_2 == INTLITERAL) || ((LA121_2 >= LONG) && (LA121_2 <= LPAREN)) || ((LA121_2 >= NEW) && (LA121_2 <= NULL)) || (LA121_2 == PLUS) || (LA121_2 == PLUSPLUS) || (LA121_2 == SHORT) || ((LA121_2 >= STRINGLITERAL) && (LA121_2 <= SUB)) || ((LA121_2 >= SUBSUB) && (LA121_2 <= SUPER)) || (LA121_2 == THIS) || (LA121_2 == TILDE) || (LA121_2 == TRUE) || (LA121_2 == VOID)))
						{
							alt121 = 1;
						}

					}
					else if ((LA121_0 == GT))
					{
						int LA121_3 = input.LA(2);

						if (((LA121_3 == BANG) || (LA121_3 == BOOLEAN) || (LA121_3 == BYTE) || ((LA121_3 >= CHAR) && (LA121_3 <= CHARLITERAL)) || ((LA121_3 >= DOUBLE) && (LA121_3 <= DOUBLELITERAL)) || (LA121_3 == EQ) || (LA121_3 == FALSE) || ((LA121_3 >= FLOAT) && (LA121_3 <= FLOATLITERAL)) || (LA121_3 == IDENTIFIER) || (LA121_3 == INT) || (LA121_3 == INTLITERAL) || ((LA121_3 >= LONG) && (LA121_3 <= LPAREN)) || ((LA121_3 >= NEW) && (LA121_3 <= NULL)) || (LA121_3 == PLUS) || (LA121_3 == PLUSPLUS) || (LA121_3 == SHORT) || ((LA121_3 >= STRINGLITERAL) && (LA121_3 <= SUB)) || ((LA121_3 >= SUBSUB) && (LA121_3 <= SUPER)) || (LA121_3 == THIS) || (LA121_3 == TILDE) || (LA121_3 == TRUE) || (LA121_3 == VOID)))
						{
							alt121 = 1;
						}

					}

					switch (alt121)
					{
						case 1:
						// Java.g:1022:10: relationalOp shiftExpression
						{
							pushFollow(FOLLOW_relationalOp_in_relationalExpression5931);
							relationalOp435 = relationalOp();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, relationalOp435.getTree());
							}

							pushFollow(FOLLOW_shiftExpression_in_relationalExpression5933);
							shiftExpression436 = shiftExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, shiftExpression436.getTree());
							}

						}
							break;

						default:
							break loop121;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 80, relationalExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "relationalExpression"

	public static class relationalOp_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "relationalOp"
	// Java.g:1026:1: relationalOp : ( '<' '=' | '>' '=' | '<' | '>' );
	public final JavaParser.relationalOp_return relationalOp() throws RecognitionException
	{
		JavaParser.relationalOp_return retval = new JavaParser.relationalOp_return();
		retval.start = input.LT(1);

		int relationalOp_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal437 = null;
		Token char_literal438 = null;
		Token char_literal439 = null;
		Token char_literal440 = null;
		Token char_literal441 = null;
		Token char_literal442 = null;

		CommonTree char_literal437_tree = null;
		CommonTree char_literal438_tree = null;
		CommonTree char_literal439_tree = null;
		CommonTree char_literal440_tree = null;
		CommonTree char_literal441_tree = null;
		CommonTree char_literal442_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 81)) { return retval; }

			// Java.g:1027:5: ( '<' '=' | '>' '=' | '<' | '>' )
			int alt122 = 4;
			int LA122_0 = input.LA(1);

			if ((LA122_0 == LT))
			{
				int LA122_1 = input.LA(2);

				if ((LA122_1 == EQ))
				{
					alt122 = 1;
				}
				else if (((LA122_1 == BANG) || (LA122_1 == BOOLEAN) || (LA122_1 == BYTE) || ((LA122_1 >= CHAR) && (LA122_1 <= CHARLITERAL)) || ((LA122_1 >= DOUBLE) && (LA122_1 <= DOUBLELITERAL)) || (LA122_1 == FALSE) || ((LA122_1 >= FLOAT) && (LA122_1 <= FLOATLITERAL)) || (LA122_1 == IDENTIFIER) || (LA122_1 == INT) || (LA122_1 == INTLITERAL) || ((LA122_1 >= LONG) && (LA122_1 <= LPAREN)) || ((LA122_1 >= NEW) && (LA122_1 <= NULL)) || (LA122_1 == PLUS) || (LA122_1 == PLUSPLUS) || (LA122_1 == SHORT) || ((LA122_1 >= STRINGLITERAL) && (LA122_1 <= SUB)) || ((LA122_1 >= SUBSUB) && (LA122_1 <= SUPER)) || (LA122_1 == THIS) || (LA122_1 == TILDE) || (LA122_1 == TRUE) || (LA122_1 == VOID)))
				{
					alt122 = 3;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 122, 1, input);

					throw nvae;

				}
			}
			else if ((LA122_0 == GT))
			{
				int LA122_2 = input.LA(2);

				if ((LA122_2 == EQ))
				{
					alt122 = 2;
				}
				else if (((LA122_2 == BANG) || (LA122_2 == BOOLEAN) || (LA122_2 == BYTE) || ((LA122_2 >= CHAR) && (LA122_2 <= CHARLITERAL)) || ((LA122_2 >= DOUBLE) && (LA122_2 <= DOUBLELITERAL)) || (LA122_2 == FALSE) || ((LA122_2 >= FLOAT) && (LA122_2 <= FLOATLITERAL)) || (LA122_2 == IDENTIFIER) || (LA122_2 == INT) || (LA122_2 == INTLITERAL) || ((LA122_2 >= LONG) && (LA122_2 <= LPAREN)) || ((LA122_2 >= NEW) && (LA122_2 <= NULL)) || (LA122_2 == PLUS) || (LA122_2 == PLUSPLUS) || (LA122_2 == SHORT) || ((LA122_2 >= STRINGLITERAL) && (LA122_2 <= SUB)) || ((LA122_2 >= SUBSUB) && (LA122_2 <= SUPER)) || (LA122_2 == THIS) || (LA122_2 == TILDE) || (LA122_2 == TRUE) || (LA122_2 == VOID)))
				{
					alt122 = 4;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 122, 2, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 122, 0, input);

				throw nvae;

			}
			switch (alt122)
			{
				case 1:
				// Java.g:1027:10: '<' '='
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal437 = (Token) match(input, LT, FOLLOW_LT_in_relationalOp5965);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal437_tree = (CommonTree) adaptor.create(char_literal437);
						adaptor.addChild(root_0, char_literal437_tree);
					}

					char_literal438 = (Token) match(input, EQ, FOLLOW_EQ_in_relationalOp5967);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal438_tree = (CommonTree) adaptor.create(char_literal438);
						adaptor.addChild(root_0, char_literal438_tree);
					}

				}
					break;
				case 2:
				// Java.g:1028:10: '>' '='
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal439 = (Token) match(input, GT, FOLLOW_GT_in_relationalOp5978);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal439_tree = (CommonTree) adaptor.create(char_literal439);
						adaptor.addChild(root_0, char_literal439_tree);
					}

					char_literal440 = (Token) match(input, EQ, FOLLOW_EQ_in_relationalOp5980);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal440_tree = (CommonTree) adaptor.create(char_literal440);
						adaptor.addChild(root_0, char_literal440_tree);
					}

				}
					break;
				case 3:
				// Java.g:1029:9: '<'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal441 = (Token) match(input, LT, FOLLOW_LT_in_relationalOp5990);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal441_tree = (CommonTree) adaptor.create(char_literal441);
						adaptor.addChild(root_0, char_literal441_tree);
					}

				}
					break;
				case 4:
				// Java.g:1030:9: '>'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal442 = (Token) match(input, GT, FOLLOW_GT_in_relationalOp6000);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal442_tree = (CommonTree) adaptor.create(char_literal442);
						adaptor.addChild(root_0, char_literal442_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 81, relationalOp_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "relationalOp"

	public static class shiftExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "shiftExpression"
	// Java.g:1033:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
	public final JavaParser.shiftExpression_return shiftExpression() throws RecognitionException
	{
		JavaParser.shiftExpression_return retval = new JavaParser.shiftExpression_return();
		retval.start = input.LT(1);

		int shiftExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.additiveExpression_return additiveExpression443 = null;

		JavaParser.shiftOp_return shiftOp444 = null;

		JavaParser.additiveExpression_return additiveExpression445 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 82)) { return retval; }

			// Java.g:1034:5: ( additiveExpression ( shiftOp additiveExpression )* )
			// Java.g:1034:9: additiveExpression ( shiftOp additiveExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_additiveExpression_in_shiftExpression6020);
				additiveExpression443 = additiveExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, additiveExpression443.getTree());
				}

				// Java.g:1035:9: ( shiftOp additiveExpression )*
				loop123:
				do
				{
					int alt123 = 2;
					int LA123_0 = input.LA(1);

					if ((LA123_0 == LT))
					{
						int LA123_1 = input.LA(2);

						if ((LA123_1 == LT))
						{
							int LA123_4 = input.LA(3);

							if (((LA123_4 == BANG) || (LA123_4 == BOOLEAN) || (LA123_4 == BYTE) || ((LA123_4 >= CHAR) && (LA123_4 <= CHARLITERAL)) || ((LA123_4 >= DOUBLE) && (LA123_4 <= DOUBLELITERAL)) || (LA123_4 == FALSE) || ((LA123_4 >= FLOAT) && (LA123_4 <= FLOATLITERAL)) || (LA123_4 == IDENTIFIER) || (LA123_4 == INT) || (LA123_4 == INTLITERAL) || ((LA123_4 >= LONG) && (LA123_4 <= LPAREN)) || ((LA123_4 >= NEW) && (LA123_4 <= NULL)) || (LA123_4 == PLUS) || (LA123_4 == PLUSPLUS) || (LA123_4 == SHORT) || ((LA123_4 >= STRINGLITERAL) && (LA123_4 <= SUB)) || ((LA123_4 >= SUBSUB) && (LA123_4 <= SUPER)) || (LA123_4 == THIS) || (LA123_4 == TILDE) || (LA123_4 == TRUE) || (LA123_4 == VOID)))
							{
								alt123 = 1;
							}

						}

					}
					else if ((LA123_0 == GT))
					{
						int LA123_2 = input.LA(2);

						if ((LA123_2 == GT))
						{
							int LA123_5 = input.LA(3);

							if ((LA123_5 == GT))
							{
								int LA123_7 = input.LA(4);

								if (((LA123_7 == BANG) || (LA123_7 == BOOLEAN) || (LA123_7 == BYTE) || ((LA123_7 >= CHAR) && (LA123_7 <= CHARLITERAL)) || ((LA123_7 >= DOUBLE) && (LA123_7 <= DOUBLELITERAL)) || (LA123_7 == FALSE) || ((LA123_7 >= FLOAT) && (LA123_7 <= FLOATLITERAL)) || (LA123_7 == IDENTIFIER) || (LA123_7 == INT) || (LA123_7 == INTLITERAL) || ((LA123_7 >= LONG) && (LA123_7 <= LPAREN)) || ((LA123_7 >= NEW) && (LA123_7 <= NULL)) || (LA123_7 == PLUS) || (LA123_7 == PLUSPLUS) || (LA123_7 == SHORT) || ((LA123_7 >= STRINGLITERAL) && (LA123_7 <= SUB)) || ((LA123_7 >= SUBSUB) && (LA123_7 <= SUPER)) || (LA123_7 == THIS) || (LA123_7 == TILDE) || (LA123_7 == TRUE) || (LA123_7 == VOID)))
								{
									alt123 = 1;
								}

							}
							else if (((LA123_5 == BANG) || (LA123_5 == BOOLEAN) || (LA123_5 == BYTE) || ((LA123_5 >= CHAR) && (LA123_5 <= CHARLITERAL)) || ((LA123_5 >= DOUBLE) && (LA123_5 <= DOUBLELITERAL)) || (LA123_5 == FALSE) || ((LA123_5 >= FLOAT) && (LA123_5 <= FLOATLITERAL)) || (LA123_5 == IDENTIFIER) || (LA123_5 == INT) || (LA123_5 == INTLITERAL) || ((LA123_5 >= LONG) && (LA123_5 <= LPAREN)) || ((LA123_5 >= NEW) && (LA123_5 <= NULL)) || (LA123_5 == PLUS) || (LA123_5 == PLUSPLUS) || (LA123_5 == SHORT) || ((LA123_5 >= STRINGLITERAL) && (LA123_5 <= SUB)) || ((LA123_5 >= SUBSUB) && (LA123_5 <= SUPER)) || (LA123_5 == THIS) || (LA123_5 == TILDE) || (LA123_5 == TRUE) || (LA123_5 == VOID)))
							{
								alt123 = 1;
							}

						}

					}

					switch (alt123)
					{
						case 1:
						// Java.g:1035:10: shiftOp additiveExpression
						{
							pushFollow(FOLLOW_shiftOp_in_shiftExpression6031);
							shiftOp444 = shiftOp();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, shiftOp444.getTree());
							}

							pushFollow(FOLLOW_additiveExpression_in_shiftExpression6033);
							additiveExpression445 = additiveExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, additiveExpression445.getTree());
							}

						}
							break;

						default:
							break loop123;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 82, shiftExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "shiftExpression"

	public static class shiftOp_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "shiftOp"
	// Java.g:1040:1: shiftOp : ( '<' '<' | '>' '>' '>' | '>' '>' );
	public final JavaParser.shiftOp_return shiftOp() throws RecognitionException
	{
		JavaParser.shiftOp_return retval = new JavaParser.shiftOp_return();
		retval.start = input.LT(1);

		int shiftOp_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal446 = null;
		Token char_literal447 = null;
		Token char_literal448 = null;
		Token char_literal449 = null;
		Token char_literal450 = null;
		Token char_literal451 = null;
		Token char_literal452 = null;

		CommonTree char_literal446_tree = null;
		CommonTree char_literal447_tree = null;
		CommonTree char_literal448_tree = null;
		CommonTree char_literal449_tree = null;
		CommonTree char_literal450_tree = null;
		CommonTree char_literal451_tree = null;
		CommonTree char_literal452_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 83)) { return retval; }

			// Java.g:1041:5: ( '<' '<' | '>' '>' '>' | '>' '>' )
			int alt124 = 3;
			int LA124_0 = input.LA(1);

			if ((LA124_0 == LT))
			{
				alt124 = 1;
			}
			else if ((LA124_0 == GT))
			{
				int LA124_2 = input.LA(2);

				if ((LA124_2 == GT))
				{
					int LA124_3 = input.LA(3);

					if ((LA124_3 == GT))
					{
						alt124 = 2;
					}
					else if (((LA124_3 == BANG) || (LA124_3 == BOOLEAN) || (LA124_3 == BYTE) || ((LA124_3 >= CHAR) && (LA124_3 <= CHARLITERAL)) || ((LA124_3 >= DOUBLE) && (LA124_3 <= DOUBLELITERAL)) || (LA124_3 == FALSE) || ((LA124_3 >= FLOAT) && (LA124_3 <= FLOATLITERAL)) || (LA124_3 == IDENTIFIER) || (LA124_3 == INT) || (LA124_3 == INTLITERAL) || ((LA124_3 >= LONG) && (LA124_3 <= LPAREN)) || ((LA124_3 >= NEW) && (LA124_3 <= NULL)) || (LA124_3 == PLUS) || (LA124_3 == PLUSPLUS) || (LA124_3 == SHORT) || ((LA124_3 >= STRINGLITERAL) && (LA124_3 <= SUB)) || ((LA124_3 >= SUBSUB) && (LA124_3 <= SUPER)) || (LA124_3 == THIS) || (LA124_3 == TILDE) || (LA124_3 == TRUE) || (LA124_3 == VOID)))
					{
						alt124 = 3;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 124, 3, input);

						throw nvae;

					}
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 124, 2, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 124, 0, input);

				throw nvae;

			}
			switch (alt124)
			{
				case 1:
				// Java.g:1041:10: '<' '<'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal446 = (Token) match(input, LT, FOLLOW_LT_in_shiftOp6066);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal446_tree = (CommonTree) adaptor.create(char_literal446);
						adaptor.addChild(root_0, char_literal446_tree);
					}

					char_literal447 = (Token) match(input, LT, FOLLOW_LT_in_shiftOp6068);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal447_tree = (CommonTree) adaptor.create(char_literal447);
						adaptor.addChild(root_0, char_literal447_tree);
					}

				}
					break;
				case 2:
				// Java.g:1042:10: '>' '>' '>'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal448 = (Token) match(input, GT, FOLLOW_GT_in_shiftOp6079);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal448_tree = (CommonTree) adaptor.create(char_literal448);
						adaptor.addChild(root_0, char_literal448_tree);
					}

					char_literal449 = (Token) match(input, GT, FOLLOW_GT_in_shiftOp6081);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal449_tree = (CommonTree) adaptor.create(char_literal449);
						adaptor.addChild(root_0, char_literal449_tree);
					}

					char_literal450 = (Token) match(input, GT, FOLLOW_GT_in_shiftOp6083);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal450_tree = (CommonTree) adaptor.create(char_literal450);
						adaptor.addChild(root_0, char_literal450_tree);
					}

				}
					break;
				case 3:
				// Java.g:1043:10: '>' '>'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal451 = (Token) match(input, GT, FOLLOW_GT_in_shiftOp6094);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal451_tree = (CommonTree) adaptor.create(char_literal451);
						adaptor.addChild(root_0, char_literal451_tree);
					}

					char_literal452 = (Token) match(input, GT, FOLLOW_GT_in_shiftOp6096);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal452_tree = (CommonTree) adaptor.create(char_literal452);
						adaptor.addChild(root_0, char_literal452_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 83, shiftOp_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "shiftOp"

	public static class additiveExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "additiveExpression"
	// Java.g:1047:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
	public final JavaParser.additiveExpression_return additiveExpression() throws RecognitionException
	{
		JavaParser.additiveExpression_return retval = new JavaParser.additiveExpression_return();
		retval.start = input.LT(1);

		int additiveExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set454 = null;
		JavaParser.multiplicativeExpression_return multiplicativeExpression453 = null;

		JavaParser.multiplicativeExpression_return multiplicativeExpression455 = null;

		CommonTree set454_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 84)) { return retval; }

			// Java.g:1048:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
			// Java.g:1048:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression6117);
				multiplicativeExpression453 = multiplicativeExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, multiplicativeExpression453.getTree());
				}

				// Java.g:1049:9: ( ( '+' | '-' ) multiplicativeExpression )*
				loop125:
				do
				{
					int alt125 = 2;
					int LA125_0 = input.LA(1);

					if (((LA125_0 == PLUS) || (LA125_0 == SUB)))
					{
						alt125 = 1;
					}

					switch (alt125)
					{
						case 1:
						// Java.g:1050:13: ( '+' | '-' ) multiplicativeExpression
						{
							set454 = (Token) input.LT(1);

							if ((input.LA(1) == PLUS) || (input.LA(1) == SUB))
							{
								input.consume();
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, (CommonTree) adaptor.create(set454));
								}
								state.errorRecovery = false;
								state.failed = false;
							}
							else
							{
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								MismatchedSetException mse = new MismatchedSetException(null, input);
								throw mse;
							}

							pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression6194);
							multiplicativeExpression455 = multiplicativeExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, multiplicativeExpression455.getTree());
							}

						}
							break;

						default:
							break loop125;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 84, additiveExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "additiveExpression"

	public static class multiplicativeExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "multiplicativeExpression"
	// Java.g:1057:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
	public final JavaParser.multiplicativeExpression_return multiplicativeExpression() throws RecognitionException
	{
		JavaParser.multiplicativeExpression_return retval = new JavaParser.multiplicativeExpression_return();
		retval.start = input.LT(1);

		int multiplicativeExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set457 = null;
		JavaParser.unaryExpression_return unaryExpression456 = null;

		JavaParser.unaryExpression_return unaryExpression458 = null;

		CommonTree set457_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 85)) { return retval; }

			// Java.g:1058:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
			// Java.g:1059:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6232);
				unaryExpression456 = unaryExpression();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, unaryExpression456.getTree());
				}

				// Java.g:1060:9: ( ( '*' | '/' | '%' ) unaryExpression )*
				loop126:
				do
				{
					int alt126 = 2;
					int LA126_0 = input.LA(1);

					if (((LA126_0 == PERCENT) || (LA126_0 == SLASH) || (LA126_0 == STAR)))
					{
						alt126 = 1;
					}

					switch (alt126)
					{
						case 1:
						// Java.g:1061:13: ( '*' | '/' | '%' ) unaryExpression
						{
							set457 = (Token) input.LT(1);

							if ((input.LA(1) == PERCENT) || (input.LA(1) == SLASH) || (input.LA(1) == STAR))
							{
								input.consume();
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, (CommonTree) adaptor.create(set457));
								}
								state.errorRecovery = false;
								state.failed = false;
							}
							else
							{
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								MismatchedSetException mse = new MismatchedSetException(null, input);
								throw mse;
							}

							pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6327);
							unaryExpression458 = unaryExpression();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, unaryExpression458.getTree());
							}

						}
							break;

						default:
							break loop126;
					}
				}
				while (true);

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 85, multiplicativeExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "multiplicativeExpression"

	public static class unaryExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "unaryExpression"
	// Java.g:1073:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );
	public final JavaParser.unaryExpression_return unaryExpression() throws RecognitionException
	{
		JavaParser.unaryExpression_return retval = new JavaParser.unaryExpression_return();
		retval.start = input.LT(1);

		int unaryExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal459 = null;
		Token char_literal461 = null;
		Token string_literal463 = null;
		Token string_literal465 = null;
		JavaParser.unaryExpression_return unaryExpression460 = null;

		JavaParser.unaryExpression_return unaryExpression462 = null;

		JavaParser.unaryExpression_return unaryExpression464 = null;

		JavaParser.unaryExpression_return unaryExpression466 = null;

		JavaParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus467 = null;

		CommonTree char_literal459_tree = null;
		CommonTree char_literal461_tree = null;
		CommonTree string_literal463_tree = null;
		CommonTree string_literal465_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 86)) { return retval; }

			// Java.g:1074:5: ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus )
			int alt127 = 5;
			switch (input.LA(1))
			{
				case PLUS:
				{
					alt127 = 1;
				}
					break;
				case SUB:
				{
					alt127 = 2;
				}
					break;
				case PLUSPLUS:
				{
					alt127 = 3;
				}
					break;
				case SUBSUB:
				{
					alt127 = 4;
				}
					break;
				case BANG:
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case CHARLITERAL:
				case DOUBLE:
				case DOUBLELITERAL:
				case FALSE:
				case FLOAT:
				case FLOATLITERAL:
				case IDENTIFIER:
				case INT:
				case INTLITERAL:
				case LONG:
				case LONGLITERAL:
				case LPAREN:
				case NEW:
				case NULL:
				case SHORT:
				case STRINGLITERAL:
				case SUPER:
				case THIS:
				case TILDE:
				case TRUE:
				case VOID:
				{
					alt127 = 5;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 127, 0, input);

					throw nvae;

			}

			switch (alt127)
			{
				case 1:
				// Java.g:1074:9: '+' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal459 = (Token) match(input, PLUS, FOLLOW_PLUS_in_unaryExpression6360);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal459_tree = (CommonTree) adaptor.create(char_literal459);
						adaptor.addChild(root_0, char_literal459_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression6363);
					unaryExpression460 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression460.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1075:9: '-' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal461 = (Token) match(input, SUB, FOLLOW_SUB_in_unaryExpression6373);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal461_tree = (CommonTree) adaptor.create(char_literal461);
						adaptor.addChild(root_0, char_literal461_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression6375);
					unaryExpression462 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression462.getTree());
					}

				}
					break;
				case 3:
				// Java.g:1076:9: '++' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal463 = (Token) match(input, PLUSPLUS, FOLLOW_PLUSPLUS_in_unaryExpression6385);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal463_tree = (CommonTree) adaptor.create(string_literal463);
						adaptor.addChild(root_0, string_literal463_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression6387);
					unaryExpression464 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression464.getTree());
					}

				}
					break;
				case 4:
				// Java.g:1077:9: '--' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal465 = (Token) match(input, SUBSUB, FOLLOW_SUBSUB_in_unaryExpression6397);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal465_tree = (CommonTree) adaptor.create(string_literal465);
						adaptor.addChild(root_0, string_literal465_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpression6399);
					unaryExpression466 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression466.getTree());
					}

				}
					break;
				case 5:
				// Java.g:1078:9: unaryExpressionNotPlusMinus
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression6409);
					unaryExpressionNotPlusMinus467 = unaryExpressionNotPlusMinus();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpressionNotPlusMinus467.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 86, unaryExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "unaryExpression"

	public static class unaryExpressionNotPlusMinus_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "unaryExpressionNotPlusMinus"
	// Java.g:1081:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
	public final JavaParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus() throws RecognitionException
	{
		JavaParser.unaryExpressionNotPlusMinus_return retval = new JavaParser.unaryExpressionNotPlusMinus_return();
		retval.start = input.LT(1);

		int unaryExpressionNotPlusMinus_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal468 = null;
		Token char_literal470 = null;
		Token set475 = null;
		JavaParser.unaryExpression_return unaryExpression469 = null;

		JavaParser.unaryExpression_return unaryExpression471 = null;

		JavaParser.castExpression_return castExpression472 = null;

		JavaParser.primary_return primary473 = null;

		JavaParser.selector_return selector474 = null;

		CommonTree char_literal468_tree = null;
		CommonTree char_literal470_tree = null;
		CommonTree set475_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 87)) { return retval; }

			// Java.g:1082:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
			int alt130 = 4;
			switch (input.LA(1))
			{
				case TILDE:
				{
					alt130 = 1;
				}
					break;
				case BANG:
				{
					alt130 = 2;
				}
					break;
				case LPAREN:
				{
					int LA130_3 = input.LA(2);

					if ((synpred202_Java()))
					{
						alt130 = 3;
					}
					else if ((true))
					{
						alt130 = 4;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 130, 3, input);

						throw nvae;

					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case CHARLITERAL:
				case DOUBLE:
				case DOUBLELITERAL:
				case FALSE:
				case FLOAT:
				case FLOATLITERAL:
				case IDENTIFIER:
				case INT:
				case INTLITERAL:
				case LONG:
				case LONGLITERAL:
				case NEW:
				case NULL:
				case SHORT:
				case STRINGLITERAL:
				case SUPER:
				case THIS:
				case TRUE:
				case VOID:
				{
					alt130 = 4;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 130, 0, input);

					throw nvae;

			}

			switch (alt130)
			{
				case 1:
				// Java.g:1082:9: '~' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal468 = (Token) match(input, TILDE, FOLLOW_TILDE_in_unaryExpressionNotPlusMinus6429);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal468_tree = (CommonTree) adaptor.create(char_literal468);
						adaptor.addChild(root_0, char_literal468_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6431);
					unaryExpression469 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression469.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1083:9: '!' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal470 = (Token) match(input, BANG, FOLLOW_BANG_in_unaryExpressionNotPlusMinus6441);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal470_tree = (CommonTree) adaptor.create(char_literal470);
						adaptor.addChild(root_0, char_literal470_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6443);
					unaryExpression471 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression471.getTree());
					}

				}
					break;
				case 3:
				// Java.g:1084:9: castExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus6453);
					castExpression472 = castExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, castExpression472.getTree());
					}

				}
					break;
				case 4:
				// Java.g:1085:9: primary ( selector )* ( '++' | '--' )?
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus6463);
					primary473 = primary();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, primary473.getTree());
					}

					// Java.g:1086:9: ( selector )*
					loop128:
					do
					{
						int alt128 = 2;
						int LA128_0 = input.LA(1);

						if (((LA128_0 == DOT) || (LA128_0 == LBRACKET)))
						{
							alt128 = 1;
						}

						switch (alt128)
						{
							case 1:
							// Java.g:1086:10: selector
							{
								pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus6474);
								selector474 = selector();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, selector474.getTree());
								}

							}
								break;

							default:
								break loop128;
						}
					}
					while (true);

					// Java.g:1088:9: ( '++' | '--' )?
					int alt129 = 2;
					int LA129_0 = input.LA(1);

					if (((LA129_0 == PLUSPLUS) || (LA129_0 == SUBSUB)))
					{
						alt129 = 1;
					}
					switch (alt129)
					{
						case 1:
						// Java.g:
						{
							set475 = (Token) input.LT(1);

							if ((input.LA(1) == PLUSPLUS) || (input.LA(1) == SUBSUB))
							{
								input.consume();
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, (CommonTree) adaptor.create(set475));
								}
								state.errorRecovery = false;
								state.failed = false;
							}
							else
							{
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								MismatchedSetException mse = new MismatchedSetException(null, input);
								throw mse;
							}

						}
							break;

					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 87, unaryExpressionNotPlusMinus_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "unaryExpressionNotPlusMinus"

	public static class castExpression_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "castExpression"
	// Java.g:1093:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' type ')' unaryExpressionNotPlusMinus );
	public final JavaParser.castExpression_return castExpression() throws RecognitionException
	{
		JavaParser.castExpression_return retval = new JavaParser.castExpression_return();
		retval.start = input.LT(1);

		int castExpression_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal476 = null;
		Token char_literal478 = null;
		Token char_literal480 = null;
		Token char_literal482 = null;
		JavaParser.primitiveType_return primitiveType477 = null;

		JavaParser.unaryExpression_return unaryExpression479 = null;

		JavaParser.type_return type481 = null;

		JavaParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus483 = null;

		CommonTree char_literal476_tree = null;
		CommonTree char_literal478_tree = null;
		CommonTree char_literal480_tree = null;
		CommonTree char_literal482_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 88)) { return retval; }

			// Java.g:1094:5: ( '(' primitiveType ')' unaryExpression | '(' type ')' unaryExpressionNotPlusMinus )
			int alt131 = 2;
			int LA131_0 = input.LA(1);

			if ((LA131_0 == LPAREN))
			{
				int LA131_1 = input.LA(2);

				if ((synpred206_Java()))
				{
					alt131 = 1;
				}
				else if ((true))
				{
					alt131 = 2;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 131, 1, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 131, 0, input);

				throw nvae;

			}
			switch (alt131)
			{
				case 1:
				// Java.g:1094:9: '(' primitiveType ')' unaryExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal476 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_castExpression6544);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal476_tree = (CommonTree) adaptor.create(char_literal476);
						adaptor.addChild(root_0, char_literal476_tree);
					}

					pushFollow(FOLLOW_primitiveType_in_castExpression6546);
					primitiveType477 = primitiveType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, primitiveType477.getTree());
					}

					char_literal478 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_castExpression6548);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal478_tree = (CommonTree) adaptor.create(char_literal478);
						adaptor.addChild(root_0, char_literal478_tree);
					}

					pushFollow(FOLLOW_unaryExpression_in_castExpression6550);
					unaryExpression479 = unaryExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpression479.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1095:9: '(' type ')' unaryExpressionNotPlusMinus
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal480 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_castExpression6560);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal480_tree = (CommonTree) adaptor.create(char_literal480);
						adaptor.addChild(root_0, char_literal480_tree);
					}

					pushFollow(FOLLOW_type_in_castExpression6562);
					type481 = type();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, type481.getTree());
					}

					char_literal482 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_castExpression6564);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal482_tree = (CommonTree) adaptor.create(char_literal482);
						adaptor.addChild(root_0, char_literal482_tree);
					}

					pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression6566);
					unaryExpressionNotPlusMinus483 = unaryExpressionNotPlusMinus();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, unaryExpressionNotPlusMinus483.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 88, castExpression_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "castExpression"

	public static class primary_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "primary"
	// Java.g:1101:1: primary : ( parExpression | 'this' ( '.' IDENTIFIER )* ( identifierSuffix )? | IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )? | 'super' superSuffix | literal | creator | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
	public final JavaParser.primary_return primary() throws RecognitionException
	{
		JavaParser.primary_return retval = new JavaParser.primary_return();
		retval.start = input.LT(1);

		int primary_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal485 = null;
		Token char_literal486 = null;
		Token IDENTIFIER487 = null;
		Token IDENTIFIER489 = null;
		Token char_literal490 = null;
		Token IDENTIFIER491 = null;
		Token string_literal493 = null;
		Token char_literal498 = null;
		Token char_literal499 = null;
		Token char_literal500 = null;
		Token string_literal501 = null;
		Token string_literal502 = null;
		Token char_literal503 = null;
		Token string_literal504 = null;
		JavaParser.parExpression_return parExpression484 = null;

		JavaParser.identifierSuffix_return identifierSuffix488 = null;

		JavaParser.identifierSuffix_return identifierSuffix492 = null;

		JavaParser.superSuffix_return superSuffix494 = null;

		JavaParser.literal_return literal495 = null;

		JavaParser.creator_return creator496 = null;

		JavaParser.primitiveType_return primitiveType497 = null;

		CommonTree string_literal485_tree = null;
		CommonTree char_literal486_tree = null;
		CommonTree IDENTIFIER487_tree = null;
		CommonTree IDENTIFIER489_tree = null;
		CommonTree char_literal490_tree = null;
		CommonTree IDENTIFIER491_tree = null;
		CommonTree string_literal493_tree = null;
		CommonTree char_literal498_tree = null;
		CommonTree char_literal499_tree = null;
		CommonTree char_literal500_tree = null;
		CommonTree string_literal501_tree = null;
		CommonTree string_literal502_tree = null;
		CommonTree char_literal503_tree = null;
		CommonTree string_literal504_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 89)) { return retval; }

			// Java.g:1102:5: ( parExpression | 'this' ( '.' IDENTIFIER )* ( identifierSuffix )? | IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )? | 'super' superSuffix | literal | creator | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
			int alt137 = 8;
			switch (input.LA(1))
			{
				case LPAREN:
				{
					alt137 = 1;
				}
					break;
				case THIS:
				{
					alt137 = 2;
				}
					break;
				case IDENTIFIER:
				{
					alt137 = 3;
				}
					break;
				case SUPER:
				{
					alt137 = 4;
				}
					break;
				case CHARLITERAL:
				case DOUBLELITERAL:
				case FALSE:
				case FLOATLITERAL:
				case INTLITERAL:
				case LONGLITERAL:
				case NULL:
				case STRINGLITERAL:
				case TRUE:
				{
					alt137 = 5;
				}
					break;
				case NEW:
				{
					alt137 = 6;
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					alt137 = 7;
				}
					break;
				case VOID:
				{
					alt137 = 8;
				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 137, 0, input);

					throw nvae;

			}

			switch (alt137)
			{
				case 1:
				// Java.g:1102:9: parExpression
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_parExpression_in_primary6588);
					parExpression484 = parExpression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, parExpression484.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1103:9: 'this' ( '.' IDENTIFIER )* ( identifierSuffix )?
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal485 = (Token) match(input, THIS, FOLLOW_THIS_in_primary6610);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal485_tree = (CommonTree) adaptor.create(string_literal485);
						adaptor.addChild(root_0, string_literal485_tree);
					}

					// Java.g:1104:9: ( '.' IDENTIFIER )*
					loop132:
					do
					{
						int alt132 = 2;
						int LA132_0 = input.LA(1);

						if ((LA132_0 == DOT))
						{
							int LA132_2 = input.LA(2);

							if ((LA132_2 == IDENTIFIER))
							{
								int LA132_3 = input.LA(3);

								if ((synpred208_Java()))
								{
									alt132 = 1;
								}

							}

						}

						switch (alt132)
						{
							case 1:
							// Java.g:1104:10: '.' IDENTIFIER
							{
								char_literal486 = (Token) match(input, DOT, FOLLOW_DOT_in_primary6621);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal486_tree = (CommonTree) adaptor.create(char_literal486);
									adaptor.addChild(root_0, char_literal486_tree);
								}

								IDENTIFIER487 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_primary6623);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									IDENTIFIER487_tree = (CommonTree) adaptor.create(IDENTIFIER487);
									adaptor.addChild(root_0, IDENTIFIER487_tree);
								}

							}
								break;

							default:
								break loop132;
						}
					}
					while (true);

					// Java.g:1106:9: ( identifierSuffix )?
					int alt133 = 2;
					switch (input.LA(1))
					{
						case LBRACKET:
						{
							int LA133_1 = input.LA(2);

							if ((synpred209_Java()))
							{
								alt133 = 1;
							}
						}
							break;
						case LPAREN:
						{
							alt133 = 1;
						}
							break;
						case DOT:
						{
							int LA133_3 = input.LA(2);

							if ((synpred209_Java()))
							{
								alt133 = 1;
							}
						}
							break;
					}

					switch (alt133)
					{
						case 1:
						// Java.g:1106:10: identifierSuffix
						{
							pushFollow(FOLLOW_identifierSuffix_in_primary6645);
							identifierSuffix488 = identifierSuffix();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, identifierSuffix488.getTree());
							}

						}
							break;

					}

				}
					break;
				case 3:
				// Java.g:1108:9: IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )?
				{
					root_0 = (CommonTree) adaptor.nil();

					IDENTIFIER489 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_primary6666);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER489_tree = (CommonTree) adaptor.create(IDENTIFIER489);
						adaptor.addChild(root_0, IDENTIFIER489_tree);
					}

					// Java.g:1109:9: ( '.' IDENTIFIER )*
					loop134:
					do
					{
						int alt134 = 2;
						int LA134_0 = input.LA(1);

						if ((LA134_0 == DOT))
						{
							int LA134_2 = input.LA(2);

							if ((LA134_2 == IDENTIFIER))
							{
								int LA134_3 = input.LA(3);

								if ((synpred211_Java()))
								{
									alt134 = 1;
								}

							}

						}

						switch (alt134)
						{
							case 1:
							// Java.g:1109:10: '.' IDENTIFIER
							{
								char_literal490 = (Token) match(input, DOT, FOLLOW_DOT_in_primary6677);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal490_tree = (CommonTree) adaptor.create(char_literal490);
									adaptor.addChild(root_0, char_literal490_tree);
								}

								IDENTIFIER491 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_primary6679);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									IDENTIFIER491_tree = (CommonTree) adaptor.create(IDENTIFIER491);
									adaptor.addChild(root_0, IDENTIFIER491_tree);
								}

							}
								break;

							default:
								break loop134;
						}
					}
					while (true);

					// Java.g:1111:9: ( identifierSuffix )?
					int alt135 = 2;
					switch (input.LA(1))
					{
						case LBRACKET:
						{
							int LA135_1 = input.LA(2);

							if ((synpred212_Java()))
							{
								alt135 = 1;
							}
						}
							break;
						case LPAREN:
						{
							alt135 = 1;
						}
							break;
						case DOT:
						{
							int LA135_3 = input.LA(2);

							if ((synpred212_Java()))
							{
								alt135 = 1;
							}
						}
							break;
					}

					switch (alt135)
					{
						case 1:
						// Java.g:1111:10: identifierSuffix
						{
							pushFollow(FOLLOW_identifierSuffix_in_primary6701);
							identifierSuffix492 = identifierSuffix();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, identifierSuffix492.getTree());
							}

						}
							break;

					}

				}
					break;
				case 4:
				// Java.g:1113:9: 'super' superSuffix
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal493 = (Token) match(input, SUPER, FOLLOW_SUPER_in_primary6722);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal493_tree = (CommonTree) adaptor.create(string_literal493);
						adaptor.addChild(root_0, string_literal493_tree);
					}

					pushFollow(FOLLOW_superSuffix_in_primary6732);
					superSuffix494 = superSuffix();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, superSuffix494.getTree());
					}

				}
					break;
				case 5:
				// Java.g:1115:9: literal
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_literal_in_primary6742);
					literal495 = literal();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, literal495.getTree());
					}

				}
					break;
				case 6:
				// Java.g:1116:9: creator
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_creator_in_primary6752);
					creator496 = creator();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, creator496.getTree());
					}

				}
					break;
				case 7:
				// Java.g:1117:9: primitiveType ( '[' ']' )* '.' 'class'
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_primitiveType_in_primary6762);
					primitiveType497 = primitiveType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, primitiveType497.getTree());
					}

					// Java.g:1118:9: ( '[' ']' )*
					loop136:
					do
					{
						int alt136 = 2;
						int LA136_0 = input.LA(1);

						if ((LA136_0 == LBRACKET))
						{
							alt136 = 1;
						}

						switch (alt136)
						{
							case 1:
							// Java.g:1118:10: '[' ']'
							{
								char_literal498 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_primary6773);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal498_tree = (CommonTree) adaptor.create(char_literal498);
									adaptor.addChild(root_0, char_literal498_tree);
								}

								char_literal499 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_primary6775);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal499_tree = (CommonTree) adaptor.create(char_literal499);
									adaptor.addChild(root_0, char_literal499_tree);
								}

							}
								break;

							default:
								break loop136;
						}
					}
					while (true);

					char_literal500 = (Token) match(input, DOT, FOLLOW_DOT_in_primary6796);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal500_tree = (CommonTree) adaptor.create(char_literal500);
						adaptor.addChild(root_0, char_literal500_tree);
					}

					string_literal501 = (Token) match(input, CLASS, FOLLOW_CLASS_in_primary6798);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal501_tree = (CommonTree) adaptor.create(string_literal501);
						adaptor.addChild(root_0, string_literal501_tree);
					}

				}
					break;
				case 8:
				// Java.g:1121:9: 'void' '.' 'class'
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal502 = (Token) match(input, VOID, FOLLOW_VOID_in_primary6808);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal502_tree = (CommonTree) adaptor.create(string_literal502);
						adaptor.addChild(root_0, string_literal502_tree);
					}

					char_literal503 = (Token) match(input, DOT, FOLLOW_DOT_in_primary6810);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal503_tree = (CommonTree) adaptor.create(char_literal503);
						adaptor.addChild(root_0, char_literal503_tree);
					}

					string_literal504 = (Token) match(input, CLASS, FOLLOW_CLASS_in_primary6812);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal504_tree = (CommonTree) adaptor.create(string_literal504);
						adaptor.addChild(root_0, string_literal504_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 89, primary_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "primary"

	public static class superSuffix_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "superSuffix"
	// Java.g:1125:1: superSuffix : ( arguments | '.' ( typeArguments )? IDENTIFIER ( arguments )? );
	public final JavaParser.superSuffix_return superSuffix() throws RecognitionException
	{
		JavaParser.superSuffix_return retval = new JavaParser.superSuffix_return();
		retval.start = input.LT(1);

		int superSuffix_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal506 = null;
		Token IDENTIFIER508 = null;
		JavaParser.arguments_return arguments505 = null;

		JavaParser.typeArguments_return typeArguments507 = null;

		JavaParser.arguments_return arguments509 = null;

		CommonTree char_literal506_tree = null;
		CommonTree IDENTIFIER508_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 90)) { return retval; }

			// Java.g:1126:5: ( arguments | '.' ( typeArguments )? IDENTIFIER ( arguments )? )
			int alt140 = 2;
			int LA140_0 = input.LA(1);

			if ((LA140_0 == LPAREN))
			{
				alt140 = 1;
			}
			else if ((LA140_0 == DOT))
			{
				alt140 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 140, 0, input);

				throw nvae;

			}
			switch (alt140)
			{
				case 1:
				// Java.g:1126:9: arguments
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_arguments_in_superSuffix6838);
					arguments505 = arguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arguments505.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1127:9: '.' ( typeArguments )? IDENTIFIER ( arguments )?
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal506 = (Token) match(input, DOT, FOLLOW_DOT_in_superSuffix6848);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal506_tree = (CommonTree) adaptor.create(char_literal506);
						adaptor.addChild(root_0, char_literal506_tree);
					}

					// Java.g:1127:13: ( typeArguments )?
					int alt138 = 2;
					int LA138_0 = input.LA(1);

					if ((LA138_0 == LT))
					{
						alt138 = 1;
					}
					switch (alt138)
					{
						case 1:
						// Java.g:1127:14: typeArguments
						{
							pushFollow(FOLLOW_typeArguments_in_superSuffix6851);
							typeArguments507 = typeArguments();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, typeArguments507.getTree());
							}

						}
							break;

					}

					IDENTIFIER508 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_superSuffix6872);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER508_tree = (CommonTree) adaptor.create(IDENTIFIER508);
						adaptor.addChild(root_0, IDENTIFIER508_tree);
					}

					// Java.g:1130:9: ( arguments )?
					int alt139 = 2;
					int LA139_0 = input.LA(1);

					if ((LA139_0 == LPAREN))
					{
						alt139 = 1;
					}
					switch (alt139)
					{
						case 1:
						// Java.g:1130:10: arguments
						{
							pushFollow(FOLLOW_arguments_in_superSuffix6883);
							arguments509 = arguments();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, arguments509.getTree());
							}

						}
							break;

					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 90, superSuffix_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "superSuffix"

	public static class identifierSuffix_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "identifierSuffix"
	// Java.g:1135:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator );
	public final JavaParser.identifierSuffix_return identifierSuffix() throws RecognitionException
	{
		JavaParser.identifierSuffix_return retval = new JavaParser.identifierSuffix_return();
		retval.start = input.LT(1);

		int identifierSuffix_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal510 = null;
		Token char_literal511 = null;
		Token char_literal512 = null;
		Token string_literal513 = null;
		Token char_literal514 = null;
		Token char_literal516 = null;
		Token char_literal518 = null;
		Token string_literal519 = null;
		Token char_literal520 = null;
		Token IDENTIFIER522 = null;
		Token char_literal524 = null;
		Token string_literal525 = null;
		Token char_literal526 = null;
		Token string_literal527 = null;
		JavaParser.expression_return expression515 = null;

		JavaParser.arguments_return arguments517 = null;

		JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments521 = null;

		JavaParser.arguments_return arguments523 = null;

		JavaParser.arguments_return arguments528 = null;

		JavaParser.innerCreator_return innerCreator529 = null;

		CommonTree char_literal510_tree = null;
		CommonTree char_literal511_tree = null;
		CommonTree char_literal512_tree = null;
		CommonTree string_literal513_tree = null;
		CommonTree char_literal514_tree = null;
		CommonTree char_literal516_tree = null;
		CommonTree char_literal518_tree = null;
		CommonTree string_literal519_tree = null;
		CommonTree char_literal520_tree = null;
		CommonTree IDENTIFIER522_tree = null;
		CommonTree char_literal524_tree = null;
		CommonTree string_literal525_tree = null;
		CommonTree char_literal526_tree = null;
		CommonTree string_literal527_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 91)) { return retval; }

			// Java.g:1136:5: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator )
			int alt143 = 8;
			switch (input.LA(1))
			{
				case LBRACKET:
				{
					int LA143_1 = input.LA(2);

					if ((LA143_1 == RBRACKET))
					{
						alt143 = 1;
					}
					else if (((LA143_1 == BANG) || (LA143_1 == BOOLEAN) || (LA143_1 == BYTE) || ((LA143_1 >= CHAR) && (LA143_1 <= CHARLITERAL)) || ((LA143_1 >= DOUBLE) && (LA143_1 <= DOUBLELITERAL)) || (LA143_1 == FALSE) || ((LA143_1 >= FLOAT) && (LA143_1 <= FLOATLITERAL)) || (LA143_1 == IDENTIFIER) || (LA143_1 == INT) || (LA143_1 == INTLITERAL) || ((LA143_1 >= LONG) && (LA143_1 <= LPAREN)) || ((LA143_1 >= NEW) && (LA143_1 <= NULL)) || (LA143_1 == PLUS) || (LA143_1 == PLUSPLUS) || (LA143_1 == SHORT) || ((LA143_1 >= STRINGLITERAL) && (LA143_1 <= SUB)) || ((LA143_1 >= SUBSUB) && (LA143_1 <= SUPER)) || (LA143_1 == THIS) || (LA143_1 == TILDE) || (LA143_1 == TRUE) || (LA143_1 == VOID)))
					{
						alt143 = 2;
					}
					else
					{
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 143, 1, input);

						throw nvae;

					}
				}
					break;
				case LPAREN:
				{
					alt143 = 3;
				}
					break;
				case DOT:
				{
					switch (input.LA(2))
					{
						case CLASS:
						{
							alt143 = 4;
						}
							break;
						case THIS:
						{
							alt143 = 6;
						}
							break;
						case SUPER:
						{
							alt143 = 7;
						}
							break;
						case NEW:
						{
							alt143 = 8;
						}
							break;
						case LT:
						{
							alt143 = 5;
						}
							break;
						default:
							if (state.backtracking > 0)
							{
								state.failed = true;
								return retval;
							}
							NoViableAltException nvae = new NoViableAltException("", 143, 3, input);

							throw nvae;

					}

				}
					break;
				default:
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 143, 0, input);

					throw nvae;

			}

			switch (alt143)
			{
				case 1:
				// Java.g:1136:9: ( '[' ']' )+ '.' 'class'
				{
					root_0 = (CommonTree) adaptor.nil();

					// Java.g:1136:9: ( '[' ']' )+
					int cnt141 = 0;
					loop141:
					do
					{
						int alt141 = 2;
						int LA141_0 = input.LA(1);

						if ((LA141_0 == LBRACKET))
						{
							alt141 = 1;
						}

						switch (alt141)
						{
							case 1:
							// Java.g:1136:10: '[' ']'
							{
								char_literal510 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_identifierSuffix6916);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal510_tree = (CommonTree) adaptor.create(char_literal510);
									adaptor.addChild(root_0, char_literal510_tree);
								}

								char_literal511 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_identifierSuffix6918);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal511_tree = (CommonTree) adaptor.create(char_literal511);
									adaptor.addChild(root_0, char_literal511_tree);
								}

							}
								break;

							default:
								if (cnt141 >= 1)
								{
									break loop141;
								}
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								EarlyExitException eee = new EarlyExitException(141, input);
								throw eee;
						}
						cnt141++;
					}
					while (true);

					char_literal512 = (Token) match(input, DOT, FOLLOW_DOT_in_identifierSuffix6939);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal512_tree = (CommonTree) adaptor.create(char_literal512);
						adaptor.addChild(root_0, char_literal512_tree);
					}

					string_literal513 = (Token) match(input, CLASS, FOLLOW_CLASS_in_identifierSuffix6941);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal513_tree = (CommonTree) adaptor.create(string_literal513);
						adaptor.addChild(root_0, string_literal513_tree);
					}

				}
					break;
				case 2:
				// Java.g:1139:9: ( '[' expression ']' )+
				{
					root_0 = (CommonTree) adaptor.nil();

					// Java.g:1139:9: ( '[' expression ']' )+
					int cnt142 = 0;
					loop142:
					do
					{
						int alt142 = 2;
						int LA142_0 = input.LA(1);

						if ((LA142_0 == LBRACKET))
						{
							int LA142_2 = input.LA(2);

							if ((synpred224_Java()))
							{
								alt142 = 1;
							}

						}

						switch (alt142)
						{
							case 1:
							// Java.g:1139:10: '[' expression ']'
							{
								char_literal514 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_identifierSuffix6952);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal514_tree = (CommonTree) adaptor.create(char_literal514);
									adaptor.addChild(root_0, char_literal514_tree);
								}

								pushFollow(FOLLOW_expression_in_identifierSuffix6954);
								expression515 = expression();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, expression515.getTree());
								}

								char_literal516 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_identifierSuffix6956);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal516_tree = (CommonTree) adaptor.create(char_literal516);
									adaptor.addChild(root_0, char_literal516_tree);
								}

							}
								break;

							default:
								if (cnt142 >= 1)
								{
									break loop142;
								}
								if (state.backtracking > 0)
								{
									state.failed = true;
									return retval;
								}
								EarlyExitException eee = new EarlyExitException(142, input);
								throw eee;
						}
						cnt142++;
					}
					while (true);

				}
					break;
				case 3:
				// Java.g:1141:9: arguments
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_arguments_in_identifierSuffix6977);
					arguments517 = arguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arguments517.getTree());
					}

				}
					break;
				case 4:
				// Java.g:1142:9: '.' 'class'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal518 = (Token) match(input, DOT, FOLLOW_DOT_in_identifierSuffix6987);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal518_tree = (CommonTree) adaptor.create(char_literal518);
						adaptor.addChild(root_0, char_literal518_tree);
					}

					string_literal519 = (Token) match(input, CLASS, FOLLOW_CLASS_in_identifierSuffix6989);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal519_tree = (CommonTree) adaptor.create(string_literal519);
						adaptor.addChild(root_0, string_literal519_tree);
					}

				}
					break;
				case 5:
				// Java.g:1143:9: '.' nonWildcardTypeArguments IDENTIFIER arguments
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal520 = (Token) match(input, DOT, FOLLOW_DOT_in_identifierSuffix6999);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal520_tree = (CommonTree) adaptor.create(char_literal520);
						adaptor.addChild(root_0, char_literal520_tree);
					}

					pushFollow(FOLLOW_nonWildcardTypeArguments_in_identifierSuffix7001);
					nonWildcardTypeArguments521 = nonWildcardTypeArguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, nonWildcardTypeArguments521.getTree());
					}

					IDENTIFIER522 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_identifierSuffix7003);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER522_tree = (CommonTree) adaptor.create(IDENTIFIER522);
						adaptor.addChild(root_0, IDENTIFIER522_tree);
					}

					pushFollow(FOLLOW_arguments_in_identifierSuffix7005);
					arguments523 = arguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arguments523.getTree());
					}

				}
					break;
				case 6:
				// Java.g:1144:9: '.' 'this'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal524 = (Token) match(input, DOT, FOLLOW_DOT_in_identifierSuffix7015);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal524_tree = (CommonTree) adaptor.create(char_literal524);
						adaptor.addChild(root_0, char_literal524_tree);
					}

					string_literal525 = (Token) match(input, THIS, FOLLOW_THIS_in_identifierSuffix7017);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal525_tree = (CommonTree) adaptor.create(string_literal525);
						adaptor.addChild(root_0, string_literal525_tree);
					}

				}
					break;
				case 7:
				// Java.g:1145:9: '.' 'super' arguments
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal526 = (Token) match(input, DOT, FOLLOW_DOT_in_identifierSuffix7027);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal526_tree = (CommonTree) adaptor.create(char_literal526);
						adaptor.addChild(root_0, char_literal526_tree);
					}

					string_literal527 = (Token) match(input, SUPER, FOLLOW_SUPER_in_identifierSuffix7029);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal527_tree = (CommonTree) adaptor.create(string_literal527);
						adaptor.addChild(root_0, string_literal527_tree);
					}

					pushFollow(FOLLOW_arguments_in_identifierSuffix7031);
					arguments528 = arguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arguments528.getTree());
					}

				}
					break;
				case 8:
				// Java.g:1146:9: innerCreator
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_innerCreator_in_identifierSuffix7041);
					innerCreator529 = innerCreator();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, innerCreator529.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 91, identifierSuffix_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "identifierSuffix"

	public static class selector_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "selector"
	// Java.g:1150:1: selector : ( '.' IDENTIFIER ( arguments )? | '.' 'this' | '.' 'super' superSuffix | innerCreator | '[' expression ']' );
	public final JavaParser.selector_return selector() throws RecognitionException
	{
		JavaParser.selector_return retval = new JavaParser.selector_return();
		retval.start = input.LT(1);

		int selector_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal530 = null;
		Token IDENTIFIER531 = null;
		Token char_literal533 = null;
		Token string_literal534 = null;
		Token char_literal535 = null;
		Token string_literal536 = null;
		Token char_literal539 = null;
		Token char_literal541 = null;
		JavaParser.arguments_return arguments532 = null;

		JavaParser.superSuffix_return superSuffix537 = null;

		JavaParser.innerCreator_return innerCreator538 = null;

		JavaParser.expression_return expression540 = null;

		CommonTree char_literal530_tree = null;
		CommonTree IDENTIFIER531_tree = null;
		CommonTree char_literal533_tree = null;
		CommonTree string_literal534_tree = null;
		CommonTree char_literal535_tree = null;
		CommonTree string_literal536_tree = null;
		CommonTree char_literal539_tree = null;
		CommonTree char_literal541_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 92)) { return retval; }

			// Java.g:1151:5: ( '.' IDENTIFIER ( arguments )? | '.' 'this' | '.' 'super' superSuffix | innerCreator | '[' expression ']' )
			int alt145 = 5;
			int LA145_0 = input.LA(1);

			if ((LA145_0 == DOT))
			{
				switch (input.LA(2))
				{
					case IDENTIFIER:
					{
						alt145 = 1;
					}
						break;
					case THIS:
					{
						alt145 = 2;
					}
						break;
					case SUPER:
					{
						alt145 = 3;
					}
						break;
					case NEW:
					{
						alt145 = 4;
					}
						break;
					default:
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 145, 1, input);

						throw nvae;

				}

			}
			else if ((LA145_0 == LBRACKET))
			{
				alt145 = 5;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 145, 0, input);

				throw nvae;

			}
			switch (alt145)
			{
				case 1:
				// Java.g:1151:9: '.' IDENTIFIER ( arguments )?
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal530 = (Token) match(input, DOT, FOLLOW_DOT_in_selector7063);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal530_tree = (CommonTree) adaptor.create(char_literal530);
						adaptor.addChild(root_0, char_literal530_tree);
					}

					IDENTIFIER531 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_selector7065);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						IDENTIFIER531_tree = (CommonTree) adaptor.create(IDENTIFIER531);
						adaptor.addChild(root_0, IDENTIFIER531_tree);
					}

					// Java.g:1152:9: ( arguments )?
					int alt144 = 2;
					int LA144_0 = input.LA(1);

					if ((LA144_0 == LPAREN))
					{
						alt144 = 1;
					}
					switch (alt144)
					{
						case 1:
						// Java.g:1152:10: arguments
						{
							pushFollow(FOLLOW_arguments_in_selector7076);
							arguments532 = arguments();

							state._fsp--;
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								adaptor.addChild(root_0, arguments532.getTree());
							}

						}
							break;

					}

				}
					break;
				case 2:
				// Java.g:1154:9: '.' 'this'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal533 = (Token) match(input, DOT, FOLLOW_DOT_in_selector7097);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal533_tree = (CommonTree) adaptor.create(char_literal533);
						adaptor.addChild(root_0, char_literal533_tree);
					}

					string_literal534 = (Token) match(input, THIS, FOLLOW_THIS_in_selector7099);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal534_tree = (CommonTree) adaptor.create(string_literal534);
						adaptor.addChild(root_0, string_literal534_tree);
					}

				}
					break;
				case 3:
				// Java.g:1155:9: '.' 'super' superSuffix
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal535 = (Token) match(input, DOT, FOLLOW_DOT_in_selector7109);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal535_tree = (CommonTree) adaptor.create(char_literal535);
						adaptor.addChild(root_0, char_literal535_tree);
					}

					string_literal536 = (Token) match(input, SUPER, FOLLOW_SUPER_in_selector7111);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal536_tree = (CommonTree) adaptor.create(string_literal536);
						adaptor.addChild(root_0, string_literal536_tree);
					}

					pushFollow(FOLLOW_superSuffix_in_selector7121);
					superSuffix537 = superSuffix();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, superSuffix537.getTree());
					}

				}
					break;
				case 4:
				// Java.g:1157:9: innerCreator
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_innerCreator_in_selector7131);
					innerCreator538 = innerCreator();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, innerCreator538.getTree());
					}

				}
					break;
				case 5:
				// Java.g:1158:9: '[' expression ']'
				{
					root_0 = (CommonTree) adaptor.nil();

					char_literal539 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_selector7141);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal539_tree = (CommonTree) adaptor.create(char_literal539);
						adaptor.addChild(root_0, char_literal539_tree);
					}

					pushFollow(FOLLOW_expression_in_selector7143);
					expression540 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression540.getTree());
					}

					char_literal541 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_selector7145);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal541_tree = (CommonTree) adaptor.create(char_literal541);
						adaptor.addChild(root_0, char_literal541_tree);
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 92, selector_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "selector"

	public static class creator_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "creator"
	// Java.g:1161:1: creator : ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest | 'new' classOrInterfaceType classCreatorRest | arrayCreator );
	public final JavaParser.creator_return creator() throws RecognitionException
	{
		JavaParser.creator_return retval = new JavaParser.creator_return();
		retval.start = input.LT(1);

		int creator_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal542 = null;
		Token string_literal546 = null;
		JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments543 = null;

		JavaParser.classOrInterfaceType_return classOrInterfaceType544 = null;

		JavaParser.classCreatorRest_return classCreatorRest545 = null;

		JavaParser.classOrInterfaceType_return classOrInterfaceType547 = null;

		JavaParser.classCreatorRest_return classCreatorRest548 = null;

		JavaParser.arrayCreator_return arrayCreator549 = null;

		CommonTree string_literal542_tree = null;
		CommonTree string_literal546_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 93)) { return retval; }

			// Java.g:1162:5: ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest | 'new' classOrInterfaceType classCreatorRest | arrayCreator )
			int alt146 = 3;
			int LA146_0 = input.LA(1);

			if ((LA146_0 == NEW))
			{
				int LA146_1 = input.LA(2);

				if ((synpred236_Java()))
				{
					alt146 = 1;
				}
				else if ((synpred237_Java()))
				{
					alt146 = 2;
				}
				else if ((true))
				{
					alt146 = 3;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 146, 1, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 146, 0, input);

				throw nvae;

			}
			switch (alt146)
			{
				case 1:
				// Java.g:1162:9: 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal542 = (Token) match(input, NEW, FOLLOW_NEW_in_creator7165);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal542_tree = (CommonTree) adaptor.create(string_literal542);
						adaptor.addChild(root_0, string_literal542_tree);
					}

					pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator7167);
					nonWildcardTypeArguments543 = nonWildcardTypeArguments();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, nonWildcardTypeArguments543.getTree());
					}

					pushFollow(FOLLOW_classOrInterfaceType_in_creator7169);
					classOrInterfaceType544 = classOrInterfaceType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classOrInterfaceType544.getTree());
					}

					pushFollow(FOLLOW_classCreatorRest_in_creator7171);
					classCreatorRest545 = classCreatorRest();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classCreatorRest545.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1163:9: 'new' classOrInterfaceType classCreatorRest
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal546 = (Token) match(input, NEW, FOLLOW_NEW_in_creator7181);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal546_tree = (CommonTree) adaptor.create(string_literal546);
						adaptor.addChild(root_0, string_literal546_tree);
					}

					pushFollow(FOLLOW_classOrInterfaceType_in_creator7183);
					classOrInterfaceType547 = classOrInterfaceType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classOrInterfaceType547.getTree());
					}

					pushFollow(FOLLOW_classCreatorRest_in_creator7185);
					classCreatorRest548 = classCreatorRest();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classCreatorRest548.getTree());
					}

				}
					break;
				case 3:
				// Java.g:1164:9: arrayCreator
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_arrayCreator_in_creator7195);
					arrayCreator549 = arrayCreator();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arrayCreator549.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 93, creator_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "creator"

	public static class arrayCreator_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "arrayCreator"
	// Java.g:1167:1: arrayCreator : ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer | 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )* );
	public final JavaParser.arrayCreator_return arrayCreator() throws RecognitionException
	{
		JavaParser.arrayCreator_return retval = new JavaParser.arrayCreator_return();
		retval.start = input.LT(1);

		int arrayCreator_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal550 = null;
		Token char_literal552 = null;
		Token char_literal553 = null;
		Token char_literal554 = null;
		Token char_literal555 = null;
		Token string_literal557 = null;
		Token char_literal559 = null;
		Token char_literal561 = null;
		Token char_literal562 = null;
		Token char_literal564 = null;
		Token char_literal565 = null;
		Token char_literal566 = null;
		JavaParser.createdName_return createdName551 = null;

		JavaParser.arrayInitializer_return arrayInitializer556 = null;

		JavaParser.createdName_return createdName558 = null;

		JavaParser.expression_return expression560 = null;

		JavaParser.expression_return expression563 = null;

		CommonTree string_literal550_tree = null;
		CommonTree char_literal552_tree = null;
		CommonTree char_literal553_tree = null;
		CommonTree char_literal554_tree = null;
		CommonTree char_literal555_tree = null;
		CommonTree string_literal557_tree = null;
		CommonTree char_literal559_tree = null;
		CommonTree char_literal561_tree = null;
		CommonTree char_literal562_tree = null;
		CommonTree char_literal564_tree = null;
		CommonTree char_literal565_tree = null;
		CommonTree char_literal566_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 94)) { return retval; }

			// Java.g:1168:5: ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer | 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )* )
			int alt150 = 2;
			int LA150_0 = input.LA(1);

			if ((LA150_0 == NEW))
			{
				int LA150_1 = input.LA(2);

				if ((synpred239_Java()))
				{
					alt150 = 1;
				}
				else if ((true))
				{
					alt150 = 2;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					NoViableAltException nvae = new NoViableAltException("", 150, 1, input);

					throw nvae;

				}
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 150, 0, input);

				throw nvae;

			}
			switch (alt150)
			{
				case 1:
				// Java.g:1168:9: 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal550 = (Token) match(input, NEW, FOLLOW_NEW_in_arrayCreator7215);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal550_tree = (CommonTree) adaptor.create(string_literal550);
						adaptor.addChild(root_0, string_literal550_tree);
					}

					pushFollow(FOLLOW_createdName_in_arrayCreator7217);
					createdName551 = createdName();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, createdName551.getTree());
					}

					char_literal552 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_arrayCreator7227);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal552_tree = (CommonTree) adaptor.create(char_literal552);
						adaptor.addChild(root_0, char_literal552_tree);
					}

					char_literal553 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_arrayCreator7229);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal553_tree = (CommonTree) adaptor.create(char_literal553);
						adaptor.addChild(root_0, char_literal553_tree);
					}

					// Java.g:1170:9: ( '[' ']' )*
					loop147:
					do
					{
						int alt147 = 2;
						int LA147_0 = input.LA(1);

						if ((LA147_0 == LBRACKET))
						{
							alt147 = 1;
						}

						switch (alt147)
						{
							case 1:
							// Java.g:1170:10: '[' ']'
							{
								char_literal554 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_arrayCreator7240);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal554_tree = (CommonTree) adaptor.create(char_literal554);
									adaptor.addChild(root_0, char_literal554_tree);
								}

								char_literal555 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_arrayCreator7242);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal555_tree = (CommonTree) adaptor.create(char_literal555);
									adaptor.addChild(root_0, char_literal555_tree);
								}

							}
								break;

							default:
								break loop147;
						}
					}
					while (true);

					pushFollow(FOLLOW_arrayInitializer_in_arrayCreator7263);
					arrayInitializer556 = arrayInitializer();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arrayInitializer556.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1174:9: 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )*
				{
					root_0 = (CommonTree) adaptor.nil();

					string_literal557 = (Token) match(input, NEW, FOLLOW_NEW_in_arrayCreator7274);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						string_literal557_tree = (CommonTree) adaptor.create(string_literal557);
						adaptor.addChild(root_0, string_literal557_tree);
					}

					pushFollow(FOLLOW_createdName_in_arrayCreator7276);
					createdName558 = createdName();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, createdName558.getTree());
					}

					char_literal559 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_arrayCreator7286);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal559_tree = (CommonTree) adaptor.create(char_literal559);
						adaptor.addChild(root_0, char_literal559_tree);
					}

					pushFollow(FOLLOW_expression_in_arrayCreator7288);
					expression560 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression560.getTree());
					}

					char_literal561 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_arrayCreator7298);
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						char_literal561_tree = (CommonTree) adaptor.create(char_literal561);
						adaptor.addChild(root_0, char_literal561_tree);
					}

					// Java.g:1177:9: ( '[' expression ']' )*
					loop148:
					do
					{
						int alt148 = 2;
						int LA148_0 = input.LA(1);

						if ((LA148_0 == LBRACKET))
						{
							int LA148_1 = input.LA(2);

							if ((synpred240_Java()))
							{
								alt148 = 1;
							}

						}

						switch (alt148)
						{
							case 1:
							// Java.g:1177:13: '[' expression ']'
							{
								char_literal562 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_arrayCreator7312);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal562_tree = (CommonTree) adaptor.create(char_literal562);
									adaptor.addChild(root_0, char_literal562_tree);
								}

								pushFollow(FOLLOW_expression_in_arrayCreator7314);
								expression563 = expression();

								state._fsp--;
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									adaptor.addChild(root_0, expression563.getTree());
								}

								char_literal564 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_arrayCreator7328);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal564_tree = (CommonTree) adaptor.create(char_literal564);
									adaptor.addChild(root_0, char_literal564_tree);
								}

							}
								break;

							default:
								break loop148;
						}
					}
					while (true);

					// Java.g:1180:9: ( '[' ']' )*
					loop149:
					do
					{
						int alt149 = 2;
						int LA149_0 = input.LA(1);

						if ((LA149_0 == LBRACKET))
						{
							int LA149_2 = input.LA(2);

							if ((LA149_2 == RBRACKET))
							{
								alt149 = 1;
							}

						}

						switch (alt149)
						{
							case 1:
							// Java.g:1180:10: '[' ']'
							{
								char_literal565 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_arrayCreator7350);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal565_tree = (CommonTree) adaptor.create(char_literal565);
									adaptor.addChild(root_0, char_literal565_tree);
								}

								char_literal566 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_arrayCreator7352);
								if (state.failed) { return retval; }
								if (state.backtracking == 0)
								{
									char_literal566_tree = (CommonTree) adaptor.create(char_literal566);
									adaptor.addChild(root_0, char_literal566_tree);
								}

							}
								break;

							default:
								break loop149;
						}
					}
					while (true);

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 94, arrayCreator_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "arrayCreator"

	public static class variableInitializer_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "variableInitializer"
	// Java.g:1184:1: variableInitializer : ( arrayInitializer | expression );
	public final JavaParser.variableInitializer_return variableInitializer() throws RecognitionException
	{
		JavaParser.variableInitializer_return retval = new JavaParser.variableInitializer_return();
		retval.start = input.LT(1);

		int variableInitializer_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.arrayInitializer_return arrayInitializer567 = null;

		JavaParser.expression_return expression568 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 95)) { return retval; }

			// Java.g:1185:5: ( arrayInitializer | expression )
			int alt151 = 2;
			int LA151_0 = input.LA(1);

			if ((LA151_0 == LBRACE))
			{
				alt151 = 1;
			}
			else if (((LA151_0 == BANG) || (LA151_0 == BOOLEAN) || (LA151_0 == BYTE) || ((LA151_0 >= CHAR) && (LA151_0 <= CHARLITERAL)) || ((LA151_0 >= DOUBLE) && (LA151_0 <= DOUBLELITERAL)) || (LA151_0 == FALSE) || ((LA151_0 >= FLOAT) && (LA151_0 <= FLOATLITERAL)) || (LA151_0 == IDENTIFIER) || (LA151_0 == INT) || (LA151_0 == INTLITERAL) || ((LA151_0 >= LONG) && (LA151_0 <= LPAREN)) || ((LA151_0 >= NEW) && (LA151_0 <= NULL)) || (LA151_0 == PLUS) || (LA151_0 == PLUSPLUS) || (LA151_0 == SHORT) || ((LA151_0 >= STRINGLITERAL) && (LA151_0 <= SUB)) || ((LA151_0 >= SUBSUB) && (LA151_0 <= SUPER)) || (LA151_0 == THIS) || (LA151_0 == TILDE) || (LA151_0 == TRUE) || (LA151_0 == VOID)))
			{
				alt151 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 151, 0, input);

				throw nvae;

			}
			switch (alt151)
			{
				case 1:
				// Java.g:1185:9: arrayInitializer
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_arrayInitializer_in_variableInitializer7383);
					arrayInitializer567 = arrayInitializer();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, arrayInitializer567.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1186:9: expression
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_expression_in_variableInitializer7393);
					expression568 = expression();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, expression568.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 95, variableInitializer_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "variableInitializer"

	public static class arrayInitializer_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "arrayInitializer"
	// Java.g:1189:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}' ;
	public final JavaParser.arrayInitializer_return arrayInitializer() throws RecognitionException
	{
		JavaParser.arrayInitializer_return retval = new JavaParser.arrayInitializer_return();
		retval.start = input.LT(1);

		int arrayInitializer_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal569 = null;
		Token char_literal571 = null;
		Token char_literal573 = null;
		Token char_literal574 = null;
		JavaParser.variableInitializer_return variableInitializer570 = null;

		JavaParser.variableInitializer_return variableInitializer572 = null;

		CommonTree char_literal569_tree = null;
		CommonTree char_literal571_tree = null;
		CommonTree char_literal573_tree = null;
		CommonTree char_literal574_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 96)) { return retval; }

			// Java.g:1190:5: ( '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}' )
			// Java.g:1190:9: '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal569 = (Token) match(input, LBRACE, FOLLOW_LBRACE_in_arrayInitializer7413);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal569_tree = (CommonTree) adaptor.create(char_literal569);
					adaptor.addChild(root_0, char_literal569_tree);
				}

				// Java.g:1191:13: ( variableInitializer ( ',' variableInitializer )* )?
				int alt153 = 2;
				int LA153_0 = input.LA(1);

				if (((LA153_0 == BANG) || (LA153_0 == BOOLEAN) || (LA153_0 == BYTE) || ((LA153_0 >= CHAR) && (LA153_0 <= CHARLITERAL)) || ((LA153_0 >= DOUBLE) && (LA153_0 <= DOUBLELITERAL)) || (LA153_0 == FALSE) || ((LA153_0 >= FLOAT) && (LA153_0 <= FLOATLITERAL)) || (LA153_0 == IDENTIFIER) || (LA153_0 == INT) || (LA153_0 == INTLITERAL) || (LA153_0 == LBRACE) || ((LA153_0 >= LONG) && (LA153_0 <= LPAREN)) || ((LA153_0 >= NEW) && (LA153_0 <= NULL)) || (LA153_0 == PLUS) || (LA153_0 == PLUSPLUS) || (LA153_0 == SHORT) || ((LA153_0 >= STRINGLITERAL) && (LA153_0 <= SUB)) || ((LA153_0 >= SUBSUB) && (LA153_0 <= SUPER)) || (LA153_0 == THIS) || (LA153_0 == TILDE) || (LA153_0 == TRUE) || (LA153_0 == VOID)))
				{
					alt153 = 1;
				}
				switch (alt153)
				{
					case 1:
					// Java.g:1191:14: variableInitializer ( ',' variableInitializer )*
					{
						pushFollow(FOLLOW_variableInitializer_in_arrayInitializer7429);
						variableInitializer570 = variableInitializer();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, variableInitializer570.getTree());
						}

						// Java.g:1192:17: ( ',' variableInitializer )*
						loop152:
						do
						{
							int alt152 = 2;
							int LA152_0 = input.LA(1);

							if ((LA152_0 == COMMA))
							{
								int LA152_1 = input.LA(2);

								if (((LA152_1 == BANG) || (LA152_1 == BOOLEAN) || (LA152_1 == BYTE) || ((LA152_1 >= CHAR) && (LA152_1 <= CHARLITERAL)) || ((LA152_1 >= DOUBLE) && (LA152_1 <= DOUBLELITERAL)) || (LA152_1 == FALSE) || ((LA152_1 >= FLOAT) && (LA152_1 <= FLOATLITERAL)) || (LA152_1 == IDENTIFIER) || (LA152_1 == INT) || (LA152_1 == INTLITERAL) || (LA152_1 == LBRACE) || ((LA152_1 >= LONG) && (LA152_1 <= LPAREN)) || ((LA152_1 >= NEW) && (LA152_1 <= NULL)) || (LA152_1 == PLUS) || (LA152_1 == PLUSPLUS) || (LA152_1 == SHORT) || ((LA152_1 >= STRINGLITERAL) && (LA152_1 <= SUB)) || ((LA152_1 >= SUBSUB) && (LA152_1 <= SUPER)) || (LA152_1 == THIS) || (LA152_1 == TILDE) || (LA152_1 == TRUE) || (LA152_1 == VOID)))
								{
									alt152 = 1;
								}

							}

							switch (alt152)
							{
								case 1:
								// Java.g:1192:18: ',' variableInitializer
								{
									char_literal571 = (Token) match(input, COMMA, FOLLOW_COMMA_in_arrayInitializer7448);
									if (state.failed) { return retval; }
									if (state.backtracking == 0)
									{
										char_literal571_tree = (CommonTree) adaptor.create(char_literal571);
										adaptor.addChild(root_0, char_literal571_tree);
									}

									pushFollow(FOLLOW_variableInitializer_in_arrayInitializer7450);
									variableInitializer572 = variableInitializer();

									state._fsp--;
									if (state.failed) { return retval; }
									if (state.backtracking == 0)
									{
										adaptor.addChild(root_0, variableInitializer572.getTree());
									}

								}
									break;

								default:
									break loop152;
							}
						}
						while (true);

					}
						break;

				}

				// Java.g:1195:13: ( ',' )?
				int alt154 = 2;
				int LA154_0 = input.LA(1);

				if ((LA154_0 == COMMA))
				{
					alt154 = 1;
				}
				switch (alt154)
				{
					case 1:
					// Java.g:1195:14: ','
					{
						char_literal573 = (Token) match(input, COMMA, FOLLOW_COMMA_in_arrayInitializer7500);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							char_literal573_tree = (CommonTree) adaptor.create(char_literal573);
							adaptor.addChild(root_0, char_literal573_tree);
						}

					}
						break;

				}

				char_literal574 = (Token) match(input, RBRACE, FOLLOW_RBRACE_in_arrayInitializer7513);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal574_tree = (CommonTree) adaptor.create(char_literal574);
					adaptor.addChild(root_0, char_literal574_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 96, arrayInitializer_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "arrayInitializer"

	public static class createdName_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "createdName"
	// Java.g:1200:1: createdName : ( classOrInterfaceType | primitiveType );
	public final JavaParser.createdName_return createdName() throws RecognitionException
	{
		JavaParser.createdName_return retval = new JavaParser.createdName_return();
		retval.start = input.LT(1);

		int createdName_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.classOrInterfaceType_return classOrInterfaceType575 = null;

		JavaParser.primitiveType_return primitiveType576 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 97)) { return retval; }

			// Java.g:1201:5: ( classOrInterfaceType | primitiveType )
			int alt155 = 2;
			int LA155_0 = input.LA(1);

			if ((LA155_0 == IDENTIFIER))
			{
				alt155 = 1;
			}
			else if (((LA155_0 == BOOLEAN) || (LA155_0 == BYTE) || (LA155_0 == CHAR) || (LA155_0 == DOUBLE) || (LA155_0 == FLOAT) || (LA155_0 == INT) || (LA155_0 == LONG) || (LA155_0 == SHORT)))
			{
				alt155 = 2;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return retval;
				}
				NoViableAltException nvae = new NoViableAltException("", 155, 0, input);

				throw nvae;

			}
			switch (alt155)
			{
				case 1:
				// Java.g:1201:9: classOrInterfaceType
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_classOrInterfaceType_in_createdName7547);
					classOrInterfaceType575 = classOrInterfaceType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, classOrInterfaceType575.getTree());
					}

				}
					break;
				case 2:
				// Java.g:1202:9: primitiveType
				{
					root_0 = (CommonTree) adaptor.nil();

					pushFollow(FOLLOW_primitiveType_in_createdName7557);
					primitiveType576 = primitiveType();

					state._fsp--;
					if (state.failed) { return retval; }
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, primitiveType576.getTree());
					}

				}
					break;

			}
			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 97, createdName_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "createdName"

	public static class innerCreator_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "innerCreator"
	// Java.g:1205:1: innerCreator : '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest ;
	public final JavaParser.innerCreator_return innerCreator() throws RecognitionException
	{
		JavaParser.innerCreator_return retval = new JavaParser.innerCreator_return();
		retval.start = input.LT(1);

		int innerCreator_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal577 = null;
		Token string_literal578 = null;
		Token IDENTIFIER580 = null;
		JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments579 = null;

		JavaParser.typeArguments_return typeArguments581 = null;

		JavaParser.classCreatorRest_return classCreatorRest582 = null;

		CommonTree char_literal577_tree = null;
		CommonTree string_literal578_tree = null;
		CommonTree IDENTIFIER580_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 98)) { return retval; }

			// Java.g:1206:5: ( '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest )
			// Java.g:1206:9: '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal577 = (Token) match(input, DOT, FOLLOW_DOT_in_innerCreator7578);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal577_tree = (CommonTree) adaptor.create(char_literal577);
					adaptor.addChild(root_0, char_literal577_tree);
				}

				string_literal578 = (Token) match(input, NEW, FOLLOW_NEW_in_innerCreator7580);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal578_tree = (CommonTree) adaptor.create(string_literal578);
					adaptor.addChild(root_0, string_literal578_tree);
				}

				// Java.g:1207:9: ( nonWildcardTypeArguments )?
				int alt156 = 2;
				int LA156_0 = input.LA(1);

				if ((LA156_0 == LT))
				{
					alt156 = 1;
				}
				switch (alt156)
				{
					case 1:
					// Java.g:1207:10: nonWildcardTypeArguments
					{
						pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator7591);
						nonWildcardTypeArguments579 = nonWildcardTypeArguments();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, nonWildcardTypeArguments579.getTree());
						}

					}
						break;

				}

				IDENTIFIER580 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_innerCreator7612);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER580_tree = (CommonTree) adaptor.create(IDENTIFIER580);
					adaptor.addChild(root_0, IDENTIFIER580_tree);
				}

				// Java.g:1210:9: ( typeArguments )?
				int alt157 = 2;
				int LA157_0 = input.LA(1);

				if ((LA157_0 == LT))
				{
					alt157 = 1;
				}
				switch (alt157)
				{
					case 1:
					// Java.g:1210:10: typeArguments
					{
						pushFollow(FOLLOW_typeArguments_in_innerCreator7623);
						typeArguments581 = typeArguments();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeArguments581.getTree());
						}

					}
						break;

				}

				pushFollow(FOLLOW_classCreatorRest_in_innerCreator7644);
				classCreatorRest582 = classCreatorRest();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, classCreatorRest582.getTree());
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 98, innerCreator_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "innerCreator"

	public static class classCreatorRest_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classCreatorRest"
	// Java.g:1216:1: classCreatorRest : arguments ( classBody )? ;
	public final JavaParser.classCreatorRest_return classCreatorRest() throws RecognitionException
	{
		JavaParser.classCreatorRest_return retval = new JavaParser.classCreatorRest_return();
		retval.start = input.LT(1);

		int classCreatorRest_StartIndex = input.index();

		CommonTree root_0 = null;

		JavaParser.arguments_return arguments583 = null;

		JavaParser.classBody_return classBody584 = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 99)) { return retval; }

			// Java.g:1217:5: ( arguments ( classBody )? )
			// Java.g:1217:9: arguments ( classBody )?
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_arguments_in_classCreatorRest7665);
				arguments583 = arguments();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, arguments583.getTree());
				}

				// Java.g:1218:9: ( classBody )?
				int alt158 = 2;
				int LA158_0 = input.LA(1);

				if ((LA158_0 == LBRACE))
				{
					alt158 = 1;
				}
				switch (alt158)
				{
					case 1:
					// Java.g:1218:10: classBody
					{
						pushFollow(FOLLOW_classBody_in_classCreatorRest7676);
						classBody584 = classBody();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, classBody584.getTree());
						}

					}
						break;

				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 99, classCreatorRest_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classCreatorRest"

	public static class nonWildcardTypeArguments_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "nonWildcardTypeArguments"
	// Java.g:1223:1: nonWildcardTypeArguments : '<' typeList '>' ;
	public final JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments() throws RecognitionException
	{
		JavaParser.nonWildcardTypeArguments_return retval = new JavaParser.nonWildcardTypeArguments_return();
		retval.start = input.LT(1);

		int nonWildcardTypeArguments_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal585 = null;
		Token char_literal587 = null;
		JavaParser.typeList_return typeList586 = null;

		CommonTree char_literal585_tree = null;
		CommonTree char_literal587_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 100)) { return retval; }

			// Java.g:1224:5: ( '<' typeList '>' )
			// Java.g:1224:9: '<' typeList '>'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal585 = (Token) match(input, LT, FOLLOW_LT_in_nonWildcardTypeArguments7708);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal585_tree = (CommonTree) adaptor.create(char_literal585);
					adaptor.addChild(root_0, char_literal585_tree);
				}

				pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments7710);
				typeList586 = typeList();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, typeList586.getTree());
				}

				char_literal587 = (Token) match(input, GT, FOLLOW_GT_in_nonWildcardTypeArguments7720);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal587_tree = (CommonTree) adaptor.create(char_literal587);
					adaptor.addChild(root_0, char_literal587_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 100, nonWildcardTypeArguments_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "nonWildcardTypeArguments"

	public static class arguments_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "arguments"
	// Java.g:1228:1: arguments : '(' ( expressionList )? ')' ;
	public final JavaParser.arguments_return arguments() throws RecognitionException
	{
		JavaParser.arguments_return retval = new JavaParser.arguments_return();
		retval.start = input.LT(1);

		int arguments_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal588 = null;
		Token char_literal590 = null;
		JavaParser.expressionList_return expressionList589 = null;

		CommonTree char_literal588_tree = null;
		CommonTree char_literal590_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 101)) { return retval; }

			// Java.g:1229:5: ( '(' ( expressionList )? ')' )
			// Java.g:1229:9: '(' ( expressionList )? ')'
			{
				root_0 = (CommonTree) adaptor.nil();

				char_literal588 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_arguments7740);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal588_tree = (CommonTree) adaptor.create(char_literal588);
					adaptor.addChild(root_0, char_literal588_tree);
				}

				// Java.g:1229:13: ( expressionList )?
				int alt159 = 2;
				int LA159_0 = input.LA(1);

				if (((LA159_0 == BANG) || (LA159_0 == BOOLEAN) || (LA159_0 == BYTE) || ((LA159_0 >= CHAR) && (LA159_0 <= CHARLITERAL)) || ((LA159_0 >= DOUBLE) && (LA159_0 <= DOUBLELITERAL)) || (LA159_0 == FALSE) || ((LA159_0 >= FLOAT) && (LA159_0 <= FLOATLITERAL)) || (LA159_0 == IDENTIFIER) || (LA159_0 == INT) || (LA159_0 == INTLITERAL) || ((LA159_0 >= LONG) && (LA159_0 <= LPAREN)) || ((LA159_0 >= NEW) && (LA159_0 <= NULL)) || (LA159_0 == PLUS) || (LA159_0 == PLUSPLUS) || (LA159_0 == SHORT) || ((LA159_0 >= STRINGLITERAL) && (LA159_0 <= SUB)) || ((LA159_0 >= SUBSUB) && (LA159_0 <= SUPER)) || (LA159_0 == THIS) || (LA159_0 == TILDE) || (LA159_0 == TRUE) || (LA159_0 == VOID)))
				{
					alt159 = 1;
				}
				switch (alt159)
				{
					case 1:
					// Java.g:1229:14: expressionList
					{
						pushFollow(FOLLOW_expressionList_in_arguments7743);
						expressionList589 = expressionList();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, expressionList589.getTree());
						}

					}
						break;

				}

				char_literal590 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_arguments7756);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal590_tree = (CommonTree) adaptor.create(char_literal590);
					adaptor.addChild(root_0, char_literal590_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 101, arguments_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "arguments"

	public static class literal_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "literal"
	// Java.g:1233:1: literal : ( INTLITERAL | LONGLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL );
	public final JavaParser.literal_return literal() throws RecognitionException
	{
		JavaParser.literal_return retval = new JavaParser.literal_return();
		retval.start = input.LT(1);

		int literal_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set591 = null;

		CommonTree set591_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 102)) { return retval; }

			// Java.g:1234:5: ( INTLITERAL | LONGLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL )
			// Java.g:
			{
				root_0 = (CommonTree) adaptor.nil();

				set591 = (Token) input.LT(1);

				if ((input.LA(1) == CHARLITERAL) || (input.LA(1) == DOUBLELITERAL) || (input.LA(1) == FALSE) || (input.LA(1) == FLOATLITERAL) || (input.LA(1) == INTLITERAL) || (input.LA(1) == LONGLITERAL) || (input.LA(1) == NULL) || (input.LA(1) == STRINGLITERAL) || (input.LA(1) == TRUE))
				{
					input.consume();
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, (CommonTree) adaptor.create(set591));
					}
					state.errorRecovery = false;
					state.failed = false;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					MismatchedSetException mse = new MismatchedSetException(null, input);
					throw mse;
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 102, literal_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "literal"

	public static class classHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "classHeader"
	// Java.g:1249:1: classHeader : modifiers 'class' IDENTIFIER ;
	public final JavaParser.classHeader_return classHeader() throws RecognitionException
	{
		JavaParser.classHeader_return retval = new JavaParser.classHeader_return();
		retval.start = input.LT(1);

		int classHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal593 = null;
		Token IDENTIFIER594 = null;
		JavaParser.modifiers_return modifiers592 = null;

		CommonTree string_literal593_tree = null;
		CommonTree IDENTIFIER594_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 103)) { return retval; }

			// Java.g:1250:5: ( modifiers 'class' IDENTIFIER )
			// Java.g:1250:9: modifiers 'class' IDENTIFIER
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_classHeader7880);
				modifiers592 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers592.getTree());
				}

				string_literal593 = (Token) match(input, CLASS, FOLLOW_CLASS_in_classHeader7882);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal593_tree = (CommonTree) adaptor.create(string_literal593);
					adaptor.addChild(root_0, string_literal593_tree);
				}

				IDENTIFIER594 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_classHeader7884);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER594_tree = (CommonTree) adaptor.create(IDENTIFIER594);
					adaptor.addChild(root_0, IDENTIFIER594_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 103, classHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "classHeader"

	public static class enumHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "enumHeader"
	// Java.g:1253:1: enumHeader : modifiers ( 'enum' | IDENTIFIER ) IDENTIFIER ;
	public final JavaParser.enumHeader_return enumHeader() throws RecognitionException
	{
		JavaParser.enumHeader_return retval = new JavaParser.enumHeader_return();
		retval.start = input.LT(1);

		int enumHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token set596 = null;
		Token IDENTIFIER597 = null;
		JavaParser.modifiers_return modifiers595 = null;

		CommonTree set596_tree = null;
		CommonTree IDENTIFIER597_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 104)) { return retval; }

			// Java.g:1254:5: ( modifiers ( 'enum' | IDENTIFIER ) IDENTIFIER )
			// Java.g:1254:9: modifiers ( 'enum' | IDENTIFIER ) IDENTIFIER
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_enumHeader7904);
				modifiers595 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers595.getTree());
				}

				set596 = (Token) input.LT(1);

				if ((input.LA(1) == ENUM) || (input.LA(1) == IDENTIFIER))
				{
					input.consume();
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, (CommonTree) adaptor.create(set596));
					}
					state.errorRecovery = false;
					state.failed = false;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					MismatchedSetException mse = new MismatchedSetException(null, input);
					throw mse;
				}

				IDENTIFIER597 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_enumHeader7912);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER597_tree = (CommonTree) adaptor.create(IDENTIFIER597);
					adaptor.addChild(root_0, IDENTIFIER597_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 104, enumHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "enumHeader"

	public static class interfaceHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "interfaceHeader"
	// Java.g:1257:1: interfaceHeader : modifiers 'interface' IDENTIFIER ;
	public final JavaParser.interfaceHeader_return interfaceHeader() throws RecognitionException
	{
		JavaParser.interfaceHeader_return retval = new JavaParser.interfaceHeader_return();
		retval.start = input.LT(1);

		int interfaceHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal599 = null;
		Token IDENTIFIER600 = null;
		JavaParser.modifiers_return modifiers598 = null;

		CommonTree string_literal599_tree = null;
		CommonTree IDENTIFIER600_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 105)) { return retval; }

			// Java.g:1258:5: ( modifiers 'interface' IDENTIFIER )
			// Java.g:1258:9: modifiers 'interface' IDENTIFIER
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_interfaceHeader7932);
				modifiers598 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers598.getTree());
				}

				string_literal599 = (Token) match(input, INTERFACE, FOLLOW_INTERFACE_in_interfaceHeader7934);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal599_tree = (CommonTree) adaptor.create(string_literal599);
					adaptor.addChild(root_0, string_literal599_tree);
				}

				IDENTIFIER600 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_interfaceHeader7936);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER600_tree = (CommonTree) adaptor.create(IDENTIFIER600);
					adaptor.addChild(root_0, IDENTIFIER600_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 105, interfaceHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "interfaceHeader"

	public static class annotationHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "annotationHeader"
	// Java.g:1261:1: annotationHeader : modifiers '@' 'interface' IDENTIFIER ;
	public final JavaParser.annotationHeader_return annotationHeader() throws RecognitionException
	{
		JavaParser.annotationHeader_return retval = new JavaParser.annotationHeader_return();
		retval.start = input.LT(1);

		int annotationHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token char_literal602 = null;
		Token string_literal603 = null;
		Token IDENTIFIER604 = null;
		JavaParser.modifiers_return modifiers601 = null;

		CommonTree char_literal602_tree = null;
		CommonTree string_literal603_tree = null;
		CommonTree IDENTIFIER604_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 106)) { return retval; }

			// Java.g:1262:5: ( modifiers '@' 'interface' IDENTIFIER )
			// Java.g:1262:9: modifiers '@' 'interface' IDENTIFIER
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_annotationHeader7956);
				modifiers601 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers601.getTree());
				}

				char_literal602 = (Token) match(input, MONKEYS_AT, FOLLOW_MONKEYS_AT_in_annotationHeader7958);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal602_tree = (CommonTree) adaptor.create(char_literal602);
					adaptor.addChild(root_0, char_literal602_tree);
				}

				string_literal603 = (Token) match(input, INTERFACE, FOLLOW_INTERFACE_in_annotationHeader7960);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					string_literal603_tree = (CommonTree) adaptor.create(string_literal603);
					adaptor.addChild(root_0, string_literal603_tree);
				}

				IDENTIFIER604 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_annotationHeader7962);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER604_tree = (CommonTree) adaptor.create(IDENTIFIER604);
					adaptor.addChild(root_0, IDENTIFIER604_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 106, annotationHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "annotationHeader"

	public static class typeHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "typeHeader"
	// Java.g:1265:1: typeHeader : modifiers ( 'class' | 'enum' | ( ( '@' )? 'interface' ) ) IDENTIFIER ;
	public final JavaParser.typeHeader_return typeHeader() throws RecognitionException
	{
		JavaParser.typeHeader_return retval = new JavaParser.typeHeader_return();
		retval.start = input.LT(1);

		int typeHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal606 = null;
		Token string_literal607 = null;
		Token char_literal608 = null;
		Token string_literal609 = null;
		Token IDENTIFIER610 = null;
		JavaParser.modifiers_return modifiers605 = null;

		CommonTree string_literal606_tree = null;
		CommonTree string_literal607_tree = null;
		CommonTree char_literal608_tree = null;
		CommonTree string_literal609_tree = null;
		CommonTree IDENTIFIER610_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 107)) { return retval; }

			// Java.g:1266:5: ( modifiers ( 'class' | 'enum' | ( ( '@' )? 'interface' ) ) IDENTIFIER )
			// Java.g:1266:9: modifiers ( 'class' | 'enum' | ( ( '@' )? 'interface' ) ) IDENTIFIER
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_typeHeader7982);
				modifiers605 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers605.getTree());
				}

				// Java.g:1266:19: ( 'class' | 'enum' | ( ( '@' )? 'interface' ) )
				int alt161 = 3;
				switch (input.LA(1))
				{
					case CLASS:
					{
						alt161 = 1;
					}
						break;
					case ENUM:
					{
						alt161 = 2;
					}
						break;
					case INTERFACE:
					case MONKEYS_AT:
					{
						alt161 = 3;
					}
						break;
					default:
						if (state.backtracking > 0)
						{
							state.failed = true;
							return retval;
						}
						NoViableAltException nvae = new NoViableAltException("", 161, 0, input);

						throw nvae;

				}

				switch (alt161)
				{
					case 1:
					// Java.g:1266:20: 'class'
					{
						string_literal606 = (Token) match(input, CLASS, FOLLOW_CLASS_in_typeHeader7985);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal606_tree = (CommonTree) adaptor.create(string_literal606);
							adaptor.addChild(root_0, string_literal606_tree);
						}

					}
						break;
					case 2:
					// Java.g:1266:28: 'enum'
					{
						string_literal607 = (Token) match(input, ENUM, FOLLOW_ENUM_in_typeHeader7987);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal607_tree = (CommonTree) adaptor.create(string_literal607);
							adaptor.addChild(root_0, string_literal607_tree);
						}

					}
						break;
					case 3:
					// Java.g:1266:35: ( ( '@' )? 'interface' )
					{
						// Java.g:1266:35: ( ( '@' )? 'interface' )
						// Java.g:1266:36: ( '@' )? 'interface'
						{
							// Java.g:1266:36: ( '@' )?
							int alt160 = 2;
							int LA160_0 = input.LA(1);

							if ((LA160_0 == MONKEYS_AT))
							{
								alt160 = 1;
							}
							switch (alt160)
							{
								case 1:
								// Java.g:1266:36: '@'
								{
									char_literal608 = (Token) match(input, MONKEYS_AT, FOLLOW_MONKEYS_AT_in_typeHeader7990);
									if (state.failed) { return retval; }
									if (state.backtracking == 0)
									{
										char_literal608_tree = (CommonTree) adaptor.create(char_literal608);
										adaptor.addChild(root_0, char_literal608_tree);
									}

								}
									break;

							}

							string_literal609 = (Token) match(input, INTERFACE, FOLLOW_INTERFACE_in_typeHeader7994);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								string_literal609_tree = (CommonTree) adaptor.create(string_literal609);
								adaptor.addChild(root_0, string_literal609_tree);
							}

						}

					}
						break;

				}

				IDENTIFIER610 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_typeHeader7998);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER610_tree = (CommonTree) adaptor.create(IDENTIFIER610);
					adaptor.addChild(root_0, IDENTIFIER610_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 107, typeHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "typeHeader"

	public static class methodHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "methodHeader"
	// Java.g:1269:1: methodHeader : modifiers ( typeParameters )? ( type | 'void' )? IDENTIFIER '(' ;
	public final JavaParser.methodHeader_return methodHeader() throws RecognitionException
	{
		JavaParser.methodHeader_return retval = new JavaParser.methodHeader_return();
		retval.start = input.LT(1);

		int methodHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token string_literal614 = null;
		Token IDENTIFIER615 = null;
		Token char_literal616 = null;
		JavaParser.modifiers_return modifiers611 = null;

		JavaParser.typeParameters_return typeParameters612 = null;

		JavaParser.type_return type613 = null;

		CommonTree string_literal614_tree = null;
		CommonTree IDENTIFIER615_tree = null;
		CommonTree char_literal616_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 108)) { return retval; }

			// Java.g:1270:5: ( modifiers ( typeParameters )? ( type | 'void' )? IDENTIFIER '(' )
			// Java.g:1270:9: modifiers ( typeParameters )? ( type | 'void' )? IDENTIFIER '('
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_methodHeader8018);
				modifiers611 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers611.getTree());
				}

				// Java.g:1270:19: ( typeParameters )?
				int alt162 = 2;
				int LA162_0 = input.LA(1);

				if ((LA162_0 == LT))
				{
					alt162 = 1;
				}
				switch (alt162)
				{
					case 1:
					// Java.g:1270:19: typeParameters
					{
						pushFollow(FOLLOW_typeParameters_in_methodHeader8020);
						typeParameters612 = typeParameters();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, typeParameters612.getTree());
						}

					}
						break;

				}

				// Java.g:1270:35: ( type | 'void' )?
				int alt163 = 3;
				switch (input.LA(1))
				{
					case IDENTIFIER:
					{
						int LA163_1 = input.LA(2);

						if (((LA163_1 == DOT) || (LA163_1 == IDENTIFIER) || (LA163_1 == LBRACKET) || (LA163_1 == LT)))
						{
							alt163 = 1;
						}
					}
						break;
					case BOOLEAN:
					case BYTE:
					case CHAR:
					case DOUBLE:
					case FLOAT:
					case INT:
					case LONG:
					case SHORT:
					{
						alt163 = 1;
					}
						break;
					case VOID:
					{
						alt163 = 2;
					}
						break;
				}

				switch (alt163)
				{
					case 1:
					// Java.g:1270:36: type
					{
						pushFollow(FOLLOW_type_in_methodHeader8024);
						type613 = type();

						state._fsp--;
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							adaptor.addChild(root_0, type613.getTree());
						}

					}
						break;
					case 2:
					// Java.g:1270:41: 'void'
					{
						string_literal614 = (Token) match(input, VOID, FOLLOW_VOID_in_methodHeader8026);
						if (state.failed) { return retval; }
						if (state.backtracking == 0)
						{
							string_literal614_tree = (CommonTree) adaptor.create(string_literal614);
							adaptor.addChild(root_0, string_literal614_tree);
						}

					}
						break;

				}

				IDENTIFIER615 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_methodHeader8030);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER615_tree = (CommonTree) adaptor.create(IDENTIFIER615);
					adaptor.addChild(root_0, IDENTIFIER615_tree);
				}

				char_literal616 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_methodHeader8032);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					char_literal616_tree = (CommonTree) adaptor.create(char_literal616);
					adaptor.addChild(root_0, char_literal616_tree);
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 108, methodHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "methodHeader"

	public static class fieldHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "fieldHeader"
	// Java.g:1273:1: fieldHeader : modifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) ;
	public final JavaParser.fieldHeader_return fieldHeader() throws RecognitionException
	{
		JavaParser.fieldHeader_return retval = new JavaParser.fieldHeader_return();
		retval.start = input.LT(1);

		int fieldHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER619 = null;
		Token char_literal620 = null;
		Token char_literal621 = null;
		Token set622 = null;
		JavaParser.modifiers_return modifiers617 = null;

		JavaParser.type_return type618 = null;

		CommonTree IDENTIFIER619_tree = null;
		CommonTree char_literal620_tree = null;
		CommonTree char_literal621_tree = null;
		CommonTree set622_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 109)) { return retval; }

			// Java.g:1274:5: ( modifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) )
			// Java.g:1274:9: modifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' )
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_modifiers_in_fieldHeader8052);
				modifiers617 = modifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, modifiers617.getTree());
				}

				pushFollow(FOLLOW_type_in_fieldHeader8054);
				type618 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type618.getTree());
				}

				IDENTIFIER619 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_fieldHeader8056);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER619_tree = (CommonTree) adaptor.create(IDENTIFIER619);
					adaptor.addChild(root_0, IDENTIFIER619_tree);
				}

				// Java.g:1274:35: ( '[' ']' )*
				loop164:
				do
				{
					int alt164 = 2;
					int LA164_0 = input.LA(1);

					if ((LA164_0 == LBRACKET))
					{
						alt164 = 1;
					}

					switch (alt164)
					{
						case 1:
						// Java.g:1274:36: '[' ']'
						{
							char_literal620 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_fieldHeader8059);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal620_tree = (CommonTree) adaptor.create(char_literal620);
								adaptor.addChild(root_0, char_literal620_tree);
							}

							char_literal621 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_fieldHeader8060);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal621_tree = (CommonTree) adaptor.create(char_literal621);
								adaptor.addChild(root_0, char_literal621_tree);
							}

						}
							break;

						default:
							break loop164;
					}
				}
				while (true);

				set622 = (Token) input.LT(1);

				if ((input.LA(1) == COMMA) || (input.LA(1) == EQ) || (input.LA(1) == SEMI))
				{
					input.consume();
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, (CommonTree) adaptor.create(set622));
					}
					state.errorRecovery = false;
					state.failed = false;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					MismatchedSetException mse = new MismatchedSetException(null, input);
					throw mse;
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 109, fieldHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "fieldHeader"

	public static class localVariableHeader_return extends ParserRuleReturnScope
	{
		CommonTree tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "localVariableHeader"
	// Java.g:1277:1: localVariableHeader : variableModifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) ;
	public final JavaParser.localVariableHeader_return localVariableHeader() throws RecognitionException
	{
		JavaParser.localVariableHeader_return retval = new JavaParser.localVariableHeader_return();
		retval.start = input.LT(1);

		int localVariableHeader_StartIndex = input.index();

		CommonTree root_0 = null;

		Token IDENTIFIER625 = null;
		Token char_literal626 = null;
		Token char_literal627 = null;
		Token set628 = null;
		JavaParser.variableModifiers_return variableModifiers623 = null;

		JavaParser.type_return type624 = null;

		CommonTree IDENTIFIER625_tree = null;
		CommonTree char_literal626_tree = null;
		CommonTree char_literal627_tree = null;
		CommonTree set628_tree = null;

		try
		{
			if ((state.backtracking > 0) && alreadyParsedRule(input, 110)) { return retval; }

			// Java.g:1278:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) )
			// Java.g:1278:9: variableModifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' )
			{
				root_0 = (CommonTree) adaptor.nil();

				pushFollow(FOLLOW_variableModifiers_in_localVariableHeader8090);
				variableModifiers623 = variableModifiers();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, variableModifiers623.getTree());
				}

				pushFollow(FOLLOW_type_in_localVariableHeader8092);
				type624 = type();

				state._fsp--;
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					adaptor.addChild(root_0, type624.getTree());
				}

				IDENTIFIER625 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_localVariableHeader8094);
				if (state.failed) { return retval; }
				if (state.backtracking == 0)
				{
					IDENTIFIER625_tree = (CommonTree) adaptor.create(IDENTIFIER625);
					adaptor.addChild(root_0, IDENTIFIER625_tree);
				}

				// Java.g:1278:43: ( '[' ']' )*
				loop165:
				do
				{
					int alt165 = 2;
					int LA165_0 = input.LA(1);

					if ((LA165_0 == LBRACKET))
					{
						alt165 = 1;
					}

					switch (alt165)
					{
						case 1:
						// Java.g:1278:44: '[' ']'
						{
							char_literal626 = (Token) match(input, LBRACKET, FOLLOW_LBRACKET_in_localVariableHeader8097);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal626_tree = (CommonTree) adaptor.create(char_literal626);
								adaptor.addChild(root_0, char_literal626_tree);
							}

							char_literal627 = (Token) match(input, RBRACKET, FOLLOW_RBRACKET_in_localVariableHeader8098);
							if (state.failed) { return retval; }
							if (state.backtracking == 0)
							{
								char_literal627_tree = (CommonTree) adaptor.create(char_literal627);
								adaptor.addChild(root_0, char_literal627_tree);
							}

						}
							break;

						default:
							break loop165;
					}
				}
				while (true);

				set628 = (Token) input.LT(1);

				if ((input.LA(1) == COMMA) || (input.LA(1) == EQ) || (input.LA(1) == SEMI))
				{
					input.consume();
					if (state.backtracking == 0)
					{
						adaptor.addChild(root_0, (CommonTree) adaptor.create(set628));
					}
					state.errorRecovery = false;
					state.failed = false;
				}
				else
				{
					if (state.backtracking > 0)
					{
						state.failed = true;
						return retval;
					}
					MismatchedSetException mse = new MismatchedSetException(null, input);
					throw mse;
				}

			}

			retval.stop = input.LT(-1);

			if (state.backtracking == 0)
			{

				retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
				adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re)
		{
			reportError(re);
			recover(input, re);
			retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		}

		finally
		{
			// do for sure before leaving
			if (state.backtracking > 0)
			{
				memoize(input, 110, localVariableHeader_StartIndex);
			}

		}
		return retval;
	}

	// $ANTLR end "localVariableHeader"

	// $ANTLR start synpred2_Java
	public final void synpred2_Java_fragment() throws RecognitionException
	{
		// Java.g:299:13: ( ( annotations )? packageDeclaration )
		// Java.g:299:13: ( annotations )? packageDeclaration
		{
			// Java.g:299:13: ( annotations )?
			int alt166 = 2;
			int LA166_0 = input.LA(1);

			if ((LA166_0 == MONKEYS_AT))
			{
				alt166 = 1;
			}
			switch (alt166)
			{
				case 1:
				// Java.g:299:14: annotations
				{
					pushFollow(FOLLOW_annotations_in_synpred2_Java100);
					annotations();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			pushFollow(FOLLOW_packageDeclaration_in_synpred2_Java129);
			packageDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred2_Java

	// $ANTLR start synpred12_Java
	public final void synpred12_Java_fragment() throws RecognitionException
	{
		// Java.g:343:10: ( classDeclaration )
		// Java.g:343:10: classDeclaration
		{
			pushFollow(FOLLOW_classDeclaration_in_synpred12_Java487);
			classDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred12_Java

	// $ANTLR start synpred27_Java
	public final void synpred27_Java_fragment() throws RecognitionException
	{
		// Java.g:374:9: ( normalClassDeclaration )
		// Java.g:374:9: normalClassDeclaration
		{
			pushFollow(FOLLOW_normalClassDeclaration_in_synpred27_Java724);
			normalClassDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred27_Java

	// $ANTLR start synpred43_Java
	public final void synpred43_Java_fragment() throws RecognitionException
	{
		// Java.g:462:9: ( normalInterfaceDeclaration )
		// Java.g:462:9: normalInterfaceDeclaration
		{
			pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred43_Java1403);
			normalInterfaceDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred43_Java

	// $ANTLR start synpred52_Java
	public final void synpred52_Java_fragment() throws RecognitionException
	{
		// Java.g:504:10: ( fieldDeclaration )
		// Java.g:504:10: fieldDeclaration
		{
			pushFollow(FOLLOW_fieldDeclaration_in_synpred52_Java1733);
			fieldDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred52_Java

	// $ANTLR start synpred53_Java
	public final void synpred53_Java_fragment() throws RecognitionException
	{
		// Java.g:505:10: ( methodDeclaration )
		// Java.g:505:10: methodDeclaration
		{
			pushFollow(FOLLOW_methodDeclaration_in_synpred53_Java1744);
			methodDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred53_Java

	// $ANTLR start synpred54_Java
	public final void synpred54_Java_fragment() throws RecognitionException
	{
		// Java.g:506:10: ( classDeclaration )
		// Java.g:506:10: classDeclaration
		{
			pushFollow(FOLLOW_classDeclaration_in_synpred54_Java1755);
			classDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred54_Java

	// $ANTLR start synpred57_Java
	public final void synpred57_Java_fragment() throws RecognitionException
	{
		// Java.g:522:10: ( explicitConstructorInvocation )
		// Java.g:522:10: explicitConstructorInvocation
		{
			pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred57_Java1892);
			explicitConstructorInvocation();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred57_Java

	// $ANTLR start synpred59_Java
	public final void synpred59_Java_fragment() throws RecognitionException
	{
		// Java.g:514:10: ( modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' )
		// Java.g:514:10: modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
		{
			pushFollow(FOLLOW_modifiers_in_synpred59_Java1804);
			modifiers();

			state._fsp--;
			if (state.failed) { return; }

			// Java.g:515:9: ( typeParameters )?
			int alt169 = 2;
			int LA169_0 = input.LA(1);

			if ((LA169_0 == LT))
			{
				alt169 = 1;
			}
			switch (alt169)
			{
				case 1:
				// Java.g:515:10: typeParameters
				{
					pushFollow(FOLLOW_typeParameters_in_synpred59_Java1815);
					typeParameters();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_synpred59_Java1836);
			if (state.failed) { return; }

			pushFollow(FOLLOW_formalParameters_in_synpred59_Java1846);
			formalParameters();

			state._fsp--;
			if (state.failed) { return; }

			// Java.g:519:9: ( 'throws' qualifiedNameList )?
			int alt170 = 2;
			int LA170_0 = input.LA(1);

			if ((LA170_0 == THROWS))
			{
				alt170 = 1;
			}
			switch (alt170)
			{
				case 1:
				// Java.g:519:10: 'throws' qualifiedNameList
				{
					match(input, THROWS, FOLLOW_THROWS_in_synpred59_Java1857);
					if (state.failed) { return; }

					pushFollow(FOLLOW_qualifiedNameList_in_synpred59_Java1859);
					qualifiedNameList();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			match(input, LBRACE, FOLLOW_LBRACE_in_synpred59_Java1880);
			if (state.failed) { return; }

			// Java.g:522:9: ( explicitConstructorInvocation )?
			int alt171 = 2;
			switch (input.LA(1))
			{
				case LT:
				{
					alt171 = 1;
				}
					break;
				case THIS:
				{
					int LA171_2 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case LPAREN:
				{
					int LA171_3 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case SUPER:
				{
					int LA171_4 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case IDENTIFIER:
				{
					int LA171_5 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case CHARLITERAL:
				case DOUBLELITERAL:
				case FALSE:
				case FLOATLITERAL:
				case INTLITERAL:
				case LONGLITERAL:
				case NULL:
				case STRINGLITERAL:
				case TRUE:
				{
					int LA171_6 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case NEW:
				{
					int LA171_7 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case BOOLEAN:
				case BYTE:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case SHORT:
				{
					int LA171_8 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
				case VOID:
				{
					int LA171_9 = input.LA(2);

					if ((synpred57_Java()))
					{
						alt171 = 1;
					}
				}
					break;
			}

			switch (alt171)
			{
				case 1:
				// Java.g:522:10: explicitConstructorInvocation
				{
					pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred59_Java1892);
					explicitConstructorInvocation();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			// Java.g:524:9: ( blockStatement )*
			loop172:
			do
			{
				int alt172 = 2;
				int LA172_0 = input.LA(1);

				if (((LA172_0 == ABSTRACT) || ((LA172_0 >= ASSERT) && (LA172_0 <= BANG)) || ((LA172_0 >= BOOLEAN) && (LA172_0 <= BYTE)) || ((LA172_0 >= CHAR) && (LA172_0 <= CLASS)) || (LA172_0 == CONTINUE) || (LA172_0 == DO) || ((LA172_0 >= DOUBLE) && (LA172_0 <= DOUBLELITERAL)) || (LA172_0 == ENUM) || ((LA172_0 >= FALSE) && (LA172_0 <= FINAL)) || ((LA172_0 >= FLOAT) && (LA172_0 <= FOR)) || ((LA172_0 >= IDENTIFIER) && (LA172_0 <= IF)) || ((LA172_0 >= INT) && (LA172_0 <= INTLITERAL)) || (LA172_0 == LBRACE) || ((LA172_0 >= LONG) && (LA172_0 <= LT)) || ((LA172_0 >= MONKEYS_AT) && (LA172_0 <= NULL)) || (LA172_0 == PLUS) || ((LA172_0 >= PLUSPLUS) && (LA172_0 <= PUBLIC)) || (LA172_0 == RETURN) || ((LA172_0 >= SEMI) && (LA172_0 <= SHORT)) || ((LA172_0 >= STATIC) && (LA172_0 <= SUB)) || ((LA172_0 >= SUBSUB) && (LA172_0 <= SYNCHRONIZED)) || ((LA172_0 >= THIS) && (LA172_0 <= THROW)) || ((LA172_0 >= TILDE) && (LA172_0 <= WHILE))))
				{
					alt172 = 1;
				}

				switch (alt172)
				{
					case 1:
					// Java.g:524:10: blockStatement
					{
						pushFollow(FOLLOW_blockStatement_in_synpred59_Java1914);
						blockStatement();

						state._fsp--;
						if (state.failed) { return; }

					}
						break;

					default:
						break loop172;
				}
			}
			while (true);

			match(input, RBRACE, FOLLOW_RBRACE_in_synpred59_Java1935);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred59_Java

	// $ANTLR start synpred68_Java
	public final void synpred68_Java_fragment() throws RecognitionException
	{
		// Java.g:568:9: ( interfaceFieldDeclaration )
		// Java.g:568:9: interfaceFieldDeclaration
		{
			pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred68_Java2310);
			interfaceFieldDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred68_Java

	// $ANTLR start synpred69_Java
	public final void synpred69_Java_fragment() throws RecognitionException
	{
		// Java.g:569:9: ( interfaceMethodDeclaration )
		// Java.g:569:9: interfaceMethodDeclaration
		{
			pushFollow(FOLLOW_interfaceMethodDeclaration_in_synpred69_Java2320);
			interfaceMethodDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred69_Java

	// $ANTLR start synpred70_Java
	public final void synpred70_Java_fragment() throws RecognitionException
	{
		// Java.g:570:9: ( interfaceDeclaration )
		// Java.g:570:9: interfaceDeclaration
		{
			pushFollow(FOLLOW_interfaceDeclaration_in_synpred70_Java2330);
			interfaceDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred70_Java

	// $ANTLR start synpred71_Java
	public final void synpred71_Java_fragment() throws RecognitionException
	{
		// Java.g:571:9: ( classDeclaration )
		// Java.g:571:9: classDeclaration
		{
			pushFollow(FOLLOW_classDeclaration_in_synpred71_Java2340);
			classDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred71_Java

	// $ANTLR start synpred96_Java
	public final void synpred96_Java_fragment() throws RecognitionException
	{
		// Java.g:666:9: ( ellipsisParameterDecl )
		// Java.g:666:9: ellipsisParameterDecl
		{
			pushFollow(FOLLOW_ellipsisParameterDecl_in_synpred96_Java3104);
			ellipsisParameterDecl();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred96_Java

	// $ANTLR start synpred98_Java
	public final void synpred98_Java_fragment() throws RecognitionException
	{
		// Java.g:667:9: ( normalParameterDecl ( ',' normalParameterDecl )* )
		// Java.g:667:9: normalParameterDecl ( ',' normalParameterDecl )*
		{
			pushFollow(FOLLOW_normalParameterDecl_in_synpred98_Java3114);
			normalParameterDecl();

			state._fsp--;
			if (state.failed) { return; }

			// Java.g:668:9: ( ',' normalParameterDecl )*
			loop175:
			do
			{
				int alt175 = 2;
				int LA175_0 = input.LA(1);

				if ((LA175_0 == COMMA))
				{
					alt175 = 1;
				}

				switch (alt175)
				{
					case 1:
					// Java.g:668:10: ',' normalParameterDecl
					{
						match(input, COMMA, FOLLOW_COMMA_in_synpred98_Java3125);
						if (state.failed) { return; }

						pushFollow(FOLLOW_normalParameterDecl_in_synpred98_Java3127);
						normalParameterDecl();

						state._fsp--;
						if (state.failed) { return; }

					}
						break;

					default:
						break loop175;
				}
			}
			while (true);

		}

	}

	// $ANTLR end synpred98_Java

	// $ANTLR start synpred99_Java
	public final void synpred99_Java_fragment() throws RecognitionException
	{
		// Java.g:670:10: ( normalParameterDecl ',' )
		// Java.g:670:10: normalParameterDecl ','
		{
			pushFollow(FOLLOW_normalParameterDecl_in_synpred99_Java3149);
			normalParameterDecl();

			state._fsp--;
			if (state.failed) { return; }

			match(input, COMMA, FOLLOW_COMMA_in_synpred99_Java3159);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred99_Java

	// $ANTLR start synpred103_Java
	public final void synpred103_Java_fragment() throws RecognitionException
	{
		// Java.g:690:9: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' )
		// Java.g:690:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
		{
			// Java.g:690:9: ( nonWildcardTypeArguments )?
			int alt176 = 2;
			int LA176_0 = input.LA(1);

			if ((LA176_0 == LT))
			{
				alt176 = 1;
			}
			switch (alt176)
			{
				case 1:
				// Java.g:690:10: nonWildcardTypeArguments
				{
					pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred103_Java3294);
					nonWildcardTypeArguments();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			if ((input.LA(1) == SUPER) || (input.LA(1) == THIS))
			{
				input.consume();
				state.errorRecovery = false;
				state.failed = false;
			}
			else
			{
				if (state.backtracking > 0)
				{
					state.failed = true;
					return;
				}
				MismatchedSetException mse = new MismatchedSetException(null, input);
				throw mse;
			}

			pushFollow(FOLLOW_arguments_in_synpred103_Java3352);
			arguments();

			state._fsp--;
			if (state.failed) { return; }

			match(input, SEMI, FOLLOW_SEMI_in_synpred103_Java3354);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred103_Java

	// $ANTLR start synpred117_Java
	public final void synpred117_Java_fragment() throws RecognitionException
	{
		// Java.g:777:9: ( annotationMethodDeclaration )
		// Java.g:777:9: annotationMethodDeclaration
		{
			pushFollow(FOLLOW_annotationMethodDeclaration_in_synpred117_Java3953);
			annotationMethodDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred117_Java

	// $ANTLR start synpred118_Java
	public final void synpred118_Java_fragment() throws RecognitionException
	{
		// Java.g:778:9: ( interfaceFieldDeclaration )
		// Java.g:778:9: interfaceFieldDeclaration
		{
			pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred118_Java3963);
			interfaceFieldDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred118_Java

	// $ANTLR start synpred119_Java
	public final void synpred119_Java_fragment() throws RecognitionException
	{
		// Java.g:779:9: ( normalClassDeclaration )
		// Java.g:779:9: normalClassDeclaration
		{
			pushFollow(FOLLOW_normalClassDeclaration_in_synpred119_Java3973);
			normalClassDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred119_Java

	// $ANTLR start synpred120_Java
	public final void synpred120_Java_fragment() throws RecognitionException
	{
		// Java.g:780:9: ( normalInterfaceDeclaration )
		// Java.g:780:9: normalInterfaceDeclaration
		{
			pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred120_Java3983);
			normalInterfaceDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred120_Java

	// $ANTLR start synpred121_Java
	public final void synpred121_Java_fragment() throws RecognitionException
	{
		// Java.g:781:9: ( enumDeclaration )
		// Java.g:781:9: enumDeclaration
		{
			pushFollow(FOLLOW_enumDeclaration_in_synpred121_Java3993);
			enumDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred121_Java

	// $ANTLR start synpred122_Java
	public final void synpred122_Java_fragment() throws RecognitionException
	{
		// Java.g:782:9: ( annotationTypeDeclaration )
		// Java.g:782:9: annotationTypeDeclaration
		{
			pushFollow(FOLLOW_annotationTypeDeclaration_in_synpred122_Java4003);
			annotationTypeDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred122_Java

	// $ANTLR start synpred125_Java
	public final void synpred125_Java_fragment() throws RecognitionException
	{
		// Java.g:825:9: ( localVariableDeclarationStatement )
		// Java.g:825:9: localVariableDeclarationStatement
		{
			pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred125_Java4161);
			localVariableDeclarationStatement();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred125_Java

	// $ANTLR start synpred126_Java
	public final void synpred126_Java_fragment() throws RecognitionException
	{
		// Java.g:826:9: ( classOrInterfaceDeclaration )
		// Java.g:826:9: classOrInterfaceDeclaration
		{
			pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred126_Java4171);
			classOrInterfaceDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred126_Java

	// $ANTLR start synpred130_Java
	public final void synpred130_Java_fragment() throws RecognitionException
	{
		// Java.g:846:9: ( ( 'assert' ) expression ( ':' expression )? ';' )
		// Java.g:846:9: ( 'assert' ) expression ( ':' expression )? ';'
		{
			// Java.g:846:9: ( 'assert' )
			// Java.g:846:10: 'assert'
			{
				match(input, ASSERT, FOLLOW_ASSERT_in_synpred130_Java4312);
				if (state.failed) { return; }

			}

			pushFollow(FOLLOW_expression_in_synpred130_Java4332);
			expression();

			state._fsp--;
			if (state.failed) { return; }

			// Java.g:848:20: ( ':' expression )?
			int alt179 = 2;
			int LA179_0 = input.LA(1);

			if ((LA179_0 == COLON))
			{
				alt179 = 1;
			}
			switch (alt179)
			{
				case 1:
				// Java.g:848:21: ':' expression
				{
					match(input, COLON, FOLLOW_COLON_in_synpred130_Java4335);
					if (state.failed) { return; }

					pushFollow(FOLLOW_expression_in_synpred130_Java4337);
					expression();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			match(input, SEMI, FOLLOW_SEMI_in_synpred130_Java4341);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred130_Java

	// $ANTLR start synpred132_Java
	public final void synpred132_Java_fragment() throws RecognitionException
	{
		// Java.g:849:9: ( 'assert' expression ( ':' expression )? ';' )
		// Java.g:849:9: 'assert' expression ( ':' expression )? ';'
		{
			match(input, ASSERT, FOLLOW_ASSERT_in_synpred132_Java4351);
			if (state.failed) { return; }

			pushFollow(FOLLOW_expression_in_synpred132_Java4354);
			expression();

			state._fsp--;
			if (state.failed) { return; }

			// Java.g:849:30: ( ':' expression )?
			int alt180 = 2;
			int LA180_0 = input.LA(1);

			if ((LA180_0 == COLON))
			{
				alt180 = 1;
			}
			switch (alt180)
			{
				case 1:
				// Java.g:849:31: ':' expression
				{
					match(input, COLON, FOLLOW_COLON_in_synpred132_Java4357);
					if (state.failed) { return; }

					pushFollow(FOLLOW_expression_in_synpred132_Java4359);
					expression();

					state._fsp--;
					if (state.failed) { return; }

				}
					break;

			}

			match(input, SEMI, FOLLOW_SEMI_in_synpred132_Java4363);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred132_Java

	// $ANTLR start synpred133_Java
	public final void synpred133_Java_fragment() throws RecognitionException
	{
		// Java.g:850:39: ( 'else' statement )
		// Java.g:850:39: 'else' statement
		{
			match(input, ELSE, FOLLOW_ELSE_in_synpred133_Java4392);
			if (state.failed) { return; }

			pushFollow(FOLLOW_statement_in_synpred133_Java4394);
			statement();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred133_Java

	// $ANTLR start synpred148_Java
	public final void synpred148_Java_fragment() throws RecognitionException
	{
		// Java.g:865:9: ( expression ';' )
		// Java.g:865:9: expression ';'
		{
			pushFollow(FOLLOW_expression_in_synpred148_Java4616);
			expression();

			state._fsp--;
			if (state.failed) { return; }

			match(input, SEMI, FOLLOW_SEMI_in_synpred148_Java4619);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred148_Java

	// $ANTLR start synpred149_Java
	public final void synpred149_Java_fragment() throws RecognitionException
	{
		// Java.g:866:9: ( IDENTIFIER ':' statement )
		// Java.g:866:9: IDENTIFIER ':' statement
		{
			match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_synpred149_Java4634);
			if (state.failed) { return; }

			match(input, COLON, FOLLOW_COLON_in_synpred149_Java4636);
			if (state.failed) { return; }

			pushFollow(FOLLOW_statement_in_synpred149_Java4638);
			statement();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred149_Java

	// $ANTLR start synpred153_Java
	public final void synpred153_Java_fragment() throws RecognitionException
	{
		// Java.g:890:13: ( catches 'finally' block )
		// Java.g:890:13: catches 'finally' block
		{
			pushFollow(FOLLOW_catches_in_synpred153_Java4794);
			catches();

			state._fsp--;
			if (state.failed) { return; }

			match(input, FINALLY, FOLLOW_FINALLY_in_synpred153_Java4796);
			if (state.failed) { return; }

			pushFollow(FOLLOW_block_in_synpred153_Java4798);
			block();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred153_Java

	// $ANTLR start synpred154_Java
	public final void synpred154_Java_fragment() throws RecognitionException
	{
		// Java.g:891:13: ( catches )
		// Java.g:891:13: catches
		{
			pushFollow(FOLLOW_catches_in_synpred154_Java4812);
			catches();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred154_Java

	// $ANTLR start synpred157_Java
	public final void synpred157_Java_fragment() throws RecognitionException
	{
		// Java.g:916:9: ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement )
		// Java.g:916:9: 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement
		{
			match(input, FOR, FOLLOW_FOR_in_synpred157_Java5004);
			if (state.failed) { return; }

			match(input, LPAREN, FOLLOW_LPAREN_in_synpred157_Java5006);
			if (state.failed) { return; }

			pushFollow(FOLLOW_variableModifiers_in_synpred157_Java5008);
			variableModifiers();

			state._fsp--;
			if (state.failed) { return; }

			pushFollow(FOLLOW_type_in_synpred157_Java5010);
			type();

			state._fsp--;
			if (state.failed) { return; }

			match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_synpred157_Java5012);
			if (state.failed) { return; }

			match(input, COLON, FOLLOW_COLON_in_synpred157_Java5014);
			if (state.failed) { return; }

			pushFollow(FOLLOW_expression_in_synpred157_Java5025);
			expression();

			state._fsp--;
			if (state.failed) { return; }

			match(input, RPAREN, FOLLOW_RPAREN_in_synpred157_Java5027);
			if (state.failed) { return; }

			pushFollow(FOLLOW_statement_in_synpred157_Java5029);
			statement();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred157_Java

	// $ANTLR start synpred161_Java
	public final void synpred161_Java_fragment() throws RecognitionException
	{
		// Java.g:930:9: ( localVariableDeclaration )
		// Java.g:930:9: localVariableDeclaration
		{
			pushFollow(FOLLOW_localVariableDeclaration_in_synpred161_Java5208);
			localVariableDeclaration();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred161_Java

	// $ANTLR start synpred202_Java
	public final void synpred202_Java_fragment() throws RecognitionException
	{
		// Java.g:1084:9: ( castExpression )
		// Java.g:1084:9: castExpression
		{
			pushFollow(FOLLOW_castExpression_in_synpred202_Java6453);
			castExpression();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred202_Java

	// $ANTLR start synpred206_Java
	public final void synpred206_Java_fragment() throws RecognitionException
	{
		// Java.g:1094:9: ( '(' primitiveType ')' unaryExpression )
		// Java.g:1094:9: '(' primitiveType ')' unaryExpression
		{
			match(input, LPAREN, FOLLOW_LPAREN_in_synpred206_Java6544);
			if (state.failed) { return; }

			pushFollow(FOLLOW_primitiveType_in_synpred206_Java6546);
			primitiveType();

			state._fsp--;
			if (state.failed) { return; }

			match(input, RPAREN, FOLLOW_RPAREN_in_synpred206_Java6548);
			if (state.failed) { return; }

			pushFollow(FOLLOW_unaryExpression_in_synpred206_Java6550);
			unaryExpression();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred206_Java

	// $ANTLR start synpred208_Java
	public final void synpred208_Java_fragment() throws RecognitionException
	{
		// Java.g:1104:10: ( '.' IDENTIFIER )
		// Java.g:1104:10: '.' IDENTIFIER
		{
			match(input, DOT, FOLLOW_DOT_in_synpred208_Java6621);
			if (state.failed) { return; }

			match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_synpred208_Java6623);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred208_Java

	// $ANTLR start synpred209_Java
	public final void synpred209_Java_fragment() throws RecognitionException
	{
		// Java.g:1106:10: ( identifierSuffix )
		// Java.g:1106:10: identifierSuffix
		{
			pushFollow(FOLLOW_identifierSuffix_in_synpred209_Java6645);
			identifierSuffix();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred209_Java

	// $ANTLR start synpred211_Java
	public final void synpred211_Java_fragment() throws RecognitionException
	{
		// Java.g:1109:10: ( '.' IDENTIFIER )
		// Java.g:1109:10: '.' IDENTIFIER
		{
			match(input, DOT, FOLLOW_DOT_in_synpred211_Java6677);
			if (state.failed) { return; }

			match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_synpred211_Java6679);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred211_Java

	// $ANTLR start synpred212_Java
	public final void synpred212_Java_fragment() throws RecognitionException
	{
		// Java.g:1111:10: ( identifierSuffix )
		// Java.g:1111:10: identifierSuffix
		{
			pushFollow(FOLLOW_identifierSuffix_in_synpred212_Java6701);
			identifierSuffix();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred212_Java

	// $ANTLR start synpred224_Java
	public final void synpred224_Java_fragment() throws RecognitionException
	{
		// Java.g:1139:10: ( '[' expression ']' )
		// Java.g:1139:10: '[' expression ']'
		{
			match(input, LBRACKET, FOLLOW_LBRACKET_in_synpred224_Java6952);
			if (state.failed) { return; }

			pushFollow(FOLLOW_expression_in_synpred224_Java6954);
			expression();

			state._fsp--;
			if (state.failed) { return; }

			match(input, RBRACKET, FOLLOW_RBRACKET_in_synpred224_Java6956);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred224_Java

	// $ANTLR start synpred236_Java
	public final void synpred236_Java_fragment() throws RecognitionException
	{
		// Java.g:1162:9: ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest )
		// Java.g:1162:9: 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
		{
			match(input, NEW, FOLLOW_NEW_in_synpred236_Java7165);
			if (state.failed) { return; }

			pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred236_Java7167);
			nonWildcardTypeArguments();

			state._fsp--;
			if (state.failed) { return; }

			pushFollow(FOLLOW_classOrInterfaceType_in_synpred236_Java7169);
			classOrInterfaceType();

			state._fsp--;
			if (state.failed) { return; }

			pushFollow(FOLLOW_classCreatorRest_in_synpred236_Java7171);
			classCreatorRest();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred236_Java

	// $ANTLR start synpred237_Java
	public final void synpred237_Java_fragment() throws RecognitionException
	{
		// Java.g:1163:9: ( 'new' classOrInterfaceType classCreatorRest )
		// Java.g:1163:9: 'new' classOrInterfaceType classCreatorRest
		{
			match(input, NEW, FOLLOW_NEW_in_synpred237_Java7181);
			if (state.failed) { return; }

			pushFollow(FOLLOW_classOrInterfaceType_in_synpred237_Java7183);
			classOrInterfaceType();

			state._fsp--;
			if (state.failed) { return; }

			pushFollow(FOLLOW_classCreatorRest_in_synpred237_Java7185);
			classCreatorRest();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred237_Java

	// $ANTLR start synpred239_Java
	public final void synpred239_Java_fragment() throws RecognitionException
	{
		// Java.g:1168:9: ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer )
		// Java.g:1168:9: 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer
		{
			match(input, NEW, FOLLOW_NEW_in_synpred239_Java7215);
			if (state.failed) { return; }

			pushFollow(FOLLOW_createdName_in_synpred239_Java7217);
			createdName();

			state._fsp--;
			if (state.failed) { return; }

			match(input, LBRACKET, FOLLOW_LBRACKET_in_synpred239_Java7227);
			if (state.failed) { return; }

			match(input, RBRACKET, FOLLOW_RBRACKET_in_synpred239_Java7229);
			if (state.failed) { return; }

			// Java.g:1170:9: ( '[' ']' )*
			loop193:
			do
			{
				int alt193 = 2;
				int LA193_0 = input.LA(1);

				if ((LA193_0 == LBRACKET))
				{
					alt193 = 1;
				}

				switch (alt193)
				{
					case 1:
					// Java.g:1170:10: '[' ']'
					{
						match(input, LBRACKET, FOLLOW_LBRACKET_in_synpred239_Java7240);
						if (state.failed) { return; }

						match(input, RBRACKET, FOLLOW_RBRACKET_in_synpred239_Java7242);
						if (state.failed) { return; }

					}
						break;

					default:
						break loop193;
				}
			}
			while (true);

			pushFollow(FOLLOW_arrayInitializer_in_synpred239_Java7263);
			arrayInitializer();

			state._fsp--;
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred239_Java

	// $ANTLR start synpred240_Java
	public final void synpred240_Java_fragment() throws RecognitionException
	{
		// Java.g:1177:13: ( '[' expression ']' )
		// Java.g:1177:13: '[' expression ']'
		{
			match(input, LBRACKET, FOLLOW_LBRACKET_in_synpred240_Java7312);
			if (state.failed) { return; }

			pushFollow(FOLLOW_expression_in_synpred240_Java7314);
			expression();

			state._fsp--;
			if (state.failed) { return; }

			match(input, RBRACKET, FOLLOW_RBRACKET_in_synpred240_Java7328);
			if (state.failed) { return; }

		}

	}

	// $ANTLR end synpred240_Java

	// Delegated rules

	public final boolean synpred43_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred43_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred98_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred98_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred157_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred157_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred224_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred224_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred211_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred211_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred121_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred121_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred239_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred239_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred69_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred69_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred202_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred202_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred154_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred154_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred71_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred71_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred133_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred133_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred125_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred125_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred132_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred132_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred119_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred119_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred54_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred54_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred148_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred148_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred117_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred117_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred2_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred2_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred130_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred130_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred126_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred126_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred59_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred59_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred212_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred212_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred161_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred161_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred57_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred57_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred209_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred209_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred68_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred68_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred53_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred53_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred52_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred52_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred236_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred236_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred12_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred12_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred149_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred149_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred120_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred120_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred122_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred122_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred240_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred240_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred206_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred206_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred70_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred70_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred27_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred27_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred96_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred96_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred153_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred153_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred99_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred99_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred103_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred103_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred237_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred237_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred118_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred118_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public final boolean synpred208_Java()
	{
		state.backtracking++;
		int start = input.mark();
		try
		{
			synpred208_Java_fragment(); // can never throw exception
		}
		catch (RecognitionException re)
		{
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}

	public static final BitSet FOLLOW_annotations_in_compilationUnit100 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000004000L });
	public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit129 = new BitSet(new long[] { 0x1200102000800012L, 0x0011040C10700600L });
	public static final BitSet FOLLOW_importDeclaration_in_compilationUnit151 = new BitSet(new long[] { 0x1200102000800012L, 0x0011040C10700600L });
	public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit173 = new BitSet(new long[] { 0x1000102000800012L, 0x0011040C10700600L });
	public static final BitSet FOLLOW_PACKAGE_in_packageDeclaration204 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration206 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_packageDeclaration216 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IMPORT_in_importDeclaration237 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000400000000L });
	public static final BitSet FOLLOW_STATIC_in_importDeclaration249 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration270 = new BitSet(new long[] { 0x0000000080000000L });
	public static final BitSet FOLLOW_DOT_in_importDeclaration272 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000100000000L });
	public static final BitSet FOLLOW_STAR_in_importDeclaration274 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_importDeclaration284 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IMPORT_in_importDeclaration301 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000400000000L });
	public static final BitSet FOLLOW_STATIC_in_importDeclaration313 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration334 = new BitSet(new long[] { 0x0000000080000000L });
	public static final BitSet FOLLOW_DOT_in_importDeclaration345 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration347 = new BitSet(new long[] { 0x0000000080000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_DOT_in_importDeclaration369 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000100000000L });
	public static final BitSet FOLLOW_STAR_in_importDeclaration371 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_importDeclaration392 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName412 = new BitSet(new long[] { 0x0000000080000002L });
	public static final BitSet FOLLOW_DOT_in_qualifiedImportName423 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName425 = new BitSet(new long[] { 0x0000000080000002L });
	public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration456 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_typeDeclaration466 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration487 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration497 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotation_in_modifiers532 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_PUBLIC_in_modifiers542 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_PROTECTED_in_modifiers552 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_PRIVATE_in_modifiers562 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_STATIC_in_modifiers572 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_ABSTRACT_in_modifiers582 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_FINAL_in_modifiers592 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_NATIVE_in_modifiers602 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_SYNCHRONIZED_in_modifiers612 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_TRANSIENT_in_modifiers622 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_VOLATILE_in_modifiers632 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_STRICTFP_in_modifiers642 = new BitSet(new long[] { 0x0000100000000012L, 0x0011040C00700600L });
	public static final BitSet FOLLOW_FINAL_in_variableModifiers674 = new BitSet(new long[] { 0x0000100000000002L, 0x0000000000000200L });
	public static final BitSet FOLLOW_annotation_in_variableModifiers688 = new BitSet(new long[] { 0x0000100000000002L, 0x0000000000000200L });
	public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration724 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration734 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_normalClassDeclaration754 = new BitSet(new long[] { 0x0000000000800000L });
	public static final BitSet FOLLOW_CLASS_in_normalClassDeclaration757 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_normalClassDeclaration759 = new BitSet(new long[] { 0x0100010000000000L, 0x0000000000000082L });
	public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration770 = new BitSet(new long[] { 0x0100010000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_EXTENDS_in_normalClassDeclaration792 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_normalClassDeclaration794 = new BitSet(new long[] { 0x0100000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_IMPLEMENTS_in_normalClassDeclaration816 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_typeList_in_normalClassDeclaration818 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_classBody_in_normalClassDeclaration851 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LT_in_typeParameters872 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_typeParameter_in_typeParameters886 = new BitSet(new long[] { 0x0008000002000000L });
	public static final BitSet FOLLOW_COMMA_in_typeParameters901 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_typeParameter_in_typeParameters903 = new BitSet(new long[] { 0x0008000002000000L });
	public static final BitSet FOLLOW_GT_in_typeParameters928 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_typeParameter948 = new BitSet(new long[] { 0x0000010000000002L });
	public static final BitSet FOLLOW_EXTENDS_in_typeParameter959 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_typeBound_in_typeParameter961 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_type_in_typeBound993 = new BitSet(new long[] { 0x0000000000000022L });
	public static final BitSet FOLLOW_AMP_in_typeBound1004 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_typeBound1006 = new BitSet(new long[] { 0x0000000000000022L });
	public static final BitSet FOLLOW_modifiers_in_enumDeclaration1038 = new BitSet(new long[] { 0x0000002000000000L });
	public static final BitSet FOLLOW_ENUM_in_enumDeclaration1050 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_enumDeclaration1071 = new BitSet(new long[] { 0x0100000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_IMPLEMENTS_in_enumDeclaration1082 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_typeList_in_enumDeclaration1084 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_enumBody_in_enumDeclaration1105 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_enumBody1130 = new BitSet(new long[] { 0x0040000002000000L, 0x0000000011000200L });
	public static final BitSet FOLLOW_enumConstants_in_enumBody1141 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000011000000L });
	public static final BitSet FOLLOW_COMMA_in_enumBody1163 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000011000000L });
	public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody1176 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_RBRACE_in_enumBody1198 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_enumConstant_in_enumConstants1218 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_enumConstants1229 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000000000200L });
	public static final BitSet FOLLOW_enumConstant_in_enumConstants1231 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_annotations_in_enumConstant1265 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_enumConstant1286 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000042L });
	public static final BitSet FOLLOW_arguments_in_enumConstant1297 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000002L });
	public static final BitSet FOLLOW_classBody_in_enumConstant1319 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_enumBodyDeclarations1360 = new BitSet(new long[] { 0x1840502100A14012L, 0x0019040C30700692L });
	public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1372 = new BitSet(new long[] { 0x1840502100A14012L, 0x0019040C30700692L });
	public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1403 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1413 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_normalInterfaceDeclaration1437 = new BitSet(new long[] { 0x1000000000000000L });
	public static final BitSet FOLLOW_INTERFACE_in_normalInterfaceDeclaration1439 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1441 = new BitSet(new long[] { 0x0000010000000000L, 0x0000000000000082L });
	public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration1452 = new BitSet(new long[] { 0x0000010000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_EXTENDS_in_normalInterfaceDeclaration1474 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration1476 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration1497 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_type_in_typeList1517 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_typeList1528 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_typeList1530 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_LBRACE_in_classBody1561 = new BitSet(new long[] { 0x1840502100A14010L, 0x0019040C31700692L });
	public static final BitSet FOLLOW_classBodyDeclaration_in_classBody1573 = new BitSet(new long[] { 0x1840502100A14010L, 0x0019040C31700692L });
	public static final BitSet FOLLOW_RBRACE_in_classBody1595 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_interfaceBody1615 = new BitSet(new long[] { 0x1840502100A14010L, 0x0019040C31700690L });
	public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody1627 = new BitSet(new long[] { 0x1840502100A14010L, 0x0019040C31700690L });
	public static final BitSet FOLLOW_RBRACE_in_interfaceBody1649 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_classBodyDeclaration1669 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_STATIC_in_classBodyDeclaration1680 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_classBodyDeclaration1702 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration1712 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_fieldDeclaration_in_memberDecl1733 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_methodDeclaration_in_memberDecl1744 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classDeclaration_in_memberDecl1755 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl1766 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_methodDeclaration1804 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_typeParameters_in_methodDeclaration1815 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration1836 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_formalParameters_in_methodDeclaration1846 = new BitSet(new long[] { 0x0000000000000000L, 0x0000400000000002L });
	public static final BitSet FOLLOW_THROWS_in_methodDeclaration1857 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration1859 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_methodDeclaration1880 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1EF2L });
	public static final BitSet FOLLOW_explicitConstructorInvocation_in_methodDeclaration1892 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1E72L });
	public static final BitSet FOLLOW_blockStatement_in_methodDeclaration1914 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1E72L });
	public static final BitSet FOLLOW_RBRACE_in_methodDeclaration1935 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_methodDeclaration1945 = new BitSet(new long[] { 0x0840400100214000L, 0x0008000020000090L });
	public static final BitSet FOLLOW_typeParameters_in_methodDeclaration1956 = new BitSet(new long[] { 0x0840400100214000L, 0x0008000020000010L });
	public static final BitSet FOLLOW_type_in_methodDeclaration1978 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_VOID_in_methodDeclaration1992 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration2012 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_formalParameters_in_methodDeclaration2022 = new BitSet(new long[] { 0x0000000000000000L, 0x0000400010000006L });
	public static final BitSet FOLLOW_LBRACKET_in_methodDeclaration2033 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_methodDeclaration2035 = new BitSet(new long[] { 0x0000000000000000L, 0x0000400010000006L });
	public static final BitSet FOLLOW_THROWS_in_methodDeclaration2057 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration2059 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000002L });
	public static final BitSet FOLLOW_block_in_methodDeclaration2114 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_methodDeclaration2128 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_fieldDeclaration2160 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_fieldDeclaration2170 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration2180 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_COMMA_in_fieldDeclaration2191 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration2193 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_fieldDeclaration2214 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_variableDeclarator2234 = new BitSet(new long[] { 0x0000004000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_variableDeclarator2245 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_variableDeclarator2247 = new BitSet(new long[] { 0x0000004000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_EQ_in_variableDeclarator2269 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1872L });
	public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator2271 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration2310 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration2320 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration2330 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classDeclaration_in_interfaceBodyDeclaration2340 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_interfaceBodyDeclaration2350 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_interfaceMethodDeclaration2370 = new BitSet(new long[] { 0x0840400100214000L, 0x0008000020000090L });
	public static final BitSet FOLLOW_typeParameters_in_interfaceMethodDeclaration2381 = new BitSet(new long[] { 0x0840400100214000L, 0x0008000020000010L });
	public static final BitSet FOLLOW_type_in_interfaceMethodDeclaration2403 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_VOID_in_interfaceMethodDeclaration2414 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration2434 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaration2444 = new BitSet(new long[] { 0x0000000000000000L, 0x0000400010000004L });
	public static final BitSet FOLLOW_LBRACKET_in_interfaceMethodDeclaration2455 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_interfaceMethodDeclaration2457 = new BitSet(new long[] { 0x0000000000000000L, 0x0000400010000004L });
	public static final BitSet FOLLOW_THROWS_in_interfaceMethodDeclaration2479 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration2481 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_interfaceMethodDeclaration2494 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_interfaceFieldDeclaration2516 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_interfaceFieldDeclaration2518 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2520 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_COMMA_in_interfaceFieldDeclaration2531 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2533 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_interfaceFieldDeclaration2554 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classOrInterfaceType_in_type2575 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_type2586 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_type2588 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_primitiveType_in_type2609 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_type2620 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_type2622 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType2654 = new BitSet(new long[] { 0x0000000080000002L, 0x0000000000000080L });
	public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2665 = new BitSet(new long[] { 0x0000000080000002L });
	public static final BitSet FOLLOW_DOT_in_classOrInterfaceType2687 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType2689 = new BitSet(new long[] { 0x0000000080000002L, 0x0000000000000080L });
	public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2704 = new BitSet(new long[] { 0x0000000080000002L });
	public static final BitSet FOLLOW_LT_in_typeArguments2841 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020800010L });
	public static final BitSet FOLLOW_typeArgument_in_typeArguments2843 = new BitSet(new long[] { 0x0008000002000000L });
	public static final BitSet FOLLOW_COMMA_in_typeArguments2854 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020800010L });
	public static final BitSet FOLLOW_typeArgument_in_typeArguments2856 = new BitSet(new long[] { 0x0008000002000000L });
	public static final BitSet FOLLOW_GT_in_typeArguments2878 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_type_in_typeArgument2898 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_QUES_in_typeArgument2908 = new BitSet(new long[] { 0x0000010000000002L, 0x0000010000000000L });
	public static final BitSet FOLLOW_set_in_typeArgument2932 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_typeArgument2976 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList3007 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_qualifiedNameList3018 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList3020 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_LPAREN_in_formalParameters3051 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000028000210L });
	public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters3062 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_formalParameters3084 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3104 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3114 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_formalParameterDecls3125 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000020000210L });
	public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3127 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3149 = new BitSet(new long[] { 0x0000000002000000L });
	public static final BitSet FOLLOW_COMMA_in_formalParameterDecls3159 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000020000210L });
	public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3181 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_variableModifiers_in_normalParameterDecl3201 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_normalParameterDecl3203 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_normalParameterDecl3205 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_normalParameterDecl3216 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_normalParameterDecl3218 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_variableModifiers_in_ellipsisParameterDecl3249 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_ellipsisParameterDecl3259 = new BitSet(new long[] { 0x0000000800000000L });
	public static final BitSet FOLLOW_ELLIPSIS_in_ellipsisParameterDecl3262 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_ellipsisParameterDecl3272 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation3294 = new BitSet(new long[] { 0x0000000000000000L, 0x0000110000000000L });
	public static final BitSet FOLLOW_set_in_explicitConstructorInvocation3320 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation3352 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation3354 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation3365 = new BitSet(new long[] { 0x0000000080000000L });
	public static final BitSet FOLLOW_DOT_in_explicitConstructorInvocation3375 = new BitSet(new long[] { 0x0000000000000000L, 0x0000010000000080L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation3386 = new BitSet(new long[] { 0x0000000000000000L, 0x0000010000000000L });
	public static final BitSet FOLLOW_SUPER_in_explicitConstructorInvocation3407 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation3417 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation3419 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName3439 = new BitSet(new long[] { 0x0000000080000002L });
	public static final BitSet FOLLOW_DOT_in_qualifiedName3450 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName3452 = new BitSet(new long[] { 0x0000000080000002L });
	public static final BitSet FOLLOW_annotation_in_annotations3484 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000200L });
	public static final BitSet FOLLOW_MONKEYS_AT_in_annotation3517 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedName_in_annotation3519 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_annotation3533 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0280A1A72L });
	public static final BitSet FOLLOW_elementValuePairs_in_annotation3560 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_elementValue_in_annotation3584 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_annotation3620 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs3652 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_elementValuePairs3663 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs3665 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_elementValuePair3696 = new BitSet(new long[] { 0x0000004000000000L });
	public static final BitSet FOLLOW_EQ_in_elementValuePair3698 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1A72L });
	public static final BitSet FOLLOW_elementValue_in_elementValuePair3700 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_conditionalExpression_in_elementValue3720 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotation_in_elementValue3730 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue3740 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_elementValueArrayInitializer3760 = new BitSet(new long[] { 0x2840C80302614200L, 0x000A91B0210A1A72L });
	public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer3771 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer3786 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1A72L });
	public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer3788 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer3817 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_RBRACE_in_elementValueArrayInitializer3821 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_annotationTypeDeclaration3844 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000200L });
	public static final BitSet FOLLOW_MONKEYS_AT_in_annotationTypeDeclaration3846 = new BitSet(new long[] { 0x1000000000000000L });
	public static final BitSet FOLLOW_INTERFACE_in_annotationTypeDeclaration3856 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_annotationTypeDeclaration3866 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration3876 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_annotationTypeBody3897 = new BitSet(new long[] { 0x1840502100A14010L, 0x0011040C31700610L });
	public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody3909 = new BitSet(new long[] { 0x1840502100A14010L, 0x0011040C31700610L });
	public static final BitSet FOLLOW_RBRACE_in_annotationTypeBody3931 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration3953 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration3963 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration3973 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration3983 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration3993 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration4003 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_annotationTypeElementDeclaration4013 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_annotationMethodDeclaration4033 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_annotationMethodDeclaration4035 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_annotationMethodDeclaration4037 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_annotationMethodDeclaration4047 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_annotationMethodDeclaration4049 = new BitSet(new long[] { 0x0000000020000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_DEFAULT_in_annotationMethodDeclaration4052 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1A72L });
	public static final BitSet FOLLOW_elementValue_in_annotationMethodDeclaration4054 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_annotationMethodDeclaration4083 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_block4107 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1E72L });
	public static final BitSet FOLLOW_blockStatement_in_block4118 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1E72L });
	public static final BitSet FOLLOW_RBRACE_in_block4139 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement4161 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement4171 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_statement_in_blockStatement4181 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement4202 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_localVariableDeclarationStatement4212 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration4232 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_localVariableDeclaration4234 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration4244 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_localVariableDeclaration4255 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration4257 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_block_in_statement4288 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ASSERT_in_statement4312 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_statement4332 = new BitSet(new long[] { 0x0000000001000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_COLON_in_statement4335 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_statement4337 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4341 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ASSERT_in_statement4351 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_statement4354 = new BitSet(new long[] { 0x0000000001000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_COLON_in_statement4357 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_statement4359 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4363 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IF_in_statement4385 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_parExpression_in_statement4387 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_statement4389 = new BitSet(new long[] { 0x0000001000000002L });
	public static final BitSet FOLLOW_ELSE_in_statement4392 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_statement4394 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_forstatement_in_statement4416 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_WHILE_in_statement4426 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_parExpression_in_statement4428 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_statement4430 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DO_in_statement4440 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_statement4442 = new BitSet(new long[] { 0x0000000000000000L, 0x0020000000000000L });
	public static final BitSet FOLLOW_WHILE_in_statement4444 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_parExpression_in_statement4446 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4448 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_trystatement_in_statement4458 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SWITCH_in_statement4468 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_parExpression_in_statement4470 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_statement4472 = new BitSet(new long[] { 0x0000000020080000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement4474 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_RBRACE_in_statement4476 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SYNCHRONIZED_in_statement4486 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_parExpression_in_statement4488 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_statement4490 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_RETURN_in_statement4500 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0300A1870L });
	public static final BitSet FOLLOW_expression_in_statement4503 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4508 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_THROW_in_statement4518 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_statement4520 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4522 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_BREAK_in_statement4532 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_statement4547 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4564 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_CONTINUE_in_statement4574 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_statement4589 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4606 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expression_in_statement4616 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_statement4619 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_statement4634 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_statement4636 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_statement4638 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SEMI_in_statement4648 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups4670 = new BitSet(new long[] { 0x0000000020080002L });
	public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup4699 = new BitSet(new long[] { 0x38C1D82350E1C312L, 0x003FB7BC347A1E72L });
	public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup4710 = new BitSet(new long[] { 0x38C1D82350E1C312L, 0x003FB7BC347A1E72L });
	public static final BitSet FOLLOW_CASE_in_switchLabel4741 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_switchLabel4743 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_switchLabel4745 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DEFAULT_in_switchLabel4755 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_switchLabel4757 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_TRY_in_trystatement4778 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_trystatement4780 = new BitSet(new long[] { 0x0000200000100000L });
	public static final BitSet FOLLOW_catches_in_trystatement4794 = new BitSet(new long[] { 0x0000200000000000L });
	public static final BitSet FOLLOW_FINALLY_in_trystatement4796 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_trystatement4798 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_catches_in_trystatement4812 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_FINALLY_in_trystatement4826 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_trystatement4828 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_catchClause_in_catches4859 = new BitSet(new long[] { 0x0000000000100002L });
	public static final BitSet FOLLOW_catchClause_in_catches4870 = new BitSet(new long[] { 0x0000000000100002L });
	public static final BitSet FOLLOW_CATCH_in_catchClause4901 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_catchClause4903 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000020000210L });
	public static final BitSet FOLLOW_formalParameter_in_catchClause4905 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_catchClause4915 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_catchClause4917 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_variableModifiers_in_formalParameter4938 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_formalParameter4940 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_formalParameter4942 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_formalParameter4953 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_formalParameter4955 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_FOR_in_forstatement5004 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_forstatement5006 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000020000210L });
	public static final BitSet FOLLOW_variableModifiers_in_forstatement5008 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_forstatement5010 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_forstatement5012 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_forstatement5014 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_forstatement5025 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_forstatement5027 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_forstatement5029 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_FOR_in_forstatement5061 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_forstatement5063 = new BitSet(new long[] { 0x2840D80300614200L, 0x000A91B0300A1A70L });
	public static final BitSet FOLLOW_forInit_in_forstatement5083 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_forstatement5104 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0300A1870L });
	public static final BitSet FOLLOW_expression_in_forstatement5124 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_forstatement5145 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0280A1870L });
	public static final BitSet FOLLOW_expressionList_in_forstatement5165 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_forstatement5186 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_forstatement5188 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_localVariableDeclaration_in_forInit5208 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expressionList_in_forInit5218 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LPAREN_in_parExpression5238 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_parExpression5240 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_parExpression5242 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expression_in_expressionList5262 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_expressionList5273 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_expressionList5275 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_conditionalExpression_in_expression5307 = new BitSet(new long[] { 0x0008004000042082L, 0x0000004280050080L });
	public static final BitSet FOLLOW_assignmentOperator_in_expression5318 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_expression5320 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_EQ_in_assignmentOperator5352 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5362 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5372 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5382 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5392 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_AMPEQ_in_assignmentOperator5402 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_BAREQ_in_assignmentOperator5412 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_CARETEQ_in_assignmentOperator5422 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5432 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LT_in_assignmentOperator5443 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_LT_in_assignmentOperator5445 = new BitSet(new long[] { 0x0000004000000000L });
	public static final BitSet FOLLOW_EQ_in_assignmentOperator5447 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_GT_in_assignmentOperator5458 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_assignmentOperator5460 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_assignmentOperator5462 = new BitSet(new long[] { 0x0000004000000000L });
	public static final BitSet FOLLOW_EQ_in_assignmentOperator5464 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_GT_in_assignmentOperator5475 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_assignmentOperator5477 = new BitSet(new long[] { 0x0000004000000000L });
	public static final BitSet FOLLOW_EQ_in_assignmentOperator5479 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression5500 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000800000L });
	public static final BitSet FOLLOW_QUES_in_conditionalExpression5511 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_conditionalExpression5513 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_conditionalExpression5515 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression5517 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression5548 = new BitSet(new long[] { 0x0000000000001002L });
	public static final BitSet FOLLOW_BARBAR_in_conditionalOrExpression5559 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression5561 = new BitSet(new long[] { 0x0000000000001002L });
	public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5592 = new BitSet(new long[] { 0x0000000000000042L });
	public static final BitSet FOLLOW_AMPAMP_in_conditionalAndExpression5603 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5605 = new BitSet(new long[] { 0x0000000000000042L });
	public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5636 = new BitSet(new long[] { 0x0000000000000802L });
	public static final BitSet FOLLOW_BAR_in_inclusiveOrExpression5647 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5649 = new BitSet(new long[] { 0x0000000000000802L });
	public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression5680 = new BitSet(new long[] { 0x0000000000020002L });
	public static final BitSet FOLLOW_CARET_in_exclusiveOrExpression5691 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression5693 = new BitSet(new long[] { 0x0000000000020002L });
	public static final BitSet FOLLOW_equalityExpression_in_andExpression5724 = new BitSet(new long[] { 0x0000000000000022L });
	public static final BitSet FOLLOW_AMP_in_andExpression5735 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_equalityExpression_in_andExpression5737 = new BitSet(new long[] { 0x0000000000000022L });
	public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression5768 = new BitSet(new long[] { 0x0000008000000402L });
	public static final BitSet FOLLOW_set_in_equalityExpression5795 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression5845 = new BitSet(new long[] { 0x0000008000000402L });
	public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression5876 = new BitSet(new long[] { 0x0400000000000002L });
	public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression5887 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_instanceOfExpression5889 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_shiftExpression_in_relationalExpression5920 = new BitSet(new long[] { 0x0008000000000002L, 0x0000000000000080L });
	public static final BitSet FOLLOW_relationalOp_in_relationalExpression5931 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_shiftExpression_in_relationalExpression5933 = new BitSet(new long[] { 0x0008000000000002L, 0x0000000000000080L });
	public static final BitSet FOLLOW_LT_in_relationalOp5965 = new BitSet(new long[] { 0x0000004000000000L });
	public static final BitSet FOLLOW_EQ_in_relationalOp5967 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_GT_in_relationalOp5978 = new BitSet(new long[] { 0x0000004000000000L });
	public static final BitSet FOLLOW_EQ_in_relationalOp5980 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LT_in_relationalOp5990 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_GT_in_relationalOp6000 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_additiveExpression_in_shiftExpression6020 = new BitSet(new long[] { 0x0008000000000002L, 0x0000000000000080L });
	public static final BitSet FOLLOW_shiftOp_in_shiftExpression6031 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_additiveExpression_in_shiftExpression6033 = new BitSet(new long[] { 0x0008000000000002L, 0x0000000000000080L });
	public static final BitSet FOLLOW_LT_in_shiftOp6066 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_LT_in_shiftOp6068 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_GT_in_shiftOp6079 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_shiftOp6081 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_shiftOp6083 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_GT_in_shiftOp6094 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_shiftOp6096 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression6117 = new BitSet(new long[] { 0x0000000000000002L, 0x0000002000020000L });
	public static final BitSet FOLLOW_set_in_additiveExpression6144 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression6194 = new BitSet(new long[] { 0x0000000000000002L, 0x0000002000020000L });
	public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6232 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000140008000L });
	public static final BitSet FOLLOW_set_in_multiplicativeExpression6259 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6327 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000140008000L });
	public static final BitSet FOLLOW_PLUS_in_unaryExpression6360 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6363 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SUB_in_unaryExpression6373 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6375 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpression6385 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6387 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SUBSUB_in_unaryExpression6397 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6399 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression6409 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_TILDE_in_unaryExpressionNotPlusMinus6429 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6431 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_BANG_in_unaryExpressionNotPlusMinus6441 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6443 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus6453 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus6463 = new BitSet(new long[] { 0x0000000080000002L, 0x0000008000080004L });
	public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus6474 = new BitSet(new long[] { 0x0000000080000002L, 0x0000008000080004L });
	public static final BitSet FOLLOW_LPAREN_in_castExpression6544 = new BitSet(new long[] { 0x0800400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_primitiveType_in_castExpression6546 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_castExpression6548 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_castExpression6550 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LPAREN_in_castExpression6560 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_castExpression6562 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_castExpression6564 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A911020001870L });
	public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression6566 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_parExpression_in_primary6588 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_THIS_in_primary6610 = new BitSet(new long[] { 0x0000000080000002L, 0x0000000000000044L });
	public static final BitSet FOLLOW_DOT_in_primary6621 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_primary6623 = new BitSet(new long[] { 0x0000000080000002L, 0x0000000000000044L });
	public static final BitSet FOLLOW_identifierSuffix_in_primary6645 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_primary6666 = new BitSet(new long[] { 0x0000000080000002L, 0x0000000000000044L });
	public static final BitSet FOLLOW_DOT_in_primary6677 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_primary6679 = new BitSet(new long[] { 0x0000000080000002L, 0x0000000000000044L });
	public static final BitSet FOLLOW_identifierSuffix_in_primary6701 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_SUPER_in_primary6722 = new BitSet(new long[] { 0x0000000080000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_superSuffix_in_primary6732 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_literal_in_primary6742 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_creator_in_primary6752 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_primitiveType_in_primary6762 = new BitSet(new long[] { 0x0000000080000000L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_primary6773 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_primary6775 = new BitSet(new long[] { 0x0000000080000000L, 0x0000000000000004L });
	public static final BitSet FOLLOW_DOT_in_primary6796 = new BitSet(new long[] { 0x0000000000800000L });
	public static final BitSet FOLLOW_CLASS_in_primary6798 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_VOID_in_primary6808 = new BitSet(new long[] { 0x0000000080000000L });
	public static final BitSet FOLLOW_DOT_in_primary6810 = new BitSet(new long[] { 0x0000000000800000L });
	public static final BitSet FOLLOW_CLASS_in_primary6812 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_arguments_in_superSuffix6838 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_superSuffix6848 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_typeArguments_in_superSuffix6851 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_superSuffix6872 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_superSuffix6883 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix6916 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix6918 = new BitSet(new long[] { 0x0000000080000000L, 0x0000000000000004L });
	public static final BitSet FOLLOW_DOT_in_identifierSuffix6939 = new BitSet(new long[] { 0x0000000000800000L });
	public static final BitSet FOLLOW_CLASS_in_identifierSuffix6941 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix6952 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_identifierSuffix6954 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix6956 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_arguments_in_identifierSuffix6977 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_identifierSuffix6987 = new BitSet(new long[] { 0x0000000000800000L });
	public static final BitSet FOLLOW_CLASS_in_identifierSuffix6989 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_identifierSuffix6999 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_identifierSuffix7001 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_identifierSuffix7003 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_identifierSuffix7005 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_identifierSuffix7015 = new BitSet(new long[] { 0x0000000000000000L, 0x0000100000000000L });
	public static final BitSet FOLLOW_THIS_in_identifierSuffix7017 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_identifierSuffix7027 = new BitSet(new long[] { 0x0000000000000000L, 0x0000010000000000L });
	public static final BitSet FOLLOW_SUPER_in_identifierSuffix7029 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_identifierSuffix7031 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_innerCreator_in_identifierSuffix7041 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_selector7063 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_selector7065 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_selector7076 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_selector7097 = new BitSet(new long[] { 0x0000000000000000L, 0x0000100000000000L });
	public static final BitSet FOLLOW_THIS_in_selector7099 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_selector7109 = new BitSet(new long[] { 0x0000000000000000L, 0x0000010000000000L });
	public static final BitSet FOLLOW_SUPER_in_selector7111 = new BitSet(new long[] { 0x0000000080000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_superSuffix_in_selector7121 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_innerCreator_in_selector7131 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACKET_in_selector7141 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_selector7143 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_selector7145 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_creator7165 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator7167 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_classOrInterfaceType_in_creator7169 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_classCreatorRest_in_creator7171 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_creator7181 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_classOrInterfaceType_in_creator7183 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_classCreatorRest_in_creator7185 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_arrayCreator_in_creator7195 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_arrayCreator7215 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_createdName_in_arrayCreator7217 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7227 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7229 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000006L });
	public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7240 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7242 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000006L });
	public static final BitSet FOLLOW_arrayInitializer_in_arrayCreator7263 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_arrayCreator7274 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_createdName_in_arrayCreator7276 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7286 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_arrayCreator7288 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7298 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7312 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_arrayCreator7314 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7328 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7350 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7352 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000004L });
	public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer7383 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expression_in_variableInitializer7393 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_arrayInitializer7413 = new BitSet(new long[] { 0x2840C80302614200L, 0x000A91B0210A1872L });
	public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer7429 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_COMMA_in_arrayInitializer7448 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1872L });
	public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer7450 = new BitSet(new long[] { 0x0000000002000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_COMMA_in_arrayInitializer7500 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000001000000L });
	public static final BitSet FOLLOW_RBRACE_in_arrayInitializer7513 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classOrInterfaceType_in_createdName7547 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_primitiveType_in_createdName7557 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_innerCreator7578 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000800L });
	public static final BitSet FOLLOW_NEW_in_innerCreator7580 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator7591 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_innerCreator7612 = new BitSet(new long[] { 0x0000000000000000L, 0x00000000000000C0L });
	public static final BitSet FOLLOW_typeArguments_in_innerCreator7623 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_classCreatorRest_in_innerCreator7644 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_arguments_in_classCreatorRest7665 = new BitSet(new long[] { 0x0000000000000002L, 0x0000000000000002L });
	public static final BitSet FOLLOW_classBody_in_classCreatorRest7676 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LT_in_nonWildcardTypeArguments7708 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments7710 = new BitSet(new long[] { 0x0008000000000000L });
	public static final BitSet FOLLOW_GT_in_nonWildcardTypeArguments7720 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LPAREN_in_arguments7740 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0280A1870L });
	public static final BitSet FOLLOW_expressionList_in_arguments7743 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_arguments7756 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_classHeader7880 = new BitSet(new long[] { 0x0000000000800000L });
	public static final BitSet FOLLOW_CLASS_in_classHeader7882 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_classHeader7884 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_enumHeader7904 = new BitSet(new long[] { 0x0040002000000000L });
	public static final BitSet FOLLOW_set_in_enumHeader7906 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_enumHeader7912 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_interfaceHeader7932 = new BitSet(new long[] { 0x1000000000000000L });
	public static final BitSet FOLLOW_INTERFACE_in_interfaceHeader7934 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_interfaceHeader7936 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_annotationHeader7956 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000200L });
	public static final BitSet FOLLOW_MONKEYS_AT_in_annotationHeader7958 = new BitSet(new long[] { 0x1000000000000000L });
	public static final BitSet FOLLOW_INTERFACE_in_annotationHeader7960 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_annotationHeader7962 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_typeHeader7982 = new BitSet(new long[] { 0x1000002000800000L, 0x0000000000000200L });
	public static final BitSet FOLLOW_CLASS_in_typeHeader7985 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_ENUM_in_typeHeader7987 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_MONKEYS_AT_in_typeHeader7990 = new BitSet(new long[] { 0x1000000000000000L });
	public static final BitSet FOLLOW_INTERFACE_in_typeHeader7994 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_typeHeader7998 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_methodHeader8018 = new BitSet(new long[] { 0x0840400100214000L, 0x0008000020000090L });
	public static final BitSet FOLLOW_typeParameters_in_methodHeader8020 = new BitSet(new long[] { 0x0840400100214000L, 0x0008000020000010L });
	public static final BitSet FOLLOW_type_in_methodHeader8024 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_VOID_in_methodHeader8026 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_methodHeader8030 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_methodHeader8032 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_fieldHeader8052 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_fieldHeader8054 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_fieldHeader8056 = new BitSet(new long[] { 0x0000004002000000L, 0x0000000010000004L });
	public static final BitSet FOLLOW_LBRACKET_in_fieldHeader8059 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_fieldHeader8060 = new BitSet(new long[] { 0x0000004002000000L, 0x0000000010000004L });
	public static final BitSet FOLLOW_set_in_fieldHeader8064 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_variableModifiers_in_localVariableHeader8090 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_localVariableHeader8092 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_localVariableHeader8094 = new BitSet(new long[] { 0x0000004002000000L, 0x0000000010000004L });
	public static final BitSet FOLLOW_LBRACKET_in_localVariableHeader8097 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_localVariableHeader8098 = new BitSet(new long[] { 0x0000004002000000L, 0x0000000010000004L });
	public static final BitSet FOLLOW_set_in_localVariableHeader8102 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotations_in_synpred2_Java100 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000004000L });
	public static final BitSet FOLLOW_packageDeclaration_in_synpred2_Java129 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classDeclaration_in_synpred12_Java487 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalClassDeclaration_in_synpred27_Java724 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred43_Java1403 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_fieldDeclaration_in_synpred52_Java1733 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_methodDeclaration_in_synpred53_Java1744 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classDeclaration_in_synpred54_Java1755 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred57_Java1892 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_modifiers_in_synpred59_Java1804 = new BitSet(new long[] { 0x0040000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_typeParameters_in_synpred59_Java1815 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred59_Java1836 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_formalParameters_in_synpred59_Java1846 = new BitSet(new long[] { 0x0000000000000000L, 0x0000400000000002L });
	public static final BitSet FOLLOW_THROWS_in_synpred59_Java1857 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_qualifiedNameList_in_synpred59_Java1859 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACE_in_synpred59_Java1880 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1EF2L });
	public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred59_Java1892 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1E72L });
	public static final BitSet FOLLOW_blockStatement_in_synpred59_Java1914 = new BitSet(new long[] { 0x38C1D82350E1C310L, 0x003FB7BC357A1E72L });
	public static final BitSet FOLLOW_RBRACE_in_synpred59_Java1935 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred68_Java2310 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceMethodDeclaration_in_synpred69_Java2320 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceDeclaration_in_synpred70_Java2330 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classDeclaration_in_synpred71_Java2340 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ellipsisParameterDecl_in_synpred96_Java3104 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalParameterDecl_in_synpred98_Java3114 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_COMMA_in_synpred98_Java3125 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000020000210L });
	public static final BitSet FOLLOW_normalParameterDecl_in_synpred98_Java3127 = new BitSet(new long[] { 0x0000000002000002L });
	public static final BitSet FOLLOW_normalParameterDecl_in_synpred99_Java3149 = new BitSet(new long[] { 0x0000000002000000L });
	public static final BitSet FOLLOW_COMMA_in_synpred99_Java3159 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred103_Java3294 = new BitSet(new long[] { 0x0000000000000000L, 0x0000110000000000L });
	public static final BitSet FOLLOW_set_in_synpred103_Java3320 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_arguments_in_synpred103_Java3352 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_synpred103_Java3354 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotationMethodDeclaration_in_synpred117_Java3953 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred118_Java3963 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalClassDeclaration_in_synpred119_Java3973 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred120_Java3983 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_enumDeclaration_in_synpred121_Java3993 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_annotationTypeDeclaration_in_synpred122_Java4003 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred125_Java4161 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred126_Java4171 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ASSERT_in_synpred130_Java4312 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred130_Java4332 = new BitSet(new long[] { 0x0000000001000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_COLON_in_synpred130_Java4335 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred130_Java4337 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_synpred130_Java4341 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ASSERT_in_synpred132_Java4351 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred132_Java4354 = new BitSet(new long[] { 0x0000000001000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_COLON_in_synpred132_Java4357 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred132_Java4359 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_synpred132_Java4363 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ELSE_in_synpred133_Java4392 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_synpred133_Java4394 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_expression_in_synpred148_Java4616 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000010000000L });
	public static final BitSet FOLLOW_SEMI_in_synpred148_Java4619 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred149_Java4634 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_synpred149_Java4636 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_synpred149_Java4638 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_catches_in_synpred153_Java4794 = new BitSet(new long[] { 0x0000200000000000L });
	public static final BitSet FOLLOW_FINALLY_in_synpred153_Java4796 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000002L });
	public static final BitSet FOLLOW_block_in_synpred153_Java4798 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_catches_in_synpred154_Java4812 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_FOR_in_synpred157_Java5004 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_LPAREN_in_synpred157_Java5006 = new BitSet(new long[] { 0x0840500100214000L, 0x0000000020000210L });
	public static final BitSet FOLLOW_variableModifiers_in_synpred157_Java5008 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_type_in_synpred157_Java5010 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred157_Java5012 = new BitSet(new long[] { 0x0000000001000000L });
	public static final BitSet FOLLOW_COLON_in_synpred157_Java5014 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred157_Java5025 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_synpred157_Java5027 = new BitSet(new long[] { 0x28C1C8035061C300L, 0x002EB7B0340A1872L });
	public static final BitSet FOLLOW_statement_in_synpred157_Java5029 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_localVariableDeclaration_in_synpred161_Java5208 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_castExpression_in_synpred202_Java6453 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LPAREN_in_synpred206_Java6544 = new BitSet(new long[] { 0x0800400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_primitiveType_in_synpred206_Java6546 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000008000000L });
	public static final BitSet FOLLOW_RPAREN_in_synpred206_Java6548 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_unaryExpression_in_synpred206_Java6550 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_synpred208_Java6621 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred208_Java6623 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_identifierSuffix_in_synpred209_Java6645 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_DOT_in_synpred211_Java6677 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_IDENTIFIER_in_synpred211_Java6679 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_identifierSuffix_in_synpred212_Java6701 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACKET_in_synpred224_Java6952 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred224_Java6954 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_synpred224_Java6956 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_synpred236_Java7165 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000080L });
	public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred236_Java7167 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_classOrInterfaceType_in_synpred236_Java7169 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_classCreatorRest_in_synpred236_Java7171 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_synpred237_Java7181 = new BitSet(new long[] { 0x0040000000000000L });
	public static final BitSet FOLLOW_classOrInterfaceType_in_synpred237_Java7183 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000040L });
	public static final BitSet FOLLOW_classCreatorRest_in_synpred237_Java7185 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_NEW_in_synpred239_Java7215 = new BitSet(new long[] { 0x0840400100214000L, 0x0000000020000010L });
	public static final BitSet FOLLOW_createdName_in_synpred239_Java7217 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000004L });
	public static final BitSet FOLLOW_LBRACKET_in_synpred239_Java7227 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_synpred239_Java7229 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000006L });
	public static final BitSet FOLLOW_LBRACKET_in_synpred239_Java7240 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_synpred239_Java7242 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000000000006L });
	public static final BitSet FOLLOW_arrayInitializer_in_synpred239_Java7263 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_LBRACKET_in_synpred240_Java7312 = new BitSet(new long[] { 0x2840C80300614200L, 0x000A91B0200A1870L });
	public static final BitSet FOLLOW_expression_in_synpred240_Java7314 = new BitSet(new long[] { 0x0000000000000000L, 0x0000000002000000L });
	public static final BitSet FOLLOW_RBRACKET_in_synpred240_Java7328 = new BitSet(new long[] { 0x0000000000000002L });

}
