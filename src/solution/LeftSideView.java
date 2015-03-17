package solution;

import scotlandyard.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;;

class LeftSideView extends JPanel {

	private Map<Colour,JTable> colourToTable;
	private Map<Colour, JLabel> colourToLabel;
	
	public LeftSideView(java.util.List<Colour> colours) {
		colourToTable = new HashMap<Colour,JTable>();
		colourToLabel = new HashMap<Colour,JLabel>();
		Box box = Box.createVerticalBox();

		for(Colour c:colours) {
			box.add(labels(c));
			JTable table;
			if(!c.equals(Colour.Black)) {
				table = ticketsTable();
			} else {
				table = new JTable(5,1);
			}
			colourToTable.put(c,table);
			box.add(new JScrollPane(table));
		}

		JScrollPane scrollPane = new JScrollPane(box);
		scrollPane.setPreferredSize(new Dimension(300,800));
		this.add(scrollPane);
	}

	//returns a box containing player and location labels
	private Box labels(Colour colour) {
		Box box = Box.createVerticalBox();
		box.add(new JLabel(colour.toString()));
		JLabel locationLabel = new JLabel("Location: ");
		colourToLabel.put(colour,locationLabel);
		box.add(locationLabel);
		return box;
	}

	//creates a table containing the number of tickets each player has
	private JTable ticketsTable() {
		JTable table = new JTable(3,1);
		table.getColumn("A").setHeaderValue("Tickets Available:");
		table.setFillsViewportHeight(true);
		return table;
	}

	//changes the entry in the tickets table of a player
	public void setNumTickets(Colour colour, Ticket ticket, int value) {
		JTable table = colourToTable.get(colour);
		String text;
			if(ticket.equals(Ticket.Taxi)) {
				text = Integer.toString(value) + " Taxi tickets";
				table.setValueAt(text,0,0);
			} else if (ticket.equals(Ticket.Bus)) {
				text = Integer.toString(value) + " Bus tickets";
				table.setValueAt(text,1,0);
			}  else if (ticket.equals(Ticket.Underground)) {
				text = Integer.toString(value) + " Underground tickets";
				table.setValueAt(text,2,0);
			} else if (ticket.equals(Ticket.SecretMove)) {
				if(colour.equals(Colour.Black)) {
					text = Integer.toString(value) + " Secret Moves";
					table.setValueAt(text,3,0);
				}
			} else {
				if(colour.equals(Colour.Black)) {
					text = Integer.toString(value) + " Double Moves";
					table.setValueAt(text,4,0);
				}
			}
	}

	//updates the location label of a player
	public void setLocation(Colour colour, int value) {
		JLabel location = colourToLabel.get(colour);
		location.setText("Location: " + value);
	}
	
	
	
}
