package solution;
import scotlandyard.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
class MrXMovesBar extends JPanel {

	private JTable table;
	
	public MrXMovesBar(int rounds) {
		JTable table = new JTable(rounds,1);
		String columnTitle = "Mr X's Moves";
		table.getColumn("A").setHeaderValue(columnTitle);
		table.setFillsViewportHeight(true);
		this.table = table;
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200,table.getRowHeight()*rounds + 22));
		this.add(scrollPane);
	}


	public void addMrXMove(Ticket ticket, Boolean showRound, int currentRound, int location) {
		String text;
		if(showRound == true) {
			text = Integer.toString(location) + ", " + ticket.toString();
		} else {
			text = ticket.toString();
		}
		table.setValueAt(ticket,currentRound-1,0);
	}
	/* TASKS
	 * highlight the show rounds with a different colour
	 * add functions to update elements of the table
	*/	
}
