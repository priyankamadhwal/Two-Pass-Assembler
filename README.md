# First	Pass

This	is	an	implementation	of	the	first	pass	of	a	two-pass	assembler	for
**Intel	8086	Microprocessor** .	It	builds	the	Symbol	Table	for	a	 **.asm** file
of	an	assembly	program.

## Getting	Started

Follow	these	instructions	to	run	the	softwware:

```
1 .	Add	your	 .asm program	file	in	the	folder	named	 docs.
```
```
2 .	Open	 firstpass.jar.
```

3 .	Enter	the	name	of	your	file.	(eg.	swap.asm)
( **You	can	use	the	filename	of	files	already	present	in	docs
folder** )

4 .	Click	on	 **START	PASS	**.

5 .	If	there	are	no	errors,	click	on	 **SHOW	SYMBOL	TABLE**.

```
The	symbol	table	gets	displayed.
```

```
6 .	Click	on	 LOAD	ANOTHER	FILE to	build	symbol	table	for
another	file.
```
To	view	source	code	open	folder	named	 **src**.
It	contains	6	files:

```
Main.java
Pass1.java
AddressingModesHelper.java
AssemblerDirectiveTable.java
MachineInstructionTable.java
SymbolTable.java
```
## Prerequisites

```
JAVA	Runtime	Environment
```
## Supported	Instructions

Programs	that	include	one	or	more	of	the	following	instructions	are
supported.

```
MOV
```

### LDS

### LEA

### LES

### XCHG

### ADD

### ADC

### SUB

### SBB

### CMP

### INC

### DEC

### MUL

### IMUL

### DIV

### IDIV

### DAA

### DAS

## Supported	Directives

Programs	that	include	one	or	more	of	the	following	directives	are
supported.

```
DB
DW
DD
ASSUME
END
```

### ENDS

### SEGMENT

### HLT

## Supported	Addressing	Modes

Programs	that	include	one	or	more	of	the	following	addressing	modes
are	supported.

```
Immediate	-	(	eg.	ADD	AX,	100H	)
Direct	-	(	eg.	MOV	AX,	[1592H]	)
Register	-	(	eg.	ADD	BX,	AX	)
Register	Indirect	-	(	eg.	MOV	AX,	[BX]	)
Register	Relative	-	(	eg.	ADD	AX,	[BX	+	SI]	)
Based	Indexed	-	(	eg.	MOV	AX,	[BX	+	7]	)
Relative	Based	Indexed	-	(	eg.	ADD	AX,	[BX	+	SI	+	7]	)
```

