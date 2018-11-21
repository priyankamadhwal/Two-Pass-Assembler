import java.util.*;

// ---------------------------------------------------------------------------------------------------------------------------------------------------------
// ***************************************************************** MACHINE INSTRUCTION TABLE *************************************************************
// ---------------------------------------------------------------------------------------------------------------------------------------------------------

class MachineInstructionTable

{
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ******************************************************************* INSTRUCTIONS *******************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	// Contains instruction mnemonics.
	static final ArrayList<String> INSTRUCTIONS = new ArrayList<String>(Arrays.asList(
																						"MOV", 
																						"LDS", 
																						"LEA",
																						"LES",
																						"XCHG", 
																						"ADD", 
																						"ADC",
																						"SUB",
																						"SBB",
																						"CMP",
																						"INC",
																						"DEC",
																						"MUL",
																						"IMUL",
																						"DIV",
																						"IDIV",
																						"DAA",
																						"DAS"
																					));
																				
	// ----------------------------------------------------------------------------------------------------------------------------------------------------																			
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ******************************************************************* OPERAND TYPES ******************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	// 1. REG- General Purpose Registers
	static final ArrayList<String> REG = new ArrayList<String>(Arrays.asList(
																				"AX",
																				"BX", 
																				"CX", 
																				"DX", 
																				"AH", 
																				"AL", 
																				"BL", 
																				"BH", 
																				"CH", 
																				"CL", 
																				"DH", 
																				"DL", 
																				"DI", 
																				"SI", 
																				"BP", 
																				"SP"
																			));
	
	// 2. SREG- Segment Registers
	static final ArrayList<String> SREG = new ArrayList<String>(Arrays.asList( 
																				"DS", 
																				"ES", 
																				"SS", 
																				"CS"		
																			)); 
	// 3. MEM- Memory	: [BX], [BX+SI+7], variable, etc
	
	// 4. IMM- Immediate: 5, -24, 3FH, 10001101B, etc 
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ********************************************************************* VARIABLES ********************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	static boolean w=false;						// If w=true, then word instruction. If w=false, then byte instruction.
	
	static AddressingModesHelper am;			// Load Addressing Mode Helper class to determine addressing modes of the instructions.
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// *********************************************************************** METHODS ********************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	/* searchInstructionTable   : 
									OBJECTIVE 	- Search a given token in Instruction table.
									INPUT  		- currToken : String, the token which is to be searched in machine instruction table.
									OUTPUT 		- boolean, true if currToken exists in instruction table, otherwise false. 
	*/
	public static boolean searchInstructionTable(String currToken)
	{
		return INSTRUCTIONS.contains(currToken);
	}

	
	/* getInstructionSize   : 
									OBJECTIVE 	- Returns the size of an instruction. Returns -1 if instruction not valid.
									INPUTS  	- 
												destOP		: String, the destination operand.
												srcOP		: String, the source operand.
									OUTPUT 		- int, size of the instruction. (-1, if not valid.)
	*/
	static int getInstructionSize(String inst, String destOP, String srcOP)
	{
		// Initially, size is -1.
		int size=-1;
		
		// Find the addressing mode of destination operand and source operand.
		String dest="";
		String src="";
		if (!destOP.equals(""))
			dest = AddressingModesHelper.addressingModeIntermediate(destOP);
		if (!srcOP.equals(""))
			src = AddressingModesHelper.addressingModeIntermediate(srcOP);
		
		// If the source/destination operand addressing mode is undefined, then set the error flag.
		if (dest.equals("UNDEF") || src.equals("UNDEF"))
		{
			Pass1.errorMsg = "Undefined addressing mode";
			Pass1.errorFlag = true;
		}
		
		// If addressing mode is valid, then calculate instruction size (in bytes).
		else
		{
			switch(inst)
			{
							/* ACC (accumulator)- AX,AL*/
							
				case "MOV" :
							/*
								Valid instruction formats for MOV instruction and their size:
								1. REG, MEM		// If REG is ACC, size=3. Else, size=4.
								2. MEM, REG		// If REG is ACC, size=3. Else, size=4.
								3. REG, REG		// size=4
								4. MEM, IMM		// If w, size=6. Else, size=5.
								5. REG, IMM		// If w, size=3. Else, size=2.
								6. SREG, MEM	// size=4.
								7. MEM, SREG	// size=4.
								8. REG, SREG	// If REG is ACC, size=2. Else, size=4.
								9. SREG, REG 	// If REG is ACC, size=2. Else, size=4.
							*/
							if (dest.equals("REG") && src.equals("MEM"))					// 1
							{
								if (destOP.equals("AX") || destOP.equals("AL"))
									size=3;
								else
									size=4;
							}
							else if (dest.equals("MEM") && src.equals("REG"))				// 2
							{
								if (srcOP.equals("AX") || srcOP.equals("AL"))
									size=3;
								else
									size=4;
							}
							else if (dest.equals("REG") && src.equals("REG"))				// 3
							{
								size=4;
							}
							else if (dest.equals("MEM") && src.equals("IMM"))				// 4
							{
								if (w)
									size=6;
								else
									size=5;
							}
							else if (dest.equals("REG") && src.equals("IMM"))				// 5
							{
								if (w)
									size=3;
								else
									size=2;
							}
							else if (dest.equals("SREG") && src.equals("MEM"))				// 6
							{
								size=4;
							}
							else if (dest.equals("MEM") && src.equals("SREG"))				// 7
							{
								size=4;
							}
							else if (dest.equals("REG") && src.equals("SREG"))				// 8
							{
								if (destOP.equals("AX") || destOP.equals("AL"))
									size=2;
								else
									size=4;
							}
							else if (dest.equals("SREG") && src.equals("REG"))				// 9
							{
								if (srcOP.equals("AX") || srcOP.equals("AL"))
									size=2;
								else
									size=4;
							}
							break;
							 
				case "LDS"	:
				
				case "LEA"	:
				
				case "LES"	:
							/*
								Valid instruction formats for LDS, LEA and LES instructions and their size:
								1. REG, MEM		// size=4.
							*/
							if (dest.equals("REG") && src.equals("MEM"))					// 1
								size=4;
							break;
				
				case "XCHG":
							/*
								Valid instruction formats XCHG instruction and their size:
								1. REG, MEM		// If REG is ACC, size=1. Else, size=4.
								2. MEM, REG		// If REG is ACC, size=1. Else, size=4.
								3. REG, REG 	// If REG is ACC, size=1. Else, size=4.
							*/
								
							if (
									(dest.equals("REG")  && src.equals("MEM"))  ||			// 1
									(dest.equals("MEM")  && src.equals("REG"))  ||			// 2
									(dest.equals("REG")  && src.equals("REG"))				// 3
								)
								{
									if (destOP.equals("AX") || destOP.equals("AL") || srcOP.equals("AX") || srcOP.equals("AL"))
										size=1;
									else
										size=4;
								}
							break;
								
				case "ADD"	:
				
				case "ADC"	:
				
				case "SUB"	:
				
				case "SBB"	:
				
				case "CMP"	:
							/*
								Valid instruction formats for ADD, ADC, SUB, SBB and CMP instructions:
								1. REG, MEM		// size=4.
								2. MEM, REG		// size=4.
								3. REG, REG		// size=4.
								4. MEM, IMM		// If w, size=6. Else, size=5.
								5. REG, IMM		// If REG is ACC and w, size=3. Else if REG is ACC and !w, size=2. Else, if REG is !ACC and w, size=6. Else, size=5.
							*/
								
							if (dest.equals("REG") && src.equals("MEM"))					// 1
							{
								size=4;
							}
							else if (dest.equals("MEM") && src.equals("REG"))				// 2
							{
									size=4;
							}
							else if (dest.equals("REG") && src.equals("REG"))				// 3
							{
								size=4;
							}
							else if (dest.equals("MEM") && src.equals("IMM"))				// 4
							{
								if (w)
									size=6;
								else
									size=5;
							}
							else if (dest.equals("REG") && src.equals("IMM"))				// 5
							{
								if (destOP.equals("AX") || destOP.equals("AL"))
								{
									if (w)
										size=3;
									else
										size=2;
								}
								else
								{
									if (w)
										size=6;
									else
										size=5;
								}
							}
							break;
								
				case "INC"	:
				
				case "DEC"	:
							/*
								Valid instruction formats for INC and DEC instructions and their size:
								1. REG				// size=1
								2. MEM				// size=4
							*/
							if (src.equals(""))
							{
								if (dest.equals("REG"))										// 1
									size=1;
								else if (dest.equals("MEM"))								// 2
									size=4;
							}
							break;
				
				case "NEG"	:
				
				case "MUL"	:
				
				case "IMUL"	:
				
				case "DIV" 	:
				
				case "IDIV"	:
							/*
								Valid instruction formats for NEG, MUL, IMUL, DIV and IDIV instructions and their size:
								1. REG				// size=4
								2. MEM				// size=4
							*/
								
							if (
									(dest.equals("REG") || dest.equals("MEM")) &&			// 1
									(src.equals(""))										// 2
								)
									size=4;
								break;
								
				case "DAA"	:
				
				case "DAS"	:	
							/*
								Valid instruction formats DAA and DAS instructions and their size:
								No operands			// size=1
							*/
								
							if ( destOP.equals("") && srcOP.equals(""))
								size=1;
							break;
				
			}
			
			
		}
		
		// Return size.
		return size;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ************************************************************************ END ***********************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
}