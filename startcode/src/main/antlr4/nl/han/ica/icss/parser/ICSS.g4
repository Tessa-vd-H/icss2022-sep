grammar ICSS;

//--- LEXER: ---

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
stylesheet : (stylerule | variableAssignment)* EOF;

color_literal : COLOR_LITERAL ;
pixel_literal : PIXEL_LITERAL ;
percentage_literal : PERCENTAGE_LITERAL ;

tag_selector : TAG_ID ;
id_selector : ID_REF ;
class_selector : CLASS_REF ;

selector : TAG_ID | ID_REF | CLASS_REF;

stylerule : selector '{' body '}';

variableAssignment  : VAR_ID ASSIGN expression SEMI_COLON;

body : declaration*;

declaration : prop=propertyName COLON expression SEMI_COLON;

propertyName : 'width' | 'height' | 'color' | 'background-color';

expression : color_literal | pixel_literal | percentage_literal;