%{
#include <stdio.h>
#include <stdlib.h>

enum tnumber { 
	TEOF, TIDENT, TNUMBER, TPLUS, TMINUS, TTIMES,
	TCOMMA, TSEMICOLON, TCOLON, TASSIGN, TDOT, TDDOT,
	TLPAREN, TRPAREN, TLBRACKET, TRBRACKET, TEQUAL, TNEQUAL,
	TLESS, TLESSE, TGREAT, TGREATE, TBEGIN, TEND,
	TDIV, TDIV2,ARRAYSYM, CONSTSYM, DOSYM, BREAKSYM,
	IFSYM, INTSYM, CHARSYM, FLOATSYM, DOUBLESYM,
	 SWITCHSYM, CASESYM,WHILESYM,TCLASSSYM
	,TVOIDSYM,TPUBLICSYM,TPROTECTEDSYM,TOPERATORSYM,TNEWSYM,
	 TPOINTER,TRETURN,COMMENT,TERROR
};


#undef yywrap
%}
letter	[a-zA-Z_]
digit	[0-9]
%%
"+"	return(TPLUS);
"-"	return(TMINUS);
"*"	return(TTIMES);
"/"	return(TDIV);
"%"	return(TDIV2);
","	return(TCOMMA);
";"	return(TSEMICOLON);
":"	return(TCOLON);
"("	return(TLPAREN);
")"	return(TRPAREN);
"["	return(TLBRACKET);
"]"	return(TRBRACKET);
"="	return(TASSIGN);
"=="	return(TEQUAL);
"!="	return(TNEQUAL);
"<"	return(TLESS);
"<="	return(TLESSE);
">"	return(TGREAT);
">="	return(TGREATE);
\.	return(TDOT);

"{"	return(TBEGIN);
"}"	return(TEND);
float	return(FLOATSYM);
do	return(DOSYM);
if	return(IFSYM);
while	return(WHILESYM);
int	return(INTSYM);
char	return(CHARSYM);
const	return(CONSTSYM);
double	return(DOUBLESYM);
switch	return(SWITCHSYM);
case	return(CASESYM);
break	return(BREAKSYM);
class	return(TCLASSSYM);
void	return(TVOIDSYM);
public	return(TPUBLICSYM);
protected	return(TPROTECTEDSYM);
"::"	return(TOPERATORSYM);
"->"	return(TPOINTER);
new	return(TNEWSYM);
return	return(TRETURN);
"//"	return(COMMENT);
{letter}({letter}|{digit})*	return(TIDENT);
{digit}+	return(TNUMBER);

[ \t\r]	;
.	return(TERROR);
%%



main()
{
	enum tnumber tn;

	printf("Start of lex\n");
	while ((tn = yylex()) != TEOF) {
		switch (tn) {
		case TIDENT:
		case TNUMBER:
			printf("\t(%2i, %s)\n", tn, yytext); break;
		case TERROR:
			printf("ERROR : [%c]\n", yytext[0]); break;
		default:
			printf("\t(%2i, 0)\n", tn);
		}
	}
}

yywrap() { printf("End of Lex\n"); return 1; }
