import java.util.*;

// -------------------------------------------------------------------------------------------------------------------------------------
// ***************************************************** ASSEMBLER DIRECTIVE TABLE *****************************************************
// -------------------------------------------------------------------------------------------------------------------------------------

class ADT
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************* DIRECTIVES ********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// Contains directive mnemonics.
	static final ArrayList<String> DIRECTIVES = new ArrayList<String>(Arrays.asList(
												"DB", 
												"DW", 
												"DD",
												"ASSUME", 
												"END", 
												"ENDS", 
												"SEGMENT", 
												"HLT"
											));
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// *************************************************** EXTERNAL DEPENDENCIES ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
		
			// SYMBOL TABLE - ST class : To load symbol table to add symbols, if any.
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	/* searchDirectiveTable     : 
						OBJECTIVE 	- Search a given token in Directive table.
						INPUT  		- currToken : String, the token which is to be searched in 
								   	      assembler directive table.
						OUTPUT 		- boolean, true if currToken exists in directive table, otherwise false. 
	*/
	static boolean searchDirectiveTable(String currToken)
	{
		return DIRECTIVES.contains(currToken);
	}
	
	/* getDirectiveSize         : 
						OBJECTIVE 	- Return size reserved by directive.
						INPUT  		- directive : String, an assembler directive.
						OUTPUT 		- int, size reserved by directive. 
	*/
	static int getDirectiveSize(String directive)
	{
		switch(directive)
		{
			case "DB"	: return 1;
			case "DW"	: return 2;
			case "DD"	: return 4;
			default		: return 0;
		}
	}
	
	/* performDirectiveFunctionPass1 : 
						OBJECTIVE 	- Stores variables and segment names into segment table after checking 
								  directive type and update location counter accordingly.
						INPUTS  	-
								   directive  : String, an assembly directive.
								   symName    : String, name of symbol.
								   nextTokens : ArrayList<String>, operands list.
						OUTPUT 		- int, an id according to function performed:					
								   0 - Symbol is added successfully, read next token.
								   1 - Read next line.
								   2 - End Pass 1 and go to pass 2.
								   3 - Duplicate error.
								   4 - Mismatch error.
	*/
	static int performDirectiveFunctionPass1(String directive, String symName, ArrayList<String> nextTokens)
	{
		// Create a new symbol instance.
		Symbol newSymbol = new Symbol();
		
		// Check directive.
		switch (directive)
		{
			case "DB"		:	
			
			case "DW"		:   
							
			case "DD"		:
						  /*
							If directive is DB, DW, or DD, then add the variable to symbol table and update 
							location counter as follows:
										DB - +1
										DW - +2
										DD - +4
						  */
						  // Calculate No of operands.
						  int count=0;
						  for (int i=0; i<nextTokens.size(); i++)
						  {
						       	if (nextTokens.get(i).equals(",") || nextTokens.get(i).equals("?"))
								continue;
						  	else
								count++;
						  }
						  if (ST.addToSymbolTable(symName, Main.locationCounter, getDirectiveSize(directive), "VAR", Pass1.currSegment))
						  {
							// Variable added successfully.
									
							// Update location counter.
							Main.locationCounter+=getDirectiveSize(directive)*count;
									
							return 0;
						  }
						  else
							// Error.
							return 3;
								
			case "SEGMENT"		:
					  	  /*
							If directive is SEGMENT, then add the segment name to symbol table and 
							clear location counter.
						  */
						  if (ST.addToSymbolTable(symName, Pass1.segNo, -5, "SEGMENT", "(ITSELF)"))
						  {
							// Segment name added successfully.
									
							// Change current segment name.
							Pass1.currSegment=symName;
									
							// Update location counter.
							Main.locationCounter=0;
									
							return 0;
						  }
						  else
							// Error.
							return 3;
				
			case "ASSUME"		:	
					  	  // Read next line
					  	  return 1;
				
			case "END"		: 	
						  // End pass 1 and go to pass 2
						  return 2;
								
			case "ENDS"		:	
						  // Check if the symbol name matches with current segment name.
						  if (Pass1.currSegment.equals(symName))
						  {
							// Matched.
									
							// Update segment number.
							Pass1.segNo++;
									
							// Update segment name to empty.
							Pass1.currSegment=""; 
									
							return 1;
						  }
						  else
						  {
							// Error
							Main.errorFlag = true;
							return 4;
						  }
			case "HLT"		: 	
						  //Stop the program
						  return 2;
								
			default			: return 0;
		}
	}
	
	/* performDirectiveFunctionPass2 : 
						OBJECTIVE 	- Reserves space in memory after checking directive type and update location counter accordingly.
						INPUTS  	-
								   directive  : String, an assembly directive.
								   nextTokens : ArrayList<String>, operands list.
								   newLine	  : Listing, a new line assembler listing.
						OUTPUT 		- int, an id according to function performed:					
								   0 - Symbol is added successfully, read next token.
								   1 - Read next line.
								   2 - End Pass 1 and go to pass 2.
								   3 - Duplicate error.
								   4 - Mismatch error.
	*/
	static int performDirectiveFunctionPass2(String directive, Listing newLine, ArrayList<String> nextTokens)
	{
		
		// Check directive.
		switch (directive)
		{
			case "DB"		:	
			
			case "DW"		:   
							
			case "DD"		:
						  /*
							If directive is DB, DW, or DD, then reserve space in memory 
							and update location counter as follows:
										DB - +1
										DW - +2
										DD - +4
						  */
						  // Calculate No of operands and generate binary code for operands.
						  newLine.loc = Main.locationCounter+"";
						  newLine.machineCode = "";
						  int count=0;
						  for (int i=0; i<nextTokens.size(); i++)
						  {
							if (nextTokens.get(i).equals(",") || nextTokens.get(i).equals("?"))
								newLine.machineCode +=nextTokens.get(i);
							else
							{
								count++;
								try
								{
									newLine.machineCode += " "+MIT.toBinary(nextTokens.get(i).substring(1,nextTokens.get(i).length()));
								}
								catch (Exception e)
								{
									// Error.
									return 3;
								}	
							}
						  }
						  // Update location counter.
						  Main.locationCounter+=getDirectiveSize(directive)*count;
						  return 0;
								
			case "SEGMENT"		:
					  	  /*
							If directive is SEGMENT, then clear location counter.
						  */
						  newLine.loc="---";
						  newLine.machineCode="--------"; 
						  // Update location counter.
						  Main.locationCounter = 0;
									
						  return 0;
				
			case "ASSUME"		:	
					  	  // Read next line
					  	  return 1;
				
			case "END"		: 	
						  // End pass 1 and go to pass 2
						  return 2;
								
			case "ENDS"		:
						  /*
							If directive is Ends, current segment ended.
						  */			
						  newLine.machineCode="--------"; 
						  newLine.loc="---"; 
									
							return 1;
						  
			case "HLT"		: 	
						  //Stop the program
						  return 2;
								
			default			: return 0;
		}
							
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************************ END ************************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
}