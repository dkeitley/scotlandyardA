package solution;
import scotlandyard.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.Set;

public class RightSideView extends JPanel{

	public JLabel roundNumLabel;
	private JLabel move2;
	private JLabel moveto;
	private JLabel usingTicket;
	public JLabel move1;
	public JLabel moveto1;
	public JLabel usingTicket1;
	public JComboBox firstMoveLocationsBox;
	public JComboBox secondMoveLocationsBox;
	public JComboBox firstMoveTicketBox;
	public JComboBox secondMoveTicketBox;
	public JButton doubleMoveButton;
	public JButton goButton;
	public JButton saveButton;
	public JButton clearButton;
	public JLabel currentPlayer;
	public JButton passButton;
	
	public RightSideView() {
	
		Box box = Box.createVerticalBox();
		box.add(roundBox());
		currentPlayer = new JLabel("Current Player: Black");
		box.add(currentPlayer);
		box.add(moveForm(moveLabels(),moveFields()));
		box.add(controlButtons());
		passButton = new JButton("Pass");
		box.add(passButton);
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
		move2 = new JLabel("Move 2:");
		moveto = new JLabel("Move to: ");
		usingTicket = new JLabel("Using ticket: ");
		
		move1 = new JLabel("Move 1:");
		moveto1 = new JLabel("Move to: ");
		usingTicket1 = new JLabel("Using ticket: ");
		
		Box box = Box.createVerticalBox();
		
		box.add(move1);
		box.add(moveto1);
		box.add(usingTicket1);
		box.add(move2);
		box.add(moveto);
		box.add(usingTicket);
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
		move2.setVisible(true);
		moveto.setVisible(true);
		usingTicket.setVisible(true);
		
	}

	public void hideSecondMove() {
		secondMoveLocationsBox.setVisible(false);
		secondMoveTicketBox.setVisible(false);
		move2.setVisible(false);
		moveto.setVisible(false);
		usingTicket.setVisible(false);
	}
	
	public void hideMoveControls() {
		hideSecondMove();
		goButton.setVisible(false);
		clearButton.setVisible(false);
		saveButton.setVisible(false);
		doubleMoveButton.setVisible(false);
		firstMoveLocationsBox.setVisible(false);
		firstMoveTicketBox.setVisible(false);
		move1.setVisible(false);
		moveto1.setVisible(false);
		usingTicket1.setVisible(false);
		
	}
	
	public void displayMoveControls() {
		displaySecondMove();
		goButton.setVisible(true);
		clearButton.setVisible(true);
		saveButton.setVisible(true);
		doubleMoveButton.setVisible(true);
		firstMoveLocationsBox.setVisible(true);
		firstMoveTicketBox.setVisible(true);
		move1.setVisible(true);
		moveto1.setVisible(true);
		usingTicket1.setVisible(true);
		
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

	public void addPassButtonListener(ActionListener listener) {
		passButton.addActionListener(listener);
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
		int target = (int)firstMoveLocationsBox.getSelectedItem();
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
