import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

// -------------------------------------------------------------------------------------------------------------------------------------
// ************************************************************ SYMBOL TABLE ***********************************************************
// -------------------------------------------------------------------------------------------------------------------------------------

class Symbol
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************** ATTRIBUTES OF A SYMBOL ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	String name;				// NAME
	int offset;				// OFFSET
	int size;				// SIZE
	String type;				// TYPE
	String segment;				// SEGMENT
	
	// Return symbol name.
	public String toString()
	{
		return name;
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
}


class SymbolTableModel extends AbstractTableModel
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ******************************************************* SYMBOL TABLE MODEL **************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
 
	java.util.List<Symbol> symbolsList;							// List of all symbols
	String headerList[] = new String[]{"NAME","OFFSET","SIZE","TYPE","SEGMENT"};		// Column Names
 
	public SymbolTableModel(java.util.List<Symbol> list) 					// Constructor
	{
		symbolsList = list;
	}
 
	@Override
	public int getColumnCount() 								// Return no. of columns
	{
		return 5;
	}
 
	@Override										// Return no. of rows
	public int getRowCount() 
	{
		return symbolsList.size();
	}
 
	@Override								// This method is called to set the value of each cell
	public Object getValueAt(int row, int column) 
	{
		Symbol entity = null;
		entity= symbolsList.get(row);
 
		switch (column) 
		{ 
			case 0	:	return entity.name;
			case 1	:	return entity.offset;
			case 2	:	return entity.size;
			case 3	:	return entity.type;
			case 4	:	return entity.segment;
			default :	return "";
		}
	}

	public String getColumnName(int col) 					// This method will be used to display the name of columns
	{
		return headerList[col];
	}
}

class ST
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	/* symbolAlreadyExists      : 
						OBJECTIVE 	- Check if a symbol already exists in the symbol table.
						INPUT  		- symName : String, the symbol name which is to be checked.
						OUTPUT 		- boolean, true if a symbol already exists, otherwise false. 
	*/
	static boolean symbolAlreadyExists(String symName)
	{
		// Compare with the symbols in symbol table, one-by-one.
		for (Symbol s : Pass1.SYMBOLS)
		{
			if (symName.equals(s+""))
			{
				return true;
			}
		}
		return false;
    }
	
	/* addToSymbolTable	    : 
						OBJECTIVE 	- Add a given symbol to the symbol table.
						INPUTS 		- 
								   name   : Name of the symbol.
								   offset : Offset of the symbol in current segment.
								   size   : Size of the symbol.
								   type   : Type of the symbol (VAR/SEGMENT/LABEL).
								   segment: Segment name.
						OUTPUT 		- boolean, true if symbol is successfully added to symbol table, 
									   else false.
	*/
	static boolean addToSymbolTable(String name, int offset, int size, String type, String segment)
	{
		// If the symbol already exists, then set error flag and return.
		if (symbolAlreadyExists(name))
		{
			Main.errorFlag = true;
			return false;
		}
		// Else add the given symbol to symbol table.
		else
		{
			Symbol newSymbol = new Symbol();
			newSymbol.name=name; 
			newSymbol.offset=offset;
			newSymbol.size=size;
			newSymbol.type=type; 
			newSymbol.segment=segment;
			Pass1.SYMBOLS.add(newSymbol);
			return true;
		}
	}
	
	/* displaySymbolTable	    : 
						OBJECTIVE 	- Display the symbol table.
						INPUTS 		- SYMBOLS, ArrayList<Symbol>, contains all the variables, 
											      segment names and labels.
						OUTPUT 		- void, just displays the symbol table in a new window.
	*/
	static void displaySymbolTable(ArrayList<Symbol> SYMBOLS)
	{
		// Create a List of symbols
		java.util.List<Symbol> entityList = SYMBOLS;
		
		// Create symbol table
		JTable table = new JTable();
		AbstractTableModel model = new SymbolTableModel(entityList);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setModel(model);
		
		// Create a new window and add table
		JFrame frame = new JFrame();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************************ END ************************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
}
