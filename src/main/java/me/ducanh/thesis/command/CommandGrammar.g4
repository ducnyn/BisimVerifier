grammar CommandGrammar;

ADD: 'add';
REMOVE: 'remove';
ALL: 'all';



CHECKSAT: 'checkSat';
CHECKDIFF: 'checkDiff';
LEFTPAR: '(';
RIGHTPAR: ')';
COMMA: ',';
QUOTE: '"';

EDGES
    :   NUMBER '-'WORD (COMMA WORD)?'>' NUMBER
    ;

WORD
    :   Letter+
    ;
NUMBER
	:	Digit+
	;

fragment Letter
    :   [a-zA-Z]
    ;

fragment Digit
    :	[0-9]
    ;
