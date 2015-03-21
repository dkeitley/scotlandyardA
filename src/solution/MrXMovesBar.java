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


	public void addMrXMove(String text, int round) {
		table.setValueAt(text,round-1,0);
	}
}
