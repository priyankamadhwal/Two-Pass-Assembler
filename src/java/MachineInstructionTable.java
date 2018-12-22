import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;

// -------------------------------------------------------------------------------------------------------------------------------------
// ***************************************************** MACHINE INSTRUCTION TABLE *****************************************************
// -------------------------------------------------------------------------------------------------------------------------------------

class MIT

{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ******************************************************** INSTRUCTIONS *******************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// Contains instructions format.
	static List<List<String>> INSTRUCTIONS;			// Load from 'MIT.txt'.
																				
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ******************************************************* OPERAND TYPES *******************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// 1. REG- General Purpose Registers
	static List<List<String>> REG;				// Load from 'REG.txt'.
	
	// 2. SREG- Segment Registers
	static List<List<String>> SREG;				// Load from 'SREG.txt'.
	
	// 3. MEM- Memory	: [BX], [BX+SI+7], variable, etc
	
	// 4. IMM- Immediate: 5, -24, 3FH, 10001101B, etc 
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// **************************************************** ADDRESSING MODES ********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	static List<List<String>> ADDR_MOD;			// Load from 'ADDR_MOD.txt'.
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// *************************************************** EXTERNAL DEPENDENCIES ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
		
			// ADDRESSING MODES HELPER - AMHelper class : To determine addressing modes of the instructions.
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	/* loadMachineInfo   : 
						OBJECTIVE 	- Load instructions, registers and addressing modes.
						INPUT  		- nothing
						OUTPUT 		- void, just loads the machine information.
	*/
	public static void loadMachineInfo()
	{
		List<String> linesInFile;
		
		// Load general purpose registers.
		try
		{
			linesInFile = Files.readAllLines(Paths.get("./src/tables/REG.txt"));
			REG = new ArrayList<>(linesInFile.size());
			linesInFile.forEach(line -> {
			String[] elements = line.split("\\s+");
			REG.add(Arrays.asList(elements));
			});
		}
		catch(Exception e)
		{
			Main.errorFlag = true;
			Main.errorMsg = "Error in loading the registers.";
		}
		
		// Load segment registers.
		try
		{
			linesInFile = Files.readAllLines(Paths.get("./src/tables/SREG.txt"));
			SREG = new ArrayList<>(linesInFile.size());
			linesInFile.forEach(line -> {
			String[] elements = line.split("\\s+");
			SREG.add(Arrays.asList(elements));
			});
		}
		catch(Exception e)
		{
			Main.errorFlag = true;
			Main.errorMsg = "Error in loading the registers.";
		}
		
		// Load instructions.
		try
		{
			linesInFile = Files.readAllLines(Paths.get("./src/tables/MIT.txt"));
			INSTRUCTIONS = new ArrayList<>(linesInFile.size());
			linesInFile.forEach(line -> {
			String[] elements = line.split("\\s+");
			INSTRUCTIONS.add(Arrays.asList(elements));
			});
		}
		catch(Exception e)
		{
			Main.errorFlag = true;
			Main.errorMsg = "Error in loading the machine instruction table.";
		}
		
		// Load addressing modes.
		try
		{
			linesInFile = Files.readAllLines(Paths.get("./src/tables/ADDR_MOD.txt"));
			ADDR_MOD = new ArrayList<>(linesInFile.size());
			linesInFile.forEach(line -> {
			String[] elements = line.split("\\s+");
			ADDR_MOD.add(Arrays.asList(elements));
			});
		}
		catch(Exception e)
		{
			Main.errorFlag = true;
			Main.errorMsg = "Error in loading the ddressing modes.";
		}
	}
	
	/* searchInstructionTable   : 
						OBJECTIVE 	- Search a given token in Instruction table.
						INPUT  		- currToken : String, the token which is to be searched in 
									      machine instruction table.
						OUTPUT 		- boolean, true if currToken exists in instruction table, 
						                 	   otherwise false. 
	*/
	public static boolean searchInstructionTable(String currToken)
	{
		return INSTRUCTIONS.stream().anyMatch(a -> a.get(0).equals(currToken));
	}
	
	/* searchRegTable   : 
						OBJECTIVE 	- Search a given token in Registers table.
						INPUT  		- currToken : String, the token which is to be searched in 
									      Registers table.
						OUTPUT 		- boolean, true if currToken exists in Registers table, 
						                 	   otherwise false. 
	*/
	public static boolean searchRegTable(String currToken)
	{
		return REG.stream().anyMatch(a -> a.get(0).equals(currToken));
	}
	
	/* searchSregTable   : 
						OBJECTIVE 	- Search a given token in Segment Registers table.
						INPUT  		- currToken : String, the token which is to be searched in 
									      Segment Registers table.
						OUTPUT 		- boolean, true if currToken exists in Segment Registers table, 
						                 	   otherwise false. 
	*/
	public static boolean searchSregTable(String currToken)
	{
		return SREG.stream().anyMatch(a -> a.get(0).equals(currToken));
	}

	
	/* getInstructionSize   : 
						OBJECTIVE 	- Returns the size of an instruction. 
								  Returns -1 if instruction not valid.
						INPUTS  	- 
									inst		: String, instruction.
									destOP		: String, the destination operand type.
									srcOP		: String, the source operand type.
									dest		: String, the destination operand.
									src			: String, the source operand
						OUTPUT 		- int, size of the instruction. (-1, if not valid.)
	*/
	static int getInstructionSize(String inst, String destOP, String srcOP, String dest, String src)
	{
		// Initially, size is -1.
		int size=-1;
		
		// If the source/destination operand addressing mode is undefined, then set the error flag.
		if (destOP.equals("UNDEF") || srcOP.equals("UNDEF"))
		{
			Main.errorMsg = "Undefined addressing mode.";
			Main.errorFlag = true;
		}
		
		// If addressing mode is valid, then calculate instruction size (in bytes).
		else
		{
			// Get instruction format from Machine Instruction Table.
			List<List<String>> result = INSTRUCTIONS.stream()
			.filter(a -> a.get(0).equals(inst) && a.get(1).equals(destOP) && a.get(2).equals(srcOP))
			.collect(Collectors.toList());
			
			//Print the instruction format.
			result.forEach(System.out::println);
			
			// If instruction present in the table.
			if (result.size() == 1)
				size = Integer.parseInt(result.get(0).get(11));
			else if (result.size() > 1)
			{
				if (srcOP.equals("IMM"))
				{
					try
					{
						if (toBinary(src.substring(1)).length() <= 8)
							size = Integer.parseInt(result.get(1).get(11));
						
						else
							size = Integer.parseInt(result.get(0).get(11));
					}
					catch(Exception e)
					{
						size=-1;
					}
				}
			}
			
		}
		
		// Return size.
		return size;
	}
	
	/* getMachineInstructionCode: 
						OBJECTIVE 	- Returns themachine code of an instruction. 
								  Returns "" if instruction not valid.
						INPUTS  	- 
									inst		: String, instruction.
									destOP		: String, the destination operand type.
									srcOP		: String, the source operand type.
									dest		: String, the destination operand.
									src			: String, the source operand
						OUTPUT 		- String, macine code of the instruction. ("", if not valid.)
	*/
	static String getMachineInstructionCode(String inst, String destOP, String srcOP, String dest, String src)
	{
		String code = "";
		
		try
		{
		// Get instruction format from Machine Instruction Table.
			List<List<String>> result = INSTRUCTIONS.stream()
			.filter(a -> a.get(0).equals(inst) && a.get(1).equals(destOP) && a.get(2).equals(srcOP))
			.collect(Collectors.toList());
			
			if (result.size() > 1)
			{
				if (srcOP.equals("IMM"))
				{
					if (toBinary(src.substring(1)).length() <= 8)
						result.remove(0);
					else
						result.remove(1);
				}
			}
				
			if (result.size() == 1)
			{
				//code = opcode+d+w+" "+mod+reg+rm+" "+disp+" "+data;
			
				String regtmp="", wtmp="";
				
				List<String> register = null;
				
				if (destOP.equals("REG") || destOP.equals("ACC"))
					register = findInList(REG,dest);
					
				else if (srcOP.equals("REG") || srcOP.equals("ACC"))
					register = findInList(REG,src);
				
				if (register!=null)
				{
					regtmp = register.get(1);
					wtmp = register.get(2);
				}
					
				String opcode = result.get(0).get(3);				// OPCODE
				String d = result.get(0).get(4);				// D
				String w = result.get(0).get(5);				// W
				String mod = result.get(0).get(6);				// MOD
				String reg = result.get(0).get(7);				// REG
				String rm = result.get(0).get(8);				// RM
				String disp = result.get(0).get(9);				// DISP
				String data = result.get(0).get(10);				// DATA
				
				if (opcode.contains("x"))
				{
					opcode = opcode.substring(0,opcode.indexOf("x"));
					opcode+=regtmp;
				}
				
				if (d.equals("n"))
					d="";
				
				if (w.equals("n"))
					w="";
				else
					w=wtmp;
				
				if (mod.equals("n"))
				{
					mod = "";
					rm="";
				}
				else
				{
					if ((destOP.equals("REG") || destOP.equals("ACC") || destOP.equals("SREG")) && 
						(srcOP.equals("REG") || srcOP.equals("ACC") || srcOP.equals("SREG")))
					{
						mod="11";
						
						List<String> r;
						
						r = destOP.equals("SREG") ? findInList(SREG, dest): findInList(REG, dest);
							
						reg = r.get(1);
						if (reg.length() <3 )
							reg = "0"+reg;
						
						r = srcOP.equals("SREG") ? findInList(SREG, src): findInList(REG, src);
						 
						rm = r.get(1);
						if (rm.length() <3 )
							rm = "0"+reg;
					}
					else
					{
						if (destOP.equals("REG") || destOP.equals("ACC"))
							reg = findInList(REG,dest).get(1);
						
						else if (srcOP.equals("REG") || srcOP.equals("ACC"))
							reg = findInList(REG,src).get(1);
						
						if (destOP.equals("MEM"))
						{
							AMHelper.AM amOP = AMHelper.getAddressingMode(dest);
							
							List<String> r;
							
							String search = dest;
							if (amOP.equals(AMHelper.AM.RELBASIND)||amOP.equals(AMHelper.AM.BASIND))
							{
								search = dest.substring(0,dest.lastIndexOf("+")+1);
								disp = toBinary(dest.substring(dest.lastIndexOf("+")+1, dest.length()-1)+"D");
								if (disp.length() <= 8)
									search += "d8]";
								else
									search += "d16]";
								
								disp = bin16(disp);
							}
							if (amOP.equals(AMHelper.AM.DIR))
								search = "d16";
							
							r = findInList(ADDR_MOD, search);
							mod = r.get(1);
							rm = r.get(2);
								
						}
						if (srcOP.equals("MEM"))
						{
							AMHelper.AM amOP = AMHelper.getAddressingMode(src);
							
							List<String> r;
							
							String search = src;
							
							if (amOP.equals(AMHelper.AM.RELBASIND)||amOP.equals(AMHelper.AM.BASIND))
							{
								search = src.substring(0,src.lastIndexOf("+")+1);
								disp = toBinary(src.substring(src.lastIndexOf("+")+1, src.length()-1)+"D");
								if (disp.length() <= 8)
									search += "d8]";
								else
									search += "d16]";
								disp = bin16(disp);
							}
							else if (amOP.equals(AMHelper.AM.DIR))
								search = "d16";
							
							r = findInList(ADDR_MOD, search);
							mod = r.get(1);
							rm = r.get(2);
							
							if (srcOP.equals("IMM"))
							{
								mod = "00";
								rm = reg;
								data = bin16(toBinary(src.substring(1)));
							}
						}
						
					}
				}	
				
				if (reg.equals("n"))
					reg="";
				if (disp.equals("n"))
					disp="";
				else if (disp.equals("y"))
				{
					
					if (destOP.equals("MEM"))
					{
						
						List<String> r;
						AMHelper.AM amOP = AMHelper.getAddressingMode(dest);
						if (amOP.equals(AMHelper.AM.RELBASIND)||amOP.equals(AMHelper.AM.BASIND))
							disp = bin16(toBinary(dest.substring(dest.lastIndexOf("+")+1, dest.length()-1)+"D"));
						else
						{
							for (Symbol s : Pass1.SYMBOLS)
							{
								if (dest.equals(s+""))
								{
									if (s.type.equals("SEGMENT"))
										disp = "-------- -------- R";
									else
										disp=bin16(toBinary(s.offset+"D"));
								}
							}
						}
					}
					else if (srcOP.equals("MEM"))
					{
						List<String> r;
						AMHelper.AM amOP = AMHelper.getAddressingMode(src);
						if (amOP.equals(AMHelper.AM.RELBASIND)||amOP.equals(AMHelper.AM.BASIND))
							disp = bin16(toBinary(src.substring(src.lastIndexOf("+")+1, src.length()-1)+"D"));
						else
						{
							for (Symbol s : Pass1.SYMBOLS)
							{
								if (src.equals(s+""))
								{
									if (s.type.equals("SEGMENT"))
										disp = "-------- -------- R";
									else
										disp=bin16(toBinary(s.offset+"D"));
								}
							}
						}
					}
				}
				
				if (data.equals("n"))
					data="";
				else if (data.equals("y"))
					data = bin16(toBinary(src.substring(1)));
				
			code = opcode+d+w+" "+mod+reg+rm+" "+disp+" "+data;
			}
				
				
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
			
		
		return code;
	}
	
	static String toBinary(String x)  throws Exception
	{ 

		char type = x.charAt(x.length()-1);
		String bin=""; 
 
		switch(type)
		{
			case 'D':
						long n = Integer.parseInt(x.substring(0,x.length()-1));
						int i = 0; 
		
						if (n==0)
							bin="0";
						else
						{
							while (n > 0) 
							{ 
								bin = bin + (n % 2); 
								n = n / 2; 
								i++; 
							} 
						}
						break;
			case 'H':
						String tmp = x.substring(0,x.length()-1);
						for (int j=0; j<tmp.length(); j++)
						{
							switch(tmp.charAt(j))
							{
								case '0' : bin += "0000"; break;
								case '1' : bin += "0001"; break;
								case '2' : bin += "0010"; break;
								case '3' : bin += "0011"; break;
								case '4' : bin += "0100"; break;
								case '5' : bin += "0101"; break;
								case '6' : bin += "0110"; break;
								case '7' : bin += "0111"; break;
								case '8' : bin += "1000"; break;
								case '9' : bin += "1001"; break;
								case 'A' : bin += "1010"; break;
								case 'B' : bin += "1011"; break;
								case 'C' : bin += "1100"; break; 
								case 'D' : bin += "1101"; break;
								case 'E' : bin += "1110"; break;
								case 'F' : bin += "1111"; break;
							}
						}
						break;
						
			case 'B': bin = x.substring(0,x.length()-1); break;
		}
		return bin;
	} 
	
	static String bin16(String bin)
	{
		int len = bin.length();
		int rem;
		String bin_low="";
		String bin_high="";
		if (len <= 8)
		{
			rem = 8-len;
			for (int i=0; i<rem; i++)
				bin="0"+bin;
			bin_low = bin;
			String sign = bin.charAt(0)+"";
			for (int i=0; i<8; i++)
				bin_high += sign;
		}
		else
		{
			rem = 16 - len;
			for (int i=0; i<rem; i++)
				bin = "0"+bin;
			bin_high = bin.substring(0,8);
			bin_low = bin.substring(8);
		}
		
		return bin_low + " " + bin_high;
		
	}
	
	static List<String> findInList(List<List<String>> lst, String search)
	{
		return 	lst.stream()
					.filter(a -> a.get(0).equals(search))
					.findFirst()
					.get();
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************************ END ************************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
}
