// Generated from ICSS.g4 by ANTLR 4.8
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ICSSLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COLOR_PROPERTIES=1, WIDTH_HEIGHT_PROPERTIES=2, IF=3, ELSE=4, BOX_BRACKET_OPEN=5, 
		BOX_BRACKET_CLOSE=6, TRUE=7, FALSE=8, PIXELSIZE=9, PERCENTAGE=10, SCALAR=11, 
		COLOR=12, ID_IDENT=13, CLASS_IDENT=14, LOWER_IDENT=15, CAPITAL_IDENT=16, 
		WS=17, OPEN_BRACE=18, CLOSE_BRACE=19, SEMICOLON=20, COLON=21, PLUS=22, 
		MIN=23, MUL=24, ASSIGNMENT_OPERATOR=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"COLOR_PROPERTIES", "WIDTH_HEIGHT_PROPERTIES", "IF", "ELSE", "BOX_BRACKET_OPEN", 
			"BOX_BRACKET_CLOSE", "TRUE", "FALSE", "PIXELSIZE", "PERCENTAGE", "SCALAR", 
			"COLOR", "ID_IDENT", "CLASS_IDENT", "LOWER_IDENT", "CAPITAL_IDENT", "WS", 
			"OPEN_BRACE", "CLOSE_BRACE", "SEMICOLON", "COLON", "PLUS", "MIN", "MUL", 
			"ASSIGNMENT_OPERATOR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'if'", "'else'", "'['", "']'", "'TRUE'", "'FALSE'", 
			null, null, null, null, null, null, null, null, null, "'{'", "'}'", "';'", 
			"':'", "'+'", "'-'", "'*'", "':='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COLOR_PROPERTIES", "WIDTH_HEIGHT_PROPERTIES", "IF", "ELSE", "BOX_BRACKET_OPEN", 
			"BOX_BRACKET_CLOSE", "TRUE", "FALSE", "PIXELSIZE", "PERCENTAGE", "SCALAR", 
			"COLOR", "ID_IDENT", "CLASS_IDENT", "LOWER_IDENT", "CAPITAL_IDENT", "WS", 
			"OPEN_BRACE", "CLOSE_BRACE", "SEMICOLON", "COLON", "PLUS", "MIN", "MUL", 
			"ASSIGNMENT_OPERATOR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ICSSLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ICSS.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\33\u00be\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2K\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3X\n\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\6\nr\n\n\r\n\16\ns"+
		"\3\n\3\n\3\n\3\13\6\13z\n\13\r\13\16\13{\3\13\3\13\3\f\6\f\u0081\n\f\r"+
		"\f\16\f\u0082\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\6\16\u008f\n\16"+
		"\r\16\16\16\u0090\3\17\3\17\6\17\u0095\n\17\r\17\16\17\u0096\3\20\3\20"+
		"\7\20\u009b\n\20\f\20\16\20\u009e\13\20\3\21\3\21\7\21\u00a2\n\21\f\21"+
		"\16\21\u00a5\13\21\3\22\6\22\u00a8\n\22\r\22\16\22\u00a9\3\22\3\22\3\23"+
		"\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\32\2\2\33\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\3\2"+
		"\t\3\2\62;\4\2\62;ch\5\2//\62;c|\3\2c|\3\2C\\\6\2\62;C\\aac|\5\2\13\f"+
		"\17\17\"\"\2\u00c7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\3J\3\2\2\2\5W\3\2\2\2\7Y\3"+
		"\2\2\2\t\\\3\2\2\2\13a\3\2\2\2\rc\3\2\2\2\17e\3\2\2\2\21j\3\2\2\2\23q"+
		"\3\2\2\2\25y\3\2\2\2\27\u0080\3\2\2\2\31\u0084\3\2\2\2\33\u008c\3\2\2"+
		"\2\35\u0092\3\2\2\2\37\u0098\3\2\2\2!\u009f\3\2\2\2#\u00a7\3\2\2\2%\u00ad"+
		"\3\2\2\2\'\u00af\3\2\2\2)\u00b1\3\2\2\2+\u00b3\3\2\2\2-\u00b5\3\2\2\2"+
		"/\u00b7\3\2\2\2\61\u00b9\3\2\2\2\63\u00bb\3\2\2\2\65\66\7e\2\2\66\67\7"+
		"q\2\2\678\7n\2\289\7q\2\29K\7t\2\2:;\7d\2\2;<\7c\2\2<=\7e\2\2=>\7m\2\2"+
		">?\7i\2\2?@\7t\2\2@A\7q\2\2AB\7w\2\2BC\7p\2\2CD\7f\2\2DE\7/\2\2EF\7e\2"+
		"\2FG\7q\2\2GH\7n\2\2HI\7q\2\2IK\7t\2\2J\65\3\2\2\2J:\3\2\2\2K\4\3\2\2"+
		"\2LM\7y\2\2MN\7k\2\2NO\7f\2\2OP\7v\2\2PX\7j\2\2QR\7j\2\2RS\7g\2\2ST\7"+
		"k\2\2TU\7i\2\2UV\7j\2\2VX\7v\2\2WL\3\2\2\2WQ\3\2\2\2X\6\3\2\2\2YZ\7k\2"+
		"\2Z[\7h\2\2[\b\3\2\2\2\\]\7g\2\2]^\7n\2\2^_\7u\2\2_`\7g\2\2`\n\3\2\2\2"+
		"ab\7]\2\2b\f\3\2\2\2cd\7_\2\2d\16\3\2\2\2ef\7V\2\2fg\7T\2\2gh\7W\2\2h"+
		"i\7G\2\2i\20\3\2\2\2jk\7H\2\2kl\7C\2\2lm\7N\2\2mn\7U\2\2no\7G\2\2o\22"+
		"\3\2\2\2pr\t\2\2\2qp\3\2\2\2rs\3\2\2\2sq\3\2\2\2st\3\2\2\2tu\3\2\2\2u"+
		"v\7r\2\2vw\7z\2\2w\24\3\2\2\2xz\t\2\2\2yx\3\2\2\2z{\3\2\2\2{y\3\2\2\2"+
		"{|\3\2\2\2|}\3\2\2\2}~\7\'\2\2~\26\3\2\2\2\177\u0081\t\2\2\2\u0080\177"+
		"\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083"+
		"\30\3\2\2\2\u0084\u0085\7%\2\2\u0085\u0086\t\3\2\2\u0086\u0087\t\3\2\2"+
		"\u0087\u0088\t\3\2\2\u0088\u0089\t\3\2\2\u0089\u008a\t\3\2\2\u008a\u008b"+
		"\t\3\2\2\u008b\32\3\2\2\2\u008c\u008e\7%\2\2\u008d\u008f\t\4\2\2\u008e"+
		"\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2"+
		"\2\2\u0091\34\3\2\2\2\u0092\u0094\7\60\2\2\u0093\u0095\t\4\2\2\u0094\u0093"+
		"\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\36\3\2\2\2\u0098\u009c\t\5\2\2\u0099\u009b\t\4\2\2\u009a\u0099\3\2\2"+
		"\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d "+
		"\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a3\t\6\2\2\u00a0\u00a2\t\7\2\2\u00a1"+
		"\u00a0\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a4\3\2"+
		"\2\2\u00a4\"\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6\u00a8\t\b\2\2\u00a7\u00a6"+
		"\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa"+
		"\u00ab\3\2\2\2\u00ab\u00ac\b\22\2\2\u00ac$\3\2\2\2\u00ad\u00ae\7}\2\2"+
		"\u00ae&\3\2\2\2\u00af\u00b0\7\177\2\2\u00b0(\3\2\2\2\u00b1\u00b2\7=\2"+
		"\2\u00b2*\3\2\2\2\u00b3\u00b4\7<\2\2\u00b4,\3\2\2\2\u00b5\u00b6\7-\2\2"+
		"\u00b6.\3\2\2\2\u00b7\u00b8\7/\2\2\u00b8\60\3\2\2\2\u00b9\u00ba\7,\2\2"+
		"\u00ba\62\3\2\2\2\u00bb\u00bc\7<\2\2\u00bc\u00bd\7?\2\2\u00bd\64\3\2\2"+
		"\2\r\2JWs{\u0082\u0090\u0096\u009c\u00a3\u00a9\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}