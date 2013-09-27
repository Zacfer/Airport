import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;


/**
 * Graphical Interface for Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael
 * Creation: 18/02/11
 * Edited: 21/02/11 - Added commenting
 * 
 * Modified 06/03/11 - Michael
 *     Updated overall model to add airport to main GUI, allowed user to choose whether taxiways are rendered, and select an individual runway
 *     Added code to allow user to rotate the airport/runway
 *     Updated aesthetics of the GUI
 *     Added support for glossary...
 *     Added 5 random tips in bottom box
 *     Added the ability to load different airports
 *     Added menu and some menu items/functionality (keyboard click events)
 * 
 *Modified 14/03/11 - Michael	
 *		Added the test function to the program - randomly outputs questions that are read from an external text file
 *		Changed keyboard shortcut to open files to Ctrl-O
 *
 *Modfied 15/03/11 - Michael
 *		Moved glossary to a tab - now no popups!
 *
 *Modified 16/03/11 - Michael
 *	Added full screen feature and removed some bugs!
 
 *Modified 22/04/11 - David
 *	Added listener for compliance ratings
 *
 */
public class GUI extends JFrame {

	private Airport2D airportPnl;
	private JCheckBox chkTaxiways;
	private GlossaryFrame glossary;
	private JComboBox runwayChosen;
	private JComboBox ratingChosen;
	final JToggleButton selectDayBtn;
	final JToggleButton selectNightBtn;
	private Dimension fullScreenBuffer;
	
	//Glossary quick info labels
	private JLabel[] helpOutput;
	
	//Compliance Info
	private String[] complianceInfo;

	//Test(Quiz) variables
	private JPanel testPnl;
	private ArrayList<Integer> answers;
	private ArrayList<String[]> questions;
	private int questionNumber = 0;
	private JButton nextBtn;
	private JButton previousBtn;
	private JButton resetBtn;
	private JButton checkTestBtn;
	private JLabel questionLbl;
	private JRadioButton[] testAnswerButtons;
	private ButtonGroup questionsBtnGroup;
	private JTabbedPane jtp;
	private int questionType=0;
	

	
	/**
	 * Constructor provides the initial framework and base event listeners
	 */
	public GUI() {
		//Set default frame values:
		setTitle("Airport Lighting and Markings Tool - Orientation Setting: 0 Degrees");
		this.setVisible(true);
		this.setMinimumSize(new Dimension(1024, 600));
		this.setPreferredSize(new Dimension(1024, 780));
		this.setResizable(true);
		final Container content = this.getContentPane();
		content.setPreferredSize(this.getSize());
		//Tab interface:
	    jtp = new JTabbedPane(JTabbedPane.BOTTOM);
	    content.add(jtp);
		this.pack();
		
		//Add the menu bar:
		JMenuBar menuBar = new JMenuBar();
		//Build the first menu.
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		//Add items
		JMenuItem menuItem = new JMenuItem("Load Airport",
		                         KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadAirport();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Close System");
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		menu.add(menuItem);
		//Options menu:
		menu = new JMenu("Options");
		//Add items
		menuItem = new JMenuItem("Taxiways - On/Off",
		                         KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkTaxiways.setSelected(!airportPnl.renderTaxiways);
				airportPnl.taxiways();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Full Screen",
                KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(airportPnl.getWidth()==getWidth()) {
				airportPnl.setNewSize(fullScreenBuffer);
			} else {
				fullScreenBuffer = airportPnl.getSize();
				airportPnl.setNewSize(getWidth(), getHeight()-91);
			}
		}
 		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Switch Day/Night");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectDayBtn.isSelected()) {
					selectDayBtn.setSelected(false);
					selectNightBtn.setSelected(true);
				} else {
					selectDayBtn.setSelected(true);
					selectNightBtn.setSelected(false);
				}
				airportPnl.night=!airportPnl.night;
				airportPnl.repaint();
			}
		});
		
		menu.add(menuItem);		
		menuBar.add(menu);
		//Help menu:
		menu = new JMenu("Help");
		//Add items
		JMenuItem aboutmenuItem = new JMenuItem("About System");
		aboutmenuItem.setMnemonic(KeyEvent.VK_A);
		aboutmenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(new Frame(),
						"Program designed and made by: Nafiseh, Mike, Zacharias and David - Group 4 SEG", "About",
						JOptionPane.OK_OPTION);
			}
		});
		menu.add(aboutmenuItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		//Set Interface to the centre of the screen:
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Dimension ssize = toolkit.getScreenSize();
	    int x = (int) ( ssize.getWidth() - this.getWidth() ) / 2;
	    int y = (int) ( ssize.getHeight() - this.getHeight() ) / 2;
	    this.setLocation( x, y );
	    this.setVisible( true );
	    
	    //Main Panel 
	    JPanel mainPnl = new JPanel();
	    mainPnl.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
	    
	    //Test Panel
	    testPnl = new JPanel();
	    testPnl.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
	    //Inner Panel for the questions
	    JPanel question = new JPanel();
		question.setBorder(BorderFactory.createLineBorder(Color.black,5));
		question.setPreferredSize(new Dimension(600, 255));
		GridLayout gridLayout = new GridLayout(7,0);
		question.setLayout(gridLayout);
		//Question text
		this.questionLbl = new JLabel("Question here");
		questionLbl.setFont(new Font("sansserif", Font.BOLD, 20));
		question.add(this.questionLbl);
		
		//Button group so only one radiobutton can be selected
		questionsBtnGroup = new ButtonGroup();
		testAnswerButtons = new JRadioButton[4];
		//initialise all the radio buttons
		for(int i=0; i<4; i++) {
			final int j =i;
			testAnswerButtons[i] = new JRadioButton(i+"");
			questionsBtnGroup.add(testAnswerButtons[i]);
			question.add(testAnswerButtons[i]);
			//action listener to keep track of answers
			testAnswerButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					answers.set(questionNumber, j);
				}
			});
		}
		testPnl.add(question);
		//Bar separator
		JPanel sep = new JPanel();
		sep.setPreferredSize(new Dimension(3000,10));
		testPnl.add(sep);
		//Inner Panel for questions navigation and marking...self explanatory
		JPanel questionNav = new JPanel();
		questionNav.setBorder(BorderFactory.createLineBorder(Color.black,5));
		questionNav.setPreferredSize(new Dimension(600, 50));
		nextBtn = new JButton("Next");
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showQuestion(questionNumber+1);
			}
		});
		previousBtn = new JButton("Previous");
		previousBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showQuestion(questionNumber-1);
			}
		});
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				questions.clear();
				answers.clear();
				fillTest();
				showQuestion(0);
			}
		});
		checkTestBtn = new JButton("Mark!");
		checkTestBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int mark=0;
				int num=0;
				for(int i=0; i<questions.size();i++) {
					num++;
					Integer answer = answers.get(i) +1;
					if(answer.toString().equals(questions.get(i)[5])) {
						mark++;
					}
				}
				JOptionPane.showMessageDialog(new Frame(),
						"You achieved: "+mark+"/"+num, "Your Mark",
						JOptionPane.OK_OPTION);
				fillTest();
			}
		});
		String[] questionTypesOpts = {"All","Categorys","Lighting","Markings","Taxiways","Runways"};
		final JComboBox questionTypes = new JComboBox(questionTypesOpts);
		questionTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				questionType=questionTypes.getSelectedIndex();
				questions.clear();
				answers.clear();
				fillTest();
				showQuestion(0);
			}
		});
		
		if(fillTest()) {
			questionNav.add(previousBtn);
			questionNav.add(nextBtn);
			questionNav.add(questionTypes);
			questionNav.add(resetBtn);
			questionNav.add(checkTestBtn);
			testPnl.add(questionNav);
			showQuestion(0);
		}
		
	    
	    
		//Airport Panel
		airportPnl= new Airport2D(new File("src/complex.apx"));
		mainPnl.add(airportPnl);

		//Info/Test Panel
		JPanel infoPnl= new JPanel();
		GridLayout gridLayoutInfo = new GridLayout(7,0);
		infoPnl.setLayout(gridLayoutInfo);
		infoPnl.setBorder(BorderFactory.createLineBorder(Color.black,5));
		infoPnl.setPreferredSize(new Dimension((this.getWidth()-40), 200));
		JLabel lblTitle = new JLabel(" Quick Tips!");
		lblTitle.setFont(getFont());
		lblTitle.setFont(new Font("sansserif", Font.BOLD, 20));
		infoPnl.add(lblTitle);
		Random r = new Random();
		ArrayList<Integer> mappings = new ArrayList<Integer>();
		int value;
		try {
			String[][] entries = GlossaryIO.readGlossary("src/glossary.txt");
			helpOutput = new JLabel[5];
			for(int i=0; i<5; i++) {
				value=r.nextInt(entries[1].length);
				while(mappings.contains(value)) {
					value=r.nextInt(entries[1].length);
				}
				mappings.add(value);
				helpOutput[i] = new JLabel(" - "+entries[0][value]+": "+entries[1][value]);
				infoPnl.add(helpOutput[i]);
			}
		} catch (IOException e1) {
			lblTitle.setText("Unable to load glossary file - please ensure it is located in the root directory.");
		}
		
		mainPnl.add(infoPnl);
		
		//Options Panel
		final JPanel optionsPnl = new JPanel();
		optionsPnl.setBorder(BorderFactory.createLineBorder(Color.black,5));
		optionsPnl.setPreferredSize(new Dimension(240, 215));
		optionsPnl.setLayout(new FlowLayout());
		//Inclusive items:
		JLabel lblRunwayChoice = new JLabel("Specific Runway?");
		optionsPnl.add(lblRunwayChoice);
		String[] runwayArg = {"Airport"};
		runwayChosen = new JComboBox(runwayArg);
		for (Runway arg0: airportPnl.getRunways()) {
			runwayChosen.addItem("UID: "+arg0.getUID());
		}
		runwayChosen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				airportPnl.setRunway(runwayChosen.getSelectedIndex());		
				if(runwayChosen.getSelectedIndex()!=0) {
					chkTaxiways.setEnabled(false);
					chkTaxiways.setSelected(false);
				} else {
					chkTaxiways.setEnabled(true);
				}
			}
		});
		optionsPnl.add(runwayChosen);
		JLabel lblComplianceRating = new JLabel("Compliance Rating:");
		optionsPnl.add(lblComplianceRating);
		String[] ratingsArgs = {"Blank","Visual","Non Precision","CAT I","CAT II","CAT III A","CAT III B","CAT III C"};
		ratingChosen = new JComboBox(ratingsArgs);
		ratingChosen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				airportPnl.setCompliance(ratingChosen.getSelectedItem().toString());
				int base = ratingChosen.getSelectedIndex()*5;
				for(int i=0; i<5; i++) {
					if(complianceInfo[base+i]!=null) {
						helpOutput[i].setText(" - "+complianceInfo[base+i]);
					} else {
						helpOutput[i].setText("");
					}
				}
			}
		});
		optionsPnl.add(ratingChosen);
		JLabel lblApproachDirection = new JLabel("Approach Direction:");
		optionsPnl.add(lblApproachDirection);
		final JSlider approachChosen = new JSlider(0,360);
		approachChosen.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {}
			public void mouseDragged(MouseEvent e) {
				setTitle("Airport Lighting and Markings Tool - Orientation Setting: "+(approachChosen.getValue()-180)+" Degrees");
				airportPnl.setRunwayRotation(approachChosen.getValue());
			}
		});
		optionsPnl.add(approachChosen);
		JLabel lblTaxiways = new JLabel("Render Taxiways?");
		optionsPnl.add(lblTaxiways);
		chkTaxiways = new JCheckBox();
		chkTaxiways.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				airportPnl.taxiways();
			}
		});
		chkTaxiways.setSelected(true);
		optionsPnl.add(chkTaxiways);
		optionsPnl.add(new JLabel("Operationally Desirable?"));
		JCheckBox chkOperationallyDesirable = new JCheckBox();
		chkOperationallyDesirable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//add code for boolean var in airport 2d
			}
		});
		chkOperationallyDesirable.setSelected(true);
		optionsPnl.add(chkOperationallyDesirable);
		JSeparator seperator = new JSeparator();
		optionsPnl.add(seperator);
		JLabel lblTimeSetting = new JLabel("Time Setting:");
		optionsPnl.add(lblTimeSetting);
		selectDayBtn = new JToggleButton("Day");;
		selectNightBtn = new JToggleButton("Night");
		selectNightBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}

			public void mouseClicked(MouseEvent arg0) {
				//Call method to alter time setting in currently shown airport object
				//
				selectDayBtn.setSelected(false);
				selectNightBtn.setSelected(true);
				airportPnl.night=true;
				airportPnl.repaint();
			}
		});
		selectDayBtn.setSelected(true);
		selectDayBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}

			public void mouseClicked(MouseEvent arg0) {
				//Call method to alter time setting in currently shown airport object
				//
				selectDayBtn.setSelected(true);
				selectNightBtn.setSelected(false);
				airportPnl.night=false;
				airportPnl.repaint();
			}
		});
		optionsPnl.add(selectDayBtn);
		optionsPnl.add(selectNightBtn);
		JSeparator seperator2 = new JSeparator();
		optionsPnl.add(seperator2);
		mainPnl.add(optionsPnl);

		//Layout
		jtp.addTab("Airport", mainPnl);
	    jtp.addTab("Take a Test", testPnl);
	    try {
			jtp.addTab("Full Glossary", new GlossaryFrame());
		} catch (IOException e1) {}
		final SpringLayout guiLayout = new SpringLayout();
		guiLayout.putConstraint(SpringLayout.NORTH, optionsPnl, 20,SpringLayout.NORTH, this);
		guiLayout.putConstraint(SpringLayout.EAST, optionsPnl, -20,SpringLayout.EAST, this);
		guiLayout.putConstraint(SpringLayout.NORTH, airportPnl, 0,SpringLayout.NORTH, this);
		guiLayout.putConstraint(SpringLayout.SOUTH, airportPnl, -20,SpringLayout.NORTH, infoPnl);
		guiLayout.putConstraint(SpringLayout.WEST, airportPnl, 0,SpringLayout.WEST, this);
		guiLayout.putConstraint(SpringLayout.EAST, airportPnl, -20,SpringLayout.WEST, optionsPnl);
		guiLayout.putConstraint(SpringLayout.SOUTH, infoPnl, -100,SpringLayout.SOUTH,this );
		guiLayout.putConstraint(SpringLayout.WEST, infoPnl, 20,SpringLayout.WEST, this);
		mainPnl.setLayout(guiLayout);
		//Relocate components
		content.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				setPreferredSize(getSize());
				pack();
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}});
		this.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				setPreferredSize(getSize());
				pack();
			}
		});
		// Kill the process when GUI closed...
	    this.pack();
		//Forces the correct zooming
		airportPnl.repaint();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.defineComplianceInfo();
	}
	
	public void loadAirport() {
		JFileChooser chooseFileToOpen = new JFileChooser();
		// Open it
		chooseFileToOpen.showOpenDialog(null);
		File file = null;
		try {
			file = chooseFileToOpen.getSelectedFile();
			if(file!=null) {
				airportPnl.airport = new Airport(file);
				airportPnl.reset();
				resetRunwayList();
				airportPnl.taxiways();
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please give a valid file");
		}
	
		}

	private void resetRunwayList() {
		runwayChosen.removeAllItems();
		runwayChosen.addItem("Airport");
		for (Runway arg0: airportPnl.getRunways()) {
			runwayChosen.addItem("UID: "+arg0.getUID());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}
	
	/**
	 * Function to setup a test, its own function so that a test can be reset
	 */
	public boolean fillTest() {
		try {
			//Read in each line and split to an array of values for easier reading
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/exampleTest.txt")));
			answers = new ArrayList<Integer>();
			questions = new ArrayList<String[]>();
			ArrayList<String[]> buffer = new ArrayList<String[]>();
			String input;
			while((input = reader.readLine()) != null) {
				buffer.add(input.split("-"));
				answers.add(-1);
			}
			//Randomly output quiz entries
			Random r = new Random();
			int value;
			while(buffer.size()>0) {
				value =r.nextInt(buffer.size());
				if(buffer.get(value)[6].equals(questionType+"") || questionType==0)questions.add(buffer.get(value));
				buffer.remove(value);
			}
			if(questions.size()==1) {
				previousBtn.setEnabled(false);
				nextBtn.setEnabled(false);
			} else {
				nextBtn.setEnabled(true);
			}
			questionNumber=0;
			//Display the first question
			return true;
		} catch (Exception e) {
			for(int i=0; i<4; i++) {
				testAnswerButtons[i].setEnabled(false);
			}
			return false;
		}
	}
	
	/**
	 * Function to display a question
	 */
	private void showQuestion(int questionNo) {
		testAnswerButtons[0].setSelected(true);
		//On last question? If so we cannot go further
		if(questionNo==questions.size()-1) {
			nextBtn.setEnabled(false);
			checkTestBtn.setEnabled(true);
			//Else we can go forward but CANNOT check test results
		} else {
			nextBtn.setEnabled(true);
			checkTestBtn.setEnabled(false);
		}
		//If at the beginning we cannot go to previous
		if(questionNo==0) {
			previousBtn.setEnabled(false);
		} else {
			//Else we can
			previousBtn.setEnabled(true);
		}
		//Set the question text
		questionLbl.setText(" "+(questionNo+1)+") "+questions.get(questionNo)[0]);
		//Output the options
		for(int i=0; i<4; i++) {
			testAnswerButtons[i].setText(questions.get(questionNo)[i+1]);
		}
		//If a question has been previously selected (and returned to) then set this as selected
		if(answers.get(questionNo)!=-1) {
			testAnswerButtons[answers.get(questionNo)].setSelected(true);
		}
		//Update global question number variable
		questionNumber=questionNo;
	}
	
	private void defineComplianceInfo() {
		complianceInfo = new String[40];
		BufferedReader reader;
		try {
			String input;
			int i =0;
			reader = new BufferedReader(new FileReader(new File("src/complianceInfo.txt")));
			while((input = reader.readLine()) != null) {
				complianceInfo[i] = input;
				i++;
			}
		} catch (Exception e) {
		}
		
	}

}