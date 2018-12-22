import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

class Listing
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// *************************************************** ATTRIBUTES OF LISTING ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	int lineNo;					// LINE NO.		
	String loc="";					// LOCATION			
	String machineCode="";				// MACHINE CODE			
	String srcCode="";				// SOURCE CODE	

	// Return line.
	public String toString()
	{
		return loc + " : " + machineCode + " [" + lineNo + "] " + srcCode;
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	/* displayAssemblerListing: 
						OBJECTIVE 	- Display the Assembler Listing.
						INPUTS 		- LISTING, ArrayList<Listing>, contains the Assembler Listing.
						OUTPUT 		- void, just displays the Assembler Listing in a new window.
	*/
	static void displayAssemblerListing(ArrayList<Listing> LISTING)
	{
		// Create a List.
		java.util.List<Listing> entityList = LISTING;
		
		// Create Assembler Listing table
		JTable table = new JTable();
		AbstractTableModel model = new AssemblerListingModel(entityList);
		
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setMinWidth(400);
		table.getColumnModel().getColumn(2).setMinWidth(70);
		table.getColumnModel().getColumn(3).setMinWidth(1000);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		// Create a new window and add table
		JFrame frame = new JFrame();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(1045,500));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
	


class AssemblerListingModel extends AbstractTableModel
{
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************** ASSEMBLER LISTING MODEL **************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
 
	java.util.List<Listing> assemblyListing;	// List of all the lines in assembler listing
	
	String headerList[] = new String[]{"LOCATION","MACHINE CODE","LINE NO.","SOURCE CODE"};		// Column Names
 
	public AssemblerListingModel(java.util.List<Listing> list) 	// Constructor
	{
		assemblyListing = list;
	}
 
	@Override
	public int getColumnCount() 					// Return no. of columns
	{
		return 4;
	}
 
	@Override							// Return no. of rows
	public int getRowCount() 
	{
		return assemblyListing.size();
	}
 
	@Override							// This method is called to set the value of each cell
	public Object getValueAt(int row, int column) 
	{
		Listing entity = null;
		entity= assemblyListing.get(row);
 
		switch (column) 
		{ 
			case 0	:	return entity.loc;
			case 1	:	return entity.machineCode;
			case 2	:	return entity.lineNo;
			case 3	:	return entity.srcCode;
			default :	return "";
		}
	}

	public String getColumnName(int col) 				// This method will be used to display the name of columns
	{
		return headerList[col];
	}
}
