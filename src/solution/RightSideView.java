package solution;

import scotlandyard.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.Set;
import javax.swing.border.*;

public class RightSideView extends JPanel{

	protected JLabel roundNumLabel;
	private Box labels;
	private JPanel firstMove;
	protected JPanel secondMove;
	private JPanel buttons;
	private JLabel showRoundsLabel;
	protected JComboBox firstMoveLocationsBox;
	protected JComboBox secondMoveLocationsBox;
	protected JComboBox firstMoveTicketBox;
	protected JComboBox secondMoveTicketBox;
	protected JButton doubleMoveButton;
	protected JButton goButton;
	protected JButton saveButton;
	protected JButton clearButton;
	protected JLabel currentPlayer;
	protected JButton passButton;
	
	//adds all components to panel
	public RightSideView() {
	
		firstMove = firstMove();
		secondMove = secondMove();
		passButton = new JButton("Pass");
		labels = labels();
		buttons = buttons();
		Box labelBox = Box.createHorizontalBox();
		labelBox.add(labels);
		Box box = Box.createVerticalBox();
		box.setAlignmentX(LEFT_ALIGNMENT);
		box.add(labelBox);
		box.add(firstMove);
		box.add(secondMove);
		box.add(buttons);
		box.add(passButton);
		this.add(box);
	}

	//@return box, JPanel containing first move controls
	JPanel firstMove() {
		GridLayout grid = new GridLayout(3,2,10,10);
		Border border = BorderFactory.createEmptyBorder(10,10,10,10);
		firstMoveLocationsBox = new JComboBox<String>();
		firstMoveTicketBox = new JComboBox<String>();
		doubleMoveButton = new JButton("Use Double Move");
		JPanel box = new JPanel();
		box.setLayout(grid);
		box.setBorder(border);
		box.add(new JLabel("Move to: "));
		box.add(firstMoveLocationsBox);
		box.add(new JLabel("Using ticket: "));
		box.add(firstMoveTicketBox);
		box.add(doubleMoveButton);
		return box;
	}

	//@return box, JPanel containing second move controls
	JPanel secondMove() {
		GridLayout grid = new GridLayout(3,1,5,5);
		Border border = BorderFactory.createEmptyBorder(5,5,5,5);
		JPanel box = new JPanel();
		box.setLayout(grid);
		box.setBorder(border);
		secondMoveLocationsBox = new JComboBox<String>();
		secondMoveTicketBox = new JComboBox<String>();
		box.add(new JLabel("Move to: "));
		box.add(secondMoveLocationsBox);
		box.add(new JLabel("Using ticket: "));
		box.add(secondMoveTicketBox);
		return box;
	}	


	//@return labels, box containing game status labels
	Box labels() {
		Box labels = Box.createVerticalBox();
		roundNumLabel = new JLabel("Round: ");
		showRoundsLabel = new JLabel("Show rounds: ");
		currentPlayer = new JLabel("Current Player: Black");
		labels.add(roundNumLabel);
		labels.add(showRoundsLabel);
		labels.add(currentPlayer);
		return labels;
	}

	//@return buttons, JPanel containing action buttons
	JPanel buttons() {
		saveButton = new JButton("Save Game");
		goButton = new JButton("Go");
		clearButton = new JButton("Clear");
		GridLayout grid = new GridLayout(3,1,10,10);
		Border border = BorderFactory.createEmptyBorder(10,10,10,10);
		JPanel buttons = new JPanel();
		buttons.setLayout(grid);
		buttons.setBorder(border);
		buttons.add(goButton);
		buttons.add(saveButton);
		buttons.add(clearButton);
		return buttons;
	}

	public void displaySecondMove() {
		secondMove.setVisible(true);
	}

	public void hideSecondMove() {
		secondMove.setVisible(false);
	}
	
	public void hideMoveControls() {
		buttons.setVisible(false);
		firstMove.setVisible(false);
		passButton.setVisible(true);
	}
	
	public void displayMoveControls() {
		displaySecondMove();
		buttons.setVisible(true);
		firstMove.setVisible(true);
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
		roundNumLabel.setText("Round: " + Integer.toString(roundNum));
	}

	public void setShowRounds(String text) {
		showRoundsLabel.setText(text);
	}
	
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

