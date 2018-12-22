import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

// -------------------------------------------------------------------------------------------------------------------------------------
// ********************************************************* TWO PASS ASSEMBLER ********************************************************
// -------------------------------------------------------------------------------------------------------------------------------------
// ******************************************************* USER INTERFACE DESIGN *******************************************************
// -------------------------------------------------------------------------------------------------------------------------------------

class Main {
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** VARIABLES ********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	static final String HINT = "eg, swap.asm";	// Hint for the text field where file name is to be entered.
	
	static boolean errorFlag = false;		// This flag is set whenever there is an error in the program.
	static String errorMsg = "";			// Holds an error message, if any.
	static int locationCounter = 0;			// Location Counter- To assign offset to program statements.
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// *************************************************** EXTERNAL DEPENDENCIES ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
		
			// SYMBOL TABLE - ST class : To load the symbol table which is generated in first pass.
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************** COMPONENTS DECLARATION ***************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	
	static Frame parentFrame;			// To create main window
	static Frame loadingFrame;			// To create a loading window
	static Panel P1;				// Panel 1
	static Panel P2;				// Panel 2
	static JLabel title;				// (label)					         TITLE
	static JLabel lFileName;			// (label)					    ENTER FILE NAME
	static JTextField tFileName;			// (text field)				        _______________________	
	static JButton startBtn;			// (button)					|        START        |
	static JButton showSymTabBtn;			// (button)					|  SHOW SYMBOL TABLE  |
	static JButton showAsmLstBtn;			// (button)					|SHOW ASSEMBLY LISTING|
	static JButton loadNewFileBtn;			// (button)					|    LOAD NEW FILE    |
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ********************************************************** METHODS **********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	
	/* showPass1Results         : 
						OBJECTIVE 	- To a show the results of pass 1 and add new components to the screen.
						INPUT  		- fileName : String, name of the file which is assembled.
						OUTPUT 		- void, just changes the components of main screen to show the result
									(Symbol table). 
	*/
	static void showPass1Results(String fileName)
	{
		// Create loading frame.
		loadingFrame = new Frame("Loading...");
		loadingFrame.setSize(500,500);
		ImageIcon loading = new ImageIcon("./res/rotatingIcon.gif");
		loadingFrame.add(new JLabel("", loading, JLabel.CENTER));
		loadingFrame.setLocationRelativeTo(null);
		loadingFrame.pack(); 
		
		// Hide the main window.
		parentFrame.setVisible(false);
		
		// Show the loading window.
		loadingFrame.setVisible(true);
	
		// Hide loading window again after approx. 1 second and show the main screen again.
		new java.util.Timer().schedule( 
        new java.util.TimerTask() 
		{
            @Override
            public void run() 
			{
				loadingFrame.dispose();
				// Change main window.
				parentFrame.setSize(500,500);
						
				// Show pass 1 and pass 2 successful message and disable the startBtn button.
				startBtn.setText("Pass 1 and Pass 2 successful!");
				startBtn.setEnabled(false);
						
				// Show file name in the text field and disable text field.
				tFileName.setText(fileName);
				tFileName.setDisabledTextColor(Color.GRAY);
				tFileName.setEnabled(false);
						
				// Add new components to panel P1.
				P1.setLayout(new GridLayout(7,1));
						
				// A button to show symbol table.
				showSymTabBtn = new JButton("SHOW SYMBOL TABLE");
				showSymTabBtn.setFont(new Font("Lucida",Font.BOLD,15));
				showSymTabBtn.setForeground(Color.WHITE);
				showSymTabBtn.setBackground(Color.BLACK);
				P1.add(showSymTabBtn);
				showSymTabBtn.setVisible(true);
				// When button is clicked, show symbol table.
				showSymTabBtn.addActionListener(new ActionListener()
				{  
					public void actionPerformed(ActionEvent e)
					{ 
						ST.displaySymbolTable(Pass1.SYMBOLS);
					}  
				}); 
				
				// A button to show assembler listing.
				showAsmLstBtn = new JButton("SHOW ASSEMBLER LISTING");
				showAsmLstBtn.setFont(new Font("Lucida",Font.BOLD,15));
				showAsmLstBtn.setForeground(Color.WHITE);
				showAsmLstBtn.setBackground(Color.BLACK);
				P1.add(showAsmLstBtn);
				showAsmLstBtn.setVisible(true);
				// When button is clicked, show assembler listing.
				showAsmLstBtn.addActionListener(new ActionListener()
				{  
					public void actionPerformed(ActionEvent e)
					{ 
						Listing.displayAssemblerListing(Pass2.LISTING);
					}  
				});
		
				// A button to load new file.
				loadNewFileBtn=new JButton("LOAD ANOTHER FILE");
				loadNewFileBtn.setFont(new Font("Lucida",Font.BOLD,15));
				loadNewFileBtn.setForeground(Color.WHITE);
				loadNewFileBtn.setBackground(Color.BLACK);
				P1.add(loadNewFileBtn);
				loadNewFileBtn.setVisible(true);
				// When button is clicked, again create the new main window.
				loadNewFileBtn.addActionListener(new ActionListener()
				{  
					public void actionPerformed(ActionEvent e)
					{ 
						// Clear the previous symbol table of pass 1.
						Pass1.SYMBOLS.clear();
						// Clear the previous listing of pass 2.
						Pass2.LISTING.clear();
						parentFrame.dispose();
						createMainWindow();
					}  
				}); 
				
				// Display the main window again.
				parentFrame.pack();
				parentFrame.setVisible(true);
            }
        }, 
        600 );
	}
	
	/* loadFile        	    : 
						OBJECTIVE 	- Load the file to assemble and start Pass 1 and Pass 2, 
						                  show error messages if file cannot be loaded.
						INPUT  		- fileName : String, the name of the file which is to be assembled.
						OUTPUT 		- void, Starts pass 1 to generate symbol table and
								  pass 2 to generate assembly listing. 
	*/
	static void loadFile(String fileName)
	{
		// If user has not enterd any file name, then show error.
		if (fileName.equals(HINT) || fileName.equals(""))
			JOptionPane.showMessageDialog(null, "Please enter a file name.", "Two Pass Assembler Error", 1);
		else
		{
			// User has enterd a file name.
			
			// Check file extension.
			String extension = "";
			int i = fileName.lastIndexOf('.');
			if (i > 0)
				extension = fileName.substring(i+1);
		
			// If .asm file, then start assembling the program.
			if (extension.equalsIgnoreCase("asm"))
			{
				try
				{
					// Open file.
					FileReader asmFile = new FileReader(fileName);
					
					// Load machine information.
					MIT.loadMachineInfo();

					// Start pass 1.
					Pass1.start(new FileReader(fileName));
					
					System.out.println("\n\n\n");
					
					// Start pass 2.
					Pass2.start(new FileReader(fileName));
					
					// If successful, change the main screen to show results of Pass 1 and Pass 2.
					if (!Main.errorFlag)
						showPass1Results(fileName.substring(7));
					// Else, again create main screen.
					else
					{
						parentFrame.dispose();
						createMainWindow();
					}
				}
			
				// If file not found, print an error message.
				catch(FileNotFoundException e)
				{
					JOptionPane.showMessageDialog(null, "File not found.", "Two Pass Assembler Error", 1);
				}
			}		
		
			// Else if not .asm file, print an error message.
			else
			{
				JOptionPane.showMessageDialog(null, "File extension is not valid.", "Two Pass Assembler Error", 1);
			}
		}
	}
	
	/* createMainWindow        	: 
						OBJECTIVE 	- Create the main window for user interface.
						INPUT  		- Nothing.
						OUTPUT 		- void, just displays the main frame. 
	*/
	
	static void createMainWindow()
	{
		// Main window
		parentFrame = new Frame("Two Pass Assembler");
		parentFrame.setSize(500,500);
		
		// Panels
		P1 = new Panel();
		P2 = new Panel();
		
		// Title
		title =  new JLabel("Two Pass Assembler");
		title.setFont(new Font("Lucida",Font.PLAIN,30));
		
		// Message to enter file name
		lFileName = new JLabel("Enter File Name : ");
		lFileName.setFont(new Font("Lucida",Font.PLAIN,24));
		lFileName.setForeground(Color.RED);
		
		// Text field with hint
		tFileName = new JTextField(HINT,20);
		tFileName.setFont(new Font("Lucida",Font.ITALIC,20));
		tFileName.setForeground(Color.GRAY);
		tFileName.setBackground(Color.GREEN);
		
		// Start Pass 1 button
		startBtn = new JButton("START");
		startBtn.setFont(new Font("Lucida",Font.BOLD,15));
		startBtn.setForeground(Color.WHITE);
		startBtn.setBackground(Color.BLACK);
		
		// Request focus to the start pass 1 button when window starts.
		startBtn.requestFocusInWindow();	
		
		// Add components to panel.
		P1.setLayout(new GridLayout(4,1));
		P1.add(title);
		P1.add(lFileName);
		P1.add(tFileName);
		P1.add(startBtn);
		P2.add(P1);
		
		// Add panels to main window and set visibility to true.
		parentFrame.add(P2,BorderLayout.NORTH);
		parentFrame.setLocationRelativeTo(null);
		parentFrame.pack();  
		parentFrame.setVisible(true);
		
		// EVENT LISTENERS
		
		// For text field - When focus is gained, clear the HINT text and set font.
		tFileName.addFocusListener(new FocusListener() 
		{
			public void focusGained(FocusEvent e) 
			{
				tFileName.setText("");
				tFileName.setFont(new Font("Lucida",Font.PLAIN,20));
				tFileName.setForeground(Color.BLACK);
			}
			public void focusLost(FocusEvent e) {
			// do nothing
			}
		});
		
		// For Start Pass 1 button - when this button is clicked, load file and start pass 1 of assembler.
		startBtn.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent e)
			{ 

				String fileName = "./docs/" + tFileName.getText();  
				loadFile(fileName);
			}  
		});
		
		// Close all windows and exit program when main window is closed.
		parentFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ******************************************************** MAIN METHOD ********************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) 
	{
		// Create main window
		createMainWindow();
		
	}
	
	//jar -cvfe assembler8086.jar Main *.class
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// ************************************************************ END ************************************************************
	// -----------------------------------------------------------------------------------------------------------------------------
}
