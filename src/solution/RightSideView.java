package solution;
import scotlandyard.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.Set;

public class RightSideView extends JPanel{

	public JLabel roundNumLabel;
	public JComboBox firstMoveLocationsBox;
	public JComboBox secondMoveLocationsBox;
	public JComboBox firstMoveTicketBox;
	public JComboBox secondMoveTicketBox;
	public JButton doubleMoveButton;
	public JButton goButton;
	public JButton saveButton;
	public JButton clearButton;
	
	public RightSideView() {
	
		Box box = Box.createVerticalBox();
		box.add(roundBox());
		box.add(moveForm(moveLabels(),moveFields()));
		box.add(controlButtons());
		this.add(box);
	}

	Box moveForm(Box labels, Box fields) {
		Box box = Box.createHorizontalBox();
		box.add(labels);
		box.add(fields);
		return box;
	}
	//will need to change this
	Box moveLabels() {
		Box box = Box.createVerticalBox();
		box.add(new JLabel("Move 1:"));
		box.add(new JLabel("Move to: "));
		box.add(new JLabel("Using ticket: "));
		box.add(new JLabel("Move 2:"));
		box.add(new JLabel("Move to: "));
		box.add(new JLabel("Using ticket: "));
		return box;
	}
	
	Box moveFields() {
		JComboBox<String> moves1 = new JComboBox<String>();
		JComboBox<String> tickets1 = new JComboBox<String>();
		JComboBox<String> moves2 = new JComboBox<String>();
		JComboBox<String> tickets2 = new JComboBox<String>();
		doubleMoveButton = new JButton("Use Double Move");
		
		firstMoveLocationsBox = moves1;
		firstMoveTicketBox = tickets1;
		secondMoveLocationsBox = moves2;
		secondMoveTicketBox = tickets2;
		
		Box box = Box.createVerticalBox();
		box.add(moves1);
		box.add(tickets1);
		box.add(doubleMoveButton);
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
		clearButton = new JButton("Clear");
		box.add(goButton);
		box.add(saveButton);
		box.add(clearButton);
		return box;
	}

	public void displaySecondMove() {
		secondMoveLocationsBox.setVisible(true);
		secondMoveTicketBox.setVisible(true);
	}

	public void hideSecondMove() {
		secondMoveLocationsBox.setVisible(false);
		secondMoveTicketBox.setVisible(false);
	}
	
	public void addGoButtonListener(ActionListener listener) {
		goButton.addActionListener(listener);
	}

	public void addClearButtonListener(ActionListener listener) {
		clearButton.addActionListener(listener);
	}
	
	public void addDoubleMoveButtonListener(ActionListener listener) {
		doubleMoveButton.addActionListener(listener);
	}

	public void addFirstMoveLocationsListener(ItemListener listener) {
		firstMoveLocationsBox.addItemListener(listener);
	}

	public void addSecondMoveLocationsListener(ItemListener listener) {
		secondMoveLocationsBox.addItemListener(listener);
	}

	public void addSaveButtonListener(ActionListener listener) {
		saveButton.addActionListener(listener);
	}
	
	public void setFirstMoveTickets(Set<Ticket> tickets) {
		for(Ticket t:tickets) {
			firstMoveTicketBox.addItem(t.toString());
		}
	}

	public void setRoundNum(int roundNum) {
		roundNumLabel.setText(Integer.toString(roundNum));
	}

	//may want to order these sets....
	public void setFirstMoveLocations(Set<Integer> moves) {
		for(Integer move:moves) {
			firstMoveLocationsBox.addItem(move);
		}
		
	}

	public void setSecondMoveLocations(Set<Integer> locations) {
		for(Integer n:locations) {
			secondMoveLocationsBox.addItem(n);
		}
	}

	public void setSecondMoveTickets(Set<Ticket> tickets) {
		for(Ticket t:tickets) {
			secondMoveTicketBox.addItem(t.toString());
		}
	}

	public int getFirstLocation() {
		System.out.println("jjhdlkj");
		int target = (int)firstMoveLocationsBox.getSelectedItem();
		System.out.println("ddfdsdfgfthhyuiyjgfdsa"+target);
		return target;
	}

	public Ticket getFirstTicket() {
		String ticket = (String)firstMoveTicketBox.getSelectedItem();
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

	public int getSecondLocation() {
		return (int) secondMoveLocationsBox.getSelectedItem();
	}

	public Ticket getSecondTicket() {
		String ticket = (String)secondMoveTicketBox.getSelectedItem();
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

}
