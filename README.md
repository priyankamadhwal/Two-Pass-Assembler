# First	Pass

This	is	an	implementation	of	the	first	pass	of	a	two-pass	assembler	for
**Intel	8086	Microprocessor** .	It	builds	the	Symbol	Table	for	a	 **.asm** file
of	an	assembly	program.

## Getting	Started

Follow	these	instructions	to	run	the	software:
  
1. Add your .asm program file in the folder named docs.  
![](https://github.com/priyankamadhwal/Two-Pass-Assembler/blob/master/resources/img1.PNG)  
![](https://github.com/priyankamadhwal/Two-Pass-Assembler/blob/master/resources/img2.PNG)  
  
2. Open firstpass.jar.  
![](https://github.com/priyankamadhwal/Two-Pass-Assembler/blob/master/resources/img3.PNG)  
  
3. Enter the name of your file. (eg. swap.asm)   
**(You can use the filename of files already present in docs folder)**    
![](https://github.com/priyankamadhwal/Two-Pass-Assembler/blob/master/resources/img4.png)  
  
4. Click on **START PASS 1**.   
  
5. If there are no errors, click on **SHOW SYMBOL TABLE**.  
![](https://github.com/priyankamadhwal/Two-Pass-Assembler/blob/master/resources/img5.PNG)  
  
The	symbol	table	gets	displayed.  
![](https://github.com/priyankamadhwal/Two-Pass-Assembler/blob/master/resources/img6.PNG)  
  
6. Click on **LOAD ANOTHER FILE** to build symbol table for another file.  
  
    
To view source code, open folder named **src**.  
It	contains	6	files:  
```
1. Main.java
2. Pass1.java
3. AddressingModesHelper.java
4. AssemblerDirectiveTable.java
5. MachineInstructionTable.java
6. SymbolTable.java
```  
  
## Prerequisites
  
- JAVA	Runtime	Environment
  
## Supported	Instructions
  
Programs that include	one	or more	of the	following	instructions are supported.  

- MOV    
- LDS  
- LEA  
- LES  
- XCHG  
- ADD
- ADC
- SUB
- SBB
- CMP
- INC
- DEC
- MUL
- IMUL
- DIV
- IDIV
- DAA
- DAS

## Supported	Directives

Programs	that	include	one	or	more	of the	following	directives	are supported.
  
- DB
- DW
- DD
- ASSUME
- END
- ENDS
- SEGMENT
- HLT
  
## Supported	Addressing	Modes

Programs	that	include	one	or	more	of	the	following	addressing	modes are	supported.  
  
- Immediate	              -	(	eg.	ADD	AX,	100H	)
- Direct	                -	(	eg.	MOV	AX,	[1592H]	)
- Register	              -	(	eg.	ADD	BX,	AX	)
- Register	Indirect	    -	(	eg.	MOV	AX,	[BX]	)
- Register	Relative	    -	(	eg.	ADD	AX,	[BX	+	SI]	)
- Based	Indexed	          -	(	eg.	MOV	AX,	[BX	+	7]	)
- Relative	Based	Indexed	-	(	eg.	ADD	AX,	[BX	+	SI	+	7]	)  

