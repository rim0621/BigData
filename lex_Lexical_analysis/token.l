%{
#include <stdio.h>
#include <stdlib.h>

enum tnumber{
	ERROR, INT, REAL, PLUS, MINUS, MUL,
	 DIV, REM, EXP, LPAREN, RPAREN, SQRT
	, SIN, COS, ABS,VAR,TEOF
};



#undef yywrap
%}
letter	[a-zA-Z_]
digit	[0-9]
%%
{digit}+	return(INT);
{digit}*\.{digit}*	return(REAL);
"+"	return(PLUS);
"-"	return(MINUS);
"*"	return(MUL);
"/"	return(DIV);
"%"	return(REM);
"^"	return(EXP);
"("	return(LPAREN);
")"	return(RPAREN);
"sqrt"	return(SQRT);
"sin"	return(SIN);
"cos"	return(COS);
"abs"	return(ABS);
"x"	return(VAR);
"y"	return(VAR);
"var1"	return(VAR);
"var2"	return(VAR);
.	return(ERROR);
%%
main()
{
	enum tnumber tn;

	printf("Start of lex\n");
	while ((tn = yylex()) != TEOF) {
		switch (tn) {
		case REAL:
		case INT:
			printf("\t(%2i, %s)\n", tn, yytext); break;
		case ERROR:
			printf("error:%c ", yytext[0]); break;
		default:
			printf("\t(%2i, 0)\n", tn);
		}
	}
}

yywrap() { printf("End of Lex\n"); return 1; }

