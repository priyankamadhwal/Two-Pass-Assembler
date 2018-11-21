; OBJECTIVE : To evaluate the expression- X+Y+24H-Z,
;             where X, Y and Z are byte variabls.
; APPROACH  : Use the binary addition and subtraction instructions.

; Data segment begins.
DATA SEGMENT
    
    ; Variable initialization.
    X       DB  15H
    Y       DB  30H
    Z       DB  10H
    ARR     DW  11H, 22H, 33H, 44H
    result  DB  ?
    
; Data segment ends.
DATA ENDS

; Code segment begins.
CODE SEGMENT      
    
    ; Binding registers with segments.
    ASSUME DS:DATA  CS:CODE
    
    START:
    ; Move the address of DATA segment to DS register.
    L1 : MOV AX  ,   DATA
    l2 : MOV DS  ,   AX
    
    ;Evaluating the expression.
    L3 : MOV AL      ,   X       ; AL <- X
    L4 : ADD AL      ,   Y       ; AL <- AL + Y
    L5 : ADD AL      ,   24H     ; AL <- AL + 24H
    L6 : SUB AL      ,   Z       ; AL <- AL - Z
    L7 : MOV result  ,   AL      ; result <- AL
    
; Code segment ends.
CODE ENDS
END START