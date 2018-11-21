import javax.swing.*;
import java.io.*;
import java.util.*;

// ---------------------------------------------------------------------------------------------------------------------------------------------------------
// ********************************************************************** ASSEMBLER PASS 1 *****************************************************************
// ---------------------------------------------------------------------------------------------------------------------------------------------------------

class Pass1
{
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ************************************************************* VARIABLES INITIALIZATION *************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	static MachineInstructionTable mit;							// LOAD MACHINE INSTRUCTION TABLE
	static AssemblerDirectiveTable adt;							// LOAD ASSEMBLER DIRECTIVE TABLE
	static SymbolTable st;										// LOAD SYMBOL TABLE (Initially empty)
		
	static ArrayList<Symbol> SYMBOLS = new ArrayList<Symbol>(); // SYMBOL TABLE (To be generated)
	
	static String currSegment="";								// Holds the current segment name.
	static int segNo=0;											// Holds the segment offset.
	
	static int locationCounter = 0;								// Location Counter- To assign offset to program statements.
	
	static boolean errorFlag = false;							// This flag is set whenever there is an error in the program.+
	static String errorMsg="";									// Holds an error message, if any.
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// *********************************************************************** METHODS ********************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	
	/* tokenize   : 
					OBJECTIVE 	- This method tokenizes a given statement. It ignores all the comments and white spaces.
					INPUT  		- currStatement : String, the statement which is to be tokenized.
					OUTPUT 		- ArrayList<String>, An ArrayList of the tokens in the given statement. 
	*/
	static ArrayList<String> tokenize(String currStatement)
	{
		// Split current statement by spaces.
		String s[]=currStatement.split(" ");
		
		// Declare an ArrayList to store the tokens.
		ArrayList<String> tokens = new ArrayList<String>();
		
		// Read the elements after splitting.
		for (String currToken : s)		
		{
			
			// Ignore empty strings due to extra whitespaces.
			if (currToken.equals(""))													
				continue;
			
			// Ignore comments.
			else if (currToken.equals(";") || currToken.charAt(0)==';')			
				break;
			
			// Separate operand from comment
			else if (currToken.length()>1 && currToken.contains(";"))
			{
				String tmp[] = currToken.split(";");
				tokens.add(tmp[0]);
			}
			
			// Seperate the commas
			// Comma at end
			else if (currToken.length()>1 && currToken.charAt(currToken.length()-1)==',')
			{
				tokens.add(currToken.substring(0,currToken.length()-1));
				tokens.add(",");
			}
			// Comma at start
			else if (currToken.length()>1 && currToken.charAt(0)==',')
			{
				tokens.add(",");
				tokens.add(currToken.substring(1,currToken.length()));
			}
			// Comma in between
			else if (currToken.length()>1 && currToken.contains(","))
			{
				String tmp[] = currToken.split(",");
				for (String tmpToken : tmp)
				{
					tokens.add(tmpToken);
					tokens.add(",");
				}
				tokens.remove(tokens.size()-1);
			}
			
			// Separate the colons
			// Colons at end
			else if (currToken.length()>1 && currToken.charAt(currToken.length()-1)==':')
			{
				tokens.add(currToken.substring(0,currToken.length()-1));
				tokens.add(":");
			}
			// Colons at start
			else if (currToken.length()>1 && currToken.charAt(0)==':')
			{
				tokens.add(":");
				tokens.add(currToken.substring(1,currToken.length()));
			}
			// Comma in between
			else if (currToken.length()>1 && currToken.contains(":"))
			{
				String tmp[] = currToken.split(":");
				for (String tmpToken : tmp)
				{
					tokens.add(tmpToken);
					tokens.add(":");
				}
				tokens.remove(tokens.size()-1);
			}
			
			// Add the element in Tokens list.
			else
				tokens.add(currToken);
		}
		
		//Return Tokens list.
		return tokens;
	}
	
	/* start    : 
					OBJECTIVE 	- Start Pass 1 to generate a symbol table by scanning the give file and determining symbol's name, offset, size, type and segment.
					INPUT  		- asmFile : FileReader, a .asm file on which Pass 1 is to be performed.
					OUTPUT 		- Symbol table 
	*/
	public static void start(FileReader asmFile)
	{
		try
		{
			// Create a buffering character-input stream from the asm file.
			BufferedReader asmBr = new BufferedReader(asmFile);
			
			// A list to store tokens.
			ArrayList<String> tokens = new ArrayList<String>();
			
			String currLine=""; 				// Holds current line being read.
			String prevToken="";				// Holds the previous token.
			String currToken=""; 				// Holds the current token.
			int i=0;			     			// Holds the current line no.
			
			// Clear location count and segment no.
			locationCounter = 0;
			segNo = 0;
			
			System.out.println("TOKENS : ");
			// Read program statements, line-by-line.
			readStatements:
				while((currLine = asmBr.readLine()) != null)	
				{
					// Increment line no.
					i++;
					
					// Tokenize current line and store tokens.
					tokens=tokenize(currLine);
					System.out.println(tokens);
					
					// Read tokens, one-by-one and determine and store symbols in symbol table.
					readTokens:
						for (int index=0; index<tokens.size(); index++)
						{
							// Store current token.
							currToken=tokens.get(index).toUpperCase();
					
							// Search the token in Machine Instruction Table.
							if (MachineInstructionTable.searchInstructionTable(currToken))
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
								
								// Get the size of instruction.
								int size = MachineInstructionTable.getInstructionSize(currToken,dest,src);
								
								// If size=-1, instruction is not valid.
								if (size==-1)
								{
									errorMsg="Line "+i+" : Invalid instruction format.";
									errorFlag = true;
								}
								else
									// Add size of instruction to location counter.
									locationCounter+=size;
							}
							
							// Token is not found in Machine Instruction Table.
							
							// Search in directive table.
							else if (adt.searchDirectiveTable(currToken))
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
								
								/* performDirectiveFunction() stores variables and segment names into segment table after checking directive type 
								   and updates location counter accordingly. 
								   It returns an id to direct the assembler to perform further action.*/
								   
								int fx_id = adt.performDirectiveFunction(currToken, prevToken, nextTokens);
								
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
									case 3 : errorMsg = "Line "+i+" : Duplicate declaration of "+prevToken+"."; break readStatements;
									case 4 : errorMsg = "Line "+i+" : Mismatched- "+prevToken+" ENDS."; break readStatements;
									default: break readTokens;
								}
							
							}
							
							// Token is not found in Assembler Directive Table.
							
							// If current token is a colon, then previous token was a label.
							else if (currToken.equals(":"))
							{
								// A  label is found.
								// Add it to symbol table.
								
								// If symbol is not added successfully, set the error flag and exit.
								if (!st.addToSymbolTable(prevToken, locationCounter, -1, "LABEL", currSegment))
								{
									errorMsg = "Line "+i+" : Duplicate declaration of "+prevToken+".";
									break readStatements;
								}
							}
							
							// If current token is not in machine instruction table or assembler directive table, it may be a symbol.
							// Store current token as previous token and in the next loop, check if its a valid symbol and then store in symbol table.
							else
								prevToken = currToken;
							
							if (errorFlag)
								break readStatements;
						}
				}
			
			// If there is an error in the program, print error message.
			if (errorFlag)
				JOptionPane.showMessageDialog(null, errorMsg, "Pass1 Error", 1);
			
			// If there is no error, print symbol table.
			else
			{
				// Symbol table generated successfully.
				// Do nothing.
				/*for (Symbol sym : SYMBOLS)
				{
					System.out.println(sym.name+" "+sym.segment+" "+sym.type+" "+sym.offset+" "+sym.size);
				}*/
			}
		}
		
		// Handle exceptions
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ************************************************************************ END ***********************************************************************
	// ----------------------------------------------------------------------------------------------------------------------------------------------------
}