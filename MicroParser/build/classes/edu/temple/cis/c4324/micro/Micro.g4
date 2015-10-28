grammar Micro;
@header {
package edu.temple.cis.c4324.micro;
}


program:            'program' ID
                    recordDeclaration*
                    (variableDeclaration ';')*
                    (procedureDeclaration | functionDeclaration)*
                    body
                    ;

body:               'begin' statement_list 'end' ';'
    ;

procedureDeclaration:   'procedure' ID '(' parameterList ')'
                        (variableDeclaration ';')*
                        body
                    ;

functionDeclaration:    'function' type ID '(' parameterList ')'
                        (variableDeclaration ';')*
                        body
                   ;

recordDeclaration:  'record' ID 'is' (variableDeclaration ';')+ 'end' ';' ;

parameterList:  parameterDeclaration (',' parameterDeclaration)*
             |
             ;

parameterDeclaration:    type ID '['']'             #arrayParamDecl
                    |    primitiveType ID           #simpleParamDecl
                    |    recordType ID              #recordParamDecl
                    ;

variableDeclaration:    type ID '[' INT ']'         #arrayVariableDecl
                   |    primitiveType ID            #simpleVariableDecl
                   |    recordType ID               #recordVariableDecl
                   ;

type:               primitiveType               #primType
    |               recordType                  #recType
    ;

primitiveType:      'int'
             |      'real'
             |      'char'
             |      'bool'
             ;

recordType: ID
          ;

statement_list:     statement*
              ;

statement:          read_statement          #read
         |          write_statement         #write
         |          assignment_statement    #assign
         |          call_statement          #call
         |          if_statement            #if_stmt
         |          while_statement         #while_stmt
         |          do_until_statement      #dountil_stmt
         |          return_statement        #return_stmt
         ;

read_statement:     'read' '(' lvalue_list ')' ';'
              ;

lvalue_list:            lvalue (',' lvalue)*
       |
       ;

write_statement:    'write' '(' expr_list ')' ';';

expr_list:          expr (',' expr)*
         |
         ;

assignment_statement:   lvalue ':=' expr ';';

lvalue: ID                  #idLvalue
      | expr '[' expr ']'   #arrayLvalue
      | expr '.' ID         #recordLvalue
      ;

call_statement:     'call' ID '(' expr_list ')' ';';

return_statement:   'return' expr? ';';

if_statement:       'if' expr 
                    'then' statement_list
                    elsif_part*
                    else_part?
                    'fi' ';'
            ;

elsif_part:     'elif' expr 'then' statement_list
          ;

else_part:      'else' statement_list
         ;

while_statement:    'while' expr 'do' statement_list 'od' ';'
               ;

do_until_statement: 'do' statement_list 'od' 'until' expr ';' ;

expr :   ID '(' expr_list ')'               #fcnCall
     |   expr '[' expr ']'                  #arrayAccess
     |   expr '.' ID                        #recordAccess
     |   op=('-'|'~'|'\u00ac') expr         #unaryop
     |   <assoc=right> expr op='**' expr    #powop
     |   expr op=('*'|'/'|'%') expr         #arithop
     |   expr op=('+'|'-') expr             #arithop
     |   expr op=('<<'|'>>'|'>>>') expr     #arithop
     |   expr op=('<'|'<='|'>='|'>') expr   #compop
     |   expr op=('='|'!=') expr            #compop
     |   expr op=('&'|'^'|'|')              #arithop
     |   expr op=('\u2227'|'\u2228') expr   #logicalop
     |   ID                                 #id
     |   INT                                #int
     |   FLOAT                              #float
     |   CHAR                               #char
     |   BOOL                               #bool
     |   '(' expr ')'                       #parens
     ;

BOOL:               'true' | 'false';
fragment LETTER:    [a-zA-Z_];
fragment DIGIT:     [0-9];
fragment ESC:       '\\\'' | '\\\\';
ID:                 LETTER (LETTER | DIGIT)*;
INT:                DIGIT+;
FLOAT:              DIGIT+ '.' DIGIT*;
CHAR:               '\''(ESC|.)*?'\'';
WS : [ \t\r\n]+ -> skip ;
