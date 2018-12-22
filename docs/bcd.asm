; OBJECTIVE: To find the sum of two BCD numbers.

; Data segment begins.
DATA SEGMENT 
    
    NUM1 DB #05D
    NUM2 DB #06D
    RESULT DB #0D   
    
; Data segment ends.
DATA ENDS

; Code segment begins.
CODE SEGMENT   
    
    ; Bind the segments with the corresponding registers.
    ASSUME DS:DATA CS:CODE 
    
    START:      
        
        ; Move the address of DATA segment to DS register.
        L1 : MOV AX,DATA
        L2 : MOV DS,AX 
        
        ; Perform addition.
        L3 : MOV AL,NUM1
        L4 : ADD AL,NUM2        
        
        ; Decimal adjustment addition.
        L5 : DAA
        L6 : MOV RESULT,AL

; Code segment ends.        
CODE ENDS     

; Program ends.
END START