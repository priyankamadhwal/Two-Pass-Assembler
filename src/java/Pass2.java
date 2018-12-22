import javax.swing.*;
import java.io.*;
import java.util.*;

// -------------------------------------------------------------------------------------------------------------------------------------
// ********************************************************** ASSEMBLER PASS 2 *********************************************************
// -------------------------------------------------------------------------------------------------------------------------------------

class Pass2
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// *************************************************** EXTERNAL DEPENDENCIES ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
		
			// MACHINE INSTRUCTION TABLE - MIT class : To load machine instruction table.
			// ASSEMBLER DIRECTIVE TABLE - ADT class : To load assembler directive table.
			// SYMBOL TABLE 	     - ST class  : To load symbol table (initially empty).
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** VARIABLES ********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
		
	static ArrayList<Listing> LISTING = new ArrayList<Listing>(); 	// ASSEMBLER LISTING (To be generated)
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	
	/* start    		    : 
						OBJECTIVE 	- Start Pass 2 to generate an assembler listing by scanning the given file and converting source code to machine code.
						INPUT  		- asmFile : FileReader, a .asm file on which Pass 1 is to be performed.
						OUTPUT 		- Assembler Listing 
	*/
	public static void start(FileReader asmFile)
	{
		try
		{
			// Create a buffering character-input stream from the asm file.
			BufferedReader asmBr = new BufferedReader(asmFile);
			
			
			// A list to store tokens.
			ArrayList<String> tokens = new ArrayList<String>();
			
			String currLine="";					// Holds the current line.
			String currToken=""; 				// Holds the current token.
			int i=0;			     		// Holds the current line no.
			
			// Clear location count and segment no.
			Main.locationCounter = 0;
			
			// Read program statements, line-by-line.
			readStatements:
				while((currLine = asmBr.readLine()) != null)	
				{
					Listing newLine = new Listing(); 
				
					// Increment line no.
					i++;
					newLine.lineNo = i;
					
					// Tokenize current line and store tokens.
					newLine.srcCode = currLine;
					tokens=Pass1.tokenize(currLine);
					
					// Read tokens, one-by-one and determine and convert instructions to machine code.
					
						if (tokens.size() == 0)
						{
							newLine.loc="";
							newLine.machineCode="";
							System.out.println();
						}
						
					readTokens:
						for (int index=0; index<tokens.size(); index++)
						{
							// Store current token.
							currToken=tokens.get(index).toUpperCase();
							
							if (ADT.searchDirectiveTable(currToken))
							{
								// The token is an assembler directive.
								
								// Read next tokens
								ArrayList<String> nextTokens = new ArrayList<String>();
								index++;
								while(index<tokens.size())
								{
									nextTokens.add(tokens.get(index).toUpperCase());
									index++;
								}
								
								/* performDirectiveFunction() reserve space in the memory for data directives and convert operands to machine code.*/
								   
								int fx_id = ADT.performDirectiveFunctionPass2(currToken, newLine, nextTokens);
								
								/*
									Check fx_id:
									0 - Symbol is added successfully, read next token.
									1 - Read next line.
									2 - Stop Pass 1 and go to pass 2.
									3 - Duplicate error.
									4 - Mismatch error.
								*/
								switch(fx_id)
								{
									case 0 : break readTokens;
									case 1 : break readTokens;
									case 2 : break readStatements;
									case 3 : Main.errorMsg = "Line "+i+" : Invalid operands."; break readStatements;
									case 4 : Main.errorMsg = "Line "+i+" : Invalid operands."; break readStatements;
									default: break readTokens;
								}
							}
							
							else if (MIT.searchInstructionTable(currToken))
							{
								// The token is an instruction.
								
								// Store source and destination operands, if any.
								String dest, src;
								
								// Destination operand.
								if (++index<tokens.size())
									dest=tokens.get(index).toUpperCase();
								else
									dest="";
								
								// Operands are separated by commas. So, skip the comma.
								++index;
								
								// Source operand.
								if (++index<tokens.size())
									src=tokens.get(index).toUpperCase();
								else
									src="";
								
								String destOP="";
								String srcOP="";
								if (!dest.equals(""))
									destOP = AMHelper.getOperandType(dest);
								else
									destOP = "n";
								if (!src.equals(""))
									srcOP = AMHelper.getOperandType(src);
								else
									srcOP = "n";
								
								// Get the size of instruction.
								String code = MIT.getMachineInstructionCode(currToken,destOP,srcOP, dest, src);
								
								// If size=-1, instruction is not valid.
								if (code.equals(""))
								{
									if (srcOP.equals("ACC") || destOP.equals("ACC"))
									{
								if (srcOP.equals("ACC"))
									srcOP = "REG";
								if (destOP.equals("ACC"))
									destOP = "REG";
								code = MIT.getMachineInstructionCode(currToken,destOP,srcOP, dest, src);
									}
									if (code.equals(""))
									{
									Main.errorMsg="Line "+i+" : Invalid instruction format.";
									Main.errorFlag = true;
									}
									else
									{
										newLine.loc = Main.locationCounter+"";
										Main.locationCounter += MIT.getInstructionSize(currToken,destOP,srcOP, dest, src);
										newLine.machineCode = code;
									}
								}
								else
									// Add size of instruction to location counter.
									{
										newLine.loc = Main.locationCounter+"";
										Main.locationCounter += MIT.getInstructionSize(currToken,destOP,srcOP, dest, src);
										newLine.machineCode = code;
									}
							}
							
						}
						
				LISTING.add(newLine);
				System.out.println(newLine);
				}
			// If there is an error in the program, print error message.
			if (Main.errorFlag)
				JOptionPane.showMessageDialog(null, Main.errorMsg, "Pass1 Error", 1);
			
			// If there is no error, print assembler listing.
			else
			{
				// Assembler listing generated successfully.
				// Do nothing.
			}
		}
		
		// Handle exceptions
		catch (Exception e)
		{
			System.out.println(e);
		}
		
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************************ END ************************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
}