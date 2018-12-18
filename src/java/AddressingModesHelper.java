import java.util.*;

// -------------------------------------------------------------------------------------------------------------------------------------
// ****************************************************** ADDRESSING MODES HELPER ******************************************************
// -------------------------------------------------------------------------------------------------------------------------------------

class AMHelper
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************* TYPES OF ADDRESSING MODES *************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	static enum AM
	{
		IMM,				// Immediate
		DIR,				// Direct
		REG,				// Register
		REGIND,				// Register Indirect
		REGREL,				// Register Relative
		BASIND,				// Based Indexed
		RELBASIND,			// Relative Based Indexed
		UNDEF				// Undefined
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// *************************************************** EXTERNAL DEPENDENCIES ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
		
			// MACHINE INSTRUCTION TABLE - MIT class : TO load machine instruction table.
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	/* getAddressingMode        : 
						OBJECTIVE 	- Return the addressing mode of given operand.
						INPUT  		- op : String, the operand whose addressing mode we have to find.
						OUTPUT 		- AM, addressing mode of operand. 
	*/
	static AM getAddressingMode(String op)
	{
		if (op.charAt(0)=='[')
		{
			if (op.charAt(op.length()-1)==']')
			{
				// If op is of type [...]
				
				// Count no. of '+' between [...]
				op = op.substring(1,op.length()-1);
				
				int plusCount=0;
				for (int i=0; i<op.length(); i++)
				{
					if (op.charAt(i)=='+')
						plusCount++;
				}
				
				if (plusCount==0)
				{
					
					// If no. of '+' = 0, and op is a register, then register indeirect addressing mode.
					// (eg, [CX])
					if (MIT.REG.contains(op))
						return AM.REGIND;				//REGISTER INDIRECT
				}
				
				else if (plusCount==1)
				{
					// If no. of '+' = 1, and op contains SI or DI, then register relative addressing mode.
					// (eg, [BX+SI])
					String o[] = op.split("\\+");
					if (o[1].equals("SI") || o[1].equals("DI"))
						return AM.REGREL;				//REGISTER RELATIVE
					
					// If no. of '+' = 1, and op does not contain SI or DI, then based indexed addressing mode.
					// (eg, [BX+7])
					return AM.BASIND;					//BASED INDEXD
				}
				else if (plusCount==2)
				{
					// If no. of '+' = 2, then relative based indexed addressing mode.
					// (eg, [BX+SI+7])
					return AM.RELBASIND;					//RELATIVE BASED INDEXED
				}
				else
				{
					// Error.
					Pass1.errorFlag = true;
					return AM.UNDEF;					//UNDEFINED
				}
			}
			else
			{
				// Error.
				Pass1.errorFlag = true;
				return AM.UNDEF;						//UNDEFINED
			}
		}
		
		if (MIT.REG.contains(op) || MIT.SREG.contains(op))
			// If op is a register, then register addressing mode.
			// (eg, DX)
			return AM.REG;								//REGISTER
		
		if (op.charAt(0)=='#')
			// If op starts with '#', then immediate addressing mode.
			// (eg, #6DH)
		{
			
			// HEXADECIMAL VALUE							// IMMEDIATE
			if (op.charAt(op.length()-1)=='H')
			{
				op=op.substring(1,op.length()-1);
				if (op.matches("^[0-9a-fA-F]+$"))
				{
					System.out.print("Hexadecimal ");
					return AM.IMM;
				}
			}
			
			// BINARY VALUE
			if (op.charAt(op.length()-1)=='B')
			{
				op=op.substring(1,op.length()-1);
				if (op.matches("[01]+"))
				{
					System.out.print("Binary ");
					return AM.IMM;
				}
			}
			
			// DECIMAL VALUE
			//if (op.matches("[0-9]+"))
			if(isNumeric(op.substring(1)))
			{
				op=op.substring(1,op.length());
				System.out.print("Decimal ");
				return AM.IMM;
			}
			return AM.UNDEF;							//UNDEFINED
		}
		else
			// Else direct addressing mode.
			// (eg, DATA)
			return AM.DIR;								//DIRECT
	}
	
	static boolean isNumeric(String str){
		try {
			
			int a = Integer.parseInt(str);
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}

	static final AM[] mem_am = {AM.DIR, AM.REGIND, AM.REGREL, AM.BASIND, AM.RELBASIND};			//Memory references
	static final AM[] reg_am = {AM.REG};									//Register
	static final AM[] imm_am = {AM.IMM};									//Immediate
	
	/* addressingModeIntermediate: 
						OBJECTIVE 	- An intermediate function to return the operand type based on 
						            	  addressing mode.
						INPUT	 	- OP : String, operand whose type is to be found.
						OUTPUT 		- String, operand type:
								  REG	- 	General purpose register
								  SREG	-	Segment register
								  MEM	- 	Memory
								  IMM	- 	Immediate
	*/
	static String addressingModeIntermediate(String OP)
	{
		
		// Get the addressing mode of operand.
		AM amOP = getAddressingMode(OP);
		
		// Print addressing modes.
		switch (amOP)
		{
			case IMM		: 	System.out.println(OP+" immediate "); 			break;
			case DIR		:	System.out.println(OP+" direct "); 			break;		
			case REG		:	System.out.println(OP+" register ");			break;			
			case REGIND		:	System.out.println(OP+" register indirect "); 		break;		
			case REGREL		:	System.out.println(OP+" register relative "); 		break;		
			case BASIND		:	System.out.println(OP+" based indexed "); 		break;	
			case RELBASIND		:	System.out.println(OP+" relative based indexed ");	break;
			case UNDEF		:	System.out.println(OP+" undefined "); 			break;
		}
		
		// To store operand type
		String type="";
		
		// If a memory reference, type is MEM.
		if (Arrays.asList(mem_am).contains(amOP))
				type="MEM";
		
		else if (Arrays.asList(reg_am).contains(amOP))
			{
				// If a general purpose register, type is REG.
				if (MIT.REG.contains(OP))
					type="REG";
				
				// If segment register, type is SREG.
				else if (MIT.SREG.contains(OP))
					type="SREG";
			}
		
		// If an immediate value, type is IMM.
		else if (Arrays.asList(imm_am).contains(amOP))
			type="IMM";
		
		// Else, type is undefined.
		else
			type="UNDEF";
		
		// Return type.
		return type;
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************************ END ************************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
}
