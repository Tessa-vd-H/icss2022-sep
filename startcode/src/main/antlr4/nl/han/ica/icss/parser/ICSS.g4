grammar ICSS;

//--- LEXER: ---

COLOR_PROPERTIES: 'color' | 'background-color';
WIDTH_HEIGHT_PROPERTIES: 'width' | 'height';

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
// stylesheet bestaat uit 1 of meerdere styleblocks
stylesheet: styleBlock+ EOF;

// een styleblock kan een regel of variabele-toekenning zijn
styleBlock: styleRule | varAssign;

// === Style rules ===
// een stylerule bevat een selector en 1 of meer regels binnen accolades
styleRule: selector OPEN_BRACE ruleType* CLOSE_BRACE;

// een ruletype kan een declaratie, variabele, of if-statement zijn
ruleType: ifStmt | declaration | varAssign;

// selectors: id, class of gewone tag
selector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;

// === Declaraties ===
// declaratie: een property gevolgd door een waarde
declaration: COLOR_PROPERTIES COLON propColorValue SEMICOLON | WIDTH_HEIGHT_PROPERTIES COLON propValue SEMICOLON;

propColorValue: var | COLOR;

propValue: var | PIXELSIZE | PERCENTAGE | calc;

// === Variabelen ===
// Variabelen kunnen verschillende waarden bevatten:
// - valueVar: COLOR | PIXELSIZE | PERCENTAGE
// - normalVar: COLOR | PIXELSIZE | PERCENTAGE | SCALAR
// - boolVar: TRUE | FALSE

varAssign: var ASSIGNMENT_OPERATOR varValue SEMICOLON;
var: CAPITAL_IDENT;
varValue: COLOR | PIXELSIZE | PERCENTAGE | SCALAR | TRUE | FALSE | calc;

// === If/Else statements ===
// Conditionele blokken kunnen alleen booleans of boolVars gebruiken
ifStmt: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE body CLOSE_BRACE (elseStmt)?;

elseStmt: ELSE OPEN_BRACE body CLOSE_BRACE;

// expressies mogen een bool of variabele met een bool-waarde zijn
expression: var | TRUE | FALSE;

// body van if- en else-blokken
body: ruleType*; // een body mag leeg zijn

// === Berekeningen ===
calc: calcPixel | calcPercent;

calcPixel: calcPixel MUL SCALAR | SCALAR MUL calcPixel | calcPixel (PLUS | MIN) calcPixel | PIXELSIZE | var;

calcPercent: calcPercent MUL SCALAR | SCALAR MUL calcPercent | calcPercent (PLUS | MIN) calcPercent | PERCENTAGE | var;