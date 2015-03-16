package solution;

import scotlandyard.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;

class LeftSideView extends JPanel {
	private java.util.List<Colour> colours;
	
	public LeftSideView(java.util.List<Colour> players) {
		colours = players;
		Box box = Box.createVerticalBox();

		
		for(Colour c:players) {
			box.add(labels(c));
			box.add(ticketsBox(c));
		}
		JScrollPane scrollPane = new JScrollPane(box);
		scrollPane.setPreferredSize(new Dimension(300,800));
		this.add(scrollPane);
	}
	private Box labels(Colour colour) {
		Box box = Box.createVerticalBox();
		box.add(new JLabel(colour.toString()));
		//need to make this field of class so can set/get
		box.add(new JLabel("Location: "));
		return box;
	}
	
	private JScrollPane ticketsBox(Colour colour) {
		JTable table;
		if(colour.equals(Colour.Black)) {
			table = new JTable(5,1);
		} else {
			table = new JTable(3,1);
		}
		table.getColumn("A").setHeaderValue("Tickets Available:");
		table.setFillsViewportHeight(true);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(200,table.getRowHeight()*5 - 9));
		return pane;
	}
	
	
	
}
