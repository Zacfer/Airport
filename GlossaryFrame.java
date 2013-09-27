import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class to build glossary GUI
 * 
 * @author 2011SEGgp04 Nafiseh, David
 *  
 * Modified: 06/03/11 - Michael
 *    Updated to fix scrolling bug
 *    

 */
public class GlossaryFrame extends JPanel{
	private JList items;
	private String[][] entries;
	private JTextArea description;
	
	public String[][] returnValues() {
		return entries;
	}
	
	public GlossaryFrame() throws IOException{
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
        
        this.setSize(500, 500);
        this.setSize(500, 500);
        
        SpringLayout spring = new SpringLayout();
        this.setLayout(spring);
        
		entries = GlossaryIO.readGlossary("src\\glossary.txt");
        
		items = new JList(entries[0]);
        JScrollPane itemsList = new JScrollPane(items);
        itemsList.setPreferredSize(new Dimension(200, 0));
        this.add(itemsList);
        items.addListSelectionListener(new GlossaryListener());
        
        description = new JTextArea();
        description.setEditable(false);
        JScrollPane descriptionList = new JScrollPane(description);
        description.setLineWrap(true);
        descriptionList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionList.setPreferredSize(new Dimension(500, 0));
        this.add(descriptionList);
        
        spring.putConstraint(SpringLayout.WEST, itemsList, 10, SpringLayout.WEST, this);
        spring.putConstraint(SpringLayout.NORTH, itemsList, 10, SpringLayout.NORTH, this);
        spring.putConstraint(SpringLayout.WEST, descriptionList, 10, SpringLayout.EAST, itemsList);
        spring.putConstraint(SpringLayout.NORTH, descriptionList, 0, SpringLayout.NORTH, itemsList);
        spring.putConstraint(SpringLayout.EAST, descriptionList, -10, SpringLayout.EAST, this);
        spring.putConstraint(SpringLayout.SOUTH, itemsList, -10, SpringLayout.SOUTH, this);
        spring.putConstraint(SpringLayout.SOUTH, descriptionList, -10, SpringLayout.SOUTH, this);
	}
	
	private class GlossaryListener implements ListSelectionListener{
		
		public void valueChanged(ListSelectionEvent e){
			boolean flag = false;
			int i =0;
			String name = (String) items.getSelectedValue();
			
			while(!flag){
				if(entries[0][i] == name){
					flag = true;
				}
				else{
					i++;
				}
			}
			description.setText(entries[1][i]);
		}
	}
	
	
}
