; OBJECTIVE : To swap two numbers.
; APPROACH  : Use MOV operation.

; Variables initialization.
DATA SEGMENT  

    NUM1 DB 20H
    NUM2 DB 45H
    
DATA ENDS 

CODE SEGMENT  
    
    ASSUME DS:DATA CS:CODE
    
    START: 
    
    ; MOVING ADDRESS OF DATA TO DS(DATA SEGMENT)
     
    X : MOV AX  , DATA
    Y : MOV DS  , AX
    
    Z : MOV AL  , NUM1
    A : MOV BL  , NUM2
    B : MOV NUM1, BL  
    C :MOV NUM2, AL
     
CODE ENDS

END START