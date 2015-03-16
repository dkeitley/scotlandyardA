package solution;
import scotlandyard.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
class MrXMovesBar extends JPanel {
	public MrXMovesBar(int rounds) {
		JTable table = new JTable(rounds,1);
		String columnTitle = "Mr X's Moves";
		table.getColumn("A").setHeaderValue(columnTitle);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200,table.getRowHeight()*(rounds+2)-11));
		this.add(scrollPane);
	}
	/* TASKS
	 * highlight the show rounds with a different colour
	 * add functions to update elements of the table
	*/	
}
