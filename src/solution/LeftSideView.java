package solution;

import scotlandyard.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;

class LeftSideView extends JPanel {
	private java.util.List<Colour> colours;
	private Map<Colour,Integer> colourToTicketsNum;
	//will need to add a function in the presenter to construct
	//Map<Colour,Integer>
	public LeftSideView(java.util.List<Colour> players, Map<Colour,Integer> tickets) {
		colours = players;
		colourToTicketsNum = tickets;
		Box box = Box.createVerticalBox();
		for(Colour c:players) {
			box.add(new JLabel(c.toString()));
			box.add(playerBox(colourToTicketsNum.get(c)));
		}
		this.add(box);
	}

	private Box playerBox(int numTickets) {
		Box box = Box.createVerticalBox();
		JTable table = new JTable(numTickets,1);
		table.getColumn("A").setHeaderValue("Tickets Available");
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300,table.getRowHeight()*(numTickets+2)-11));
		box.add(scrollPane);
		return box;
	}
	
	
}
