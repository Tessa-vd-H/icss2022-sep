grammar ICSS;

//--- LEXER: ---
COLOR_PROPERTIES: 'color' | 'background-color';
WIDTH_HEIGHT_PROPERTIES: 'width' | 'height';

// IF/ELSE support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

// Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

// Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

// Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

// General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

// All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

// Symbols
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
// A stylesheet consists of one or more style rules or variable assignments
stylesheet: (styleRule | varAssign)* EOF;

// === Style rules ===
// A style rule contains a selector and one or more rule statements inside braces
styleRule: selector OPEN_BRACE body+ CLOSE_BRACE;

// The body of a rule can contain if-statements, declarations, or variable assignments
body: ifStmt | declaration | varAssign;

// Selectors: id, class or standard tag
selector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;

// === Declarations ===
// Declaration: defines a property followed by a value
declaration: COLOR_PROPERTIES COLON propColorValue SEMICOLON | WIDTH_HEIGHT_PROPERTIES COLON propValue SEMICOLON;

propColorValue: CAPITAL_IDENT | COLOR;

propValue: CAPITAL_IDENT | PIXELSIZE | PERCENTAGE | calc;

// === Variabeles ===
// Variables can store different types of values:
// - valueVar: COLOR | PIXELSIZE | PERCENTAGE
// - normalVar: COLOR | PIXELSIZE | PERCENTAGE | SCALAR
// - boolVar: TRUE | FALSE

varAssign: CAPITAL_IDENT ASSIGNMENT_OPERATOR varValue SEMICOLON;

varValue: CAPITAL_IDENT | COLOR | PIXELSIZE | PERCENTAGE | SCALAR | TRUE | FALSE | calc;

// === If/Else statements ===
// Conditional blocks can only use boolean values or boolean variables
ifStmt: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE body+ CLOSE_BRACE (elseStmt)?;

elseStmt: ELSE OPEN_BRACE body+ CLOSE_BRACE;

// Expressions can be booleans or variables containing a boolean value
expression: CAPITAL_IDENT | TRUE | FALSE;

// === Calculations ===
// Calculations allow arithmetic with pixel or percentage values
calc: calcPixel | calcPercent;

calcPixel: calcPixel MUL scalar | scalar MUL calcPixel | calcPixel (PLUS | MIN) calcPixel | PIXELSIZE | CAPITAL_IDENT;

calcPercent: calcPercent MUL scalar | scalar MUL calcPercent | calcPercent (PLUS | MIN) calcPercent | PERCENTAGE | CAPITAL_IDENT;

scalar: SCALAR;