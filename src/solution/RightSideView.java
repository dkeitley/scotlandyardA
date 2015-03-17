package solution;
import scotlandyard.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class RightSideView extends JPanel{

	public JLabel roundNumLabel;
	public JComboBox singleMoveBox;
	public JComboBox doubleMoveBox1;
	public JComboBox doubleMoveBox2;
	public JComboBox singleTicketBox;
	public JComboBox doubleTicketBox1;
	public JComboBox doubleTicketBox2;
	public JButton goButton;
	public JButton saveButton;
	
	public RightSideView() {
	
		Box box = Box.createVerticalBox();
		box.add(roundBox());
		box.add(new JLabel("Single Move"));
		box.add(moveForm(singleMoveLabels(),singleMoveFields()));
		box.add(new JLabel("Double Move"));
		box.add(moveForm(doubleMoveLabels(),doubleMoveFields()));
		box.add(controlButtons());
		this.add(box);
	}

	Box singleMoveLabels() {
		JLabel move = new JLabel("Move to: ");
		JLabel ticket = new JLabel("Using ticket: ");
		Box box = Box.createVerticalBox();
		box.add(move);
		box.add(ticket); 
		return box;
	}

	Box singleMoveFields() {
		JComboBox<String> moves = new JComboBox<String>();
		JComboBox<String> tickets = new JComboBox<String>();
		singleMoveBox = moves;
		singleTicketBox = tickets;
		Box box = Box.createVerticalBox();
		box.add(moves);
		box.add(tickets);
		return box;
	}

	Box moveForm(Box labels, Box fields) {
		Box box = Box.createHorizontalBox();
		box.add(labels);
		box.add(fields);
		return box;
	}

	Box doubleMoveLabels() {
		Box box = Box.createVerticalBox();
		box.add(new JLabel("Move 1:"));
		box.add(new JLabel("Move to: "));
		box.add(new JLabel("Using ticket: "));
		box.add(new JLabel("Move 2:"));
		box.add(new JLabel("Move to: "));
		box.add(new JLabel("Using ticket: "));
		return box;
	}
	
	Box doubleMoveFields() {
		JComboBox<String> moves1 = new JComboBox<String>();
		JComboBox<String> tickets1 = new JComboBox<String>();
		JComboBox<String> moves2 = new JComboBox<String>();
		JComboBox<String> tickets2 = new JComboBox<String>();
		
		doubleMoveBox1 = moves1;
		doubleTicketBox1 = tickets1;
		doubleMoveBox2 = moves2;
		doubleTicketBox2 = tickets2;
		
		Box box = Box.createVerticalBox();
		box.add(moves1);
		box.add(tickets1);
		box.add(moves2);
		box.add(tickets2);
		
		return box;
	}



	Box roundBox() {
		Box box = Box.createHorizontalBox();
		box.add(new JLabel("Round: "));
		roundNumLabel = new JLabel();
		box.add(roundNumLabel);
		return box;
	}

	Box controlButtons() {
		Box box = Box.createHorizontalBox();
		saveButton = new JButton("Save Game");
		goButton = new JButton("Go");
		box.add(goButton);
		box.add(saveButton);
		return box;
	}

	public void addGoButtonListener(ActionListener listener) {
		goButton.addActionListener(listener);
	}

	public void addSaveButtonListener(ActionListener listener) {
		saveButton.addActionListener(listener);
	}
	
	public void setSingleTickets(java.util.Set<Ticket> tickets) {
		for(Ticket t:tickets) {
			singleTicketBox.addItem(t.toString());
		}
	}

	public void setRoundNum(int roundNum) {
		roundNumLabel.setText(Integer.toString(roundNum));
	}

	//may want to order these sets....
	public void setSingleMoves(java.util.Set<Integer> moves) {
		for(Integer move:moves) {
			singleMoveBox.addItem(move);
		}
		
	}
	
	public void setDoubleMoves1(java.util.Set<Integer> nodes) {
		for(Integer n:nodes) {
			doubleMoveBox1.addItem(n);
		}
	}

	public void setDoubleTickets1(java.util.Set<Ticket> tickets) {
		for(Ticket t:tickets) {
			doubleTicketBox1.addItem(t.toString());
		}
	}

	public void setDoubleMoves2(java.util.Set<Integer> nodes) {
		for(Integer n:nodes) {
			doubleMoveBox2.addItem(n);
		}
	}

	public void setDoubleTickets2(java.util.Set<Ticket> tickets) {
		for(Ticket t:tickets) {
			doubleTicketBox2.addItem(t.toString());
		}
	}

	public int getSingleMove() {
		int target = (int)singleMoveBox.getSelectedItem();
		return target;
	}

	public Ticket getSingleTicket() {
		String ticket = (String)singleTicketBox.getSelectedItem();
		switch(ticket) {
			case "Taxi":
				return Ticket.Taxi;
			case "Bus":
				return Ticket.Bus;
			case "Underground":
				return Ticket.Underground;
			case "SecretMove":
				return Ticket.SecretMove;
			default: return null;
		}
	}

	public int[] getDoubleMove() {
		int target1 = (int) doubleMoveBox1.getSelectedItem();
		int target2 = (int) doubleMoveBox2.getSelectedItem();
		int[] targets = new int[2];
		targets[0] = target1;
		targets[1] = target2;
		return targets;
	}
	//How can I use an array of Tickets? Shall we just use strings?
	/*public Ticket[] getDoubleTickets() {
		Ticket ticket1 = (Ticket) doubleTicketBox1.getSeletcedItem();
		Ticket ticket2 = (Ticket) doubleTicketBox2.getSelectedItem();
		Ticket tickets = new Ticket[2];
		tickets[0] = ticket1;
		tickets[1] = ticket2;
		return tickets;
	} 
	*/
}
