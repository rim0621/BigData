%{
#include <stdio.h>
#include <stdlib.h>

enum tnumber { 
	TEOF,IDENT,NUMBER, D_L,PLUS, MINUS, TIMES,
	 DIV,DIV2,COMMA,SEMICOLON,
	COLON, LPAREN,RPAREN,LBRACKET,	
	 RBRACKET,ASSIGN,EQUAL, NEQUAL,
	LESS,LESSE,GREAT,GREATE,DOT,FLOAT,
	 DO, IF,ELIF,ELSE,WHILE,
	IMPORT,FUNTION,RETURN,PRINT,FOR,IN,
	EXCEPT,QUOTATION,BQUOTATION,AND,SSAB,LEN ,ERROR
};


#undef yywrap
%}
letter	[a-zA-Z_]
digit	[0-9]
%%
"+"	return(PLUS);
"-"	return(MINUS);
"*"	return(TIMES);
"/"	return(DIV);
"%"	return(DIV2);
","	return(COMMA);
";"	return(SEMICOLON);
":"	return(COLON);
"("	return(LPAREN);
")"	return(RPAREN);
"["	return(LBRACKET);
"]"	return(RBRACKET);
"="	return(ASSIGN);
"=="	return(EQUAL);
"!="	return(NEQUAL);
"<"	return(LESS);
"<="	return(LESSE);
">"	return(GREAT);
">="	return(GREATE);
\.	return(DOT);
float	return(FLOAT);
do	return(DO);
if	return(IF);
elif	return(ELIF);
else	return(ELSE);
while	return(WHILE);
import	return(IMPORT);
def	return(FUNTION);
return return(RETURN);
print return(PRINT);
for	return(FOR);
in	return(IN);
except	return(EXCEPT);
"'"	return(QUOTATION);
"\""	return(BQUOTATION);
"#"	return(SSAB);
and	return(AND);
len	return(LEN);

{letter}({letter}|{digit})*	return(IDENT);
{digit}+	return(NUMBER);
{digit}+{letter}+	return(D_L);
[ \t\r]	;
.	return(ERROR);
%%



main()
{
	enum tnumber tn;

	printf("Start of lex\n");
	while ((tn = yylex()) != TEOF) {
		switch (tn) {
		case IDENT:
		case NUMBER:
			printf("\t(%2i, %s)\n", tn, yytext); break;
		case ERROR:
			printf("ERROR : [%c]\n", yytext[0]); break;
		default:
			printf("\t(%2i, 0)\n", tn);
		}
	}
}

yywrap() { printf("End of Lex\n"); return 1; }
